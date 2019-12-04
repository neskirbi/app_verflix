package com.example.raul.verflix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Animeflv.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Animeflv#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Animeflv extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    String Data="",HTML="",URL="https://animeflv.ru";
    String regex2="<a href=\"https:\\/\\/animeflv.ru(.*?)>(.*?)<\\/a>";
    String estilo="",regex="<li\\s*class=.TPostMv.>(.*?)<\\/li>",regex21="<main>(.*?)<\\/main>",regex22="<table>(.*?)<\\/table>",regexf="<iframe(.*?)>(.*?)<\\/iframe>";
    WebView webview;
    ImageButton b_buscar,boton;
    EditText e_buscar,editText;
    View view;
    LinearLayout lay;

    int width;
    public Animeflv() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Animeflv.
     */
    // TODO: Rename and change types and number of parameters
    public static Animeflv newInstance(String param1, String param2) {
        Animeflv fragment = new Animeflv();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_animeflv, container, false);
        webview = view.findViewById(R.id.navegador);




        //Configurando barra de busqueda
        //Obteniendo tamaño de pantalla
        Display display = ((AppCompatActivity)getActivity()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;



        //ActionBar actionBar2 = getActivity().getActionBar();
        // add the custom view to the action bar
        //actionBar.setCustomView(R.layout.search);
        //search = (EditText) actionBar.getCustomView().findViewById(R.id.search_user);


        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        /*actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View lay = inflator.inflate(R.layout.busqueda, null);
        actionBar.setCustomView(lay);*/
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.busqueda);
        //b_buscar=view.findViewById(R.id.b_buscar);
        //e_buscar=view.findViewById(R.id.e_buscar);
        e_buscar =  actionBar.getCustomView().findViewById(R.id.e_buscar);
        b_buscar =  actionBar.getCustomView().findViewById(R.id.b_buscar);
        Log.i("log_objeto",""+b_buscar);

        e_buscar.getLayoutParams().width= (int) Math.round(.50*width);





        b_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Conectar(URL+"/?nombre="+e_buscar.getText().toString().replace(" ","+"),1);
            }
        });




        // If your extra data is represented as strings, then you can use intent.getStringExtra(String name) method. In your case:

        //String HTML = intent.getStringExtra("Data");

        //webview.setWebChromeClient(new WebChromeClient());

        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

                Log.d("LogTag", message);
                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                Conectar(message,2);


                result.confirm();
                return true;
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
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);

            }
        });


        webview.getSettings().setJavaScriptEnabled(true);



        Conectar(URL,1);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    void Conectar(final String url, final int opc){
        try {
            RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(getActivity());
            //String url ="http://www.google.com";

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                // Display the first 500 characters of the response string.

                                Data=response.replace("\n","").replace("\r","");
                                Log.i("respuesta",Data);
                                String body="",tbody="";
                                int conter=0;

                                Pattern patternf = Pattern.compile(regexf, Pattern.MULTILINE);
                                Matcher matcherf = patternf.matcher(Data);

                                while (matcherf.find()) {
                                    //System.out.println("Full match: " + matcher.group(0));
                                    // Toast.makeText(Conexion.this, ""+"Full match: " + matcher.group(0), Toast.LENGTH_SHORT).show();
                                    body=body+matcherf.group(0);
                                    Log.i("framing",body);
                                    conter++;
                                    break;

                                }
                                if (conter>0){
                                    Intent intent=new Intent(getContext(),AnuncioVerPelicula.class);
                                    intent.putExtra("iframe",body);
                                    intent.putExtra("URL",url);
                                    startActivity(intent);
                                }else{

                                    switch (opc){
                                        case 1:

                                            Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                                            Matcher matcher = pattern.matcher(Data);



                                            while (matcher.find()) {
                                                //System.out.println("Full match: " + matcher.group(0));
                                                // Toast.makeText(Conexion.this, ""+"Full match: " + matcher.group(0), Toast.LENGTH_SHORT).show();
                                                tbody=tbody+matcher.group(0);


                                            }


                                            Pattern pattern2 = Pattern.compile(regex2, Pattern.MULTILINE);
                                            Matcher matcher2 = pattern2.matcher(tbody);



                                            while (matcher2.find()) {
                                                //System.out.println("Full match: " + matcher.group(0));
                                                // Toast.makeText(Conexion.this, ""+"Full match: " + matcher.group(0), Toast.LENGTH_SHORT).show();
                                                body=body+matcher2.group(0);
                                                Log.i("regexing",body);
                                                conter++;


                                            }


                                            if(conter==0){
                                                body="<img src=\"http://www.frilligallery.com/img/common/noitemsfound.png\" width=\"100%\"/>";
                                            }

                                            body=body.replace("<h2","<p").replace("</h2","</p");
                                            Log.i("regex",body);
                                            HTML="<!DOCTYPE html>\n" +
                                                    "<html lang=\\\"es\\\">\n" +
                                                    "<head>\n" +
                                                    "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>" +
                                                    "<link rel=\"stylesheet\"  href=\"https://cwarezmovies.000webhostapp.com/css/estilowikiserie.css\" type=\"text/css\" />" +
                                                    "</head>\n" +
                                                    "<style>\n" +
                                                    "a{\n" +
                                                    "\t\ttext-decoration: none;\n" +
                                                    "\t\tposition: relative;\n" +
                                                    "\t\tcolor: #fff;\n" +
                                                    "size:9px; " +
                                                    "\t}\n" +
                                                    "\tbody{\n" +
                                                    "\t\tpadding: 0px;\n" +
                                                    "\t\tmargin: 0px;\n" +
                                                    "\t\tbackground-color:#191F26; \n" +
                                                    "\t}" +
                                                    "#main{\n" +
                                                    "\t\t\tposition: fixed;\n" +
                                                    "\t\t\twidth: 100%;\n" +
                                                    "            background: #249FD7;\n" +
                                                    "            font-size:20px;\n" +
                                                    "            color:#ffffff;" +
                                                    "z-index: 1000;\n" +
                                                    "            \n" +
                                                    "\n" +
                                                    "\t\t}" +
                                                    "</style>\n" +
                                                    "<meta charset=\\\"utf-8\\\" />\n" +
                                                    "<title>VEFLIX</title>\n" +
                                                    "<body class=\"home\">" +
                                                    "<center>" +
                                                    "<div id=\"main\"><img src=\"https://images-na.ssl-images-amazon.com/images/I/610aD3uOsRL.png\" height=\"45px\" style=\"vertical-align: middle;\"><b>WMovies</b></div>" +
                                                    "<br><br><br>"+body+
                                                    "" +
                                                    "</center></body>" +
                                                    "<script>" +
                                                    "$(\"a\").on( \"click\",function(eve) {" +
                                                    "try{" +
                                                    "eve.preventDefault();" +
                                                    "alert($(this).attr(\"href\"));" +
                                                    "}catch(err){" +
                                                    "alert(err.message);" +
                                                    "}" +
                                                    "});" +
                                                    "</script>" +
                                                    "</html>";
                                            Log.i("HTML",HTML);

                                            webview.loadDataWithBaseURL("", HTML, "text/html", "UTF-8", "");


                                            //Log.i("LOG_VOLLEY",response);


                                            break;
                                        case 2:


                                            Pattern pattern21 = Pattern.compile(regex21, Pattern.MULTILINE);
                                            Matcher matcher21 = pattern21.matcher(Data);




                                            while (matcher21.find()) {
                                                //System.out.println("Full match: " + matcher.group(0));
                                                // Toast.makeText(Conexion.this, ""+"Full match: " + matcher.group(0), Toast.LENGTH_SHORT).show();
                                                tbody=tbody+matcher21.group(0).replace("//image.tmdb.org","https://image.tmdb.org").replace("&quot;","\"").replace("&lt;","<").replace("&gt;",">");
                                                Log.i("regexing2",tbody);


                                            }

                                            Pattern pattern22 = Pattern.compile(regex22, Pattern.MULTILINE);
                                            Matcher matcher22 = pattern22.matcher(tbody);




                                            while (matcher22.find()) {
                                                //System.out.println("Full match: " + matcher.group(0));
                                                // Toast.makeText(Conexion.this, ""+"Full match: " + matcher.group(0), Toast.LENGTH_SHORT).show();
                                                body=body+"<span class=\"tempo\">Temporada "+(conter+1)+"</span>"+matcher22.group(0);
                                                Log.i("regexing22",body);
                                                conter++;

                                            }


                                            if(conter==0){
                                                body="<img src=\"http://www.frilligallery.com/img/common/noitemsfound.png\" width=\"100%\"/>";
                                            }

                                            body=body.replace("<h2","<p").replace("</h2","</p");
                                            Log.i("regex",body);
                                            HTML="<!DOCTYPE html>\n" +
                                                    "<html lang=\\\"es\\\">\n" +
                                                    "<head>\n" +
                                                    "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>" +
                                                    "<link rel=\"stylesheet\"  href=\"https://cwarezmovies.000webhostapp.com/css/estilowikiserie.css\" type=\"text/css\" />" +
                                                    "</head>\n" +
                                                    "<style>" +
                                                    ".Num{" +
                                                    "color:#fff;" +
                                                    "}\n" +
                                                    ".Title{" +
                                                    "size:15px;" +
                                                    "color:#fff;" +
                                                    "}" +
                                                    ".tempo{" +
                                                    "color:#fff;" +
                                                    "size:15px;" +
                                                    "}" +
                                                    "a{\n" +
                                                    "\t\ttext-decoration: none;\n" +
                                                    "\t\tposition: relative;\n" +
                                                    "\t\tcolor: #fff;\n" +
                                                    "size:9px; " +
                                                    "\t}\n" +
                                                    "\tbody{\n" +
                                                    "\t\tpadding: 0px;\n" +
                                                    "\t\tmargin: 0px;\n" +
                                                    "\t\tbackground-color:#191F26; \n" +
                                                    "\t}" +
                                                    "#main{\n" +
                                                    "\t\t\tposition: fixed;\n" +
                                                    "\t\t\twidth: 100%;\n" +
                                                    "            background: #249FD7;\n" +
                                                    "            font-size:20px;\n" +
                                                    "            color:#ffffff;" +
                                                    "z-index: 1000;\n" +
                                                    "            \n" +
                                                    "\n" +
                                                    "\t\t}" +
                                                    "</style>\n" +
                                                    "<meta charset=\\\"utf-8\\\" />\n" +
                                                    "<title>VEFLIX</title>\n" +
                                                    "<body class=\"home\">" +
                                                    "<center>" +
                                                    "<div id=\"main\"><img src=\"https://images-na.ssl-images-amazon.com/images/I/610aD3uOsRL.png\" height=\"45px\" style=\"vertical-align: middle;\"><b>WMovies</b></div>" +
                                                    "<br><br><br>"+body+
                                                    "" +
                                                    "</center></body>" +
                                                    "<script>" +
                                                    "$(\"a\").on( \"click\",function(eve) {" +
                                                    "try{" +
                                                    "eve.preventDefault();" +
                                                    "alert($(this).attr(\"href\"));" +
                                                    "}catch(err){" +
                                                    "alert(err.message);" +
                                                    "}" +
                                                    "});" +
                                                    "</script>" +
                                                    "</html>";
                                            Log.i("HTML",HTML);

                                            webview.loadDataWithBaseURL("", HTML, "text/html", "UTF-8", "");


                                            //Log.i("LOG_VOLLEY",response);
                                            break;
                                    }

                                }







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
