package com.project.xchange.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.xchange.R;
import com.project.xchange.item_View;
import com.project.xchange.model.Shop;

import java.util.List;

public class shopAdapter extends RecyclerView.Adapter<shopAdapter.shopHolder> {
    private static final String TAG = "shopAdapter";

    List<Shop> shopItem;
    List<String> idList;
    Context context;

    public shopAdapter(List<Shop> shopItem, List<String> idList, Context context) {
        this.shopItem = shopItem;
        this.context = context;
        this.idList = idList;
    }

    @NonNull
    @Override
    public shopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        shopHolder holder = new shopHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull shopHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Shop shop = shopItem.get(position);

        holder.name.setText(shop.getItem_Name());
        Glide.with(context).load(shop.getImage()).into(holder.imageView);
        holder.price.setText(String.valueOf("Price: Php. "+shop.getPrice()));
        holder.str_id = idList.get(position);
        holder.str_image = shop.getImage();
        holder.str_name = shop.getItem_Name();
        holder.str_price = String.valueOf(shop.getPrice());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, item_View.class);
                intent.putExtra("itemKey", holder.str_id);
                intent.putExtra("itemImage", holder.str_image);
                intent.putExtra("itemPrice", holder.str_price);
                intent.putExtra("itemName", holder.str_name);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return shopItem.size();
    }

    //    holder
    public class shopHolder extends RecyclerView.ViewHolder{
        TextView name, price;
        ImageView imageView;
        LinearLayout parentLayout;
        String str_id, str_name, str_image, str_price;

        public shopHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.imageView);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
}
}
