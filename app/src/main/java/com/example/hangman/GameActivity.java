package com.example.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private TextView wordView, timerView, scoreView, guessedLettersView;
    private EditText inputLetter;
    private Button btnGuess;

    private String wordToGuess;
    private char[] displayedWord;
    private int score = 0;
    private boolean isTimed;
    private int timerDuration = 30000; // 30 seconds
    private CountDownTimer timer;
    private ArrayList<String> wordList;
    private HashSet<Character> guessedLetters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Link UI elements
        wordView = findViewById(R.id.wordView);
        timerView = findViewById(R.id.timerView);
        scoreView = findViewById(R.id.scoreView);
        guessedLettersView = findViewById(R.id.guessedLettersView);
        inputLetter = findViewById(R.id.inputLetter);
        btnGuess = findViewById(R.id.btnGuess);

        // Determine game mode
        isTimed = getIntent().getBooleanExtra("isTimed", false);

        // Initialize word list and guessed letters
        initializeWordList();
        guessedLetters = new HashSet<>();
        startNewGame();

        // Timer setup
        if (isTimed) {
            startTimer();
        } else {
            timerView.setVisibility(View.GONE); // Hide timer in non-timed mode
        }

        // Guess button listener
        btnGuess.setOnClickListener(v -> processGuess());
    }

    private void initializeWordList() {
        wordList = new ArrayList<>();
        wordList.add("APPLE");
        wordList.add("BANANA");
        wordList.add("ORANGE");
        wordList.add("CHERRY");
        wordList.add("GRAPE");
    }

    private void startNewGame() {
        // Pick a random word
        Random random = new Random();
        wordToGuess = wordList.get(random.nextInt(wordList.size()));
        displayedWord = new char[wordToGuess.length()];
        for (int i = 0; i < displayedWord.length; i++) {
            displayedWord[i] = '_'; // Initialize with underscores
        }
        guessedLetters.clear(); // Clear guessed letters for the new game
        updateDisplayedWord();
        updateGuessedLetters();
    }

    private void updateDisplayedWord() {
        wordView.setText(new String(displayedWord));
    }

    private void updateGuessedLetters() {
        guessedLettersView.setText("Guessed: " + guessedLetters.toString());
    }

    private void processGuess() {
        String guess = inputLetter.getText().toString().toUpperCase();
        inputLetter.setText(""); // Clear the input field

        if (guess.isEmpty() || guess.length() != 1) {
            Toast.makeText(this, "Enter a single letter", Toast.LENGTH_SHORT).show();
            return;
        }

        char guessedLetter = guess.charAt(0);
        if (guessedLetters.contains(guessedLetter)) {
            Toast.makeText(this, "You already guessed that letter", Toast.LENGTH_SHORT).show();
            return;
        }

        guessedLetters.add(guessedLetter);
        boolean correct = false;

        // Check if the guessed letter is in the word
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == guessedLetter && displayedWord[i] == '_') {
                displayedWord[i] = guessedLetter;
                correct = true;
            }
        }

        if (correct) {
            updateDisplayedWord();
            if (new String(displayedWord).equals(wordToGuess)) {
                score++;
                Toast.makeText(this, "You guessed the word!", Toast.LENGTH_SHORT).show();
                startNewGame(); // Start a new game
                if (isTimed) startTimer(); // Restart timer if in timed mode
            }
        } else {
            Toast.makeText(this, "Incorrect guess!", Toast.LENGTH_SHORT).show();
        }

        scoreView.setText("Score: " + score);
        updateGuessedLetters();
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel(); // Cancel any existing timer
        }

        timer = new CountDownTimer(timerDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerView.setText("Time: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                endGame();
            }
        };

        timer.start();
    }

    private void endGame() {
        if (timer != null) {
            timer.cancel();
        }

        // Save the score in SharedPreferences
        SharedPreferences prefs = getSharedPreferences("HangmanScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("lastScore", score);
        editor.apply();

        // Go to the ScoreActivity
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
        finish();
    }
}
