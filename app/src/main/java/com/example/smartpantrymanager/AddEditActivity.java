package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

/**
 * Add/Edit Ingredient screen. If an item id is passed in the Intent,
 * the form is pre-filled and saving UPDATES that item; otherwise it CREATES one.
 */
public class AddEditActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";
    private static final String[] UNITS = {"pcs", "g", "kg", "ml", "l", "cup", "tbsp", "tsp"};

    private EditText etName, etQuantity, etExpiry;
    private Spinner spinnerUnit;
    private AppDatabase db;
    private PantryItem existingItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        db = AppDatabase.getInstance(this);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        Button btnSave = findViewById(R.id.btnSave);
        TextView tvFormTitle = findViewById(R.id.tvFormTitle);

        spinnerUnit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, UNITS));

        // Edit mode: an item id was sent through the Intent
        int itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            existingItem = db.pantryItemDao().getItemById(itemId);
            if (existingItem != null) {
                tvFormTitle.setText("Edit Ingredient");
                etName.setText(existingItem.getName());
                etQuantity.setText(String.valueOf(existingItem.getQuantity()));
                etExpiry.setText(existingItem.getExpiryDate());
                for (int i = 0; i < UNITS.length; i++) {
                    if (UNITS[i].equals(existingItem.getUnit())) {
                        spinnerUnit.setSelection(i);
                    }
                }
            }
        }

        etExpiry.setOnClickListener(v -> showDatePicker());
        etExpiry.setOnLongClickListener(v -> { etExpiry.setText(""); return true; }); // long-press clears
        btnSave.setOnClickListener(v -> saveItem());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) ->
                etExpiry.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveItem() {
        String name = etName.getText().toString().trim();
        String qtyText = etQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = etExpiry.getText().toString().trim();

        // Input validation
        if (name.isEmpty()) {
            etName.setError("Please enter an ingredient name");
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(qtyText);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            return;
        }
        if (quantity <= 0) {
            etQuantity.setError("Quantity must be greater than 0");
            return;
        }

        if (existingItem == null) {
            db.pantryItemDao().insert(new PantryItem(name, quantity, unit, expiry)); // Create
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            existingItem.setName(name);
            existingItem.setQuantity(quantity);
            existingItem.setUnit(unit);
            existingItem.setExpiryDate(expiry);
            db.pantryItemDao().update(existingItem); // Update
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish(); // back to the Pantry List
    }
}