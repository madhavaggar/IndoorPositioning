package com.talentica.wifiindoorpositioning.wifiindoorpositioning.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.R;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.adapter.ReferenceReadingsAdapter;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.AccessPoint;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.IndoorProject;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.ReferencePoint;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.utils.AppContants;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by suyashg on 07/09/17.
 */

public class LocateMeActivity extends AppCompatActivity{

    private String TAG = "LocateMe";
    private String projectId;


    private ReferenceReadingsAdapter readingsAdapter = new ReferenceReadingsAdapter();
    private List<AccessPoint> apsWithReading = new ArrayList<>();
    private Map<String, List<Integer>> readings = new HashMap<>();
    private Map<String, AccessPoint> aps = new HashMap<>();

    private AddOrEditReferencePointActivity.AvailableAPsReceiver receiverWifi;

    private boolean wifiWasEnabled;
    private WifiManager mainWifi;
    private final Handler handler = new Handler();
    private boolean isCaliberating = false;
    private int readingsCount = 0;
    private boolean isEdit = false;
    private String rpId;
    private ReferencePoint referencePointFromDB;
    Double max;
    String name;
    TextView tv;
    ProgressBar pb;
    ImageView map;
    ImageButton button;
    ArrayList<String> ref;
    ArrayList<String> wifi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_me);
        mainWifi = null;
        receiverWifi = null;
        tv = (TextView)findViewById(R.id.tv_nearest_location);
        pb = (ProgressBar) findViewById(R.id.pb);
        map = (ImageView)findViewById(R.id.map);

        readingsCount=0;
        wifi= new ArrayList<String>();
        ref = new ArrayList<String>();
        wifi.add("DIRECT-ZxDESKTOP-ICRIRH3ms2X"); ref.add("game_room");
        wifi.add("XL-Healthcare"); ref.add("regdesk");
        wifi.add("Siemens");ref.add("stage");
        wifi.add("POCO PHONE");ref.add("mentor_room");
        wifi.add("connect");ref.add("hack_room");
        wifi.add("motorola one power");ref.add("dining");

        projectId = getIntent().getStringExtra("projectId");
        if (projectId == null) {
            Toast.makeText(this, "Reference point not found", Toast.LENGTH_LONG).show();
            this.finish();
        }

        if (getIntent().getStringExtra("rpId") != null) {
            isEdit = true;
            rpId = getIntent().getStringExtra("rpId");
        }
        Realm realm = Realm.getDefaultInstance();
        if (isEdit) {
            referencePointFromDB = realm.where(ReferencePoint.class).equalTo("id", rpId).findFirst();
            if (referencePointFromDB == null) {
                Toast.makeText(this, "Reference point not found", Toast.LENGTH_LONG).show();
                this.finish();
            }
            RealmList<AccessPoint> readings = referencePointFromDB.getReadings();
            for (AccessPoint ap:readings) {
                readingsAdapter.addAP(ap);
            }
            readingsAdapter.notifyDataSetChanged();
        } else {
            mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            mainWifi.setWifiEnabled(true);
            receiverWifi = new AddOrEditReferencePointActivity.AvailableAPsReceiver();
            wifiWasEnabled = mainWifi.isWifiEnabled();

            RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());

            String url = "http://10.2.89.153:8000/api/get_wifi";// <----enter your post url here

            JSONObject paramJson = new JSONObject();
            try {
                paramJson.put("body", new Gson().toJson(mainWifi.getScanResults()));
                Log.d("tag",String.valueOf(mainWifi.getScanResults().size()));
            } catch (Exception e) {
            }

            final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, paramJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            // display response
                            try {
                                name = (String) response.get("name");
                                tv.setText("You are near " + name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());

                            String url = "http://10.2.89.153:8000/api/get_image";     // <----enter your post url here
                            Log.d("url to send", url);
                            pb.setVisibility(View.VISIBLE);
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                            // Initialize a new ImageRequest
                            ImageRequest imageRequest = new ImageRequest(
                                    url,// Image URL
                                    new Response.Listener<Bitmap>() { // Bitmap listener
                                        @Override
                                        public void onResponse(Bitmap response) {
                                            // Do something with response
                                            Glide.with(getApplicationContext()).load(response).fitCenter().into(map);

                                            // Save this downloaded bitmap to internal storage


                                            // Display the internal storage saved image to image view
                                        }
                                    },
                                    0, // Image width
                                    0, // Image height
                                    ImageView.ScaleType.CENTER_CROP, // Image scale type
                                    Bitmap.Config.RGB_565, //Image decode configuration
                                    new Response.ErrorListener() { // Error listener
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // Do something with error response
                                            error.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "errormsg" + error.getMessage(), Toast.LENGTH_LONG).cancel();
                                        }
                                    }
                            );
                            pb.setVisibility(View.INVISIBLE);
                            // Add ImageRequest to the RequestQueue
                            MyRequestQueue.add(imageRequest);
                            Toast.makeText(getApplicationContext(), "positive" + response.toString(), Toast.LENGTH_LONG).show();
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
                    return MyData;
                }
            };
            MyRequestQueue.add(getRequest);

        }


        IndoorProject project = realm.where(IndoorProject.class).equalTo("id", projectId).findFirst();
        RealmList<AccessPoint> points = project.getAps();
        for (AccessPoint accessPoint : points) {
            aps.put(accessPoint.getMac_address(), accessPoint);
        }
        if (aps.isEmpty()) {
            Toast.makeText(this, "No Access Points Found", Toast.LENGTH_SHORT).show();
        }
        if (!Utils.isLocationEnabled(this)) {
            Toast.makeText(this,"Please turn on the location", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        if (!isEdit) {
            registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            Log.v(TAG, "caliberationStarted");
            if (!isCaliberating) {
                isCaliberating = true;
                refresh();
            }
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (!isEdit) {
            unregisterReceiver(receiverWifi);
            isCaliberating = false;

        }
        super.onPause();
    }

    public void refresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainWifi.startScan();
                if (readingsCount < AppContants.READINGS_BATCH) {
                    refresh();
                } else {
                    caliberationCompleted();
                }
            }
        }, AppContants.FETCH_INTERVAL);
    }

    private void caliberationCompleted() {
        isCaliberating = false;
        Log.v(TAG, "caliberationCompleted");
        Map<String, List<Integer>> values = readings;
        Log.v(TAG, "values:"+values.toString());
        for (Map.Entry<String, List<Integer>> entry : values.entrySet()) {
            List<Integer> readingsOfAMac = entry.getValue();
            Double mean = calculateMeanValue(readingsOfAMac);
            Log.v("DIFFERENT ENTRY KEYS", "entry.Key:"+entry.getKey()+" aps:"+aps);
            AccessPoint accessPoint = aps.get(entry.getKey());
            AccessPoint updatedPoint = new AccessPoint(accessPoint);
            updatedPoint.setMeanRss(mean);
            apsWithReading.add(updatedPoint);
        }
        pb.setVisibility(View.INVISIBLE);
        tv.setText("You are near to: " + name);
        for(int i=0;i<wifi.size();i++)
            if(wifi.get(i).equals(name)) {
                name=ref.get(i);
                break;
            }

        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://10.2.89.153:8000/api/locate_me?rp1=";     // <----enter your post url here

        String url2 = url + name;
        Log.d("url to send",url2);
        pb.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(
                url2,// Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        Glide.with(getApplicationContext()).load(response).fitCenter().into(map);
                        pb.setVisibility(View.INVISIBLE);

                        // Save this downloaded bitmap to internal storage


                        // Display the internal storage saved image to image view
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pb.setVisibility(View.INVISIBLE);
                        // Do something with error response
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "errormsg" + error.getMessage(), Toast.LENGTH_LONG).cancel();
                    }
                }
        );
        pb.setVisibility(View.INVISIBLE);
        // Add ImageRequest to the RequestQueue
        requestQueue.add(imageRequest);

    }

    private Double calculateMeanValue(List<Integer> readings) {
        if (readings.isEmpty()) {
            return 0.0d;
        }
        Integer sum = 0;
        for (Integer integer : readings) {
            sum = sum + integer;
        }
        double mean = Double.valueOf(sum) / Double.valueOf(readings.size());
        return mean;
    }


    class AvailableAPsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> scanResults = mainWifi.getScanResults();


            ++readingsCount;
            for (Map.Entry<String, AccessPoint> entry : aps.entrySet()) {
                String apMac = entry.getKey();
                for (ScanResult scanResult : scanResults) {
                    if (entry.getKey().equals(scanResult.BSSID)) {
                        checkAndAddApRSS(apMac, scanResult.level);
                        apMac = null;//do this after always :|
                        break;
                    }
                }
                if (apMac != null) {
                    checkAndAddApRSS(apMac, AppContants.NaN.intValue());
                }
            }
//            results.put(Calendar.getInstance(), map);

            Log.v(TAG, "Count:" + readingsCount+" scanResult:"+ scanResults.toString()+" aps:"+aps.toString());

            name = scanResults.get(0).SSID;
            for (int i = 0; i < readingsCount; ++i) {
//                Log.v(TAG, "  BSSID       =" + results.get(i).BSSID);
//                Log.v(TAG, "  SSID        ="; + results.get(i).SSID);
//                Log.v(TAG, "  Capabilities=" + results.get(i).capabilities);
//                Log.v(TAG, "  Frequency   =" + results.get(i).frequency);
//                Log.v(TAG, "  Level       =" + results.get(i).level);
//                Log.v(TAG, "---------------");
            }
        }
    }

    private void checkAndAddApRSS(String apMac, Integer level) {
        if (readings.containsKey(apMac)) {
            List<Integer> integers = readings.get(apMac);
            integers.add(level);
        } else {
            List<Integer> integers = new ArrayList<>();
            integers.add(level);
            readings.put(apMac, integers);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!wifiWasEnabled && !isEdit) {
        }
    }
}