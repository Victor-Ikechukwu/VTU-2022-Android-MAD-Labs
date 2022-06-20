package com.example.myapps;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DatePickerActivity extends AppCompatActivity {

    private EditText mEdtMedicine;
    private EditText mEdtDate;
    private Spinner mSpnTime;
    private Button mBtnInsert;
    private int mYear, mMonth, mDay;
    private DBHelper mDbHelper;
    private TextView mTxtDbContent;
    private Button mBtnRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
        setClickListener();
        setTimeSpinnerData();
        initializeDatabase();
    }

    private void initializeView() {
        mEdtMedicine = findViewById(R.id.edtMedicineName);
        mEdtDate = findViewById(R.id.edtDate);
        mSpnTime = findViewById(R.id.spnTimeOfTheDay);
        mBtnInsert = findViewById(R.id.btnInsert);
        mTxtDbContent = findViewById(R.id.txtDbContent);
        mBtnRead = findViewById(R.id.btnRead);

    }

    private void initializeDatabase() {
        mDbHelper = new DBHelper(this);
    }

    private void setClickListener() {
        mEdtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        mBtnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataIntoDb();
            }
        });

        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readDataFromDatabase();
            }
        });
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mEdtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void setTimeSpinnerData() {
        final List<String> plantsList = new ArrayList<>(Arrays.asList(Constants.TIME_OF_THE_DAY_LIST));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, plantsList);
        //spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
        mSpnTime.setAdapter(spinnerArrayAdapter);
    }

    private void insertDataIntoDb() {
        String medicineName = mEdtMedicine.getText().toString();
        String date = mEdtDate.getText().toString();
        int timeOfTheDa= mSpnTime.getSelectedItemPosition();
        boolean status = mDbHelper.insertMedicine(medicineName, date,timeOfTheDa);
        if(status) {
            setDefaultValues();
            Toast.makeText(this, "Data updated Successfully",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Data updated Failed",Toast.LENGTH_LONG).show();
        }
    }

    private void setDefaultValues() {
        mEdtMedicine.getText().clear();
        mEdtDate.getText().clear();
        mSpnTime.setSelection(0);
    }

    private void readDataFromDatabase() {
        if (mDbHelper == null) {
            initializeDatabase();
        }
        Cursor cursor = mDbHelper.getData();

        // iteration of the cursor
        // to print whole table
        if (cursor.moveToFirst()) {
            StringBuilder strBuild = new StringBuilder();
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String medicineName = cursor.getString(cursor.getColumnIndex("medicine_name"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));

                strBuild.append("\n" + id + "-" +medicineName + "-" + date + "-" + time);
                cursor.moveToNext();
            }
            mTxtDbContent.setText(strBuild);
        } else {
            mTxtDbContent.setText("No Records Found");
        }
    }
}