package com.woomiao.myutils.openFile;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 打开文件
 *
 */
public class OpenFileUtils {

    /**
     * 打开文件
     *
     * @param context
     * @param file 文件
     * @param authority 传入：BuildConfig.APPLICATION_ID+".provider"
     *                  前提是需要在Manifests中添加：
     *                  <provider
     *                     android:name="androidx.core.content.FileProvider"
     *                     android:authorities="包名.provider"
     *                     android:exported="false"
     *                     android:grantUriPermissions="true">
     *                     <meta-data
     *                         android:name="android.support.FILE_PROVIDER_PATHS"
     *                         android:resource="@xml/file_paths" /> //res/xml文件夹下创建该文件
     *                  </provider>
     *
     *                  @xml/file_paths 文件内容：
     *                         <paths xmlns:android="http://schemas.android.com/apk/res/android">
     *                             <external-path name="my_files" path="" />
     *
     *                         </paths>
     *
     */
    public static void open(Context context, File file, @NonNull String authority){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        String type = getContentType(file.getName());
        if (TextUtils.isEmpty(type)){
            Toast.makeText(context, "文件打开失败", Toast.LENGTH_SHORT).show();
            return;
        }
        FileProviderUtils.setIntentDataAndType(context, intent, type, file, true,authority);
        context.startActivity(intent);
    }

    //获取mine type
    public static String getContentType(String filename){
        String type = null;
        Path path = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            path = Paths.get(filename);
            try {
                type = Files.probeContentType(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return type;
    }
}
