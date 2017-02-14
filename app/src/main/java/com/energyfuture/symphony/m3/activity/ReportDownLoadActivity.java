package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.ReportDownLoadAdapter;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.domain.TrReport;
import com.energyfuture.symphony.m3.ui.RoundImageView;
import com.energyfuture.symphony.m3.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDownLoadActivity extends ActionBarActivity implements View.OnClickListener{
    private Context context = ReportDownLoadActivity.this;
    private ImageView roundImageView,reportSearch; //标题栏图片
    private EditText keyword;
    private File reportFile;
    //任务信息
    private TextView toolbar_name,reportPrompt;
    private LinearLayout toolbar_back;
    private RecyclerView recyclerView;
    private ReportDownLoadAdapter mAdapter;
    private String userId;
    private PopupWindow window;
    private RelativeLayout relativeLayout,toolbar_head;
    private LinearLayout layout;
    private boolean flag = true;//标志搜索按钮是否可点击
    private String reportSavePath = "";
    private List<TrReport> reportList = new ArrayList<TrReport>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_download);
        //禁止弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        userId = Constants.getLoginPerson(this).get("userId");

        initView();

    }
    private void initView() {
        //创建文件夹
        reportSavePath = Constants.FILE_SAVEPATH + "report/";
        reportFile = new File(reportSavePath);
        if(!reportFile.exists()){
            reportFile.mkdir();
        }
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        toolbar_head= (RelativeLayout) findViewById(R.id.toolbar_head);
        layout= (LinearLayout) findViewById(R.id.report_background_download);
        roundImageView= (RoundImageView) findViewById(R.id.toolbar_head_image);
        toolbar_back = (LinearLayout)findViewById(R.id.toolbar_back);
        recyclerView= (RecyclerView) findViewById(R.id.report_recycle_download);
        toolbar_name = (TextView)findViewById(R.id.toolbar_name);
        reportSearch = (ImageView)findViewById(R.id.report_search2);
        keyword = (EditText)findViewById(R.id.report_keyword);
        reportPrompt = (TextView)findViewById(R.id.report_prompt_down);
        toolbar_name.setText("巡检报告");
        //设置标题栏图片
        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            roundImageView.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(userId);
            roundImageView.setImageResource(userhead);
        }

        toolbar_head.setOnClickListener(this);
        toolbar_back.setOnClickListener(this);
        layout.setOnClickListener(this);
        reportSearch.setOnClickListener(this);

        setmAdapterReport();

    }

    /**
     * 绑定适配器
     */
    private void setmAdapterReport(){
        mAdapter=new ReportDownLoadAdapter(reportList,R.layout.report_view_item,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //用户头像
            case R.id.toolbar_head:
                if(window==null){
                    window=Constants.createPopupWindow(userId,ReportDownLoadActivity.this,layout);
                }
                if(!window.isShowing()){
                   window.showAtLocation(relativeLayout,Gravity.TOP|Gravity.RIGHT,20,140);
                      if(layout.getVisibility()==View.GONE){
                        layout.setVisibility(View.VISIBLE);
                      }
                   }
                break;
            //返回
            case R.id.toolbar_back:
                finish();
                break;
            //点击空白出popuwindow消息
            case R.id.report_background_download:
                if(layout.getVisibility()==View.VISIBLE){
                    layout.setVisibility(View.GONE);
                    window.dismiss();
                    window=null;
                }
                break;
            //搜索报告
            case R.id.report_search2:
                if(flag){
                    flag = false;
                    getReportList();
                }
                break;
        }
    }

    /**
     * 获取报告信息
     */
    private void getReportList(){
        reportList.clear();
        String keywordText = keyword.getText().toString().trim();
        if(!("").equals(keywordText)) {
            final Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("reportname", keywordText);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //提交消息到云平台
                    String http = URLs.HTTP + URLs.HOST + "/" + "inspectfuture/" + URLs.REPORTDOWNLOAD;
                    Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");
                    Log.i("==================报告resultMap=","--" + resultMap);
                    if (resultMap != null) {
                        try {
                            String dataStr = (String) resultMap.get("data");
                            JSONArray dataArray = new JSONArray(dataStr);
                            for(int i=0;i<dataArray.length();i++){
                                JSONObject dataObj = dataArray.getJSONObject(i);
                                TrReport trReport = new TrReport();
                                trReport.setAuditingPerson(dataObj.getString("AUDITINGPERSON"));
                                trReport.setCreatePerson(dataObj.getString("CREATEPERSON"));
                                trReport.setStationId(dataObj.getString("STATIONID"));
                                trReport.setStatus(dataObj.getString("STATUS"));
                                trReport.setId(dataObj.getString("ID"));
                                trReport.setWordUrl(dataObj.getString("WORDURL"));
                                trReport.setStationName(dataObj.getString("STATIONNAME"));
                                trReport.setReportName(dataObj.getString("STATIONNAME") + "最终报告" + ".docx");
                                String audDate = dataObj.getString("AUDITINGTIME");
                                if(audDate != null && !audDate.equals("")){
                                    Long time=new Long(audDate);
                                    audDate = Constants.dateformat4.format(time);
                                    trReport.setAuditingTime(audDate);
                                }
                                reportList.add(trReport);
                            }
                            handler.sendEmptyMessage(1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Constants.recordExceptionInfo(e, context, context.toString());
                        }
                    }else{
                        handler.sendEmptyMessage(0);
                    }
                }
            };
            new Thread(runnable).start();
        }else{
            Toast.makeText(context,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //获取到报告,更新页面
            if(msg.what == 1){
                reportPrompt.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                flag = true;
            }else if(msg.what == 0){
                reportPrompt.setVisibility(View.VISIBLE);
                Toast.makeText(context,"暂无未下载报告",Toast.LENGTH_SHORT).show();
                flag = true;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 打开word文档
     * @param trReport
     */
    public void openWord(TrReport trReport){
        String fileUrl = Constants.FILE_SAVEPATH + "report/" + trReport.getReportName().replace(".docx",trReport.getAuditingTime() + ".docx");
        File file = new File(fileUrl);
        if(file.exists()){
            Constants.openFile(context,file);
        }else{
            Toast.makeText(context,"文件不存在",Toast.LENGTH_SHORT).show();
        }
    }

}
