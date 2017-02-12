package com.xxx.gogo.model.order;

import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.model.provider.ProviderModel;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderConfirmModel {
    private ArrayList<GoodsItemInfo> mGoods;

    private ArrayList<String> mProviderIds;
    private HashMap<String, ArrayList<GoodsItemInfo>> mGoodsMap;

    public OrderConfirmModel(ArrayList<GoodsItemInfo> goodsInfo){
        mGoods = goodsInfo;
        mProviderIds = new ArrayList<>();
        mGoodsMap = new HashMap<>();
    }

    public void build(final Callback cb){
        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                synchronized (OrderConfirmModel.this){
                    for (GoodsItemInfo info : mGoods){
                        if(!mProviderIds.contains("" + info.providerId)){
                            mProviderIds.add("" + info.providerId);
                            ArrayList<GoodsItemInfo> list = new ArrayList<>();
                            list.add(info);
                            mGoodsMap.put("" + info.providerId, list);
                        }else {
                            ArrayList<GoodsItemInfo> list = mGoodsMap.get("" + info.providerId);
                            list.add(info);
                        }
                    }
                }
                cb.onReady();
            }
        });
    }

    public int getGroupCount(){
        return mProviderIds.size();
    }

    public String getGroup(int position){
        String providerId = mProviderIds.get(position);
        return ProviderModel.getInstance().getProviderInfo(providerId).name;
    }

    public int getChildCount(int groupPosition){
        String providerId = mProviderIds.get(groupPosition);
        return mGoodsMap.get(providerId).size();
    }

    public GoodsItemInfo getItem(int groupPosition, int childPosition){
        String providerId = mProviderIds.get(groupPosition);
        return mGoodsMap.get(providerId).get(childPosition);
    }

    public synchronized ArrayList<GoodsItemInfo> getGoods(){
        return mGoods;
    }

    public synchronized HashMap<String, ArrayList<GoodsItemInfo>> getGoodsMap(){
        return mGoodsMap;
    }

    public interface Callback{
        void onReady();
    }
}
