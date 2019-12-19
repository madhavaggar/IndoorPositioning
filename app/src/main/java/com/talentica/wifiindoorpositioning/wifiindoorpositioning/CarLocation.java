package com.talentica.wifiindoorpositioning.wifiindoorpositioning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.R;


public class CarLocation extends AppCompatActivity {
    Button button1, button2, button3;
    ProgressBar progressBar;
    ImageView map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_location);
        button1 = (Button) findViewById(R.id.buttonfind);
        button2 = (Button) findViewById(R.id.buttonremove);
        button3 = (Button) findViewById(R.id.buttonnav);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        map = (ImageView) findViewById(R.id.map);



       // Glide.with(getApplicationContext()).load(R.drawable.seimens).fitCenter().into(map);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://10.2.89.153:8000/parking/api/park_avail";     // <----enter your post url here
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
                        progressBar.setVisibility(View.INVISIBLE);
                        // Do something with error response
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "errormsg" + error.getMessage(), Toast.LENGTH_LONG).cancel();
                    }
                }
        );
        // Add ImageRequest to the RequestQueue
        requestQueue.add(imageRequest);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    progressBar.setVisibility(View.VISIBLE);
                    RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = "http://10.2.89.153:8000/parking/api/locate_car?uid=1";     // <----enter your post url here
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    // Initialize a new ImageRequest
                    ImageRequest imageRequest = new ImageRequest(
                            url,// Image URL
                            new Response.Listener<Bitmap>() { // Bitmap listener
                                @Override
                                public void onResponse(Bitmap response) {
                                    // Do something with response
                                    Glide.with(getApplicationContext()).load(response).fitCenter().into(map);
                                    progressBar.setVisibility(View.INVISIBLE);

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
                                    progressBar.setVisibility(View.INVISIBLE);
                                    // Do something with error response
                                    error.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "errormsg" + error.getMessage(), Toast.LENGTH_LONG).cancel();
                                }
                            }
                    );
                    progressBar.setVisibility(View.INVISIBLE);
                    // Add ImageRequest to the RequestQueue
                    requestQueue.add(imageRequest);

            }
        });

     button2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://10.2.89.153:8000/parking/api/leave_view?uid=1";     // <----enter your post url here
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                // Initialize a new ImageRequest
                ImageRequest imageRequest = new ImageRequest(
                        url,// Image URL
                        new Response.Listener<Bitmap>() { // Bitmap listener
                            @Override
                            public void onResponse(Bitmap response) {
                                // Do something with response
                                Glide.with(getApplicationContext()).load(response).fitCenter().into(map);
                                progressBar.setVisibility(View.INVISIBLE);

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
                                progressBar.setVisibility(View.INVISIBLE);
                                // Do something with error response
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(), "errormsg" + error.getMessage(), Toast.LENGTH_LONG).cancel();
                            }
                        }
                );
                progressBar.setVisibility(View.INVISIBLE);
                // Add ImageRequest to the RequestQueue
                requestQueue.add(imageRequest);

        }
    });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    progressBar.setVisibility(View.VISIBLE);
                    RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = "http://10.2.89.153:8000/parking/api/park_view?uid=1&pname=p1";     // <----enter your post url here
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    // Initialize a new ImageRequest
                    ImageRequest imageRequest = new ImageRequest(
                            url,// Image URL
                            new Response.Listener<Bitmap>() { // Bitmap listener
                                @Override
                                public void onResponse(Bitmap response) {
                                    // Do something with response
                                    Glide.with(getApplicationContext()).load(response).fitCenter().into(map);
                                    progressBar.setVisibility(View.INVISIBLE);

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
                                    progressBar.setVisibility(View.INVISIBLE);
                                    // Do something with error response
                                    error.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "errormsg" + error.getMessage(), Toast.LENGTH_LONG).cancel();
                                }
                            }
                    );
                    progressBar.setVisibility(View.INVISIBLE);
                    // Add ImageRequest to the RequestQueue
                    requestQueue.add(imageRequest);

            }
        });
    }
}
