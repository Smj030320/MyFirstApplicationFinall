package com.jnu.student;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class BaiduMapFrag extends Fragment{
    public BaiduMapFrag() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View baidurootview = inflater.inflate(R.layout.fragment_baidumap, container, false);
        WebView webView = baidurootview.findViewById(R.id.webView_baidu);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient()); // 设置WebViewClient，用于处理页面加载和各种通知
        webView.loadUrl("https://map.baidu.com"); // 加载网页
        return baidurootview;
    }
}