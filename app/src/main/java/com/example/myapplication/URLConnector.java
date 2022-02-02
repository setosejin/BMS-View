package com.example.myapplication;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class URLConnector extends Thread {

    private JsonObject result;
    private String URL;

    public URLConnector(String url){
        URL = url;
    }

    @Override
    public void run() {
        StringBuilder output = new StringBuilder();
        JSONArray outputArr = new JSONArray();
        try {
            URL url = new URL(URL);

//            String data = getJSON(URL);
//            AuthMsg msg = new Gson().fromJson(data, AuthMsg.class);
//            System.out.println(msg);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if (conn != null) {

                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
//                System.out.println("CONN " + conn);
                conn.connect();

                int resCode = conn.getResponseCode();
//                System.out.println("RESCODE: " + resCode);
                if (resCode == HttpURLConnection.HTTP_OK) { //200

//                    //InputStream으로 읽어들인다.
//                    InputStream response = httpCon.getInputStream();
//                    InputStreamReader inputStreamReader = new InputStreamReader(response, "UTF-8");
//                    //버퍼 리더로 변환
//                    BufferedReader bf = new BufferedReader(inputStreamReader);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    //System.out.println("READER : " + conn.getInputStream());

                    String line;
                    while(true) {
                        line = reader.readLine();

                        if (line == null) {
                            break;
                        }
                        //System.out.println("line, "+ line.toString());
                        output.append(line + "\n");

                    }

                    String outputStr = String.valueOf(output);
//                    outputStr = outputStr.substring(1, output.length()-2);
//                    outputStr.replace("[", "");
//                    outputStr.replace("]", "");
//                    System.out.println("outputStr: "+outputStr);
                    JsonObject jobj = new Gson().fromJson(outputStr, JsonObject.class);
//                    System.out.println("JsonObject: "+jobj.toString());
                    reader.close();
                    result = jobj;
                }
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public JsonObject getResult(){
        return result;
    }

}