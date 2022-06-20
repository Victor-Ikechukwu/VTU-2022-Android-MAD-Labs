package com.example.myapps;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEdtMedicine;
    private EditText mEdtDate;
    private Spinner mSpnTime;
    private Button mBtnInsert;
    private DBHelper mDbHelper;
    private TextView mTxtDbContent;
    private Button mBtnRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
        setClickListener();
        setTimeSlotDropdownData();
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

    private void setTimeSlotDropdownData() {
        final List<String> dropdownItems = new ArrayList<>();
        dropdownItems.add("Morning");
        dropdownItems.add("Afternoon");
        dropdownItems.add("Evening");
        dropdownItems.add("Night");

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dropdownItems);

        mSpnTime.setAdapter(spinnerArrayAdapter);
    }

    private void insertDataIntoDb() {
        String medicineName = mEdtMedicine.getText().toString();
        String date = mEdtDate.getText().toString();
        int timeOfTheDa = mSpnTime.getSelectedItemPosition();
        boolean status = mDbHelper.insertMedicine(medicineName, date, timeOfTheDa);
        if (status) {
            setDefaultValues();
            Toast.makeText(this, "Data inserted Successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Data insert Failed", Toast.LENGTH_LONG).show();
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
                int columnIndexId = cursor.getColumnIndex("id");
                String id = cursor.getString(columnIndexId);

                int indexMedicineName = cursor.getColumnIndex("medicine_name");
                String medicineName = cursor.getString(indexMedicineName);
                int indexDate = cursor.getColumnIndex("date");
                String date = cursor.getString(indexDate);
                int indexTime = cursor.getColumnIndex("time");
                String time = cursor.getString(indexTime);

                strBuild.append(id)
                        .append("\t")
                        .append(medicineName)
                        .append("\t")
                        .append(date)
                        .append("\t")
                        .append(time)
                        .append("\n");

                cursor.moveToNext();
            }
            mTxtDbContent.setText(strBuild);
        } else {
            mTxtDbContent.setText("No Records Found");
        }
    }
}