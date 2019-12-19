package com.talentica.wifiindoorpositioning.wifiindoorpositioning.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.adapter.NearbyReadingsAdapter;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.core.Algorithms;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.core.WifiService;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.IndoorProject;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.LocDistance;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.LocationWithNearbyPlaces;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.WifiData;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.utils.AppContants;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class BackendDataSend extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private WifiData mWifiData;
    private Algorithms algorithms = new Algorithms();
    private String projectId, defaultAlgo;
    private IndoorProject project;
    private MainActivityReceiver mReceiver = new MainActivityReceiver();
    private Intent wifiServiceIntent;

    private LinearLayoutManager layoutManager;
    private NearbyReadingsAdapter readingsAdapter = new NearbyReadingsAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedpreferences = getApplicationContext().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        mWifiData = null;

        // set receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(AppContants.INTENT_FILTER));

        // launch WiFi service
        wifiServiceIntent = new Intent(this, WifiService.class);
        startService(wifiServiceIntent);

        // recover retained object
        mWifiData = (WifiData) getLastNonConfigurationInstance();

        // set layout
        initUI();

        defaultAlgo = Utils.getDefaultAlgo(this);
        projectId = getIntent().getStringExtra("projectId");
        if (projectId == null) {
            Toast.makeText(getApplicationContext(), "Project Not Found", Toast.LENGTH_LONG).show();
            this.finish();
        }
        Realm realm = Realm.getDefaultInstance();
        project = realm.where(IndoorProject.class).equalTo("id", projectId).findFirst();
        Log.v("LocateMeActivity", "onCreate");

        finish();
    }

    private void initUI() {
        layoutManager = new LinearLayoutManager(this);

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mWifiData;
    }

    public class MainActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("LocateMeActivity", "MainActivityReceiver");
            mWifiData = (WifiData) intent.getParcelableExtra(AppContants.WIFI_DATA);

            if (mWifiData != null) {
                LocationWithNearbyPlaces loc = Algorithms.processingAlgorithms(mWifiData.getNetworks(), project, Integer.parseInt(defaultAlgo));
                Log.v("LocateMeActivity", "loc:" + loc);
                if (loc == null) {
                    Toast.makeText(getApplicationContext(),"Location: NA\nNote:Please switch on your wifi and location services with permission provided to App", Toast.LENGTH_LONG).show();
                } else {
                    String locationValue = Utils.reduceDecimalPlaces(loc.getLocation());
                    Toast.makeText(getApplicationContext(),"Location: " + locationValue, Toast.LENGTH_LONG).show();
                    String theDistancefromOrigin = Utils.getTheDistancefromOrigin(loc.getLocation());
                    LocDistance theNearestPoint = Utils.getTheNearestPoint(loc);
                    Toast.makeText(getApplicationContext(),"Location near: " + theNearestPoint.getName(), Toast.LENGTH_LONG).show();

                    final String loc1,time,current;
                   final Timestamp currenttime;
                    if (theNearestPoint != null) {
                        loc1 = sharedpreferences.getString("loc","def");
                        time = sharedpreferences.getString("time","def");
                        current = theNearestPoint.getName();
                        currenttime = new Timestamp(System.currentTimeMillis());
                        if(loc1.equals("def")) {
                            editor.putString("loc",current);
                            editor.putString("time", String.valueOf(currenttime));
                            editor.commit();
                        }
                        else {
                            if (!current.equals(loc1)) {
                                editor.putString("loc", current);
                                editor.putString("time", String.valueOf(currenttime));
                                editor.commit();

                                RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
                                String url = "http://192.168.1.7:8000/api/entry/";     // <----enter your post url here
                                JSONObject paramJson = new JSONObject();
                                try {
                                    paramJson.put("rname", loc1);
                                    paramJson.put("start_time", time);
                                    paramJson.put("end_time", String.valueOf(currenttime));
                                    paramJson.put("userid", "1234");
                                } catch (Exception e) {
                                }

                                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, paramJson,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                // display response
                                                try {
                                                    String token = (String) response.get("status");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
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
                                        MyData.put("rname", loc1);
                                        MyData.put("start_time", time);
                                        MyData.put("end_time", String.valueOf(currenttime));
                                        MyData.put("userid", "1234");
                                        return MyData;
                                    }
                                };
                                MyRequestQueue.add(getRequest);
                            } else {
                                Toast.makeText(getApplicationContext(), "Same Loc no Change", Toast.LENGTH_LONG).show();
                            }
                        }
                        }
                    }
                }
            }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        stopService(wifiServiceIntent);
    }
}

