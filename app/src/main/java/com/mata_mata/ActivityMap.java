package com.mata_mata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ActivityMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        WebView web1 = (WebView) findViewById(R.id.web1);
        WebSettings web1Settings = web1.getSettings();
        web1Settings.setJavaScriptEnabled(true);
        web1.loadUrl("http://www.google.com");
    }
}
