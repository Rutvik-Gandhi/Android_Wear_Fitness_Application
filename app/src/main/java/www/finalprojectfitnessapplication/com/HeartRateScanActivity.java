package www.finalprojectfitnessapplication.com;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import www.finalprojectfitnessapplication.com.databinding.ActivityHeartRateScanBinding;

public class HeartRateScanActivity extends Activity implements SensorEventListener {

    private static final String PREFS_NAME = "HeartRatePrefs";
    private static final String HEART_RATE_KEY = "heartRate";
    private static final String STRESS_LEVEL_KEY = "stressLevel";
    private ActivityHeartRateScanBinding binding;
    private static final int PERMISSIONS_REQUEST_BODY_SENSORS = 1;
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private int heartRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHeartRateScanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize UI elements
        TextView tvHeartRate = binding.tvHeartRate;
        TextView tvStressLevel = binding.tvStressLevel;
        Button btnSaveData = binding.btnSaveData;

        // Request permission for accessing sensors
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.BODY_SENSORS},
                PERMISSIONS_REQUEST_BODY_SENSORS
        );

        // Initialize the sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get the heart rate sensor and register the listener
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        if (heartRateSensor == null) {
            tvHeartRate.setText("Heart rate sensor not available");
        }

        // Perform heart rate scan when the button is clicked
        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performHeartRateScan();
                // Save the data to SharedPreferences
                saveData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the sensor listener
        if (heartRateSensor != null) {
            sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the heart rate sensor listener to save resources
        if (heartRateSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for heart rate sensor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Check if the sensor type is heart rate
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            // Get the heart rate value
            heartRate = (int) event.values[0];

            // Update the UI with the heart rate and stress level values
            binding.tvHeartRate.setText(getString(R.string.heart_rate) + heartRate);
            binding.tvStressLevel.setText(getString(R.string.stress_level) + getStressLevelInWords(heartRate));
        }
    }

    private void performHeartRateScan() {
        // Perform any additional logic needed during the heart rate scan
        // (e.g., animations, sound effects)
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Retrieve existing entries
        String previousHeartRate = sharedPreferences.getString(HEART_RATE_KEY, "");
        String previousStressLevel = sharedPreferences.getString(STRESS_LEVEL_KEY, "");

        // Create a new entry with the current data
        int currentHeartRate = heartRate;
        String currentStressLevel = getStressLevelInWords(heartRate);

        // Append the new data to the previous data (you can customize this logic based on your requirements)
        String updatedHeartRateData = previousHeartRate + ", " + currentHeartRate;
        String updatedStressLevelData = previousStressLevel + ", " + currentStressLevel;

        // Save the updated data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HEART_RATE_KEY, updatedHeartRateData);
        editor.putString(STRESS_LEVEL_KEY, updatedStressLevelData);
        editor.apply();
    }

    private String getStressLevelInWords(int heartRate) {
        if (heartRate < 70) {
            return "Relaxed";
        } else if (heartRate < 90) {
            return "Moderate Stress";
        } else {
            return "High Stress";
        }
    }
}
