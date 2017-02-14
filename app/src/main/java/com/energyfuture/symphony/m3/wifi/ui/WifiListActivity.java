package com.energyfuture.symphony.m3.wifi.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.ImageGetActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.activity.SettingImageActivity;
import com.energyfuture.symphony.m3.common.GetPicFromWifiSDScard;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletFileObjDao;
import com.energyfuture.symphony.m3.dao.TrParameterDao;
import com.energyfuture.symphony.m3.domain.TrParameter;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Network;
import com.energyfuture.symphony.m3.wifi.adapter.MyListViewAdapter;
import com.energyfuture.symphony.m3.wifi.customview.MyListView;
import com.energyfuture.symphony.m3.wifi.util.WifiAdmin;
import com.energyfuture.symphony.m3.wifi.util.WifiAdmin2;
import com.energyfuture.symphony.m3.wifi.util.WifiConnect;
import com.energyfuture.symphony.m3.wifi.view.OnNetworkChangeListener;
import com.energyfuture.symphony.m3.wifi.view.WifiConnDialog;
import com.energyfuture.symphony.m3.wifi.view.WifiStatusDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class WifiListActivity extends Activity {
    protected static final String TAG = WifiListActivity.class.getSimpleName();

    private static final int REFRESH_CONN = 100;
    private SharedPreferences sharedPreferences;
    private static final int REQ_SET_WIFI = 200;

    // Wifi管理类
    private WifiAdmin mWifiAdmin;
    // 扫描结果列表
    private List<ScanResult> list = new ArrayList<ScanResult>();
    // 显示列表
    private MyListView listView;
//  private ToggleButton tgbWifiSwitch;

    private MyListViewAdapter mAdapter;
    Button  bulk_import;
    TextView import_count;
    String wifi_item;
    String wifi_name ,count;
    ScanResult scanResult;
    TrTask trTask;
    private int type;
    private TextView none_data;
    WifiAdmin2 wifiAdmin2;
    private WifiManager wifiManager;
    private Spinner p_spinner;
    private int position;
    private ArrayAdapter adapter;
    private List<TrParameter> trParameters;
    private SharedPreferences M3_SETTNG;
    private List<String> parameterName = new ArrayList<String>();
    private GetPicFromWifiSDScard getPicFromWifiSDScard;
    private Context context=WifiListActivity.this;
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private final String FILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/picture/";
    private TrDetectionTempletFileObjDao fileObjDao=new TrDetectionTempletFileObjDao(context);
    private TrParameterDao trParameterDao = new TrParameterDao(context);
    private ProgressDialog progressDialog;
    private LinearLayout input_count;
    private List<String> imagelist=new ArrayList<>();
    private OnNetworkChangeListener mOnNetworkChangeListener = new OnNetworkChangeListener() {
        @Override
        public void onNetWorkDisConnect() {
            getWifiListInfo();
            mAdapter.setDatas(list);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNetWorkConnect() {
            getWifiListInfo();
            mAdapter.setDatas(list);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Constants.recordExceptionInfo(e, context, context.toString());
            }
            mAdapter.notifyDataSetChanged();
        }
    };


    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);

        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.TOP|Gravity.RIGHT;

        params.x=0;
        params.y=100;
        getWindow().setAttributes(params);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        count= bundle.get("count")!=null?bundle.get("count").toString():"0";
        trTask= (TrTask) bundle.get("TrTask");
        type = (int)bundle.get("flag");

        Constants.imagelist=fileObjDao.getnoImageValue(trTask.getTaskid());

        if(Constants.wifiid.equals("")&&Network.isWifiConnected(context)&&Network.isconnectFlashairWifi(context)){
            Constants.wifiid=Network.getWifiId(context);
        }

        M3_SETTNG = context.getSharedPreferences("M3_SETTNG", context.getApplicationContext().MODE_PRIVATE);
        position = M3_SETTNG.getInt("Position",0);

        none_data = (TextView) this.findViewById(R.id.none_data);
        p_spinner = (Spinner) this.findViewById(R.id.p_spinner);
        listView = (MyListView) findViewById(R.id.freelook_listview);
        sharedPreferences = this.getSharedPreferences("wifi", Context.MODE_PRIVATE);
        Wifiadmin();
        wifiAdmin2.OpenWiFi(WifiListActivity.this);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.putString("count",count);
        editor1.commit();


        initData();
        initView();
        setListener();

        refreshWifiStatusOnTime();

        progressDialog = new ProgressDialog(WifiListActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIcon(R.drawable.green);
        //获取当前项目名称
        progressDialog.setCancelable(false);
        progressDialog.setMessage("上传图片");

    }
    private void Wifiadmin() {
        wifiAdmin2 = new WifiAdmin2(WifiListActivity.this);
		/* 以getSystemService取得WIFI_SERVICE */
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
    }

    private void getData(){
        parameterName.clear();
        TrParameter trParameter = new TrParameter();
        trParameter.setParametertype(type + "");
        trParameters = trParameterDao.queryTrParameterList(trParameter);
        for(int i = 0;i < trParameters.size() ; i++){
            parameterName.add(i,trParameters.get(i).getCompany() + " " + trParameters.get(i).getModel());
        }
        if(parameterName.size() <= 0){
            parameterName.add(0,"无");
        }
        parameterName.add(parameterName.size(),"                管理");
    }

    private void initData() {
        getData();
        adapter = new ArrayAdapter(this,R.layout.spinner, parameterName){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if(convertView==null){
                    convertView = getLayoutInflater().inflate(R.layout.p_spinner_item, parent, false);
                }
                TextView label = (TextView) convertView.findViewById(R.id.label);
                label.setText(getItem(position).toString());
                return convertView;
            }
        };
        p_spinner.setAdapter(adapter);

        p_spinner.setSelection(position);

        p_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parameterName.get(position).equals("                管理")){
                    Intent intent = new Intent(WifiListActivity.this, SettingImageActivity.class);
                    startActivity(intent);
                    p_spinner.setSelection(0);
                }else if(!parameterName.get(position).equals("无")){
                    SharedPreferences.Editor editor = M3_SETTNG.edit();
                    editor.putInt("Position",position);
                    editor.commit();
                    String sd_route = trParameters.get(position).getSdfileurl();
                    String file_prefix = trParameters.get(position).getFileprefix();
                    String file_postfix = trParameters.get(position).getFilesuffix();
                    String pic_route=FILE_SAVEPATH+trTask.getProjectid()+"/"+trTask.getTaskid()+"/";

                    editor.putString("rh_sd_route",sd_route);
                    editor.putString("rh_file_prefix",file_prefix);
                    editor.putString("rh_file_postfix",file_postfix);
                    editor.commit();

                    getPicFromWifiSDScard=new GetPicFromWifiSDScard(context,sd_route,file_prefix,file_postfix,pic_route,trTask);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mWifiAdmin = new WifiAdmin(WifiListActivity.this);
        // 获得Wifi列表信息
        getWifiListInfo();
    }

    private void initView() {
        mAdapter = new MyListViewAdapter(this, list);
        listView.setAdapter(mAdapter);

        input_count= (LinearLayout) findViewById(R.id.input_count);
        bulk_import= (Button) findViewById(R.id.bulk_import);
        import_count= (TextView) findViewById(R.id.count);

        import_count.setText(Constants.successcount+"/" + Constants.imagelist.size());

        if(getPicFromWifiSDScard.isTheWifiContected(context) && !parameterName.get(0).equals("无")){
            inioImport();
        }

        bulk_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(getPicFromWifiSDScard.isTheWifiContected(context)){
                        Constants.datamap.clear();
                        Intent intent=new Intent(WifiListActivity.this, ImageGetActivity.class);
                        intent.putExtra("TrTask",trTask);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(context,"请连接SD卡网络",Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void setListener() {

//        tgbWifiSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//                    Log.w(TAG, "======== open wifi ========");
//                    // 打开Wifi
//                    mWifiAdmin.openWifi();
//                } else {
//                    Log.w(TAG, "======== close wifi ========");
////					// 关闭Wifi
////					boolean res = mWifiAdmin.closeWifi();
////					if (!res) {
////						gotoSysCloseWifi();
////					}
//                    mWifiAdmin.closeWifi();
//                }
//            }
//        });

        // 设置刷新监听
        listView.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        getWifiListInfo();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mAdapter.setDatas(list);
                        mAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                        if(list==null||list.size()<=0)
                        {
                            none_data.setVisibility(View.VISIBLE);

                        }
                        else {
                            none_data.setVisibility(View.GONE);
                        }
                    }

                }.execute();
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,long id) {
                WifiAdmin mWifiAdmin = new WifiAdmin(context);
                if(!Network.isconnectFlashairWifi(context)){
                    if(!Constants.wifiid.equals("")){
                        Network.closeWIFI(context);
                    }else{
                        String networkid=Network.getWifiId(context);
                        mWifiAdmin.disConnectionWifi(Integer.parseInt(networkid));

                        mAdapter.setDatas(list);
                        mAdapter.notifyDataSetChanged();
                    }
                }else{
                    int position = pos - 1;
                    // String wifiName = list.get(position).SSID;
                    // String singlStrength = "" + list.get(position).frequency;
                    // String secuString = list.get(position).capabilities;
                    //view.setBackgroundResource(R.color.card_shadow);
                    scanResult = list.get(position);

                    wifi_item=scanResult.toString();
                    wifi_name=scanResult.SSID;

                    Toast.makeText(context,"WIFI连接中...",Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("wifi_name",wifi_name);
                    editor.putString("type",scanResult.capabilities.toUpperCase());
                    editor.commit();
                    WifiConnect.WifiCipherType type = null;
                    if (scanResult.capabilities.toUpperCase().contains("WPA")) {
                        type = WifiConnect.WifiCipherType.WIFICIPHER_WPA;
                    } else if (scanResult.capabilities.toUpperCase()
                            .contains("WEP")) {
                        type = WifiConnect.WifiCipherType.WIFICIPHER_WEP;
                    } else {
                        type = WifiConnect.WifiCipherType.WIFICIPHER_NOPASS;
                    }
                    // 连接网络
                    boolean bRet = mWifiAdmin.connect(scanResult.SSID, "12345678", type);
                    if (bRet) {
                        Toast.makeText(context,"连接成功",Toast.LENGTH_SHORT).show();
                    } else {
                        wifiAdmin2.CloseWiFi(WifiListActivity.this);
                        Toast.makeText(WifiListActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                }
//                String desc = "";
//                String descOri = scanResult.capabilities;
//                if (descOri.toUpperCase().contains("WPA-PSK")) {
//                    desc = "WPA";
//                }
//                if (descOri.toUpperCase().contains("WPA2-PSK")) {
//                    desc = "WPA2";
//                }
//                if (descOri.toUpperCase().contains("WPA-PSK")
//                        && descOri.toUpperCase().contains("WPA2-PSK")) {
//                    desc = "WPA/WPA2";
//                }
//
//                if (desc.equals("")) {
//                    isConnectSelf(scanResult);
//                    return;
//                }
//                isConnect(scanResult);
            }

            private void isConnect(ScanResult scanResult) {
                if (mWifiAdmin.isConnect(scanResult)) {
                    // 已连接，显示连接状态对话框
                    WifiStatusDialog mStatusDialog = new WifiStatusDialog(
                            WifiListActivity.this, R.style.PopDialog,
                            scanResult, mOnNetworkChangeListener);
                    mStatusDialog.show();
                } else {
                    // 未连接显示连接输入对话框
                    WifiConnDialog mDialog = new WifiConnDialog(
                            WifiListActivity.this, R.style.PopDialog,
                            scanResult, mOnNetworkChangeListener);
                    // WifiConnDialog mDialog = new WifiConnDialog(
                    // WifiListActivity.this, R.style.PopDialog, wifiName,
                    // singlStrength, secuString);
                    mDialog.show();
                }
            }

            private void isConnectSelf(ScanResult scanResult) {
                if (mWifiAdmin.isConnect(scanResult)) {

                    // 已连接，显示连接状态对话框
                    WifiStatusDialog mStatusDialog = new WifiStatusDialog(
                            WifiListActivity.this, R.style.PopDialog,
                            scanResult, mOnNetworkChangeListener);
                    mStatusDialog.show();

                } else {
                    boolean iswifi = mWifiAdmin.connectSpecificAP(scanResult);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (iswifi) {
                        Toast.makeText(WifiListActivity.this, "连接成功！",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(WifiListActivity.this, "连接失败！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getWifiListInfo() {
        System.out.println("WifiListActivity#getWifiListInfo");
        mWifiAdmin.startScan();
        List<ScanResult> tmpList = mWifiAdmin.getWifiList();
        if (tmpList == null) {
            list.clear();

        } else {
            list.clear();
            for(ScanResult scanResult :tmpList)
            {
                if(scanResult.SSID.toLowerCase().contains("flashair")||scanResult.SSID.toLowerCase().contains("efwifisd"))
                {
                    list.add(scanResult);
                }
            }

//            list = tmpList;
        }

//        if(list==null||list.size()<=0)
//        {
//            none_data.setVisibility(View.VISIBLE);
//
//        }
//        else {
//            none_data.setVisibility(View.GONE);
//        }


    }

    private Handler mHandler = new MyHandler(this);

    protected boolean isUpdate = true;

    private static class MyHandler extends Handler {

        private WeakReference<WifiListActivity> reference;

        public MyHandler(WifiListActivity activity) {
            this.reference = new WeakReference<WifiListActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            WifiListActivity activity = reference.get();

            switch (msg.what) {
                case REFRESH_CONN:
                    activity.getWifiListInfo();
                    activity.mAdapter.setDatas(activity.list);
                    activity.mAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }
    }

    /**
     * Function:定时刷新Wifi列表信息<br>
     *
     * @author ZYT DateTime 2014-5-15 上午9:14:34<br>
     * <br>
     */
    private void refreshWifiStatusOnTime() {
        new Thread() {
            public void run() {
                while (isUpdate) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Constants.recordExceptionInfo(e, context, context.toString());
                    }
                    mHandler.sendEmptyMessage(REFRESH_CONN);
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isUpdate = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
        adapter.notifyDataSetChanged();
    }

    /**
     * Function:到系统中设置wifi，如果用户手动关闭失败，跳转到系统中进行关闭Wifi<br>
     *
     * @author ZYT DateTime 2014-5-15 上午10:03:15<br>
     * <br>
     */
    private void gotoSysCloseWifi() {
        // 05-15 09:57:56.351: I/ActivityManager(397): START
        // {act=android.settings.WIFI_SETTINGS flg=0x14000000
        // cmp=com.android.settings/.Settings$WifiSettingsActivity} from pid 572

        Intent intent = new Intent("android.settings.WIFI_SETTINGS");
        intent.setComponent(new ComponentName("com.android.settings",
                "com.android.settings.Settings$WifiSettingsActivity"));
        startActivityForResult(intent, REQ_SET_WIFI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_SET_WIFI:
                // 处理改变wifi状态结果
                //
                int wifiState = mWifiAdmin.checkState();
//                if (wifiState == WifiManager.WIFI_STATE_DISABLED
//                        || wifiState == WifiManager.WIFI_STATE_DISABLING
//                        || wifiState == WifiManager.WIFI_STATE_UNKNOWN) {
//                    tgbWifiSwitch.setChecked(false);
//                } else {
//                    tgbWifiSwitch.setChecked(true);
//                }
                break;

            default:
                break;
        }
    }
    public void inioImport(){
        if(!parameterName.get(0).equals("无")){
            input_count.setVisibility(View.VISIBLE);
            bulk_import.setBackgroundColor(Color.parseColor("#00C55B"));
            bulk_import.setTextColor(Color.parseColor("#ffffff"));
            bulk_import.setEnabled(true);
        }
    }

    public void restoreImpotr(){
        input_count.setVisibility(View.GONE);
        bulk_import.setBackgroundColor(Color.parseColor("#E1E1E1"));
        bulk_import.setTextColor(Color.parseColor("#BDBDBD"));
        bulk_import.setEnabled(false);
    }
}
