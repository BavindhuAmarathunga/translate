package com.example.cw2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
public Button btn_add,btn_View,edit,subscription,translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//============================================add button=========================================
        btn_add = (Button) findViewById(R.id.button_Add_Phrases);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,add_phrases.class);
                startActivity(intent);
            }
        });

        //============================================view button=========================================
        btn_View = (Button) findViewById(R.id.button_Display_Phrases);
        btn_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,display_phrases.class);
                startActivity(intent);
            }
        });

        //============================================edit button=========================================
        edit = (Button) findViewById(R.id.button_Edit_Phrases);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,edit_phrases.class);
                startActivity(intent);
            }
        });

        //============================================subscription button=========================================
        subscription = (Button) findViewById(R.id.button_Language_Subscription);
        subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,language_subscription.class);
                startActivity(intent);
            }
        });

        //============================================translate button=========================================
        translate = (Button) findViewById(R.id.button_Translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,translate.class);
                startActivity(intent);
            }
        });
    }
}
