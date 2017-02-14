package com.energyfuture.symphony.m3.adapter;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.activity.ReportDownLoadActivity;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrReportDao;
import com.energyfuture.symphony.m3.domain.TrReport;
import com.energyfuture.symphony.m3.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 */
public class ReportDownLoadAdapter extends RecyclerView.Adapter<ReportDownLoadAdapter.Viewholder>{
    public List<TrReport> reportList;
    private int rowlayout;
    private ReportDownLoadActivity context;

    public ReportDownLoadAdapter(List<TrReport> reportList, int rowlayout, ReportDownLoadActivity context) {
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
    public void onBindViewHolder(final Viewholder holder, int position) {
        final TrReport trReport = reportList.get(position);
        holder.reportName.setText(trReport.getReportName());
        holder.personName.setText(trReport.getCreatePerson());
        holder.reportDate.setText(trReport.getAuditingTime());
        holder.downBtn.setVisibility(View.VISIBLE);
        String fileUrl = Constants.FILE_SAVEPATH + "report/" + trReport.getReportName().replace(".docx",trReport.getAuditingTime() + ".docx");
        File file = new File(fileUrl);
        if(!file.exists()){
            holder.downBtn.setText("下载");
        }else if(file.exists()){
            holder.downBtn.setText("已下载");
            holder.downBtn.setEnabled(false);
        }
        //下载按钮点击事件
        holder.downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下载报告
                if(!trReport.getWordUrl().equals("")){
                    new AsyncTaskReport(holder.downProgress,holder.downBtn,trReport).execute();
                }else{
                    Toast.makeText(context,"文件路径不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //打开word
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.openWord(trReport);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList == null ? 0 : reportList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        public TextView reportName,personName,reportDate;
        private Button downBtn;
        private ProgressBar downProgress;
        public Viewholder(View itemView) {
            super(itemView);
            reportName= (TextView) itemView.findViewById(R.id.report_name);
            reportDate = (TextView) itemView.findViewById(R.id.report_date);
            personName= (TextView) itemView.findViewById(R.id.report_person_name);
            downBtn = (Button) itemView.findViewById(R.id.report_down);
            downProgress = (ProgressBar) itemView.findViewById(R.id.report_progress);
        }
    }
    class AsyncTaskReport extends AsyncTask<String, Integer, String>{
        private ProgressBar mBar;
        private TrReport mTrReport;
        private Button mBtn;
        private String reportSavePath = "",urlString = "",url1 = "",reportName = "";
        private TrReportDao trReportDao = new TrReportDao(context);

        public AsyncTaskReport(ProgressBar bar,Button btn,TrReport trReport){
            mBar = bar;
            mBtn = btn;
            mTrReport = trReport;
            reportSavePath = Constants.FILE_SAVEPATH + "report/";
            String wordUrl = mTrReport.getWordUrl();
            url1 = wordUrl.substring(0, wordUrl.lastIndexOf("/"));
            reportName = wordUrl.substring(wordUrl.lastIndexOf("/") + 1);
        }
        @Override
        protected void onPreExecute() {
            mBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            int FileLength;
            int DownedFileLength=0;
            int downLoadFilePosition = 0;
            InputStream inputStream = null;
            URLConnection connection = null;
            OutputStream outputStream = null;

            try {
                urlString = URLs.HTTP + URLs.HOST + "/" + "INSP/" + url1 + URLEncoder.encode(reportName, "UTF-8");
                URL url=new URL(urlString);
                connection = url.openConnection();
                inputStream=connection.getInputStream();
                if(inputStream !=null) {
                    String fileName = mTrReport.getStationName() + "最终报告" + mTrReport.getAuditingTime() + ".docx";
                    mTrReport.setReportName(mTrReport.getStationName() + "最终报告" + ".docx");
                    outputStream = new FileOutputStream(reportSavePath + fileName);
                    byte[] buffer = new byte[1024 * 4];
                    FileLength = connection.getContentLength();
                    while ((DownedFileLength = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, DownedFileLength);
                        downLoadFilePosition += DownedFileLength;
                        if (FileLength > 0) {
                            publishProgress((int) ((downLoadFilePosition / (float) FileLength) * 100));
                        }
                    }
                    try {
                        inputStream.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Constants.recordExceptionInfo(ex, context, context.toString()+"/ReportDownLoadAdapter");
                    }
                }else{
                    Toast.makeText(context,"文件不存在",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, context, context.toString()+"/ReportDownLoadAdapter");
                return "false";
            }
            return "true";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("true")){
                mBar.setVisibility(View.GONE);
                mBtn.setVisibility(View.GONE);

                List<TrReport> list = new ArrayList<TrReport>();
                list.add(mTrReport);
                trReportDao.insertListData(list);
            }else{
                Toast.makeText(context,"文件不存在",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
