package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class receipt_Transaction extends AppCompatActivity {

    TextView viewName, viewId, viewAmount, viewDate;

    Button goBackBtn;

    String transactionID, receiverName, transactionAmount, transactionDate;

    userHandler userhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_transaction);

        viewName = (TextView) findViewById(R.id.view_reciever_name);
        viewAmount = (TextView) findViewById(R.id.view_reciever_amount);
        viewId = (TextView) findViewById(R.id.view_reciever_id);
        viewDate = (TextView) findViewById(R.id.view_reciever_date);

        transactionID = getIntent().getStringExtra("transactionId");
        receiverName = getIntent().getStringExtra("receiverName");
        transactionAmount = getIntent().getStringExtra("transactionAmount");
        transactionDate = getIntent().getStringExtra("transactionDate");

        userhandler = new userHandler();

        goBackBtn = (Button) findViewById(R.id.goback_btn);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        viewName.setText(receiverName);
        viewDate.setText(transactionDate);
        viewAmount.setText(transactionAmount);
        viewId.setText("Transaction Ref ID: " + transactionID);
    }

}