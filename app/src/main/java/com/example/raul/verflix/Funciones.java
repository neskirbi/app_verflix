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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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

    public String Conexion(String data, String url) {

        String result = "";


        try {

            //v.vibrate(50);

            Log.i("Conexion", "Enviando: "+url+"    "+data);

            //Create a URL object holding our url
            URL myUrl = new URL(url);
            //Create a connection
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod("POST");
            connection.setReadTimeout(1500);
            connection.setConnectTimeout(1500);
            //connection.addRequestProperty("pr","33");

            //Connect to our url
            connection.connect();


            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            //Log.i("datos",getPostDataString(postDataParams));
            writer.write("data="+data);

            writer.flush();
            writer.close();
            os.close();


            //Create a new InputStreamReader

            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //stringBuilder.append(data);

            //Check if the line we are reading is not null
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
            Log.i("Conexion","Recibiendo: "+result);
            return result;


        } catch (Exception ee) {
            Log.i("Conexion", "Error_conexion: "+ ee.getMessage());
            return  "";
        }



    }
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


                            }catch (Exception ee){
                                Log.i("Log_Visitas",ee.toString());

                                //Iniciar();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (Exception e) {
            Log.i("LOG_VOLLEY",e.toString());
        }
    }

    void Iniciar(Context cont){
        context=cont;

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
