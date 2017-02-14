package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.CommunionSendReplyPicAdapter;
import com.energyfuture.symphony.m3.adapter.CommunionThemeReplyAdapter;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.common.Base;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympCommunicationAccessoryDao;
import com.energyfuture.symphony.m3.dao.SympCommunicationPromptDao;
import com.energyfuture.symphony.m3.dao.SympCommunicationReplyDao;
import com.energyfuture.symphony.m3.dao.SympCommunicationThemeDao;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.domain.SympCommunicationAccessory;
import com.energyfuture.symphony.m3.domain.SympCommunicationPrompt;
import com.energyfuture.symphony.m3.domain.SympCommunicationReply;
import com.energyfuture.symphony.m3.domain.SympCommunicationTheme;
import com.energyfuture.symphony.m3.domain.SympMessageReal;
import com.energyfuture.symphony.m3.piclook.ImagePagerCommActivity;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.ui.MultiSpinner;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.OnClickListener;

public class CommunionThemeReplyActivity extends ActionBarActivity implements OnClickListener{
    private static final int REQUEST_PICK = 0;
    public static String pathTemp = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static  String FILE_SAVEPATH = pathTemp.substring(0, pathTemp.length() - 1) + "legacy" + "/M3/transformer/";
    private List<SympCommunicationReply> sympCommunicationReplyList = new ArrayList<SympCommunicationReply>();
    private Context context=CommunionThemeReplyActivity.this;
    private TextView reply_theme_position,reply_theme_person,reply_theme_time,no_reply,reply_object_name,reply_see_more,reply_theme_content,reply_theme_title,reply_text,reply_person,reply_time,reply_content,reply_count,reply_call,reply_person_name;
    private ImageView reply_theme_img,reply_person_img;
    private EditText reply_input;
    private GridView reply_theme_picture;
    private View viewItem;
    private ImageView theme_reply;
    private LinearLayout reply_back,reply_pic;
    private ListView listView;
    private Toolbar toolbar;
    private GridView reply_gridview,reply_item_gridview;
    private PicGridAdapter picGridAdapter,reply_theme_pictureAdpter;
    private ArrayList<String> selectedPicture = new ArrayList<String>();
    private DisplayImageOptions options;
    private Button reply_send;
    private ProgressBar mProgressBar;
    private String spinnerMultiPerson;
    private SympCommunicationTheme theme;
    private CommunionThemeReplyAdapter mAdapter;
    private SympCommunicationReplyDao sympCommunicationReplyDao = new SympCommunicationReplyDao(context);
    private SympCommunicationPromptDao ympCommunicationPromptDao = new SympCommunicationPromptDao(context);
    private SympCommunicationAccessoryDao sympCommunicationAccessoryDao = new SympCommunicationAccessoryDao(context);
    private SimpleAdapter adapter;
    private String replyId;
    private List<String> urlList = new ArrayList<String>();
    private CommunionSendReplyPicAdapter replyPicAdapter;
    private String type = "1";
    private boolean flag = false;
    private SympCommunicationReply reply;
    private RelativeLayout activity_reply;
    private String picUrl,opicUrl,loginUserId,loginUserName;
    private Map<String,List<Map<String, Object>> > messageContent = new HashMap<String,List<Map<String, Object>> >();
    private List<String> dataTypeList=new ArrayList<String>();
    private List<String> dataTypeListDel=new ArrayList<String>();
    private Map<String,List<Map<String, Object>> > messageContentDel = new HashMap<String,List<Map<String, Object>> >();
    private String replyTime = "",belongType,launchPosition;
    private int count = 0;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Map<String, Object> condMap;
    private Handler handler1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.communion_theme_reply);

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.pic_loading)
                .showImageForEmptyUri(R.drawable.pic_loading).showImageOnFail(R.drawable.pic_loading)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();

        initView();
        setRefresh();

    }


    @Override
    protected void onRestart() {
        Utils.iscurrentpage=true;// 设置消息提醒为当前页面
        Utils.themeid=theme.getId();
        super.onRestart();
        handler.post(runnable);
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.pic_loading)
                .showImageForEmptyUri(R.drawable.comm_add_pic_selector).showImageOnFail(R.drawable.comm_add_pic_selector)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
    }
    //实时同步数据
    Handler  handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            new DataSyschronized(context).getDataFromWeb(condMap, handler1);
            List<SympCommunicationReply> list = queryReplyList("");
             if(list.size() > 0){
//                 Utils.isMore = true;
                 new InitializeApplicationsTask().execute();
            }
            handler.postDelayed(runnable, 5000);
        }
    };

    /**
     * 从平台同步数据
     *
     */
    private  void setRefresh(){
        final Map<String, Object> condMap = new HashMap<String, Object>();
        condMap.put("OBJ","COMMREPLY");
        condMap.put("TYPE",null);
        condMap.put("SIZE",5);
        condMap.put("USERID",loginUserId);
        condMap.put("KEY",theme.getId());
        condMap.put("UPDATETIME",replyTime);
        handler1 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1 && msg.obj != null){
                    new InitializeApplicationsTask().execute();
                }
            }
        };
        Utils.position=1;
        //第一进来请求刷新
        new DataSyschronized(context).getDataFromWeb(condMap, handler1);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#0288D1"));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                condMap.put("UPDATETIME",replyTime);
                new DataSyschronized(context).getDataFromWeb(condMap, handler1);
            }
        });
        //定时器操作类,每隔5秒从数据库刷一下数据
        handler.postDelayed(runnable, 5000);
    }
    /**
     * 查询回复表数据
     * @return
     */
    private List<SympCommunicationReply> queryReplyList(String replyTime){
        SympCommunicationReply sympCommunicationReply = new SympCommunicationReply();
        sympCommunicationReply.setThemeid(theme.getId());
        sympCommunicationReply.setReplytime(replyTime);
        List<SympCommunicationReply> replyList = sympCommunicationReplyDao.querySympCommunicationReplyList(sympCommunicationReply);
        return replyList;
    }

    /**
     * 设置toolbar
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunionThemeReplyActivity.this.onBackPressed();
                Utils.iscurrentpage = false;
                Utils.themeid = "";
            }
        });

    }

    private void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        theme = (SympCommunicationTheme)bundle.get("theme");
        launchPosition = (String)bundle.get("launchPosition");

        Utils.themeid=theme.getId();
        picUrl = FILE_SAVEPATH+"comm/"+theme.getId()+"/small/";
        opicUrl = FILE_SAVEPATH+"comm/"+theme.getId()+"/original/";
        loginUserId = Constants.getLoginPerson(context).get("userId");
        loginUserName = Constants.getLoginPerson(context).get("userName");

        viewItem = LayoutInflater.from(context).inflate(R.layout.communion_theme_reply_item, null);
        reply_person_img = (ImageView) viewItem.findViewById(R.id.reply_person_img);
        reply_person= (TextView) viewItem.findViewById(R.id.reply_person);
        reply_time= (TextView) viewItem.findViewById(R.id.reply_time);
        reply_call= (TextView) viewItem.findViewById(R.id.reply_call);
        reply_content= (TextView) viewItem.findViewById(R.id.reply_content);
        reply_object_name= (TextView) viewItem.findViewById(R.id.reply_object_name);
        reply_person_name= (TextView) viewItem.findViewById(R.id.reply_person_name);
        reply_text= (TextView) viewItem.findViewById(R.id.reply_text);
        reply_item_gridview= (GridView) viewItem.findViewById(R.id.reply_item_gridview);
        toolbar = (Toolbar) findViewById(R.id.reply_toolbar);
        reply_theme_position = (TextView) findViewById(R.id.reply_theme_position);
        reply_theme_img = (ImageView) findViewById(R.id.reply_theme_img);
        reply_pic = (LinearLayout) findViewById(R.id.reply_pic);
        reply_input = (EditText) findViewById(R.id.reply_input);
        reply_theme_person = (TextView) findViewById(R.id.reply_theme_person);
        reply_theme_time = (TextView) findViewById(R.id.reply_theme_time);
        reply_theme_title = (TextView) findViewById(R.id.reply_theme_title);
        reply_theme_content = (TextView) findViewById(R.id.reply_theme_content);
        reply_theme_picture = (GridView) findViewById(R.id.reply_theme_picture);
        reply_count = (TextView) findViewById(R.id.reply_count);
        reply_see_more = (TextView) findViewById(R.id.reply_see_more);
        no_reply = (TextView) findViewById(R.id.no_reply);
        reply_send = (Button) findViewById(R.id.reply_send);
        reply_gridview = (GridView) findViewById(R.id.reply_gridview);
        listView = (ListView) findViewById(R.id.reply_listview);
        mProgressBar = (ProgressBar) findViewById(R.id.reply_bar);
        activity_reply = (RelativeLayout) findViewById(R.id.activity_reply);
        theme_reply = (ImageView)findViewById(R.id.theme_reply);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.reply_swip);

        reply_pic.setOnClickListener(this);
        reply_send.setOnClickListener(this);
        theme_reply.setOnClickListener(this);
        reply_see_more.setOnClickListener(this);
        int headUser = 0;
        if (theme.getCreatepersonid()!=null){
            headUser=  Constants.userHead(theme.getCreatepersonid());
        }
        //根据用户id查用户姓名
        List<String> userList = Constants.getUserNameById(theme.getCreatepersonid(), context);
        //查询总回复数量
        String replyCount = sympCommunicationReplyDao.querySympCommunicationReplyCount(theme.getId(),"");
        //封装参数
        theme.setCreatepersonname(userList.get(0));
        theme.setHead(headUser);
        theme.setReplyCount(replyCount);

        String createTime = theme.getCreatetime();
        if(!theme.getCreatetime().contains("-")){
            Long timestamp = Long.parseLong(theme.getCreatetime());
            createTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
        }
        reply_count.setText("讨论信息["+theme.getReplyCount()+"]");
        reply_theme_position.setText(launchPosition);
        reply_theme_title.setText(theme.getThemetitle());
        reply_theme_content.setText(theme.getThemecontent());
        reply_theme_time.setText(createTime);
        reply_theme_img.setImageResource(headUser);
        reply_theme_person.setText(theme.getCreatepersonname());

        //初始化ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);


        //主贴图片展示
        reply_theme_pictureAdpter=new PicGridAdapter(qurrySympCommunicationAccessory(),2,1);
        reply_theme_picture.setAdapter(reply_theme_pictureAdpter);

        setToolbar();
        setSpinner();
        initListView();
        initGridView();
    }

    private void initListView(){
        mAdapter =new CommunionThemeReplyAdapter(sympCommunicationReplyList,context,picUrl,opicUrl,theme.getId());
        listView.setAdapter(mAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SympCommunicationReply sympCommunicationReply = mAdapter.list.get(position);
                //判断权限
                if(sympCommunicationReply.getReplypersonid().equals(loginUserId)){
                    deleteReply(position,sympCommunicationReply);
                }else{
                    Toast.makeText(context,"您无权删除该回复",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    /**
     * 删除回复
     * @param position
     */
    public void deleteReply(final int position,final SympCommunicationReply sympCommunicationReply) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(this);
        mMaterialDialog.setTitle("确认要删除吗？").setMessage("")
                .setPositiveButton(
                        "删除", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    String replyId = sympCommunicationReply.getId();
                                    String replyPersonId = sympCommunicationReply.getReplypersonid();
                                    //删除回复表
                                    sympCommunicationReplyDao.deleteSympCommunicationReply(replyId,"ID");

                                    //查询是否有图片
                                    SympCommunicationAccessory sympCommunicationAccessory = new SympCommunicationAccessory();
                                    sympCommunicationAccessory.setReplyid(replyId);
                                    List<SympCommunicationAccessory> accList = sympCommunicationAccessoryDao.querySympCommunicationAccessoryList(sympCommunicationAccessory);

                                    //查询是否有提醒
                                    SympCommunicationPrompt sympCommunicationPrompt = new SympCommunicationPrompt();
                                    sympCommunicationPrompt.setReplyid(replyId);
                                    List<SympCommunicationPrompt> promptlist = ympCommunicationPromptDao.querySympCommunicationPromptList(sympCommunicationPrompt);

                                    mAdapter.list.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    reply_count.setText("讨论信息[" + mAdapter.list.size() + "]");
                                    //发送消息
                                    sendMessageDelete(replyId, accList, promptlist);

                                    Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                                    mMaterialDialog.dismiss();
                                } catch (Exception e) {
                                    Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                    mMaterialDialog.dismiss();
                                    Constants.recordExceptionInfo(e,context,context.toString());
                                }
                            }
                        }
                )
                .setNegativeButton(
                        "取消", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        }
                )
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
     * 设置回复中的图片显示
     */
    private void initGridView(){
        replyPicAdapter = new CommunionSendReplyPicAdapter(context,urlList,replyId,picUrl,opicUrl,theme.getId());
        reply_item_gridview.setAdapter(replyPicAdapter);
        replyPicAdapter.notifyDataSetChanged();
    }

    /**
     * 获取所有人员
     */
    private void setSpinner(){
        adapter = new SimpleAdapter(this,Constants.getMulSpinnerUser(context),R.layout.spinner_user,new String[]{"name","id"},new int[]{R.id.spinner_username,R.id.spinner_userid});
        MultiSpinner spinner = (MultiSpinner) findViewById(R.id.reply_spinnerMulti);
        spinner.setAdapter(adapter, false, onSelectedListener);
    }

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
            StringBuilder builderName = new StringBuilder();
            StringBuilder builderId = new StringBuilder();
            String input = reply_input.getText().toString();

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    String item = adapter.getItem(i).toString();
                    String id = item.substring(item.lastIndexOf("=")+1,item.indexOf("}"));
                    String name = item.substring(item.indexOf("=")+1,item.indexOf(","));
                    builderName.append("@"+name);
                    builderId.append(id).append(";");
                }
            }
            if(("").equals(input)){
                builderName.append(":");
                reply_input.setText(builderName.toString());
                reply_input.setSelection(builderName.toString().length());
            }else{
                String text = input + " " + builderName.toString() + " ";
                reply_input.setText(text);
                reply_input.setSelection(text.length());
            }
            reply_input.requestFocus();
            spinnerMultiPerson=builderId.toString();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reply_pic://添加图片
                startActivityForResult(new Intent(this, CommunionSelectPictureActivity.class), REQUEST_PICK);
                break;
            case R.id.reply_see_more://查看更多
