package com.example.medicinedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbConnection extends SQLiteOpenHelper {
    //Step 6: We have to overide the onCreate(), create its constructor
    //To override, press "CTRL+O" and select SQLiteOpenHelper (first option)
    //Erase ehatever is displayed and retain only one class "Context"

    public DbConnection(Context context) {
        super(context, "MedicineDBase", null, 1);
    }

    //STEP 7: Now, override two functions viz: onCreate and onUpgrade

    @Override
    public void onCreate(SQLiteDatabase dbase) {
        dbase.execSQL("create Table MedTable(medicineName TEXT primary key, date TEXT, time TEXT)");//Creates the table
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbase, int i, int i1) { //NOTE: i=oldVersion, i1=newVersion

    }
    //STEP 8: Write what should happen next
    public boolean insertValues(String medName, String medDate, String medTime){
        SQLiteDatabase database = this.getWritableDatabase();//Sort of Opens the database for read/write
        ContentValues contentValues = new ContentValues();//Stores the set of values ie,name, date & time
        contentValues.put("medicineName",medName); //Uses KEY-Value Pair to store those values
        contentValues.put("date",medDate);
        contentValues.put("time",medTime);
        long result = database.insert("MedTable",null,contentValues);
        if(result==-1)//meaning that the values where not inserted..
            return false;
        else
            return true;//Returns this if inserted, else FALSE
        //Remember to call this fuction in MainActivity.java....go over there now
    }
    //For RETRIEVING / Fetching Data, we'll use Cursor type ...explain the meaning and why we need it
    //Cursor: an object used to return values...we'll fetch the data based on "date and time" of the day
    public Cursor RetrieveData(String date, String time){
        //Open the Database in Readable Mode
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from MedTable where date= '"+date+"' AND time='"+time+"'", null);
        return cursor;
        //Go BACK and Update MainActivity.java to update the database
        //Make Sure you refer the fetchButton
    }


}
