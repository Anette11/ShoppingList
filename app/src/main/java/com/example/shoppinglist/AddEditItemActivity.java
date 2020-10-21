package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditItemActivity extends AppCompatActivity {
    private EditText editTextType;
    private EditText editTextAmount;
    public static final String TYPE_PUT_EXTRA_INTENT
            = "com.example.shoppinglist.TYPE_PUT_EXTRA_INTENT";
    public static final String AMOUNT_PUT_EXTRA_INTENT
            = "com.example.shoppinglist.AMOUNT_PUT_EXTRA_INTENT";
    public static final String STRING_INT_VALUE_PUT_EXTRA_INTENT
            = "com.example.shoppinglist.STRING_INT_VALUE_PUT_EXTRA_INTENT";
    public static final int DEFAULT_VALUE_PUT_EXTRA_INTENT_ADD_NOTE_REQUEST = 333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_item_activity_layout);
        initialize();
        checkIntentAndSetTitle();
    }

    private void initialize() {
        editTextType = findViewById(R.id.edit_text_type);
        editTextAmount = findViewById(R.id.edit_text_amount);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void checkIntentAndSetTitle() {
        Intent intent = getIntent();
        if (intent.hasExtra(STRING_INT_VALUE_PUT_EXTRA_INTENT)) {
            setTitle(getResources().getString(R.string.edit_item));
            editTextType.setText(intent.getStringExtra(TYPE_PUT_EXTRA_INTENT));
            editTextAmount.setText(intent.getStringExtra(AMOUNT_PUT_EXTRA_INTENT));
        } else {
            setTitle(getResources().getString(R.string.add_item));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cancel) {
            cancelItem();
            return true;
        } else if (item.getItemId() == R.id.save) {
            saveItem();
            return true;
        } else {
            super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void cancelItem() {
        Intent intent = new Intent(AddEditItemActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveItem() {
        String type = editTextType.getText().toString();
        String amount = editTextAmount.getText().toString();
        if (type.trim().isEmpty()) {
            toastShow(getResources().getString(R.string.insert_type_toast));
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(TYPE_PUT_EXTRA_INTENT, type);
        intent.putExtra(AMOUNT_PUT_EXTRA_INTENT, amount);
        int id = getIntent().getIntExtra(STRING_INT_VALUE_PUT_EXTRA_INTENT, DEFAULT_VALUE_PUT_EXTRA_INTENT_ADD_NOTE_REQUEST);
        if (id != DEFAULT_VALUE_PUT_EXTRA_INTENT_ADD_NOTE_REQUEST) {
            intent.putExtra(STRING_INT_VALUE_PUT_EXTRA_INTENT, id);
        }
        setResult(RESULT_OK, intent);
        finish();
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