package com.example.smartpantrymanager.util;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.smartpantrymanager.MainActivity;
import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.SuggestedRecipesActivity;

/** Wires up the bottom navigation bar on each main screen using Intents. */
public final class NavHelper {

    private NavHelper() { }

    public static void setup(Activity activity, BottomNavigationView nav, int currentId) {
        nav.setSelectedItemId(currentId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == currentId) return true;

            Class<?> target;
            if (id == R.id.nav_pantry) {
                target = MainActivity.class;
            } else if (id == R.id.nav_suggested) {
                target = SuggestedRecipesActivity.class;
            } else {
                Toast.makeText(activity, "Settings arrives in Stage 9", Toast.LENGTH_SHORT).show();
                return false;
            }

            Intent intent = new Intent(activity, target);
            // Reuse the existing screen instead of stacking a new copy each tap
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            return false;   // false = keep this screen's own tab highlighted
        });
    }
}