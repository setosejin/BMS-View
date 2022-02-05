package com.example.myapplication;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.util.EventLogTags.Description;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView textView;
    String temp, volt;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private PushHistoryAdapter pushHistoryAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<PushHistory> arrayList;
    ArrayList<Entry> list_temp = new ArrayList<>();

    private ScatterChart scatter_temp, scatter_volt;
    private LineChart lineChart_temp, lineChart_volt;
    List<ScatterDataSet> set_temp = new ArrayList<>();
    List<Entry> entryList_temp = new ArrayList<>();
    List<Entry> entryList_volt = new ArrayList<>();

    public static Button refresh_button;

    public StateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StateFragment newInstance(String param1, String param2) {
        StateFragment fragment = new StateFragment();
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
        temp = new String();
        arrayList = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String get_history = "http://192.168.56.1:80/get_push_history.php";
        URLConnector history_thread = new URLConnector(get_history);

        history_thread.start();
        try{
            history_thread.join();
            //System.out.println("waiting... for result");
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        JsonObject resultObj = history_thread.getResult();
        JsonArray jsonArray = new JsonArray();

        jsonArray = resultObj.get("push_history").getAsJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            PushHistory pushHistory = new PushHistory(0,
                    jsonArray.get(i).getAsJsonObject().get("push_type").toString(),
                    jsonArray.get(i).getAsJsonObject().get("send_time").toString(),
                    jsonArray.get(i).getAsJsonObject().get("send_msg").toString());
            arrayList.add(pushHistory);
        }


        String get_btry = "http://192.168.56.1:80/get_btry.php";
        URLConnector btry_thread = new URLConnector(get_btry);

        btry_thread.start();
        try{
            btry_thread.join();
            //System.out.println("waiting... for result");
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        JsonObject btry_resultObj = btry_thread.getResult();
        JsonArray btry_jsonArray = new JsonArray();

        btry_jsonArray = btry_resultObj.get("btry").getAsJsonArray();

        temp = btry_jsonArray.get(0).getAsJsonObject().get("btry_mdul_tempr_arr").toString().replace("\"{}", "");
        long count_temp = temp.chars().filter(ch -> ch == ',').count() + 1;
//        System.out.println(temp);
        for(int i = 0; i < count_temp; i++){
//        entryList_temp.add(new Entry(i, ))
        }
        volt = btry_jsonArray.get(0).getAsJsonObject().get("btry_cells_arr").toString().replace("\"{}", "");
        long count_volt = volt.chars().filter(ch -> ch == ',').count() + 1;
//        System.out.println(volt);
        for(int i = 0; i < count_volt; i++){
//        entryList_temp.add(new Entry(i, ))
        }

        return inflater.inflate(R.layout.fragment_state, container, false);
    }

    Fragment f = this;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        if(view != null) {
            refresh_button = view.findViewById(R.id.state_refresh);

            refresh_button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Btn Clicked");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(f).attach(f).commit();
                    //push_history clear
                    arrayList.clear();
                    System.out.println("Refreshed");
                }
            }) ;

            //push_history
            pushHistoryAdapter = new PushHistoryAdapter(arrayList);
            recyclerView = (RecyclerView) getView().findViewById(R.id.push_history);
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(pushHistoryAdapter);




//            scatter_temp.setData();



        }
    }


}