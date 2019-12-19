package com.talentica.wifiindoorpositioning.wifiindoorpositioning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ramotion.foldingcell.FoldingCell;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.ReferencePoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<ReferencePoint> {

    ArrayList NoOfEmp = new ArrayList();

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

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
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
          viewHolder.refname = cell.findViewById(R.id.refname);
          viewHolder.refimage = cell.findViewById(R.id.refimage);
          viewHolder.barChart = cell.findViewById(R.id.barchart);
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

        if (null == item)
            return cell;

        // bind data from selected element to view through view holder
        viewHolder.refname.setText(item.getName());


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
        viewHolder.barChart.animateY(3000);
        BarData data = new BarData(year, bardataset);
        bardataset.setColors(ColorTemplate.JOYFUL_COLORS);
        viewHolder.barChart.setData(data);

        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
            viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
        } else {

            // (optionally) add "default" handler if no handler found in item
            //viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
        }

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

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView refname;
        ImageView refimage;
        BarChart barChart;
        TextView contentRequestBtn;

    }
}