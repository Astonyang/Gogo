package com.xxx.gogo.model.provider;

import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;

public class ProviderSearcher implements LowMemoryListener{
    private Callback mCb;
    private int mCurrentPageNum;
    private String mSearchKeyWord;
    private List<ProviderItemInfo> mInfoList;

    private boolean mIsLoading;

    //TODO
    static int sCount = 0;

    private static class InstanceHolder{
        private static ProviderSearcher sInstance = new ProviderSearcher();
    }

    public static ProviderSearcher getInstance(){
        return InstanceHolder.sInstance;
    }

    private ProviderSearcher(){
    }

    public void init(Callback callback){
        mCb = callback;
        mInfoList = new ArrayList<>();
    }

    public void load(String query){
        mSearchKeyWord = query;
        mIsLoading = true;

        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                final ArrayList<ProviderItemInfo> list = new ArrayList<>();
                for (int i = 0; i < 20; ++i){
                    ProviderItemInfo info = new ProviderItemInfo();
                    info.id = "1000001" + i;
                    info.name = "Jack King_" + sCount++;
                    info.phone = "1312839927";

                    list.add(info);
                }

                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        mIsLoading = false;
                        mInfoList.addAll(list);
                        mCb.onSuccess(0);
                    }
                }, 2000);
            }
        });
    }

    public void loadNext(){
        if(mIsLoading){
            return;
        }
        mIsLoading = true;
        mCurrentPageNum += 1;

        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                final ArrayList<ProviderItemInfo> list = new ArrayList<>();

                for (int i = 0; i < 20; ++i){
                    ProviderItemInfo info = new ProviderItemInfo();
                    info.id = mCurrentPageNum + "000001" + i;
                    info.name = "Jack King_" + sCount++;
                    info.phone = "1312839927";

                    list.add(info);
                }

                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        mInfoList.addAll(list);

                        mIsLoading = false;
                        mCb.onSuccess(mCurrentPageNum);
                    }
                }, 2000);
            }
        });
    }

    public void cancel(){

    }

    public int getCount(){
        return mInfoList.size();
    }

    public ProviderItemInfo getItem(int position){
        return mInfoList.get(position);
    }

    @Override
    public void onLowMemory() {
        if(mInfoList != null){
            mInfoList.clear();
        }
    }

    public interface Callback{
        void onSuccess(int page);
        void onFail();
    }
}
