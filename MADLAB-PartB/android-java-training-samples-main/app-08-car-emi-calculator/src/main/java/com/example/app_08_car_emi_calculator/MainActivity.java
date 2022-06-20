package com.example.app_08_car_emi_calculator;

import static com.example.app_08_car_emi_calculator.Constants.VSM_SERVICE_PACKAGE_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import Common.IEmiCalcultorAidlInterface;

public class MainActivity extends AppCompatActivity {

    private IEmiCalcultorAidlInterface mEmiCalculatorService = null;
    private EditText mEdtPrincipalAmount, mEdtInterestRate, mEdtLoanTerm, mEdtDownPayment;
    private TextView mEmiResult;
    private Button mBtnEmiResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, EmiCalculatorService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        bindExternalService();
        initView();
        initClickListener();
    }


    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.w("Suh", "Service connected");
            mEmiCalculatorService = IEmiCalcultorAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private void initView() {
        mEdtPrincipalAmount = findViewById(R.id.edtPrincipleAmount);
        mEdtDownPayment = findViewById(R.id.edtDownPayment);
        mEdtInterestRate = findViewById(R.id.edtInterestRate);
        mEdtLoanTerm = findViewById(R.id.edtLoanTerm);
        mBtnEmiResult = findViewById(R.id.btnResult);
        mEmiResult = findViewById(R.id.txtResult);
    }

    public void bindExternalService() {

        Intent vehicleServiceIntent = new Intent();
        vehicleServiceIntent.setClassName(VSM_SERVICE_PACKAGE_NAME,
                Constants.VSM_SERVICE_PACKAGE_NAME
                        + Constants.VSM_SERVICE_CLASS_NAME);
        vehicleServiceIntent.putExtra(Constants.BINDER_TYPE,
                1);
        if (null == mEmiCalculatorService) {
            boolean serviceConnection = bindService(vehicleServiceIntent, myConnection, Context
                    .BIND_AUTO_CREATE);
            if (serviceConnection) {
                Log.w("Suh", "Service binding success");
            } else {
                Log.w("Suh", "Service binding failed");
            }
        }
    }

    public double getEmiAmount() throws RemoteException {
        double emiAmount = mEmiCalculatorService.getCarEMI(0, 0, 0);
        return emiAmount;
    }

    private void initClickListener() {
        mBtnEmiResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndCalculateEMI();
            }
        });
    }

    private void validateAndCalculateEMI() {
        double principleAmount, downPayment, interestRate;
        int loanTerm;
        String strPrincipleAmount = mEdtPrincipalAmount.getText().toString();
        String strDownPayment = mEdtDownPayment.getText().toString();
        String strInterestRate = mEdtInterestRate.getText().toString();
        String strLoanTerm = mEdtLoanTerm.getText().toString();

        if (null != strPrincipleAmount && !strPrincipleAmount.isEmpty()) {
            principleAmount = Double.parseDouble(strPrincipleAmount);
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Valid Principal Amount", Toast.LENGTH_LONG).show();
            return;
        }
        if (null != strDownPayment && !strDownPayment.isEmpty()) {
            downPayment = Double.parseDouble(strDownPayment);
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Valid DownPayment Amount", Toast.LENGTH_LONG).show();
            return;
        }
        if (null != strInterestRate && !strInterestRate.isEmpty()) {
            interestRate = Double.parseDouble(strInterestRate);
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Valid Interest Rate", Toast.LENGTH_LONG).show();
            return;
        }
        if (null != strLoanTerm && !strLoanTerm.isEmpty()) {
            loanTerm = Integer.parseInt(strLoanTerm);
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Valid Loan Tenure", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            double totalEmi = mEmiCalculatorService.getCarEMI((principleAmount - downPayment), interestRate /12 / 100 , loanTerm);
            DecimalFormat df = new DecimalFormat("0.00");
            mEmiResult.setText("" +  df.format(totalEmi));
        } catch (Exception e) {
            Log.w("suh", "AIDL call error" + e.toString());
        }

    }
}
