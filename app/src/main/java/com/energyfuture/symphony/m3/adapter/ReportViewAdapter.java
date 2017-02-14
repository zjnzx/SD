package com.energyfuture.symphony.m3.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.activity.ReportViewActivity;
import com.energyfuture.symphony.m3.domain.TrReport;

import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 */
public class ReportViewAdapter extends RecyclerView.Adapter<ReportViewAdapter.Viewholder>{
    public List<TrReport> reportList;
    private int rowlayout;
    private ReportViewActivity context;

    public ReportViewAdapter(List<TrReport> reportList, int rowlayout, ReportViewActivity context) {
        this.reportList = reportList;
        this.rowlayout = rowlayout;
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowlayout, parent, false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {
        final TrReport trReport = reportList.get(position);
        holder.reportName.setText(trReport.getReportName());
        holder.personName.setText(trReport.getCreatePerson());
        holder.reportDate.setText(trReport.getAuditingTime());
        holder.reportDown.setVisibility(View.GONE);
        holder.downProgress.setVisibility(View.GONE);
        //打开word文档
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.openWord(trReport);
            }
        });
        //长按删除
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.deleteReport(trReport,position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList == null ? 0 : reportList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        public TextView reportName,personName,reportDate;
        private Button reportDown;
        private ProgressBar downProgress;
        public Viewholder(View itemView) {
            super(itemView);
            reportName = (TextView) itemView.findViewById(R.id.report_name);
            reportDate = (TextView) itemView.findViewById(R.id.report_date);
            personName = (TextView) itemView.findViewById(R.id.report_person_name);
            reportDown = (Button) itemView.findViewById(R.id.report_down);
            downProgress = (ProgressBar) itemView.findViewById(R.id.report_progress);
        }
    }
}
