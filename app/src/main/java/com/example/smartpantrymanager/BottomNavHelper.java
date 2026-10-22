package com.example.smartpantrymanager;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/** Sets up the bottom navigation bar and moves between screens using Intents. */
public class BottomNavHelper {

    public static final int NAV_PANTRY = 1;
    public static final int NAV_SUGGESTED = 2;
    public static final int NAV_SETTINGS = 3;

    public static void setup(Activity activity, int selectedId) {
        BottomNavigationView nav = activity.findViewById(R.id.bottomNav);
        Menu menu = nav.getMenu();
        menu.add(Menu.NONE, NAV_PANTRY, 0, "Pantry").setIcon(android.R.drawable.ic_menu_agenda);
        menu.add(Menu.NONE, NAV_SUGGESTED, 1, "Suggested").setIcon(android.R.drawable.ic_menu_search);
        menu.add(Menu.NONE, NAV_SETTINGS, 2, "Settings").setIcon(android.R.drawable.ic_menu_preferences);
        nav.setSelectedItemId(selectedId);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedId) return true;
            Class<?> target;
            if (id == NAV_PANTRY) target = MainActivity.class;
            else if (id == NAV_SUGGESTED) target = SuggestedActivity.class;
            else target = SettingsActivity.class;
            activity.startActivity(new Intent(activity, target));
            activity.overridePendingTransition(0, 0);
            activity.finish();
            return true;
        });
    }
}