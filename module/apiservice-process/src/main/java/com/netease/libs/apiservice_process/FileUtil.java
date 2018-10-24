package com.netease.libs.apiservice_process;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zyl06 on 2018/10/19.
 */

class FileUtil {

    // 不处理文件夹
    public static boolean deleteFile(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        File file = new File(path);
        return file.exists() && file.delete();
    }


    static boolean fileCopy(String fromPath, String toPath) {
        //如果原文件不存在
        File fromFile = new File(fromPath);

        if (!fromFile.exists()) {
            return false;
        }
        //获得原文件流
        FileInputStream is = null;
        try {
            is = new FileInputStream(fromFile);
            return fileCopy(is, toPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            safeClose(is);
        }

        return false;
    }

    private static void safeClose(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制单个文件(一般用于将Assets目录下的文件拷贝)
     * @param is InputStream
     * @param outFilePath
     * @return
     * @throws IOException
     */
    public static boolean fileCopy(InputStream is, String outFilePath) {
        if (is == null) {
            return false;
        }

        File outFile = new File(outFilePath);
        if (outFile.exists()) {
            outFile.delete();
        }

        File parentDir = outFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        byte[] data = new byte[4 * 1024];
        int length;//保存每次读取到的字节个数
        //输出流
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(outFile);

            //开始处理流
            while ((length = is.read(data)) != -1) {
                os.write(data, 0, length);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            safeClose(os);
        }

        return false;
    }
}
