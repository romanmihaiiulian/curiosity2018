package com.example.ing.hackathon2018;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;


public class RecordActivity extends AppCompatActivity {

    private Button btn_record, btn_replay, btn_next;
    private ProgressBar progressBar;
    private boolean isRecordActive;
    private static int step;

    private MediaRecorder mediaRecorder;
    String voiceStoragePath;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        step = 1;

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setMax(3);
        progressBar.setProgress(1);

        File audioVoice = new File("/mnt/sdcard/hackathon/");
        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }
        voiceStoragePath = "/mnt/sdcard/hackathon/" + String.valueOf(step) + ".wav";
        System.out.println("Audio path : " + voiceStoragePath);

        btn_record = (Button)findViewById(R.id.btn_record);
        btn_replay = (Button)findViewById(R.id.btn_replay);
        btn_next = (Button)findViewById(R.id.btn_next);

        btn_replay.setEnabled(false);
        btn_next.setEnabled(false);
        isRecordActive = false;

        initializeMediaRecord();

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRecordActive){
                    isRecordActive = true;
                    btn_replay.setEnabled(false);
                    btn_next.setEnabled(false);
                    btn_record.setText("STOP");
                    if(mediaRecorder == null){
                        initializeMediaRecord();
                    }
                    startAudioRecording();
                }else {
                    btn_record.setText("RECORD");
                    isRecordActive = false;
                    stopAudioRecording();
                    btn_replay.setEnabled(true);
                    btn_next.setEnabled(true);
                }
            }
        });

        btn_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLastStoredAudioMusic();
                mediaPlayerPlaying();
                btn_record.setEnabled(true);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step += 1;
                progressBar.setProgress(step);
                btn_record.setEnabled(true);
                if (step == 3) {
                    btn_next.setText("FINISH");
                }
                btn_next.setEnabled(false);
                btn_replay.setEnabled(false);
            }
        });
    }

    private void startAudioRecording(){
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopAudioRecording(){
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void playLastStoredAudioMusic(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(voiceStoragePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        btn_record.setEnabled(false);
        btn_replay.setEnabled(false);
    }

    private void stopAudioPlay(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void mediaPlayerPlaying(){
        if(!mediaPlayer.isPlaying()){
            stopAudioPlay();
        }
    }

    private void initializeMediaRecord(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(voiceStoragePath);
    }

}
