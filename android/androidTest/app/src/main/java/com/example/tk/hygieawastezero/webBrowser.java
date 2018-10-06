package com.example.tk.hygieawastezero;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webBrowser extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        view.loadUrl(url);
        return true;
    }
}
