package com.example.raul.verflix;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Update extends AppCompatActivity {
   Funciones funciones=new Funciones();
   Button descarga;
   String URL="";
   TextView narchivo;

    View view;
    Button descargar,instalar,explorar;
    String nombre="",nombref="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        descarga=findViewById(R.id.descargar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            verifyPermission();
        }


        setContentView(R.layout.activity_update);
        funciones.Conectar("",getApplicationContext(),2);
        Intent intent=getIntent();
        URL=intent.getStringExtra("URL");
        nombre=intent.getStringExtra("nombre");
        nombref=nombre.replace(".zip",".apk");

        narchivo=findViewById(R.id.narchivo);
        descargar=findViewById(R.id.descargar);
        instalar=findViewById(R.id.instalar);
        explorar=findViewById(R.id.explorar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        view = getWindow().getDecorView().getRootView();
        narchivo.setText("Descargar: "+nombre.replace(".zip",".apk"));
        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Download descargar=new Download(getApplicationContext(),URL+nombre,view,nombref,instalar,explorar);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(verifyPermission()){
                        descargar.execute();
                    }
                }else{
                    descargar.execute();
                }


            }
        });


        instalar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Path = Environment.getExternalStorageDirectory().getPath() + "/Download/"+nombref;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File( Path )), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        explorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }

    Boolean verifyPermission() {
        int permsRequestCode = 100;
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE};
        int accessFinePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarseLocation = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        int writeExternalStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readExternalStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int readPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);

        if (readPhoneState == PackageManager.PERMISSION_GRANTED && accessFinePermission == PackageManager.PERMISSION_GRANTED && accessCoarseLocation == PackageManager.PERMISSION_GRANTED && readExternalStorage == PackageManager.PERMISSION_GRANTED&& writeExternalStorage == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(perms, permsRequestCode);
            return false;
        }
    }



}
