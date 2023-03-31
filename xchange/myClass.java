package com.project.xchange;


import static android.content.Context.WINDOW_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import android.view.WindowManager;

public class myClass {

    public boolean netIsAvailable() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateReferenceData(DatabaseReference reference, HashMap map, String path){
        try {
            if(reference.child(path) != null){
                reference.child(path).updateChildren(map);
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public boolean updateReferenceData(DatabaseReference reference, Double amount, String path){
        try {
            if(reference.child(path) != null){
                reference.child(path).setValue(ServerValue.increment(amount));
                return true;
            }
        }catch (Exception e){
            return  false;
        }
        return false;
    }

    public void logOut(FirebaseAuth mAuth){
        mAuth.getInstance().signOut();
        return;
    }

    public void loadLayout(LinearLayout layout){
        if(layout.getVisibility() == View.GONE){
            layout.setVisibility(View.VISIBLE);
        }else{
            layout.setVisibility(View.GONE);
        }

    }




}
