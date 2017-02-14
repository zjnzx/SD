package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.RiskTitleAdpater;
import com.energyfuture.symphony.m3.adapter.SafeConfirmAdapter;
import com.energyfuture.symphony.m3.adapter.SafeTestAdpater;
import com.energyfuture.symphony.m3.adapter.SafetyConfirmPersonAdapter;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrProjectDao;
import com.energyfuture.symphony.m3.dao.TrSafetytipsObjDao;
import com.energyfuture.symphony.m3.dao.TrTaskDao;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrSafetytipsObj;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiveTaskActivity extends ActionBarActivity {

    private Context context = ReceiveTaskActivity.this;
    private String useid;
    private TrTask trTask;
    private ViewPager viewPager;
    private ArrayList<View> pageViews;
    private LayoutInflater inflater;
    private ImageView imageView;
    private ListView guide_risk_listview;
    private TrSafetytipsObjDao trSafetytipsObjDao;
    private RiskTitleAdpater riskTitleAdpater;
    private LinearLayout toolbar_back;
    private ImageView[] imageViews;
    private ImageView toolbar_head_image,task_type;
    // 包裹小圆点的LinearLayout
    private ViewGroup group;
    private TextView index_button;
    private StringBuffer sbf;
    private List<TrSafetytipsObj> trSafetytipsObjs = new ArrayList<TrSafetytipsObj>();
    private TrTaskDao trTaskDao;
    private SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);;
    //信息栏
    private TextView task_name,substation_name,task_group,task_state,toolbar_news_show,toolbar_name;
    private FrameLayout frameLayout;

    private List<TrProject> list;  //根据项目ID查出来的项目列表
    private String date = Constants.dateformat2.format(new Date());

    private int flag; //从不同activity跳转时的标记
    private PopupWindow window;
    private RelativeLayout relativeLayout,toolbar_head;
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_task);
        initTaskInfo();
        initGuide();
    }

    /**
     * 初始化任务信息
     */
    private void initTaskInfo() {
        trTask= (TrTask)getIntent().getSerializableExtra("trTask");
        flag=getIntent().getIntExtra("flag",-1);

        TrProject trProject=new TrProject();
        trProject.setProjectid(trTask.getProjectid());
        TrProjectDao trProjectDao=new TrProjectDao(context);
        list = trProjectDao.queryTrProjectsList(trProject);

        CardView include= (CardView) findViewById(R.id.receive_row_name);

        task_type= (ImageView) include.findViewById(R.id.task_receive_type);
        task_name= (TextView) include.findViewById(R.id.task_receive_name);
        substation_name= (TextView) include.findViewById(R.id.substation_name);
        task_group= (TextView) include.findViewById(R.id.tack_group);
        task_state= (TextView) include.findViewById(R.id.task_receive_state);
        toolbar_back = (LinearLayout)findViewById(R.id.toolbar_back);
        toolbar_head_image = (ImageView)findViewById(R.id.toolbar_head_image);
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        toolbar_head= (RelativeLayout) findViewById(R.id.toolbar_head);
        layout= (LinearLayout) findViewById(R.id.background);
        /*toolbar_news_show = (TextView)findViewById(R.id.toolbar_news_show);*/
        index_button = (TextView)findViewById(R.id.index_button);
        index_button.setVisibility(View.GONE);
        toolbar_name = (TextView)findViewById(R.id.toolbar_name);
        toolbar_name.setText("安全须知");

        frameLayout= (FrameLayout) include.findViewById(R.id.receive_progressBar_FrameLayout);

        task_name.setText(trTask.getTaskname());
        substation_name.setText(Constants.getStationName(context, list));
        task_group.setText(Constants.getGroupName(context,list));
        Constants.setState(trTask.getTaskstate(),frameLayout,task_type,task_state);
        menuClick();
        useid = Constants.getLoginPerson(this).get("userId");
        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            toolbar_head_image.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(useid);
            toolbar_head_image.setImageResource(userhead);
        }
        /*toolbar_news_show.bringToFront();*/

        toolbar_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(window==null){
                    window=Constants.createPopupWindow(useid,ReceiveTaskActivity.this,layout);
                }
                if(!window.isShowing()){
                    window.showAtLocation(relativeLayout,Gravity.TOP|Gravity.RIGHT,20,140);
                    if(layout.getVisibility()==View.GONE){
                       layout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getVisibility()==View.VISIBLE){
                    layout.setVisibility(View.GONE);
                    window.dismiss();
                    window=null;
                }
            }
        });
    }

    private void menuClick() {
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    /**
     * 初始化引导页
     */
    private void initGuide(){
        trSafetytipsObjDao = new TrSafetytipsObjDao(ReceiveTaskActivity.this);
        inflater = getLayoutInflater();
        pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.task_guide_item_risk, null));
        pageViews.add(inflater.inflate(R.layout.task_guide_item_safetytest, null));
        pageViews.add(inflater.inflate(R.layout.task_guide_item_safetyconfirm, null));

        imageViews = new ImageView[pageViews.size()];
        group = (ViewGroup)findViewById(R.id.viewGroup);
        viewPager = (ViewPager)findViewById(R.id.guidePages);

        for (int i = 0; i < pageViews.size(); i++) {
            imageView = new ImageView(context);
            imageViews[i] = imageView;

            if (i == 0) {
                //默认选中第一张图片
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams( 40,40));
            params.leftMargin = 20;//设置点点点view的左边距
            params.rightMargin = 20;//设置点点点view的右边距
            group.addView(imageViews[i],params);
        }
        viewPager.setAdapter(new GuidePageAdapter());
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }

    /**
     * 查询安全须知实体表
     * @param param
     * @return
     */
    private List<TrSafetytipsObj> queryTrSafetytipsObj(String param,String taskid){
        TrSafetytipsObj trSafetytipsObj = new TrSafetytipsObj();
        trSafetytipsObj.setSafetype(param);
        trSafetytipsObj.setTaskid(taskid);
        List<TrSafetytipsObj> list = trSafetytipsObjs = trSafetytipsObjDao.queryTrSafetytipsObjList(trSafetytipsObj);
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 修改任务表的小组人员和项目状态并封装消息
     */
    private void updateTrTask(){
        Map<Object,Object> columnsMap = new HashMap<Object,Object>();
        Map<Object,Object> wheresMap = new HashMap<Object,Object>();
        sbf = new StringBuffer();
        for(Map.Entry<String, Boolean> entry: Utils.personMap.entrySet()){
             if(entry.getValue()){
                 sbf.append(entry.getKey()).append(";");
             }
        }
        if(!sbf.toString().equals("")){
            columnsMap.put("WORKPERSON",sbf.toString());
        }
        columnsMap.put("TASKSTATE","2");
        columnsMap.put("UPDATETIME",date);
        wheresMap.put("TASKID",trTask.getTaskid());
        trTaskDao.updateTrTaskInfo(columnsMap, wheresMap);
        TrTask trTask1 = new TrTask();
        trTask1.setWorkperson(sbf.toString());
        trTask1.setTaskstate("2");
        trTask1.setUpdatetime(date);
        trTask1.setTaskid(trTask.getTaskid());
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trTask1);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
        if(list.get(0).getProjectstate() != null && list.get(0).getProjectstate().equals("1")){
            Map<Object,Object> proColumnsMap = new HashMap<Object,Object>();
            proColumnsMap.put("PROJECTSTATE","3");
            proColumnsMap.put("UPDATETIME",date);
            Map<Object,Object> proWheresMap = new HashMap<Object,Object>();
            proWheresMap.put("PROJECTID",list.get(0).getProjectid());
            TrProjectDao trProjectDao = new TrProjectDao(context);
            trProjectDao.updateProjectInfo(proColumnsMap,proWheresMap);
            TrProject trProject = new TrProject();
            trProject.setProjectstate("3");
            trProject.setUpdatetime(date);
            trProject.setProjectid(list.get(0).getProjectid());
            List<List<Object>> list3 = new ArrayList<List<Object>>();
            List<Object> list4 = new ArrayList<Object>();
            list4.add(trProject);
            list3.add(list4);
            sympMessageRealDao.updateTextMessages(list3);
        }
    }


    /**
     * 修改安全须知实体表中的确认人并封装消息
     */
    private void updateTrSafetytipsObj(){
        String userId = Constants.getLoginPerson(context).get("userId");
        TrSafetytipsObjDao trSafetytipsObjDao = new TrSafetytipsObjDao(context);
        Map<Object,Object> columnsMap = new HashMap<Object,Object>();
        Map<Object,Object> wheresMap = new HashMap<Object,Object>();
        columnsMap.put("PERSION",userId);
        columnsMap.put("UPDATETIME",date);
        wheresMap.put("TASKID",trTask.getTaskid());
        wheresMap.put("SAFETYPE","confirm");
        trSafetytipsObjDao.updateTrSafetytipsObjInfo(columnsMap, wheresMap);

        TrSafetytipsObj trSafetytipsObj = new TrSafetytipsObj();
        trSafetytipsObj.setPersion(userId);
        trSafetytipsObj.setId(getId());
        trSafetytipsObj.setUpdatetime(date);
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trSafetytipsObj);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);

    }

    private String getId() {   //获取id
        TrSafetytipsObjDao trSafetytipsObjDao = new TrSafetytipsObjDao(context);
        TrSafetytipsObj trSafetytipsObjParam=new TrSafetytipsObj();
        trSafetytipsObjParam.setSafetype("confirm");
        trSafetytipsObjParam.setTaskid(trTask.getTaskid());
        List<TrSafetytipsObj> datalist = trSafetytipsObjDao.queryTrSafetytipsObjList(trSafetytipsObjParam);
        if(datalist.size()!=0){
            return datalist.get(0).getId();
        }
        return "";
    }

    // 指引页面数据适配器
    class GuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            // TODO Auto-generated method stub
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            // TODO Auto-generated method stub
            ((ViewPager) arg0).addView(pageViews.get(arg1));
            if(arg1 == 0){//风险评估
                trSafetytipsObjs = queryTrSafetytipsObj("risk",trTask.getTaskid());
                guide_risk_listview = (ListView) pageViews.get(arg1).findViewById(R.id.risk_listview);
                riskTitleAdpater = new RiskTitleAdpater(context,trSafetytipsObjs);
                riskTitleAdpater.notifyDataSetChanged();
                guide_risk_listview.setAdapter(riskTitleAdpater);
            }else if(arg1 == 1){//安全交底
                trSafetytipsObjs = queryTrSafetytipsObj("safesay",trTask.getTaskid());
                List<String> list = new ArrayList<String>();
                if(trSafetytipsObjs != null && trSafetytipsObjs.size() > 0 ){
                    String endangerrang = trSafetytipsObjs.get(0).getEndangerrang();
                    String rang[] = endangerrang.split("。");
                    for(int i = 0;i < rang.length;i++){
                        list.add(rang[i]);
                    }
                }
                ListView safetytest_listview = (ListView) pageViews.get(arg1).findViewById(R.id.safetytest_listview);
                SafeTestAdpater safeTestAdpater = new SafeTestAdpater(context,list);
                safeTestAdpater.notifyDataSetChanged();
                safetytest_listview.setAdapter(safeTestAdpater);
            }else if(arg1 == 2){//安全确认信息
                trSafetytipsObjs = queryTrSafetytipsObj("confirm",trTask.getTaskid());
                ListView safetyconfirm_listview = (ListView) pageViews.get(arg1).findViewById(R.id.safetytest_listview);
                SafeConfirmAdapter safeConfirmAdapter = new SafeConfirmAdapter(context,trSafetytipsObjs);
                safeConfirmAdapter.notifyDataSetChanged();
                safetyconfirm_listview.setAdapter(safeConfirmAdapter);
                //小组人员
                trTaskDao = new TrTaskDao(context);
                TrTask task = new TrTask();
                task.setTaskid(trTask.getTaskid());
                List<TrTask> trTaskList = trTaskDao.queryTrTaskList(task);
                final String person = trTaskList.get(0).getWorkperson();
                String str[] = person.split(";");
                final List<String> list = new ArrayList<String>();
                for(int i = 0;i < str.length;i++){
                    list.add(str[i]);
                }
                RecyclerView safetytest_recyclerView = (RecyclerView) pageViews.get(arg1).findViewById(R.id.safetyconfirm_person_recyclerView);

                Button safeConfirmBtn = (Button) pageViews.get(arg1).findViewById(R.id.button);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context,4);
                safetytest_recyclerView.setLayoutManager(gridLayoutManager);
                final SafetyConfirmPersonAdapter safetyConfirmPersonAdapter = new SafetyConfirmPersonAdapter(context,list,flag);
                safetyConfirmPersonAdapter.notifyDataSetChanged();
                safetytest_recyclerView.setAdapter(safetyConfirmPersonAdapter);

                if(flag!=-1){
                    safeConfirmBtn.setVisibility(View.GONE);
                }

                //安全确认按钮事件
                safeConfirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到工具页面
                        if(Utils.personMap.size() > 0){
                            for (Map.Entry<String, Boolean> entry : Utils.personMap.entrySet()) {
                                boolean value = entry.getValue();
                                if(value){
                                    //修改任务表的小组人员
                                    updateTrTask();
                                    //修改安全须知实体表中的确认人
                                    updateTrSafetytipsObj();
                                    //修改项目状态

                                    Intent intent = new Intent(context, TaskToolActivity.class);
                                    Bundle bundle = new Bundle();
                                    trTask.setTaskstate("2");
                                    bundle.putSerializable("trTask",trTask);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                    Utils.personMap.clear();
                                    return;
                                }
                            }
                        }else{
                            Toast.makeText(context,"请选择确认人员",Toast.LENGTH_SHORT).show();
                        }
                  }
             });
           }
            return pageViews.get(arg1);
        }
    }

    // 指引页面更改事件监听器
    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0].setBackgroundResource(R.drawable.page_indicator_focused);

                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.drawable.page_indicator);
                }
            }
        }
    }
}
