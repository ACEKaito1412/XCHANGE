package com.project.xchange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class privacy extends AppCompatActivity {

//    connect to webview'
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        webView = (WebView) findViewById(R.id.privacy_id);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.freeprivacypolicy.com/live/75c090b7-d663-42ed-b880-12b1dea280d3");
    }
}