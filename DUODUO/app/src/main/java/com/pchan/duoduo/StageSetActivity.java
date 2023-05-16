package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class StageSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_set);
        Intent messageIntent = getIntent();
        String[] firstSetMessage = messageIntent.getStringArrayExtra("FirstSetMessage");
        for (String message : firstSetMessage) {
            Log.d("test intent", message);
        }
    }
}