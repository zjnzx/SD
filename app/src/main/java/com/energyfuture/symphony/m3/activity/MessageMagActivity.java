package com.energyfuture.symphony.m3.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.analysis.MessageHandlerService;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.domain.SympMessageReal;
import com.energyfuture.symphony.m3.setting.ListViewPlus;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Network;
import com.energyfuture.symphony.m3.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MessageMagActivity extends ActionBarActivity implements ListViewPlus.ListViewPlusListener,View.OnClickListener {
    private Context context = MessageMagActivity.this;
    private String messageType;
    private ProgressDialog progressDialog;
    int messagecount;//消息数量
    private ImageView mImgScreen,messageimage;
    Handler handler = null;
    private int failcount;// 发送失败数量.
    ListViewPlus listview;
    Handler mHandler;
    private RadioButton radio_automatic,radio_handmovement,radio_wifi;
    public LinearLayout mLytMenuList, childList,mLytMessageType,mLytMessageTypeText,mLytMessageTypePic,mLytMessageTypeAll,theme_back;
    private DrawerLayout mDrawerLayout;
    private ImageButton mImgBtnSubmit,mImgBtnFatherBack,mImgBtnChildBack;
    private TextView mTxtMessageSelectValue,mTxtTextNum,mTxtMeaaageNum;
    List<Map<String, String>> messageList;
    List<Map<String, String>> dataList=new ArrayList<>();
    private SharedPreferences mySharedPreferences;
    MessageListviewAdapter messageListviewAdapter;
    SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_mag);
        mySharedPreferences = context.getSharedPreferences("ISOK", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = mySharedPreferences.edit();
        editor1.putString("isok", "true");
        editor1.commit();
        String flag = Constants.SENDMESSAGEFLAG;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.message_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mLytMenuList = (LinearLayout) findViewById(R.id.menuList);
        mDrawerLayout.closeDrawer(mLytMenuList);
        childList = (LinearLayout) findViewById(R.id.childList);
        mDrawerLayout.closeDrawer(childList);
        mLytMessageType = (LinearLayout) findViewById(R.id.message_type_LinearLayout);
        mLytMessageType.setOnClickListener(this);
        mImgScreen = (ImageView) findViewById(R.id.message_image_screen);
        mImgScreen.setOnClickListener(this);
        mImgBtnSubmit = (ImageButton) findViewById(R.id.submit_select_condition);
        mImgBtnSubmit.setOnClickListener(this);
        mImgBtnFatherBack = (ImageButton) findViewById(R.id.father_menu_back);
        mImgBtnFatherBack.setOnClickListener(this);
        mImgBtnChildBack = (ImageButton) findViewById(R.id.child_menu_back);
        mImgBtnChildBack.setOnClickListener(this);
        mLytMessageTypeAll = (LinearLayout) findViewById(R.id.lyt_message_type_all);
        mLytMessageTypeAll.setOnClickListener(this);
        mLytMessageTypeText = (LinearLayout) findViewById(R.id.lyt_message_type_text);
        mLytMessageTypeText.setOnClickListener(this);
        mLytMessageTypePic = (LinearLayout) findViewById(R.id.lyt_message_type_pic);
        mLytMessageTypePic.setOnClickListener(this);
        mTxtMessageSelectValue = (TextView) findViewById(R.id.message_select_value);
        mTxtMessageSelectValue.setText("全部");
        radio_automatic = (RadioButton) findViewById(R.id.radio_automatic);
        radio_handmovement = (RadioButton) findViewById(R.id.radio_handmovement);
        radio_wifi = (RadioButton) findViewById(R.id.radio_wifi);
        radio_automatic.setOnClickListener(this);
        radio_handmovement.setOnClickListener(this);
        radio_wifi.setOnClickListener(this);
        if(flag.equals("0")){
            radio_automatic.setChecked(true);
        }else if(flag.equals("1")){
            radio_wifi.setChecked(true);
        }else if(flag.equals("2")){
            radio_handmovement.setChecked(true);
        }
        mTxtTextNum = (TextView) findViewById(R.id.txt_text_num);
        mTxtMeaaageNum = (TextView) findViewById(R.id.txt_message_num);
        theme_back = (LinearLayout) findViewById(R.id.theme_back);
        theme_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview = (ListViewPlus) findViewById(R.id.message_listview);
        listview.setRefreshEnable(false);
        listview.setLoadEnable(true);
        listview.setAutoLoadEnable(true);
        messageimage = (ImageView) findViewById(R.id.message_image_cloud);
        //        数据展示
        dataList.addAll(listdata(null));
//        dataList.addAll(lists);
        messageListviewAdapter = new MessageListviewAdapter(context,dataList);
        listview.setAdapter(messageListviewAdapter);
        listview.setListViewPlusListener(this);
        String textCount = sympMessageRealDao.queryMessageCount("1");
        String picCount = sympMessageRealDao.queryMessageCount("2");
        mTxtTextNum.setText(textCount+"条");
        mTxtMeaaageNum.setText(picCount+"条");
        if(dataList.size()>=20){
            listview.setLoadEnable(true);
        }else{
            listview.setLoadEnable(false);
        }
        //listview点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MaterialDialog dialog = new MaterialDialog(context);
                Map<String, String> map = (Map<String, String>) messageListviewAdapter.listmessage.get(position-1);
                String content = "";
                if(map != null){
                    content = map.get("CONTENT");
                }
                dialog.setMessage(content).setCanceledOnTouchOutside(true).show();
            }
        });
        mHandler = new Handler();
        //上传操作
        messageimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failcount=0;
                //网络判断是否为4G或WiFi
                SympMessageReal sympMessageReal = new SympMessageReal();
                sympMessageReal.setMessagetype("1");
                List<Map<String,String>> textdataList = sympMessageRealDao.querySympMessageReal(sympMessageReal);
                sympMessageReal.setMessagetype("2");
                List<Map<String,String>> imagedataList = sympMessageRealDao.querySympMessageReal(sympMessageReal);
                messagecount=textdataList.size()+imagedataList.size();
                dataList.clear();
                dataList.addAll(textdataList);
                dataList.addAll(imagedataList);

                if (Network.isNetworkConnected(context) == true) {
                    if (dataList != null && dataList.size() > 0) {

                        progressDialog = new ProgressDialog(context);
                        progressDialog.setIcon(R.drawable.green);
                        progressDialog.setTitle("消息上传");
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("正在上传消息" + "(" + MessageHandlerService.MessageCount + "/" + messagecount + ")");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条样式为水平
                        progressDialog.setButton2("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.isstop=true;
                                messageList = qurryMessageReal();
                                if(dataList.size()>0){
                                    for(int i=0;i<dataList.size();i++){
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String date=df.format(new Date());
                                        try{
                                            Date dt1 = df.parse(dataList.get(i).get("CREATEDATE"));
                                            Date dt2 = df.parse(messageList.size()>0?messageList.get(0).get("CREATEDATE"):date);
                                            if (dt1.getTime() < dt2.getTime()) {
                                                dataList.remove(i);
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            Constants.recordExceptionInfo(e, context, context.toString());
                                        }
                                    }
                                }
                                messageListviewAdapter.notifyDataSetChanged();
                            }
                        });
                        progressDialog.show();

                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                Map resMap = (Map)msg.obj;
                                if(resMap==null){
                                    //消息发送失败数量
                                    failcount++;
                                }
                                if(failcount==messagecount){
                                    Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }else{
                                    progressDialog.setMax(messagecount);//设置消息总数
                                    progressDialog.setMessage("正在上传消息" + "(" + MessageHandlerService.MessageCount + "/" + messagecount + ")");
                                    //更新进度条
                                    progressDialog.setProgress( MessageHandlerService.MessageCount);
                                }
                                if(MessageHandlerService.MessageCount==messagecount){
                                    messageListviewAdapter.listmessage.clear();
                                    messageListviewAdapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                    Toast.makeText( context, "消息上传完成,"+"失败"+failcount+"条", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        if(MessageHandlerService.flagRunnable==true){

                            if(textdataList.size()!=0){
                                MessageHandlerService.queryMessage(dataList, handler, context);//上传文本
                            }
                            if(imagedataList.size()!=0){
//                                MessageHandlerService.uploadFile(imagedataList,handler,context); //上传图片
                            }

                        }else{
                            Toast.makeText( context, "后台发送中...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(context, "没有可上传的数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.message_image_screen:

                mDrawerLayout.openDrawer(Gravity.RIGHT);

                break;
            case R.id.submit_select_condition:

                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                String text = mTxtMessageSelectValue.getText().toString();
                if(("文本消息").equals(text)){
                    messageType = "1";
                    String textCount = sympMessageRealDao.queryMessageCount("1");
                    mTxtTextNum.setText(textCount+"条");
                    mTxtMeaaageNum.setText(0+"条");
                    dataList.clear();
                    dataList.addAll(queryMessageByCondition());
                }else if(("图片消息").equals(text)){
                    messageType = "2";
                    String picCount = sympMessageRealDao.queryMessageCount("2");
                    mTxtMeaaageNum.setText(picCount+"条");
                    mTxtTextNum.setText(0+"条");
                    dataList.clear();
                    dataList.addAll(queryMessageByCondition());
                }else if(("全部消息").equals(text)){
                    messageType = null;
                    String textCount = sympMessageRealDao.queryMessageCount("1");
                    String picCount = sympMessageRealDao.queryMessageCount("2");
                    mTxtTextNum.setText(textCount+"条");
                    mTxtMeaaageNum.setText(picCount+"条");
                    dataList.clear();
                    dataList.addAll(queryMessageByCondition());
                }

                messageListviewAdapter.notifyDataSetChanged();

                break;
            case R.id.message_type_LinearLayout:

                mDrawerLayout.openDrawer(childList);

                break;
            case R.id.lyt_message_type_text:

                mTxtMessageSelectValue.setText("文本消息");
                mDrawerLayout.closeDrawer(childList);

                break;
            case R.id.lyt_message_type_pic:

                mTxtMessageSelectValue.setText("图片消息");
                mDrawerLayout.closeDrawer(childList);

                break;
            case R.id.lyt_message_type_all:

                mTxtMessageSelectValue.setText("全部消息");
                mDrawerLayout.closeDrawer(childList);

                break;
            case R.id.father_menu_back:

                mDrawerLayout.closeDrawer(Gravity.RIGHT);

                break;
            case R.id.child_menu_back:

                mDrawerLayout.closeDrawer(childList);

                break;
            //自动
            case R.id.radio_automatic:
                messageimage.setVisibility(View.GONE);
                updateMessageType("0");
                Constants.SENDMESSAGEFLAG = "0";
                break;
            //WIFI
            case R.id.radio_wifi:
                messageimage.setVisibility(View.GONE);
                updateMessageType("1");
                Constants.SENDMESSAGEFLAG = "1";
                break;
            //手动
            case R.id.radio_handmovement:
                messageimage.setVisibility(View.VISIBLE);
                updateMessageType("2");
                Constants.SENDMESSAGEFLAG = "2";
                break;
        }
    }


    private void updateMessageType(String type) {
        Intent intent=new Intent("MESSAGE");
        intent.putExtra("type", type);
        sendBroadcast(intent);
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                if (listdata(messageType).size() == 0) {
                    editor.putString("isok", "false");
                } else {
                    editor.putString("isok", "ture");
                }
                editor.commit();
                if(listdata(messageType).size()>0){

                    dataList.addAll(listdata(messageType));
                    messageListviewAdapter.notifyDataSetChanged();
                    if(dataList.size()>=20){

                        listview.setLoadEnable(true);
                    }else{
                        listview.setLoadEnable(false);

                    }
                }else{
                    Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                }
                onLoad();
            }
        }, 2000);

    }

    private void onLoad() {

        listview.stopLoadMore();

    }


    /**
     * 根据条件筛选
     * @return
     */
    private List<Map<String, String>> queryMessageByCondition(){
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list = sympMessageRealDao.querySympMessageRealByParameter2(null,messageType);
        return list;
    }

    private  List<Map<String, String>> listdata(String type){
        messageList = qurryMessageReal();List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        if(listview.getCount()!=0) {
            if(dataList.size()>0){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date=df.format(new Date());
                for(int i=0;i<dataList.size();i++){
                    try{
                        Date dt1 = df.parse(dataList.get(i).get("CREATEDATE"));
                        Date dt2 = df.parse(messageList.size()>0?messageList.get(0).get("CREATEDATE"):date);
                        if (dt1.getTime() < dt2.getTime()) {
                            dataList.remove(i);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Constants.recordExceptionInfo(e, context, context.toString());
                    }
                }
                list = sympMessageRealDao.querySympMessageRealByParameter2(dataList.size()>0?dataList.get(dataList.size()-1).get("CREATEDATE"):date,type);
            }

        }else {
            list = sympMessageRealDao.querySympMessageRealByParameter2(null,type);
        }
        return list;
    }

    static class MessageListviewAdapter extends BaseAdapter {
        public List<Map<String, String>> listmessage;
        private Context context;

        MessageListviewAdapter(Context context,List<Map<String, String>> listmessage) {
            this.listmessage = listmessage;
            this.context = context;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return listmessage.size();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.message_listview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.serialnumber = (TextView) convertView.findViewById(R.id.serialnumber);
                viewHolder.messageid = (TextView) convertView.findViewById(R.id.messageid);
                viewHolder.messagetype = (TextView) convertView.findViewById(R.id.messagetype);
                convertView.setTag(viewHolder);
            }else {

                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.serialnumber.setText(position + 1 + "");
            viewHolder.messageid.setText(listmessage.get(position).get("MESSAGEID"));
            String type = listmessage.get(position).get("MESSAGETYPE");
            switch (type) {
                case "1":
                    viewHolder.messagetype.setText("文本");
                    break;
                case "2":
                    viewHolder.messagetype.setText("图片");
                    break;
                case "3":
                    viewHolder.messagetype.setText("文件");
                    break;
            }

            return convertView;
        }

        static class ViewHolder {
            TextView serialnumber;
            TextView messageid;
            TextView messagetype;
            TextView messagedata;
        }
    }

    /**
     * 查询消息记录
     * @return
     */
    private List<Map<String, String>> qurryMessageReal() {
        List<Map<String, String>> list = sympMessageRealDao.querySympMessageRealByParameter1(null);
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataList.clear();
        messageList.clear();
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
}
