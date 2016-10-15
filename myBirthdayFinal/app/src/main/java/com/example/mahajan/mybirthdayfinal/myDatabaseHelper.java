package com.example.mahajan.mybirthdayfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mahajan on 8/3/2016.
 */
public class myDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Birthdays";

    public myDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE friends (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mybirthday TEXT, phoneno INTEGER);" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      /*  db.execSQL("DROP TABLE IF EXISTS friends");*/
        if(oldVersion == 1 && newVersion == 2)
        {
            db.execSQL("ALTER TABLE friends ADD phoneno INTEGER");
        }
    }



    public void addBirthday(String name, String birthday, long phoneno){
        ContentValues values = new ContentValues((3));
        values.put("name", name);
        values.put("mybirthday", birthday);
        values.put("phoneno", phoneno);
        getWritableDatabase().insert("friends",null,values);
    }

    public Cursor getFriends(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM friends", null);
        return cursor;

    }
}
