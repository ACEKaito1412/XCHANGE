package com.project.xchange;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.xchange.model.Cold_Storage;
import com.project.xchange.model.Order_List;
import com.project.xchange.shopping.lazadaIntegration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class item_confirm extends AppCompatActivity {

    private ImageView image_view;
    private TextView item_name, item_quantity, item_price;

    private Button confirmBtn;

    private userHandler userhandler;

    private String itemKey, itemImage,  itemPrice,itemName, itemQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_confirm);

        itemKey = getIntent().getStringExtra("itemKey");
        itemImage = getIntent().getStringExtra("itemImage");
        itemPrice = getIntent().getStringExtra("itemPrice");
        itemName = getIntent().getStringExtra("itemName");
        itemQuantity = getIntent().getStringExtra("itemQuantity");

        item_name = (TextView) findViewById(R.id.item_name );
        item_quantity = (TextView) findViewById(R.id.item_quantity);
        item_price = (TextView) findViewById(R.id.item_price);

        image_view = (ImageView) findViewById(R.id.image_View);;
        Glide.with(getApplicationContext()).load(itemImage).into(image_view);

        item_name.setText(itemName);
        item_price.setText("Total Price: "+itemPrice);
        item_quantity.setText("Quantity: "+itemQuantity);


        userhandler = new userHandler();

        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double balance = userhandler.getUserBalance();
                Double total_price = Double.valueOf(itemPrice);

                if(balance < total_price){
                    Toast.makeText(item_confirm.this, "Don't Have Enough Balance", Toast.LENGTH_SHORT).show();
                    return;
                }


                String order_id = String.valueOf(SystemClock.elapsedRealtime() + 100 * 3);
                String cold_key = String.valueOf(SystemClock.elapsedRealtime() + 111 * 2);

                create_Order(order_id, cold_key);
                freezeAmount(order_id, cold_key);

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();


//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Cold_Storage");
//                Cold_Storage cold_store = new Cold_Storage();
//                ref.child(userhandler.getUser()).setValue(cold_store).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getApplicationContext(), "COMPLETE HERE COLD STORAGE", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }

    private void create_Order(String order_id, String cold_key){
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date dateobj = new Date();

        DatabaseReference order_ref = FirebaseDatabase.getInstance().getReference("Order_Items");
        Order_List order = new Order_List(order_id, itemKey, itemImage, itemName,
                cold_key,
                "Confirm",
                String.valueOf(dateobj),
                Double.valueOf(itemPrice),
                Integer.valueOf(itemQuantity));


        order_ref.child(userhandler.getUser()).child(order_id).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users_information");
                DatabaseReference child_ref =  ref.child(userhandler.getUser()).child("Cart").getRef();

                child_ref.child(itemKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Order Has Been Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), lazadaIntegration.class));
                    }
                });
            }
        });
    }

    private void freezeAmount(String order_id, String cold_key){
        DatabaseReference cold_ref = FirebaseDatabase.getInstance().getReference("Cold_Storage");
        Cold_Storage cold_store = new Cold_Storage(cold_key, userhandler.getUser(), "seller_id", order_id, Double.valueOf(itemPrice));

        int price =  Double.valueOf(itemPrice).intValue();;

        cold_ref.child(cold_key).setValue(cold_store).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userhandler.updateAccountBalance(userhandler.getUser(), -price);
                userhandler.transactionHandler("Freeze", userhandler.getUser(), "Cold_Storage", userhandler.createTransactioId(), price);
            }
        });
    }
}