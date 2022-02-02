package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private Button button;
    private Button token_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.notifyBtn);
        token_button = (Button) findViewById(R.id.token_button);
        //백그라운드에서 동작하는 스케쥴러 시작
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SchedulerService.class);
                startService(intent);
            }
        });
        button.performClick();

        token_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fcm token
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()){
                                    Log.w("SF", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }
                                // Get new FCM registration token
                                String token = task.getResult();

                                // Log and toast
                                String msg = getString(R.string.msg_token_fmt, token);
                                Log.d("SF", msg);
                            }
                        });
            }
        });
        token_button.performClick();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();

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

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                return true;
            }
        });
    }


}