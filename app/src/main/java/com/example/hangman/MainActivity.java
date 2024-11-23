package com.example.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTimedMode(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("isTimed", true);
        startActivity(intent);
    }

    public void startNonTimedMode(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("isTimed", false);
        startActivity(intent);
    }

    public void viewScores(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }
}
