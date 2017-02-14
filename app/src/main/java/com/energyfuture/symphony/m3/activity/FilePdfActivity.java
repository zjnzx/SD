package com.energyfuture.symphony.m3.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrDataSynchronizationDao;
import com.energyfuture.symphony.m3.domain.TrDataSynchronization;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.IconifiedText;
import com.energyfuture.symphony.m3.util.IconifiedTextListAdapter;
import com.energyfuture.symphony.m3.util.UplodaFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilePdfActivity extends Activity {
    private Context context = FilePdfActivity.this;
    private List<IconifiedText> datalist = new ArrayList<IconifiedText>();
    private List<String> paths = new ArrayList<String>();
    private File currentDirectory = new File("/");
    private ProgressDialog progressDialog;
    private IconifiedTextListAdapter itla;
    private ListView listview;
    private Button check_bt;
    private String max = "";
    private TextView filepath;
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private final String FILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/";
    private LinearLayout menu_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_pdf);
        itla = new IconifiedTextListAdapter(this);
        menu_back = (LinearLayout)findViewById(R.id.menu_back);
        check_bt = (Button)findViewById(R.id.check_bt);
        listview = (ListView)findViewById(R.id.list);
        filepath = (TextView)findViewById(R.id.filepath);
        File file = new File(FILE_SAVEPATH + "file/FILE/");
        if (!file.exists()) {
            file.mkdirs();
        }
        browseToRoot();
