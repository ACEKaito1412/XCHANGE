package com.project.xchange.bill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.xchange.MainActivity;
import com.project.xchange.R;
import com.project.xchange.model.Create_Bill;
import com.project.xchange.userHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class createdBill extends AppCompatActivity {

    private TextView company_name, customer_name, bill_amount, ref_idView, date_view, note;

    private Button confirm_btn, goback_btn;

    public static final int TASK_A = 1;

    private LinearLayout splash_layout, confirm_layout;

    private userHandler userhandler;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TASK_A:
                    Toast.makeText(createdBill.this, "Task Complete", Toast.LENGTH_SHORT).show();
                    goback_btn.setVisibility(View.VISIBLE);
                    splash_layout.setVisibility(View.GONE);
                    confirm_layout.setVisibility(View.VISIBLE);
                    note.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_bill);

        company_name = (TextView) findViewById(R.id.view_comp_name);
        customer_name = (TextView) findViewById(R.id.view_cust_name);
        bill_amount = (TextView) findViewById(R.id.view_bill_amount);
        ref_idView = (TextView) findViewById(R.id.view_bill_ref);
        date_view = (TextView) findViewById(R.id.view_bill_date);
        note = (TextView) findViewById(R.id.note);

        confirm_layout = (LinearLayout) findViewById(R.id.layout_confirm);
        splash_layout = (LinearLayout) findViewById(R.id.splashLayout);

        String utilType = getIntent().getStringExtra("utilType");
        String compName = getIntent().getStringExtra("compName");
        String custName = getIntent().getStringExtra("custName");
        String billAmount = getIntent().getStringExtra("billAmount");

        userhandler = new userHandler();


        company_name.setText(compName+" "+utilType);
        customer_name.setText(custName);
        bill_amount.setText(billAmount);

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date dateobj = new Date();
        String date = df.format(dateobj);

        date_view.setText(date);

        String ref_id = utilType.toLowerCase(Locale.ROOT)+"_"+ SystemClock.elapsedRealtime();

        ref_idView.setText("Ref. ID: "+ref_id);



        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Create_Bill create_bill = new Create_Bill(utilType, compName, custName, ref_id, Double.valueOf(billAmount), date, "Unpaid", userhandler.getUser());
                            DatabaseReference bill_reference = FirebaseDatabase.getInstance().getReference("Create_Bills");
                            DatabaseReference type = bill_reference.child(utilType);
                            type.child(ref_id).setValue(create_bill).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(TASK_A);
                    }
                };

                splash_layout.setVisibility(View.VISIBLE);
                confirm_layout.setVisibility(View.GONE);
                confirm_btn.setVisibility(View.GONE);
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });

        goback_btn = (Button) findViewById(R.id.goback_btn);
        goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }
}