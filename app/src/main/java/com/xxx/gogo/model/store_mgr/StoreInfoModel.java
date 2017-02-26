package com.xxx.gogo.model.store_mgr;

import com.google.gson.Gson;
import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.CryptoUtil;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.Preconditions;
import com.xxx.gogo.utils.ThreadManager;

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
        final byte[] data = gson.toJson(mInfo).getBytes();

        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                FileManager.writeFile(SHOP_INFO, CryptoUtil.encrypt(data));
            }
        });
    }

    public void getInfo(final Callback callback){
        if(mInfo != null){
            callback.onLoaded(mInfo);
            return;
        }

        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                byte[] rawData = FileManager.readFile(SHOP_INFO);
                if(rawData != null && rawData.length != 0){
                    byte[] data = CryptoUtil.deEncrypt(rawData);
                    if(data == null){
                        notifyFail(callback);
                        return;
                    }
                    final String strJson = new String(data);

                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            callback.onLoaded(gson.fromJson(strJson, StoreInfo.class));
                        }
                    });
                }else {
                    notifyFail(callback);
                }
            }
        });
    }

    private void notifyFail(final Callback callback){
        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                callback.onLoaded(null);
            }
        });
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

    public interface Callback{
        void onLoaded(StoreInfo storeInfo);
    }
}
