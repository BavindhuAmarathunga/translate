package com.example.cw2;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class language_subscription extends AppCompatActivity {
    Db_handler DICTIONARY_DB;
    TextView textView;
    Button subscribe;
    ArrayAdapter arrayAdapter;
    ListView langList;
    ArrayList<String> listLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);
        textView = (TextView) findViewById(R.id.tvTrWord);
        langList = (ListView) findViewById(R.id.LangList);
        subscribe = (Button) findViewById(R.id.btn_sub);
        DICTIONARY_DB = new Db_handler(this);

        DICTIONARY_DB.clearLans();      // clear languages in db
        Toast.makeText(this,"Please wait 5 seconds",Toast.LENGTH_LONG).show();
        GetLanguages getLanguages=new GetLanguages();
        getLanguages.execute();

        final Handler handler = new Handler();   //delay 7 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewLang();
                System.out.println("listed languages");
                System.out.println(listLang);

            }
        },7000);

        langList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {                  //identify the position in the array adapter
                String Lang = (String) parent.getItemAtPosition(position);

                System.out.println(Lang+"*********************************"+langList.getItemIdAtPosition(position));
            }
        });
//=====================================================subscribe button=========================================
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DICTIONARY_DB.clearSub();               // clear subscribed languages in db
                SparseBooleanArray listId =langList.getCheckedItemPositions();
                for (int a=0; listId.size()>a; a++){
                    if (listId.valueAt(a)){
                        String value = listLang.get(listId.keyAt(a));
                        int x = (listId.keyAt(a));
                        System.out.println(value+" = Position: "+x);
                        DICTIONARY_DB.CheckedLanguages(x,value);            //pass checked language details in to the database
                        Toast.makeText(language_subscription.this,"languages subscribed ",Toast.LENGTH_LONG).show();
                    }
                }
                System.out.println("=====================================================");
            }
        });
    }
//=======================================================IBM translator===============================================
    private class GetLanguages extends AsyncTask{
        List<IdentifiableLanguage> langlist =new ArrayList<>();

        @Override
        protected Object doInBackground(Object[] objects) {
            System.out.println("before doInBackground ============================================");
            Authenticator authenticator = new IamAuthenticator("exKYVqW7nsj5htVoE-CnkZaRnxLhv6sNSa2FKD1N17UM");
            System.out.println("after IamAuthenticator ============================================");
            LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
            System.out.println("before setServiceUrl ============================================");
            languageTranslator.setServiceUrl("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/152ea726-e579-4fe7-b25a-7b4d9292dd31");
            System.out.println("after setServiceUrl ============================================");
            IdentifiableLanguages languages = languageTranslator.listIdentifiableLanguages().execute().getResult();
            System.out.println("after languages ============================================");
            System.out.println(languages);
            langlist  = languages.getLanguages();
            System.out.println("after doInBackground ============================================");
            return langlist;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            for (int i = 0; i <langlist.size(); i++) {
                System.out.println("before onPostExecute ============================================");
                IdentifiableLanguage nm=langlist.get(i);
                String language = nm.getLanguage();             //get language name and language code
                String LangName = nm.getName();
                System.out.println(language);
                System.out.println(LangName);
                DICTIONARY_DB.insertLanguage(LangName,language);
                System.out.println("after onPostExecute ============================================");
            }
        }
    }

    //========================================== set list view =====================================
    private void viewLang() {
        Cursor cursor = DICTIONARY_DB.viewAllLanguages();
        listLang = new ArrayList<>();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                listLang.add(cursor.getString(0));
            }
        }


        Collections.sort(listLang);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, listLang);
        langList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        langList.setAdapter(arrayAdapter);
//=================================================check for the checked langs=========================================
        Cursor cursorA = DICTIONARY_DB.viewCheckedLanguages();
        System.out.println(cursorA.getCount() + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~```");

        if (cursorA.getCount() == 0) {
            Collections.sort(listLang);
            Toast.makeText(this, "No subscribed languages", Toast.LENGTH_LONG).show();
        } else {
            while (cursorA.moveToNext()) {
                System.out.println(cursorA.getCount() + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~```" + cursorA.getInt(0));
                int position = cursorA.getInt(0);
                langList.setItemChecked(position, true);        //set already subscribed languages
            }
        }
    }

}