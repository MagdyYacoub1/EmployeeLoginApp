package com.example.employeeloginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayAbsenteesActivity extends AppCompatActivity {


    private TextView numOfAbsentees;
    private ListView myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_absentees);

        numOfAbsentees = (TextView) findViewById(R.id.DisplayAbsenteesActivity_number);
        myList = (ListView) findViewById(R.id.DisplayAbsenteesActivity_listView);
        Employee.absentees.clear();
        for(Employee emp : Employee.allEmployees)
        {
            if(emp.loginTime == null)
            {
                Employee.absentees.add(emp);
            }
        }
        numOfAbsentees.setText(String.valueOf(Employee.absentees.size()));

        CustomAdapter customAdapter = new CustomAdapter();
        myList.setAdapter(customAdapter);
    }


    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Employee.absentees.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.custom_absentees_list, null);
            ImageView empImage = (ImageView) view.findViewById(R.id.AbsenteesListItem_imageView);
            TextView empName = (TextView) view.findViewById(R.id.AbsenteesListItem_NameText);
            TextView empDepartment = (TextView) view.findViewById(R.id.AbsenteesListItem_DepartmentText);

            empImage.setImageBitmap(Employee.absentees.get(position).image);
            empName.setText(Employee.absentees.get(position).name);
            empDepartment.setText(Employee.absentees.get(position).department);
            return view;
        }
    }
}