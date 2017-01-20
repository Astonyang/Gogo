package com.xxx.gogo.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtil {
    public static final String ROOT_DIR = "xxx_data";

    public static boolean isExistFile(String path){
        File file = new File(path);
        return file.exists();
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
            file.delete();
        } catch (Throwable t) {
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
            } catch (Exception e) {
            }
        }
        return newFile;
    }

    public static void writeFile(String path, byte[] data){
        FileOutputStream outputStream = null;
        try {
            File file = createFile(path, false);
            outputStream = new FileOutputStream(file);
            outputStream.write(data);
        }catch (Exception e){
        }finally {
            if(outputStream != null){
                try{
                    outputStream.close();
                }catch (Exception e){
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
            return outputStream.toByteArray();
        }catch (Exception e){
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch (Exception e){
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                }catch (Exception e){
                }
            }
        }
        return null;
    }
}
