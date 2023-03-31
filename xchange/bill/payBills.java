package com.project.xchange.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.xchange.R;
import com.project.xchange.bill.form_pay_bills;

public class payBills extends AppCompatActivity implements View.OnClickListener{

    private Button electric_btn, internet_btn, water_btn;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bills);

        electric_btn = (Button) findViewById(R.id.electric_btn);
        electric_btn.setOnClickListener(this);
        internet_btn = (Button) findViewById(R.id.internet_btn);
        internet_btn.setOnClickListener(this);
        water_btn = (Button) findViewById(R.id.water_btn);
        water_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.electric_btn:
                intent = new Intent(getApplicationContext(), form_pay_bills.class);
                intent.putExtra("utilType", "Electric");
                startActivity(intent);
                break;
            case R.id.internet_btn:
                intent = new Intent(getApplicationContext(), form_pay_bills.class);
                intent.putExtra("utilType", "Internet");
                startActivity(intent);
                break;
            case R.id.water_btn:
                intent = new Intent(getApplicationContext(), form_pay_bills.class);
                intent.putExtra("utilType", "Water");
                startActivity(intent);
                break;
        }
    }
}