package com.energyfuture.symphony.m3.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.energyfuture.symphony.m3.adapter.FlowListviewAdapter;
import com.energyfuture.symphony.m3.adapter.FlowScreenAdapter;
import com.energyfuture.symphony.m3.dao.SympFlowInfoDao;
import com.energyfuture.symphony.m3.dao.SympUserInfoDao;
import com.energyfuture.symphony.m3.domain.SympFlowInfo;
import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.setting.ListViewPlus;
import com.energyfuture.symphony.m3.util.DateTimePickDialogUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class FlowActivity extends ActionBarActivity  implements View.OnClickListener, ListViewPlus.ListViewPlusListener {
    public Context context = FlowActivity.this;
    public FlowListviewAdapter flowListviewAdapter;
    public FlowScreenAdapter flowScreenAdapter;
    public ListViewPlus xListView;
    private Handler mHandler;
    private List<SympFlowInfo> flowList, flowInfos, flowInfos1, flowList1;
    private SympFlowInfoDao sympFlowInfoDao;
    private SympUserInfoDao sympUserInfoDao;
    private ImageView mImgSubmit, mImgMenuBack, mImgChildBack, mImgScreen;
    public LinearLayout mLytMenuList, childList, mLytUserName, mLytConnextType, mLytFlowType, mLytUseType;
    private TextView mTxtUserName, userSelectValue, connectSelectValue, flowTypeSelectValue, useSelectValue;
    private ListView mLstView;
    private DrawerLayout mDrawerLayout;
    private TextView mTxtSCFlow, mTxtXZFlow, mTxtZGFlow, mTxtStateTime, mTxtEndTime;
    private LinearLayout lytStateTime, lytEndTime,theme_back;
    List<UserInfo> userInfos, userInfos1, userInfos2, userInfos3, userInfos4;
    String useSelect, flowTypeSelect, connectSelect, stateTime, endTime, uName;
    String userid;
    private SharedPreferences mySharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);

        mySharedPreferences = context.getSharedPreferences("ISOK", Activity.MODE_PRIVATE);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.flow_drawer);
        mDrawerLayout.closeDrawer(Gravity.RIGHT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mLytMenuList = (LinearLayout) findViewById(R.id.menuList);
        mDrawerLayout.closeDrawer(mLytMenuList);
        childList = (LinearLayout) findViewById(R.id.childList);
        mDrawerLayout.closeDrawer(childList);
        mImgMenuBack = (ImageView) findViewById(R.id.father_menu_back);
        mImgChildBack = (ImageView) findViewById(R.id.child_menu_back);
        useSelectValue = (TextView) findViewById(R.id.flow_use_select);
        useSelectValue.setText("全部");
        flowTypeSelectValue = (TextView) findViewById(R.id.flow_type_select_value);
        flowTypeSelectValue.setText("全部");
        connectSelectValue = (TextView) findViewById(R.id.connect_select_value);
        connectSelectValue.setText("全部");
        userSelectValue = (TextView) findViewById(R.id.user_select_value);
        userSelectValue.setText("全部");
        mTxtStateTime = (TextView) findViewById(R.id.flow_state_time_select);
        mTxtStateTime.setText("全部");
        mTxtEndTime = (TextView) findViewById(R.id.flow_end_time_select);
        mTxtEndTime.setText("全部");

        mTxtSCFlow = (TextView) findViewById(R.id.txt_scll);
        mTxtXZFlow = (TextView) findViewById(R.id.txt_xzll);
        mTxtZGFlow = (TextView) findViewById(R.id.txt_zgll);

        lytStateTime = (LinearLayout) findViewById(R.id.flow_state_time_LinearLayout);
        lytStateTime.setOnClickListener(this);
        lytEndTime = (LinearLayout) findViewById(R.id.flow_end_time_LinearLayout);
        lytEndTime.setOnClickListener(this);
        mImgChildBack.setOnClickListener(this);
        mImgMenuBack.setOnClickListener(this);
        sympFlowInfoDao = new SympFlowInfoDao(context);
        flowList = sympFlowInfoDao.queryFlowInfo(null);
        mLstView = (ListView) findViewById(R.id.select_condition);
        xListView = (ListViewPlus) findViewById(R.id.xlistview);
        xListView.setRefreshEnable(false);
        xListView.setLoadEnable(true);
        xListView.setAutoLoadEnable(true);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        if (flowList.size() == 35) {
            editor.putString("isok", "true");
            xListView.setLoadEnable(true);
        } else {
            editor.putString("isok", "false");
            xListView.setLoadEnable(false);
        }
        editor.commit();
        flowUse();
        flowListviewAdapter = new FlowListviewAdapter(context, flowList);
        xListView.setAdapter(flowListviewAdapter);
        xListView.setListViewPlusListener(this);

        mLytUserName = (LinearLayout) findViewById(R.id.flow_username_LinearLayout);
        mTxtUserName = (TextView) findViewById(R.id.condition_name);
        mLytUserName.setOnClickListener(this);
        mLytConnextType = (LinearLayout) findViewById(R.id.flow_connect_time_LinearLayout);
        mLytConnextType.setOnClickListener(this);
        mLytFlowType = (LinearLayout) findViewById(R.id.flow_type_LinearLayout);
        mLytFlowType.setOnClickListener(this);
        mLytUseType = (LinearLayout) findViewById(R.id.flow_use_type_LinearLayout);
        mLytUseType.setOnClickListener(this);
        mImgSubmit = (ImageView) findViewById(R.id.submit_select_condition);
        mImgSubmit.setOnClickListener(this);
        mImgScreen = (ImageView) findViewById(R.id.flow_image_screen);
        mImgScreen.setOnClickListener(this);
        theme_back = (LinearLayout) findViewById(R.id.theme_back);
        theme_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 查询全部流量
     */
    public void flowUse() {

        Float SCquantity = 0f;
        Float XZquantity = 0f;

        String upload = sympFlowInfoDao.querySelectFlow("上传");
        if (upload != null && !("").equals(upload)) {
            SCquantity = Float.parseFloat(upload);
        }
        String download = sympFlowInfoDao.querySelectFlow("下载");
        if (download != null && !("").equals(download)) {
            XZquantity = Float.parseFloat(download);
        }

        BigDecimal a = new BigDecimal(SCquantity / 1024);
        BigDecimal b = new BigDecimal(XZquantity / 1024);
        Float a1 = a.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
        Float b1 = b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();

        Float c1 = (SCquantity + XZquantity)/1024;
        BigDecimal c = new BigDecimal(c1);
        Float zg = c.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();

        mTxtSCFlow.setText(a1 + " M");
        mTxtXZFlow.setText(b1 + " M");
        mTxtZGFlow.setText(zg + " M");
    }

    /**
     * 查询筛选流量
     *
     * @param listinfos
     */
    public void flowUse(List<SympFlowInfo> listinfos) {

        Float SCUsequantity = 0f;
        Float XZUsequantity = 0f;

        for (int i = 0; i < listinfos.size(); i++) {


            String useType = listinfos.get(i).getUsetype().toString();
            if (useType.equals("上传")) {

                Float SCquantity = Float.parseFloat(listinfos.get(i).getUsequantity());
                SCUsequantity += SCquantity;

            } else if (useType.equals("下载")) {

                Float XZquantity = Float.parseFloat(listinfos.get(i).getUsequantity());
                XZUsequantity += XZquantity;
            }
        }

        BigDecimal a = new BigDecimal(SCUsequantity / 1024);
        BigDecimal b = new BigDecimal(XZUsequantity / 1024);
        Float a1 = a.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
        Float b1 = b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();

        Float c1 = (SCUsequantity / 1024) + (XZUsequantity / 1024);
        BigDecimal c = new BigDecimal(c1);
        Float zg = c.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();

        mTxtSCFlow.setText(a1 + " M");
        mTxtXZFlow.setText(b1 + " M");
        mTxtZGFlow.setText(zg + " M");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (uName == null && flowTypeSelect == null && connectSelect == null && useSelect == null && stateTime == null && endTime == null) {

                    String date = flowList.get(flowList.size() - 1).getRecordtime();
                    //获取更多数据
                    List<SympFlowInfo> fList = sympFlowInfoDao.queryFlowInfo(date);

                    //更新listview显示
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    if (fList.size() == 0) {
                        editor.putString("isok", "false");
                    } else {
                        flowList.addAll(fList);
                        editor.putString("isok", "true");
                    }
                    editor.commit();
                    flowListviewAdapter.onDateChange(flowList);
                } else {

                    String date = flowInfos.get(flowInfos.size() - 1).getRecordtime();
                    List<SympFlowInfo> fList;
                    if (userid != null) {
                        //获取更多数据
                        fList = sympFlowInfoDao.querySelectFlowInfo(date, userid, flowTypeSelect, connectSelect, useSelect, stateTime, endTime);
                    } else {
                        //获取更多数据
                        fList = sympFlowInfoDao.querySelectFlowInfo(date, uName, flowTypeSelect, connectSelect, useSelect, stateTime, endTime);
                    }
                    //更新listview显示
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    if (fList.size() < 35) {
                        editor.putString("isok", "false");

                    } else {
                        flowInfos.addAll(fList);
                        editor.putString("isok", "true");
                    }
                    editor.commit();
                    flowListviewAdapter.onDateChange(flowInfos);
                }
//                通知listview加载完毕
                xListView.stopLoadMore();
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.flow_state_time_LinearLayout:

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 1);
                DateTimePickDialogUtil dateTimePickDialogUtil = new DateTimePickDialogUtil(this, "");
                dateTimePickDialogUtil.dateTxtTimePicKDialog(mTxtStateTime);


                break;

            case R.id.flow_end_time_LinearLayout:

                InputMethodManager imm1 = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(v.getWindowToken(), 1);
                DateTimePickDialogUtil dateTimePickDialogUtil1 = new DateTimePickDialogUtil(this, "");
                dateTimePickDialogUtil1.dateTxtTimePicKDialog(mTxtEndTime);
                break;
            case R.id.flow_image_screen:

                SharedPreferences.Editor editor1 = mySharedPreferences.edit();
                editor1.putString("isok", "true");
                editor1.commit();
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.submit_select_condition:

                connectSelect = connectSelectValue.getText().toString();
                useSelect = useSelectValue.getText().toString();
                flowTypeSelect = flowTypeSelectValue.getText().toString();
                uName = userSelectValue.getText().toString();
                stateTime = mTxtStateTime.getText().toString();
                endTime = mTxtEndTime.getText().toString();
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                if (userid != null) {
                    flowInfos = sympFlowInfoDao.querySelectFlowInfo(null, userid, flowTypeSelect, connectSelect, useSelect, stateTime, endTime);
                    flowInfos1 = sympFlowInfoDao.querySelectFlowInfo1(null, userid, flowTypeSelect, connectSelect, useSelect, stateTime, endTime);
                } else {
                    flowInfos = sympFlowInfoDao.querySelectFlowInfo(null, uName, flowTypeSelect, connectSelect, useSelect, stateTime, endTime);
                    flowInfos1 = sympFlowInfoDao.querySelectFlowInfo1(null, uName, flowTypeSelect, connectSelect, useSelect, stateTime, endTime);
                }
                if (flowInfos.size() == 35) {
                    editor.putString("isok", "true");
                    xListView.setEnabled(true);
                } else {
                    editor.putString("isok", "false");
                    xListView.setEnabled(false);
                }
                editor.commit();
                flowUse(flowInfos1);
                flowListviewAdapter.onDateChange(flowInfos);
                mDrawerLayout.closeDrawer(Gravity.RIGHT);

                break;
            case R.id.flow_use_type_LinearLayout:

//                mDrawerLayout.closeDrawer(mLytMenuList);
                mDrawerLayout.openDrawer(childList);
                mTxtUserName.setText("使用类型");
                userInfos3 = new ArrayList<UserInfo>();
                UserInfo userInfo = new UserInfo();
                UserInfo userInfo1 = new UserInfo();
                UserInfo userInfo2 = new UserInfo();
                userInfo.setXm("全部");
                userInfos3.add(userInfo);
                userInfo1.setXm("上传");
                userInfos3.add(userInfo1);
                userInfo2.setXm("下载");
                userInfos3.add(userInfo2);
                flowScreenAdapter = new FlowScreenAdapter(context, userInfos3);
                mLstView.setAdapter(flowScreenAdapter);

                mLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String use = userInfos3.get(position).getXm().toString();
                        useSelectValue.setText(use);
                        mDrawerLayout.closeDrawer(childList);
//                        mDrawerLayout.openDrawer(mLytMenuList);
                    }
                });
                break;
            case R.id.flow_username_LinearLayout:

