package com.example.smartpantrymanager;

import java.util.List;
import java.util.Locale;

/**
 * Core business logic. A recipe qualifies ONLY if EVERY ingredient is in the
 * pantry in at least the required quantity.
 */
public class RecipeMatcher {

    /** Makes names comparable: "Tomatoes " -> "tomato", "Eggs" -> "egg". */
    public static String normalize(String raw) {
        if (raw == null) return "";
        String s = raw.trim().toLowerCase(Locale.ROOT);
        if (s.length() > 4 && s.endsWith("ies")) return s.substring(0, s.length() - 3) + "y";
        if (s.length() > 4 && s.endsWith("oes")) return s.substring(0, s.length() - 2);
        if (s.length() > 3 && s.endsWith("s") && !s.endsWith("ss") && !s.endsWith("us")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    private static String unitType(String unit) {
        switch (unit.toLowerCase(Locale.ROOT)) {
            case "g": case "kg": return "mass";
            case "ml": case "l": case "cup": case "tbsp": case "tsp": return "volume";
            default: return "count";
        }
    }

    /** Converts to a base unit (g, ml or pieces) so units can be compared. */
    private static double toBase(double qty, String unit) {
        switch (unit.toLowerCase(Locale.ROOT)) {
            case "kg": case "l": return qty * 1000;
            case "cup": return qty * 240;
            case "tbsp": return qty * 15;
            case "tsp": return qty * 5;
            default: return qty;
        }
    }

    public static boolean canMake(List<RecipeIngredient> needed, List<PantryItem> pantry) {
        for (RecipeIngredient ingredient : needed) {
            if (!hasIngredient(ingredient, pantry)) {
                return false; // one missing ingredient rejects the whole recipe
            }
        }
        return true;
    }

    private static boolean hasIngredient(RecipeIngredient ing, List<PantryItem> pantry) {
        String target = normalize(ing.getName());
        String type = unitType(ing.getUnit());
        double required = toBase(ing.getQuantity(), ing.getUnit());
        double available = 0;
        boolean found = false;
        boolean comparable = false;

        for (PantryItem item : pantry) {
            if (!normalize(item.getName()).equals(target)) continue;
            found = true;
            if (unitType(item.getUnit()).equals(type)) {
                comparable = true;
                available += toBase(item.getQuantity(), item.getUnit());
            }
        }
        if (!found) return false;        // ingredient not in pantry
        if (!comparable) return true;    // units can't be compared (e.g. g vs pcs)
        return available >= required;    // enough quantity?
    }
}