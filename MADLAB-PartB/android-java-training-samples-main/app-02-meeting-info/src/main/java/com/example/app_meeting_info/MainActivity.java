package com.example.app_meeting_info;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Uri CONTENT_URI = Uri.parse("content://com.demo.meeting.provider/meetings");
    private EditText mEdtDateInfo;
    Button mBtnSearch;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setClickListener();
    }

    private void initView() {
        mEdtDateInfo = findViewById(R.id.edtInfoDate);
        mBtnSearch = findViewById(R.id.loadButton);
    }

    private void setClickListener() {
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = mEdtDateInfo.getText().toString();
                if (null != date && !date.isEmpty()) {
                    showDetails(view);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mEdtDateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
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
                        mEdtDateInfo.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void showDetails(View view) {
        // inserting complete table details in this text field
        TextView resultView = (TextView) findViewById(R.id.res);

        // creating a cursor object of the
        // content URI
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.demo.meeting.provider/meetings"), null, null, null, null);

        // iteration of the cursor
        // to print whole table
        if (null != cursor && cursor.moveToFirst()) {
            StringBuilder strBuild = new StringBuilder();
            while (!cursor.isAfterLast()) {
                if (cursor.getString(cursor.getColumnIndex("date")).equalsIgnoreCase(mEdtDateInfo.getText().toString())) {
                    strBuild.append("\n" + cursor.getString(cursor.getColumnIndex("date")) + "-" + cursor.getString(cursor.getColumnIndex("time")) + "-" + cursor.getString(cursor.getColumnIndex("agenda")));
                }
                cursor.moveToNext();
            }
            if (null != strBuild && strBuild.length() > 0) {
                resultView.setText(strBuild);
            } else {
                resultView.setText("No Meetings on this date");
            }
        } else {
            resultView.setText("No Records Found");
        }
    }
}