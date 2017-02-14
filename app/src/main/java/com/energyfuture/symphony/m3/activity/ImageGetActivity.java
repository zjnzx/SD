package com.energyfuture.symphony.m3.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.ImageGetAdapter;
import com.energyfuture.symphony.m3.common.GetPicFromWifiSDScard;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletFileObjDao;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;

public class ImageGetActivity extends ActionBarActivity {
    private Button back,input;
    private GridView imageshow;
    private TextView inputcount;
    private TrTask trTask;
    private Context context=ImageGetActivity.this;
    private GetPicFromWifiSDScard getPicFromWifiSDScard;
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private final String FILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/picture/";
    private String pic_route;
    private ImageGetAdapter adapter;
    private ProgressDialog progressDialog;
    private TrDetectionTempletFileObjDao fileObjDao=new TrDetectionTempletFileObjDao(context);
    private int totalcount=Constants.imagelist.size();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_get);
        trTask= (TrTask) getIntent().getExtras().get("TrTask");

        SharedPreferences M3_SETTNG = context.getSharedPreferences("M3_SETTNG", context.getApplicationContext().MODE_PRIVATE);
        String sd_route  = M3_SETTNG.getString("rh_sd_route","");//sd卡路径
        String file_prefix  = M3_SETTNG.getString("rh_file_prefix","");//前缀
        String file_postfix  = M3_SETTNG.getString("rh_file_postfix","");//后缀
        pic_route=FILE_SAVEPATH+trTask.getProjectid()+"/"+trTask.getTaskid()+"/";
        getPicFromWifiSDScard=new GetPicFromWifiSDScard(context,sd_route,file_prefix,file_postfix,pic_route,trTask);

        inioView();
        inioData();

        Constants.getImageHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){  //图片导入完成
                    Toast.makeText(context,"图片导入完成",Toast.LENGTH_SHORT).show();
                    input.setText("继续导入");
                    input.setEnabled(true);
                    inputcount.setText("数量："+Constants.successcount+" (已完成) / "+totalcount + " (总数)");
                    progressDialog.dismiss();

                    Constants.flag=true;  //重新导入图片
                    //Constants.successcount=0;
                    //adapter.setdata();
                    adapter.notifyDataSetChanged();
                }else if(msg.what==2){  //更新成功导入数量
                    progressDialog.setProgress(Constants.successcount);
                }
            }
        };
    }


    private void inioView() {
        back= (Button) findViewById(R.id.image_get_back);
        input= (Button) findViewById(R.id.image_get_input);
        inputcount= (TextView) findViewById(R.id.image_get_inputcount);
        imageshow= (GridView) findViewById(R.id.image_get_show);
    }

    private void inioData() {
        inputcount.setText("数量："+Constants.successcount+" (已完成) / "+totalcount + " (总数)");
        adapter=new ImageGetAdapter(ImageGetActivity.this,trTask,pic_route);
        imageshow.setAdapter(adapter);

        input.setOnTouchListener(new View.OnTouchListener() {
            int touch_flag=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                input.setFocusable(true);
                input.setFocusableInTouchMode(true);
                input.requestFocus();
                touch_flag++;
                if(touch_flag==2){
                    if(Constants.flag){
                        Constants.imagelist=fileObjDao.getnoImageValue(trTask.getTaskid());
                    }
                    if(Constants.imagelist.size()!=0){

                        input.setEnabled(false);
                        input.setText("导入中...");

                        progressDialog = new ProgressDialog(ImageGetActivity.this);
                        progressDialog.setIcon(R.drawable.green);
                        //获取当前项目名称
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("正在导入图片");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setMax(Constants.imagelist.size());
                        progressDialog.show();

                        getPicFromWifiSDScard.getImage(Constants.imagelist);
                    }else{
                        Toast.makeText(context,"暂无图片可导入",Toast.LENGTH_SHORT).show();
                    }
                    touch_flag=0;
                }
                return false;
            }
        });
        /*input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.flag){
                    Constants.imagelist=fileObjDao.getnoImageValue(trTask.getTaskid());
                }
                if(Constants.imagelist.size()!=0){
                    *//*if(Constants.datamap.size()!=0){
                        Constants.datamap.clear();
                    }*//*
                    input.setFocusable(true);
                    input.setFocusableInTouchMode(true);
                    input.requestFocus();

                    input.setEnabled(false);
                    input.setText("导入中...");

                    progressDialog = new ProgressDialog(ImageGetActivity.this);
                    progressDialog.setIcon(R.drawable.green);
                    //获取当前项目名称
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("正在导入图片");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMax(Constants.imagelist.size());
                    progressDialog.show();

                    getPicFromWifiSDScard.getImage(Constants.imagelist);
                }else{
                    Toast.makeText(context,"暂无图片可导入",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();

        Constants.imagelist.clear();
        Constants.successcount=0;
        Constants.flag=false;
        Utils.number="";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_get, menu);
        return true;
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
