package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.xchange.model.Transaction;

import java.util.ArrayList;

public class paymentHistory extends AppCompatActivity {
    private DatabaseReference transaction_reference_from;
    private FirebaseUser user;
    private DatabaseReference childRef;

    private ListView sendView, receiveView;
    private ArrayAdapter<String> arrayAdapterSend, arrayAdapterReceive;
    private ArrayList<String> sendlist, receivelist;

    private userHandler userhandler;

    private Button gobackbtn, btn_send_view, btn_receive_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        Boolean out = getIntent().getBooleanExtra("signout", false);
        if(out){
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        transaction_reference_from = FirebaseDatabase.getInstance().getReference("Transaction");
        user = FirebaseAuth.getInstance().getCurrentUser();
        childRef = transaction_reference_from.child(user.getUid());

        sendView = findViewById(R.id.send_view);
        sendlist = new ArrayList<String>();
        arrayAdapterSend = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sendlist);
        sendView.setAdapter(arrayAdapterSend);

        receiveView = findViewById(R.id.receive_view);
        receivelist = new ArrayList<String>();
        arrayAdapterReceive = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, receivelist);
        receiveView.setAdapter(arrayAdapterReceive);

        btn_send_view = (Button) findViewById(R.id.btn_send_view);
        btn_send_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendView.setVisibility(View.VISIBLE);
                receiveView.setVisibility(View.GONE);
            }
        });

        btn_receive_view = (Button) findViewById(R.id.btn_receive_view);
        btn_receive_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendView.setVisibility(View.GONE);
                receiveView.setVisibility(View.VISIBLE);
            }
        });

        gobackbtn = (Button) findViewById(R.id.goback_btn);
        gobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sendlist.clear();
                receivelist.clear();
                for (DataSnapshot data:snapshot.getChildren()){
                    String key = data.getKey();
                    Transaction db = data.getValue(Transaction.class);
                    if(db.getTransaction_Type().toString().equals("Receive")) {
                        String output = "ID: \t" + key + "\nType: \t" + db.getTransaction_Type() + " \nAmount:\t" + db.getAmount() + " \nDate:\t" + db.getDate();
                        receivelist.add(output);
                    }else{
                        String output = "ID: \t" + key + "\nType: \t" + db.getTransaction_Type() + " \nAmount:\t" + db.getAmount() + " \nDate:\t" + db.getDate();
                        sendlist.add(output);
                    }
                }

                if(arrayAdapterReceive.getCount() < 1){
                    String output = "No Transaction Found";
                    receivelist.add(output);
                }

                if(arrayAdapterSend.getCount() < 1){
                    String output = "No Transaction Found";
                    sendlist.add(output);
                }


                arrayAdapterSend.notifyDataSetChanged();
                arrayAdapterReceive.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}