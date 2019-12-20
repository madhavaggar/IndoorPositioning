package com.talentica.wifiindoorpositioning.wifiindoorpositioning.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.talentica.wifiindoorpositioning.wifiindoorpositioning.CarLocation;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.R;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.SearchRefActivity;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.IndoorProject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class StartPage extends AppCompatActivity {
    CardView locateme,locatecar,crowd,personal,navigate,offers;
    ImageView createnewproject;
    private RealmResults<IndoorProject> projects;
    private String projectId;
    private int PERM_REQ_CODE_RP_ACCESS_COARSE_LOCATION = 198;
    private int PERM_REQ_CODE_LM_ACCESS_COARSE_LOCATION = 197;

    private Realm realm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        projectId = getIntent().getStringExtra("projectId");
        locateme = (CardView)findViewById(R.id.locateme);
        locatecar = (CardView)findViewById(R.id.locatecar);
        navigate = (CardView)findViewById(R.id.navigate);
        offers=(CardView) findViewById(R.id.offers);

        locatecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        crowd = (CardView)findViewById(R.id.crowd);
        personal = (CardView)findViewById(R.id.personal);
        createnewproject = (ImageView)findViewById(R.id.createnewproj);

        createnewproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"select floor act", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StartPage.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        // Create a new empty instance of Realm

        // Clear the realm from last time
//        Realm.deleteRealm(realmConfiguration);

        realm = Realm.getInstance(realmConfiguration);
        if(projectId!=null) {

            projects = realm.where(IndoorProject.class).findAll();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 5 * 60 * 1000); // every 5 minutes
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERM_REQ_CODE_LM_ACCESS_COARSE_LOCATION);
                        //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                    } else {
                        if(projectId!=null)
                        startSendDataActivity();
                    }
                }
            }, 5* 60 * 1000); // first run after 5 minutes

        }
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StartPage.this,PersonalAnalysis.class);
                startActivity(intent);
            }
        });
        locateme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (projectId == null) {
                    Toast.makeText(getApplicationContext(), "Project Not Found", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(StartPage.this, LocateMeActivity.class);
                    intent.putExtra("projectId", projectId);
                    startActivity(intent);
                }
            }
        });
        locatecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPage.this, CarLocation.class);
                startActivity(intent);
            }
        });
        crowd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartPage.this, SearchRefActivity.class);
                intent.putExtra("projectId", projectId);
                startActivity(intent);
            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPage.this,Navigation.class);
                intent.putExtra("projectId", projectId);
                startActivity(intent);
            }
        });

        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPage.this,Offers.class);
                intent.putExtra("projectId", projectId);
                startActivity(intent);
            }
        });
    }

    private void startSendDataActivity() {
        Intent intent = new Intent(this,BackendDataSend.class);
        intent.putExtra("projectId", projectId);
        startActivity(intent);
    }

}
