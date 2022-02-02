package com.example.myapplication;

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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChargeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    String perfect_charge, perfect_discharge, quick_charge, slow_charge, fuel, distance, time;

    private LineChart lineChart_soc, lineChart_soh;
    List<Entry> entryList_soc = new ArrayList<>();
    List<Entry> entryList_soh = new ArrayList<>();


    public ChargeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChargeFragment.
     */
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
//        textView = (TextView) getView().findViewById(R.id.textView_test);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String get_chrg = "http://192.168.56.1:80/get_chrg.php";
        URLConnector thread_chrg = new URLConnector(get_chrg);

        thread_chrg.start();
        try{
            thread_chrg.join();
            //System.out.println("waiting... for result");
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        JsonObject resultObj_chrg = thread_chrg.getResult();
        JsonArray jsonArray_chrg = new JsonArray();

        jsonArray_chrg = resultObj_chrg.get("chrg").getAsJsonArray();

        quick_charge = jsonArray_chrg.get(0).getAsJsonObject().get("quick_recharge_cnt").toString();
        slow_charge = jsonArray_chrg.get(0).getAsJsonObject().get("slow_recharge_cnt").toString();
        distance = jsonArray_chrg.get(0).getAsJsonObject().get("odometer").toString();
        time = jsonArray_chrg.get(0).getAsJsonObject().get("mvmn_time").toString();

        for(int i = 0; i < 100; i++){
            //System.out.println("TEST: "+jsonArray_chrg.get(i));
            //System.out.println("["+i+"] "+(jsonArray_chrg.get(i).getAsJsonObject().get("state_of_chrg_bms").toString()));
            int len = jsonArray_chrg.get(i).getAsJsonObject().get("state_of_chrg_bms").toString().length();
            int len2 = jsonArray_chrg.get(i).getAsJsonObject().get("state_of_health").toString().length();
            //line_chart로 그려줄 (속성, 값) entry에 넣어주기
            entryList_soc.add(new Entry(i, Float.parseFloat(jsonArray_chrg.get(i).getAsJsonObject().get("state_of_chrg_bms").toString().substring(1, len - 1))));
            entryList_soh.add(new Entry(i, Float.parseFloat(jsonArray_chrg.get(i).getAsJsonObject().get("state_of_health").toString().substring(1, len2 - 1))));
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
//            textView = view.findViewById(R.id.textView_test);
//            textView.setText("modified");
            tv_distance = view.findViewById(R.id.distance2);
            tv_distance.setText(distance);

            tv_quick_charge = view.findViewById(R.id.quick_charge_num);
            tv_quick_charge.setText(quick_charge);

            tv_slow_charge = view.findViewById(R.id.slow_charge_num);
            tv_slow_charge.setText(slow_charge);

            tv_fuel = view.findViewById(R.id.fuel2);
            tv_fuel.setText("6.1km/kWh");

            tv_time = view.findViewById(R.id.time2);
            tv_time.setText(time);

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
            lineChart_soc = (LineChart) view.findViewById(R.id.line_chart_soc);
            lineChart_soh = (LineChart) view.findViewById(R.id.line_chart_soh);

            LineDataSet lineDataSet_soc = new LineDataSet(entryList_soc, "속성명1");
            LineDataSet lineDataSet_soh = new LineDataSet(entryList_soh, "속성명2");

            lineDataSet_soc.setLineWidth(2);
            lineDataSet_soc.setCircleRadius(3);
            lineDataSet_soc.setCircleColor(Color.parseColor("#FFA1B4DC"));
            //lineDataSet_soc.setCircleColorHole(Color.BLUE);
            lineDataSet_soc.setColor(Color.parseColor("#FFA1B4DC"));
            lineDataSet_soc.setDrawCircleHole(true);
            lineDataSet_soc.setDrawCircles(true);
            lineDataSet_soc.setDrawHorizontalHighlightIndicator(false);
            lineDataSet_soc.setDrawHighlightIndicators(false);
            lineDataSet_soc.setDrawValues(false);

            lineDataSet_soh.setLineWidth(2);
            lineDataSet_soh.setCircleRadius(3);
            lineDataSet_soh.setCircleColor(Color.parseColor("#FFA1B4DC"));
            //lineDataSet_soh.setCircleColorHole(Color.BLUE);
            lineDataSet_soh.setColor(Color.parseColor("#FFA1B4DC"));
            lineDataSet_soh.setDrawCircleHole(true);
            lineDataSet_soh.setDrawCircles(true);
            lineDataSet_soh.setDrawHorizontalHighlightIndicator(false);
            lineDataSet_soh.setDrawHighlightIndicators(false);
            lineDataSet_soh.setDrawValues(false);

            LineData lineData_soc = new LineData();
            lineData_soc.addDataSet(lineDataSet_soc);
            lineChart_soc.setData(lineData_soc);

            LineData lineData_soh = new LineData();
            lineData_soh.addDataSet(lineDataSet_soh);
            lineChart_soh.setData(lineData_soh);

            XAxis xAxis = lineChart_soc.getXAxis();
            //xAxis.setLabelCount(10, true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(Color.BLACK);
            xAxis.enableGridDashedLine(8, 24, 0);
            YAxis yLAxis = lineChart_soc.getAxisLeft();
            yLAxis.setTextColor(Color.BLACK);
            YAxis yRAxis = lineChart_soc.getAxisRight();
            yRAxis.setDrawLabels(false);
            yRAxis.setDrawAxisLine(false);
            yRAxis.setDrawGridLines(false);

            lineChart_soc.setDoubleTapToZoomEnabled(false);
            lineChart_soc.setDrawGridBackground(false);
            lineChart_soc.animateY(2000, Easing.EasingOption.EaseInCubic);

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
            lineChart_soh.animateY(2000, Easing.EasingOption.EaseInCubic);

            lineChart_soc.invalidate();
            lineChart_soh.invalidate();


        }
    }
}