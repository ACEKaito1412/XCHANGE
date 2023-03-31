package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.xchange.bill.bill_status;
import com.project.xchange.bill.create_newBill;
import com.project.xchange.bill.payBills;
import com.project.xchange.faucet.faucet;
import com.project.xchange.model.Balance;
import com.project.xchange.qr.qr_pay;
import com.project.xchange.shopping.lazadaIntegration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    myClass mFunction = new myClass();

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference balance_reference = FirebaseDatabase.getInstance().getReference("Balance");
    private DatabaseReference user_reference = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference faucet_reference = FirebaseDatabase.getInstance().getReference("FAUCET");
    private DatabaseReference transaction_reference_from = FirebaseDatabase.getInstance().getReference("Transaction");

    private static double xchange_balance = 0.0;
    private String userID;

    private TextView acc_email,
            acc_name,
            acc_id,
            x_change_ID, reload_view;;


    private Button x_sendMoney,
            x_faucet,
            x_details,
            x_transaction,
            x_cashin,
            x_bill,
            x_bill_status,
            x_shoppe,
            x_paybills,
            x_qrPay,
            x_moneyRate,
            acc_logout,
            send_next;



    private TextView x_change_balance,
            x_faucet_amt, acc_balance,
            deleteTransactions;


    private boolean dataStatus = false;

    private Intent intent;

    private LinearLayout main_layout,
            main_splashlayout;

    public static final int TASK_A = 1;
    public static final int TASK_B = 2;

    userHandler userhandler;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TASK_A:
                    break;
                case TASK_B:
                    Log.d(TAG, "handleMessage: Finish Loading Data");
                    mFunction.loadLayout(main_layout);
                    mFunction.loadLayout(main_splashlayout);
                    String k = userhandler.getUserType();
                    if(k.equals("Admin")){
                        x_bill.setVisibility(View.VISIBLE);
                        x_bill_status.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Boolean out = getIntent().getBooleanExtra("signout", false);
        if(out){
            FirebaseAuth.getInstance().signOut();
            finish();
        }

//      Set up onclicks
        x_sendMoney = (Button) findViewById(R.id.xchange_sendMoney);
        x_sendMoney.setOnClickListener(this);

        x_faucet = (Button) findViewById(R.id.xchange_faucet);
        x_faucet.setOnClickListener(this);

        x_details = (Button) findViewById(R.id.xchange_accDetails);
        x_details.setOnClickListener(this);

        x_cashin = (Button) findViewById(R.id.xchange_cashin);
        x_cashin.setOnClickListener(this);

        x_bill = (Button) findViewById(R.id.xchange_bill);
        x_bill.setOnClickListener(this);

        x_bill_status = (Button) findViewById(R.id.xchange_bill_status);
        x_bill_status.setOnClickListener(this);

        x_shoppe = (Button) findViewById(R.id.xchange_shoppe);
        x_shoppe.setOnClickListener(this);

        x_paybills = (Button) findViewById(R.id.xchange_paybills);
        x_paybills.setOnClickListener(this);

        x_qrPay = (Button) findViewById(R.id.xchange_qrPay);
        x_qrPay.setOnClickListener(this);

        x_moneyRate = (Button) findViewById(R.id.xchange_moneyRates);
        x_moneyRate.setOnClickListener(this);

        x_transaction = (Button) findViewById(R.id.xchange_transactions);
        x_transaction.setOnClickListener(this);

        reload_view  = (TextView) findViewById(R.id.reload_btn);
        reload_view.setOnClickListener(this);


//      account Information
        acc_email = (TextView) findViewById(R.id.acc_email);
        acc_name = (TextView) findViewById(R.id.acc_name);
        acc_id = (TextView) findViewById(R.id.acc_id);
        x_change_ID = (TextView) findViewById(R.id.x_changeID);
        x_faucet_amt = (TextView) findViewById(R.id.x_faucet_amount);
        acc_balance = (TextView) findViewById(R.id.acc_balance);


//      main layouts
        main_layout = (LinearLayout) findViewById(R.id.layout_main);
        main_splashlayout = (LinearLayout) findViewById(R.id.layout_splashscreen);


/*      class here */
        userhandler = new userHandler();
        loadUserData(user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xchange_faucet:
                intent = new Intent(getApplicationContext(), faucet.class);
                startActivity(intent);
                break;
            case R.id.xchange_sendMoney:
                intent = new Intent(getApplicationContext(), send_money.class);
                startActivity(intent);
                break;
            case R.id.xchange_accDetails:
                intent = new Intent(getApplicationContext(), accountDetails.class);
                startActivity(intent);
                break;
            case R.id.xchange_transactions:
                intent = new Intent(getApplicationContext(), paymentHistory.class);
                startActivity(intent);
                break;
            case R.id.xchange_cashin:
                intent = new Intent(getApplicationContext(), cash_in.class);
                startActivity(intent);
                break;
            case R.id.xchange_bill:
                intent = new Intent(getApplicationContext(), create_newBill.class);
                startActivity(intent);
                break;
            case R.id.xchange_bill_status:
                intent = new Intent(getApplicationContext(), bill_status.class);
                startActivity(intent);
                break;
            case R.id.xchange_shoppe:
                intent = new Intent(getApplicationContext(), lazadaIntegration.class);
                startActivity(intent);
                break;
            case R.id.xchange_paybills:
                intent = new Intent(getApplicationContext(), payBills.class);
                startActivity(intent);
                break;
            case R.id.xchange_moneyRates:
                intent = new Intent(getApplicationContext(), money_rates.class);
                startActivity(intent);
                break;
            case R.id.xchange_qrPay:
                intent = new Intent(getApplicationContext(), qr_pay.class);
                startActivity(intent);
                break;
            case R.id.deleteTransactions:
                deleteAllTransaction();
                break;
            case R.id.reload_btn:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }



    private void deleteAllTransaction() {
        try {
            if(transaction_reference_from.child(userID)!= null){
                transaction_reference_from.child(userID).removeValue();
                Toast.makeText(this, "Transactions removed", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void updateBalance(){
        balance_reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Balance wallet = snapshot.getValue(Balance.class);
                Double balance = wallet.getBalance();

                if(wallet != null){
                    acc_balance.setText(balance.toString());
                    xchange_balance = balance;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "ERROR HAS OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadUserData(FirebaseUser user){


        if(user == null){
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
            return;
        }
        if(mFunction.netIsAvailable()) {
            mFunction.loadLayout(main_layout);
            mFunction.loadLayout(main_splashlayout);
            Runnable runnableObj = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        acc_balance.setText(String.valueOf(userhandler.getUserBalance()));
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(TASK_B);
                }
            };
            Thread ebgThread = new Thread(runnableObj);
            ebgThread.start();

        }
    }
}

