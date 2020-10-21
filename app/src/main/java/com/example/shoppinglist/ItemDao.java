package com.example.shoppinglist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("DELETE FROM item_tableName")
    void deleteAllItems();

    @Query("SELECT * FROM item_tableName ORDER BY itemType ASC")
    LiveData<List<Item>> getAllItems();
}
