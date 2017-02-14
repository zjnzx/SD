package com.energyfuture.symphony.m3.analysis;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * 图片处理工具类
 * zhaogr
 */
public class ImageUtil {

    /**
     * 根据传入的二进制字符，将字符解析成图片
     * @designer
     * @since  2015-4-15
     * @author zhaogr
     * @param @param hex
     * @param @return
     * @return boolean
     */
    public static boolean imageAnalysis(String hex, String pathName){
//        String unString = ImageUtil.unZipString(hex);
        byte[] imageByte = ImageUtil.hex2byte(hex);

        FileOutputStream fi1;
        try {
            fi1 = new FileOutputStream(pathName);
            fi1.write(imageByte);
            fi1.flush();
            fi1.close();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return true;
    }



    public static  String image2String(File f) throws Exception {
        FileInputStream fis = new FileInputStream( f );
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        fis.close();

        // 生成字符串
        String imgStr = byte2hex( bytes );
        return imgStr;

    }

    private static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1){
                sb.append("0" + stmp);
            }else{
                sb.append(stmp);
            }

        }
        return sb.toString();
    }

    /**
     * 字符进行压缩
     * @designer
     * @since  2015-4-15
     * @author zhaogr
     * @param @param aInput
     * @param @return
     * @return String
     */
    public static String zipString(String aInput) {
        // Encode a String into bytes
        byte[] input;
        try {
            if (aInput == null) return null;

            input = aInput.getBytes("UTF-8");

            // Compress the bytes
            Deflater compresser = new Deflater();
            compresser.setLevel(Deflater.BEST_COMPRESSION);
            compresser.setInput(input);
            compresser.finish();

            ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int count = compresser.deflate(buf);
                bos.write(buf, 0, count);
            }
            bos.close();

            byte[] bt = bos.toByteArray();
            return Base64.encodeToString(bt, true);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符解压缩
     * @designer
     * @since  2015-4-15
     * @author zhaogr
     * @param @param aInput
     * @param @return
     * @return String
     */
    public static String unZipString(String aInput) {
        byte[] input;
        try {
            if (aInput == null) return null;

            input = Base64.decodeFast(aInput);

            // Decompress the bytes
            Inflater decompresser = new Inflater();
            decompresser.setInput(input, 0, input.length);

            ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int count = decompresser.inflate(buf);
                bos.write(buf, 0, count);
            }
            bos.close();

            return bos.toString("UTF-8");

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (DataFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 将字符转成二进制流
     * @designer
     * @since  2015-4-15
     * @author zhaogr
     * @param @param textFound
     * @param @return
     * @return byte[]
     */
    public static byte[] hex2byte(String textFound) {
        String str = textFound;
        if (str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1)
            return null;
        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0X" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断文件夹是否存在，不存在则创建文件夹
     * @designer
     * @since  2015-4-15
     * @author zhaogr
     * @param
     * @return void
     */
    public static String fileExists(String path){
        if(path != null && !path.equals("")){
            File file =new File(path);
            //如果文件夹不存在则创建
            if  (!file .exists())
            {
                file.mkdirs();
            } else
            {
//			    System.out.println("//目录存在");
            }

            return path;
        }
        return null;
    }



    /**
     * @param args
     */
    public static void main(String[] args) {
//		StringBuffer sb = new StringBuffer();
//	    String  imageByte=null;
//		File f = new File("C:\\Users\\Administrator\\Desktop\\IMG\\1.jpg");
//        if(f.exists()){
//            try {
//                imageByte=image2String(f);//得到转码后的字符串
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            String aa = zipString(imageByte.toString());
//            //将图片还原回来
//            imageAnalysis(aa);
//        }


    }








}
