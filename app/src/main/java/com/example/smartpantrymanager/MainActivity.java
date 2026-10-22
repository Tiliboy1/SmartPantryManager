package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Pantry List screen: shows every ingredient stored in the database.
 */
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
        tvEmpty = findViewById(R.id.tvEmpty);

        RecyclerView recyclerView = findViewById(R.id.recyclerPantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            // TEMPORARY test: adds a sample item. Replaced by the Add screen in Step 5.
            db.pantryItemDao().insert(new PantryItem("Tomato", 2, "pcs", ""));
            loadItems();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems(); // refresh the list every time the screen comes back
    }

    private void loadItems() {
        List<PantryItem> items = db.pantryItemDao().getAllItems();
        adapter.setItems(items);
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(PantryItem item) {
        Toast.makeText(this, "Edit " + item.getName() + " (coming in Step 5)",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(PantryItem item) {
        Toast.makeText(this, "Delete " + item.getName() + " (coming in Step 5)",
                Toast.LENGTH_SHORT).show();
    }
}