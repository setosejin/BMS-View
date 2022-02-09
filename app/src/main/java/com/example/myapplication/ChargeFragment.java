package com.example.myapplication;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ChargeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static Button refresh_button;

    TextView tv_perfect_charge, tv_perfect_discharge, tv_quick_charge, tv_slow_charge, tv_fuel, tv_distance, tv_time;
    //충전 횟수는 쿼리에서 처리 필요
    String perfect_charge, perfect_discharge, quick_charge, slow_charge, fuel, distance, time, temp;

    private LineChart lineChart_soh;
    List<Entry> entryList_soh = new ArrayList<>();

    private ScatterChart scatterChart;
    List<Entry> entryList_temp = new ArrayList<>();

    public ChargeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChargeFragment newInstance(String param1, String param2) {
        ChargeFragment fragment = new ChargeFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String get_chrg = "http://192.168.56.1:80/get_chrg.php";
        URLConnector thread_chrg = new URLConnector(get_chrg);

        thread_chrg.start();
        try{
            thread_chrg.join();
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        JsonObject resultObj_chrg = thread_chrg.getResult();
        JsonArray jsonArray_chrg = new JsonArray();

        jsonArray_chrg = resultObj_chrg.get("chrg").getAsJsonArray();

        quick_charge = jsonArray_chrg.get(0).getAsJsonObject().get("quick_recharge_cnt").toString();
        quick_charge = quick_charge.replace("\"", "");
        quick_charge = quick_charge + "회";
        slow_charge = jsonArray_chrg.get(0).getAsJsonObject().get("slow_recharge_cnt").toString();
        slow_charge = slow_charge.replace("\"", "");
        slow_charge = slow_charge + "회";
        distance = jsonArray_chrg.get(0).getAsJsonObject().get("odometer").toString();
        distance = distance.replace("\"", "");
        distance = distance + "km";
        time = jsonArray_chrg.get(0).getAsJsonObject().get("mvmn_time").toString();
        time = time.replace("\"", "");
        time = time + "시간";

        for(int i = 0; i < 100; i+=10){
            int len2 = jsonArray_chrg.get(i).getAsJsonObject().get("state_of_health").toString().length();
            //line_chart로 그려줄 (속성, 값) entry에 넣어주기
            entryList_soh.add(new Entry((i+10)/10, Float.parseFloat(jsonArray_chrg.get(i).getAsJsonObject().get("state_of_health").toString().substring(1, len2 - 1))));
        }


        String get_btry = "http://192.168.56.1:80/get_btry.php";
        URLConnector btry_thread = new URLConnector(get_btry);

        btry_thread.start();
        try{
            btry_thread.join();
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        JsonObject btry_resultObj = btry_thread.getResult();
        JsonArray btry_jsonArray = new JsonArray();

        btry_jsonArray = btry_resultObj.get("btry").getAsJsonArray();

        temp = btry_jsonArray.get(0).getAsJsonObject().get("btry_mdul_tempr_arr").toString().replace("\"{}", "");
        long count_temp = temp.chars().filter(ch -> ch == ',').count() + 1;
        temp = temp.replace("\"", "");
        temp = temp.replace("{", "");
        temp = temp.replace("}", "");
        String []tokens_temp=temp.split(",");
        for(int i = 0; i < count_temp; i++){
            entryList_temp.add(new Entry(i+1, Float.parseFloat(tokens_temp[i])));
        }

        return inflater.inflate(R.layout.fragment_charge, container, false);
    }


    Fragment f = this;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if(view != null) {
            //실제로 매칭해주고 setText 등등..
            tv_distance = view.findViewById(R.id.distance2);
            tv_distance.bringToFront();
            tv_distance.setText(distance);

            tv_quick_charge = view.findViewById(R.id.quick_charge_num);
            tv_quick_charge.bringToFront();
            tv_quick_charge.setText(quick_charge);

            tv_slow_charge = view.findViewById(R.id.slow_charge_num);
            tv_slow_charge.bringToFront();
            tv_slow_charge.setText(slow_charge);

            tv_fuel = view.findViewById(R.id.fuel2);
            tv_fuel.bringToFront();
            tv_fuel.setText("6.1km/kWh"); // 배터리 용량 필요

            tv_time = view.findViewById(R.id.time2);
            tv_time.bringToFront();
            tv_time.setText((int) Float.parseFloat(time));

            refresh_button = view.findViewById(R.id.charge_refresh);
            refresh_button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Btn Clicked");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(f).attach(f).commit();
                    System.out.println("Refreshed");
                }
            }) ;

            //lineChart
            lineChart_soh = (LineChart) view.findViewById(R.id.line_chart_soh);

            LineDataSet lineDataSet_soh = new LineDataSet(entryList_soh, "시간에 따른 배터리 수명 추이");

            lineDataSet_soh.setLineWidth(2);
            lineDataSet_soh.setCircleRadius(3);
            lineDataSet_soh.setCircleColor(Color.parseColor("#3C8F2C"));
            lineDataSet_soh.setColor(Color.parseColor("#3C8F2C"));
            lineDataSet_soh.setDrawCircleHole(true);
            lineDataSet_soh.setDrawCircles(true);
            lineDataSet_soh.setDrawHorizontalHighlightIndicator(false);
            lineDataSet_soh.setDrawHighlightIndicators(false);
            lineDataSet_soh.setDrawValues(false);


            LineData lineData_soh = new LineData();
            lineData_soh.addDataSet(lineDataSet_soh);
            lineChart_soh.setData(lineData_soh);


            XAxis xAxis_soh = lineChart_soh.getXAxis();
            xAxis_soh.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis_soh.setTextColor(Color.BLACK);
            xAxis_soh.enableGridDashedLine(8, 24, 0);
            YAxis yLAxis_soh = lineChart_soh.getAxisLeft();
            yLAxis_soh.setTextColor(Color.BLACK);
            YAxis yRAxis_soh = lineChart_soh.getAxisRight();
            yRAxis_soh.setDrawLabels(false);
            yRAxis_soh.setDrawAxisLine(false);
            yRAxis_soh.setDrawGridLines(false);

            lineChart_soh.setDoubleTapToZoomEnabled(false);
            lineChart_soh.setDrawGridBackground(false);
            lineChart_soh.animateY(20, Easing.EasingOption.EaseInCubic);

            lineChart_soh.invalidate();

            //scatter chart
            scatterChart = (ScatterChart) view.findViewById(R.id.scatter_tempr);

            ScatterDataSet scatterDataSet = new ScatterDataSet(entryList_temp, "배터리 모듈의 온도 분포도");
            scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            scatterDataSet.setColor(Color.parseColor("#D0B25E"));
            ArrayList<IScatterDataSet> dataList = new ArrayList<IScatterDataSet>();
            dataList.add(scatterDataSet);

            ScatterData scatterData = new ScatterData(dataList);
            scatterChart.setData(scatterData);
        }
    }
}