//                mDrawerLayout.closeDrawer(mLytMenuList);
                mDrawerLayout.openDrawer(childList);
                mTxtUserName.setText("用户名");
                sympUserInfoDao = new SympUserInfoDao(context);
                userInfos = sympUserInfoDao.queryUserInfoList(new UserInfo());
                userInfos1 = new ArrayList<UserInfo>();
                UserInfo userInfo3 = new UserInfo();
                userInfo3.setXm("全部");
                userInfo3.setYhid("全部");
                userInfos1.add(userInfo3);
                userInfos1.addAll(userInfos);
                flowScreenAdapter = new FlowScreenAdapter(context, userInfos1);
                mLstView.setAdapter(flowScreenAdapter);
                mLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String userName = userInfos1.get(position).getXm().toString();
                        userid = userInfos1.get(position).getYhid().toString();
                        userSelectValue.setText(userName);
                        mDrawerLayout.closeDrawer(childList);
//                        mDrawerLayout.openDrawer(mLytMenuList);
                    }
                });

                break;
            case R.id.father_menu_back:

                mDrawerLayout.closeDrawer(Gravity.RIGHT);

                break;
            case R.id.child_menu_back:

                mDrawerLayout.closeDrawer(childList);

                break;
            case R.id.flow_connect_time_LinearLayout:

