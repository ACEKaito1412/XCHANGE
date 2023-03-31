package com.project.xchange.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.xchange.R;
import com.project.xchange.item_confirm;
import com.project.xchange.model.Cart;
import com.project.xchange.shopping.lazadaIntegration;
import com.project.xchange.userHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    private Context context;
    private ArrayList<Cart> cartArrayList;


    public CartAdapter(Context context, ArrayList<Cart> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
    }


    @NonNull
    @Override
    public CartAdapter.CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false);
        CartHolder holder = new CartHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartHolder holder, int position) {
        Cart cart = cartArrayList.get(position);

        holder.text_name.setText(cart.getProduct_name());
        holder.text_price.setText("Item Price: "+ cart.getProduct_price());
        holder.text_quantity.setText("Quantity: "+String.valueOf(
                Integer.valueOf(cart.getQuantity())));




        if(cart.getStatus() == "Confirm"){
            holder.text_status.setText("Status: " + cart.getStatus());
            holder.text_status.setVisibility(View.VISIBLE);
            holder.cfrm_btn.setVisibility(View.GONE);
            holder.delete_btn.setVisibility(View.GONE);
        }else{
            holder.text_status.setVisibility(View.GONE);
            holder.cfrm_btn.setVisibility(View.VISIBLE);
            holder.delete_btn.setVisibility(View.VISIBLE);
        }

        Double total = Integer.valueOf(cart.getQuantity()) * Double.valueOf(cart.getProduct_price());
        holder.text_totalP.setText("Total: \n"+String.valueOf(total));


        Glide.with(context).load(cart.getImage_url()).into(holder.imgView);

        holder.str_id = cart.getKey();
        holder.str_image =cart.getImage_url();
        holder.str_name = cart.getProduct_name();
        holder.str_quantity = String.valueOf(cart.getQuantity());
        holder.str_price = String.valueOf(total);

        holder.cfrm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, item_confirm.class);
                intent.putExtra("itemKey", String.valueOf(holder.str_id));
                intent.putExtra("itemImage", holder.str_image);
                intent.putExtra("itemPrice", holder.str_price);
                intent.putExtra("itemName", holder.str_name);
                intent.putExtra("itemQuantity", holder.str_quantity);
                context.startActivity(intent);
            }
        });

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.child_ref.child(cart.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Item has been Removed from cart", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, lazadaIntegration.class));
                    }
                });
            }
        });



    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {

        private TextView text_name, text_price, text_status, text_quantity, text_totalP;
        private ImageView imgView;

        private userHandler userhandler;

        private DatabaseReference child_ref;

        private Button cfrm_btn, delete_btn;

        String str_id, str_name, str_image, str_price, str_quantity;

        public CartHolder(@NonNull View itemView) {
            super(itemView);

            text_name = (TextView) itemView.findViewById(R.id.itemName);
            text_price = (TextView) itemView.findViewById(R.id.itemPrice);
            text_status = (TextView) itemView.findViewById(R.id.itemStatus);
            text_quantity = (TextView) itemView.findViewById(R.id.itemQuantity);
            text_totalP = (TextView) itemView.findViewById(R.id.itemTotalPrice);

            imgView = (ImageView) itemView.findViewById(R.id.item_image);

            cfrm_btn = (Button) itemView.findViewById(R.id.confirm_btn);
            delete_btn = (Button) itemView.findViewById(R.id.delete_btn);

            userhandler = new userHandler();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users_information");
            child_ref =  ref.child(userhandler.getUser()).child("Cart").getRef();
        }
    }
}
