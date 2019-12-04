package com.example.raul.verflix;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Sugerencias extends AppCompatActivity {

    EditText mail,msn;
    String correo,mensaje;
    Button enviar;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerencias);

        enviar=findViewById(R.id.enviar);
        mail=findViewById(R.id.mail);
        msn=findViewById(R.id.msn);


        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Sugerencias.this, ""+StrToB64(msn.getText().toString()), Toast.LENGTH_SHORT).show();
                if(!mail.getText().toString().replace(" ","").equals("") && !msn.getText().toString().replace(" ","").equals("") ){
                    Enviar(getResources().getString(R.string.Url_sugerencias ),mail.getText().toString(),msn.getText().toString());
                }else{
                    Toast.makeText(Sugerencias.this, "Debes de llenar los dos campos", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }



    String StrToB64(String texto){
        String str64="";
        str64 = Base64.encodeToString(texto.getBytes(), Base64.DEFAULT);

        return str64;
    }

    void Enviar(final String url,final String correo, final String mensaje){

        Log.i("Log_Com",url);

        try {
            final RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(getApplicationContext());

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {


                            try {
                                Log.i("Log_Com", "" + "R: " + response);
                                JSONObject object = new JSONObject(response);


                                if (parseInt(object.getString("status")) == 1) {
                                    Toast.makeText(Sugerencias.this, "Mensaje Enviado", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(Sugerencias.this, "Error al enviar", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception ee) {
                                Log.i("Log_Com", ee.toString());

                            }
                        }


                            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Log_Com",error.toString());
                    Toast.makeText(Sugerencias.this, "Sin conexion al servidor", Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                public Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String> ();
                    JSONObject objeto =new JSONObject();
                    try {
                        objeto.put("mail",StrToB64(correo));
                        objeto.put("msn",StrToB64(mensaje));
                        objeto.put("imei",getImei());
                        mail.setText("");
                        msn.setText("");
                    } catch (Exception e) {
                        Log.i("Log_com",e.getMessage());
                    }
                    Log.i("Log_com",objeto+"");
                    params.put("objeto", objeto+"");

                    return params;
                }
            };

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (Exception e) {
            Log.i("Log_Com",e.toString());
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
