package com.example.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.io.IOException;
import java.io.InputStream;

public class GameActivity extends AppCompatActivity {
    private TextView wordView, timerView, scoreView, guessedLettersView, coinView, ticketView;
    private EditText inputLetter;
    private Button btnGuess, btnHint;

    private String wordToGuess;
    private char[] displayedWord;
    private int score = 0;
    private int coins = 0;
    private int tickets = 0;
    private boolean isTimed;
    private int timerDuration = 30000; // 30 seconds
    private CountDownTimer timer;
    private ArrayList<String> wordList;
    private HashSet<Character> guessedLetters;

    private GameDataManager gameDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize GameDataManager
        gameDataManager = new GameDataManager(this);

        // Load saved values
        coins = gameDataManager.getCoins();
        tickets = gameDataManager.getTickets();

        // Initialize UI elements
        wordView = findViewById(R.id.wordView);
        timerView = findViewById(R.id.timerView);
        scoreView = findViewById(R.id.scoreView);
        guessedLettersView = findViewById(R.id.guessedLettersView);
        coinView = findViewById(R.id.coinView);
        ticketView = findViewById(R.id.ticketView);
        inputLetter = findViewById(R.id.inputLetter);
        btnGuess = findViewById(R.id.btnGuess);
        btnHint = findViewById(R.id.btnHint);

        // Button listeners
        btnGuess.setOnClickListener(v -> processGuess());
        btnHint.setOnClickListener(v -> useHint());

        // Determine game mode
        isTimed = getIntent().getBooleanExtra("isTimed", false);

        // Timer setup
        if (isTimed) {
            startTimer();
        } else {
            timerView.setVisibility(View.GONE); // Hide timer in non-timed mode
        }


        // Initialize word list and guessed letters
        initializeWordList();
        guessedLetters = new HashSet<>();
        startNewGame();

        // Back to Main Button
        ImageButton btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
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

    private void initializeWordList() {
        wordList = new ArrayList<>(); // Initialize the word list
        try {
            // Open the text file from res/raw
            InputStream inputStream = getResources().openRawResource(R.raw.wordlist); // File name without extension
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line.trim().toUpperCase()); // Add each word, converting to uppercase
            }

            reader.close(); // Close the reader
            inputStream.close(); // Close the InputStream
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading word list!", Toast.LENGTH_SHORT).show();
        }

        if (wordList.isEmpty()) {
            // Handle case where the word list is empty
            Toast.makeText(this, "Word list is empty. Please check your file.", Toast.LENGTH_LONG).show();
            finish(); // Exit the activity
        }
    }




    private void updateUI() {
        wordView.setText(new String(displayedWord));
        guessedLettersView.setText("Guessed: " + guessedLetters.toString());
        scoreView.setText("Score: " + score);
        coinView.setText("Coins: " + coins);
        ticketView.setText("Tickets: " + tickets);

        gameDataManager.updateCoinsAndTickets(coins, tickets);
    }

    private void processGuess() {
        String guess = inputLetter.getText().toString().toUpperCase();
        inputLetter.setText("");

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

        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == guessedLetter) {
                displayedWord[i] = guessedLetter;
                correct = true;
            }
        }

        updateDisplayedWord();
        updateUI();

        if (correct) {
            if (new String(displayedWord).equals(wordToGuess)) {
                score++;
                coins += 3; // เพิ่ม 3 coins ต่อการเดาถูกทั้งคำ
                Toast.makeText(this, "You guessed the word!", Toast.LENGTH_SHORT).show();
                startNewGame();
            }
        } else {
            Toast.makeText(this, "Incorrect guess!", Toast.LENGTH_SHORT).show();
        }
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

        // Update the displayed word and guessed letters
        updateDisplayedWord();
        updateGuessedLetters();
    }

    private void updateGuessedLetters() {
        guessedLettersView.setText("Guessed: " + guessedLetters.toString());
    }


    private void updateDisplayedWord() {
        StringBuilder formattedWord = new StringBuilder();
        for (char letter : displayedWord) {
            formattedWord.append(letter).append(" "); // Add a space after each character
        }
        wordView.setText(formattedWord.toString().trim()); // Set the formatted string and remove the trailing space
    }





    private void useHint() {
        if (tickets <= 0) {
            Toast.makeText(this, "Not enough tickets for a hint!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < wordToGuess.length(); i++) {
            if (displayedWord[i] == '_') {
                displayedWord[i] = wordToGuess.charAt(i);
                tickets--;
                updateUI();
                return;
            }
        }
    }


}
