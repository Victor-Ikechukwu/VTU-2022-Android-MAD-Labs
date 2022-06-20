package com.example.app_08_car_emi_calculator;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import Common.IEmiCalcultorAidlInterface;


public class EmiCalculatorService extends Service {
    public EmiCalculatorService() {

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        Intent intent1 =new Intent(this, MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent1,0);
        Notification notification= new NotificationCompat.Builder(this,"ChannelId1").setContentTitle("Service application")
                .setContentText("Application Running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();
        startForeground(1,notification);

        return  START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O);
        {
            NotificationChannel notificationChannel= new NotificationChannel(
                    "ChannelId1", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }
    private final IEmiCalcultorAidlInterface.Stub binder= new IEmiCalcultorAidlInterface.Stub() {
        @Override
        public double getCarEMI(double principalAmount, double interestRate, int loanTenure) throws RemoteException {
            double emi =  (principalAmount) * (interestRate) *((Math.pow(1 + interestRate, loanTenure)) / ((Math.pow(1 + interestRate, (loanTenure ))) - 1));
            return emi;
        }

        @Override
        public IBinder asBinder() {
            return super.asBinder();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
       return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}