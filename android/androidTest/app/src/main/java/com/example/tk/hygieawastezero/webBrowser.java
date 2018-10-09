package com.example.tk.hygieawastezero;

import android.webkit.WebView;
import android.webkit.WebViewClient;
public class webBrowser extends WebViewClient {

    private static webBrowser INSTANCE = null;

    String token = "";
    boolean tokenChanged = false;

    private webBrowser(){};

    public static webBrowser getINSTANCE() {
        if(INSTANCE == null){
            INSTANCE = new webBrowser();
        }
        return(INSTANCE);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        if (url.startsWith("hygieawastezero://"))
        {
            token = url.substring(18);
            tokenChanged = true;
        }
        else
        {
            view.loadUrl(url);
        }
        return true;
    }
    public String getToken(){
        return token;
    }
    public boolean getTokenChanged(){
        return tokenChanged;
    }
}
