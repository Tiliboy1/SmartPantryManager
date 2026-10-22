package com.example.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** Pantry List screen: shows every ingredient stored in the database. */
public class MainActivity extends AppCompatActivity
        implements PantryAdapter.OnItemClickListener {

    private PantryAdapter adapter;
    private TextView tvEmpty;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);
        RecipeSeeder.seedIfEmpty(db); // loads the recipes on first run only
        tvEmpty = findViewById(R.id.tvEmpty);

        RecyclerView recyclerView = findViewById(R.id.recyclerPantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditActivity.class)));

        BottomNavHelper.setup(this, BottomNavHelper.NAV_PANTRY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        List<PantryItem> items = db.pantryItemDao().getAllItems();
        adapter.setItems(items);
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        showExpiryAlert(items);
    }

    /** Uses the Settings toggle: warns about items expiring within 3 days. */
    private void showExpiryAlert(List<PantryItem> items) {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        if (!prefs.getBoolean("expiry_alerts", true)) return;
        int count = 0;
        for (PantryItem item : items) {
            if (isExpiringSoon(item.getExpiryDate())) count++;
        }
        if (count > 0) {
            Toast.makeText(this, count + " item(s) expiring within 3 days",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isExpiringSoon(String expiry) {
        if (expiry == null || expiry.isEmpty()) return false;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(expiry);
            long days = (date.getTime() - System.currentTimeMillis()) / (24L * 60 * 60 * 1000);
            return days <= 3;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public void onItemClick(PantryItem item) {
        Intent intent = new Intent(this, AddEditActivity.class);
        intent.putExtra(AddEditActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Remove " + item.getName() + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.pantryItemDao().delete(item);
                    Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
                    loadItems();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}