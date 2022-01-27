package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.StringTokenizer;

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

    private ArrayList<PushHistory> arrayList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private PushHistoryAdapter pushHistoryAdapter;
    private LinearLayoutManager linearLayoutManager;

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

        arrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String test = "http://192.168.56.1:80/get_push_history.php";
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

        jsonArray = resultObj.get("push_history").getAsJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            System.out.println("TEST: "+jsonArray.get(i));
            //System.out.println(jsonArray.get(i).getAsJsonObject().get("push_type"));
            PushHistory pushHistory = new PushHistory(0,
                    jsonArray.get(i).getAsJsonObject().get("push_type").toString(),
                    jsonArray.get(i).getAsJsonObject().get("send_time").toString(),
                    jsonArray.get(i).getAsJsonObject().get("send_msg").toString());
            arrayList.add(pushHistory);
        }


        return inflater.inflate(R.layout.fragment_charge, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if(view != null) {
            //실제로 매칭해주고 setText 등등..
//            textView = view.findViewById(R.id.textView_test);
//            textView.setText("modified");

            pushHistoryAdapter = new PushHistoryAdapter(arrayList);

            recyclerView = (RecyclerView) getView().findViewById(R.id.push_history);
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(pushHistoryAdapter);
        }
    }
}