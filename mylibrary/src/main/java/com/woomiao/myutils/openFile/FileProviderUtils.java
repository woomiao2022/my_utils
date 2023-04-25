package com.woomiao.myutils.openFile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;

public class FileProviderUtils {
    public static Uri getUriForFile(Context mContext, File file, @NonNull String authority) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) { // Android 7 及以上版本
            fileUri = getUriForFile24(mContext, file, authority);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public static Uri getUriForFile24(Context mContext, File file, @NonNull String authority) {
        Uri fileUri = FileProvider.getUriForFile(mContext, authority,file);
//        Uri fileUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID+".provider",file);
        return fileUri;
    }

    public static void setIntentDataAndType(Context mContext,
                                            Intent intent,
                                            String type,
                                            File file,
                                            boolean writeAble, @NonNull String authority) {
        if (Build.VERSION.SDK_INT >= 24) { // Android 7 及以上版本
            intent.setDataAndType(getUriForFile(mContext, file, authority), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }
}
