package com.example.employeeloginproject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    private int buttonId;
    private final int totalButtons = 5;
    List<String> buttonsTitles = Arrays.asList("Sign In", "Sign Out", "All Employees", "Attendants", "Absentees");
    public ButtonAdapter(Context context) {
        this.mContext = context;
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
                Toast.makeText(v.getContext(), "Button #" + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });

        return btn;
    }
}
