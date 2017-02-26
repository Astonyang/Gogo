package com.xxx.gogo.model.order;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xxx.gogo.utils.CryptoUtil;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.ThreadManager;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

public class OrderItemDetailModel {
    private List<OrderItemDetailInfo> mDatas;

    private OrderItemInfo mOrderInfo;

    public OrderItemDetailModel(OrderItemInfo info){
        mOrderInfo = info;
    }

    public void load(Callback cb){
        final WeakReference<Callback> callback = new WeakReference<>(cb);

        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                byte[] encrypted = FileManager.readFile(OrderLocalDataSource.ORDER_DETAIL_PATH
                        + File.separator + mOrderInfo.id);
                if(encrypted != null){
                    byte[] rawData = CryptoUtil.deEncrypt(encrypted);
                    if(rawData != null){
                        Gson gson = new Gson();
                        final List<OrderItemDetailInfo> list = gson.fromJson(new String(rawData),
                                new TypeToken<List<OrderItemDetailInfo>>(){}.getType());

                        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                            @Override
                            public void run() {
                                mDatas = list;

                                if(callback != null && callback.get() != null){
                                    callback.get().onSuccess();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public int getCount(){
        return mDatas == null ? 0 : mDatas.size();
    }

    public OrderItemDetailInfo getItem(int pos){
        return mDatas.get(pos);
    }

    public interface Callback{
        void onSuccess();
    }
}
