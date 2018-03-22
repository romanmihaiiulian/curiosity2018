package com.example.ing.hackathon2018;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Looper;
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
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    private boolean isError;

    HashMap<String, String> hashMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Put elements to the hashMap
        hashMap.put("af123456", "usr_e9474a4e87b64448bd2bc2ce18def910");
        hashMap.put("af123457", "usr_5746f73caa9a40e3a43ed00a327a2492");
        hashMap.put("mr123456", "usr_e9474a4e87b64448bd2bc2ce18def910");
        hashMap.put("ep123456", "usr_221ebde3c6d443089c498ab9feb8364a");
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
                EditText usernameEditText = (EditText)findViewById(R.id.et_login_username);
                String username = usernameEditText.getText().toString();

                uid = hashMap.get(username);
              //  uid = "\"" + uid + "\"";
                Log.i("uid", uid + " af123");

               if(uid == null || uid.equals("")){
                    SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    uid = prefs.getString("userId", "323");

                Log.i("uid", uid + " af123");
              }
                File audioVoice = new File("/mnt/sdcard/hackathon/");

                if(!audioVoice.exists()){
                    audioVoice.mkdir();
                }
                voiceStoragePath = "/mnt/sdcard/hackathon/rec.wav";

                if(!isRecordActive){
                    isRecordActive = true;
                    record_view.setImageResource(R.drawable.stop);
                    if(mediaRecorder == null){
                        initializeMediaRecord();
                    }
                    startAudioRecording();
                    //record_view.setBackgroundColor(Color.RED);
                }else {
                    record_view.setImageResource(R.drawable.ic_register);
                    isRecordActive = false;
                    stopAudioRecording();
                    record_view.setBackgroundColor(Color.WHITE);
                }

                if (!isRecordActive) {
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

                                Request request = new Request.Builder()
//                                        .url("http://10.1.3.207:8088/api/login/usr_e9474a4e87b64448bd2bc2ce18def910")
                                        .url("http://10.1.3.207:8088/api/login/" + uid.replaceAll("\"", ""))
                                        .post(RequestBody.create(JSON, Base64.encodeToString(bytes, 0)))
                                        .build();

                                Response response = client.newCall(request).execute();
                                isError = true;
                                if (response.body().string().contains("SUCC")) {
                                    isError = false;
                                }


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
                    if (isError) {
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
//                        intent.putExtra("isError", isError);
                    } else {
//                        intent.putExtra("isError", isError);
                        Toast.makeText(getApplicationContext(), "Authentication successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(intent);
                    }
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
