package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.xchange.model.Cart;
import com.project.xchange.shopping.lazadaIntegration;

public class item_View extends AppCompatActivity {

    private TextView item_name, item_key, item_price;
    private ImageView item_image;

    private Button addCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        item_name = (TextView) findViewById(R.id.text_productName);
        item_image  = (ImageView) findViewById(R.id.itemImage);
        item_price = (TextView) findViewById(R.id.text_productPrice);

        String itemKey = getIntent().getStringExtra("itemKey");
        String itemImage = getIntent().getStringExtra("itemImage");
        String itemPrice = getIntent().getStringExtra("itemPrice");
        String itemName = getIntent().getStringExtra("itemName");

        Glide.with(getApplicationContext()).load(itemImage).into(item_image);
        item_name.setText("Product Name: "+itemName);
        item_price.setText("Php. "+itemPrice);

        userHandler uhandler = new userHandler();


        addCart = (Button) findViewById(R.id.btn_add_Cart);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users_information");
                DatabaseReference childRef =  ref.child(uhandler.getUser()).child("Cart").child(itemKey);


                childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
//                      fire this section
                            childRef.child("quantity").setValue(ServerValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(item_View.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), lazadaIntegration.class));
                                    finish();
                                }
                            });
                        }else{
                            Cart cart = new Cart(itemKey, itemName, itemPrice, itemImage, 1, "Added");
                            childRef.setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(item_View.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), lazadaIntegration.class));
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}