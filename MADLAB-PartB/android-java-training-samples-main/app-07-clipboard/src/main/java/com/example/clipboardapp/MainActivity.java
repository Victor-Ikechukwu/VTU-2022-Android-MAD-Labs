package com.example.clipboardapp;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private ClipboardManager clipboard;
    private Button copyButton, pasteButton;
    private EditText inputEditText;
    private TextView outputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        copyButton = findViewById(R.id.button_copy);
        pasteButton = findViewById(R.id.button_paste);
        inputEditText = findViewById(R.id.input);
        outputTextView = findViewById(R.id.output);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedText = inputEditText.getText().toString();
                copyToClipboard(typedText);
            }
        });
        pasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputTextView.setText(getClipboardText());
            }
        });
    }

    private void copyToClipboard(String text) {
        clipboard.setText(text);
    }

    private String getClipboardText() {
        return clipboard.getText().toString();
    }
}