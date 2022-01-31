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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    TextView textView;
    String charge_percent;
    Button refresh_button;

    //FragmentTransaction ft = getFragmentManager().beginTransaction();


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

        charge_percent = new String();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String test = "http://192.168.56.1:80/get_soc.php";
        URLConnector thread = new URLConnector(test);

        thread.start();
        try{
            thread.join();
            //System.out.println("waiting... for result");
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        JsonObject resultObj = thread.getResult();
        JsonArray jsonArray = new JsonArray();

        jsonArray = resultObj.get("soc").getAsJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            System.out.println("TEST: "+jsonArray.get(i));
            System.out.println((jsonArray.get(i).getAsJsonObject().get("soc_real").toString()));
            charge_percent = jsonArray.get(i).getAsJsonObject().get("soc_real").toString();
        }


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    Fragment f = this;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if(view != null) {
            //실제로 매칭해주고 setText 등등..
            refresh_button = view.findViewById(R.id.updateBtn);
            refresh_button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Btn Clicked");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(f).attach(f).commit();
                    System.out.println("Refreshed");
                }
            }) ;

            textView = view.findViewById(R.id.charge_status);
            textView.setText(charge_percent);

            ArrayList colors = new ArrayList();

            PieChart pieChart = view.findViewById(R.id.charge_chart);
            ArrayList charge_per = new ArrayList();
            float c_p = 0;
            charge_percent = charge_percent.substring(1, charge_percent.length() - 1);
            System.out.println(charge_percent);
            c_p = Float.parseFloat(charge_percent);
            System.out.println("Float: " + c_p);

            if(c_p > 80){
                colors.add(Color.parseColor("#0000ff"));
            }
            else if(c_p > 50){
                colors.add(Color.parseColor("#00ff00"));
            }
            else if(c_p > 20){
                colors.add(Color.parseColor("#ffff00"));
            }
            else{
                colors.add(Color.parseColor("#ff0000"));
            }
            colors.add(Color.parseColor("#ffffff"));


            charge_per.add(new PieEntry(c_p, 0));
            charge_per.add(new PieEntry(100-c_p, 1));
            ArrayList charge_amount = new ArrayList<String>();
            charge_amount.add("충전량");
            charge_amount.add("");

            PieDataSet dataSet = new PieDataSet(charge_per, "l");
            dataSet.setColors(colors);
            PieData data = new PieData(dataSet);

            pieChart.setData(data);
        }
    }
}