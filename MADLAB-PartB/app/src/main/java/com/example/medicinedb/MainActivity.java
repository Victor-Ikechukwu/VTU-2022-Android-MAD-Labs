package com.example.medicinedb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText medicineName,medicineDate;
    TextView textViewMed;
    Button insertButton, fetchButton;
    Spinner dayTimeSpinner;
    Switch swtch;
    DbConnection dbConnection;
    //STEP 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbConnection = new DbConnection(this);

        textViewMed = findViewById(R.id.txtViewMed);
        medicineName = findViewById(R.id.edtTxtMed);
        medicineDate = findViewById(R.id.edtTxtDate);
        insertButton = findViewById(R.id.btnInsert);
        fetchButton = findViewById(R.id.btnFetch);
        fetchButton.setVisibility(View.INVISIBLE);
        dayTimeSpinner = findViewById(R.id.spinner);
        swtch = findViewById(R.id.switcher);
        //STEP 2

        //VIP: Provide functionality for switch to toggle blw InsertData / Fetch Data

        //STEP 3:
        swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Checked whether the switch is checked to enable some components
                if(!isChecked){
                    fetchButton.setVisibility(View.INVISIBLE);
                    insertButton.setVisibility(View.VISIBLE);
                    medicineName.setVisibility(View.VISIBLE);
                    textViewMed.setVisibility(View.VISIBLE);
                }
                else{
                    //Enable some of the buttons and components (Views) as Visible
                    medicineName.setVisibility(View.INVISIBLE);
                    insertButton.setVisibility(View.INVISIBLE);
                    textViewMed.setVisibility(View.INVISIBLE);
                    fetchButton.setVisibility(View.VISIBLE);

                    //End of STEP 3....NOW RUN AND SHOW THEM
                }
            }
        });

        //STEP 4: Create the functionality for INSSERT BUTTON
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = medicineName.getText().toString();
                String date = medicineDate.getText().toString();
                String time = dayTimeSpinner.getSelectedItem().toString();

                //STEP 5: We now have to create a Database and write SQL Queries to
                //..INSERT items into the database as...
                //Create a Class Called DbConnection and overide the methods accordingly

                //After STEP 8, do this STEP 9
                //First create an object of DbConection and initialize above (in onCreate Method)
                boolean insert = dbConnection.insertValues(name,date,time);
                if(insert==true){
                    Toast.makeText(getApplicationContext(),"Data inserted Successfully", Toast.LENGTH_SHORT).show();
                    medicineName.setText(null);
                    medicineDate.setText(null);//This allows us to insert new data
                }
                else //if otherwise
                    Toast.makeText(getApplicationContext(), "Data insertion unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });

        //STEP 10:
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = medicineDate.getText().toString();
                String time = dayTimeSpinner.getSelectedItem().toString();
                String med = "";
                Cursor cu = dbConnection.RetrieveData(date, time);
                //Since the cursor will return more than one row, we need to traverse through the records/rows

                cu.moveToFirst();
                do{
                    //med=med+(String.valueOf(cursor.getString(cursor.getColumnIndex("medicineName"))));
                    med = med+(String.valueOf(cu.getString(cu.getColumnIndex("medicineName"))));
                    med+="\n";
                }while (cu.moveToNext());
                //Once done, PRINT a confirmatory Toast Message
                Toast.makeText(getApplicationContext(), med, Toast.LENGTH_LONG).show();
            }
        });
    }

}