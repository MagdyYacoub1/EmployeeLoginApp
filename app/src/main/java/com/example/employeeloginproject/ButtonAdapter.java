package com.example.employeeloginproject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    private int buttonId;
    private final int totalButtons = 6;
    List<String> buttonsTitles = Arrays.asList("Sign In", "Sign Out", "All Employees", "Attendees", "Absentees", "Add Employee");
    List<Intent> activitiesList;



    public ButtonAdapter(Context context) {
        this.mContext = context;
        Intent AddActivityIntent = new Intent(mContext, AddEmployee_Activity.class);
        Intent SignInActivityIntent = new Intent(mContext, LoginActivity.class);

        activitiesList = Arrays.asList(SignInActivityIntent, null, null, null, null, AddActivityIntent);
    }

    @Override
    public int getCount() {
        return totalButtons;
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
        Button btn;
        if(convertView == null) {
            btn = new Button(mContext);
            btn.setText(buttonsTitles.get(position));
        }
        else{
            btn = (Button) convertView;
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Button #" + (position), Toast.LENGTH_SHORT).show();
                switchActivity(position);
            }
        });

        return btn;
    }

    void switchActivity(int position){
        mContext.startActivity(activitiesList.get(position));
    }
}
