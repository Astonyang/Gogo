package com.xxx.gogo.model.store_mgr;

import com.google.gson.Gson;
import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.CryptoUtil;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.Preconditions;

public class StoreInfoModel implements LowMemoryListener{
    private static final String SHOP_INFO = "user/shop_info";

    private StoreInfo mInfo;

    public static class InstanceHolder{
        static StoreInfoModel infoModel = new StoreInfoModel();
    }

    public static StoreInfoModel getInstance(){
        return InstanceHolder.infoModel;
    }

    private StoreInfoModel(){
    }

    public void save(StoreInfo info){
        Preconditions.checkNotNull(info);

        mInfo = info;
        Gson gson = new Gson();
        FileManager.writeFile(SHOP_INFO, CryptoUtil.encrypt(gson.toJson(info).getBytes()));
    }

    public StoreInfo getInfo(){
        if(mInfo != null){
            return mInfo;
        }
        byte[] rawData = FileManager.readFile(SHOP_INFO);
        if(rawData != null && rawData.length != 0){
            byte[] data = CryptoUtil.deEncrypt(rawData);
            if(data == null){
                return null;
            }
            Gson gson = new Gson();
            return gson.fromJson(new String(data), StoreInfo.class);
        }
        return null;
    }

    public static class StoreInfo{
        public String name;
        public String addr;
        public String owner;
        public String phone;
        public String startTime;
        public String endTime;
    }

    @Override
    public void onLowMemory() {
        mInfo = null;
    }
}
