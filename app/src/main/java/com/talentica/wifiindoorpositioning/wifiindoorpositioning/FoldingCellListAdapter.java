package com.talentica.wifiindoorpositioning.wifiindoorpositioning;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ramotion.foldingcell.FoldingCell;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.ReferencePoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<ReferencePoint> {

    int total;
    JSONArray v = new JSONArray();
    String s;
    ArrayList<BarEntry> yvalues = new ArrayList<>();
    BarDataSet dataSet;

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    public FoldingCellListAdapter(Context context, List<ReferencePoint> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        ReferencePoint item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.refname = cell.findViewById(R.id.refname);
            viewHolder.refimage = cell.findViewById(R.id.refimage);
            viewHolder.barChart = cell.findViewById(R.id.barchart);
            viewHolder.contentRequestBtn = cell.findViewById(R.id.content_request_btn);
            cell.setTag(viewHolder);
        } else {

            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item) {
            return cell;
        }

        // bind data from selected element to view through view holder
        viewHolder.refname.setText(item.getName());
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getContext());
        String url = "http://10.2.89.153:8000/api/rpfreq?rp=";// <----enter your post url here
        url=url + item.getName().trim();
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
                            yvalues.clear();
                            for (int i = 0; i < arr.length; i++) {
                                yvalues.add(new BarEntry(Float.parseFloat(arr[i].trim()), (int) Float.parseFloat(String.valueOf(i).trim())));
                                Log.i("Tag", yvalues.get(i).toString());
                            }
                            dataSet = new BarDataSet(yvalues, "Values");
                            dataSet.setDrawValues(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getApplicationContext(), "positive" + response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "errormsg" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("rp", "1");
                return MyData;
            }
        };
        MyRequestQueue.add(getRequest);
        viewHolder.refimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSet != null) {
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
                    BarData data = new BarData(year, dataSet);
                    viewHolder.barChart.setData(data);
                    viewHolder.barChart.setDescription("Crowd Frequency");
                    dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    viewHolder.barChart.animateY(3000);
                }
            }
        });
        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView refname;
        ImageView refimage;
        BarChart barChart;
        TextView contentRequestBtn;

    }
}