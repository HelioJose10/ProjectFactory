package com.example.techhelm.downloadtasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class loginbackgroung extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try{

            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();
            while(data != -1)
            {
                char current = (char)data;
                result += current;
                data = reader.read();
            }

            JSONObject jsonObject = new JSONObject(result);
            //String logininfo = jsonObject.getString(result);
            //JSONArray arr = new JSONArray(result);
            Log.i("", urls[0]);

            return jsonObject;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}