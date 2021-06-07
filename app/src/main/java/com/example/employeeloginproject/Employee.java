package com.example.employeeloginproject;

import android.graphics.Bitmap;

public class Employee {
    int id;
    String name;
    String password;
    Bitmap image;
    String loginTime;
    String signOutTime;
    String department;

    public Employee(int id, String name, String password, Bitmap image, String department) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.image = image;
        this.department = department;
    }
}
