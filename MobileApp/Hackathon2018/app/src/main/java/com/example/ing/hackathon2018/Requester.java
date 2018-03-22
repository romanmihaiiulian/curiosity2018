package com.example.ing.hackathon2018;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Created by eddie on 22.03.2018.
 */

public abstract class Requester extends AsyncTask<String,Void,Bitmap>

{

    @Override

    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override

    protected void onPostExecute(Bitmap bitmap) {

        super.onPostExecute(bitmap);

    }

//    @Override
//
//    protected Bitmap doInBackground(String... params) {
//
////        return result;
//
//    }

}

