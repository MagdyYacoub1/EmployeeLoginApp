package com.example.employeeloginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class DisplayAttendeesActivity extends AppCompatActivity {


    private TextView numOfAttendees;
    private TextView numOfSignedOut;
    private TextView numOfStillWorking;
    private ListView myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_attendees);

        numOfAttendees = (TextView) findViewById(R.id.DisplayAttendeesActivity_numberTotal);
        numOfSignedOut = (TextView) findViewById(R.id.DisplayAttendeesActivity_numberSigned);
        numOfStillWorking = (TextView) findViewById(R.id.DisplayAttendeesActivity_numberStill);
        myList = (ListView) findViewById(R.id.DisplayAttendeesActivity_listView);
        Employee.attendees.clear();
        for(Employee emp : Employee.allEmployees)
        {
            if(emp.loginTime != null)
            {
                Employee.attendees.add(emp);
            }
        }
        numOfAttendees.setText(String.valueOf(Employee.attendees.size()));
        int stillWorking = 0;
        int signed_Out = 0;
        for(Employee emp : Employee.attendees)
        {
            if(emp.signOutTime != null)
                signed_Out++;
            else
                stillWorking++;

        }
        numOfSignedOut.setText(String.valueOf(signed_Out));
        numOfStillWorking.setText(String.valueOf(stillWorking));
        CustomAdapter customAdapter = new CustomAdapter();
        myList.setAdapter(customAdapter);
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Employee.attendees.size();
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
            View view = getLayoutInflater().inflate(R.layout.custom_attendees_list, null);
            ImageView empImage = (ImageView) view.findViewById(R.id.AttendeesListItem_imageView);
            TextView empName = (TextView) view.findViewById(R.id.AttendeesListItem_NameText);
            TextView empLoginTime = (TextView) view.findViewById(R.id.AttendeesListItem_loginTimeText);
            TextView empSignOutTime = (TextView) view.findViewById(R.id.AttendeesListItem_signOutTimeText);

            empImage.setImageBitmap(Employee.attendees.get(position).image);
            empName.setText(Employee.attendees.get(position).name);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:mm");
            String loginTime = dateFormat.format(Employee.attendees.get(position).loginTime);
            String signOutTime;
            if(Employee.attendees.get(position).signOutTime == null)
                signOutTime = "Still Working...";
            else
                signOutTime = dateFormat.format(Employee.attendees.get(position).signOutTime);
            empLoginTime.setText(loginTime);
            empSignOutTime.setText(signOutTime);
            return view;
        }
    }
}