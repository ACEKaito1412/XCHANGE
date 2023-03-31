package com.project.xchange.faucet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.xchange.R;
import com.project.xchange.myClass;
import com.project.xchange.userHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class faucet extends AppCompatActivity implements View.OnClickListener {



    TextView xChangeID, xFaucet_amount;

    Button requestBtn, receiveBtn;

    userHandler userhandler;
    myClass mFunction;

    String transactionId, date;

    LinearLayout layoutLoad, layoutFaucet;

    public static final int TASK_A = 1;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TASK_A:
                    Intent intent = new Intent(getApplicationContext(), faucet_confirm.class);
                    intent.putExtra("transactionId", transactionId);
                    intent.putExtra("transactionAmount", xFaucet_amount.getText().toString());
                    intent.putExtra("receiverName", userhandler.getUserName());
                    intent.putExtra("transactionDate", date);
                    finish();
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faucet);

        Boolean out = getIntent().getBooleanExtra("signout", false);
        if(out){
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        xChangeID = (TextView) findViewById(R.id.x_changeID);
        xFaucet_amount = (TextView) findViewById(R.id.x_faucet_amount);

        requestBtn = (Button) findViewById(R.id.amount_req);
        requestBtn.setOnClickListener(this);

        receiveBtn = (Button) findViewById(R.id.recieve_btn);
        receiveBtn.setOnClickListener(this);

        userhandler = new userHandler();
        mFunction = new myClass();

        xChangeID.setText(userhandler.getUser());

        layoutLoad = (LinearLayout) findViewById(R.id.splashLayout);
        layoutFaucet = (LinearLayout) findViewById(R.id.layout_faucet);

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date dateobj = new Date();
        date = df.format(dateobj);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.amount_req:
                int min = 50;
                int max = 100;
                int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
                xFaucet_amount.setText(String.valueOf(random_int));
                break;
            case R.id.recieve_btn:
                String amount = xFaucet_amount.getText().toString();

                if(amount.isEmpty()){
                    xFaucet_amount.setError("Please Request Amount First");
                    xFaucet_amount.requestFocus();
                    return;
                }

                Runnable runnableObj = new Runnable() {
                    @Override
                    public void run() {
                        transactionId = userhandler.requestFaucet(Integer.valueOf(amount));
                        handler.sendEmptyMessage(TASK_A);
                    }
                };
                Thread ebgThread = new Thread(runnableObj);
                ebgThread.start();
                mFunction.loadLayout(layoutFaucet);
                mFunction.loadLayout(layoutLoad);
                break;
        }
    }
}