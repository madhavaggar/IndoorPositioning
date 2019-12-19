package com.talentica.wifiindoorpositioning.wifiindoorpositioning.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Crowd_Activity extends AppCompatActivity {
    int total;
    JSONArray v = new JSONArray();
    String s;
    ArrayList<BarEntry> yvalues = new ArrayList<>();
    BarDataSet dataSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crowd_activity);
        final BarChart chart = findViewById(R.id.barchart);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://10.2.89.153:8000/api/rpfreq?rp=1";     // <----enter your post url here
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
                        try {
                            total = (Integer) response.get("total_visits");
                            s = (String) response.get("crowd_freq");
                            String[] arr = s.split(",");
                            for (int i = 0; i < arr.length; i++) {
                                yvalues.add(new BarEntry(Float.parseFloat(arr[i].trim()), (int) Float.parseFloat(String.valueOf(i).trim())));
                                Log.i("Tag", yvalues.get(i).toString());
                            }
                            dataSet = new BarDataSet(yvalues, "Values");
                            dataSet.setDrawValues(true);
                            ArrayList<String> year = new ArrayList<String>();

                            year.add("7-8");
                            year.add("8-9");
                            year.add("9-10");
                            year.add("10-11");
                            year.add("11-12");
                            year.add("12-13");
                            year.add("13-14");
                            year.add("14-15");
                            year.add("15-16");
                            year.add("16-17");
                            year.add("17-18");
                            year.add("18-19");
                            year.add("19-20");
                            year.add("20-21");

                            if (dataSet != null) {
                                BarData data = new BarData(year, dataSet);
                                chart.setData(data);
                                chart.setDescription("Crowd Frequency");
                                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                chart.animateY(3000);
                            }

                            Toast.makeText(getApplicationContext(), String.valueOf(v.length()), Toast.LENGTH_LONG).show();
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
                MyData.put("rp", "1");
                return MyData;
            }
        };
        MyRequestQueue.add(getRequest);
    }
}
