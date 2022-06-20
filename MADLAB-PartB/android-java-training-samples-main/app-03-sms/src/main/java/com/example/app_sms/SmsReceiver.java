package com.example.app_sms;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    public static final String KEY_SMS_FROM = "SMS_FROM";
    public static final String KEY_SMS_BODY = "SMS_BODY";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Log.d("SMS", msg_from);
                        Log.d("SMS", msgBody);
                        Toast.makeText(context, "SMS from" + msg_from, Toast.LENGTH_SHORT).show();

                    }
                    Intent activityIntent = new Intent(context, MainActivity.class);
                    activityIntent.putExtra(KEY_SMS_FROM, msgs[0].getOriginatingAddress());
                    activityIntent.putExtra(KEY_SMS_BODY, msgs[0].getMessageBody());
                    activityIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityIntent);
                } catch (Exception e) {
                    Log.e("Exception caught", e.getMessage());
                }
            }
        }
    }
}
