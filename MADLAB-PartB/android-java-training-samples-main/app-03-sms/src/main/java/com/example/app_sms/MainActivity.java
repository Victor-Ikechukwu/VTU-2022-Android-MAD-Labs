package com.example.app_sms;

import static com.example.app_sms.SmsReceiver.KEY_SMS_BODY;
import static com.example.app_sms.SmsReceiver.KEY_SMS_FROM;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    private TextView from, body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        from = findViewById(R.id.tv_from);
        body = findViewById(R.id.tv_body);
        // Request the permission immediately here for the first time run
        requestPermissions(Manifest.permission.RECEIVE_SMS, PERMISSIONS_REQUEST_RECEIVE_SMS);
        if (getIntent().hasExtra(KEY_SMS_FROM)) {
            String smsFrom = getIntent().getStringExtra(KEY_SMS_FROM);
            from.setText(smsFrom);
        }
        if (getIntent().hasExtra(KEY_SMS_BODY)) {
            String smsBody = getIntent().getStringExtra(KEY_SMS_BODY);
            body.setText(smsBody);
        }
    }

    private void requestPermissions(String permission, int requestCode) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showToast("Granting permission is necessary!");

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        requestCode);

                // requestCode is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_RECEIVE_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    /*NotificationUtil.getInstance().show(this, NotificationUtil.CONTENT_TYPE.INFO,
                            getResources().getString(R.string.app_name),*/
                    showToast("Permission granted!");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    /*NotificationUtil.getInstance().show(this, NotificationUtil.CONTENT_TYPE.ERROR,
                            getResources().getString(R.string.app_name),*/
                    showToast("Permission denied! App will not function correctly");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}