package com.example.app_05_media_player;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static int oneTimeOnly = 0;
    private final Handler myHandler = new Handler();
    private final int forwardTime = 5000;
    private final int backwardTime = 5000;
    private Button btnFastForward, btnPause, btnPlay, btnRewind;
    private ImageView iv;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private SeekBar seekbar;
    private final Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnFastForward = findViewById(R.id.ib_ff);
        btnPause = findViewById(R.id.ib_pause);
        btnPlay = findViewById(R.id.ib_play);
        btnRewind = findViewById(R.id.ib_rew);
        iv = findViewById(R.id.iv_art);
//        tx3.setText("song_hum_deewane.mp3");

        mediaPlayer = MediaPlayer.create(this, R.raw.song_hum_deewane);
        seekbar = findViewById(R.id.seekbar);
        seekbar.setClickable(false);
        btnPause.setEnabled(false);
        btnPlay.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();

            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();

            if (oneTimeOnly == 0) {
                seekbar.setMax((int) finalTime);
                oneTimeOnly = 1;
            }

            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(UpdateSongTime, 100);
            btnPause.setEnabled(true);
            btnPlay.setEnabled(false);
        });

        btnPause.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
            mediaPlayer.pause();
            btnPause.setEnabled(false);
            btnPlay.setEnabled(true);
        });

        btnFastForward.setOnClickListener(v -> {
            int temp = (int) startTime;

            if ((temp + forwardTime) <= finalTime) {
                startTime = startTime + forwardTime;
                mediaPlayer.seekTo((int) startTime);
                Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
            }
        });
        btnRewind.setOnClickListener(v -> {
            int temp = (int) startTime;

            if ((temp - backwardTime) > 0) {
                startTime = startTime - backwardTime;
                mediaPlayer.seekTo((int) startTime);
                Toast.makeText(getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
            }
        });
    }
}