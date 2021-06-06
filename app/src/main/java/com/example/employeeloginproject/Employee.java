package com.example.employeeloginproject;

public class Employee {
    int id;
    String name;
    String password;
    String image;
    String loginTime;
    String signOutTime;
    String department;

    public Employee(int id, String name, String password, String image, String loginTime, String signOutTime, String department) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.image = image;
        this.loginTime = loginTime;
        this.signOutTime = signOutTime;
        this.department = department;
    }
}
