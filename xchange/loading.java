package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class loading extends AppCompatActivity {

    private static final String TAG = "loading";

    public static final int TASK_A = 1;
    public static final int TASK_B = 2;

    myClass mfunction = new myClass();

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TASK_A:
                    Log.d(TAG, "Internet Is Available");
                    loading_image(img);
                    break;

                case TASK_B:
                    Log.d(TAG, "Internet is not Available");
                    loading_image(img);
                    break;
            }
        }
    };

    ImageView img;

    Button btn_A, btn_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        img = (ImageView) findViewById(R.id.gifImage);

        btn_A = (Button) findViewById(R.id.Task_A);
        btn_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable objRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if(mfunction.netIsAvailable()){
                            handler.sendEmptyMessage(TASK_A);
                        }else{
                            handler.sendEmptyMessage(TASK_B);
                        }
                    }
                };

                Thread ebgThread = new Thread(objRunnable);
                ebgThread.start();
                loading_image(img);
            }
        });

        btn_B = (Button) findViewById(R.id.Task_B);
        btn_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable objRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(TASK_B);
                    }
                };

                Thread ebgThread = new Thread(objRunnable);
                ebgThread.start();
                loading_image(img);
            }
        });


    }


    void loading_image(ImageView img){
        if(img.getVisibility() == View.GONE){
            img.setVisibility(View.VISIBLE);
        }else{
            img.setVisibility(View.GONE);
        }
    }
}