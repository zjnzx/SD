package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympCommunicationAccessoryDao;
import com.energyfuture.symphony.m3.dao.SympCommunicationPromptDao;
import com.energyfuture.symphony.m3.dao.SympCommunicationThemeDao;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.domain.SympCommunicationAccessory;
import com.energyfuture.symphony.m3.domain.SympCommunicationPrompt;
import com.energyfuture.symphony.m3.domain.SympCommunicationTheme;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.ui.MultiSpinnerCommCreate;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunionThemeCreateActivity extends ActionBarActivity implements View.OnClickListener {
    private Context context=CommunionThemeCreateActivity.this;
    public static String pathTemp = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static  String FILE_SAVEPATH = pathTemp.substring(0, pathTemp.length() - 1) + "legacy" + "/M3/transformer/";
    private TextView create_content,create_part,create_theme;
    private LinearLayout menu_back,menu_check;
    private RelativeLayout create_add;
    private EditText reply_img_num,reply_temp;
    private static final int REQUEST_PICK = 0;
    private GridView create_gridview;
    private GridAdapter gridAdapter;
    private ArrayList<String> selectedPicture = new ArrayList<String>();
    private SimpleAdapter adapter;
    private String path;
    private DisplayImageOptions options;
    private String spinnerMultiPerson;//交流群成员
    private String ID= Constants.getUuid();
    private Map<String,List<Map<String, Object>> > messageContentText = new HashMap<String,List<Map<String, Object>> >();
    List<String> dataTypeList = new ArrayList<String>();
    private SympMessageRealDao messageRealDao = new SympMessageRealDao(context);
    private SympCommunicationPromptDao ympCommunicationPromptDao = new SympCommunicationPromptDao(context);
    private String picUrl,userName,userId;
    private SympCommunicationTheme theme;
    private String belongId,launchPosition,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.communion_theme_create);

        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.pic_loading)
                .showImageForEmptyUri(R.drawable.comm_add_pic_selector).showImageOnFail(R.drawable.comm_add_pic_selector)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    private void initView() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        type = (String) bundle.get("type");
        if(type.equals("project")){
            TrProject trProject=(TrProject) bundle.get("belongType");
            belongId = trProject.getProjectid();
            launchPosition = trProject.getProjectname();
        }else if(type.equals("task")){
            TrTask trTask=(TrTask) bundle.get("belongType");
            belongId = trTask.getTaskid();
            launchPosition = trTask.getTaskname();
        }

        Map<String,String> map= Constants.getLoginPerson(context);
        userId=map.get("userId");
        userName=map.get("userName");

        create_content = (TextView) findViewById(R.id.create_content);//主题内容
        create_theme = (TextView) findViewById(R.id.create_theme);//主题
        create_add = (RelativeLayout) findViewById(R.id.create_add);//附件添加按钮
        create_part = (TextView) findViewById(R.id.create_part);//主贴
        create_gridview = (GridView) findViewById(R.id.create_gridview);//图片展示控件
        menu_back = (LinearLayout)findViewById(R.id.menu_back);
        menu_check = (LinearLayout)findViewById(R.id.menu_check);
        menu_back.setOnClickListener(this);
        menu_check.setOnClickListener(this);
        create_add.setOnClickListener(this);

        //初始化ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        gridAdapter = new GridAdapter();
        create_gridview.setAdapter(gridAdapter);

        setSpinner();
        //        设置主贴内容不可编辑
        create_part.setEnabled(false);
       //        设置主贴内容
        create_part.setText(launchPosition);

    }
    private void setSpinner(){
        adapter = new SimpleAdapter(this,Constants.getMulSpinnerUser(context),R.layout.spinner_user,new String[]{"name","id"},new int[]{R.id.spinner_username,R.id.spinner_userid});
        MultiSpinnerCommCreate spinner = (MultiSpinnerCommCreate) findViewById(R.id.spinnerMulti);
        spinner.setAdapter(adapter, false, onSelectedListener);

    }

    private MultiSpinnerCommCreate.MultiSpinnerListener onSelectedListener = new MultiSpinnerCommCreate.MultiSpinnerListener() {

        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    String item = adapter.getItem(i).toString();
                    String id = item.substring(item.lastIndexOf("=")+1,item.indexOf("}"));
                    builder.append(id).append(";");
                }
            }
            spinnerMultiPerson=builder.toString();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_add:
                startActivityForResult(new Intent(this, CommunionSelectPictureActivity.class), REQUEST_PICK);
                break;
            case R.id.menu_check:
                if(!createCommuionTheme()){
                    break;
                }
                insertCommnuicationAccessory();
                insertPrompt();
                addMessage();
                menu_check.setVisibility(View.GONE);
                //清空
                if(Constants.mapUrl.size() > 0){
                    Constants.mapUrl.clear();
                }
                if(spinnerMultiPerson != null && !spinnerMultiPerson.equals("")){
                    CommunionThemeCreateActivity.this.onBackPressed();
                }
                break;
            case R.id.menu_back:
                CommunionThemeCreateActivity.this.onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            selectedPicture = (ArrayList<String>) data.getSerializableExtra(
                    CommunionSelectPictureActivity.INTENT_SELECTED_PICTURE);
            if(selectedPicture.size() != 0){
                selectedPicture.add("");
            }
            gridAdapter.notifyDataSetChanged();
        }
    }

    class GridAdapter extends BaseAdapter {
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(190, 190);

        @Override
        public int getCount() {
            return selectedPicture.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.communion_pic_del_item, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.create_image);
                holder.delete = (Button) convertView.findViewById(R.id.delete);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.picChecked.remove(selectedPicture.get(position));
                    if(Utils.cameraPath.contains(selectedPicture.get(position))){
                        Utils.cameraPath.remove(position);
                    }
                    selectedPicture.remove(position);
                    if(selectedPicture.size() == 1){
                        options = null;
                        selectedPicture = new ArrayList<String>();
                    }

                    notifyDataSetChanged();
                }
            });
            if(position == (selectedPicture.size()-1)){
                 holder.delete.setVisibility(View.GONE);
                 holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(context, CommunionSelectPictureActivity.class), REQUEST_PICK);
                    }
                });
            }else{
                 holder.delete.setVisibility(View.VISIBLE);
            }
                 ImageLoader.getInstance().displayImage("file://" + selectedPicture.get(position), holder.imageView,options);
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageView;
        Button delete;
    }
    /**
     * 主贴创建
     *
     * 2015/8/13
     */
    private boolean createCommuionTheme(){
        if(TextUtils.isEmpty(create_theme.getText())){
            Toast.makeText(context,"沟通交流主题不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(create_content.getText())){
            Toast.makeText(context,"沟通交流主题内容不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(spinnerMultiPerson!=null){
            if(!spinnerMultiPerson.contains(userId)){
                spinnerMultiPerson = spinnerMultiPerson + userId + ";";
            }
        }else{
            Toast.makeText(context,"请选择交流群成员",Toast.LENGTH_SHORT).show();
            return false;
        }

       List<SympCommunicationTheme> communicationThemeList=new ArrayList<>();
       SympCommunicationThemeDao sympCommunicationThemeDao=new SympCommunicationThemeDao(context);
       SympCommunicationTheme sympCommunicationTheme=new SympCommunicationTheme();
       sympCommunicationTheme.setId(ID);
       sympCommunicationTheme.setBelongId(belongId);
       sympCommunicationTheme.setCreatepersonid(userId);
       sympCommunicationTheme.setCreatetime(URLs.dateformat.format(new Date()));
       sympCommunicationTheme.setParticipator(spinnerMultiPerson);
       sympCommunicationTheme.setThemetitle(create_theme.getText().toString());
       sympCommunicationTheme.setThemecontent(create_content.getText()+"");
       sympCommunicationTheme.setThemestate("1");
        if(type.equals("project")){
            sympCommunicationTheme.setPosition("project");
        }else if(type.equals("task")){
            sympCommunicationTheme.setPosition("task");
        }
        communicationThemeList.add(sympCommunicationTheme);
        //主贴表中插入数据
        sympCommunicationThemeDao.insertListData(communicationThemeList);
        // 向平台发送主贴消息
        List<Map<String, Object>> listMessage=new ArrayList<Map<String, Object>>();

        for(SympCommunicationTheme bean : communicationThemeList){
            Map<String, Object> messageContent = new HashMap<String, Object>();
            messageContent.put("ID",bean.getId());
            messageContent.put("BELONGID",bean.getBelongId());
            messageContent.put("POSITION",bean.getPosition());
            messageContent.put("THEMETITLE",bean.getThemetitle());
            messageContent.put("THEMECONTENT",bean.getThemecontent());
            messageContent.put("CREATEPERSONID",bean.getCreatepersonid());
            messageContent.put("CREATETIME",bean.getCreatetime());
            messageContent.put("THEMESTATE", bean.getThemestate());
            messageContent.put("PARTICIPATOR",bean.getParticipator());
            listMessage.add(messageContent);
        }
        messageContentText.put("6",listMessage);
        dataTypeList.add("6");//主贴表

        return true;
    }

    /**
     * 插入附件表
     */
    private void insertCommnuicationAccessory(){
        picUrl = FILE_SAVEPATH+"comm/"+ID+"/"+"small/";
        String opicUrl = FILE_SAVEPATH+"comm/"+ID+"/"+"original/";
        for(int i = 0;i < selectedPicture.size();i++){
            if(!selectedPicture.get(i).equals(""));
            String fileName = selectedPicture.get(i).substring(selectedPicture.get(i).lastIndexOf("/")+1);
            if(!fileName.equals("")){
                Constants.saveFile(selectedPicture.get(i), picUrl + fileName,picUrl,"2");
                Constants.saveFile(selectedPicture.get(i), opicUrl + fileName,opicUrl,"");
            }
        }

        SympCommunicationAccessoryDao sympCommunicationAccessoryDao=new SympCommunicationAccessoryDao(context);
        List<SympCommunicationAccessory> sympCommunicationAccessoryList=new ArrayList<>();
        for(Map.Entry<String, String> entry: Constants.mapUrl.entrySet()){
             SympCommunicationAccessory sympCommunicationAccessory=new SympCommunicationAccessory();
             sympCommunicationAccessory.setAccessorytype("1");
             sympCommunicationAccessory.setFilename(entry.getValue());
             sympCommunicationAccessory.setFileurl("comm/"+ID+"/");
             sympCommunicationAccessory.setId(Constants.getUuid());
             sympCommunicationAccessory.setRecordtime(URLs.dateformat.format(new Date()));
             sympCommunicationAccessory.setReplyid("");
             sympCommunicationAccessory.setThemeid(ID);
             sympCommunicationAccessory.setRemarks("");
             sympCommunicationAccessoryList.add(sympCommunicationAccessory);

         }
         sympCommunicationAccessoryDao.insertListData(sympCommunicationAccessoryList);

        // 向平台发送附件消息
        if(sympCommunicationAccessoryList.size()>0) {
            for (SympCommunicationAccessory bean : sympCommunicationAccessoryList) {
                String fileUrl = bean.getFileurl().substring(bean.getFileurl().indexOf("comm"),bean.getFileurl().lastIndexOf("/")+1);
                Constants.communionImage(bean,fileUrl,context,userId);
            }
        }
    }
    /**
     * 提醒表中插入数据
     * @param
     */
    private void insertPrompt(){
        //封装消息内容
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //如果有@,向消息提醒表中插入数据
        List<SympCommunicationPrompt> promptList = new ArrayList<SympCommunicationPrompt>();
        if(spinnerMultiPerson == null || spinnerMultiPerson.equals("")){
            Toast.makeText(context,"请选择参与人员",Toast.LENGTH_SHORT).show();
            return;
        }
        String [] personid=spinnerMultiPerson.split(";");
        for(int i=0; i<personid.length;i++) {
            SympCommunicationPrompt sympCommunicationPrompt = new SympCommunicationPrompt();
            sympCommunicationPrompt.setId(Constants.getUuid());
            sympCommunicationPrompt.setThemeid(ID);
            sympCommunicationPrompt.setPrompttype("0");
            sympCommunicationPrompt.setState("1");
            sympCommunicationPrompt.setPromptcontent("主贴参与讨论");
            sympCommunicationPrompt.setCreatetime(URLs.dateformat.format(new Date()));
            sympCommunicationPrompt.setReplyid("");
            sympCommunicationPrompt.setPromptpersonid(personid[i]);
            promptList.add(sympCommunicationPrompt);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID", sympCommunicationPrompt.getId());
            map.put("THEMEID", sympCommunicationPrompt.getThemeid());
            map.put("REPLYID", sympCommunicationPrompt.getReplyid());
            map.put("PROMPTCONTENT", sympCommunicationPrompt.getPromptcontent());
            map.put("CREATETIME", sympCommunicationPrompt.getCreatetime());
            map.put("STATE", sympCommunicationPrompt.getState());
            map.put("PROMPTTYPE", sympCommunicationPrompt.getPrompttype());
            map.put("PROMPTPERSONID", sympCommunicationPrompt.getPromptpersonid());
            list.add(map);
        }
        messageContentText.put("8",list);
        dataTypeList.add("8");//提醒表
        ympCommunicationPromptDao.insertListData(promptList);

    }

    /**
     *
     * 消息发送
     */
    private  void addMessage(){
        try {
            //回复表,主贴表和提醒表消息
            if (messageContentText != null && messageContentText.size() > 0) {
                messageRealDao.addMessageList(Constants.getSympMessageReal(context, "1",userName+"发起了一个讨论"), dataTypeList, 1, messageContentText);
            }
        }catch (Exception e){
            e.printStackTrace();
            Constants.recordExceptionInfo(e,context,context.toString());
        }
    }


}
