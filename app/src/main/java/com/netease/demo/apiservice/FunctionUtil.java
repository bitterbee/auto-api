package com.netease.demo.apiservice;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;

import java.io.File;

/**
 * Created by zyl06 on 2018/10/27.
 */
@ApiServiceClassAnno(name = "AppFunction", allPublicStaticApi = true)
public class FunctionUtil {
    public static long getSDAvailableSize() {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                return blockSize * availableBlocks;
            } catch (Exception e) {
                e.printStackTrace();// 关闭权限可能会抛出异常
            }
        }
        return 0;
    }

    public static void noReturn() {
        Log.i("Calculator", "noReturn");
    }
}
