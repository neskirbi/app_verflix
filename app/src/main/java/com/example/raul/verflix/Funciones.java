package com.example.raul.verflix;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static java.lang.Integer.parseInt;

public class Funciones {
    String respuesta="{\"status\":\"2\",\"desc\":\"Error de Conexion\"}";

Context context;
    void Conectar(String url,Context con, final int opc){
        context=con;
        Log.i("Log_Visitas",url);

        try {
            final RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(context);

            final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{

                                Log.i("Log_Visitas",response);
                                respuesta=""+response;

                                switch (opc){
                                    case 1:

                                        try {
                                            Log.i("Log_Visitas", ""+ "R: "+respuesta);
                                            JSONObject object=new JSONObject(respuesta);
                                            Log.i("Log_Visitas", "Obj-version: "+ object.getString("version"));
                                            Log.i("Log_Visitas", "Obj-nombre: "+ object.getString("nombre"));

                                            Log.i("Log_Visitas", "Obj-version-apk: "+ parseInt( context.getResources().getString(R.string.app_version )) );

                                            if ( parseInt(object.getString("version"))>parseInt( context.getResources().getString(R.string.app_version )) ){
                                                Intent intent=new Intent(context,Update.class);
                                                intent.putExtra("URL",object.getString("url"));
                                                intent.putExtra("nombre",object.getString("nombre"));
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);

                                            }else {
                                                Iniciar();
                                            }
                                        } catch (Exception e) {
                                            Log.i("Log_Visitas",e.getMessage());
                                            Iniciar();
                                        }
                                        break;
                                }
                            }catch (Exception ee){
                                Log.i("Log_Visitas",ee.toString());

                                Iniciar();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    switch (opc){
                        case 1:
                        Log.i("Log_Visitas",error.toString());
                        Iniciar();
                        break;
                    }

                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (Exception e) {
            Log.i("LOG_VOLLEY",e.toString());
        }
    }

    void Iniciar(){

                Intent mainIntent = new Intent(context, Home.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainIntent);

    }



    void Descargar(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch(FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }





}
