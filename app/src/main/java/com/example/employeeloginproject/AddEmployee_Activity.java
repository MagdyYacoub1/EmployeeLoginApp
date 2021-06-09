package com.example.employeeloginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.ByteArrayOutputStream;

public class AddEmployee_Activity extends AppCompatActivity {
    private final int cam_request = 1888;
    private Bitmap croppedImage;
    private EditText name;
    private EditText password;
    private EditText department;
    private Button smile_btn;
    private Button addEmployee_btn;
    private ImageView Image_view;
    private Bitmap myImage;


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
                    byte[] imageBytes = getBitmapAsByteArray(croppedImage);
                    long success = db.InsertEmp(name.getText().toString(), password.getText().toString(), imageBytes, department.getText().toString());
                    //add in all employees list
                    Employee tempEmployee= new Employee((int)success, name.getText().toString(), password.getText().toString(), croppedImage, department.getText().toString());
                    Employee.allEmployees.add(tempEmployee);
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
            //Image_view.setImageBitmap(myImage);
            detectFace();
        }
    }

    public void detectFace(){
        Paint boxPaint = new Paint();
        boxPaint.setStrokeWidth(5);
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);

        Bitmap temp = Bitmap.createBitmap(myImage.getWidth(), myImage.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(temp);
        canvas.drawBitmap(myImage, 0, 0, null);
        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        if(!faceDetector.isOperational()){
            Toast.makeText(AddEmployee_Activity.this, "Face Detector Won't work on your device", Toast.LENGTH_SHORT).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(myImage).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);
        //for(int i=0; i< sparseArray.size(); i++){
        Face face = sparseArray.valueAt(0);
        float x1 = face.getPosition().x;
        float y1 = face.getPosition().y;
        float x2 = x1 + face.getWidth();
        float y2 = y1 + face.getHeight();
        RectF rectF = new RectF(x1,y1, x2, y2);
        canvas.drawRoundRect(rectF, 2, 2, boxPaint);
        //}
        Image_view.setImageDrawable(new BitmapDrawable(getResources(), temp));
        croppedImage = Bitmap.createBitmap(myImage, (int)rectF.left, (int)rectF.top, (int)rectF.width(), (int)rectF.height());
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}