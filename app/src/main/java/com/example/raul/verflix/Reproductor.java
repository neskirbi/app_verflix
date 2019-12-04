package com.example.raul.verflix;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.regex.Matcher;
public class Reproductor extends AppCompatActivity {
    WebView webview;
    String HTML="",THTML="";
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);
        webview = findViewById(R.id.navegador);



        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent=getIntent();
        THTML=intent.getStringExtra("video");


        Log.i("web_load R2 ",THTML);




        webview.getSettings().setAppCacheEnabled(false);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setJavaScriptEnabled(true);


        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

                return super.onJsAlert(view, url, message, result);
            }
        });



        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);



            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);



            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Toast.makeText(Reproductor.this, ""+url, Toast.LENGTH_SHORT).show();
                Log.i("redireccion",url);

                if (url.contains("rapidvideo.com")) {
                    // This is my web site, so do not override; let my WebView load the page
                    return false;
                }else if (url.substring(0,22).contains("fembed.com")){
                    Intent intent = new Intent(getApplicationContext(),Descarga.class);
                    intent.putExtra("URL",url);
                    startActivity(intent);

                }
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //startActivity(intent);
                return true;
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);

            }
        });





        //webview.loadDataWithBaseURL("", HTML, "text/html", "UTF-8", "");
        webview.loadUrl(THTML);
        Conectar(THTML);





    }


    void Conectar(String url){
        try {
            RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(getApplicationContext());


// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                // Display the first 500 characters of the response string.

                                String respuesta=response.replace("\n","").replace("\r","");

                            }catch (Exception ee){
                                Log.i("LOG_VOLLEY",ee.toString());
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //text.setText("That didn't work!");
                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (Exception e) {
            Log.i("LOG_VOLLEY",e.toString());
        }
    }


}
