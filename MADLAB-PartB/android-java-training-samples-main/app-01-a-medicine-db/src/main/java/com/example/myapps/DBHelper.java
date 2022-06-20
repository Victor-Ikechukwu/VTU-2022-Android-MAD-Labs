package com.example.myapps;

import static android.os.Build.ID;
import static com.example.myapps.Constants.*;
import static com.example.myapps.Constants.DATABASE_NAME;
import static com.example.myapps.Constants.MEDICINE_TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "create table " + MEDICINE_TABLE_NAME +
                "(" +
                MEDICINE_COLUMN_ID + " integer primary key, " +
                MEDICINE_COLUMN_MEDICINE_NAME + " text, " +
                MEDICINE_COLUMN_DATE + " text, " +
                MEDICINE_COLUMN_TIME_OF_THE_DAY + " text)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  MEDICINE_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMedicine (String medicineName, String date, int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDICINE_COLUMN_MEDICINE_NAME, medicineName);
        contentValues.put(MEDICINE_COLUMN_DATE, date);
        contentValues.put(MEDICINE_COLUMN_TIME_OF_THE_DAY, time);
        db.insert(MEDICINE_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from " + MEDICINE_TABLE_NAME;
        Cursor cursor =  db.rawQuery(sqlQuery,null, null );
        return cursor;
    }

    public boolean updateMedicine (Integer id, String medicineName, String date, int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDICINE_COLUMN_MEDICINE_NAME, medicineName);
        contentValues.put(MEDICINE_COLUMN_DATE, date);
        contentValues.put(MEDICINE_COLUMN_TIME_OF_THE_DAY, time);
        db.update(MEDICINE_TABLE_NAME, contentValues, ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteMedicine (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MEDICINE_TABLE_NAME,
                ID + " = ? ",
                new String[] { Integer.toString(id) });
    }
}