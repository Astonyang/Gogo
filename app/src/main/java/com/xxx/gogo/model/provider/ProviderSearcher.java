package com.xxx.gogo.model.provider;

import com.xxx.gogo.model.LowMemoryListener;
import com.xxx.gogo.net.NetworkProtocolFactory;
import com.xxx.gogo.net.NetworkResponse;
import com.xxx.gogo.net.NetworkServiceFactory;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProviderSearcher implements LowMemoryListener{
    private WeakReference<Callback> mCb;
    private int mCurrentPageNum;
    private String mSearchKeyWord;
    private List<ProviderItemInfo> mInfoList;

    private boolean mIsLoading;

    private Call<NetworkResponse.ProviderSearchResponse> mCall;

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
        mCb = new WeakReference<>(callback);
        mInfoList = new ArrayList<>();
    }

    public void load(String query){
        mSearchKeyWord = query;

        loadNext();
    }

    public void loadNext(){
        if(mIsLoading){
            return;
        }
        mIsLoading = true;

        mCall = NetworkServiceFactory.getInstance().getService().searchProvider(
                NetworkProtocolFactory.buildProviderSearchRequest(mSearchKeyWord, mCurrentPageNum));

        mCall.enqueue(new retrofit2.Callback<NetworkResponse.ProviderSearchResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse.ProviderSearchResponse> call,
                                   Response<NetworkResponse.ProviderSearchResponse> response) {
                if(mCb == null || mCb.get() == null){
                    return;
                }

                if(response.isSuccessful() && response.body().isSuccessful()){
                    //// TODO: 17/3/2 for test
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

                                    mCb.get().onSuccess(mCurrentPageNum);
                                    mCurrentPageNum += 1;
                                }
                            }, 1000);
                        }
                    });

                }else {
                    mIsLoading = false;
                    mCb.get().onFail();
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse.ProviderSearchResponse> call, Throwable t) {
                mIsLoading = false;
                if(mCb != null && mCb.get() != null){
                    mCb.get().onFail();
                }
            }
        });
    }

    //// TODO: 17/3/2 test
    List<ProviderItemInfo> getTestData(){
        final ArrayList<ProviderItemInfo> list = new ArrayList<>();

        for (int i = 0; i < 20; ++i){
            ProviderItemInfo info = new ProviderItemInfo();
            info.id = mCurrentPageNum + "000001" + i;
            info.name = "Jack King_" + sCount++;
            info.phone = "1312839927";

            list.add(info);
        }
        return list;
    }

    public void cancel(){
        mIsLoading = false;
        if(mCall != null && !mCall.isCanceled() && !mCall.isExecuted()){
            mCall.cancel();
        }
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
        void onHasNoData();
    }
}
