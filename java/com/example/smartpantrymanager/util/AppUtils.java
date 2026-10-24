package com.example.smartpantrymanager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**for formatting numbers/text*/
public final class AppUtils {

    private AppUtils() { }

    /**6.0 -> "6", 0.5 -> "0.5"*/
    public static String formatQty(double q) {
        if (q == Math.rint(q)) return String.valueOf((long) q);
        return String.valueOf(q);
    }

    /**"egg" -> "Egg"*/
    public static String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
    }

    /**true only for dates written in YYYY-MM-DD format*/
    public static boolean isValidDate(String s) {
        if (s == null || !s.matches("\\d{4}-\\d{2}-\\d{2}")) return false;
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            f.setLenient(false);
            f.parse(s);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**days from today until the date*/
     /** negative=already expired*/
    /**large number if the date is bad. */
    public static long daysUntil(String s) {
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date target = f.parse(s);
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            return Math.round((target.getTime() - today.getTimeInMillis()) / 86400000.0);
        } catch (Exception e) {
            return Long.MAX_VALUE;
        }
    }
}
