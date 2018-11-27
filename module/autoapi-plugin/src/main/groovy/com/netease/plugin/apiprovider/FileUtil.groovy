package com.netease.plugin.apiprovider;

/**
 * Created by zyl06 on 2018/10/19.
 */

class FileUtil {

    static boolean fileCopy(String fromPath, String toPath) throws IOException {
        //如果原文件不存在
        File fromFile = new File(fromPath)

        if (!fromFile.exists()) {
            return false
        }
        //获得原文件流
        FileInputStream inputStream = new FileInputStream(fromFile)
        return fileCopy(inputStream, toPath)
    }

    /**
     * 复制单个文件(一般用于将Assets目录下的文件拷贝)
     * @param inputStream InputStream
     * @param outFilePath
     * @return
     * @throws IOException
     */
    public static boolean fileCopy(InputStream inputStream, String outFilePath) throws IOException {
        if (inputStream == null) {
            return false
        }

        File outFile = new File(outFilePath)
        if (outFile.exists()) {
            outFile.delete()
        }

        File parentDir = outFile.getParentFile()
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs()
        }

        byte[] data = new byte[4 * 1024]
        int length//保存每次读取到的字节个数
        //输出流
        FileOutputStream outputStream = new FileOutputStream(outFile)
        //开始处理流
        while ((length = inputStream.read(data)) != -1) {
            outputStream.write(data, 0, length)
        }
        inputStream.close()
        outputStream.close()
        return true
    }

    public static boolean directoryCopy(String fromPath, String toPath) throws IOException {
        File fromDir = new File(fromPath)
        if (fromDir.exists()) {
            if (fromDir.isFile()) {
                fileCopy(fromDir.getAbsolutePath(), toPath + File.separator + fromDir.getName())
            } else if (fromDir.isDirectory()) {
                File[] files = fromDir.listFiles()
                for (File f : files) {
                    fileCopy(f.getAbsolutePath(), toPath + File.separator + f.getName())
                }
            }
        }
        return true
    }
}
