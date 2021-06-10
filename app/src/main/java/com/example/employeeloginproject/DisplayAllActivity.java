package com.example.employeeloginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayAllActivity extends AppCompatActivity {

    private TextView numOfEmployees;
    private ListView myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all);


        numOfEmployees = (TextView) findViewById(R.id.DisplayALLActivity_number);
        myList = (ListView) findViewById(R.id.DisplayALLActivity_listView);

        numOfEmployees.setText(String.valueOf(Employee.allEmployees.size()));

        CustomAdapter customAdapter = new CustomAdapter();
        myList.setAdapter(customAdapter);

    }

    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return Employee.allEmployees.size();
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
            View view = getLayoutInflater().inflate(R.layout.custom_all_list, null);
            ImageView empImage = (ImageView) view.findViewById(R.id.AllListItem_imageView);
            ImageView deleteImage = (ImageView) view.findViewById(R.id.AllListItem_deleteImage);
            TextView empName = (TextView) view.findViewById(R.id.AllListItem_NameText);
            TextView empDepartment = (TextView) view.findViewById(R.id.AllListItem_DepartmentText);

            empImage.setImageBitmap(Employee.allEmployees.get(position).image);
            empName.setText(Employee.allEmployees.get(position).name);
            empDepartment.setText(Employee.allEmployees.get(position).department);
            deleteImage.setTag(position);
            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Verify Authority");
                    final EditText input = new EditText(v.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (TextUtils.isEmpty(input.getText())) {
                                input.setError(" Please Enter Password");
                            }else{
                                if(input.getText().toString().equals(Admin.Password)){
                                    int pos = (int)v.getTag();
                                    int id = Employee.allEmployees.get(pos).id;
                                    Employee.allEmployees.remove(pos);
                                    CustomAdapter.this.notifyDataSetChanged();
                                    numOfEmployees.setText(String.valueOf(Employee.allEmployees.size()));
                                    EmployeeHelper db = new EmployeeHelper(getApplicationContext());
                                    db.deleteEmployee(id);
                                    Toast.makeText(v.getContext(), "Employee deleted successfully", Toast.LENGTH_SHORT).show();
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

                }
            });
            return view;
        }
    }
}