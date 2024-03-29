package www.finalprojectfitnessapplication.com;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import www.finalprojectfitnessapplication.com.databinding.SleepPatternActivityBinding;

public class SleepPatternActivity extends Activity implements SensorEventListener {

    private SleepPatternActivityBinding binding;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isSleeping = false;
    private Handler handler;
    private int sleepCycle = 0;
    private int totalSleepCycles = 0;
    private int awakeCount = 0;
    private boolean isSleepPatternRunning = false; // To track if sleep pattern is running

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = SleepPatternActivityBinding.inflate(LayoutInflater.from(this));
        View view = binding.getRoot();
        setContentView(view);

        // Initialize UI elements
        TextView tvSleepInfo = binding.tvSleepInfo;
        Button btnStartStopSleepPattern = binding.btnStartSleepPattern;

        // Initialize SensorManager and accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // Initialize Handler for periodic updates
        handler = new Handler();

        // Set click listener for the start/stop button
        btnStartStopSleepPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSleepPatternRunning) {
                    stopSleepPattern();
                } else {
                    startSleepPattern();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register the accelerometer sensor listener
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // Start periodic sleep pattern updates
        handler.postDelayed(updateSleepPattern, 1000); // Update every 1 second
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the accelerometer sensor listener
        if (accelerometer != null) {
            sensorManager.unregisterListener(this);
        }

        // Stop periodic updates
        handler.removeCallbacks(updateSleepPattern);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for accelerometer
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Detect wrist movement (actigraphy)
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double acceleration = Math.sqrt(x * x + y * y + z * z);
        Log.d("Acceleration", "onSensorChanged: " + acceleration);
        if (acceleration < 10.0) {
            // If there's minimal movement, consider it as sleep
            isSleeping = true;
        } else {
            // If there's movement, consider it as awake
            isSleeping = false;
        }
    }

    private Runnable updateSleepPattern = new Runnable() {
        @Override
        public void run() {
            // Update sleep pattern information
            if (isSleeping) {
                sleepCycle++;
                totalSleepCycles++;
                binding.tvSleepInfo.setText("Sleep Cycle: " + sleepCycle);
            } else {
                awakeCount++;
                binding.tvSleepInfo.setText("Awake");
            }

            // Schedule the next update
            handler.postDelayed(this, 1000); // Update every 1 second
        }
    };

    private double calculateSleepQuality() {
        // Calculate total duration of sleep in hours
        double totalSleepHours = totalSleepCycles / 3600.0; // Assuming each sleep cycle is 1 second

        double idealSleepHours = 8.0; // Ideal sleep hour to consider good sleep

        // Calculate the ratio of total sleep duration to ideal sleep duration, considering awake count
        double sleepQualityRatio = totalSleepHours / idealSleepHours;

        // Penalize for awake count, assuming each awake count reduces sleep quality by a certain factor
        double penaltyFactor = 0.1;
        double penalizedSleepQualityRatio = sleepQualityRatio - (awakeCount * penaltyFactor);

        // Ensure the sleep quality ratio is within bounds (0 to 1)
        penalizedSleepQualityRatio = Math.max(0.0, Math.min(1.0, penalizedSleepQualityRatio));

        // Convert the ratio to percentage
        return penalizedSleepQualityRatio * 100.0;
    }

    private void startSleepPattern() {
        // Reset sleep cycle and other variables
        sleepCycle = 0;
        totalSleepCycles = 0;
        awakeCount = 0;

        // Start periodic sleep pattern updates
        handler.postDelayed(updateSleepPattern, 1000); // Update every 1 second

        // Update button text
        binding.btnStartSleepPattern.setText("Stop Sleep Pattern");
        isSleepPatternRunning = true;
    }

    private void stopSleepPattern() {
        // Stop periodic updates
        handler.removeCallbacks(updateSleepPattern);

        // Analyze and display sleep quality
        analyzeSleepQuality();

        // Update button text
        binding.btnStartSleepPattern.setText("Start Sleep Pattern");
        isSleepPatternRunning = false;
    }

    private String describeSleepQuality(double sleepQuality) {
        if (sleepQuality >= 90) {
            return "Excellent";
        } else if (sleepQuality >= 70) {
            return "Good";
        } else if (sleepQuality >= 50) {
            return "Fair";
        } else if (sleepQuality >= 30) {
            return "Poor";
        } else {
            return "Very Poor";
        }
    }

    private void analyzeSleepQuality() {
        // Calculate sleep quality
        double sleepQuality = calculateSleepQuality();

        // Format sleep quality to display only the last two digits after the decimal
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedSleepQuality = decimalFormat.format(sleepQuality);

        // Describe sleep quality in words
        String sleepQualityDescription = describeSleepQuality(sleepQuality);

        // Display the sleep quality to the user
        String sleepQualityText = "Sleep Quality: " + formattedSleepQuality + "%\n" +
                "Description: " + sleepQualityDescription;
        binding.tvSleepInfo.setText(sleepQualityText);
    }
}
