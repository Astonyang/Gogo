package com.xxx.gogo.model.order;

import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderConfirmModel {
    //// TODO: 17/2/16 should put it into strings.xml later
    private static final String PROVIDER_PREFIX = "供货商：";

    //share the same goods list with shop cart
    private ArrayList<GoodsItemInfo> mGoods;

    private ArrayList<String> mProviderIds; // for group
    private Map<String, List<GoodsItemInfo>> mGoodsMap;
    private Map<String, Double> mGroupTotalPrice;

    private WeakReference<Callback> mCb;

    public OrderConfirmModel(ArrayList<GoodsItemInfo> goodsInfo){
        mGoods = goodsInfo;
        mProviderIds = new ArrayList<>();
        mGoodsMap = new HashMap<>();
        mGroupTotalPrice = new HashMap<>();
    }

    public void setCallback(Callback callback){
        mCb = new WeakReference<>(callback);
    }

    public void build(){
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
                            mGroupTotalPrice.put("" + info.providerId, info.count * info.price);
                        }else {
                            List<GoodsItemInfo> list = mGoodsMap.get("" + info.providerId);
                            list.add(info);

                            double totalPrice = mGroupTotalPrice.get("" + info.providerId);
                            totalPrice += info.count * info.price;
                            mGroupTotalPrice.put("" + info.providerId, totalPrice);
                        }
                    }
                }
                if(mCb != null && mCb.get() != null){
                    mCb.get().onReady();
                }
            }
        });
    }

    public int getGroupCount(){
        return mProviderIds.size();
    }

    public String getGroup(int position){
        String providerId = mProviderIds.get(position);
        return PROVIDER_PREFIX + providerId;
//        ProviderItemInfo providerItemInfo = ProviderModel.getInstance().getProviderInfo(providerId);
//        if(providerItemInfo == null){
//            providerItemInfo = ProviderSearcher.getInstance().getItem(position);
//        }
//        if(providerItemInfo == null){
//            return PROVIDER_PREFIX + providerId + " ()";
//        }
//        return  PROVIDER_PREFIX + providerId + " ( " + providerItemInfo.name + " )";
    }

    public int getChildCount(int groupPosition){
        String providerId = mProviderIds.get(groupPosition);
        return mGoodsMap.get(providerId).size();
    }

    public int getCount(){
        return mGoods.size();
    }

    public GoodsItemInfo getItem(int groupPosition, int childPosition){
        String providerId = mProviderIds.get(groupPosition);
        return mGoodsMap.get(providerId).get(childPosition);
    }

    public synchronized ArrayList<GoodsItemInfo> getGoods(){
        return mGoods;
    }

    public synchronized Map<String, List<GoodsItemInfo>> getGoodsMap(){
        return mGoodsMap;
    }

    public synchronized double getTotalPriceForFroup(int groupPosition){
        String providerId = mProviderIds.get(groupPosition);
        return mGroupTotalPrice.get(providerId);
    }

    public void modifyState(int groupPosition, int childPosition, int state){
        GoodsItemInfo info = getItem(groupPosition, childPosition);
        if(info != null){
            info.state = state;
            if(mCb != null && mCb.get() != null){
                mCb.get().onDataChanged();
            }
        }
    }

    public void deleteItem(int groupPosition, int childPosition){
        String providerId = mProviderIds.get(groupPosition);
        mGoods.remove(mGoodsMap.get(providerId).remove(childPosition));
        if(mCb != null && mCb.get() != null){
            mCb.get().onDataChanged();
        }
    }

    public interface Callback{
        void onReady();
        void onDataChanged();
    }
}
