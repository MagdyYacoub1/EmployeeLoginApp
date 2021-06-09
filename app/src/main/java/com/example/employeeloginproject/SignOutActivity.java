package com.example.employeeloginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
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

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SignOutActivity extends AppCompatActivity {
    private final int cam_request = 300;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private float[][] ori_embedding = new float[1][128];
    private float[][] test_embedding = new float[1][128];
    private int imageSizeX;
    private int imageSizeY;
    private EditText name;
    private EditText password;
    private Button smileBtn;
    private Button signOutBtn;
    private ImageView employeeImage;
    private Bitmap myImage;
    private Bitmap croppedImage;
    private Interpreter tfLite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);


        name = (EditText) findViewById(R.id.signOutActivity_NameText);
        password = (EditText) findViewById(R.id.signOutActivity_PasswordText);
        smileBtn = (Button) findViewById(R.id.signOutActivity_smileBtn);
        signOutBtn = (Button) findViewById(R.id.signOutActivity_SignOutBtn);
        employeeImage = (ImageView) findViewById(R.id.signOutActivity_imageView);
        try {
            tfLite = new Interpreter(loadModelFile(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText())) {
                    name.setError(" Please Enter a Name");
                } else if (TextUtils.isEmpty(password.getText())) {
                    password.setError(" Please Enter a Password");
                } else if (employeeImage.getDrawable() == null) {
                    Toast.makeText(v.getContext(), "Please include a picture", Toast.LENGTH_SHORT).show();
                } else {
                    Employee signOutEmployee = null;
                    int index = -1;
                    boolean found = false;
                    List<Employee> allEmployees = Employee.allEmployees;
                    for (int i = 0, attendeesSize = allEmployees.size(); i < attendeesSize; i++) {
                        Employee emp = allEmployees.get(i);
                        if (emp.name.equalsIgnoreCase(name.getText().toString())
                                && emp.password.equals(password.getText().toString())) {
                            signOutEmployee = emp;
                            index = i;
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        getEmbeddings(signOutEmployee.image, "original");
                        double distance = calculate_distance(ori_embedding, test_embedding);

                        if (distance < 6.0) {
                            if (signOutEmployee.signOutTime != null) {
                                Toast.makeText(v.getContext(), signOutEmployee.name + " is already signed out for today", Toast.LENGTH_SHORT).show();

                            } else {
                                Date currentTime = Calendar.getInstance().getTime();
                                allEmployees.get(index).signOutTime = currentTime;
                                EmployeeHelper db = new EmployeeHelper(getApplicationContext());
                                db.updateSignOutTime(allEmployees.get(index).id);
                                Toast.makeText(v.getContext(), "Verified and Signed Out Successfully", Toast.LENGTH_SHORT).show();
                            }
                            name.getText().clear();
                            password.getText().clear();
                            employeeImage.setImageResource(android.R.color.transparent);
                        } else
                            Toast.makeText(v.getContext(), "Picture Don't match with data", Toast.LENGTH_SHORT).show();
                    } else {
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
        if (requestCode == cam_request) {
            myImage = (Bitmap) data.getExtras().get("data");
            detectFace();
        }
    }

    public void detectFace() {
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
        if (!faceDetector.isOperational()) {
            Toast.makeText(SignOutActivity.this, "Face Detector Won't work on your device", Toast.LENGTH_SHORT).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(myImage).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);
        RectF rectF = null;
        if (sparseArray.valueAt(0) != null) {
            Face face = sparseArray.valueAt(0);
            float x1 = face.getPosition().x;
            float y1 = face.getPosition().y;
            float x2 = x1 + face.getWidth();
            float y2 = y1 + face.getHeight();
            rectF = new RectF(x1, y1, x2, y2);
            canvas.drawRoundRect(rectF, 2, 2, boxPaint);
            faceDetector.release();
        } else {
            Toast.makeText(SignOutActivity.this, "No Face Detected Please take another image", Toast.LENGTH_SHORT).show();
            return;
        }
        employeeImage.setImageDrawable(new BitmapDrawable(getResources(), temp));
        croppedImage = Bitmap.createBitmap(myImage, (int) rectF.left, (int) rectF.top, (int) rectF.width(), (int) rectF.height());
        getEmbeddings(croppedImage, "test");
    }
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("Qfacenet.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public void getEmbeddings(Bitmap bitmap, String imageType) {
        TensorImage inputImageBuffer;
        float[][] embedding = new float[1][128];

        int imageTensorIndex = 0;
        int[] imageShape = tfLite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tfLite.getInputTensor(imageTensorIndex).dataType();

        inputImageBuffer = new TensorImage(imageDataType);

        inputImageBuffer = loadImage(bitmap, inputImageBuffer);

        tfLite.run(inputImageBuffer.getBuffer(), embedding);

        if (imageType.equals("original")) {
            Arrays.fill(ori_embedding, null);
            ori_embedding = embedding;
        } else if (imageType.equals("test")) {
            Arrays.fill(test_embedding, null);
            test_embedding = embedding;
        }

    }

    private TensorImage loadImage(final Bitmap bitmap, TensorImage inputImageBuffer) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(new NormalizeOp(IMAGE_MEAN, IMAGE_STD))
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private double calculate_distance(float[][] ori_embedding, float[][] test_embedding) {
        double sum = 0.0;
        for (int i = 0; i < 128; i++) {
            sum = sum + Math.pow((ori_embedding[0][i] - test_embedding[0][i]), 2.0);
        }
        return Math.sqrt(sum);
    }
}