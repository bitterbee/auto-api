package com.netease.libs.apiservice_process;

import com.squareup.javapoet.JavaFile;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zyl06 on 2018/10/19.
 */

public class FileUtil {

    public static String sToProjectPath;
    public static String sFromPkgName;

    public static void writeTo(JavaFile javaFile) throws IOException {
        String to = sToProjectPath + "/src/main/java/";
        File toDir = new File(to);
        if (!toDir.exists()) {
            boolean success = toDir.mkdirs();
            if (!success) {
                Logger.e(to + " folder not exists!!!");
                return;
            }
        }
        javaFile.writeTo(toDir);

        // 生成 record 文件
        String pkgName = javaFile.packageName;
        String name = javaFile.typeSpec.name;

        String relativePath = null;
        if (pkgName == null || pkgName.isEmpty()) {
            relativePath = name + ".java";

        } else {
            pkgName = pkgName.replaceAll("\\.", "/");
            relativePath = pkgName + "/" + name + ".java";
        }

        String filePath = to + relativePath;
        Logger.i("java filePath = " + filePath);

        FileWriter fw = null;
        try {
            File file = new File(getRecordFilePath());
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            fw = new FileWriter(file, true);
            fw.append(relativePath).append(";").append("\n");
            fw.flush();
        } catch (IOException e) {
            Logger.e(e.toString());
        } finally {
            safeClose(fw);
        }
    }

    public static String getRecordFilePath() {
        return sFromPkgName == null || sFromPkgName.isEmpty() ?
                sToProjectPath + "/api_records/records.txt" :
                sToProjectPath + "/api_records/" + sFromPkgName + ".records.txt";
    }

    public static void deleteRecord() {
        deleteFile(getRecordFilePath());
    }

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
