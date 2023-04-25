package com.woomiao.myutils.downloadfileutil;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class DownloadFileUtils {
    public static String path = Environment.getExternalStorageDirectory().toString() + "/MarketingPlanningTJ";
    private static File dir;

    public DownloadFileUtils() {
        File file = new File(path);
        /**
         *如果文件夹不存在就创建
         */
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 创建一个文件
     * @param FileName 文件名
     * @return
     */
    public File createFile(String FileName) {
        return new File(path, FileName);
    }




    public static void searchAllFile(String fileName, String tag, GetFile getFileInterface){
        Thread t1 = new Thread() {
            @Override
            public void run() {
                if (dir == null){
                    dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                }
                //"/Tencent/QQfile_recv"
                Log.i("TAG", "存储路径: " + Environment.getExternalStorageDirectory().getAbsolutePath());
                getfile(dir, fileName, tag, getFileInterface);

            }
        };
        t1.start();
    }


    private static void getfile(File dir,  String fileName, String tag, GetFile getFileInterface) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    getfile(listFile[i], fileName, tag, getFileInterface);
                } else {
                    if (listFile[i].getName().endsWith(tag)) {
                        if (fileName.equals(listFile[i].getName())) {
                            //找到了
                            getFileInterface.onSuccess(listFile[i]);
                        }
                    }
                }
            }
        }

        getFileInterface.onFail();
    }

    public interface GetFile{
        void onSuccess(File file);
        void onFail();
    }

}
