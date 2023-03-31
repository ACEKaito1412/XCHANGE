package com.project.xchange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class terms extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);


        webView = (WebView) findViewById(R.id.terms_and_condition);
        webView.loadUrl("https://www.termsfeed.com/live/d9322470-9779-4464-82c0-7bca4b1d476a");
    }
}