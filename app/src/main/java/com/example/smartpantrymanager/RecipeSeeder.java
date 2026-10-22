package com.example.smartpantrymanager;

/**
 * Pre-loads the recipe collection the first time the app runs.
 * Ingredient format: "name:quantity:unit".
 */
public class RecipeSeeder {

    public static void seedIfEmpty(AppDatabase db) {
        RecipeDao dao = db.recipeDao();
        if (dao.countRecipes() > 0) return;

        add(dao, "Scrambled Eggs", "1. Beat the eggs.\n2. Melt the butter in a pan.\n3. Stir gently until just set.", "egg:2:pcs", "butter:10:g");
        add(dao, "Tomato Omelette", "1. Beat the eggs.\n2. Chop the tomato and add it.\n3. Cook in a pan, top with cheese and fold.", "egg:3:pcs", "tomato:1:pcs", "cheese:30:g");
        add(dao, "Egg Fried Rice", "1. Fry the chopped onion.\n2. Add the cooked rice.\n3. Push aside, scramble the eggs, then mix.", "rice:200:g", "egg:2:pcs", "onion:1:pcs");
        add(dao, "Tomato Pasta", "1. Boil the pasta.\n2. Fry onion and tomatoes into a sauce.\n3. Toss with the pasta.", "pasta:200:g", "tomato:3:pcs", "onion:1:pcs");
        add(dao, "Cheese Toast", "1. Butter the bread.\n2. Top with cheese.\n3. Grill until golden.", "bread:2:pcs", "cheese:40:g", "butter:10:g");
        add(dao, "Pancakes", "1. Whisk flour, egg and milk.\n2. Pour into a hot pan.\n3. Flip when bubbles form.", "flour:200:g", "egg:1:pcs", "milk:250:ml");
        add(dao, "Mashed Potatoes", "1. Boil the potatoes until soft.\n2. Mash with butter and milk.", "potato:4:pcs", "butter:30:g", "milk:50:ml");
        add(dao, "Rice Pudding", "1. Simmer rice in milk.\n2. Stir in sugar.\n3. Cook until creamy.", "rice:100:g", "milk:500:ml", "sugar:30:g");
        add(dao, "Tomato Soup", "1. Fry onion in butter.\n2. Add chopped tomatoes and simmer.\n3. Blend until smooth.", "tomato:5:pcs", "onion:1:pcs", "butter:20:g");
        add(dao, "French Toast", "1. Whisk egg and milk.\n2. Dip the bread.\n3. Fry both sides until golden.", "bread:4:pcs", "egg:2:pcs", "milk:100:ml");
        add(dao, "Cheese Omelette", "1. Beat the eggs.\n2. Melt butter in a pan and pour in the eggs.\n3. Add cheese and fold.", "egg:3:pcs", "cheese:50:g", "butter:10:g");
        add(dao, "Lentil Soup", "1. Fry onion and carrots.\n2. Add lentils and water.\n3. Simmer for 30 minutes.", "lentil:200:g", "onion:1:pcs", "carrot:2:pcs");
        add(dao, "Garlic Butter Rice", "1. Cook the rice.\n2. Fry garlic in butter.\n3. Mix through the rice.", "rice:200:g", "garlic:3:pcs", "butter:15:g");
        add(dao, "Chicken Rice Bowl", "1. Fry the chicken with onion.\n2. Cook the rice.\n3. Serve the chicken over the rice.", "chicken:300:g", "rice:200:g", "onion:1:pcs");
        add(dao, "Mac and Cheese", "1. Boil the pasta.\n2. Melt butter, add milk and cheese.\n3. Stir in the pasta.", "pasta:200:g", "cheese:100:g", "milk:200:ml", "butter:20:g");
        add(dao, "Banana Pancakes", "1. Mash the bananas.\n2. Mix with egg and flour.\n3. Fry spoonfuls until golden.", "banana:2:pcs", "egg:2:pcs", "flour:100:g");
        add(dao, "Garlic Roast Potatoes", "1. Chop the potatoes.\n2. Toss with crushed garlic.\n3. Roast at 200 degrees C for 40 minutes.", "potato:6:pcs", "garlic:2:pcs");
        add(dao, "Veggie Fried Rice", "1. Fry carrots, onion and garlic.\n2. Add the cooked rice.\n3. Stir-fry until hot.", "carrot:2:pcs", "onion:1:pcs", "rice:150:g", "garlic:2:pcs");
    }

    private static void add(RecipeDao dao, String name, String steps, String... ingredients) {
        long recipeId = dao.insertRecipe(new Recipe(name, steps));
        for (String s : ingredients) {
            String[] p = s.split(":");
            dao.insertIngredient(new RecipeIngredient((int) recipeId, p[0],
                    Double.parseDouble(p[1]), p[2]));
        }
    }
}