package com.example.ing.hackathon2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        new Thread(new Runnable(){

            @Override

            public void run() {
                register();
            }

        }).start();
    }

    public String register() {
        try {
            MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
//                    .url("http://10.1.3.207:8088/api/register")
                    .url("http://10.1.4.48:8088/api/register")
                    .post(RequestBody.create(JSON, ""))
                    .build();
            Response response = client.newCall(request).execute();
            String resultId = response.body().string();
            System.out.println("got: " + resultId);
            return resultId;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       return "";
    }

    private void init(){
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                startActivity(intent);
            }
        });
    }


}
