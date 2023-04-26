package com.woomiao.myutils.downloadfileutil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载文件
 */
public class DownloadUtils {

    /**
     * 从服务器下载文件
     * @param activity
     * @param path 下载文件的地址
     * @param FileName 文件名
     */
    public static void downLoad(Activity activity, final String path, final String FileName, IDownload iDownload) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        FileOutputStream fileOutputStream = null;//文件输出流
                        if (is != null) {
                            DownloadFileUtils fileUtils = new DownloadFileUtils();
                            fileOutputStream = new FileOutputStream(fileUtils.createFile(FileName));//指定文件保存路径，代码看下一步
                            byte[] buf = new byte[1024];
                            int ch;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        //下载完成后关闭进度条
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iDownload.onSuccess(new File(DownloadFileUtils.path, FileName));
                            }
                        });
//                        Log.i("TAG", "下載完成: "+FileName);
                    }
                } catch (Exception e) {
                    iDownload.onError(e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface IDownload{
        void onSuccess(File file);
        void onError(String e);
    }

    public static File getFile(String filename){
        return new File(Environment.getExternalStorageDirectory().toString()+"/shidoe", filename);
    }


    public static void loadImg(ImageView imageView, Activity activity, String filename){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().toString()+"/shidoe";
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(path, filename)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);
//                String[] type = filename.split(".");
//                for (int i = 0; i < type.length; i++){
//                    Log.i("TAG", "文件路径分解: "+type);
//                }
            }
        });

    }

}
