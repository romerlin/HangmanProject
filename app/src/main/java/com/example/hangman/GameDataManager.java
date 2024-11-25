package com.example.hangman;

import android.content.Context;
import android.content.SharedPreferences;

public class GameDataManager {
    private static final String PREFS_NAME = "GameData";
    private static final String KEY_COINS = "coins";
    private static final String KEY_TICKETS = "tickets";

    private SharedPreferences sharedPreferences;

    public GameDataManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public int getCoins() {
        return sharedPreferences.getInt(KEY_COINS, 0);
    }

    public int getTickets() {
        return sharedPreferences.getInt(KEY_TICKETS, 0);
    }

    public void setCoins(int coins) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_COINS, coins);
        editor.apply();
    }

    public void setTickets(int tickets) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_TICKETS, tickets);
        editor.apply();
    }

    public void updateCoinsAndTickets(int coins, int tickets) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_COINS, coins);
        editor.putInt(KEY_TICKETS, tickets);
        editor.apply();
    }
}
