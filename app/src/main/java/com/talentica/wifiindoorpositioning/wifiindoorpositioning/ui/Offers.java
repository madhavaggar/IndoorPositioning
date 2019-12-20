package com.talentica.wifiindoorpositioning.wifiindoorpositioning.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Offers extends AppCompatActivity {

    String name;
    TextView t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        t1=findViewById(R.id.offers1);
        t2=findViewById(R.id.offers2);
        name="game_room" ;
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://10.2.89.153:8000/api/get_offers?rp=";// <----enter your post url here
        url=url + name;
        JSONObject paramJson = new JSONObject();
        try {
            paramJson.put("rp", "1");
        } catch (Exception e) {
        }

        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        GsonBuilder builder = new GsonBuilder();
                        Gson mgson=builder.create();
                        OfferItem offers1 = mgson.fromJson(response.getString("1").toString(),OfferItem.class);
                        OfferItem offers2 = mgson.fromJson(response.getString("2").toString(),OfferItem.class);

                        try {
                             offers1 =(OfferItem) response.get("1");
                             offers2 =(OfferItem) response.get("2");
                             t1.setText(offers1.rpname);
                             t2.setText(offers2.rpname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getApplicationContext(), "positive" + response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "errormsg" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("rp", name);
                return MyData;
            }
        };
        MyRequestQueue.add(getRequest);

    }
}
