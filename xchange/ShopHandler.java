package com.project.xchange;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShopHandler {
    public void addItem(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Shop");

    }
    public void viewItem(int itemId){

    }

    public void deleteItem(int itemID){}

    public void addToCart(int itemId){

    }
}
