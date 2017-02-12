package com.xxx.gogo.model.provider;

import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;

public class ProviderSearcher {
    private Callback mCb;
    private int mCurrentPageNum;
    private String mSearchKeyWord;
    private List<ProviderItemInfo> mInfoList;

    private boolean mIsLoading;

    //TODO
    static int sCount = 0;

    public ProviderSearcher(Callback cb){
        mCb = cb;
        mInfoList = new ArrayList<>();
    }

    public void load(String query){
        mSearchKeyWord = query;
        mIsLoading = true;

        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                final ArrayList<ProviderItemInfo> list = new ArrayList<>();
                for (int i = 0; i < 2; ++i){
                    ProviderItemInfo info = new ProviderItemInfo();
                    info.id = "1001";
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

        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                final ArrayList<ProviderItemInfo> list = new ArrayList<>();

                for (int i = 0; i < 20; ++i){
                    ProviderItemInfo info = new ProviderItemInfo();
                    info.id = "1001" + i;
                    info.name = "Jack King_" + sCount++;
                    info.phone = "1312839927";

                    list.add(info);
                }

                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        mCurrentPageNum += 1;
                        mInfoList.addAll(list);

                        mIsLoading = false;
                        mCb.onSuccess(mCurrentPageNum);
                    }
                }, 5000);
            }
        });
    }

    public int getCount(){
        return mInfoList.size();
    }

    public ProviderItemInfo getItem(int position){
        return mInfoList.get(position);
    }

    public interface Callback{
        void onSuccess(int page);
        void onFail();
    }
}
