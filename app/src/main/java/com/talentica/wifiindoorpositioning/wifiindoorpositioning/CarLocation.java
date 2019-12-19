package com.talentica.wifiindoorpositioning.wifiindoorpositioning;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class CarLocation extends AppCompatActivity {

    EditText x,y;

    private RandomCanvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_location);
        x = findViewById(R.id.editText1);
        y = findViewById(R.id.editText2);
        canvas= (RandomCanvas)findViewById(R.id.canvas);
    }

    void ManualPlace(View view){
        canvas.x = Float.parseFloat(x.getText().toString());
        canvas.y = Float.parseFloat(y.getText().toString());
        canvas.invalidate();
    }
}

