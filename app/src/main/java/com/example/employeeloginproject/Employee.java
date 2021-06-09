package com.example.employeeloginproject;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee {
    int id;
    String name;
    String password;
    Bitmap image;
    Date loginTime;
    Date signOutTime;
    String department;
    static List<Employee> allEmployees = new ArrayList<Employee>();
    static List<Employee> attendees = new ArrayList<Employee>();
    static List<Employee> absentees= new ArrayList<Employee>();


    public Employee(int id, String name, String password, Bitmap image, String department) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.image = image;
        this.department = department;
        this.signOutTime = null;
        this.department = null;
    }

}
