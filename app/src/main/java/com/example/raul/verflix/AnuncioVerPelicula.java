package com.example.raul.verflix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


import com.android.animatedgif.Utils.GifImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;



public class AnuncioVerPelicula extends AppCompatActivity  {
    Button verpeli;
    String frame="";
    WebView webview;
    private AdView mAdView;
    private RewardedVideoAd mRewardedVideoAd;
    int reward=0;
    String URL="";
    Funciones funciones=new Funciones();
    GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_ver_pelicula);
        Intent intent=getIntent();

        URL=intent.getStringExtra("URL");


        webview = findViewById(R.id.navegador);
        verpeli=findViewById(R.id.verpeli);

        View someView = findViewById(R.id.a);
        View root = someView.getRootView();

        // Set the color
        root.setBackgroundColor(getApplication().getResources().getColor(R.color.Fondo));

        funciones.Conectar(getResources().getString(R.string.Url_vpeliculas)+"?imei="+getImei()+"&url="+URL,getApplicationContext(),0);

        //Ver Gif en GifImageView
        gifImageView = findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.c3);




        /*
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/


        if(intent.getStringExtra("iframe")!=null){

            frame=intent.getStringExtra("iframe");
            gifImageView.setGifImageResource(R.drawable.l2);
            Toast.makeText(getApplicationContext(), "Video Listo", Toast.LENGTH_SHORT).show();
            verpeli.setEnabled(true);
            verpeli.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.botones_azul));
            verpeli.setTextColor(getApplication().getResources().getColor(R.color.Blanco));
            verpeli.setText("Ver Video");


        }else{
            webview.loadUrl(URL);
        }

        verpeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reward==1 && frame!="")
                {
                    //Toast.makeText(AnuncioVerPelicula.this, ""+frame, Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(), Reproductor.class);
                    intent2.putExtra("video", frame);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                }else if(frame==""){
                    Toast.makeText(AnuncioVerPelicula.this, "Error al cargar el video", Toast.LENGTH_SHORT).show();

                }else if(reward!=1){
                    Toast.makeText(AnuncioVerPelicula.this, "Primero tiene que ver el anuncio", Toast.LENGTH_SHORT).show();
                }

            }
        });

        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //Log.i("web_load",newProgress+"");
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //if(url.contains("/ver/")) {
                Log.d("web_load","-->"+ message);

                frame=message;
                if(frame!="")
                {
                    gifImageView.setGifImageResource(R.drawable.l2);
                    Toast.makeText(getApplicationContext(), "Video Listo: ", Toast.LENGTH_SHORT).show();
                    verpeli.setEnabled(true);
                    verpeli.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.botones_azul));
                    verpeli.setTextColor(getApplication().getResources().getColor(R.color.Blanco));
                    verpeli.setText("Ver Video");
                }else{
                    Toast.makeText(AnuncioVerPelicula.this, "Error al cargar el video", Toast.LENGTH_SHORT).show();
                }
                //}
                result.confirm();
                return true;
            }
        });

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(url.contains("/ver/")){
                    webview.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url.contains("/ver/")){
                    String ancho=""+webview.getWidth();

                    webview.loadUrl("javascript:(function() { " +

                            "try{" +
                            "var ancho=$(\"body\").width();" +
                            "var tplayer=$(\"#frame-player\");" +
                            /*"tplayer.css({\"width\": \"100%\",\"height\":ancho+\"px\"});" +
                            "var player=tplayer[0].outerHTML;" +
*/                          "alert(tplayer.attr('src'));" +
                            //"alert(tplayer);" +

                            //  "$('body').html(player);" +

                            "}catch(err) {" +
                            "alert(err.message);" +
                            "}" +

                            "})()");

                    //webview.setVisibility(View.VISIBLE);

                }else{
                    //webview.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /*if (Uri.parse(url).getHost().contains("ver-pelis.tv")) {
                    // This is my web site, so do not override; let my WebView load the page
                    return false;
                }*/
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //startActivity(intent);
                return false;
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);

            }
        });

        webview.getSettings().setAppCacheEnabled(false);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setJavaScriptEnabled(true);





        /////Anuncio

        if (!BuildConfig.DEBUG) {
            Toast.makeText(this, "Cargando...", Toast.LENGTH_SHORT).show();
            MobileAds.initialize(this, "ca-app-pub-2655019425304215~9114663184");

        }else{
            Toast.makeText(this, "Debugin", Toast.LENGTH_SHORT).show();
            MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        }

        //Prueba
        //MobileAds.initialize(this, "ca-app-pub-3940256099942544/5224354917");


        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getApplicationContext());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem reward) {
                // Toast.makeText(getApplicationContext(), "onRewarded! currency: " + reward.getType() + "  amount: " + reward.getAmount(), Toast.LENGTH_SHORT).show();
                // Reward the user.
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
               Log.i("anuncio","onRewardedVideoAdLeftApplication"); //Toast.makeText(getApplicationContext(), "onRewardedVideoAdLeftApplication",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                if (BuildConfig.DEBUG) {

                    reward=1;
                }
                Log.i("anuncio","onRewardedVideoAdClosed");//Toast.makeText(getApplicationContext(), "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                Log.i("anuncio","onRewardedVideoAdFailedToLoad");
                reward=1;

            }

            @Override
            public void onRewardedVideoAdLoaded() {
                Log.i("anuncio","onRewardedVideoAdLoaded");
                //Toast.makeText(getApplicationContext(), "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Log.i("anuncio","onRewardedVideoAdOpened");
                //Toast.makeText(getApplicationContext(), "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Log.i("anuncio","onRewardedVideoStarted");
                //Toast.makeText(getApplicationContext(), "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {
                Log.i("anuncio","onRewardedVideoCompleted");
                reward=1;




            }
        });

        loadRewardedVideoAd();

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }


    }

    private void loadRewardedVideoAd() {
        //ca-app-pub-3940256099942544/5224354917

        if (!BuildConfig.DEBUG) {
            mRewardedVideoAd.loadAd("ca-app-pub-2655019425304215/8595788433",
                    new AdRequest.Builder().build());

        }else{
            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                    new AdRequest.Builder().build());

        }

    }




    private String getImei() {
        //StringBuilder builder = new StringBuilder();
        //.append()
        String imei="";
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        try{
            imei =tm.getDeviceId(); // Obtiene el imei  or  "352319065579474";
        }catch (Exception e){}

        return imei;
        //TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //mngr.getDeviceId();
    }
}
