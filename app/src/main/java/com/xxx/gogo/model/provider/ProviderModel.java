package com.xxx.gogo.model.provider;

import com.xxx.gogo.model.BaseModel;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.FileUtil;
import com.xxx.gogo.utils.LogUtil;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ProviderModel extends BaseModel{
    private WeakReference<Callback> mCb;
    private ProviderLocalDataSource mDataSource;
    private ArrayList<ProviderItemInfo> mDatas;
    private HashMap<String, ProviderItemInfo> mIds;

    private static class InstanceHolder{
        private static ProviderModel sInstance = new ProviderModel();
    }

    public static ProviderModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private ProviderModel(){
        mIds = new HashMap<>();
        mDatas = new ArrayList<>();
        mDataSource = new ProviderLocalDataSource(this);
    }

    public void setCallback(Callback cb){
        mCb = new WeakReference<>(cb);
    }

    public void addItem(final ProviderItemInfo info){
        if(mDatas.isEmpty()){
            ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
                @Override
                public void run() {
                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            addItemInternal(info);
                        }
                    });
                }
            });
        }else {
            addItemInternal(info);
        }
    }

    @SuppressWarnings("unused")
    public void deleteItem(final ProviderItemInfo info){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        for (ProviderItemInfo item : mDatas){
            if(info.id.equalsIgnoreCase(item.id)){
                mDatas.remove(item);
                break;
            }
        }
        mIds.remove(info.id);
        mDataSource.deleteItem(info);
        Callback callback = mCb.get();
        if(callback != null){
            callback.onDeleteItem();
        }
    }

    public void deleteItem(final int pos){
        if(mDatas.isEmpty()){
            ThreadManager.postTask(ThreadManager.TYPE_DB, new Runnable() {
                @Override
                public void run() {
                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            deleteItemInternal(pos);
                        }
                    });
                }
            });
        }else {
            deleteItemInternal(pos);
        }
    }

    private void addItemInternal(ProviderItemInfo info){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mIds != null && mIds.containsKey(info.id)){
            return;
        }
        mDatas.add(info);
        mIds.put(info.id, info);
        Collections.sort(mDatas, new Comparator<ProviderItemInfo>() {
            @Override
            public int compare(ProviderItemInfo o1, ProviderItemInfo o2) {
                return o1.name.compareTo(o2.name);
            }
        });
        mDataSource.addItem(info);
        Callback callback = mCb.get();
        if(callback != null){
            callback.onAddItem();
        }
    }

    private void deleteItemInternal(int pos){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        ProviderItemInfo info = mDatas.remove(pos);
        mDataSource.deleteItem(info);
        mIds.remove(info.id);
        Callback callback = mCb.get();
        if(callback != null){
            callback.onDeleteItem();
        }
    }

    public int getCount(){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        return mDatas.size();
    }

    public void load(){
        mState = STATE_LOADING;
        mDataSource.load();
    }

    public void clear(){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        mIds.clear();
        mDatas.clear();
        mState = STATE_INIT;
    }

    public String getContactName(int pos){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mDatas != null && mDatas.size() > pos){
            return mDatas.get(pos).name;
        }
        return null;
    }

    public String getId(int pos){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mDatas != null && mDatas.size() > pos){
            return mDatas.get(pos).id;
        }
        return null;
    }

    public ProviderItemInfo getProviderInfo(int position){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(position < mDatas.size()){
            return mDatas.get(position);
        }
        return null;
    }

    public ProviderItemInfo getProviderInfo(String id){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mIds.containsKey(id)){
            return mIds.get(id);
        }
        return null;
    }

    void onDataReady(ArrayList<ProviderItemInfo> datas, HashMap<String, ProviderItemInfo> idSets){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        mState = BaseModel.STATE_LOADED;
        if(datas != null && !datas.isEmpty()){
            mDatas = datas;
            mIds = idSets;
        }
        Callback callback = mCb.get();
        if(callback != null){
            if(mDatas != null && !mDatas.isEmpty()){
                callback.onLoadSuccess();
                checkIfNeedPurgeProviderData();
            }else {
                callback.onLoadFail();
            }
        }
    }

    private void checkIfNeedPurgeProviderData(){
        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                long availableSize = FileUtil.getDiskAvaliableSize();
                long fileSize = FileManager.getGlobalFileSize(null);

                LogUtil.v("availableSize = %d MB fileSize = %d KB",
                        availableSize/(1024*1024), fileSize / (1024));

                if((float)fileSize / (float) availableSize > 0.8){
                    LogUtil.d("clear all data from " + FileManager.sRootDir);

                    for (ProviderItemInfo info : mDatas){
                        FileManager.deleteGlobalFile(info.id);
                    }
                }
            }
        });
    }

    public interface Callback{
        void onLoadSuccess();
        void onLoadFail();
        void onAddItem();
        void onDeleteItem();
    }
}
