package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.energyfuture.symphony.m3.adapter.ReportViewAdapter;
import com.energyfuture.symphony.m3.dao.TrReportDao;
import com.energyfuture.symphony.m3.domain.TrReport;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.ui.RoundImageView;
import com.energyfuture.symphony.m3.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReportViewActivity extends ActionBarActivity implements View.OnClickListener{
    private Context context = ReportViewActivity.this;
    private ImageView roundImageView; //标题栏图片
    private EditText keyword;
    private File reportFile;
    private TextView toolbarName,reportPrompt;
    private LinearLayout toolbar_back;
    private RelativeLayout toolbarSearch;
    private RecyclerView recyclerView;
    private ReportViewAdapter mAdapter;
    private String userId;
    private PopupWindow window;
    private RelativeLayout relativeLayout,toolbarHead;
    private LinearLayout layout;
    private String reportSavePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_view);
        //禁止弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        userId = Constants.getLoginPerson(this).get("userId");
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        //创建文件夹
        reportSavePath = Constants.FILE_SAVEPATH + "report";
        reportFile = new File(reportSavePath);
        if(!reportFile.exists()){
            reportFile.mkdir();
        }
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        toolbarHead= (RelativeLayout) findViewById(R.id.toolbar_head);
        layout= (LinearLayout) findViewById(R.id.report_background);
        roundImageView= (RoundImageView) findViewById(R.id.toolbar_head_image);
        toolbar_back = (LinearLayout)findViewById(R.id.toolbar_back);
        toolbarSearch = (RelativeLayout)findViewById(R.id.toolbar_search);
        recyclerView= (RecyclerView) findViewById(R.id.report_recycle);
        toolbarName = (TextView)findViewById(R.id.toolbar_name);
        keyword = (EditText)findViewById(R.id.report_keyword);
        reportPrompt = (TextView)findViewById(R.id.report_prompt);
        toolbarName.setText("巡检报告");
        toolbarSearch.setVisibility(View.VISIBLE);
        //设置标题栏图片
        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            roundImageView.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(userId);
            roundImageView.setImageResource(userhead);
        }

        toolbarHead.setOnClickListener(this);
        toolbar_back.setOnClickListener(this);
        toolbarSearch.setOnClickListener(this);
        layout.setOnClickListener(this);

        setmAdapterReport();

    }

    /**
     * 绑定适配器
     */
    private void setmAdapterReport(){
        TrReportDao trReportDao = new TrReportDao(context);
        List<TrReport> list = trReportDao.queryTrReportList();
//        List<String> list = getReport();
        if(list.size() > 0){
            reportPrompt.setVisibility(View.GONE);
        }else{
            reportPrompt.setVisibility(View.VISIBLE);
        }
        mAdapter=new ReportViewAdapter(list,R.layout.report_view_item,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 获取报告
     */
//    private List<String> getReport(){
//        List<String> reportList = new ArrayList<>();
//        File[] listFiles = reportFile.listFiles();
//        for(int i = 0;i < listFiles.length;i++){
//            String name = listFiles[i].getName().substring(0);
//            reportList.add(name);
//        }
//        return  reportList;
//    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //用户头像
            case R.id.toolbar_head:
                if(window==null){
                    window=Constants.createPopupWindow(userId,ReportViewActivity.this,layout);
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
            //点击空白关闭popuwindow
            case R.id.report_background:
                if(layout.getVisibility()==View.VISIBLE){
                    layout.setVisibility(View.GONE);
                    window.dismiss();
                    window=null;
                }
                break;
            //搜索报告
            case R.id.toolbar_search:
                Intent intent = new Intent(this,ReportDownLoadActivity.class);
                startActivity(intent);
                break;
        }
    }

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

    /**
     * 删除报告
     * @param trReport
     */
    public void deleteReport(final TrReport trReport, final int position){
        final MaterialDialog materialDialog = new MaterialDialog(context);
        materialDialog.setTitle("确定删除吗?").setMessage("")
                .setPositiveButton("删除",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TrReportDao trReportDao = new TrReportDao(context);
                        List<TrReport> trReportList = new ArrayList<TrReport>();
                        trReportList.add(trReport);
                        trReportDao.deleteListData(trReportList);
                        //删除文档
                        String fileUrl = Constants.FILE_SAVEPATH + "report/" + trReport.getReportName().replace(".docx",trReport.getAuditingTime() + ".docx");
                        File file = new File(fileUrl);
                        if(file.exists()){
                            file.delete();
                        }
                        materialDialog.dismiss();
                        mAdapter.reportList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                    }
                }).show();
    }
}
