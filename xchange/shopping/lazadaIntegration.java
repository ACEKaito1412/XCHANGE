package com.project.xchange.shopping;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.xchange.R;
import com.project.xchange.RecyclerViewAdapter;
import com.project.xchange.adapter.CartAdapter;
import com.project.xchange.model.Cart;
import com.project.xchange.model.Shop;
import com.project.xchange.adapter.shopAdapter;
import com.project.xchange.userHandler;

import java.util.ArrayList;

public class lazadaIntegration extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private ArrayList<String> idList = new ArrayList<>();

    private ArrayList<Shop> mshopItems;
    private shopAdapter shopad;
    RecyclerView recyclerView;

    private ArrayList<Cart> mcartItems;
    private CartAdapter cartad;
    RecyclerView cartRecylerView;

    private Button btn_cart;
    private Button btn_items;

    private userHandler usHandler = new userHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazada_integration);


        btn_cart = (Button) findViewById(R.id.btn_carts_view);
        btn_items = (Button) findViewById(R.id.btn_items_view);

        btn_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartRecylerView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartRecylerView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });

        getShopData();
        getCartData();
    }

    private void getShopData(){

        recyclerView = findViewById(R.id.viewerRec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mshopItems = new ArrayList<>();
        shopad = new shopAdapter(mshopItems, idList, this);
        recyclerView.setAdapter(shopad);

        DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference("Shop");
        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String Key = snapshot1.getKey();
                    Shop shop = snapshot1.getValue(Shop.class);
                    mshopItems.add(shop);
                    idList.add(Key);
                }
                shopad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getCartData(){

        cartRecylerView = findViewById(R.id.cartViewer);
        cartRecylerView.setLayoutManager(new LinearLayoutManager(this));


        mcartItems = new ArrayList<>();
        cartad = new CartAdapter(this, mcartItems);
        cartRecylerView.setAdapter(cartad);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users_information");
        DatabaseReference childRef =  ref.child(usHandler.getUser()).child("Cart");
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String Key = snapshot1.getKey();
                    Cart cart = snapshot1.getValue(Cart.class);
                    mcartItems.add(cart);
                }
                cartad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void getData(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://techpilipinas.com/wp-content/uploads/2022/07/apsp-oppo-a57-front-600x600.jpg");
        mNames.add("Oppo A57");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Rocky Mountain National Park");


        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");


        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");


        Toast.makeText(this, mImageUrls.toString(), Toast.LENGTH_LONG).show();
        initRecyclerView();


    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.viewerRec);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);

    }
    private void initRecyclerView1(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.viewerRec);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        shopAdapter adapter = new shopAdapter(mshopItems, idList, this);
        recyclerView.setAdapter(adapter);

    }
}
















