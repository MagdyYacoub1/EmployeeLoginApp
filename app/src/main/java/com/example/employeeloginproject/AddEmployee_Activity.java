package com.example.employeeloginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddEmployee_Activity extends AppCompatActivity {
    private final int cam_request = 1888;

    EditText name;
    EditText password;
    EditText department;
    Button smile_btn;
    Button addEmployee_btn;
    ImageView Image_view;
    Bitmap myImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emloyee);


         name = (EditText) findViewById(R.id.addEmployee_TextText1);
         password = (EditText) findViewById(R.id.addEmployee_TextText2);
         department = (EditText) findViewById(R.id.addEmployee_TextText3);
         smile_btn = (Button) findViewById(R.id.addEmployee_btn1);
         addEmployee_btn = (Button) findViewById(R.id.addEmployee_btn2);
         Image_view = (ImageView) findViewById(R.id.addEmployee_imageView);

        addEmployee_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText()))
                {
                    name.setError(" Please Enter a Name");
                }else if(TextUtils.isEmpty(password.getText())){
                    password.setError(" Please Enter a Password");
                }else if(TextUtils.isEmpty(department.getText())){
                    department.setError(" Please Enter a Department");
                }else if(Image_view.getDrawable() == null){
                    Toast.makeText(v.getContext(), "Please include a picture", Toast.LENGTH_SHORT).show();
                } else {
                    //add in database
                    EmployeeHelper db = new EmployeeHelper(getApplicationContext());
                    byte[] imageBytes = getBitmapAsByteArray(myImage);
                    long success = db.InsertEmp(name.getText().toString(), password.getText().toString(), imageBytes, department.getText().toString());
                    if(success != -1) {
                        Toast.makeText(v.getContext(), "Employee inserted Successfully", Toast.LENGTH_SHORT).show();
                        name.getText().clear();
                        password.getText().clear();
                        department.getText().clear();
                        Image_view.setImageResource(android.R.color.transparent);
                    }
                    else
                        Toast.makeText(v.getContext(), "Problem happened please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        smile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, cam_request);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == cam_request){
            myImage = (Bitmap) data.getExtras().get("data");
            Image_view.setImageBitmap(myImage);
        }
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}