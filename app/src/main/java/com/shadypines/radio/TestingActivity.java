package com.shadypines.radio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class TestingActivity extends AppCompatActivity {
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        webview = findViewById(R.id.webview);
        webview.loadUrl("https://www.shadypinesradio.com/schedule");
    }
}