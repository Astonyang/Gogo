package com.xxx.gogo.utils;

import com.xxx.gogo.manager.user.UserManager;

import java.io.File;

public class FileManager {
    public static String sRootDir;

    public static void writeFile(String path, byte[] data){
        FileUtil.writeFile(UserManager.getInstance().getUserDir() + File.separator + path, data);
    }

    public static byte[] readFile(String path){
        return FileUtil.readFile(UserManager.getInstance().getUserDir() + File.separator + path);
    }

    public static void writeGlobalFile(String path, byte[] data){
        FileUtil.writeFile(sRootDir + File.separator + path, data);
    }

    public static byte[] readGlobalFile(String path){
        return FileUtil.readFile(sRootDir + File.separator + path);
    }

    public static void deleteGlobalFile(String path){
        FileUtil.deleteFile(sRootDir + File.separator + path);
    }
}
