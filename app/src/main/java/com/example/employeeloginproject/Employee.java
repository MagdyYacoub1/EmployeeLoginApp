package com.example.employeeloginproject;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    int id;
    String name;
    String password;
    Bitmap image;
    String loginTime;
    String signOutTime;
    String department;
    static List<Employee> allEmployees = new ArrayList<Employee>();;
    static List<Employee> attendees;
    static List<Employee> absentees;


    public Employee(int id, String name, String password, Bitmap image, String department) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.image = image;
        this.department = department;
    }

}
