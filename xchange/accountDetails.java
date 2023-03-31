package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class accountDetails extends AppCompatActivity {

    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    ImageView qrCodeIV;

    Button acc_logout;

    LinearLayout loadingScreen, details;

    public static final int TASK_A = 1;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TASK_A:
                    clearAppData();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        userHandler userhandler = new userHandler();

        acc_logout = (Button) findViewById(R.id.acc_logout);
        acc_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FirebaseAuth.getInstance().signOut();
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(TASK_A);
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();

                loadingScreen.setVisibility(View.VISIBLE);
                details.setVisibility(View.GONE);
            }
        });

        loadingScreen = (LinearLayout) findViewById(R.id.logout);
        details = (LinearLayout) findViewById(R.id.details);

        final TextView acc_email = (TextView) findViewById(R.id.acc_email);
        final TextView acc_name = (TextView) findViewById(R.id.acc_name);
        final TextView acc_id = (TextView) findViewById(R.id.acc_id);

        qrCodeIV = findViewById(R.id.idIVQrcode);

        acc_id.setText(userhandler.getUser());
        acc_email.setText(userhandler.getUserEmail());
        acc_name.setText(userhandler.getUserName());

        createQR(acc_id.getText().toString(), qrCodeIV, bitmap);
    }

    private void createQR(String data, ImageView idView, Bitmap bitmap){
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, dimen);
        qrgEncoder.setColorBlack(Color.RED);
        qrgEncoder.setColorWhite(Color.argb(1, 2,2,2));
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            qrCodeIV.setImageBitmap(bitmap);
        } catch (Exception e) {
            // this method is called for
            // exception handling.
        }
    }

    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear "+packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}