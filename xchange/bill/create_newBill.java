package com.project.xchange.bill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.project.xchange.R;


public class create_newBill extends AppCompatActivity {
    private static final String TAG = "create_newBill";

    private EditText company_name, customer_name, amount;
    private Button create_btn;

    private static String utilitype;

    private RadioGroup utilityGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        Boolean out = getIntent().getBooleanExtra("signout", false);
        if(out){
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        company_name = (EditText) findViewById(R.id.company_name);
        customer_name = (EditText) findViewById(R.id.customer_name);
        amount = (EditText) findViewById(R.id.amount);

        utilityGroup = (RadioGroup)  findViewById(R.id.utilityGroup);
        utilityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.internet:
                        utilitype = "Internet";
                        break;
                    case R.id.electric:
                        utilitype = "Electric";
                        break;
                    case R.id.water:
                        utilitype = "Water";
                        break;
                }
            }
        });


        create_btn = (Button) findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comp_name = company_name.getText().toString().trim();
                String cust_name = customer_name.getText().toString().trim();
                String pay_amount = amount.getText().toString().trim();

                if(utilitype == null){
                    Toast.makeText(create_newBill.this, "Please Choose Utility Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cust_name.isEmpty()){
                    customer_name.setError("Enter A Name");
                    customer_name.requestFocus();
                    return;
                }
                if(comp_name.isEmpty()){
                    company_name.setError("Enter Company Name");
                    company_name.requestFocus();
                    return;
                }
                if(Double.valueOf(pay_amount) < 500){
                    amount.setError("Minimum amount of 500 is required");
                    amount.requestFocus();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), createdBill.class);
                intent.putExtra("utilType", utilitype);
                intent.putExtra("compName", comp_name);
                intent.putExtra("custName", cust_name);
                intent.putExtra("billAmount", pay_amount);
                startActivity(intent);
            }
        });
    }

}
