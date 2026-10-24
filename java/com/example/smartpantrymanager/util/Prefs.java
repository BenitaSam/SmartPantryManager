package com.example.smartpantrymanager.util;

import android.content.Context;
import android.content.SharedPreferences;

/**stores the Settings screen choices on the phone tru SharedPreferences*/
public final class Prefs {

    private static final String FILE = "pantry_prefs";
    private static final String KEY_ALERTS = "expiry_alerts";
    private static final String KEY_UNIT = "default_unit";

    private Prefs() { }

    private static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    public static boolean isExpiryAlertsOn(Context c) {
        return sp(c).getBoolean(KEY_ALERTS, true);
    }

    public static void setExpiryAlerts(Context c, boolean on) {
        sp(c).edit().putBoolean(KEY_ALERTS, on).apply();
    }

    public static String getDefaultUnit(Context c) {
        return sp(c).getString(KEY_UNIT, "pcs");
    }

    public static void setDefaultUnit(Context c, String unit) {
        sp(c).edit().putString(KEY_UNIT, unit).apply();
    }
}