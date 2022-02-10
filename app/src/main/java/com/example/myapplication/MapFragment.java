package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MapFragment extends Fragment implements OnMapReadyCallback {


    private MapView mapView;

    public MapFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.car_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LatLng location = new LatLng(37.47156575506914, 127.02938828426859);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("KT 우면동");
        markerOptions.snippet("회사");
        markerOptions.position(location);


        //get_crd.php
        String get_crd = "http://192.168.56.1:80/get_crd.php";
        URLConnector crd_thread = new URLConnector(get_crd);

        crd_thread.start();
        try{
            crd_thread.join();
        }
        catch(InterruptedException e){
            System.out.println(e);
        }

        JsonObject crd_resultObj = crd_thread.getResult();
        JsonArray crd_jsonArray = new JsonArray();

        crd_jsonArray = crd_resultObj.get("crd").getAsJsonArray();
        System.out.println("CRD: "+crd_jsonArray);

        //현재 위치 마커 맵핑 필요



        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17));

        //carMap = googleMap;
    }
}