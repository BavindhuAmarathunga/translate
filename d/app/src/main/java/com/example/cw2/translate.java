package com.example.cw2;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.ArrayList;
import java.util.Collections;

public class translate extends AppCompatActivity {
    Db_handler DICTIONARY_DB;
    ArrayList<String> listWords;
    ArrayAdapter arrayAdapter;
    ListView listView;
    ArrayList checkedLangsN;
    Spinner spinner;
    String LCode;
    String code;
    String selectedWord;
    TextView textView;
    LanguageTranslator languageTranslator;
    Button translate,speech;
    private StreamPlayer player = new StreamPlayer();
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        spinner = (Spinner) findViewById(R.id.spinner);
        textView = (TextView) findViewById(R.id.tvTrWord);
        translate = (Button) findViewById(R.id.btnTran);
        speech = (Button)findViewById(R.id.btnSpeech);




        listView = (ListView) findViewById(R.id.lvTr);
        DICTIONARY_DB = new Db_handler(this);
        listWords = new ArrayList<>();
        checkedLangsN = new ArrayList<String>();
        setSpinner();
        viewData();


        //===================================Language Translate button==============================================
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnTranslate clicked");
                languageTranslator = LanguageTranslatorService();
                new TranslationTask().execute(selectedWord);
//                arrayAdapter.clear();
                System.out.println("btnTranslate clicked end"+selectedWord);

            }
        });

        //===================================Language speech button==============================================
        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String translateWord = textView.getText().toString();
                textToSpeech = TextToSpeechService();
                new SynthesisTask().execute(translateWord);
            }
        });
    }

//===================================IBM Language Translator==============================================

    private LanguageTranslator LanguageTranslatorService() {
        Authenticator authenticator
                = new
                IamAuthenticator("exKYVqW7nsj5htVoE-CnkZaRnxLhv6sNSa2FKD1N17UM");
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);

                service.setServiceUrl("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/152ea726-e579-4fe7-b25a-7b4d9292dd31");
        return service;
    }

    private  class TranslationTask extends AsyncTask<String,Void,String>{
        String firstTranslation;
        @Override
        protected String doInBackground(String... strings) {

          try {
              TranslateOptions translateOptions = new
                      TranslateOptions.Builder()
                      .addText(strings[0])
                      .source(Language.ENGLISH)
                      .target(code)
                      .build();
              TranslationResult result
                      =languageTranslator.translate(translateOptions).execute().getResult();
               firstTranslation =
                      result.getTranslations().get(0).getTranslation();

          }catch (NotFoundException e){
              System.out.println(e);
          }
            return firstTranslation;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
             textView.setText(s);
        }
    }

    //===================================IBM Language text speech==============================================

    private TextToSpeech TextToSpeechService() {
        Authenticator authenticator = new IamAuthenticator("BOthJiaX1euIB9AAJSJyfZAtUnkXrY4vHf6mNByjNCRA");
        TextToSpeech serviceSpeech = new TextToSpeech(authenticator);
        serviceSpeech.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/b1a9a684-19bb-43a8-8711-61356c64b1e0");
        return serviceSpeech;
    }

    private class SynthesisTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                SynthesizeOptions synthesizeOptions = new
                        SynthesizeOptions.Builder()
                        .text(params[0])
                        .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                        .accept(HttpMediaType.AUDIO_WAV)
                        .build();
                player.playStream(textToSpeech.synthesize(synthesizeOptions).execute()
                        .getResult());

            }catch (NotFoundException e){
                System.out.println(e);
            }
            return "Did synthesize";
        }
    }

    //====================================setting spinner===================================
    private void setSpinner() {
        Cursor name = DICTIONARY_DB.showCheckedLanguages();         //get subscribed languages names
        if (name.getCount() == 0) {
            Toast.makeText(this, "No subscribed languages", Toast.LENGTH_LONG).show();
        } else {
            while (name.moveToNext()) {
                checkedLangsN.add(name.getString(0));              //set subscribed languages in to array
                System.out.println(checkedLangsN + " ~~~~~~~" );
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,checkedLangsN);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LCode = (String) parent.getSelectedItem();                              //get selected language from spinner
                System.out.println(LCode);
                getLangCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //===================================== getting language code ================================

private  void  getLangCode(){

    Cursor codeLang = DICTIONARY_DB.viewCheckedLangCode(LCode);     //get subscribed languages code
    if (codeLang.getCount() > 0){
        codeLang.moveToPosition(codeLang.getCount() -1);
        code = codeLang.getString(0);
        System.out.println(code+" <== language code <== "+LCode);
    }

}

    //===================================== view data in list view ================================
    private void viewData() {
        Cursor cursor = DICTIONARY_DB.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Words To Display", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                listWords.add(cursor.getString(1));             //set words in to array
            }
            Collections.sort(listWords);
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, listWords);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {          //get selected word position from the list view
                    selectedWord = (String) parent.getItemAtPosition(position);
                    System.out.println("Selected word fom the list ==> "+selectedWord);
                }
            });
        }
    }
}
