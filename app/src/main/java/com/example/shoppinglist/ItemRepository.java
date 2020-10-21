package com.example.shoppinglist;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemRepository {
    private final ItemDao itemDao;
    private final LiveData<List<Item>> liveDataListItems;

    public ItemRepository(Application application) {
        ItemDataBase itemDataBase = ItemDataBase.getInstance(application);
        itemDao = itemDataBase.itemDao();
        liveDataListItems = itemDao.getAllItems();
    }

    void insert(Item item) {
        new InsertItemAsyncTask(itemDao).execute(item);
    }

    void update(Item item) {
        new UpdateItemAsyncTask(itemDao).execute(item);
    }

    void delete(Item item) {
        new DeleteItemAsyncTask(itemDao).execute(item);
    }

    void deleteAllItems() {
        new DeleteAllItemsAsyncTask(itemDao).execute();
    }

    LiveData<List<Item>> getAllItems() {
        return liveDataListItems;
    }

    private static class InsertItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private final ItemDao itemDao;

        private InsertItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.insert(items[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private final ItemDao itemDao;

        private UpdateItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.update(items[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private final ItemDao itemDao;

        private DeleteItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.delete(items[0]);
            return null;
        }
    }

    private static class DeleteAllItemsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ItemDao itemDao;

        private DeleteAllItemsAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDao.deleteAllItems();
            return null;
        }
    }
}
