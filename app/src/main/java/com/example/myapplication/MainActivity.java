package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.bottomNavigationView, new HomeFragment()).commit();


        bottomNavigationView = findViewById((R.id.bottomNavigationView));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.charge:
                        fragment = new ChargeFragment();
                        break;
                    case R.id.map:
                        fragment = new MapFragment();
                        break;
                    case R.id.state:
                        fragment = new StateFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.bottomNavigationView, fragment).commit();
                return true;
            }
        });

    }
}