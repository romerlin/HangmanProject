package com.example.hangman;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private TextView scoreView;
    private static final String Pref ="GameData";
    private static final String KEY_SCORE ="lastscore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        scoreView = findViewById(R.id.scoreView);

        SharedPreferences prefs = getSharedPreferences(Pref, MODE_PRIVATE);
        int lastScore = prefs.getInt(KEY_SCORE, 0);
        scoreView.setText("Last Score: " + lastScore);
    }

    public void goBackToMainMenu(View view) {
        finish();
    }

}
