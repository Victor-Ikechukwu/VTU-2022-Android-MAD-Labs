package com.example.app_06_async_task;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mBtnStart;
    private Button mBtnEnd;
    private TextView mTxtBanner;
    private MovingBannerTask movingBannerTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setClickListner();
    }

    private void initView() {
        mBtnStart = findViewById(R.id.btnStart);
        mBtnEnd = findViewById(R.id.btnEnd);
        mTxtBanner = findViewById(R.id.txtBanner);
    }

    private void init() {
        movingBannerTask = null;
        movingBannerTask = new MovingBannerTask(false);
    }

    private void setClickListner() {
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                if (null != movingBannerTask) {
                    movingBannerTask.execute();
                }
            }
        });

        mBtnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != movingBannerTask) {
                    movingBannerTask.setFinish(true);
                }
            }
        });
    }

    private class MovingBannerTask extends AsyncTask<Void, Void, Void> {

        private boolean isFinish;

        public MovingBannerTask(boolean finish) {
            isFinish = finish;
        }

        private void setFinish(boolean flag) {
            isFinish = flag;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTxtBanner.setSelected(true);
            mTxtBanner.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mTxtBanner.setSingleLine(true);
            mBtnStart.setClickable(false);
            mBtnStart.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (!isFinish) {
                Log.w("suh", "flag is:" + isFinish);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            mTxtBanner.setSelected(false);
            mBtnStart.setClickable(true);
            mBtnStart.setEnabled(true);
        }
    }


}