//                mDrawerLayout.closeDrawer(mLytMenuList);
                mDrawerLayout.openDrawer(childList);
                mTxtUserName.setText("连接类型");
                userInfos4 = new ArrayList<UserInfo>();
                UserInfo userInfo4 = new UserInfo();
                userInfo4.setXm("全部");
                userInfos4.add(userInfo4);
                UserInfo userInfo5 = new UserInfo();
                UserInfo userInfo6 = new UserInfo();
                UserInfo userInfo7 = new UserInfo();
                UserInfo userInfo8 = new UserInfo();
                userInfo5.setXm("WIFI");
                userInfos4.add(userInfo5);
                userInfo6.setXm("移动");
                userInfos4.add(userInfo6);
                userInfo7.setXm("联通");
                userInfos4.add(userInfo7);
                userInfo8.setXm("电信");
                userInfos4.add(userInfo8);
                flowScreenAdapter = new FlowScreenAdapter(context, userInfos4);
                mLstView.setAdapter(flowScreenAdapter);

                mLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String connect = userInfos4.get(position).getXm().toString();
                        connectSelectValue.setText(connect);
                        mDrawerLayout.closeDrawer(childList);
//                        mDrawerLayout.openDrawer(mLytMenuList);
                    }
                });
                break;
            case R.id.flow_type_LinearLayout:

                mDrawerLayout.openDrawer(childList);
                mTxtUserName.setText("流量类型");
                userInfos2 = new ArrayList<UserInfo>();
                UserInfo userInfo9 = new UserInfo();
                UserInfo userInfo01 = new UserInfo();
                UserInfo userInfo02 = new UserInfo();
                UserInfo userInfo03 = new UserInfo();
                userInfo9.setXm("全部");
                userInfos2.add(userInfo9);
                userInfo01.setXm("文本");
                userInfos2.add(userInfo01);
                userInfo02.setXm("文件");
                userInfos2.add(userInfo02);
                flowScreenAdapter = new FlowScreenAdapter(context, userInfos2);
                mLstView.setAdapter(flowScreenAdapter);
                mLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String flowType = userInfos2.get(position).getXm().toString();
                        flowTypeSelectValue.setText(flowType);
                        mDrawerLayout.closeDrawer(childList);
                    }
                });
                break;
        }
    }
}
