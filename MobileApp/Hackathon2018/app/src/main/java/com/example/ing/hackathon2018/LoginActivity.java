package com.example.ing.hackathon2018;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login;
    private ImageView record_view;
    private String uid;
    String voiceStoragePath;
    private MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    private boolean isRecordActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }



    private void init(){
        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        String result = "";
        result = sharedPref.getString(getString(R.string.username), "");
        Log.i("username","original username " + result);

        EditText usernameEditText = (EditText)findViewById(R.id.et_login_username);
        usernameEditText.setText(result);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);

                EditText usernameEditText = (EditText)findViewById(R.id.et_login_username);
                String username = usernameEditText.getText().toString();


                SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.username), username);
                editor.commit();
                Log.i("username",username);
                startActivity(intent);
            }
        });

        isRecordActive = false;

        record_view = (ImageView) findViewById(R.id.imgview_record);
        record_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                uid = prefs.getString("userId", "323");

                File audioVoice = new File("/mnt/sdcard/hackathon/recordings/voices/");


                if(!audioVoice.exists()){
                    audioVoice.mkdir();
                }
//        voiceStoragePath = "/mnt/sdcard/hackathon/" + String.valueOf(step) + ".wav";
                voiceStoragePath = "/mnt/sdcard/hackathon/rec.wav";

                if(!isRecordActive){
                    isRecordActive = true;
                    if(mediaRecorder == null){
                        initializeMediaRecord();
                    }
                    startAudioRecording();
                }else {
                    isRecordActive = false;
                    stopAudioRecording();
                }

                if (!isRecordActive) {
                    new Thread(new Runnable(){

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

                                Request request = new Request.Builder()
//                    .url("http://10.1.3.207:8088/api/register")
                                        .url("http://10.1.4.48:8088/api/login/" + uid.replaceAll("\"", ""))
                                        .post(RequestBody.create(JSON, Base64.encodeToString(bytes, 0)))
                                        .build();
                                Response response = client.newCall(request).execute();
                                Response resultId = response.networkResponse();
                                System.out.println("got: " + resultId.toString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                    }).start();
                }

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
