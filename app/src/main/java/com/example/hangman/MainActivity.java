package com.example.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to navigate to Shake Egg Activity
        Button btnToShakeEgg = findViewById(R.id.btnToShakeEgg);
        btnToShakeEgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShakeEggActivity.class);
                startActivity(intent);
            }
        });
    }

    public void startTimedMode(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("isTimed", true); // Pass timed mode flag
        startActivity(intent);
    }

    public void startNonTimedMode(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("isTimed", false); // Pass non-timed mode flag
        startActivity(intent);
    }

    public void viewScores(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }
}
