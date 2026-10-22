package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/** Recipe Detail screen: full ingredient list and method. */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        AppDatabase db = AppDatabase.getInstance(this);
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        Recipe recipe = db.recipeDao().getRecipeById(recipeId);
        if (recipe == null) {
            finish();
            return;
        }

        ((TextView) findViewById(R.id.tvRecipeTitle)).setText(recipe.getName());

        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient ing : db.recipeDao().getIngredientsForRecipe(recipe.getId())) {
            double q = ing.getQuantity();
            String qty = (q == Math.floor(q)) ? String.valueOf((int) q) : String.valueOf(q);
            sb.append("• ").append(qty).append(" ").append(ing.getUnit())
                    .append(" ").append(ing.getName()).append("\n");
        }
        ((TextView) findViewById(R.id.tvIngredients)).setText(sb.toString().trim());
        ((TextView) findViewById(R.id.tvMethod)).setText(recipe.getSteps());
    }
}