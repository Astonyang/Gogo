package com.xxx.gogo.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@SuppressWarnings("unused")
public class FileUtil {
    private static final String LOG_PREFIX = "FileManager: ";

    private static void FileUtilLog(String format, Object... args) {
        LogUtil.v(LOG_PREFIX + format, args);
    }

    public static boolean isExistFile(String path){
        File file = new File(path);
        return file.exists();
    }

    public static float getDiskAvailableWeight() {
        float weight = 0;
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            int blockSize;
            long total;
            long available;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = (int) statFs.getBlockSizeLong();
                total = statFs.getBlockCountLong() * blockSize;
                available = statFs.getAvailableBlocksLong() * blockSize;
            } else {
                blockSize = statFs.getBlockSize();
                total = statFs.getBlockCount() * blockSize;
                available = statFs.getAvailableBlocks() * blockSize;
            }

            weight = (float) available / (float) total;

        } catch (IllegalArgumentException e) {
            LogUtil.e("");
        }
        return weight;
    }

    public static long getDiskAvaliableSize() {
        long available = 0;
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            int blockSize;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = (int) statFs.getBlockSizeLong();
                available = statFs.getAvailableBlocksLong() * blockSize;
            } else {
                blockSize = statFs.getBlockSize();
                available = statFs.getAvailableBlocks() * blockSize;
            }

        } catch (IllegalArgumentException e) {
            LogUtil.e("");
        }
        return available;
    }

    public static long getFileSize(File file){
        if (file == null || !file.exists()) {
            return 0;
        }
        if (file.isDirectory()) {
            long temp = 0;
            File[] fileList = file.listFiles();
            if (null != fileList) {
                for (File child : fileList) {
                    temp += getFileSize(child);
                }
            }
            return temp;
        } else {
            return file.length();
        }
    }

    public static long getFileSize(String path){
        return getFileSize(new File(path));
    }

    public static void deleteFile(String path){
        deleteFile(new File(path));
    }

    public static void deleteFile(File file){
        if (file.isDirectory()) {
            String[] children = file.list();
            for (String child : children){
                deleteFile(new File(file, child));
            }
        }
        try {
            boolean ret = file.delete();
            FileUtilLog("deleteFile [ %s ] succeed", file.getAbsolutePath());
        } catch (Throwable t) {
            FileUtilLog("deleteFile [ %s ] fail with exception %s",
                    file.getAbsolutePath(), t.toString());
        }
    }

    public static File createFile(String path, boolean append) {
        File newFile = new File(path);
        if (!append) {
            if (newFile.exists()) {
                newFile.delete();
            }
        }
        if (!newFile.exists()) {
            try {
                File parent = newFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                newFile.createNewFile();

                FileUtilLog("createFile [ %s ] success", newFile.getAbsolutePath());
            } catch (Exception e) {
                FileUtilLog("createFile [ %s ] fail with exception %s",
                        newFile.getAbsolutePath(), e);
            }
        }
        return newFile;
    }

    public static void writeFile(String path, byte[] data){
        FileOutputStream outputStream = null;
        File file = createFile(path, false);
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(data);

            FileUtilLog("writeFile [ %s ] success", file.getAbsolutePath());

        }catch (Exception e){
            FileUtilLog("writeFile [ %s ] fail with exception %s", file.getAbsolutePath());
        }finally {
            if(outputStream != null){
                try{
                    outputStream.close();
                }catch (Exception e){
                    FileUtilLog("writeFile close handle [ %s ] fail with exception %s",
                            file.getAbsolutePath());
                }
            }
        }
    }

    public static byte[] readFile(String path){
        if(!isExistFile(path)){
            return null;
        }
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(path);

            byte[] buffer = new byte[4096];
            outputStream = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            FileUtilLog("readFile [ %s ] success", path);

            return outputStream.toByteArray();
        }catch (Exception e){
            FileUtilLog("readFile [ %s ] fail with exception %s", path, e);
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch (Exception e){
                    FileUtilLog("readFile close handle [ %s ] fail with exception %s", path, e);
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                }catch (Exception e){
                    FileUtilLog("readFile close handle [ %s ] fail with exception %s", path, e);
                }
            }
        }
        return null;
    }
}
