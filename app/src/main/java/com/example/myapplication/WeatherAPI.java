package com.example.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

//    private String nx = "37.47156575506914";	//위도 37.47156575506914
//    private String ny = "127.02938828426859";	//경도 127.02938828426859
//    private String baseDate = "20220209";	//조회하고싶은 날짜
//    private String baseTime = "0500";	//조회하고싶은 시간
//	  참고문서에 있는 url주소
//    String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
//    홈페이지에서 받은 키
//    String serviceKey = "kT4NFde5ytWji1AClynuHY7JxhhwtwH2DLZk7XLaQrmPQjOL%2B%2FDxDPP%2B%2BF7myC1hBC7NIVYvsGAoAtDYMxHTsg%3D%3D";

public class WeatherAPI extends Thread{
    public static String tomorrowTmp;
    public void func() throws IOException, JSONException {
        String now = getDate();
        Float d = Float.parseFloat(now.substring(0, 4) + now.substring(5, 7) + now.substring(8, 10)) + 1;
        String endPoint =  "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/";
        String serviceKey = "kT4NFde5ytWji1AClynuHY7JxhhwtwH2DLZk7XLaQrmPQjOL%2B%2FDxDPP%2B%2BF7myC1hBC7NIVYvsGAoAtDYMxHTsg%3D%3D";
        String pageNo = "1";
        String numOfRows = "10";
        String baseDate = d.toString(); //원하는 날짜 = 내일
        String baseTime = "0200"; //원하는 시간
        String nx = "38"; //위경도임.
        String ny = "127"; //위경도 정보는 api문서 볼 것

        String s = endPoint+"getVilageFcst?serviceKey="+serviceKey
                +"&pageNo=" + pageNo
                +"&numOfRows=" + numOfRows
                +"+&dataType=JSON"
                +"&base_date=" + baseDate
                +"&base_time="+baseTime
                +"&nx="+nx
                +"&ny="+ny;

        URL url = new URL(s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader bufferedReader = null;
        if(conn.getResponseCode() == 200) {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }else{
            //connection error :(
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        String result= stringBuilder.toString();
        conn.disconnect();

        JSONObject mainObject = new JSONObject(result);
        JSONArray itemArray = mainObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
        for(int i=0; i<itemArray.length(); i++){
            JSONObject item = itemArray.getJSONObject(i);
            String category = item.getString("category");
            String value = item.getString("fcstValue");

            if(category == "TMP"){
                tomorrowTmp = value;
            }
//            System.out.println("["+category+"  "+value+"]");
        }

    }

    private String getDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String gettime = dateFormat.format(date);
        return gettime;
    }
}
