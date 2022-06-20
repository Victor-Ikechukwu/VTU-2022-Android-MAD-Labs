package com.example.app_file_operation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String FILE_NAME = "workshopfile.txt";
    public static final String FILE_DIR = "WorkshopFiles";
    private static final int PERMISSIONS_REQUEST_STORAGE = 10;
    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Button createButton, openButton, saveButton;
    private EditText inputContent;
    private TextView logs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createButton = findViewById(R.id.btn_create);
        openButton = findViewById(R.id.btn_open);
        saveButton = findViewById(R.id.btn_save);
        inputContent = findViewById(R.id.input_content);
        logs = findViewById(R.id.logs);
        requestPermissions();

        createButton.setOnClickListener(v -> {
            createFile();
        });

        openButton.setOnClickListener(v -> {
            openFile();
        });

        saveButton.setOnClickListener(v -> {
            String text = inputContent.getText().toString();
            saveFile(text);
        });
    }

    private void openFile() {
        File sdCard = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File directory = new File(sdCard.getAbsolutePath());
        // create the file in which we will write the contents
        File file = new File(directory, FILE_NAME);
        logs.setText("Opening " + file.getAbsolutePath());
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
            showToast("Error:" + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            inputContent.setText(text + "");
        }
    }

    private void createFile() {
        File sdCard = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File directory = new File(sdCard.getAbsolutePath());
        directory.mkdirs();

        // create the file in which we will write the contents
        File file = new File(directory, FILE_NAME);

        logs.setText("Creating file " + file.getAbsolutePath());
        FileOutputStream os;
        try {
            os = new FileOutputStream(file);
            String data = "This is the content of my file";
            os.write(data.getBytes());
            os.close();
        } catch (IOException e) {
            showToast("Error:" + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            inputContent.setText("");
        }
    }

    private void saveFile(String content) {
        File sdCard = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File directory = new File(sdCard.getAbsolutePath());
        // create the file in which we will write the contents
        File file = new File(directory, FILE_NAME);

        logs.setText("Saving to file " + file.getAbsolutePath());

        FileOutputStream os;
        try {
            os = new FileOutputStream(file);
            os.write(content.getBytes());
            os.close();
        } catch (IOException e) {
            showToast("Error:" + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            inputContent.setText("");
        }
    }

    private void requestPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, permissions[0])
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissions[1])
                        != PackageManager.PERMISSION_GRANTED) {

            // we can request the permission.
            ActivityCompat.requestPermissions(this,
                    permissions,
                    PERMISSIONS_REQUEST_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                showToast("Permission granted!");

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                showToast("Permission denied! App will not function correctly");
            }
            return;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}