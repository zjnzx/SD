package com.energyfuture.symphony.m3.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.energyfuture.symphony.m3.adapter.CommunionThemeListAdapter;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.dao.SympCommunicationAccessoryDao;
import com.energyfuture.symphony.m3.dao.SympCommunicationPromptDao;
import com.energyfuture.symphony.m3.dao.SympCommunicationReplyDao;
import com.energyfuture.symphony.m3.dao.SympCommunicationThemeDao;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.domain.SympCommunicationAccessory;
import com.energyfuture.symphony.m3.domain.SympCommunicationPrompt;
import com.energyfuture.symphony.m3.domain.SympCommunicationReply;
import com.energyfuture.symphony.m3.domain.SympCommunicationTheme;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunionThemeListActivity extends Activity implements View.OnClickListener {
    private Context context=CommunionThemeListActivity.this;
    private List<SympCommunicationTheme> sympCommunicationThemeList = new ArrayList<SympCommunicationTheme>();
    private RecyclerView mRecyclerView;
    private LinearLayout mBack,mCreate;
    private ProgressBar mProgressBar;
    private Spinner mSpinner;
    private TrProject trProject;
    private TrTask trTask;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CommunionThemeListAdapter mAdapter;
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String userId,belongId;
    private String launchPosition;
    private TextView no_theme;
    private String queryType = "0";//默认查询全部
    private String type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communion_theme_list);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initRecycleView();
    }

    private void initView() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        type = (String) bundle.get("type");
        if(type.equals("project")){
            trProject=(TrProject) bundle.get("belongType");
            belongId = trProject.getProjectid();
            launchPosition = trProject.getProjectname();
        }else if(type.equals("task")){
            trTask=(TrTask) bundle.get("belongType");
            String projectName = (String) bundle.get("projectName");
            belongId = trTask.getTaskid();
            launchPosition = projectName + "/" + trTask.getTaskname();
            trTask.setTaskname(launchPosition);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.theme_recycleview);
        mBack = (LinearLayout) findViewById(R.id.theme_back);
        mCreate = (LinearLayout) findViewById(R.id.theme_create);
        mSpinner = (Spinner) findViewById(R.id.theme_spinner);
        mProgressBar = (ProgressBar) findViewById(R.id.theme_bar);
        no_theme = (TextView) findViewById(R.id.no_theme);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.theme_swip);
        //创建主题
        mCreate.setOnClickListener(this);
        //返回
        mBack.setOnClickListener(this);

        setSpinner();
        setRefresh();
        //定时器操作类,每隔5秒从数据库刷一下数据


    }

    private void setSpinner(){
        List<String> list = new ArrayList<String>();
        list.add("全部主题");
        list.add("我发起的");
        list.add("我参与的");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        //条件筛选
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://全部主题
                        TextView textView1 = (TextView) view;
                        textView1.setTextSize(20);
                        queryType = "0";
                        new InitializeApplicationsTask().execute("0");
                        break;
                    case 1://我发起的
                        TextView textView2 = (TextView) view;
                        textView2.setTextSize(20);
                        queryType = "1";
                        new InitializeApplicationsTask().execute("1");
                        break;
                    case 2://我参与的
                        TextView textView3 = (TextView) view;
                        textView3.setTextSize(20);
                        queryType = "2";
                        new InitializeApplicationsTask().execute("2");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 设置适配器
     */
    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommunionThemeListAdapter(new ArrayList<SympCommunicationTheme>(), R.layout.communion_theme_list_item,context,launchPosition);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<SympCommunicationTheme> queryThemeList(String queryType){
        SympCommunicationThemeDao themeDao = new SympCommunicationThemeDao(context);
        SympCommunicationTheme sympCommunicationTheme = new SympCommunicationTheme();
        sympCommunicationTheme.setBelongId(belongId);
        String loginUserId = Constants.getLoginPerson(context).get("userId");
        if(queryType.equals("1")){//查询我发起的
            sympCommunicationTheme.setCreatepersonid(loginUserId);
        }else if(queryType.equals("2")){//查询我参与的
            List<SympCommunicationTheme> themeList = new ArrayList<SympCommunicationTheme>();
            List<SympCommunicationTheme> list = themeDao.querySympCommunicationThemeList(sympCommunicationTheme);
            for(SympCommunicationTheme theme : list){
                if(theme.getParticipator() != null && !theme.getParticipator().equals("") && theme.getParticipator().contains(loginUserId)){
                    themeList.add(theme);
                }
            }
            return themeList;
        }
        List<SympCommunicationTheme> list = themeDao.querySympCommunicationThemeList(sympCommunicationTheme);
        return list;
    }

    /**
     * 下拉刷新
     */
    private void setRefresh(){
        Map<String,String> map= Constants.getLoginPerson(context);
        userId=map.get("userId");
        final Map<String, Object> condMap = Constants.getRequestParam(context,"COMMTHEME",null,belongId);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1 && msg.obj != null){
                    new InitializeApplicationsTask().execute(queryType);
                }
            }
        };
