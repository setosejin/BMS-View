package com.example.myapplication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class URLConnectorPost extends Thread {

    private String result;
    private String URL;

    public URLConnectorPost(String url){
        URL = url;
    }

    @Override
    public void run() {
        StringBuilder output = new StringBuilder();
        JSONArray outputArr = new JSONArray();
        try {
            URL url = new URL(URL);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if (conn != null) {

                conn.setRequestMethod("POST");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) { //200
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;

                    String line;
                    while(true) {
                        line = reader.readLine();

                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }

                    String outputStr = String.valueOf(output);
                    reader.close();
                    result = outputStr;
                }
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getResult(){
        return result;
    }

}