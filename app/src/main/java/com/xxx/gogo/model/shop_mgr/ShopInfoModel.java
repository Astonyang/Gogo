package com.xxx.gogo.model.shop_mgr;

import com.google.gson.Gson;
import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.CryptoUtil;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.Preconditions;

public class ShopInfoModel implements LowMemoryListener{
    private static final String SHOP_INFO = "user/shop_info";

    private ShopInfo mInfo;

    public static class InstanceHolder{
        static ShopInfoModel infoModel = new ShopInfoModel();
    }

    public static ShopInfoModel getInstance(){
        return InstanceHolder.infoModel;
    }

    private ShopInfoModel(){
    }

    public void save(ShopInfo info){
        Preconditions.checkNotNull(info);

        mInfo = info;
        Gson gson = new Gson();
        FileManager.writeFile(SHOP_INFO, CryptoUtil.encrypt(gson.toJson(info).getBytes()));
    }

    public ShopInfo getInfo(){
        if(mInfo != null){
            return mInfo;
        }
        byte[] data = CryptoUtil.deEncrypt(FileManager.readFile(SHOP_INFO));
        if(data == null){
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(new String(data), ShopInfo.class);
    }

    public static class ShopInfo{
        public String name;
        public String addr;
        public String owner;
        public String phone;
        public short startTime;
        public short endTime;
    }

    @Override
    public void onLowMemory() {
        mInfo = null;
    }
}
