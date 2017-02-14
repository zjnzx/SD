package com.energyfuture.symphony.m3.adapter;


import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.ProjectListActivity;
import com.energyfuture.symphony.m3.activity.R;

import java.util.List;
import java.util.Map;

public class SelectConditionAdapter extends RecyclerView.Adapter<SelectConditionAdapter.ViewHolder> {
    private List<Map<String,String>> applications;
    private int rowLayout;
    private ProjectListActivity mAct;
    private DrawerLayout drawerLayout;
    private LinearLayout child_menu;
    private TextView textview;
    private TextView  textviewId;

    public SelectConditionAdapter(List<Map<String,String>> applications, int rowLayout, ProjectListActivity act,DrawerLayout drawerLayout,LinearLayout child_menu,TextView textview,TextView textviewId) {
        this.applications = applications;
        this.rowLayout = rowLayout;
        this.mAct = act;
        this.drawerLayout=drawerLayout;
        this.child_menu=child_menu;
        this.textview=textview;
        this.textviewId=textviewId;
    }

    public void clearApplications() {
        int size = this.applications.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                applications.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addApplications(List<Map<String,String>> applications) {
        this.applications.addAll(applications);
        this.notifyItemRangeInserted(0, applications.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Map<String,String> map = applications.get(i);
        viewHolder.name.setText(map.get("select_name").toString());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.get("select_id");
                String select_id = map.get("select_id");
                String select_name = map.get("select_name");
                textview.setText(select_name);
                textviewId.setText(select_id);
                drawerLayout.closeDrawer(child_menu);
            }
        });

    }

    @Override
    public int getItemCount() {
        return applications == null ? 0 : applications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.select_name);
        }
    }
}