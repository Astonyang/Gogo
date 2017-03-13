package com.xxx.gogo.model.often_buy;

import com.xxx.gogo.model.BaseModel;
import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.model.goods.GoodsItemInfo;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OftenBuyModel extends BaseModel
        implements LowMemoryListener, OftenBuyLocalDataSource.Callback{
    private static final long OFTEN_BUY_TIMEOUT = 1000*60*60*24*7;
    private static final int OFTEN_BUY_MIN_COUNT = 1;

    private OftenBuyLocalDataSource mDataSource;
    private ArrayList<GoodsItemInfo> mGoods;

    private WeakReference<Callback> mCb;

    private static class InstanceHolder{
        private static OftenBuyModel sInstance = new OftenBuyModel();
    }

    public static OftenBuyModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private OftenBuyModel(){
        mGoods = new ArrayList<>();
        mDataSource = new OftenBuyLocalDataSource(this);
    }

    public void setCallback(Callback callback){
        mCb = new WeakReference<>(callback);
    }

    public void add(final ArrayList<GoodsItemInfo> infos){
        final ArrayList<GoodsItemInfo> newList = new ArrayList<>();
        newList.addAll(infos);

        ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
            @Override
            public void run() {

                final ArrayList<GoodsItemInfo> oldList = new ArrayList<>();
                synchronized (OftenBuyModel.this){
                    oldList.addAll(mGoods);
                }

                final ArrayList<GoodsItemInfo> list = processGoodsList(oldList, newList);

                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        synchronized (OftenBuyModel.this){
                            mGoods.clear();
                            mGoods.addAll(list);
                            mDataSource.add(mGoods);
                        }
                    }
                });
            }
        });
    }

    public void load(){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mGoods != null && !mGoods.isEmpty()){
            return;
        }
        mState = STATE_LOADING;
        mDataSource.load();
    }

    public void clear(){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        mState = STATE_INIT;
        if(mGoods != null){
            mGoods.clear();
        }
    }

    public void removeByProviderId(String providerId){
        mDataSource.removeByProviderId(providerId);
    }

    public synchronized int getCount() {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        return mGoods.size();
    }

    public synchronized Object getChild(int position) {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        return mGoods.get(position);
    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onLoadFail() {
        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                mState = STATE_LOADED;
                if(mCb != null && mCb.get() != null){
                    mCb.get().onLoadFail();
                }
            }
        });
    }

    @Override
    public void onLoadSuccess(List<GoodsItemInfo> goods) {
        if(goods != null && !goods.isEmpty()) {
            synchronized (this) {
                mGoods.addAll(goods);
            }
        }
        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                mState = STATE_LOADED;
                if(mCb != null && mCb.get() != null){
                    mCb.get().onLoadSuccess();
                }
            }
        });
    }

    @Override
    public void onAdded() {
        ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
            @Override
            public void run() {
                if(mCb != null && mCb.get() != null){
                    mCb.get().onAdded();
                }
            }
        });
    }

    @Override
    public void onRemoved(final String providerId) {
        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {

                synchronized (OftenBuyModel.this){
                    Iterator<GoodsItemInfo> iterator = mGoods.iterator();
                    while (iterator.hasNext()){
                        String pid = String.valueOf(iterator.next().providerId);
                        if(providerId.equals(pid)){
                            iterator.remove();
                        }
                    }
                }

                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        if(mCb != null && mCb.get() != null){
                            mCb.get().onRemoved();
                        }
                    }
                });
            }
        });
    }

    /**
     * 生成常购数据列表，规则：删除7天内未再次购买的商品
     */
    private ArrayList<GoodsItemInfo> processGoodsList(ArrayList<GoodsItemInfo> oldList,
                                                      ArrayList<GoodsItemInfo> newList){
        for (GoodsItemInfo itemInfo : newList){
            if(oldList.contains(itemInfo)){
                GoodsItemInfo info = oldList.get(oldList.indexOf(itemInfo));
                info.buyTime = System.currentTimeMillis();
                info.buyCount += 1;
            }else {
                itemInfo.buyCount += 1;
                itemInfo.buyTime = System.currentTimeMillis();
                oldList.add(itemInfo);
            }
        }
        ArrayList<GoodsItemInfo> list = new ArrayList<>();
        for (GoodsItemInfo info : oldList){
            if(System.currentTimeMillis() - info.buyTime < OFTEN_BUY_TIMEOUT
                    || info.buyCount > OFTEN_BUY_MIN_COUNT){
                list.add(info);
            }
        }
        return list;
    }

    public interface Callback{
        void onAdded();
        void onRemoved();
        void onLoadSuccess();
        void onLoadFail();
    }
}
