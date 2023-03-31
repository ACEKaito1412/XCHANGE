package com.project.xchange.bill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.xchange.R;
import com.project.xchange.model.Create_Bill;
import com.project.xchange.send_confirm;
import com.project.xchange.userHandler;

public class form_pay_bills extends AppCompatActivity {

    TextView utilType, company_view, customer_view, amount_view, reference_view, titleBill;
    Button search_btn, confirm_btn;
    EditText ref_id;

    LinearLayout search_layout;
    LinearLayout bill_layout;

    userHandler userhandler;

    private static String receiver_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pay_bills);

        utilType = (TextView) findViewById(R.id.utilType);

        utilType.setText(getIntent().getStringExtra("utilType"));

        company_view = (TextView) findViewById(R.id.view_company_name);
        customer_view = (TextView) findViewById(R.id.view_bill_name);
        amount_view = (TextView) findViewById(R.id.view_bill_amount);
        reference_view = (TextView) findViewById(R.id.view_ref_id);
        titleBill = (TextView) findViewById(R.id.title_bill);

        search_layout = (LinearLayout) findViewById(R.id.layout_search);
        bill_layout = (LinearLayout) findViewById(R.id.layout_bill);

        ref_id = (EditText) findViewById(R.id.ref_id);

        userhandler = new userHandler();

        search_btn = (Button) findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ref_id.getText().toString().trim().isEmpty()){
                    ref_id.setError("Please Enter your Reference Number");
                    ref_id.requestFocus();
                    return;
                }

                String utility = getIntent().getStringExtra("utilType");

                String refferenceID = ref_id.getText().toString().trim();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Create_Bills");
                ref.child(utility).child(refferenceID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        company_view, customer_view, amount_view, reference_view, titleBill;
                        if(snapshot.exists()){
                            Create_Bill bill = snapshot.getValue(Create_Bill.class);
                            bill_layout.setVisibility(View.VISIBLE);
                            search_layout.setVisibility(View.GONE);
                            titleBill.setText(utilType.getText().toString());
                            company_view.setText(bill.getCompany_name());
                            customer_view.setText("Customer's Name: "+bill.getCustomer_name());
                            amount_view.setText(""+String.valueOf(bill.getAmount()));
                            reference_view.setText("Ref. ID: "+bill.getRef_id());

                            //reference on receiver of amount
                            receiver_id = bill.getAccount_number();
                        }else{
                            ref_id.setError("Ref. ID Does not Exist");
                            ref_id.requestFocus();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });

        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount = Double.valueOf(amount_view.getText().toString().trim());
                int i = (int) amount;

                if(amount > userhandler.getUserBalance()){
                    Toast.makeText(form_pay_bills.this, "Doesn't Have Enough Balance to Continue Transaction.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String utility = getIntent().getStringExtra("utilType");


                Intent intent = new Intent(getApplicationContext(), send_confirm.class);
                intent.putExtra("addressId", receiver_id);
                intent.putExtra("sendAmount", String.valueOf(i));
                intent.putExtra("billAddress", ref_id.getText().toString().trim());
                intent.putExtra("billType", utility);
                startActivity(intent);
            }
        });
    }
}

