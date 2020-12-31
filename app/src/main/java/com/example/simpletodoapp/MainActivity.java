package com.example.simpletodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "note text";
    public static final String KEY_ITEM_POSITION = "note position";
    public static final int UPDATE_TEXT_CODE = 1;

    List<String> notes;

    Button addBtn;
    EditText inTxt;
    RecyclerView rViewItems;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.addBtn);
        inTxt = findViewById(R.id.inTxt);
        rViewItems = findViewById(R.id.rViewItems);

        //inTxt.setText("Testing");

        loadNotes();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            public void onItemLongClicked(int location) {
                //delete the item and notify the adapter of the position
                notes.remove(location);
                itemsAdapter.notifyItemRemoved(location);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveNotes();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int location) {
                Log.d("MainActivity", "Single click at point " + location);
                //create
                Intent n = new Intent(MainActivity.this, EditActivity.class);
                //pass data
                n.putExtra(KEY_ITEM_TEXT, notes.get(location));
                n.putExtra(KEY_ITEM_POSITION, location);
                //display
                startActivityForResult(n, UPDATE_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(notes, onLongClickListener, onClickListener);
        rViewItems.setAdapter(itemsAdapter);
        rViewItems.setLayoutManager(new LinearLayoutManager(this));

        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View n) {
                String newItem = inTxt.getText().toString();
                //add item
                notes.add(newItem);
                //call adapter
                itemsAdapter.notifyItemInserted(notes.size() - 1);
                inTxt.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveNotes();
            }
        });
    }

    //handle edit activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UPDATE_TEXT_CODE) {
            //retrieve new data
            String noteText = data.getStringExtra(KEY_ITEM_TEXT);
            //get position of item being edited
            int location = data.getExtras().getInt(KEY_ITEM_POSITION);
            //update position data
            notes.set(location, noteText);
            //call the adapter
            itemsAdapter.notifyItemChanged(location);
            //save changes to the item
            saveNotes();
            Toast.makeText(getApplicationContext(), "Item has been updated", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivity");
        }

    }


    //get file
    private File getNotesFile() {
        return new File(getFilesDir(), "file.notes");
    }

    //load the file to read the notes in
    private void loadNotes() {
        try {
            notes = new ArrayList<>(FileUtils.readLines(getNotesFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading notes file", e);
            notes = new ArrayList<>();
        }
    }

    //saves notes to the file
    private void saveNotes() {
        try {
            FileUtils.writeLines(getNotesFile(), notes);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing notes", e);
        }
    }
}