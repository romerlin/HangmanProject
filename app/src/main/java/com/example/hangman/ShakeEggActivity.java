package com.example.hangman;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ShakeEggActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isShaking = false;
    private int coins;
    private int tickets;
    private TextView coinBalanceView, ticketBalanceView;
    private ImageView eggImageView;

    private GameDataManager gameDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_egg);

        gameDataManager = new GameDataManager(this);

        coins = gameDataManager.getCoins();
        tickets = gameDataManager.getTickets();

        coinBalanceView = findViewById(R.id.coinBalanceView);
        ticketBalanceView = findViewById(R.id.ticketBalanceView);
        eggImageView = findViewById(R.id.eggImageView);

        updateUI();

        ImageButton btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Are you sure to exit?")
                    .setNegativeButton("Yes", (dialog, which) -> finish()) // Finish the activity
                    .setPositiveButton("No", (dialog, which) -> dialog.dismiss()) // Dismiss the dialog
                    .setCancelable(false) // Prevent dismissing by tapping outside
                    .show();
        });


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        gameDataManager.updateCoinsAndTickets(coins, tickets);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isShaking) return;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float shakeMagnitude = (float) Math.sqrt(x * x + y * y + z * z);

        if (shakeMagnitude > 12) {
            isShaking = true;

            if (coins < 5) {
                Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show();
                isShaking = false;
                return;
            }

            coins -= 5;
            Random random = new Random();
            int earnedTickets = random.nextInt(5) + 1;
            tickets += earnedTickets;

            eggImageView.setImageResource(R.drawable.egg_cracked);
            new AlertDialog.Builder(this)
                    .setTitle("Congratulations!")
                    .setMessage("You earned " + earnedTickets + " tickets!")
                    .setPositiveButton("OK", (dialog, which) -> {
                        eggImageView.setImageResource(R.drawable.egg);
                        isShaking = false;
                        updateUI();
                    })
                    .show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void updateUI() {
        coinBalanceView.setText("Coins: " + coins);
        ticketBalanceView.setText("Tickets: " + tickets);
        gameDataManager.updateCoinsAndTickets(coins, tickets);
    }
}
