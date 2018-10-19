package com.netease.demo.apiprovider;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.netease.libs.neapiprovider.anno.NEApiProviderAnno;

import java.io.File;
import java.util.Timer;

/**
 * Created by zyl06 on 2018/10/18.
 */

@NEApiProviderAnno(provideStaticApi = true)
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

    public static void doNoReturn() {
        Log.i("FunctionUtil", "doNoReturn");
    }

    public static int add(int a, int b) {
        return a + b;
    }


    public static Timer getTimer() {
        return null;
    }

//    public static DataModel genDataModel() {
//        return new DataModel();
//    }

    public static class DataModel {
        public int a;
        public long b;
    }
}
