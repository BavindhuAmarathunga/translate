package com.example.cw2;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class display_phrases extends AppCompatActivity {
    Db_handler DICTIONARY_DB;
    ArrayList<String> listWords;
    ArrayAdapter arrayAdapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);
        listView = (ListView) findViewById(R.id.listView);

        DICTIONARY_DB = new Db_handler(this);

        listWords = new ArrayList<>();

        viewData();
    }
    //===========================================set list view===========================================
    private void viewData() {
        Cursor cursor =DICTIONARY_DB.viewData();                    //get words from database

        if (cursor.getCount() == 0){
            Toast.makeText(this,"No Words To Display",Toast.LENGTH_LONG).show();
        }else {
            while (cursor.moveToNext()){
                listWords.add(cursor.getString(1));
            }
            Collections.sort(listWords);
            arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listWords);

            listView.setAdapter(arrayAdapter);
        }
    }
}
