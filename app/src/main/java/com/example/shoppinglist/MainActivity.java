package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ITEM_ADD = 111;
    private static final int REQUEST_ITEM_EDIT = 222;
    private static final int DEFAULT_VALUE = -1;
    private ItemViewModel itemViewModel;
    private ItemAdapter itemAdapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        initialize();
        setListToBeDisplayed();
    }

    private void initialize() {
        initFloatingActionButton();
        initRecyclerView();
    }

    private void initFloatingActionButton() {
        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButtonSetOnClickListener();
    }

    private void floatingActionButtonSetOnClickListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
                startActivityForResult(intent, REQUEST_ITEM_ADD);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);
        itemAdapterSetOnItemClickListenerInterface();
    }

    private void itemAdapterSetOnItemClickListenerInterface() {
        itemAdapter.setOnItemClickListenerInterface(new ItemAdapter.OnItemClickListenerInterface() {
            @Override
            public void onItemClickListenerInterfaceMethodEditItem(int position) {
                editItemIntent(position);
            }

            @Override
            public void onItemClickListenerInterfaceMethodDeleteItem(int position) {
                itemViewModel.delete(itemAdapter.getItemAt(position));
            }
        });
    }

    private void editItemIntent(int position) {
        Intent intentEditItem = new Intent(MainActivity.this, AddEditItemActivity.class);
        intentEditItem.putExtra(AddEditItemActivity.STRING_INT_VALUE_PUT_EXTRA_INTENT, itemAdapter.getItemAt(position).getId());
        intentEditItem.putExtra(AddEditItemActivity.TYPE_PUT_EXTRA_INTENT, itemAdapter.getItemAt(position).getItemType());
        intentEditItem.putExtra(AddEditItemActivity.AMOUNT_PUT_EXTRA_INTENT, itemAdapter.getItemAt(position).getItemAmount());
        startActivityForResult(intentEditItem, REQUEST_ITEM_EDIT);
    }

    private void setListToBeDisplayed() {
        itemViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(ItemViewModel.class);
        itemViewModelObserveAllItems();
    }

    private void itemViewModelObserveAllItems() {
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemAdapter.submitList(items);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ITEM_ADD && resultCode == RESULT_OK) {
            assert data != null;
            itemViewModelInsert(data);
        } else if (requestCode == REQUEST_ITEM_EDIT && resultCode == RESULT_OK) {
            assert data != null;
            itemViewModelUpdate(data);
        }
    }

    private void itemViewModelInsert(Intent data) {
        String type = data.getStringExtra(AddEditItemActivity.TYPE_PUT_EXTRA_INTENT);
        String amount = data.getStringExtra(AddEditItemActivity.AMOUNT_PUT_EXTRA_INTENT);
        Item item = new Item(type, amount);
        itemViewModel.insert(item);
        toastShow(getResources().getString(R.string.inserted_toast));
    }

    private void itemViewModelUpdate(Intent data) {
        int id = data.getIntExtra(AddEditItemActivity.STRING_INT_VALUE_PUT_EXTRA_INTENT, DEFAULT_VALUE);
        if (id == DEFAULT_VALUE) {
            toastShow(getResources().getString(R.string.not_updated_toast));
            return;
        }
        String type = data.getStringExtra(AddEditItemActivity.TYPE_PUT_EXTRA_INTENT);
        String amount = data.getStringExtra(AddEditItemActivity.AMOUNT_PUT_EXTRA_INTENT);
        Item item = new Item(type, amount);
        item.setId(id);
        itemViewModel.update(item);
        toastShow(getResources().getString(R.string.updated_toast));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_delete_all_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            itemViewModel.deleteAllItems();
            toastShow(getResources().getString(R.string.deleted_toast));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void toastShow(String textToast) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_id));
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(textToast);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}

