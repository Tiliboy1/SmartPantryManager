package com.example.smartpantrymanager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object: the CRUD operations for pantry items.
 */
@Dao
public interface PantryItemDao {

    @Insert
    long insert(PantryItem item);          // Create

    @Query("SELECT * FROM pantry_items ORDER BY name ASC")
    List<PantryItem> getAllItems();        // Read

    @Query("SELECT * FROM pantry_items WHERE id = :id")
    PantryItem getItemById(int id);        // Read one

    @Update
    void update(PantryItem item);          // Update

    @Delete
    void delete(PantryItem item);          // Delete
}