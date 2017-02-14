package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.analysis.ImageUtil;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.Base;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrUserInfoDao;
import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.util.Constants;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserInfoActivity extends ActionBarActivity implements View.OnClickListener{
    private String userId;
    private Context context = UserInfoActivity.this;
    private TextView username,usernameinfo;
    private ImageView usericon;
    private RelativeLayout alertname,alterpassword;
    private Button back;
    private MaterialDialog nameDialog,iconDialog;
    private TrUserInfoDao userInfoDao;
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private final String FILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/picture/";
    private String pic_route;
    private String sphotopath; //小图保存的路径
    private String ophotopath; //大图保存的路径
    private int PHOTO_REQUEST_CAMERA=1; //拍照启动Activity的请求码
    private int PHOTO_REQUEST_GALLERY=2; //启动图库的请求码
    private Handler handler;
    private List<UserInfo> userInfolist;
    private DisplayImageOptions options;
    private String oldname;
    private LinearLayout toolbar_back;
    private boolean isuploading=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);

        inioView();
        inioData();

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){  //修改名称成功后修改数据库
                    String name= (String) msg.obj;

                    Map<Object,Object> columnsMap=new HashMap<>();
                    Map<Object,Object> wheresMap=new HashMap<>();
                    columnsMap.put("XM",name);
                    wheresMap.put("YHID",userId);
                    boolean issuccess = userInfoDao.updateUserInfo(columnsMap, wheresMap);
                    if(issuccess){
                        username.setText(name);
                        usernameinfo.setText(name);
                        oldname=name;
                        Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT).show();
                    }
                }
                if(msg.what==2){ //修改名称失败
                    Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT).show();
                }
                if(msg.what==3){
                    String filename= (String) msg.obj;

                    Map<Object,Object> columnsMap=new HashMap<>();
                    Map<Object,Object> wheresMap=new HashMap<>();
                    columnsMap.put("SSXT","USER\\"+filename);
                    wheresMap.put("YHID",userId);
                    boolean issuccess = userInfoDao.updateUserInfo(columnsMap, wheresMap);
                    if(issuccess){
                        usericon.setImageURI(Uri.parse(sphotopath));
                        isuploading=false;
                        Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT).show();
                    }
                }
                if(msg.what==4){
                    Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void inioView() {
        username= (TextView) findViewById(R.id.user_name);
        usernameinfo= (TextView) findViewById(R.id.user_nameinfo);
        alertname= (RelativeLayout) findViewById(R.id.user_info_rl1);
        alterpassword= (RelativeLayout) findViewById(R.id.user_info_rl2);
        usericon= (ImageView) findViewById(R.id.user_icon);
        back= (Button) findViewById(R.id.user_info_back);
        toolbar_back= (LinearLayout) findViewById(R.id.toolbar_back);
    }

    private void inioData() {
        userInfolist=queryUserInfo();
        pic_route= FILE_SAVEPATH+"head/USER/";

        if(userInfolist.get(0).getXm()!=null&&!userInfolist.get(0).getXm().equals("null")&&!userInfolist.get(0).getXm().equals("")){
            oldname=userInfolist.get(0).getXm();
            username.setText(oldname);
            usernameinfo.setText(oldname);
        }else{
            if(Constants.getUserNameById(userId,context).size()!=0){
                username.setText(Constants.getUserNameById(userId,context).get(0));
                usernameinfo.setText(Constants.getUserNameById(userId,context).get(0));
            }else{
                username.setText("未知用户");
                usernameinfo.setText("未知用户");
                }
            }
        if(userInfolist.get(0).getSsxt()!=null&&!userInfolist.get(0).getSsxt().equals("null")&&!userInfolist.get(0).getSsxt().equals("")){
            String yhtx=userInfolist.get(0).getSsxt();
            String imagename=yhtx.substring(yhtx.lastIndexOf("\\")+1);

            File file=new File(pic_route+"small/"+imagename);
            if(file.exists()){
                usericon.setImageURI(Uri.parse(pic_route+"small/"+imagename));
            }else{
                String url  = URLs.HTTP+URLs.HOSTA+"/INSP/USER/"+imagename;
                ImageLoader.getInstance().displayImage(url,usericon);
            }
        }else{
            usericon.setImageResource(Constants.userHead(userId));
        }

        usericon.setOnClickListener(this);
        alertname.setOnClickListener(this);
        alterpassword.setOnClickListener(this);
        toolbar_back.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private List<UserInfo> queryUserInfo() {
        userId = Constants.getLoginPerson(this).get("userId");
        userInfoDao=new TrUserInfoDao(context);
        UserInfo userInfo=new UserInfo();
        userInfo.setYhid(userId);
        return userInfoDao.queryUserInfoList(userInfo);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_icon:  //修改头像
                if(isuploading){
                    Toast.makeText(context,"正在上传图片",Toast.LENGTH_SHORT).show();
                }else{
                    creatAlertIconDialog();
                }
                break;
            case R.id.user_info_rl1: //修改姓名
                creatAlertNameDialog();
                break;
            case R.id.user_info_rl2: //修改密码
                Intent intent2=new Intent(context,AlertPasswordActivity.class);
                startActivity(intent2);
                break;
            case R.id.toolbar_back: //标题栏返回键
                onBackPressed();
                break;
            case R.id.user_info_back: //返回键
                onBackPressed();
                break;
            case R.id.Album:  //相册获取
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                iconDialog.dismiss();
                break;
            case R.id.photo:  //拍照
                String name= UUID.randomUUID().toString().replaceAll("-","");
                String url = pic_route+"original/";
                sphotopath=pic_route+"small/"+name+".jpg";
                ophotopath=pic_route+"original/"+name+".jpg";
                if(!url.equals("")){
                    Base.mardDir(url);
                }
                Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                intent1.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(ophotopath)));
                startActivityForResult(intent1, PHOTO_REQUEST_CAMERA);
                iconDialog.dismiss();
                break;
            case R.id.cancel: // 修改头像的退出
                iconDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != 0) { //判断是否拍照或者选择图片
            if (requestCode == PHOTO_REQUEST_CAMERA) {  //相机
                Constants.saveFile(ophotopath,sphotopath,pic_route + "small/","1");//保存小图片
                File file = new File(sphotopath);
                try {
                    revitionImageSize(file);
                    String name=sphotopath.substring(sphotopath.lastIndexOf("/")+1);
                    Saveyhtx(file,name);
                } catch (IOException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString());
                }
            }else if(requestCode == PHOTO_REQUEST_GALLERY){
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(data.getData(), proj, null, null, null);

                    int column_path = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(column_path);
                    String name= UUID.randomUUID().toString().replaceAll("-","");

                    sphotopath=pic_route+"small/"+name+".jpg";
                    Constants.saveFile(path,sphotopath,pic_route + "small/","1");//保存小图片
                    File file = new File(sphotopath);
                    try {
                        revitionImageSize(file);
                        Saveyhtx(file,name+".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Constants.recordExceptionInfo(e, context, context.toString());
                    }
            }
        }else{}
    }

    private void Saveyhtx(final File file,final String filename) {  //将图片转换为二进制保存到数据库
        String imageByte = null;
        try {
            imageByte = ImageUtil.image2String(file);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString());
        }
        if(imageByte!=null){
            final Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("FILEINFO", imageByte);
            dataMap.put("FILENAME",filename);
            dataMap.put("YHID",userId);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isuploading=true;
                    String http = URLs.HTTP+URLs.HOST+"/"+"inspectfuture/" +URLs.UPDATEUSERICON;
                    Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");

                    if(resultMap != null && resultMap.get("ISTRUE").equals("1")){
                        Message msg = new Message();
                        msg.obj = filename;
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }else{
                        handler.sendEmptyMessage(4); //修改失败
                    }
                }
            }).start();
        }
    }

    private void creatAlertNameDialog() {
        nameDialog = new MaterialDialog(context);
        nameDialog.setView(getnameView()).setCanceledOnTouchOutside(true).show();
    }

    private void creatAlertIconDialog() {
        iconDialog= new MaterialDialog(context);
        iconDialog.setView(geticonView()).setCanceledOnTouchOutside(true).show();
    }

    private View getnameView() {
        View v= LayoutInflater.from(context).inflate(R.layout.user_info_alertname,null);
        final EditText alert_name= (EditText) v.findViewById(R.id.alert_name);
        alert_name.setText(oldname);
        Button cancel= (Button) v.findViewById(R.id.cancel);
        Button confirm= (Button) v.findViewById(R.id.confirm);

        Constants.showSoftKeyboard(alert_name);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname=alert_name.getText().toString();
                if(TextUtils.isEmpty(newname)){
                    Toast.makeText(context,"修改的姓名不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    if(!newname.equals(oldname)){
                        alertName(newname);
                        nameDialog.dismiss();
                    }else{
                        nameDialog.dismiss();
                    }
                }
            }
        });
        return v;
    }

    private View geticonView() {
        View v= LayoutInflater.from(context).inflate(R.layout.user_info_alerticon,null);

        TextView Album= (TextView) v.findViewById(R.id.Album);
        TextView photo= (TextView) v.findViewById(R.id.photo);
        TextView cancel= (TextView) v.findViewById(R.id.cancel);

        Album.setOnClickListener(this);
        photo.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return v;
    }

    private void alertName(final String newname) {  //修改昵称
        final Map<String,Object> dataMap = new HashMap<String,Object>();

        dataMap.put("XM",newname);
        dataMap.put("YHID",userId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String http = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.UPDATEUSER;
                Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");

                if(resultMap != null && resultMap.get("ISTRUE").equals("1")){
                    Message msg = new Message();
                    msg.obj = newname;
                    msg.what = 1;
                    handler.sendMessage(msg);
                }else{
                    handler.sendEmptyMessage(2); //修改失败
                }
            }
        }).start();
    }
    public static Bitmap revitionImageSize(File file) throws IOException {
        int size = 1000;
        // 取得图片
        InputStream temp = new FileInputStream(file);
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
        options.inJustDecodeBounds = true;
        // 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
        BitmapFactory.decodeStream(temp, null, options);
        // 关闭流
        temp.close();

        // 生成压缩的图片
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            // 这一步是根据要设置的大小，使宽和高都能满足
            if ((options.outWidth >> i <= size)
                    && (options.outHeight >> i <= size)) {
                // 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
                temp = new FileInputStream(file);
                // 这个参数表示 新生成的图片为原始图片的几分之一。
                options.inSampleSize = (int) Math.pow(2.0D, i);
                // 这里之前设置为了true，所以要改为false，否则就创建不出图片
                options.inJustDecodeBounds = false;

                bitmap = BitmapFactory.decodeStream(temp, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }
}
