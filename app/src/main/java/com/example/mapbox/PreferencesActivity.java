package com.example.mapbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PreferencesActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.preferences);   //환경설정 화면 창 구성 연결
    }
}
