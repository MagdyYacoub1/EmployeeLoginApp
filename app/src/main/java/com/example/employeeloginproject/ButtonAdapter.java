package com.example.employeeloginproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        Intent SignOutActivityIntent = new Intent(mContext, SignOutActivity.class);
        Intent DisplayAllActivityIntent = new Intent(mContext, DisplayAllActivity.class);
        Intent DisplayAttendeesActivityIntent = new Intent(mContext, DisplayAttendeesActivity.class);
        Intent DisplayAbsenteesActivityIntent = new Intent(mContext, DisplayAbsenteesActivity.class);

        activitiesList = Arrays.asList(
                SignInActivityIntent,
                SignOutActivityIntent,
                DisplayAllActivityIntent,
                DisplayAttendeesActivityIntent,
                DisplayAbsenteesActivityIntent,
                AddActivityIntent);
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
               // Toast.makeText(v.getContext(), "Button #" + (position), Toast.LENGTH_SHORT).show();
                if(position == 2 || position == 3 || position == 4 || position == 5){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Admin Only");
                    final EditText input = new EditText(mContext);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (TextUtils.isEmpty(input.getText())) {
                                input.setError(" Please Enter a Name");
                            }else{
                                if(input.getText().toString().equals(Admin.Password)){
                                    switchActivity(position);
                                }else{
                                    dialog.cancel();
                                    Toast.makeText(v.getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }else{
                    switchActivity(position);
                }

            }
        });

        return btn;
    }

    void switchActivity(int position){
        mContext.startActivity(activitiesList.get(position));
    }
}