//        new DataSyschronized(context).getDataFromWeb(condMap, handler);
          mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#0288D1"));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Utils.position=1;
                new DataSyschronized(context).getDataFromWeb(condMap, handler);
            }
        });
//        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.theme_back:
                this.onBackPressed();
                break;
            case R.id.theme_create: //新增主题
                Intent intent = new Intent(this,CommunionThemeCreateActivity.class);
//                传项目sympProject对象
                Bundle bundle=new Bundle();
                if(type.equals("project")){
                    bundle.putSerializable("belongType",trProject);
                }else if(type.equals("task")){
                    bundle.putSerializable("belongType",trTask);
                }
                bundle.putSerializable("type",type);
                intent.putExtras(bundle);
                startActivity(intent);
                Utils.picChecked.clear();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到回复页面
     * @param theme
     */
    public void toActivity(SympCommunicationTheme theme){
        Intent intent = new Intent(this,CommunionThemeReplyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("launchPosition",launchPosition);
        bundle.putSerializable("theme",theme);
        intent.putExtras(bundle);
        startActivity(intent);
        updateState(theme);
        Utils.picChecked.clear();
    }

    /**
     *
     * 删除主贴及相应回复
     */
    public void toDeleteThemeAndReply(final SympCommunicationTheme theme, final int position){
        final MaterialDialog mMaterialDialog=new MaterialDialog(context);
        mMaterialDialog.setTitle("确认要删除吗？").setMessage("")
                .setPositiveButton("删除",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SympCommunicationThemeDao themeDao = new SympCommunicationThemeDao(context);
                        SympCommunicationAccessoryDao accessoryDao=new SympCommunicationAccessoryDao(context);
                        SympCommunicationReplyDao replyDao=new SympCommunicationReplyDao(context);
                        SympCommunicationPromptDao promptDao=new SympCommunicationPromptDao(context);

                        SympCommunicationAccessory sympCommunicationAccessory=new SympCommunicationAccessory();
                        sympCommunicationAccessory.setThemeid(theme.getId());
                        //要被删除的主贴信息
                        List<Map<String, Object>> messageContentthemeDelete = new ArrayList<Map<String, Object>>();
                        Map<String, Object> maptheme = new HashMap<String, Object>();
                        maptheme.put("ID", theme.getId());

                        messageContentthemeDelete.add(maptheme);
                        //要被删除的附件信息
                        List<Map<String, Object>> messageContentaccesoryDelete = new ArrayList<Map<String, Object>>();
                        List<SympCommunicationAccessory> accessoryList=  accessoryDao.querySympCommunicationAccessoryList1(sympCommunicationAccessory);
                        for(SympCommunicationAccessory accessory: accessoryList){

                            Map<String, Object> mapaccessory = new HashMap<String, Object>();
                            mapaccessory.put("ID", accessory.getId());
                            messageContentaccesoryDelete.add(mapaccessory);
                        }

                        //要被删除的回复信息
                        List<Map<String, Object>> messageContentreplyDelete = new ArrayList<Map<String, Object>>();
                        SympCommunicationReply sympCommunicationReply=new SympCommunicationReply();
                        sympCommunicationReply.setThemeid(theme.getId());
                        List<SympCommunicationReply> replyList= replyDao.querySympCommunicationReplyList(sympCommunicationReply);
                        for(SympCommunicationReply reply:replyList){
                            Map<String, Object> mapreply = new HashMap<String, Object>();
                            mapreply.put("ID", reply.getId());
                            messageContentreplyDelete.add(mapreply);
                        }
                        //要被删除的提醒信息
                        List<Map<String, Object>> messageContentpromptDelete = new ArrayList<Map<String, Object>>();
                        SympCommunicationPrompt sympCommunicationprompt=new SympCommunicationPrompt();
                        sympCommunicationprompt.setThemeid(theme.getId());
                        List<SympCommunicationPrompt> promptList= promptDao.querySympCommunicationPromptList(sympCommunicationprompt);
                        for(SympCommunicationPrompt prompt:promptList){
                            Map<String, Object> mapprompt = new HashMap<String, Object>();
                            mapprompt.put("ID", prompt.getId());
                            messageContentpromptDelete.add(mapprompt);
                        }

                        Map<String,List<Map<String, Object>> > messageContentText = new HashMap<String,List<Map<String, Object>> >();
                        List<String> dataTypeList = new ArrayList<String>();
                        if(messageContentthemeDelete!=null&&messageContentthemeDelete.size()>0){
                            messageContentText.put("6",messageContentthemeDelete);
                            dataTypeList.add("6");
                        }
                        if(messageContentaccesoryDelete!=null&&messageContentaccesoryDelete.size()>0){
                            messageContentText.put("9",messageContentaccesoryDelete);
                            dataTypeList.add("9");
                        }
                        if(messageContentreplyDelete!=null&&messageContentreplyDelete.size()>0){
                            messageContentText.put("7",messageContentreplyDelete);
                            dataTypeList.add("7");
                        }
                        if(messageContentpromptDelete!=null&&messageContentpromptDelete.size()>0){
                            messageContentText.put("8",messageContentpromptDelete);
                            dataTypeList.add("8");
                        }
                        SympMessageRealDao messageRealDao = new SympMessageRealDao(context);
                        Map<String,String> map= Constants.getLoginPerson(context);
                        userId=map.get("userId");
                        String userName=map.get("userName");
                        try {
                            if (messageContentText != null && messageContentText.size() > 0) {
                                messageRealDao.addMessageList(Constants.getSympMessageReal(context, "1",userName+"删除了一个讨论"), dataTypeList, 3, messageContentText);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Constants.recordExceptionInfo(e,context,context.toString());
                        }
                        //删除主贴信息
                        themeDao.deleteSympCommunicationTheme(theme.getId());
                        //删除与主贴有关的所有附件信息

                        accessoryDao.deleteSympCommunicationAccessory(theme.getId(),"THEMEID");
                        //删除回复表信息
                        replyDao.deleteSympCommunicationReply(theme.getId(),"THEMEID");
                        //删除提醒表信息
                        promptDao.deleteSympCommunicationPrompt(theme.getId(),"THEMEID");
                        mAdapter.list.remove(position);
                        mAdapter.notifyItemRemoved(position); //Attention!
                        mRecyclerView.setItemViewCacheSize(mAdapter.list.size());
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         mMaterialDialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(false)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }
                        }
                )
                .show();
    }



    /**
     * 修改状态
     */
    private void updateState(SympCommunicationTheme theme){
        //修改回复表状态
        List<SympCommunicationReply> sympCommunicationReplys = new ArrayList<SympCommunicationReply>();
        SympCommunicationReplyDao sympCommunicationReplyDao = new SympCommunicationReplyDao(context);
        SympCommunicationReply sympCommunicationReply = new SympCommunicationReply();
        sympCommunicationReply.setThemeid(theme.getId());
        sympCommunicationReply.setState("0");
        sympCommunicationReplys = sympCommunicationReplyDao.querySympCommunicationReplyList(sympCommunicationReply);
        for(SympCommunicationReply sympCommunicationReply2 : sympCommunicationReplys){
            Map<Object,Object> columnsMap = new HashMap<Object,Object>();
            Map<Object,Object> wheresMap = new HashMap<Object,Object>();
            columnsMap.put("STATE","1");
            wheresMap.put("STATE","0");
            wheresMap.put("ID",sympCommunicationReply2.getId());
            sympCommunicationReplyDao.udateSympCommunicationReply(columnsMap, wheresMap);
        }
        //修改提醒表状态
        List<SympCommunicationPrompt> sympCommunicationPrompts = new ArrayList<SympCommunicationPrompt>();
        SympCommunicationPromptDao sympCommunicationPromptDao = new SympCommunicationPromptDao(context);
        SympCommunicationPrompt sympCommunicationPrompt = new SympCommunicationPrompt();
        sympCommunicationPrompt.setThemeid(theme.getId());
        sympCommunicationPrompt.setState("0");
        sympCommunicationPrompts = sympCommunicationPromptDao.querySympCommunicationPromptList(sympCommunicationPrompt);
        for(SympCommunicationPrompt sympCommunicationPrompt2 : sympCommunicationPrompts){
            Map<Object,Object> columns = new HashMap<Object,Object>();
            Map<Object,Object> wheres = new HashMap<Object,Object>();
            columns.put("STATE","1");
            wheres.put("STATE","0");
            wheres.put("ID",sympCommunicationPrompt2.getId());
            sympCommunicationPromptDao.udateSympCommunicationPrompt(columns,wheres);
        }
    }

    /**
     * 异步执行任务
     */
    private class InitializeApplicationsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            mAdapter.clearList();
            mAdapter.notifyDataSetChanged();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            sympCommunicationThemeList.clear();
            //Query the applications
            List<SympCommunicationTheme> lists = queryThemeList(params[0]);
            mRecyclerView.setItemViewCacheSize(lists.size());
            if(lists.size()>0){
                for (SympCommunicationTheme theme : lists) {
                    sympCommunicationThemeList.add(theme);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //handle visibility
            mRecyclerView.setItemViewCacheSize(sympCommunicationThemeList.size());
            mRecyclerView.destroyDrawingCache();
            mRecyclerView.getRecycledViewPool().clear();
            mRecyclerView.setVisibility(View.VISIBLE);
            if(sympCommunicationThemeList.size() == 0){
                no_theme.setVisibility(View.VISIBLE);
            }else{
                no_theme.setVisibility(View.GONE);
            }
            mProgressBar.setVisibility(View.GONE);
            //set data for list
            mAdapter.addToList(sympCommunicationThemeList);
            mSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

}
