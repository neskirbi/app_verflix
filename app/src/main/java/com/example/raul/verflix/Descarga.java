package com.example.raul.verflix;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class Descarga extends AppCompatActivity {
//https://www.fembed.com
    String URL="";
    WebView webview;
    Boolean stop=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga);
        Intent intent=getIntent();
        URL=intent.getStringExtra("URL");
        //Toast.makeText(this, ""+URL, Toast.LENGTH_SHORT).show();
        webview=findViewById(R.id.descargar);

        //webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebChromeClient(new WebChromeClient(){
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("URL-->","C: "+ cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId() );

                if (cm.message().contains("fvs.io") ){
                    Log.i("URL-->","C2: "+cm.message());
                    if(stop){
                        webview.loadUrl(cm.message());
                        stop=false;
                    }

                }
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
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
                webview.evaluateJavascript(
                        "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
                                try {
                                    byte[] charset = new byte[0];
                                    charset = html.getBytes("UTF-8");
                                    String result = new String(charset, "UTF-8");
                                    Log.d("URL-->","Codigo"+ result);
                                    //webview.loadDataWithBaseURL("", ""+result, "text/html", "UTF-8", "");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }


                                // code here
                            }
                        });
                super.onPageFinished(view, url);



            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.i("URL-->","Actual: "+webview.getUrl());
                Log.i("URL-->","Ir: "+url);
                /*webview.loadUrl("javascript:(function() { " +

                        "try{" +
                        "$('.clickdownload').on('click',function(){" +
                        "console.log($(this).attr('href'));" +
                        "alert($(this).attr('href'));" +
                        "});" +
                        //"var player=enlace.attr('href');" +
                        // "alert(player);" +


                        "}catch(err) {" +
                        "alert('Error: '+err.message);" +
                        "}" +

                        "})()");*/
                if(!url.contains("fembed.com") && webview.getUrl().contains("fembed.com")){
                    String command = "javascript:console.log('ok');";
                    webview.loadUrl(command);
                    //webview.loadUrl("javascript:  console.log('Desde consola '+$('a').html());");

                    /*webview.evaluateJavascript(
                            "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String html) {
                                    Log.d("URL-->","Codigo"+ html);
                                    // code here
                                    //webview.loadUrl(html);



                                    try {
                                        byte[] charset = new byte[0];
                                        charset = html.getBytes("UTF-8");
                                        String result = new String(charset, "UTF-8");
                                        webview.loadDataWithBaseURL("", ""+result, "text/html", "UTF-8", "");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }




                                }
                            });*/
                   /* Log.i("URL-->","Si carga controles: ");
                    webview.loadUrl("javascript:(function() { " +
                            "try{" +
                            "console.log('Desde consola '+$('a').html());" +

                            "$('a').each(function(){" +
                            "console.log($(this).attr('href'));" +
                            "});" +
                            "}catch(err) {" +
                            "console.log('Error: '+err.message);" +
                            "}" +

                            "})()");*/
                }else {
                    Log.i("URL-->", "No carga controles: ");
                }

                if ((url.contains("fembed.com") /*|| url.contains("fvs.io")*/) /*&& !url.contains("ver-pelis.me")*/) {
                    Log.i("URL-->","2: "+url);

                    webview.loadUrl(url);
                    //return true;
                    //return super.shouldOverrideUrlLoading(view, url);
                    //return false;
                }
                return true;


            }



            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);

            }
        });

        webview.setDownloadListener(new DownloadListener()
        {

            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if(verifyPermission()){

                    //for downloading directly through download manager
                    final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.allowScanningByMediaScanner();

                    request.setMimeType(mimetype);
                    //------------------------COOKIE------------------------
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    //------------------------COOKIE------------------------
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("WCMovies");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                    final DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                    new Thread("Browser download") {
                        public void run() {
                            dm.enqueue(request);
                        }
                    }.start();
                }

            }
        });

        webview.getSettings().setAppCacheEnabled(false);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(URL);


    }

    Boolean verifyPermission() {
        int permsRequestCode = 100;
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.READ_EXTERNAL_STORAGE};

        int writeExternalStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readExternalStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (readExternalStorage == PackageManager.PERMISSION_GRANTED&& writeExternalStorage == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(perms, permsRequestCode);
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        webview.loadUrl(URL);
        super.onBackPressed();
    }
}
