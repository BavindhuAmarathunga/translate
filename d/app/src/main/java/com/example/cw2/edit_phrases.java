package com.example.cw2;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class edit_phrases extends AppCompatActivity {

    Db_handler DICTIONARY_DB;
    ArrayList<String> listWords;
    ArrayAdapter arrayAdapter;
    ListView listView;
    EditText editText;
    Button btn_Edit;
    Button btn_Save;
    String word;
    int vq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);
        listView = (ListView) findViewById(R.id.listViewEdit);
        editText = (EditText) findViewById(R.id.txtEditWord);
        btn_Edit = (Button) findViewById(R.id.btnEdit);
        btn_Save = (Button) findViewById(R.id.btnSave);



        DICTIONARY_DB = new Db_handler(this);


        listWords = new ArrayList<>();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word = (String) parent.getItemAtPosition(position);


                btn_Edit.setVisibility(View.VISIBLE);               //setting visibility of the button

                if (vq==1){
                    editText.setText(word);                 //set selected wor to the text view
                }
            }
        });

        //=============================== button edit===============================================
        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vq = 1;
                editText.setText(word);

                editText.setVisibility(View.VISIBLE);
                btn_Save.setVisibility(View.VISIBLE);

            }
        });

        viewData();

        save();

    }

    private void save(){
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EWord = editText.getText().toString();
                System.out.println(EWord);
                Cursor Id = DICTIONARY_DB.getId(word);                      //get id of the selected word
                int id =0;
                while (Id.moveToNext()){
                    id = Id.getInt(0);
                    System.out.println(id);
                }

                if (!EWord.equals("")){
                    DICTIONARY_DB.editWord(EWord,id,word);              //set new word to the selected word place
                    arrayAdapter.clear();
                    Collections.sort(listWords);
                    viewData();
                    editText.setText("");
                    Toast.makeText(edit_phrases.this,"Word Edit Successfully",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(edit_phrases.this,"You must ENTER a word",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//===========================================set list view===========================================
    private void viewData() {
        Cursor cursor =DICTIONARY_DB.viewData();

        if (cursor.getCount() == 0){
            Toast.makeText(this,"No Words To Display",Toast.LENGTH_LONG).show();
        }else {
            while (cursor.moveToNext()){
                listWords.add(cursor.getString(1));
            }
            Collections.sort(listWords);
            arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice,listWords);
            listView.setAdapter(arrayAdapter);

        }
    }

}
