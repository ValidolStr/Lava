package com.example.application02.tecnics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ResetReceiver extends BroadcastReceiver {

    private static final String PREFS_NAME = "AffirmationsPrefs";
    private static final String KEY_AFFIRMATION_USED = "affirmationUsed";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Reset the "used" flag for affirmation
        editor.putBoolean(KEY_AFFIRMATION_USED, false);
        editor.apply();
    }
}