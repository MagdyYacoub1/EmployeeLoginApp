package com.example.employeeloginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmployeeHelper db = new EmployeeHelper(getApplicationContext());
        Cursor cursor = db.getAllEmployees();
        Employee tempEmployee;
        while (!cursor.isAfterLast()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String password = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
            String department = cursor.getString(4);
            tempEmployee = new Employee(id, name, password, bmp, department);
            Employee.allEmployees.add(tempEmployee);
            cursor.moveToNext();
        }

        GridView mainGrid = (GridView) findViewById(R.id.gridView);
        mainGrid.setAdapter(new ButtonAdapter(this));
    }
}