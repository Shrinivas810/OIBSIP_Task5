package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean running;
    private long startTime;
    private long elapsedTime = 0L;
    private TextView timerTextView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timer_text_view);
        ImageButton startButton = findViewById(R.id.start_button);
        ImageButton stopButton = findViewById(R.id.stop_button);
        ImageButton resetButton = findViewById(R.id.reset_button);

        startButton.setOnClickListener(v -> onStartClicked());
        stopButton.setOnClickListener(v -> onStopClicked());
        resetButton.setOnClickListener(v -> onResetClicked());
    }

    private void onStartClicked() {
        if (!running) {
            // Start the stopwatch
            startTime = SystemClock.uptimeMillis() - elapsedTime;
            handler.post(updateTimer);
            running = true;
        }
    }

    private void onStopClicked() {
        if (running) {
            // Stop the stopwatch
            elapsedTime = SystemClock.uptimeMillis() - startTime;
            handler.removeCallbacks(updateTimer);
            running = false;
        }
    }

    private void onResetClicked() {
        running = false;
        elapsedTime = 0L;
        handler.removeCallbacks(updateTimer);
        updateTimerTextView();
    }

    private void updateTimerTextView() {
        int minutes = (int) (elapsedTime / 60000);
        int seconds = (int) ((elapsedTime % 60000) / 1000);
        int milliseconds = (int) (elapsedTime % 1000);

        timerTextView.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
    }

    private Runnable updateTimer = new Runnable() {
        public void run() {
            if (running) {
                elapsedTime = SystemClock.uptimeMillis() - startTime;
                updateTimerTextView();
                handler.postDelayed(this, 10); // Update the timer every 10 milliseconds
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (running) {
            handler.removeCallbacks(updateTimer);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (running) {
            startTime = SystemClock.uptimeMillis() - elapsedTime;
            handler.post(updateTimer);
        }
    }
}
