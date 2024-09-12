package com.shadypines.radio.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shadypines.radio.R;

public class Admin_panal_Activity extends AppCompatActivity {

    private WebView webView;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panal);

        progressDialog=new ProgressDialog(this);
        // progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wite...");

        webView=(WebView)findViewById(R.id.webview_id);
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        getUrl();
    }

    void getUrl(){

        Bundle  bundle=getIntent().getExtras();

        if(bundle!=null){

          String url=  bundle.getString("url");
            webView.loadUrl(url);
            progressDialog.dismiss();
        }
    }
}