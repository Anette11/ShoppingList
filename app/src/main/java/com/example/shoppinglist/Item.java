package com.example.shoppinglist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_tableName")
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String itemType;
    private final String itemAmount;

    public Item(String itemType, String itemAmount) {
        this.itemType = itemType;
        this.itemAmount = itemAmount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemAmount() {
        return itemAmount;
    }
}
