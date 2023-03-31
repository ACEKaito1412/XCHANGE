package com.project.xchange.faucet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.xchange.MainActivity;
import com.project.xchange.R;

public class faucet_confirm extends AppCompatActivity {

    private String transactionId,
    transactionAmount,
    receiverName,
    transactionDate;

    Button goBackBtn;

    TextView viewName, viewId, viewAmount, viewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faucet_confirm);

        viewName = (TextView) findViewById(R.id.view_reciever_name);
        viewAmount = (TextView) findViewById(R.id.view_reciever_amount);
        viewId = (TextView) findViewById(R.id.view_reciever_id);
        viewDate = (TextView) findViewById(R.id.view_reciever_date);

        transactionAmount = getIntent().getStringExtra("transactionAmount");
        receiverName = getIntent().getStringExtra("receiverName");
        transactionDate = getIntent().getStringExtra("transactionDate");
        transactionId = getIntent().getStringExtra("transactionId");

        goBackBtn = (Button) findViewById(R.id.goback_btn);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        loadData();
    }

    private void loadData() {
        viewAmount.setText("PHP "+transactionAmount);
        viewName.setText(receiverName);
        viewDate.setText(transactionDate);
        viewId.setText("Transaction Ref. ID\n "+transactionId);
    }
}