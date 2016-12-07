package com.example.annie_pc.projectchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferencesUtils {

    private static final String PREFS_NAME = "ge.edu.freeuni.assignment4.PREFS";
    private static final String NOTIFICATIONS = "notifications";
    private static final String SOUNDS = "sounds";

    public synchronized static boolean getNotificationsState(Context context) {
        return getState(context, NOTIFICATIONS);
    }

    public synchronized static void saveNotificationsState(Context context, boolean state) {
        saveState(context, NOTIFICATIONS, state);
    }

    public synchronized static boolean getSoundsState(Context context) {
        return getState(context, SOUNDS);
    }

    public synchronized static void saveSoundsState(Context context, boolean state) {
        saveState(context, SOUNDS, state);
    }

    private synchronized static void saveState(Context context, String name, boolean state) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(name, state);
        editor.apply();
    }

    private synchronized static boolean getState(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(name, getDefaultValue(name));
    }

    private synchronized static boolean getDefaultValue(String name) {
        return name.equals(NOTIFICATIONS);
    }

}
