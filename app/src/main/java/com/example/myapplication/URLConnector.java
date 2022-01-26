package com.example.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class URLConnector extends Thread {

    private String result;
    private String URL;

    public URLConnector(String url){
        URL = url;
    }

    @Override
    public void run() {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if (conn != null) {

                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                System.out.println("CONN" + conn);
                conn.connect();

                int resCode = conn.getResponseCode();
                System.out.println("RESCODE" + resCode);
                if (resCode == HttpURLConnection.HTTP_OK) { //200

//                    //InputStream으로 읽어들인다.
//                    InputStream response = httpCon.getInputStream();
//                    InputStreamReader inputStreamReader = new InputStreamReader(response, "UTF-8");
//                    //버퍼 리더로 변환
//                    BufferedReader bf = new BufferedReader(inputStreamReader);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    System.out.println("READER : " + reader);

                    String line;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }
                    reader.close();
                }
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = output.toString();
    }

    public String getResult(){
        return result;
    }

//    private String request(String urlStr) {
//        StringBuilder output = new StringBuilder();
//        try {
//            URL url = new URL(urlStr);
//            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//            if (conn != null) {
//                conn.setConnectTimeout(10000);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                int resCode = conn.getResponseCode();
//                if (resCode == HttpURLConnection.HTTP_OK) {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
//                    System.out.println("READER : " + reader);
//                    String line = null;
//                    while(true) {
//                        line = reader.readLine();
//                        if (line == null) {
//                            break;
//                        }
//                        output.append(line + "\n");
//                    }
//                    reader.close();
//                }
//                conn.disconnect();
//            }
//        } catch(Exception ex) {
//            Log.e("SampleHTTP", "Exception in processing response.", ex);
//            ex.printStackTrace();
//        }

//        return output.toString();
//    }
}