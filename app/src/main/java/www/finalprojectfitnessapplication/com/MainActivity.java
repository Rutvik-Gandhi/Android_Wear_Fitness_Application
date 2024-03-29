package www.finalprojectfitnessapplication.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import www.finalprojectfitnessapplication.com.HeartRateScanActivity;
import www.finalprojectfitnessapplication.com.PreviousDataActivity;
import www.finalprojectfitnessapplication.com.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Button to start heart rate scan
        binding.btnStartHeartRateScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HeartRateScanActivity.class));
            }
        });

        // Button to show previous data
        binding.btnShowPreviousData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PreviousDataActivity.class));
            }
        });

        // Button for sleep tracking (you can add functionality as needed)
        binding.btnSleepTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SleepPatternActivity.class));
            }
        });
    }
}
