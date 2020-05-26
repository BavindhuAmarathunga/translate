package com.example.cw2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class add_phrases extends AppCompatActivity {
    Db_handler DICTIONARY_DB;
    EditText add_Text;
Button btn_Add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);

        DICTIONARY_DB = new Db_handler(this);
        add_Text = (EditText) findViewById(R.id.addText);
        btn_Add = (Button)findViewById(R.id.button_add);
        addData();

    }

    //===========================================set list view===========================================
    public void addData(){
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = add_Text.getText().toString();                //get text from text view
                if (!text.equals("")){
                    boolean isWordInseted = DICTIONARY_DB.insertData(add_Text.getText().toString());            //insert wor in to database
                    if (isWordInseted = true)
                        Toast.makeText(add_phrases.this,"Word Save Successfully",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(add_phrases.this,"Word not Save Successfully",Toast.LENGTH_LONG).show();
                    add_Text.setText("");
                }else {                 //if nothing in the text view
                    Toast.makeText(add_phrases.this, "You must ENTER a word",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
