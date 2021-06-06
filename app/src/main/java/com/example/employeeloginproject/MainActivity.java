package com.example.employeeloginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView mainGrid = (GridView) findViewById(R.id.gridView);
        mainGrid.setAdapter(new ButtonAdapter(this));
    }
}