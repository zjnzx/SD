package com.energyfuture.symphony.m3.util;

import android.os.Environment;
import android.util.Log;

import com.energyfuture.symphony.m3.domain.TrDataSynchronization;
import com.energyfuture.symphony.m3.domain.UserInfo;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by Administrator on 2015/7/3.
 */
public class UplodaFile {


    /**
     * 上传文件至Server的方法
     * @param uploadUrl 请求url
     * @param srcPath 本地文件地址
     */
    public static void uploadFile(String uploadUrl,String srcPath){

        //String uploadUrl = "http://192.168.1.21:9090/upload_file_service/UploadServlet";
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try{
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens+""+boundary+""+end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""+
                    srcPath.substring(srcPath.lastIndexOf("/")+1)+"\""+end);
            dos.writeBytes(end);
            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1){
                dos.write(buffer, 0, count);

            }
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens+" "+boundary+" "+twoHyphens+" "+end);
            dos.flush();
//            InputStream is = httpURLConnection.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is, "utf-8");
//            BufferedReader br = new BufferedReader(isr);
//            String result = br.readLine();
//
//            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            dos.close();
//            is.close();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    //删除文件
    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            }
        }
    }


    public  static void downFile(String urlString, TrDataSynchronization sys){

        /*
         * 连接到服务器
         */
                int FileLength;
                int DownedFileLength=0;
                int downLoadFilePosition = 0;
                InputStream inputStream = null;
                URLConnection connection = null;
                OutputStream outputStream = null;
                String savePath="";
                try {
                    URL url=new URL(urlString);
                    connection = url.openConnection();
                    if (connection.getReadTimeout()==5) {
                        Log.i("---------->", "当前网络有问题");
                        // return;
                    }
                    inputStream=connection.getInputStream();
                    if(inputStream ==null){
                        return ;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        /*
         * 文件的保存路径和和文件名其中Nobody.mp3是在手机SD卡上要保存的路径，如果不存在则新建
         */
                String pathTemp = Environment.getExternalStorageDirectory().getAbsolutePath();
        if(sys.getTablename().equals("user_info")||sys.getTablename().equals("USER_INFO")){

            savePath= pathTemp.substring(0,pathTemp.length()-1)+ "legacy" + "/M3/transformer/picture/head/"+sys.getFileurl();
        }else{
            savePath= pathTemp.substring(0,pathTemp.length()-1)+ "legacy" + "/M3/transformer/file/"+sys.getFileurl();

        }
                File file1=new File(savePath);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File file =new File(savePath+"/"+sys.getFilename());
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        /*
         * 向SD卡中写入文件,用Handle传递线程
         */
//        Message message=new Message();
                try {
                    outputStream=new FileOutputStream(file);
                    byte [] buffer=new byte[1024*4];
                    FileLength= connection.getContentLength();
                    if (FileLength <= 0) {  // 获取内容长度为0
                        throw new RuntimeException("无法获知文件大小 ");
                    }
//            message.what=0;
//            handler.sendMessage(message);
                    while ((DownedFileLength = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, DownedFileLength);
                        downLoadFilePosition += DownedFileLength;
//                Message message1=new Message();
//                message1.what=1;
//                handler.sendMessage(message1);
                    }
//            Message message2=new Message();
//            message2.what=2;
//            handler.sendMessage(message2);

                    try {
                        inputStream.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

    public  static void downuserFile(String urlString, UserInfo userinfo){

        /*
         * 连接到服务器
         */
        int FileLength;
        int DownedFileLength=0;
        int downLoadFilePosition = 0;
        InputStream inputStream = null;
        URLConnection connection = null;
        OutputStream outputStream = null;
        String savePath="";
        try {
            URL url=new URL(urlString);
            connection = url.openConnection();
            if (connection.getReadTimeout()==5) {
                Log.i("---------->", "当前网络有问题");
                // return;
            }
            inputStream=connection.getInputStream();
            if(inputStream ==null){
                return ;
            }
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*
         * 文件的保存路径和和文件名其中Nobody.mp3是在手机SD卡上要保存的路径，如果不存在则新建
         */
        String pathTemp = Environment.getExternalStorageDirectory().getAbsolutePath();
        savePath= pathTemp.substring(0,pathTemp.length()-1)+ "legacy" + "/M3/transformer/picture/head/USER/small/";
        File file1=new File(savePath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File file =new File(savePath + userinfo.getSsxt().substring(5));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        /*
         * 向SD卡中写入文件,用Handle传递线程
         */
//        Message message=new Message();
        try {
            outputStream=new FileOutputStream(file);
            byte [] buffer=new byte[1024*4];
            FileLength= connection.getContentLength();
            if (FileLength <= 0) {  // 获取内容长度为0
                throw new RuntimeException("无法获知文件大小 ");
            }
//            message.what=0;
//            handler.sendMessage(message);
            while ((DownedFileLength = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, DownedFileLength);
                downLoadFilePosition += DownedFileLength;
//                Message message1=new Message();
//                message1.what=1;
//                handler.sendMessage(message1);
            }
//            Message message2=new Message();
//            message2.what=2;
//            handler.sendMessage(message2);

            try {
                inputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
