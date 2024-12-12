package com.example.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

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

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            // Close the app if the back button is pressed again within 2 seconds
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            // Show a toast message
            backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
