package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class send_money extends AppCompatActivity{

    userHandler userhandler;

    Button send_next;

    EditText amount_send,
             receiver_address;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        Boolean out = getIntent().getBooleanExtra("signout", false);
        if(out){
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        amount_send = (EditText) findViewById(R.id.amountsend);
        receiver_address = (EditText) findViewById(R.id.receiveraddress);

        userhandler = new userHandler();

        send_next = (Button) findViewById(R.id.send_next_btn);
        send_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendConfirm();
            }
        });


    }

    private void sendConfirm(){
        String address;
        int amount;

        if(receiver_address.getText().toString().isEmpty()){
            receiver_address.setError("Please Enter Receiver Address.");
            receiver_address.requestFocus();
            return;
        }

        if(receiver_address.getText().length() != 28){
            receiver_address.setError("Please Enter Valid Address.");
            receiver_address.requestFocus();
            return;
        }

        if(amount_send.getText().toString().isEmpty()){
            amount_send.setError("Please Enter Amount");
            amount_send.requestFocus();
            return;
        }

        if(Integer.valueOf(amount_send.getText().toString()) > userhandler.getUserBalance()){
            amount_send.setError("User Doesn't Have Enough Balance");
            amount_send.requestFocus();
            return;
        }

        if(userhandler.userExist(receiver_address.getText().toString().trim())) {
            receiver_address.setError("Address Doesn't Exist.");
            receiver_address.requestFocus();
            return;
        }


        Intent intent = new Intent(getApplicationContext(), send_confirm.class);
        intent.putExtra("addressId", receiver_address.getText().toString().trim());
        intent.putExtra("sendAmount", amount_send.getText().toString());
        intent.putExtra("billAddress", "");
        intent.putExtra("billType", "");
        startActivity(intent);
    }
}