package com.example.applicationtype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private int filter = TypeActivity.FILTER_ALL_APP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListener();
    }

    private void setListener() {
        findViewById(R.id.btn_all_app).setOnClickListener(view -> {
            filter = TypeActivity.FILTER_ALL_APP;
            startActivity();
        });
        findViewById(R.id.btn_system_app).setOnClickListener(view -> {
            filter = TypeActivity.FILTER_SYSTEM_APP;
            startActivity();
        });
        findViewById(R.id.btn_third_app).setOnClickListener(view -> {
            filter = TypeActivity.FILTER_THIRD_APP;
            startActivity();
        });
        findViewById(R.id.btn_sdcard_app).setOnClickListener(view -> {
            filter = TypeActivity.FILTER_SDCARD_APP;
            startActivity();
        });

    }

    private void startActivity() {
        Intent intent = new Intent(getBaseContext(), TypeActivity.class);
        intent.putExtra("filter", filter);
        startActivity(intent);
    }
}
