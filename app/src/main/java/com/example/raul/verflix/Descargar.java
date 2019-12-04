package com.example.raul.verflix;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class Download extends AsyncTask {
    Context ctx;
    String link;
    View view;
    String nom_archivo;
    ProgressDialog dia_des;
    Button boton,boton2;


    public Download(Context ctx, String link, View view, String nom_archivo, Button boton,Button boton2) {
        this.ctx = ctx;
        this.link = link;
        this.view = view;
        this.nom_archivo= nom_archivo;
        this.boton=boton;
        this.boton2=boton2;

    }

    @Override
    protected void onPreExecute() {
        dia_des= new ProgressDialog(this.view.getContext());
        dia_des.setMessage("Iniciado descarga...");
        Log.v("DownloadProgress", "PATH: " + this.link);

        dia_des.setCancelable(false);
        dia_des.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {

        dia_des.dismiss();
        /*Snackbar.make(view, "Descargado: 100% ", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
        boton.setVisibility(View.VISIBLE);
        //boton2.setVisibility(View.VISIBLE);
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        dia_des.setMessage("Descargando... "+values[0]+"% ");
        dia_des.show();

        super.onProgressUpdate(values);
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String uri=link;
        Log.v("DownloadProgress", "Url: "+uri);
        try {

            URL url = new URL(uri);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            int contentLength = c.getContentLength();



            String Path = Environment.getExternalStorageDirectory().getPath() + "/Download/";
            Log.v("DownloadProgress", "PATH: " + Path);
            File file = new File(Path);
            file.mkdirs();
            FileOutputStream fos = new FileOutputStream(Path+nom_archivo);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[702];
            int len1 = 0;
            int fileLength = c.getContentLength();
            long total = 0;
            int progress=0;

            while ((len1 = is.read(buffer)) != -1) {

                fos.write(buffer, 0, len1);
                total+=len1;
                progress=(int) (total * 100 / fileLength);


                if((progress%2)==0 && progress!=100)
                {
                    publishProgress(progress);
                    Log.v("DownloadProgress", "Link: "+uri+" Progres: "+progress);
                }
            }

            //Log.v("PdfManager", "Error: "+fos.toString());
            fos.close();
            is.close();


        } catch (Exception e) {
            Log.v("DownloadProgress", "Error: "+e);
        }
        Log.v("DownloadProgress", "Check: ");


        return null;
    }


}
