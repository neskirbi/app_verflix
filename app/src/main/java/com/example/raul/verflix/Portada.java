package com.example.raul.verflix;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Integer.parseInt;

public class Portada extends AppCompatActivity {
    TelephonyManager telephonyManager;
    Funciones funciones=new Funciones();
    String respuesta="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portada);

        funciones.Conectar(getResources().getString(R.string.Url_visitas)+"?imei="+getImei(),getApplicationContext(),0);

        funciones.Conectar(getResources().getString(R.string.Url_updates),getApplicationContext(),1);



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
