package com.shadypines.radio.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;

import com.shadypines.radio.R;
import com.shadypines.radio.utils.AppSettings;
import com.shadypines.radio.view.activity.TempActivity;

import im.delight.android.webview.AdvancedWebView;


public class CommonFragment extends Fragment {
    View view;

    AdvancedWebView webview;
    ProgressBar pbload;
    RelativeLayout rl_load;
    TempActivity activity;
    ScrollView scroll;
    TextView textChatMarque;
    ConstraintLayout constraintLayout;

    TextView CloseKey;
    EditText OpenKey;


//     SwipeRefreshLayout pullToRefresh ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_common, container, false);
        init();


        int height = 650;

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(Constraints.LayoutParams.MATCH_PARENT, height);


        OpenKey = view.findViewById(R.id.openkey);
        CloseKey = view.findViewById(R.id.closeKey);

        OpenKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.setLayoutParams(layoutParams);


            }
        });





        CloseKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout.LayoutParams Params = new ConstraintLayout.LayoutParams(Constraints.LayoutParams.MATCH_PARENT,Constraints.LayoutParams.MATCH_PARENT);
                webview.setLayoutParams(Params);
                InputMethodManager imm = (InputMethodManager)
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        activity = ((TempActivity) getActivity());
        webview = view.findViewById(R.id.webview);
        //todo: when the chat text is change in android...
        textChatMarque = view.findViewById(R.id.text_marque_chat);
        textChatMarque.setSelected(true);
        textChatMarque.setText(AppSettings.title + " - " + AppSettings.album + "    ");

//        pullToRefresh = view.findViewById(R.id.pullToRefresh);

//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                fnRefreshView();
//                pullToRefresh.setRefreshing(false);
//            }
//        });

        // scroll = view.findViewById(R.id.scroll);

        if (Build.VERSION.SDK_INT >= 21) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //FOR WEBPAGE SLOW UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        newWebview();

//        LoadUrlWebView(activity.loadUrl);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void newWebview() {
        webview.loadUrl(activity.loadUrl);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setDomStorageEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
//        myWebview.setLayerType(WebView.LAYER_TYPE_NONE, null);
        if (Build.VERSION.SDK_INT >= 21) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //FOR WEBPAGE SLOW UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                activity.loadUrl = url;
                activity.fnLoadInnerPage();
//                Toast.makeText(getActivity(),url,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    private void LoadUrlWebView(String url_api) {
        try {

            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webview.setVerticalScrollBarEnabled(true);
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return super.shouldOverrideUrlLoading(view, request);
//                    Toast.makeText(getActivity(),"Working",Toast.LENGTH_SHORT).show();
                }
            });
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            webview.setWebChromeClient(new WebChromeClient());

            webview.setWebViewClient(new WebViewClient());
            webview.setWebChromeClient(new WebChromeClient());
            webview.setWebViewClient(new WebViewClient());
//            webview.setWebChromeClient(new MyWebChromeClient( url_api ));
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);


            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setBuiltInZoomControls(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
//            webview.addJavascriptInterface(
//                    new WebAppInterface(mParentActivity.getApplicationContext()),
//                    "Android");


            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setLoadWithOverviewMode(true);

            webview.setVerticalScrollBarEnabled(true);
            webview.setHorizontalScrollBarEnabled(true);
            webview.requestFocusFromTouch();


            webview.getSettings().setUseWideViewPort(true);
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


            if (Build.VERSION.SDK_INT >= 21) {
                webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            //FOR WEBPAGE SLOW UI
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            webview.getSettings().setSupportZoom(true);
            webview.getSettings().setAllowContentAccess(true);
            webview.getSettings().setBuiltInZoomControls(true);
            webview.getSettings().setDisplayZoomControls(false);


            webview.setVerticalScrollBarEnabled(true);
            webview.requestFocus();

            webview.getSettings().setDefaultTextEncodingName("utf-8");
            webview.getSettings().setJavaScriptEnabled(true);

            webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportZoom(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setUseWideViewPort(true);

            webview.loadUrl(url_api);
        } catch (Exception e) {
            Log.w("TAG", "setUpNavigationView", e);
        }
    }


    private class MyWebChromeClient extends WebViewClient {
        private String urlAccount;

//        public MyWebChromeClient(String urlAccount) {
//            this.urlAccount = urlAccount;
//        }
//
//        @Override
//        public void onProgressChanged(WebView view, int newProgress) {
//            {
//                if(newProgress == 100){
//                    pbload.setVisibility(View.GONE);
//                    rl_load.setVisibility(View.GONE);
//
//                }else {
//                    if (activity.loadUrl.equalsIgnoreCase("http://m.youtube.com/shadypinesmedia")){
//                        pbload.setVisibility(View.GONE);
//                        rl_load.setVisibility(View.GONE);
//                    }else {
//                        pbload.setVisibility(View.VISIBLE);
//                        rl_load.setVisibility(View.VISIBLE);
//                    }
//                }
//
//            }
//
//        }
//
//        @Override
//        public void onReceivedTitle(WebView view, String title) {
//            super.onReceivedTitle(view, title);
//
////            sharedPreferences = new Shared_Preferences(context);
////            sharedPreferences.setPageWebView(view.getUrl());
//        }


    }

    @Override
    public void onResume() {
        super.onResume();
//        LoadUrlWebView(activity.loadUrl);
    }

    public void fnRefreshView() {
        webview = view.findViewById(R.id.webview);
        scroll.fullScroll(ScrollView.FOCUS_UP);
        webview.loadUrl(null);
        activity = ((TempActivity) getActivity());
        LoadUrlWebView(activity.loadUrl);


    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager)
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}