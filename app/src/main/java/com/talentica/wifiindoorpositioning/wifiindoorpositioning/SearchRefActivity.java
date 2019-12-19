package com.talentica.wifiindoorpositioning.wifiindoorpositioning;

import android.content.ClipData;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import io.realm.Realm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ramotion.foldingcell.FoldingCell;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.adapter.holder.ReferencePointSection;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.IndoorProject;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.ReferencePoint;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


public class SearchRefActivity extends AppCompatActivity {

    String projectId;
    private IndoorProject project;
    private SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
    private ReferencePointSection rpSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ref);

        // get our list view
        final ListView theListView = findViewById(R.id.mainListView);
        projectId = getIntent().getStringExtra("projectId");
        if (projectId == null) {
            Toast.makeText(getApplicationContext(), "No projected selected", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Realm realm = Realm.getDefaultInstance();
            project = realm.where(IndoorProject.class).equalTo("id", projectId).findFirst();
            // prepare elements to display
            final List<ReferencePoint> items = project.getRps();//

            // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
            final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, items);

            // set elements to adapter
            theListView.setAdapter(adapter);


            // set on click event listener to list view
            theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    // toggle clicked cell state
                    ((FoldingCell) view).toggle(false);
                    // register in adapter that state for selected cell is toggled
                    adapter.registerToggle(pos);
                }
            });

        }
    }
}