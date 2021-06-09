package com.example.employeeloginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
            String department = cursor.getString(6);
            tempEmployee = new Employee(id, name, password, bmp, department);
            String loginTime  = cursor.getString(4);
            String signOutTime  = cursor.getString(5);
            SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy, h:mm");
            try {
                if(loginTime != null)
                    tempEmployee.loginTime = format.parse(loginTime);
                if(signOutTime != null)
                    tempEmployee.signOutTime = format.parse(signOutTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Employee.allEmployees.add(tempEmployee);
            cursor.moveToNext();
        }

        GridView mainGrid = (GridView) findViewById(R.id.gridView);
        mainGrid.setAdapter(new ButtonAdapter(this));
    }
}