//        this.setSelection(0);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFileString = datalist.get(position)
                        .getText();

                if (selectedFileString.equals(getString(R.string.current_dir))) {
                    // 如果选中的是刷新
                    browseTo(currentDirectory);
                } else if (selectedFileString.equals(getString(R.string.up_one_level))) {
                    // 返回上一级目录
                    upOneLevel();
                } else {

                    File clickedFile = null;
                    clickedFile = new File(currentDirectory.getAbsolutePath()
                            + datalist.get(position).getText());
                    if (clickedFile != null)
                        browseTo(clickedFile);
                }
            }
        });
        menuClick();
    }

    final Handler cwjHandler = new Handler();

    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            fill1(new File(FILE_SAVEPATH + "file/FILE/").listFiles());
        }
    };

    final Runnable noFile = new Runnable() {
        public void run() {
            Toast.makeText(FilePdfActivity.this,"暂无需要更新的文件",Toast.LENGTH_SHORT).show();
        }
    };

    final Runnable updateFile = new Runnable() {
        public void run() {
            Toast.makeText(FilePdfActivity.this,"文件更新完成",Toast.LENGTH_SHORT).show();
        }
    };

    // 浏览文件系统的指定目录
    private void browseToRoot() {
        browseTo(new File(FILE_SAVEPATH + "file/FILE/"));
    }

    // 返回上一级目录
    private void upOneLevel() {
        if (this.currentDirectory.getParent() != null)
            this.browseTo(this.currentDirectory.getParentFile());
    }

    private void menuClick() {
        menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File tmp=new File(currentDirectory.getAbsolutePath());
                //equals（）字符比较，==内存比较
                if (tmp.getAbsolutePath().toString().equals((FILE_SAVEPATH.toString() + "file/FILE"))){
                    FilePdfActivity.this.finish();
                }else{
                    upOneLevel();
                }
                return;
            }
        });
        check_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FilePdfActivity.this,"正在更新文件...",Toast.LENGTH_SHORT).show();
                TrDataSynchronizationDao trDataSynchronizationDao = new TrDataSynchronizationDao(context);
                max = trDataSynchronizationDao.queryTrDataSynchronizationMax();
                Map<String,Object> condMap1 = new HashMap<String, Object>();
                condMap1.put("OBJ","LEDGER");
                condMap1.put("OPELIST",null);
                condMap1.put("TYPE",null);
                condMap1.put("SIZE",5);
                if(max == null){
                    condMap1.put("MAX","");
                }else{
                    condMap1.put("MAX",max);
                }
                condMap1.put("USERID", Constants.getLoginPerson(context).get("userId"));
                new DataSyschronized(FilePdfActivity.this).getDataFromWeb(condMap1, handler1);
            }
        });
    }

    final Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1 && msg.obj != null){
                final Map<String,Object> resMap = (Map<String,Object>)msg.obj;
                if("ok".equals(resMap.get("0"))) {
                    new Thread(){
                        @Override
                        public void run() {
                            List<TrDataSynchronization> syncData  = (ArrayList) resMap.get("data");
                            if(syncData.size()>0){
                                int i = 0;
                                for(TrDataSynchronization sys : syncData){
                                        i++;
                                        if(sys.getOpertype() != null && sys.getOpertype().equals("1")){
                                            String url = "";
                                            String fileUrl = sys.getFileurl().replaceAll("\\\\","/");
                                            sys.setFileurl(fileUrl);
                                            File file = new File(FILE_SAVEPATH + "file/" + sys.getFileurl() + "/" + sys.getFilename());
                                            if(!file.exists()){
                                                try {
//                                            url2 = URLs.HTTP+ URLs.HOST+"/"+"INSP/"+ sys.getFileurl()+"/"+ sys.getFilename();
                                                    url = URLs.HTTP+ URLs.HOST+"/"+"INSP/"+ URLEncoder.encode(sys.getFileurl(), "UTF-8").replaceAll("%2F","/")+"/"+ URLEncoder.encode(sys.getFilename(), "UTF-8");
                                                    UplodaFile.downFile(url, sys);
                                                    cwjHandler.post(mUpdateResults);
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                    Constants.recordExceptionInfo(e,context,context.toString());
                                                }
                                            }
                                        }else if(sys.getOpertype() != null && sys.getOpertype().equals("3")){
                                            File file = new File(FILE_SAVEPATH + "file/" + sys.getFileurl() + "/" + sys.getFilename());
                                            UplodaFile.deleteFile(file);
                                        }
                                    }
                                cwjHandler.post(updateFile);
                                }else{
                                cwjHandler.post(noFile);
                            }
                        }
                    }.start();

                }else{
                    Toast.makeText(FilePdfActivity.this,"暂无需要更新的文件",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(FilePdfActivity.this, "暂无需要更新的文件", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 浏览指定的目录,如果是文件则进行打开操作
    private void browseTo(final File file) {
//        this.setTitle(file.getAbsolutePath());
        if (file.isDirectory()) {
            this.currentDirectory = file;
            fill(file.listFiles());
        }else {
            String string_path=file.getAbsolutePath();
            File fileUrl = new File(string_path);
            Constants.openFile(context,fileUrl);
        }
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return 文件后缀名
     */
//    public static String getFileType(String fileName) {
//        if (fileName != null) {
//            int typeIndex = fileName.lastIndexOf(".");
//            if (typeIndex != -1) {
//                String fileType = fileName.substring(typeIndex + 1)
//                        .toLowerCase();
//                return fileType;
//            }
//        }
//        return "";
//    }

    /**
     * 根据后缀名判断是否是图片文件
     *
     * @param
     * @return 是否是图片结果true or false
     */
//    public static boolean isImage(String type) {
//        if (type != null
//                && (type.equals("jpg") || type.equals("gif")
//                || type.equals("png") || type.equals("jpeg")
//                || type.equals("bmp") || type.equals("wbmp")
//                || type.equals("ico") || type.equals("jpe"))) {
//            return true;
//        }
//        return false;
//    }
//    public static boolean isPDF(String type) {
//        if (type != null && (type.equals("pdf"))) {
//            return true;
//        }
//        return false;
//    }

    // 这里可以理解为设置ListActivity的源
    private void fill(File[] files) {
        // 清空列表
        this.datalist.clear();
        int startStr = 0;
        int endStr = 0;
        String path = "/";
        Drawable currentIcon = null;
        for (File currentFile : files) {
            // 判断是一个文件夹还是一个文件
            if (currentFile.isDirectory()) {
                currentIcon = getResources().getDrawable(R.drawable.folder);
            } else {
                // 取得文件名
                String fileName = currentFile.getName();
                // 根据文件名来判断文件类型，设置不同的图标
                if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingImage))) {
                    currentIcon = getResources().getDrawable(R.drawable.image);
                } else if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingWebText))) {
                    currentIcon = getResources()
                            .getDrawable(R.drawable.webtext);
                } else if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingPackage))) {
                    currentIcon = getResources().getDrawable(R.drawable.packed);
                }  else {
                    currentIcon = getResources().getDrawable(R.drawable.text);
                }
            }
            // 确保只显示文件名、不显示路径如：/sdcard/111.txt就只是显示111.txt
            int currentPathStringLenght = this.currentDirectory.getAbsolutePath().length();
            String filename = currentFile.getAbsolutePath().substring(currentPathStringLenght);
            this.datalist.add(new IconifiedText(filename,currentIcon));
            startStr = currentFile.getAbsolutePath().indexOf("FILE/") + 4;
            endStr = currentFile.getAbsolutePath().indexOf(filename);
            path = currentFile.getAbsolutePath().substring(startStr,endStr);
        }
        if(endStr - startStr > 0){
            filepath.setText("路径：" + path);
        }else{
            filepath.setText("路径：/");
        }
        Collections.sort(this.datalist);
        // 将表设置到ListAdapter中
        itla.setListItems(this.datalist);
        // 为ListActivity添加一个ListAdapter
        listview.setAdapter(itla);
    }

    // 这里可以理解为设置ListActivity的源
    private void fill1(File[] files) {
        // 清空列表
        this.datalist.clear();
        int startStr = 0;
        int endStr = 0;
        String path = "/";
        Drawable currentIcon = null;
        for (File currentFile : files) {
            // 判断是一个文件夹还是一个文件
            if (currentFile.isDirectory()) {
                currentIcon = getResources().getDrawable(R.drawable.folder);
            } else {
                // 取得文件名
                String fileName = currentFile.getName();
                // 根据文件名来判断文件类型，设置不同的图标
                if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingImage))) {
                    currentIcon = getResources().getDrawable(R.drawable.image);
                } else if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingWebText))) {
                    currentIcon = getResources()
                            .getDrawable(R.drawable.webtext);
                } else if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingPackage))) {
                    currentIcon = getResources().getDrawable(R.drawable.packed);
                } else {
                    currentIcon = getResources().getDrawable(R.drawable.text);
                }
            }
            // 确保只显示文件名、不显示路径如：/sdcard/111.txt就只是显示111.txt
            int currentPathStringLenght = this.currentDirectory
                    .getAbsolutePath().length();
            String filename = currentFile.getAbsolutePath().substring(currentPathStringLenght);
            this.datalist.add(new IconifiedText(filename,currentIcon));
            startStr = currentFile.getAbsolutePath().indexOf("FILE/") + 4;
            endStr = currentFile.getAbsolutePath().indexOf(filename);
            path = currentFile.getAbsolutePath().substring(startStr,endStr);
        }
        if(endStr - startStr > 0){
            filepath.setText("路径：" + path);
        }else{
            filepath.setText("路径：/");
        }
        Collections.sort(this.datalist);
        // 将表设置到ListAdapter中
        itla.setListItems(this.datalist);

        itla.notifyDataSetChanged();
    }

//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        // 取得选中的一项的文件名
//        String selectedFileString = this.datalist.get(position)
//                .getText();
//
//        if (selectedFileString.equals(getString(R.string.current_dir))) {
//            // 如果选中的是刷新
//            this.browseTo(this.currentDirectory);
//        } else if (selectedFileString.equals(getString(R.string.up_one_level))) {
//            // 返回上一级目录
//            this.upOneLevel();
//        } else {
//
//            File clickedFile = null;
//            clickedFile = new File(this.currentDirectory.getAbsolutePath()
//                    + this.datalist.get(position).getText());
//            if (clickedFile != null)
//                this.browseTo(clickedFile);
//        }
//    }

    // 通过文件名判断是什么类型的文件
    private boolean checkEndsWithInStringArray(String checkItsEnd,
                                               String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.endsWith(aEnd))
                return true;
        }
        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Log.e("LOG","返回点击!");
            File tmp=new File(this.currentDirectory.getAbsolutePath());
            //equals（）字符比较，==内存比较
            if (tmp.getAbsolutePath().toString().equals((FILE_SAVEPATH.toString() + "file/FILE"))){
                FilePdfActivity.this.finish();
            }else{
                this.upOneLevel();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
