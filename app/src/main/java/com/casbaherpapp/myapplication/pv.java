package com.casbaherpapp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class pv extends AppCompatActivity  {



private String branche;

private int id;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pventes);
        Intent i = getIntent();

        Bundle extras = i.getExtras();
        if(extras.containsKey("branche")) {
           branche = i.getStringExtra("branche");
            map();

        }
        if(extras.containsKey("id")) {
            id= Integer.parseInt(i.getStringExtra("id"));
            mapp();

        }




    }
    public void map(){


        TouchyWebView myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String url = "http://casbahdz.com/css/pventes.php";
        String postData = null;
        try {
            postData = "branche=" + URLEncoder.encode(String.valueOf(branche), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        myWebView.postUrl(url,postData.getBytes());


    }


    public void mapp(){


        TouchyWebView myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String url = "http://casbahdz.com/css/pvente.php";
        String postData = null;
        try {
            postData = "id=" + URLEncoder.encode(String.valueOf(id), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        myWebView.postUrl(url,postData.getBytes());


    }







}
