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

public class LoginActivity extends AppCompatActivity {
    private final int cam_request = 100;
    EditText name;
    EditText password;
    Button smileBtn;
    Button signInBtn;
    ImageView employeeImage;
    Bitmap myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        name = (EditText) findViewById(R.id.loginActivity_NameText);
        password = (EditText) findViewById(R.id.loginActivity_PasswordText);
        smileBtn = (Button) findViewById(R.id.loginActivity_smileBtn);
        signInBtn = (Button) findViewById(R.id.loginActivity_loginBtn);
        employeeImage = (ImageView) findViewById(R.id.loginActivity_imageView);


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText()))
                {
                    name.setError(" Please Enter a Name");
                }else if(TextUtils.isEmpty(password.getText())){
                    password.setError(" Please Enter a Password");
                }else if(employeeImage.getDrawable() == null){
                    Toast.makeText(v.getContext(), "Please include a picture", Toast.LENGTH_SHORT).show();
                } else {
                    Employee signEmployee;
                    boolean found = false;
                    for(Employee emp: Employee.allEmployees){
                        if(emp.name.equalsIgnoreCase(name.getText().toString())
                                && emp.password.equals(password.getText().toString()))
                        {
                            signEmployee = emp;
                            found = true;
                        }
                    }
                    if(found){

                    }
                    else{
                        Toast.makeText(v.getContext(), "Employee not found please enter correct name and password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        smileBtn.setOnClickListener(new View.OnClickListener() {
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
            employeeImage.setImageBitmap(myImage);
        }
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}