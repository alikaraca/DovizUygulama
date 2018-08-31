package com.example.snow_.dovizuygulama;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String baseUrl = "https://www.doviz.com/api/v1/currencies/all/latest";// döviz değerlerini çektiğimiz api
    String url;
    RequestQueue requestQueue,requestQueue1;
    Spinner spinner;
    TextView txt;
    private ArrayAdapter <String> ilkKur;
    String sembol,deger;
    List<String> list=new ArrayList<>();
    List<String> list1=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner=(Spinner) findViewById(R.id.spinner);
        txt=(TextView) findViewById(R.id.textView);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue1=Volley.newRequestQueue(this);
        Task task=new Task();
        task.execute();

    }
    public void aktifle(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                degerGetir();
                //long degerId=spinner.getSelectedItemId(); //hangi veriyi seçtiysek onun değerini getiriyoruz.
                //txt.setText(list1.get((int) degerId));//burada da textview e yazdırma işlemini yapıyoruz.
                txt.setText(list1.get((int) id));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void son(){
        ilkKur = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list); //degerGetir fonksiyonunda listeye attıgımız verileri spinner a ekliyoruz.
        ilkKur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ilkKur);
    }
    public void isimGetir(){
        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, baseUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //verinin olup olmadığını sorguluyoruz.
                        if (response.length() > 0) {
                            // kaç veri varsa o kadar dönecek for döngüsünü oluşturuyoruz.
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObj = response.getJSONObject(i);
                                    sembol = jsonObj.get("full_name").toString();//json formatında hangi veriyi çekecekseniz onu yazıyorsunuz.
                                    list.add(i,sembol);//burada listeye ekledik.
                                    son(); //bu fonksiyona gitmesini belirttik.
                                    aktifle();
                                } catch (JSONException e) {

                                    Log.e("Volley", "JSON Nesnesi Bulunamadı.");
                                }
                            }
                        } else {
                            Log.e("Volley", "Veri Yok.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                }
        );
        requestQueue.add(arrReq);
    }
    public void degerGetir(){
        JsonArrayRequest arrReq1 = new JsonArrayRequest(Request.Method.GET, baseUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response1) {
                        //verinin olup olmadığını sorguluyoruz.
                        if (response1.length() > 0) {
                            // kaç veri varsa o kadar dönecek for döngüsünü oluşturuyoruz.
                            for (int i = 0; i < response1.length(); i++) {
                                try {
                                    JSONObject jsonObj1 = response1.getJSONObject(i);
                                    deger = jsonObj1.get("selling").toString();
                                    list1.add(i,deger);
                                } catch (JSONException e) {
                                    Log.e("Volley", "JSON Nesnesi Bulunamadı.");
                                }
                            }
                        } else {
                            Log.e("Volley", "Veri Yok.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                }
        );
        requestQueue1.add(arrReq1);
    }
    public class Task extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isimGetir();
        }

        @Override
        protected String doInBackground(String... params) {
            degerGetir();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            aktifle();
        }
    }
}