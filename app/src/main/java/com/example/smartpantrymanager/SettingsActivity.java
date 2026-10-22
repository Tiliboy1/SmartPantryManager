package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

/** Settings screen: toggle for expiring-soon alerts, saved in SharedPreferences. */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        SwitchCompat switchExpiry = findViewById(R.id.switchExpiry);
        switchExpiry.setChecked(prefs.getBoolean("expiry_alerts", true));
        switchExpiry.setOnCheckedChangeListener((button, checked) ->
                prefs.edit().putBoolean("expiry_alerts", checked).apply());

        BottomNavHelper.setup(this, BottomNavHelper.NAV_SETTINGS);
    }
}