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

public class StateFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private PushHistoryAdapter pushHistoryAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<PushHistory> arrayList;

    public static Button refresh_button;
    String type = new String();
    int type_c, type_b, type_d = 0;

    TextView textView_c, textView_b, textView_d;
    public StateFragment() {
        // Required empty public constructor
    }

    public static StateFragment newInstance(String param1, String param2) {
        StateFragment fragment = new StateFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
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
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        JsonObject resultObj = history_thread.getResult();
        JsonArray jsonArray = new JsonArray();

        jsonArray = resultObj.get("push_history").getAsJsonArray();
        type_c=type_b=type_d=0;
        for(int i = 0; i < jsonArray.size(); i++){
            type = jsonArray.get(i).getAsJsonObject().get("push_type").toString().replace("\"", "");
            PushHistory pushHistory = new PushHistory(0,
                    type,
                    jsonArray.get(i).getAsJsonObject().get("send_time").toString().replace("\"", ""),
                    jsonArray.get(i).getAsJsonObject().get("send_msg").toString().replace("\"", ""));
            arrayList.add(pushHistory);

            //날짜별로 볼 수 있었으면 좋겠다.

            // 푸시별 횟수 계산 코드
            switch (type){
                case "주간 안전 리포트":
                    type_d++;
                    break;
                case "배터리 셀 전압 이상 감지":
                case "배터리 온도 이상 감지":
                    type_b++;
                    break;
                case "배터리 충전 알림":
                    type_c++;
                    break;
            }
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

            textView_c = view.findViewById(R.id.push_charge);
            textView_b = view.findViewById(R.id.push_charge2);
            textView_d = view.findViewById(R.id.push_charge3);

            textView_c.setText(Integer.toString(type_c)+"회");
            textView_b.setText(Integer.toString(type_b)+"회");
            textView_d.setText(Integer.toString(type_d)+"회");

            //push_history
            pushHistoryAdapter = new PushHistoryAdapter(arrayList);
            recyclerView = (RecyclerView) getView().findViewById(R.id.push_history);
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(pushHistoryAdapter);

        }
    }


}