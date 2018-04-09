package com.techyshed.codez.Handler;


import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (Uri.parse(url).getHost().endsWith("thecrazyprogrammer.com") || Uri.parse(url).getHost().endsWith("thejavaprogrammer.com")
        || Uri.parse(url).getHost().endsWith("codingalpha.com") || Uri.parse(url).getHost().endsWith("codeforwin.org")
    || Uri.parse(url).getHost().endsWith("w3schools.in")) {

            return false;
        }


        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }
}