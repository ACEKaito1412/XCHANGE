package com.project.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class money_rates extends AppCompatActivity {
    private static final String TAG = "money_rates";


    Button gobackbtn;

    String[] isoCode = {"USD", "EUR", "JPY", "GBP","AUD","CAD","CHF","CNY","HKD","NZD"};

    TextView usd_price, eur_price, jpy_price, gbp_price, aud_price, cad_price, chf_price, cny_price, hkd_price, nzd_price;

    RequestQueue mQuer;

    public static final int TASK_A = 1;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TASK_A:
                    Log.d(TAG, "handleMessage: Finish Loading Data");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_rates);

        mQuer = Volley.newRequestQueue(this);

        gobackbtn = (Button) findViewById(R.id.goback_btn);
        gobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        {"USD", "EUR", "JPY", "GBP","AUD","CAD","CHF","CNY","HKD","NZD"};
        usd_price = (TextView) findViewById(R.id.usd_price);
        eur_price = (TextView) findViewById(R.id.eur_price);
        jpy_price = (TextView) findViewById(R.id.jpy_price);
        gbp_price = (TextView) findViewById(R.id.gb_price);
        aud_price = (TextView) findViewById(R.id.aud_price);
        cad_price = (TextView) findViewById(R.id.cad_price);
        chf_price = (TextView) findViewById(R.id.chf_price);
        cny_price = (TextView) findViewById(R.id.cny_price);
        hkd_price = (TextView) findViewById(R.id.hkd_price);
        nzd_price = (TextView) findViewById(R.id.nzd_price);

        parseJson();
    }

    private void parseJson(){

        DecimalFormat df = new DecimalFormat("0.00");

        Runnable runnableObj = new Runnable() {
            @Override
            public void run() {
                String url = "https://api.exchangerate.host/query?base=PHP";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                double msg2 = 0;
                                try {
                                    JSONObject msg = response.getJSONObject("rates");
//                                    "USD", "EUR", "JPY", "GBP","AUD","CAD","CHF","CNY","HKD","NZD"
                                    usd_price.setText("Php. " + df.format(1 / msg.getDouble("USD")));
                                    eur_price.setText("Php. " + df.format(1 / msg.getDouble("EUR")));
                                    jpy_price.setText("Php. " + df.format(1 / msg.getDouble("JPY")));
                                    gbp_price.setText("Php. " + df.format(1 / msg.getDouble("GBP")));
                                    aud_price.setText("Php. " + df.format(1 / msg.getDouble("AUD")));
                                    cad_price.setText("Php. " + df.format(1 / msg.getDouble("CAD")));
                                    chf_price.setText("Php. " + df.format(1 / msg.getDouble("CHF")));
                                    cny_price.setText("Php. " + df.format(1 / msg.getDouble("CNY")));
                                    hkd_price.setText("Php. " + df.format(1 / msg.getDouble("HKD")));
                                    nzd_price.setText("Php. " + df.format(1 / msg.getDouble("NZD")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                mQuer.add(request);
                handler.sendEmptyMessage(TASK_A);
            }
        };
        Thread ebgThread = new Thread(runnableObj);
        ebgThread.start();
    }
}
