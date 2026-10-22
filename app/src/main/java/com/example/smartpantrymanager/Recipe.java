package com.example.smartpantrymanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String steps;

    public Recipe(String name, String steps) {
        this.name = name;
        this.steps = steps;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }
}