package com.project.xchange.bill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.xchange.R;
import com.project.xchange.model.Create_Bill;

import java.util.ArrayList;

public class bill_status extends AppCompatActivity implements View.OnClickListener{

    private Button btn_internet, btn_water, btn_electric, btn_goback;

    private ListView view_internet, view_water, view_electric;
    private ArrayAdapter<String> arrayAdapterInternet, arrayAdapterWater, arrayAdapterElectric;
    private ArrayList<String> list_internet, list_water, list_electric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_status);

        btn_electric = (Button) findViewById(R.id.btn_electric_view);
        btn_internet = (Button) findViewById(R.id.btn_internet_view);
        btn_water = (Button) findViewById(R.id.btn_water_view);
        btn_goback = (Button) findViewById(R.id.goback_btn);

        btn_internet.setOnClickListener(this);
        btn_water.setOnClickListener(this);
        btn_electric.setOnClickListener(this);
        btn_goback.setOnClickListener(this);

        view_water = findViewById(R.id.water_view);
        list_water = new ArrayList<String>();
        arrayAdapterWater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_water);
        view_water.setAdapter(arrayAdapterWater);

        view_internet = findViewById(R.id.internet_view);
        list_internet = new ArrayList<String>();
        arrayAdapterInternet = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_internet);
        view_internet.setAdapter(arrayAdapterInternet);

        view_electric = findViewById(R.id.electric_view);
        list_electric = new ArrayList<String>();
        arrayAdapterElectric = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_electric);
        view_electric.setAdapter(arrayAdapterElectric);


        DatabaseReference bills = FirebaseDatabase.getInstance().getReference("Create_Bills");
        bills.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_electric.clear();
                list_water.clear();
                list_internet.clear();
                for (DataSnapshot data:snapshot.getChildren()){
                    String key = data.getKey();
                    for (DataSnapshot data_child : data.getChildren()){
                        String key2 = data_child.getKey();
                        Create_Bill db = data_child.getValue(Create_Bill.class);
                        if(key.equals("Electric")) {
                            if(db.getBill_status().equals("Unpaid")){
                                String output = "Ref. ID: \t" + key2 + "\nCompany: \t"
                                        + db.getCompany_name() + "\nCustomer: \t"
                                        + db.getCustomer_name() + "\nAmount: \t"
                                        + db.getAmount() + "\nStatus: \t"
                                        + db.getBill_status();
                                list_electric.add(output);
                            }
                        }
                        if(key.equals("Water")) {
                            if(db.getBill_status().equals("Unpaid")){
                                String output = "Ref. ID: \t" + key2 + "\nCompany: \t"
                                        + db.getCompany_name() + "\nCustomer: \t"
                                        + db.getCustomer_name() + "\nAmount: \t"
                                        + db.getAmount() + "\nStatus: \t"
                                        + db.getBill_status();
                                list_water.add(output);
                            }
                        }
                        if(key.equals("Internet")) {
                            if(db.getBill_status().equals("Unpaid")){
                                String output = "Ref. ID: \t" + key2 + "\nCompany: \t"
                                        + db.getCompany_name() + "\nCustomer: \t"
                                        + db.getCustomer_name() + "\nAmount: \t"
                                        + db.getAmount() + "\nStatus: \t"
                                        + db.getBill_status();
                                list_internet.add(output);
                            }
                        }
                    }
                }
            if(arrayAdapterWater.getCount() < 1){
                    String output = "No Bills Found";
                    list_water.add(output);
            }

            if(arrayAdapterElectric.getCount() < 1){
                    String output = "No Bills Found";
                    list_electric.add(output);
            }

            if(arrayAdapterInternet.getCount() < 1){
                    String output = "No Bills Found";
                    list_internet.add(output);
            }

            arrayAdapterWater.notifyDataSetChanged();
            arrayAdapterInternet.notifyDataSetChanged();
            arrayAdapterElectric.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_electric_view:
                view_electric.setVisibility(View.VISIBLE);
                view_internet.setVisibility(View.GONE);
                view_water.setVisibility(View.GONE);
                break;
            case R.id.btn_internet_view:
                view_electric.setVisibility(View.GONE);
                view_internet.setVisibility(View.VISIBLE);
                view_water.setVisibility(View.GONE);
                break;
            case R.id.btn_water_view:
                view_electric.setVisibility(View.GONE);
                view_internet.setVisibility(View.GONE);
                view_water.setVisibility(View.VISIBLE);
                break;
            case R.id.goback_btn:
                finish();
                break;
        }
    }
}