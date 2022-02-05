package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView car_now;
    TextView charge_status;
    TextView key_status;
    TextView tvDate;
    TextView textView;
    TextView fuel;
    TextView distance;
    TextView time;

    String charge_percent;
    String safety_grade;
    String charge;
    String key;
    String fuel_;
    String distance_;
    String time_;

    public static Button refresh_button;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String get_soc = "http://192.168.56.1:80/get_soc.php";
        URLConnector soc_thread = new URLConnector(get_soc);

        soc_thread.start();
        try{
            soc_thread.join();
            //System.out.println("waiting... for result");
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        JsonObject soc_resultObj = soc_thread.getResult();
        JsonArray soc_jsonArray = new JsonArray();

        soc_jsonArray = soc_resultObj.get("soc").getAsJsonArray();
        for(int i = 0; i < soc_jsonArray.size(); i++){
            //System.out.println("TEST: "+soc_jsonArray.get(i));
            //System.out.println((soc_jsonArray.get(i).getAsJsonObject().get("soc_real").toString()));
            charge_percent = soc_jsonArray.get(i).getAsJsonObject().get("soc_real").toString();
            charge_percent = charge_percent.replace("\"", "");
            charge_percent = charge_percent + "%";
            if(soc_jsonArray.get(i).getAsJsonObject().get("charge_status").getAsInt() == 0){
                charge = "충전 없음";
            }
            else{
                charge = "충전중";
            }
            if(soc_jsonArray.get(i).getAsJsonObject().get("key_status").getAsInt() == 0){
                key = "시동 OFF";
            }
            else{
                key = "시동 ON";
            }
            safety_grade = soc_jsonArray.get(i).getAsJsonObject().get("safety_grade").toString();

//            distance_ = soc_jsonArray.get(i).getAsJsonObject().get("odometer").toString();
//            time_ = soc_jsonArray.get(i).getAsJsonObject().get("mvmn_time").toString();
            distance_ = "69.3";
            time_ = "71";
            safety_grade = safety_grade.replace("\"", "");

            distance_ = distance_.replace("\"", "");
            distance_ = distance_ + "km";
            time_ = time_.replace("\"", "");
            time_ = time_ + "분";
        }
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    Fragment f = this;
    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String gettime = dateFormat.format(date);
        return gettime;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if(view != null) {
            //실제로 매칭해주고 setText 등등..
            tvDate = (TextView) view.findViewById(R.id.update_time);
            tvDate.setText(getTime()); // 버튼 클릭 이벤트 정의
            refresh_button = view.findViewById(R.id.updateBtn);

            refresh_button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Btn Clicked");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(f).attach(f).commit();
                    tvDate.setText(getTime());
                    System.out.println("Refreshed");
                }
            }) ;

            textView = view.findViewById(R.id.charge_percent);
            textView.bringToFront();
            textView.setText(charge_percent);

            charge_status = view.findViewById(R.id.car_charge_status);
            charge_status.bringToFront();
            charge_status.setText(charge);

            key_status = view.findViewById(R.id.car_key_status);
            key_status.bringToFront();
            key_status.setText(key);

            car_now = view.findViewById(R.id.my_car_now);
            car_now.bringToFront();
            car_now.setText(safety_grade);

            distance = view.findViewById(R.id.distance);
            distance.setText(distance_);

            time = view.findViewById(R.id.time);
            time.setText(time_);

            fuel = view.findViewById(R.id.fuel);
            fuel.setText("6.1km/kWh");

            ArrayList colors = new ArrayList();

            PieChart pieChart = view.findViewById(R.id.charge_chart);
            pieChart.bringToFront();
            ArrayList charge_per = new ArrayList();
            float c_p = 0;
            charge_percent = charge_percent.substring(0, charge_percent.length() - 1);
//            System.out.println(charge_percent);
            c_p = Float.parseFloat(charge_percent);
//            System.out.println("Float: " + c_p);

            if(c_p > 80){
                colors.add(Color.parseColor("#1682BF"));
            }
            else if(c_p > 60){
                colors.add(Color.parseColor("#3C8F2C"));
            }
            else if(c_p > 40){
                colors.add(Color.parseColor("#D0B25E"));
            }
            else if(c_p > 20){
                colors.add(Color.parseColor("#D6652F"));
            }
            else{
                colors.add(Color.parseColor("#BF5050"));
            }
            colors.add(Color.parseColor("#dddddd"));


            charge_per.add(new PieEntry(c_p, 0));
            charge_per.add(new PieEntry(100-c_p, 1));
//            ArrayList charge_amount = new ArrayList<String>();
//            charge_amount.add("충전량");
//            charge_amount.add("");

            PieDataSet dataSet = new PieDataSet(charge_per, "l");
            dataSet.setColors(colors);
            PieData data = new PieData(dataSet);

            pieChart.setData(data);
        }
    }
}