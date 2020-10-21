package com.example.shoppinglist;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Item.class, version = 1)
public abstract class ItemDataBase extends RoomDatabase {

    private static ItemDataBase itemDataBaseInstance;

    public abstract ItemDao itemDao();

    public static synchronized ItemDataBase getInstance(Context context) {
        if (itemDataBaseInstance == null) {
            itemDataBaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ItemDataBase.class, "item_tableName")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomDatabaseCallback)
                    .build();
        }
        return itemDataBaseInstance;
    }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
