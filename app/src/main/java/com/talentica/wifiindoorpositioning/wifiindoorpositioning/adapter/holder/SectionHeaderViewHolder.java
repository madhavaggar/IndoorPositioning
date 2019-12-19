package com.talentica.wifiindoorpositioning.wifiindoorpositioning.adapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.talentica.wifiindoorpositioning.wifiindoorpositioning.R;

/**
 * Created by suyashg on 26/08/17.
 */

public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
    final TextView tvTitle;


    public SectionHeaderViewHolder(View headerView) {
        super(headerView);
        tvTitle = (TextView) headerView.findViewById(R.id.tv_section_name);
    }
}
