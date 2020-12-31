package com.example.simpletodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText upNote;
    Button upBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        upNote = findViewById(R.id.upNote);
        upBtn = findViewById(R.id.upBtn);

        getSupportActionBar().setTitle("Edit Note");

        upNote.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        //click to save new notes
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent to hold results of modifications
                Intent i = new Intent();
                //pass the data back
                i.putExtra(MainActivity.KEY_ITEM_TEXT, upNote.getText().toString());
                i.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                //close activity view and return to main view
                setResult(RESULT_OK, i);
                //close the window
                finish();
            }
        });
    }
}