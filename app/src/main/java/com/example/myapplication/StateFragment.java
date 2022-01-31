package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    String quick_cnt;

    ArrayList<Entry> list_temp = new ArrayList<>();

    private ScatterChart scatter_temp, scatter_volt;
    List<ScatterDataSet> set_temp = new ArrayList<>();


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
        quick_cnt = new String();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String test = "http://192.168.56.1:80/get_btry.php";
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

        jsonArray = resultObj.get("btry").getAsJsonArray();

        for(int i = 0; i < jsonArray.size(); i++){
            System.out.println("TEST: "+jsonArray.get(i));
            System.out.println((jsonArray.get(i).getAsJsonObject().get("btry_mdul_tempr_arr").toString()));
            quick_cnt = jsonArray.get(i).getAsJsonObject().get("btry_mdul_tempr_arr").toString();

//            list_temp.add(new Entry(i, ))
        }

        return inflater.inflate(R.layout.fragment_state, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        if(view != null) {
            scatter_temp = view.findViewById(R.id.scatter_tempr);
            scatter_volt = view.findViewById(R.id.scatter_volts);

            Legend l = scatter_temp.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);


//            scatter_temp.setData();



            //FCM token 받아옴
            //cNnuuXz-To2ARwy-NVTR_F:APA91bE6EHUNYt2arLCUUFaGWTG6mNHnfNW1Dhtp2KrmZALY53PeZ84l40K59nLGzd7boHL_vtsGviD_zNBZ987a5uK46Zf90CwhKhTuEqAwllZ2cFI7mjfpSthjVc44M8dSovqKx34y
//            Button token_button = view.findViewById(R.id.token_button);
//
//            token_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //fcm token
//                    FirebaseMessaging.getInstance().getToken()
//                            .addOnCompleteListener(new OnCompleteListener<String>() {
//                                @Override
//                                public void onComplete(@NonNull Task<String> task) {
//                                    if (!task.isSuccessful()){
//                                        Log.w("SF", "Fetching FCM registration token failed", task.getException());
//                                        return;
//                                    }
//                                    // Get new FCM registration token
//                                    String token = task.getResult();
//
//                                    // Log and toast
//                                    String msg = getString(R.string.msg_token_fmt, token);
//                                    Log.d("SF", msg);
//                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                }
//            });
        }
    }


}