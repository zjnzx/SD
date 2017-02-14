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
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.TrFeedBackReplyAdapter;
import com.energyfuture.symphony.m3.adapter.TrFeedBackSendReplyPicAdapter;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.common.Base;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrFeedBackAccessoryDao;
import com.energyfuture.symphony.m3.dao.TrFeedBackReplyDao;
import com.energyfuture.symphony.m3.domain.SympMessageReal;
import com.energyfuture.symphony.m3.domain.TrFeedBackAccessory;
import com.energyfuture.symphony.m3.domain.TrFeedBackReply;
import com.energyfuture.symphony.m3.domain.TrFeedBackTheme;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
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

public class TrFeedBackReplyActivity extends ActionBarActivity implements OnClickListener{
    private static final int REQUEST_PICK = 0;
    public static String pathTemp = Environment.getExternalStorageDirectory().getAbsolutePath();
    private List<TrFeedBackReply> trFeedBackReplyList = new ArrayList<TrFeedBackReply>();
    private Context context=TrFeedBackReplyActivity.this;
    private TextView reply_theme_person,reply_theme_time,no_reply,reply_see_more,reply_theme_title,reply_count;
    private ImageView reply_theme_img;
    private EditText reply_input;
    private View viewItem;
    private LinearLayout reply_back,reply_pic;
    private ListView listView;
    private Toolbar toolbar;
    private GridView reply_gridview,reply_item_gridview;
    private PicGridAdapter picGridAdapter;
    private ArrayList<String> selectedPicture = new ArrayList<String>();
    private DisplayImageOptions options;
    private Button reply_send;
    private ProgressBar mProgressBar;
    private TrFeedBackReplyAdapter mAdapter;
    private TrFeedBackReplyDao trFeedBackReplyDao = new TrFeedBackReplyDao(context);
    private TrFeedBackAccessoryDao trFeedBackAccessoryDao = new TrFeedBackAccessoryDao(context);
    private String replyId;
    private List<String> urlList = new ArrayList<String>();
    private TrFeedBackSendReplyPicAdapter replyPicAdapter;
    private String picUrl,opicUrl,loginUserId,loginUserName;
    private Map<String,List<Map<String, Object>> > messageContent = new HashMap<String,List<Map<String, Object>> >();
    private List<String> dataTypeList=new ArrayList<String>();
    private List<String> dataTypeListDel=new ArrayList<String>();
    private Map<String,List<Map<String, Object>> > messageContentDel = new HashMap<String,List<Map<String, Object>> >();
    private String replyTime = "";
    private int count = 0;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TrFeedBackTheme theme;
    private Map<String, Object> condMap;
    private Handler handler1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.feedback_theme_reply);

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.pic_loading)
                .showImageForEmptyUri(R.drawable.pic_loading).showImageOnFail(R.drawable.pic_loading)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();

        initView();
        setRefresh();

    }


    @Override
    protected void onRestart() {
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
            List<TrFeedBackReply> list = queryReplyList("");
             if(list.size() > 0){
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
        condMap.put("OBJ","FEEDBACKREPLY");
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
    private List<TrFeedBackReply> queryReplyList(String replyTime){
        TrFeedBackReply trFeedBackReply = new TrFeedBackReply();
        trFeedBackReply.setReplypersonid(loginUserId);
        trFeedBackReply.setThemeid(theme.getId());
        trFeedBackReply.setReplytime(replyTime);
        List<TrFeedBackReply> replyList = trFeedBackReplyDao.queryTrFeedBackReplyList(trFeedBackReply);
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
                TrFeedBackReplyActivity.this.onBackPressed();
            }
        });

    }

    private void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        theme = (TrFeedBackTheme)bundle.get("theme");
        picUrl = Constants.FILE_SAVEPATH+"feedback/"+theme.getId()+"/small/";
        opicUrl = Constants.FILE_SAVEPATH+"feedback/"+theme.getId()+"/original/";
        loginUserId = Constants.getLoginPerson(context).get("userId");
        loginUserName = Constants.getLoginPerson(context).get("userName");

        viewItem = LayoutInflater.from(context).inflate(R.layout.feedback_reply_item, null);
        reply_item_gridview= (GridView) viewItem.findViewById(R.id.feedback_item_gridview);
        toolbar = (Toolbar) findViewById(R.id.feedback_toolbar);
        reply_theme_img = (ImageView) findViewById(R.id.feedback_theme_img);
        reply_pic = (LinearLayout) findViewById(R.id.feedback_pic);
        reply_input = (EditText) findViewById(R.id.feedback_input);
        reply_theme_person = (TextView) findViewById(R.id.feedback_theme_person);
        reply_theme_time = (TextView) findViewById(R.id.feedback_theme_time);
        reply_theme_title = (TextView) findViewById(R.id.feedback_theme_title);
        reply_count = (TextView) findViewById(R.id.feedback_count);
        reply_see_more = (TextView) findViewById(R.id.feedback_see_more);
        no_reply = (TextView) findViewById(R.id.no_feedback);
        reply_send = (Button) findViewById(R.id.feedback_send);
        reply_gridview = (GridView) findViewById(R.id.feedback_gridview);
        listView = (ListView) findViewById(R.id.feedback_listview);
        mProgressBar = (ProgressBar) findViewById(R.id.feedback_bar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.feedback_swip);

        reply_pic.setOnClickListener(this);
        reply_send.setOnClickListener(this);
        reply_see_more.setOnClickListener(this);
        String replyCount = trFeedBackReplyDao.queryTrFeedBackReplyCount(loginUserId,theme.getId());

        List<String> list = Constants.getUserNameById(theme.getCreatepersonid(),context);
        String createPersonName = "";
        if(list.size() > 0){
            createPersonName = list.get(0);
        }
        String createTime = theme.getCreatetime();
        if(!createTime.contains("-")){
            Long timestamp = Long.parseLong(theme.getCreatetime());
            createTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
        }
        reply_count.setText("讨论信息["+replyCount+"]");
        reply_theme_time.setText(createTime);
        reply_theme_img.setImageResource(R.drawable.nh);
        reply_theme_title.setText(theme.getThemetitle());
        reply_theme_person.setText(createPersonName);

        //初始化ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);

        setToolbar();
        initListView();
        initGridView();
    }

    private void initListView(){
        mAdapter =new TrFeedBackReplyAdapter(trFeedBackReplyList,context,picUrl,opicUrl,theme.getId());
        listView.setAdapter(mAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TrFeedBackReply trFeedBackReply = mAdapter.list.get(position);
                //判断权限
                if(trFeedBackReply.getReplypersonid().equals(loginUserId)){
                    deleteReply(position,trFeedBackReply);
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
    public void deleteReply(final int position,final TrFeedBackReply trFeedBackReply) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(this);
        mMaterialDialog.setTitle("确认要删除吗？").setMessage("")
                .setPositiveButton(
                        "删除", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    String id = trFeedBackReply.getId();
                                    //删除回复表
                                    trFeedBackReplyDao.deleteTrFeedBackReply(id);

                                    //查询是否有图片
                                    TrFeedBackAccessory trFeedBackAccessory = new TrFeedBackAccessory();
                                    trFeedBackAccessory.setReplyid(id);
                                    List<TrFeedBackAccessory> accList = trFeedBackAccessoryDao.queryTrFeedBackAccessoryList(trFeedBackAccessory);

                                    mAdapter.list.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    reply_count.setText("讨论信息[" + mAdapter.list.size() + "]");

                                    //发送消息
                                    sendMessageDelete(id, accList);

                                    Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                                    mMaterialDialog.dismiss();
                                } catch (Exception e) {
                                    Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                    mMaterialDialog.dismiss();
                                    Constants.recordExceptionInfo(e, context, context.toString());
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
        replyPicAdapter = new TrFeedBackSendReplyPicAdapter(context,urlList,replyId,picUrl,opicUrl,theme.getId());
        reply_item_gridview.setAdapter(replyPicAdapter);
        replyPicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.feedback_pic://添加图片
                startActivityForResult(new Intent(this, TrFeedBackSelectPictureActivity.class), REQUEST_PICK);
                break;
            case R.id.reply_see_more://查看更多
//                Utils.isMore = true;
//                new InitializeApplicationsTask().execute();
                break;
            case R.id.feedback_send: //发送回复
                if(!reply_input.getText().toString().equals("")){
                    sendReply();
                    sendMessageReply();
                    reply_input.setText("");
                    Utils.picCheckedFeedBack.clear();
                    messageContent.clear();
                    dataTypeList.clear();
                }else {
                    Toast.makeText(context,"内容不能为空!",Toast.LENGTH_SHORT).show();
                }
            break;
            default:
                break;
        }
    }

    /**
     * 发表回复
     */
    public void sendReply(){
        String date = URLs.dateformat.format(new Date());
        String inputContent = reply_input.getText().toString();
        int picCount = Utils.picCheckedFeedBack.size();

        //回复表插入数据
        insertReply(inputContent,date);

        listView.setVisibility(View.VISIBLE);
        reply_gridview.setVisibility(View.GONE);

        //附件表中插入数据
        if(picCount > 0){
            insertAccessory(date);
        }
        //回复后清空map
        if(Constants.mapUrl.size() > 0){
            Constants.mapUrl.clear();
        }

        new InitializeApplicationsTask().execute();
//        int count = trFeedBackReplyList.size();
//        if(count > 10){
//            Utils.isMore = true;
//        }
        //定位到最后一条
        listView.setSelection(trFeedBackReplyList.size());
    }

    /**
     * 回复表中插入数据
     * @param inputContent
     * @param date
     */
    private void insertReply(String inputContent,String date){
        TrFeedBackReply trFeedBackReply = new TrFeedBackReply();
        replyId = Constants.getUuid();
        trFeedBackReply.setId(replyId);
        trFeedBackReply.setThemeid(theme.getId());
        trFeedBackReply.setState("0");
        trFeedBackReply.setReplycontent(inputContent);
        trFeedBackReply.setReplypersonid(loginUserId);
        trFeedBackReply.setReplytime(date);
        trFeedBackReplyDao.insertListData(trFeedBackReply);

        //封装消息内容
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("ID", trFeedBackReply.getId());
        map.put("THEMEID", trFeedBackReply.getThemeid());
        map.put("REPLYPERSONID", trFeedBackReply.getReplypersonid());
        map.put("REPLYCONTENT", trFeedBackReply.getReplycontent());
        map.put("REPLYTIME", trFeedBackReply.getReplytime());
        map.put("STATE",trFeedBackReply.getState());
        list.add(map);
        if(list != null && list.size() > 0){
            messageContent.put("10",list);
            dataTypeList.add("10");//回复表
        }
    }

    /**
     * 附件表中插入数据
     * @param date
     */
    private void insertAccessory(String date){
        List<TrFeedBackAccessory> accList = new ArrayList<TrFeedBackAccessory>();

        urlList.clear();
        for(Map.Entry<String, String> entry: Utils.picCheckedFeedBack.entrySet()){
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
            TrFeedBackAccessory trFeedBackAccessory = new TrFeedBackAccessory();
            trFeedBackAccessory.setId(Constants.getUuid());
            trFeedBackAccessory.setThemeid(theme.getId());
            trFeedBackAccessory.setRecordtime(date);
            trFeedBackAccessory.setAccessorytype("1");
            trFeedBackAccessory.setReplyid(replyId);
            trFeedBackAccessory.setFileurl("feedback/"+theme.getId()+"/");
            trFeedBackAccessory.setFilename(entry.getValue());
            trFeedBackAccessory.setIsupload("0");
            accList.add(trFeedBackAccessory);
            trFeedBackAccessoryDao.insertListData(accList);

            urlList.add(entry.getKey());

            String url = trFeedBackAccessory.getFileurl();
            String fileUrl = url.substring(url.indexOf("feedback"),url.lastIndexOf("/")+1);
            Constants.feedBackImage(trFeedBackAccessory,fileUrl,context,Constants.getLoginPerson(context).get("userId"));
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
            Constants.recordExceptionInfo(e, context, context.toString());
        }
    }

    /**
     * 删除后向平台发送消息
     */
    private void sendMessageDelete(String id,List<TrFeedBackAccessory> accessorys){
        SympMessageReal sympMessageReal = Constants.getSympMessageReal(context, "1","");
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        //回复表
        List<Map<String,Object>> replyList = new ArrayList<Map<String,Object>>();
        Map<String,Object> replyMap = new HashMap<String,Object>();
        replyMap.put("ID", id);
        replyList.add(replyMap);
        messageContentDel.put("10", replyList);
        dataTypeListDel.add("10");
        //附件表
        if(accessorys != null && accessorys.size() > 0){
            List<Map<String,Object>> accList = new ArrayList<Map<String,Object>>();
            for(TrFeedBackAccessory acc : accessorys){
                Map<String,Object> accMap = new HashMap<String,Object>();
                accMap.put("ID",acc.getId());
                accList.add(accMap);
            }
            messageContentDel.put("11",accList);
            dataTypeListDel.add("11");

            //删除附件表
            trFeedBackAccessoryDao.deleteTrFeedBackAccessory(id);
        }

        try {
            //回复表和附件表消息
            if(messageContentDel != null && messageContentDel.size() > 0){
                sympMessageRealDao.addMessageList(sympMessageReal,dataTypeListDel,3,messageContentDel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString());
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
//        int flag;//1;回复 2；主贴   判断是那类图片
        int type;//1;保存后路径图片 2其他路径图片
        PicGridAdapter(List<String>  picurl,int flag,int type){
            this.pictureUrl=picurl;
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
                convertView = View.inflate(context, R.layout.communion_pic_del_item, null);
                holder = new ViewHolder1();
                holder.imageView = (ImageView) convertView.findViewById(R.id.create_image);
                holder.delete = (Button) convertView.findViewById(R.id.delete);
                if(position == (pictureUrl.size()-1)){
                    holder.delete.setVisibility(View.GONE);
                    holder.imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(TrFeedBackReplyActivity.this, TrFeedBackSelectPictureActivity.class), REQUEST_PICK);
                        }
                    });
                }else{
                    holder.delete.setVisibility(View.VISIBLE);
                }
                holder.delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.picCheckedFeedBack.remove(pictureUrl.get(position));
                        if(Utils.picCheckedFeedBack.size() == 0){
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
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder1) convertView.getTag();
                if(position == (pictureUrl.size() - 1)){
                    holder.delete.setVisibility(View.GONE);
                    holder.imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(TrFeedBackReplyActivity.this, TrFeedBackSelectPictureActivity.class), REQUEST_PICK);
                        }
                    });
                }
            }
            File file=null;
            if(type==1){
                file=new File(Constants.FILE_SAVEPATH+pictureUrl.get(position));
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
            count = trFeedBackReplyList.size();
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            trFeedBackReplyList.clear();
            List<TrFeedBackReply> lists = queryReplyList("");
            for (TrFeedBackReply reply : lists) {
                trFeedBackReplyList.add(reply);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();
            if(trFeedBackReplyList.size() > 0){
                replyTime = trFeedBackReplyList.get(trFeedBackReplyList.size()-1).getReplytime();
                no_reply.setVisibility(View.GONE);
            }else{
                no_reply.setVisibility(View.VISIBLE);
            }
//            if(Utils.isMore){
                //获取最后一个可见view的下标
                int position = listView.getLastVisiblePosition();
                if(position == (count - 1)){
                    listView.setSelection(trFeedBackReplyList.size());
                }
//            }

            mProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            reply_count.setText("讨论信息["+trFeedBackReplyList.size()+"]");
            super.onPostExecute(result);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
     handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.picCheckedFeedBack.clear();
//        Utils.isMore = false;
    }
}