//                Utils.isMore = true;
//                new InitializeApplicationsTask().execute();
                break;
            case R.id.reply_send: //发送回复
                if(!reply_input.getText().toString().equals("")){
                    sendReply();
                    sendMessageReply();
                    reply_input.setText("");
                    reply_input.setHint("");
                    type = "1";
                    reply = null;
                    flag = false;
                    Utils.picChecked.clear();
                    messageContent.clear();
                    dataTypeList.clear();
                    //隐藏软键盘
//                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }else {
                    Toast.makeText(context,"内容不能为空!",Toast.LENGTH_SHORT).show();
                }
            break;
            case R.id.theme_reply:
                reply_input.setHint("");
                type = "1";
                //弹出键盘
                reply_input.requestFocus();
                InputMethodManager imm = (InputMethodManager) reply_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(reply_input,InputMethodManager.SHOW_FORCED);
                break;
            default:
                break;
        }
    }

    /**
     * 点击回复
     */
    public void replyForPerson(SympCommunicationReply reply){
        reply_input.setHint("回复"+reply.getPersonname()+":");
        this.reply = reply;
        //回复某一个人
        type = "2";
        //弹出键盘
        reply_input.requestFocus();
        InputMethodManager imm = (InputMethodManager) reply_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(reply_input,InputMethodManager.SHOW_FORCED);
    }

    /**
     * 发表回复
     */
    public void sendReply(){
        String date = URLs.dateformat.format(new Date());
        String inputContent = reply_input.getText().toString();
        int picCount = Utils.picChecked.size();

        //回复表插入数据
        insertReply(inputContent,date);

        listView.setVisibility(View.VISIBLE);
        reply_gridview.setVisibility(View.GONE);

        String replyCount = sympCommunicationReplyDao.querySympCommunicationReplyCount(theme.getId(), "");
        reply_count.setText("讨论信息["+replyCount+"]");

        //判断是否包含@
        if(inputContent.contains("@")){
            flag = true;

            //修改主题表中参与人员
            updateTheme();
        }

        //附件表中插入数据
        if(picCount > 0){
            insertAccessory(date);
        }
        //消息提醒表中插入数据
        insertPrompt(date);

        //回复后清空map
        if(Constants.mapUrl.size() > 0){
            Constants.mapUrl.clear();
        }

        new InitializeApplicationsTask().execute();
        int count = sympCommunicationReplyList.size();
//        if(count > 10){
//            Utils.isMore = true;
//        }
        //定位到最后一条
        listView.setSelection(sympCommunicationReplyList.size());
    }

    private void updateTheme(){
        SympCommunicationThemeDao sympCommunicationThemeDao = new SympCommunicationThemeDao(context);
        SympCommunicationTheme sympCommunicationTheme = new SympCommunicationTheme();
        sympCommunicationTheme.setId(theme.getId());
        List<SympCommunicationTheme> themeList = sympCommunicationThemeDao.querySympCommunicationThemeList(sympCommunicationTheme);
        String participator = themeList.get(0).getParticipator();
        Map<Object,Object> columnsMap = new HashMap<Object,Object>();
        Map<Object,Object> wheresMap = new HashMap<Object,Object>();
        columnsMap.put("PARTICIPATOR",participator+spinnerMultiPerson);
        wheresMap.put("ID",theme.getId());
        sympCommunicationThemeDao.udateSympCommunicationTheme(columnsMap,wheresMap);

        //封装消息内容
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("PARTICIPATOR", participator+spinnerMultiPerson);
        map.put("ID", theme.getId());
        list.add(map);

        Map<String,List<Map<String, Object>> > messageContent = new HashMap<String,List<Map<String, Object>> >();
        List<String> dataTypeList=new ArrayList<String>();
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        if(list != null && list.size() > 0){
            messageContent.put("6",list);
            dataTypeList.add("6");
        }
        try {
            //回复表和提醒表消息
            if(messageContent != null && messageContent.size() > 0){
                sympMessageRealDao.addMessageList(Constants.getSympMessageReal(context,"1","有新的参与人员"),dataTypeList,2,messageContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e,context,context.toString());
        }
    }

    /**
     * 回复表中插入数据
     * @param inputContent
     * @param date
     */
    private void insertReply(String inputContent,String date){
        SympCommunicationReply sympCommunicationReply = new SympCommunicationReply();

        if(type.equals("1")){//针对主题回复
            sympCommunicationReply.setReplyobjectid("");
            sympCommunicationReply.setReplyid("");
        }else if(type.equals("2")){//针对某人回复
            sympCommunicationReply.setReplyobjectid(reply.getReplypersonid());
            sympCommunicationReply.setReplyid(reply.getId());
        }
        replyId = Constants.getUuid();
        sympCommunicationReply.setId(replyId);
        sympCommunicationReply.setState("0");
        sympCommunicationReply.setReplycontent(inputContent);
        sympCommunicationReply.setReplypersonid(loginUserId);
        sympCommunicationReply.setReplytime(date);
        sympCommunicationReply.setThemeid(theme.getId());
        sympCommunicationReplyDao.insertListData(sympCommunicationReply);

        //封装消息内容
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("ID", sympCommunicationReply.getId());
        map.put("THEMEID", sympCommunicationReply.getThemeid());
        map.put("REPLYPERSONID", sympCommunicationReply.getReplypersonid());
        map.put("REPLYCONTENT", sympCommunicationReply.getReplycontent());
        map.put("REPLYTIME", sympCommunicationReply.getReplytime());
        map.put("REPLYID", sympCommunicationReply.getReplyid() == null ? "":sympCommunicationReply.getReplyid());
        map.put("REPLYOBJECTID", sympCommunicationReply.getReplyobjectid() == null ? "":sympCommunicationReply.getReplyobjectid());
        map.put("STATE",sympCommunicationReply.getState());
        list.add(map);
        if(list != null && list.size() > 0){
            messageContent.put("7",list);
            dataTypeList.add("7");//回复表
        }
    }

    /**
     * 提醒表中插入数据
     * @param date
     */
    private void insertPrompt(String date){
        //如果有@,向消息提醒表中插入数据
        List<SympCommunicationPrompt> promptList = new ArrayList<SympCommunicationPrompt>();
        String participator = theme.getParticipator();
//        String personId[] = (theme.getCreatepersonid()+";"+participator).split(";");
        String personId[] = participator.split(";");
        if(flag){//有人@
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for(int i = 0;i < personId.length;i++){
                SympCommunicationPrompt sympCommunicationPrompt = new SympCommunicationPrompt();
                sympCommunicationPrompt.setPromptpersonid(personId[i]);
                sympCommunicationPrompt.setPrompttype("3");
                sympCommunicationPrompt.setReplyid("");
                sympCommunicationPrompt.setId(Constants.getUuid());
                sympCommunicationPrompt.setThemeid(theme.getId());
                sympCommunicationPrompt.setState("0");
                sympCommunicationPrompt.setCreatetime(date);
                sympCommunicationPrompt.setPromptcontent(loginUserName+"在回复中@了你,请查看!");
                promptList.add(sympCommunicationPrompt);

                //封装消息内容
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ID",sympCommunicationPrompt.getId());
                map.put("THEMEID",sympCommunicationPrompt.getThemeid());
                map.put("REPLYID",sympCommunicationPrompt.getReplyid());
                map.put("PROMPTCONTENT",sympCommunicationPrompt.getPromptcontent());
                map.put("CREATETIME",sympCommunicationPrompt.getCreatetime());
                map.put("STATE",sympCommunicationPrompt.getState());
                map.put("PROMPTTYPE",sympCommunicationPrompt.getPrompttype());
                map.put("PROMPTPERSONID",sympCommunicationPrompt.getPromptpersonid());
                list.add(map);
            }
            if(list != null && list.size() > 0){
                messageContent.put("8",list);
                dataTypeList.add("8");//提醒表
            }
        }else{//有人回复
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for(int i = 0;i < personId.length;i++){
                SympCommunicationPrompt sympCommunicationPrompt = new SympCommunicationPrompt();
                sympCommunicationPrompt.setPrompttype("1");
                sympCommunicationPrompt.setReplyid(type.equals("1") ? "":reply.getId());//1:回复主题 2:回复某人
                sympCommunicationPrompt.setPromptpersonid(type.equals("1") ? personId[i]:reply.getReplypersonid());
                sympCommunicationPrompt.setId(Constants.getUuid());
                sympCommunicationPrompt.setThemeid(theme.getId());
                sympCommunicationPrompt.setState("0");
                sympCommunicationPrompt.setCreatetime(date);
                sympCommunicationPrompt.setPromptcontent(loginUserName+"回复了你,请查看!");
                promptList.add(sympCommunicationPrompt);

                //封装消息内容
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ID",sympCommunicationPrompt.getId());
                map.put("THEMEID",sympCommunicationPrompt.getThemeid());
                map.put("REPLYID",sympCommunicationPrompt.getReplyid());
                map.put("PROMPTCONTENT",sympCommunicationPrompt.getPromptcontent());
                map.put("CREATETIME",sympCommunicationPrompt.getCreatetime());
                map.put("STATE",sympCommunicationPrompt.getState());
                map.put("PROMPTTYPE",sympCommunicationPrompt.getPrompttype());
                map.put("PROMPTPERSONID",sympCommunicationPrompt.getPromptpersonid());
                list.add(map);
            }
            if(list != null && list.size() > 0){
                messageContent.put("8",list);
                dataTypeList.add("8");//提醒表
            }
        }
        ympCommunicationPromptDao.insertListData(promptList);

    }

    /**
     * 附件表中插入数据
     * @param date
     */
    private void insertAccessory(String date){
        SympCommunicationAccessoryDao sympCommunicationAccessoryDao = new SympCommunicationAccessoryDao(context);
        List<SympCommunicationAccessory> accList = new ArrayList<SympCommunicationAccessory>();

        urlList.clear();
        for(Map.Entry<String, String> entry: Utils.picChecked.entrySet()){
            if(!entry.getKey().equals("")){//picUrl = FILE_SAVEPATH+"COMM/"+theme.getId()+"/";
                String fileName = entry.getKey().substring(entry.getKey().lastIndexOf("/")+1);
                Constants.saveFile(entry.getKey(), picUrl + fileName,picUrl,"2");
                Constants.saveFile(entry.getKey(), opicUrl + fileName,opicUrl,"");
            }
        }
//        for(int i = 0;i < selectedPicture.size();i++){
//            if(!selectedPicture.get(i).equals(""));
//            Constants.saveFile(selectedPicture.get(i), picUrl);
//        }

        for(Map.Entry<String, String> entry: Constants.mapUrl.entrySet()){
            SympCommunicationAccessory sympCommunicationAccessory = new SympCommunicationAccessory();
            sympCommunicationAccessory.setId(Constants.getUuid());
            sympCommunicationAccessory.setThemeid(theme.getId());
            sympCommunicationAccessory.setRecordtime(date);
            sympCommunicationAccessory.setAccessorytype("1");
            sympCommunicationAccessory.setRemarks("");
            sympCommunicationAccessory.setReplyid(replyId);
            sympCommunicationAccessory.setFileurl("comm/"+theme.getId()+"/");
            sympCommunicationAccessory.setFilename(entry.getValue());
            sympCommunicationAccessory.setIsupload("0");
            accList.add(sympCommunicationAccessory);
            sympCommunicationAccessoryDao.insertListData(accList);

            urlList.add(entry.getKey());

            String url = sympCommunicationAccessory.getFileurl();
            String fileUrl = url.substring(url.indexOf("comm"),url.lastIndexOf("/")+1);
            Constants.communionImage(sympCommunicationAccessory,fileUrl,context,Constants.getLoginPerson(context).get("userId"));
        }

        if(urlList.size() > 0){
            replyPicAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 回复后向平台发送消息
     */
    private void sendMessageReply(){
        SympMessageReal sympMessageReal = Constants.getSympMessageReal(context, "1","您有新的回复,请查看!");
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        try {
            //回复表和提醒表消息
            if(messageContent != null && messageContent.size() > 0){
                sympMessageRealDao.addMessageList(sympMessageReal,dataTypeList,1,messageContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e,context,context.toString());
        }
    }

    /**
     * 删除后向平台发送消息
     */
    private void sendMessageDelete(String id,List<SympCommunicationAccessory> accessorys,List<SympCommunicationPrompt> prompts){
        SympMessageReal sympMessageReal = Constants.getSympMessageReal(context, "1","");
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        //回复表
        List<Map<String,Object>> replyList = new ArrayList<Map<String,Object>>();
        Map<String,Object> replyMap = new HashMap<String,Object>();
        replyMap.put("ID", id);
        replyList.add(replyMap);
        messageContentDel.put("7", replyList);
        dataTypeListDel.add("7");
        //附件表
        if(accessorys != null && accessorys.size() > 0){
            List<Map<String,Object>> accList = new ArrayList<Map<String,Object>>();
            for(SympCommunicationAccessory acc : accessorys){
                Map<String,Object> accMap = new HashMap<String,Object>();
                accMap.put("ID",acc.getId());
                accList.add(accMap);
            }
            messageContentDel.put("9",accList);
            dataTypeListDel.add("9");
            //删除附件表
            sympCommunicationAccessoryDao.deleteSympCommunicationAccessory(replyId,"REPLYID");
        }
        //提醒表
        if(prompts != null && prompts.size() > 0){
            List<Map<String,Object>> promptList = new ArrayList<Map<String,Object>>();
            for(SympCommunicationPrompt prompt : prompts){
                Map<String,Object> promptMap = new HashMap<String,Object>();
                promptMap.put("ID",prompt.getId());
                promptList.add(promptMap);
            }
            messageContentDel.put("8",promptList);
            dataTypeListDel.add("8");
            //提醒表
            ympCommunicationPromptDao.deleteSympCommunicationPrompt(replyId,"REPLYID");
        }

        try {
            //回复表和附件表消息
            if(messageContentDel != null && messageContentDel.size() > 0){
                sympMessageRealDao.addMessageList(sympMessageReal,dataTypeListDel,3,messageContentDel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e,context,context.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            selectedPicture = (ArrayList<String>) data
                    .getSerializableExtra(CommunionSelectPictureActivity.INTENT_SELECTED_PICTURE);
            if(selectedPicture.size() != 0){
                selectedPicture.add("");
            }

           //发送图片的展示
            picGridAdapter = new PicGridAdapter(selectedPicture,1,2);
            reply_gridview.setAdapter(picGridAdapter);
            picGridAdapter.notifyDataSetChanged();
            //隐藏listview
            listView.setVisibility(View.GONE);
            reply_gridview.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 图片显示适配器
     */
    class PicGridAdapter extends BaseAdapter {
        List<String>  pictureUrl;
        int flag;//1;回复 2；主贴   判断是那类图片
        int type;//1;保存后路径图片 2其他路径图片
        PicGridAdapter(List<String>  picurl,int flag,int type){
            this.pictureUrl=picurl;
            this.flag=flag;
            this.type=type;

        }
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(190, 190);

        @Override
        public int getCount() {
            return pictureUrl.size();
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
            ViewHolder1 holder = null;
            if (convertView == null) {
                if(flag==1) {//回复的图片
                    convertView = View.inflate(context, R.layout.communion_pic_del_item, null);
                    holder = new ViewHolder1();
                    holder.imageView = (ImageView) convertView.findViewById(R.id.create_image);
                    holder.delete = (Button) convertView.findViewById(R.id.delete);
                    if(position == (pictureUrl.size()-1)){
                        holder.delete.setVisibility(View.GONE);
                        holder.imageView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityForResult(new Intent(CommunionThemeReplyActivity.this, CommunionSelectPictureActivity.class), REQUEST_PICK);
                            }
                        });
                    }else{
                        holder.delete.setVisibility(View.VISIBLE);
                    }
                    holder.delete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.picChecked.remove(pictureUrl.get(position));
                            if(Utils.picChecked.size() == 0){
                                reply_gridview.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                            }
                            pictureUrl.remove(position);
                            if(pictureUrl.size() == 1){
                                options = null;
                                pictureUrl = new ArrayList<String>();
                            }
                            notifyDataSetChanged();
                        }
                    });
                }else if(flag==2){
                    convertView = View.inflate(context, R.layout.communion_theme_pic, null);
                    holder = new ViewHolder1();
                    holder.imageView = (ImageView) convertView.findViewById(R.id.theme_image);
                    //点击图片放大
                    holder.imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = URLs.HTTP+URLs.HOSTA+"/INSP/COMM/" + theme.getId() +"/original/";
                            Intent intent = new Intent(context, ImagePagerCommActivity.class);
                            intent.putExtra("image_index", position);
                            intent.putExtra("url", url);
                            intent.putExtra("localurl",opicUrl);
                            intent.putExtra("id", theme.getId());
                            intent.putExtra("type","1");//1;主贴 2；回复
                            context.startActivity(intent);
                        }
                    });
                }
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder1) convertView.getTag();
                if(position == (pictureUrl.size() - 1)){
                    holder.delete.setVisibility(View.GONE);
                    holder.imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(context, CommunionSelectPictureActivity.class), REQUEST_PICK);
                        }
                    });
                }
            }
            File file=null;
            if(type==1){
                file=new File(FILE_SAVEPATH+pictureUrl.get(position));
            }else{
                file=new File(pictureUrl.get(position));

            }
            if(file.exists()) {
                ImageLoader.getInstance().displayImage("file://" +file.getPath(), holder.imageView, options);
            }else{

                final String url = URLs.HTTP + URLs.HOSTA + "/INSP/" +pictureUrl.get(position);
                //显示图片的配置
                final ViewHolder1 finalHolder = holder;
                ImageLoader.getInstance().displayImage(url, holder.imageView,options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        //开始加载
                        finalHolder.imageView.setImageResource(R.drawable.pic_loading);
                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                                                FailReason arg2) {
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                                                  Bitmap arg2) {
                        //加载成功
                        String fileName = pictureUrl.get(position).substring(pictureUrl.get(position).lastIndexOf("/") + 1);
                        Base.saveMyBitmap(fileName, arg2, picUrl);
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        //加载取消
                    }
                });
            }

            return convertView;
        }
    }
    class ViewHolder1 {
        ImageView imageView;
        Button delete;
    }

    /**
     * 异步执行任务 获取数据
     */
    private class InitializeApplicationsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            count = sympCommunicationReplyList.size();
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
            updateState(theme);
        }

        @Override
        protected Void doInBackground(String... params) {
            sympCommunicationReplyList.clear();
            List<SympCommunicationReply> lists = queryReplyList("");
            for (SympCommunicationReply reply : lists) {
                sympCommunicationReplyList.add(reply);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();
            if(sympCommunicationReplyList.size() > 0){
                replyTime = sympCommunicationReplyList.get(sympCommunicationReplyList.size()-1).getReplytime();
                no_reply.setVisibility(View.GONE);
            }else{
                no_reply.setVisibility(View.VISIBLE);
            }
//            if(Utils.isMore){
                //获取最后一个可见view的下标
                int position = listView.getLastVisiblePosition();
                if(position == (count - 1)){
                    listView.setSelection(sympCommunicationReplyList.size());
                }
//            }

            mProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            reply_count.setText("讨论信息["+sympCommunicationReplyList.size()+"]");
            super.onPostExecute(result);
        }
    }
    private  List<String> qurrySympCommunicationAccessory(){
        SympCommunicationAccessoryDao sympCommunicationAccessoryDao=new SympCommunicationAccessoryDao(CommunionThemeReplyActivity.this);
        SympCommunicationAccessory sympCommunicationAccessory=new SympCommunicationAccessory();
        sympCommunicationAccessory.setThemeid(theme.getId());
        List<SympCommunicationAccessory> list1=sympCommunicationAccessoryDao.querySympCommunicationAccessoryList(sympCommunicationAccessory);
        List<String> list=new ArrayList<>();
        for(SympCommunicationAccessory s:list1){
            list.add(s.getFileurl() + "small/" + s.getFilename());
        }
       return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.iscurrentpage=false;
        Utils.themeid="";
     handler.removeCallbacks(runnable);
    }
    /**
     * 修改状态
     */
    private void updateState(SympCommunicationTheme theme){
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Utils.isMore = false;
    }
}
