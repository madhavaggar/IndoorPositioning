package com.talentica.wifiindoorpositioning.wifiindoorpositioning.ui;

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


public class Navigation extends AppCompatActivity {
    EditText to,from;
    Button button;
    ProgressBar progressBar;
    ImageView map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        to = (EditText) findViewById(R.id.to);
        from = (EditText)findViewById(R.id.from);
        button = (Button) findViewById(R.id.buttonnav);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        map = (ImageView)findViewById(R.id.map);
        Glide.with(getApplicationContext()).load(R.drawable.seimens).fitCenter().into(map);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String loc1 = from.getText().toString();
                final String loc2 = to.getText().toString();

                if (loc1.isEmpty())
                    from.setError("Enter From Location");
                if (loc2.isEmpty())
                    to.setError("Enter To Location");
                if (!loc1.isEmpty() && !loc2.isEmpty()) {

                    progressBar.setVisibility(View.VISIBLE);
                    RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = "http://10.2.89.153:8000/api/draw_path?rp1=";     // <----enter your post url here
                    String url2 = url + loc1.trim()+"&rp2="+loc2.trim();
                    Log.d("url to send",url2);
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    // Initialize a new ImageRequest
                    ImageRequest imageRequest = new ImageRequest(
                            url2,// Image URL
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
            }
        });
    }
}
