package com.talentica.wifiindoorpositioning.wifiindoorpositioning.ui;

import android.os.Bundle;
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

import java.nio.channels.FileLock;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crowd_Activity extends AppCompatActivity {
    int total;
    ArrayList<Integer> values;
    JSONArray v = new JSONArray();
    String s;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crowd_activity);
        BarChart chart = findViewById(R.id.barchart);

        ArrayList NoOfEmp = new ArrayList();


        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://10.2.89.153:8000/api/rpfreq?rp=1";     // <----enter your post url here
        JSONObject paramJson = new JSONObject();
        try {
            paramJson.put("rp", "1");
        } catch (Exception e) {
        }

        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            total = (Integer) response.get("total_visits");

                    /*
                            NoOfEmp.add(new BarEntry(1, 0));
                            NoOfEmp.add(new BarEntry(2, 1));
                            NoOfEmp.add(new BarEntry(3, 2));

                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[3]), 3));
                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[4]), 4));
                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[5]), 5));
                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[6]), 6));
                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[7]), 7));
                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[8]), 8));
                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[9]), 9));
                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[10]), 10));
                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[11]), 11));
                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[12]), 12));
                            NoOfEmp.add(new BarEntry(Float.valueOf(arr[13]), 13));
                            */

                            Toast.makeText(getApplicationContext(),String.valueOf(v.length()),Toast.LENGTH_LONG).show();
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

        NoOfEmp.add(new BarEntry(0, 0));
        NoOfEmp.add(new BarEntry(10f, 1));
        NoOfEmp.add(new BarEntry(13f, 2));
        NoOfEmp.add(new BarEntry(12f, 3));
        NoOfEmp.add(new BarEntry(1f, 4));
        NoOfEmp.add(new BarEntry(14f, 5));
        NoOfEmp.add(new BarEntry(15f, 6));
        NoOfEmp.add(new BarEntry(18f, 7));
        NoOfEmp.add(new BarEntry(18f, 8));
        NoOfEmp.add(new BarEntry(1f, 9));

        ArrayList year = new ArrayList();

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
            /*
            year.add("17-18");
            year.add("18-19");
            year.add("19-20");
            year.add("20-21");
            */


        BarDataSet bardataset = new BarDataSet(NoOfEmp, "Crowd Frequency");
        chart.animateY(3000);
        BarData data = new BarData(year, bardataset);
        bardataset.setColors(ColorTemplate.JOYFUL_COLORS);
        chart.setData(data);
    }
}