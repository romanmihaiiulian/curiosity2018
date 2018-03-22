package com.example.ing.hackathon2018;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Base64;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RecordActivity extends AppCompatActivity {

    private Button btn_record, btn_replay, btn_next;
    private ProgressBar progressBar;
    private boolean isRecordActive;
    private static int step;
    String result = "";
    private boolean isError;

    private MediaRecorder mediaRecorder;
    String voiceStoragePath;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Bundle extras = getIntent().getExtras();
        result = extras.getString("userId");
        step = 1;

        File audioVoice = new File("/mnt/sdcard/hackathon/recordings/voices/");

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setMax(3);
        progressBar.setProgress(1);

        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }
//        voiceStoragePath = "/mnt/sdcard/hackathon/" + String.valueOf(step) + ".wav";
        voiceStoragePath = "/mnt/sdcard/hackathon/rec.wav";
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
                System.out.println(step);
                sendRecording();
                if (!isError) {
                    step += 1;
                    progressBar.setProgress(step);
                    btn_record.setEnabled(true);
                    if (step == 3) {
                        btn_next.setText("FINISH");
                    }
                    if (step == 4) {
                        Toast.makeText(getApplicationContext(), "Successfully registered your voice", Toast.LENGTH_SHORT).show();
                    }
                    btn_next.setEnabled(false);
                    btn_replay.setEnabled(false);
                } else {
                    Toast.makeText(getApplicationContext(), "Please record your voice again", Toast.LENGTH_SHORT).show();
                    btn_record.setEnabled(true);
                    btn_next.setEnabled(false);
                    btn_replay.setEnabled(false);
                }
            }
        });
    }

    private void sendRecording() {
//        SharedPreferences sharedPref = RecordActivity.this.getPreferences(Context.MODE_PRIVATE);
//
//        result = sharedPref.getString("userId", "");
//        System.out.println(result);
        Thread t = new Thread(new Runnable(){

            @Override

            public void run() {
                try {

                    File file = new File("/mnt/sdcard/hackathon/rec.wav");
                    byte[] bytes = new byte[0];
                    try {
                        bytes = FileUtils.readFileToByteArray(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    MediaType JSON
                            = MediaType.parse("application/json; charset=utf-8");

                    OkHttpClient client = new OkHttpClient();

                    System.out.println(result);
                    Request request = new Request.Builder()
//                    .url("http://10.1.3.207:8088/api/enroll/usr_e9474a4e87b64448bd2bc2ce18def910")
                            .url("http://10.1.4.48:8088/api/enroll/" + result.replaceAll("\"", ""))
                            .post(RequestBody.create(JSON, Base64.encodeToString(bytes, 0)))
                            .build();
                    Response response = client.newCall(request).execute();
                    Response resultId = response.networkResponse();
                    if (response.body().string().contains("Fail")) {
                        isError = true;
                    } else {
                        isError = false;
                    }
                    System.out.println("got: " + resultId.toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
