package com.example.shoppinglist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private final ItemRepository itemRepository;
    private final LiveData<List<Item>> liveDataListItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new ItemRepository(application);
        liveDataListItems = itemRepository.getAllItems();
    }

    void insert(Item item) {
        itemRepository.insert(item);
    }

    void update(Item item) {
        itemRepository.update(item);
    }

    void delete(Item item) {
        itemRepository.delete(item);
    }

    void deleteAllItems() {
        itemRepository.deleteAllItems();
    }

    LiveData<List<Item>> getAllItems() {
        return liveDataListItems;
    }
}
