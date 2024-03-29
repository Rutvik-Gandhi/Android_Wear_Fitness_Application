package www.finalprojectfitnessapplication.com;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PreviousDataActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "HeartRatePrefs";
    private static final String HEART_RATE_KEY = "heartRate";
    private static final String STRESS_LEVEL_KEY = "stressLevel";

    private TextView tvPreviousDataTitle;
    private ListView listPreviousData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_data);

        // Initialize UI elements
        tvPreviousDataTitle = findViewById(R.id.tvPreviousDataTitle);
        listPreviousData = findViewById(R.id.listPreviousData);

        // Retrieve and display previous heart rate and stress level data
        displayPreviousData();
    }

    private void displayPreviousData() {
        // Retrieve heart rate and stress level from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Retrieve the stored stress level data as a comma-separated string
        String stressLevelData = sharedPreferences.getString(STRESS_LEVEL_KEY, "");
        String[] stressLevels = stressLevelData.split(", "); // Split the string into an array

        // Retrieve the stored heart rate data
        String previousHeartRate = sharedPreferences.getString(HEART_RATE_KEY, "");
        String[] heartRates = previousHeartRate.split(", ");

        // Display previous heart rate and stress level data
        tvPreviousDataTitle.setText(getString(R.string.previous_heart_rate_data_title));

        List<String> data = new ArrayList<>();

        // Add heart rate and stress level data to the list
        for (int i = 1; i < heartRates.length; i++) {
            data.add(getString(R.string.heart_rate) + heartRates[i]+"\n"+getString(R.string.stress_level) + stressLevels[i]);
        }

        // Use an ArrayAdapter to display the data in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listPreviousData.setAdapter(adapter);
    }
}
