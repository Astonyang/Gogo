package com.xxx.gogo.utils;

import java.io.File;

public class FileManager {
    public static String sRootDir;

    public static void writeFile(String path, byte[] data){
        FileUtil.writeFile(sRootDir + File.separator + path, data);
    }

    public static byte[] readFile(String path){
        return FileUtil.readFile(sRootDir + File.separator + path);
    }
}
