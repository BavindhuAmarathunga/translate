package com.example.cw2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Db_handler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "DICTIONARY";
    private static final String TAABLE_NAME = "WORDS";
    private static final String COL_1 = "WORD_ID";
    private static final String COL_2 = "WORD";
    private static final String tag= "Db_handler";
    private static final String LngTABLE_NAME = "Languages";
    private static final String Language_Id = "Id";
    private static final String Language_NAME = "Name";
    private static final String Language_Code  = "Code";
    private static final String Subscribed_TABLE  = "subscribed_lang";
    private static final String Subscribed_Id  = "subscribed_Id";
    private static final String checked_Lang_Code  = "lang_position";
    private static final String Language_Checked  = "subscribed";

//=============================create database==========================================================================
    public Db_handler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    //=============================create database table==========================================================================
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TAABLE_NAME + " (WORD_ID INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT) " );
        db.execSQL("Create table " + LngTABLE_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, Code TEXT,  Name TEXT) " );
        db.execSQL("Create table " + Subscribed_TABLE + " (subscribed_Id INTEGER PRIMARY KEY AUTOINCREMENT, subscribed TEXT, lang_position TEXT) " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + TAABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + LngTABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + Subscribed_TABLE);
        onCreate(db);
    }

    public boolean insertLanguage(String language,String Code){         //insert languages in to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Language_NAME,language);
        contentValues.put(Language_Code,Code);
        long isInsert = db.insert(LngTABLE_NAME,null,contentValues);
        if (isInsert == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean insertData(String word){                         //insert words in to database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,word);
       long isInsert = DB.insert(TAABLE_NAME,null,contentValues);
       if (isInsert == -1)
           return false;
       else
           return true;
    }

    public Cursor viewData(){                                      //sending words from database
        SQLiteDatabase DB = this.getReadableDatabase();
        String query = "select * from "+TAABLE_NAME;
        Cursor cursor = DB.rawQuery(query,null);
        return cursor;
    }
    
    public void editWord(String newWord, int id, String oldWord){           //setting edited words in database side
        SQLiteDatabase DB = this.getWritableDatabase();
        String query = " UPDATE " + TAABLE_NAME +" SET " + COL_2 + " = '" + newWord +
                "' WHERE " + COL_1 + "= '" + id + "' AND " + "" + COL_2 + " = '" + oldWord + "' ";
        Log.d(tag,"UpdateWord: query: " + query);
        Log.d(tag,"UpdateWord: Setting name to " + newWord);
        DB.execSQL(query);
    }

    public Cursor getId(String word){                                        //sending relevant id for the relevant word
        SQLiteDatabase DB =this.getWritableDatabase();
        String query = " SELECT " + COL_1 + " FROM " + TAABLE_NAME + " WHERE " + COL_2 + " = '" + word + "'";
        Cursor cursor = DB.rawQuery(query,null);
        return cursor;
    }

    public void clearLans(){                                    //clear all the data in this table
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + LngTABLE_NAME);
        db.close();
    }

    public void clearSub(){                                      //clear all the data in this table
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Subscribed_TABLE);
        db.close();
    }

    public Cursor viewAllLanguages() {                          //sending language names
        SQLiteDatabase DB = this.getReadableDatabase();
        String query = "SELECT "+Language_NAME+" FROM "+LngTABLE_NAME;
        Cursor cursor = DB.rawQuery(query,null);
        return cursor;
    }
//    public Cursor isChecked(){
//        SQLiteDatabase DB = this.getReadableDatabase();
//        String query = "SELECT * FROM sqlite_master WHERE name='"+Subscribed_TABLE+"' AND type='table'";
//        Cursor cursor = DB.rawQuery(query,null);
//        return cursor;
//    }

    public Cursor viewCheckedLanguages() {                       //sending language codes
        SQLiteDatabase DB = this.getReadableDatabase();
        String query = "SELECT " + checked_Lang_Code + " FROM " +Subscribed_TABLE ;
        Cursor cursor = DB.rawQuery(query, null);
        return cursor;
    }

    public Cursor viewCheckedLangCode(String Lang) {                    //sending relevant code for the relevant language
        SQLiteDatabase DB = this.getReadableDatabase();
        String query = " SELECT " + Language_Code + " FROM " + LngTABLE_NAME + " WHERE " + Language_NAME + " = '" + Lang + "'";
        Cursor cursor = DB.rawQuery(query, null);
        return cursor;
    }

    public Cursor showCheckedLanguages() {                                   //sending checked languages names
        SQLiteDatabase DB = this.getReadableDatabase();
        String query = "SELECT " + Language_Checked + " FROM " +Subscribed_TABLE ;
        Cursor cursor = DB.rawQuery(query, null);
        return cursor;
    }

    public boolean CheckedLanguages(int i, String lang) {                   //insert checked languages in to database

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Language_Checked,lang);
        contentValues.put(checked_Lang_Code,i);
        long isInsert =db.insert(Subscribed_TABLE,null,contentValues);
        if (isInsert == -1){
            return false;
        }else {
            return true;
        }
    }
//    public void deleteLang(int id, String word){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM " + Subscribed_TABLE + " WHERE " + COL_1 + " = '" +
//                id + "'" + " AND " + COL_2 + " = '" + word + "'";
//        Log.d(tag, "deleteWord: query: " + query);
//        Log.d(tag, "deleteWord: Deleting " + word + " from database.");
//        db.execSQL(query);
//    }
}
