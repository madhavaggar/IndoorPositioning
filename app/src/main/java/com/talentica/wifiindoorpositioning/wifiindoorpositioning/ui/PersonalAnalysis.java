package com.talentica.wifiindoorpositioning.wifiindoorpositioning.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.R;

import java.util.ArrayList;

public class PersonalAnalysis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_analysis);
        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new Entry(1.2f, 0));
        NoOfEmp.add(new Entry(.40f, 1));
        NoOfEmp.add(new Entry(3.3f, 2));
        NoOfEmp.add(new Entry(0.4f, 3));
        NoOfEmp.add(new Entry(0.5f, 4));
        NoOfEmp.add(new Entry(2.3f, 5));
        NoOfEmp.add(new Entry(1.5f, 6));
        NoOfEmp.add(new Entry(1.2f, 7));
        ;
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Places");

        ArrayList year = new ArrayList();

        year.add("dining");
        year.add("hack_room");
        year.add("game_room");
        year.add("regdesk");
        year.add("stage");
        year.add("office");
        year.add("parking");
        year.add("canteen");


        PieData data = new PieData(year, dataSet);
        pieChart.setData(data);
        pieChart.setDescriptionColor(Color.WHITE);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieChart.animateXY(5000, 5000);
    }
}
