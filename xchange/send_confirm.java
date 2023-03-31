package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.xchange.model.users_information;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class send_confirm extends AppCompatActivity {

    private static final String TAG = "send_confirm";

    private String receiverId;
    private int sendAmount;
    private String billAddress, billType;
    private static String transactionId;

    TextView viewName, viewId, viewAmount, viewDate;

    Button sendBtn;

    public static final int TASK_A = 1;


    userHandler userhandler;
    myClass mFunction;

    LinearLayout layout_confirm, layout_splash;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TASK_A:
                    Intent intent = new Intent(getApplicationContext(), receipt_Transaction.class);
                    intent.putExtra("transactionId", transactionId);
                    intent.putExtra("transactionAmount", viewAmount.getText().toString());
                    intent.putExtra("receiverName", viewName.getText().toString().trim());
                    intent.putExtra("transactionDate", viewDate.getText().toString());
                    finish();
                    startActivity(intent);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_confirm);

        viewName = (TextView) findViewById(R.id.view_reciever_name);
        viewAmount = (TextView) findViewById(R.id.view_reciever_amount);
        viewId = (TextView) findViewById(R.id.view_reciever_id);
        viewDate = (TextView) findViewById(R.id.view_reciever_date);

        receiverId = getIntent().getStringExtra("addressId");
        sendAmount = Integer.valueOf(getIntent().getStringExtra("sendAmount"));
        billAddress = getIntent().getStringExtra("billAddress");
        billType = getIntent().getStringExtra("billType");

        layout_confirm = (LinearLayout) findViewById(R.id.layout_confirm);
        layout_splash = (LinearLayout) findViewById(R.id.splashLayout);

        userhandler = new userHandler();
        mFunction = new myClass();

        loadData();

        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable objRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            transactionId = String.valueOf(SystemClock.elapsedRealtime());
//                          send amount to
                            userhandler.updateAccountBalance(userhandler.getUser(), -sendAmount);
                            if(userhandler.transactionHandler("Send", userhandler.getUser(), receiverId, transactionId, sendAmount)){
//                              receive amount
                                Thread.sleep(1000);
                                userhandler.updateAccountBalance(receiverId, sendAmount);
                                if(userhandler.transactionHandler("Receive", receiverId, userhandler.getUser(), transactionId, sendAmount)){
                                    Log.d(TAG, "run: Receive Data");
                                }
                            }

                            if(billAddress != null){
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Create_Bills");
                                ref.child(billType).child(billAddress).child("bill_status").setValue("Paid");
                                Toast.makeText(send_confirm.this, "PAID", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(TASK_A);
                    }
                };
                Thread ebgThread = new Thread(objRunnable);
                ebgThread.start();
                mFunction.loadLayout(layout_confirm);
                mFunction.loadLayout(layout_splash);
            }
        });
    }
    private void loadData(){
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date dateobj = new Date();

        DatabaseReference userInformation = FirebaseDatabase.getInstance().getReference("users_information");
        userInformation.child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users_information userInfo = snapshot.getValue(users_information.class);
                if(snapshot.exists()){
                    viewDate.setText(String.valueOf(dateobj));
                    viewId.setText(receiverId);
                    viewAmount.setText("Php "+ sendAmount);
                    viewName.setText(userInfo.FName + " " + userInfo.LName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}