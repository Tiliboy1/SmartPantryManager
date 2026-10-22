package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** Suggested Recipes screen: only recipes the user can make right now. */
public class SuggestedActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecipeAdapter adapter;
    private TextView tvNoMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);

        db = AppDatabase.getInstance(this);
        tvNoMatch = findViewById(R.id.tvNoMatch);

        RecyclerView recyclerView = findViewById(R.id.recyclerSuggested);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(recipe -> {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        BottomNavHelper.setup(this, BottomNavHelper.NAV_SUGGESTED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestions(); // re-run the matching every time the screen is shown
    }

    private void loadSuggestions() {
        List<PantryItem> pantry = db.pantryItemDao().getAllItems();
        List<Recipe> suggested = new ArrayList<>();
        for (Recipe recipe : db.recipeDao().getAllRecipes()) {
            List<RecipeIngredient> needed = db.recipeDao().getIngredientsForRecipe(recipe.getId());
            if (RecipeMatcher.canMake(needed, pantry)) {
                suggested.add(recipe);
            }
        }
        adapter.setRecipes(suggested);
        tvNoMatch.setVisibility(suggested.isEmpty() ? View.VISIBLE : View.GONE);
    }
}