package com.xxx.gogo.model;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xxx.gogo.loader.IPagedLoader;
import com.xxx.gogo.loader.IPagedLoaderCallback;
import com.xxx.gogo.utils.Constants;
import com.xxx.gogo.utils.FileManager;
import com.xxx.gogo.utils.LogUtil;
import com.xxx.gogo.utils.ThreadManager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class PagedModel<ContentType> {

    private static final String VERSION_FILE_NAME = "content_version";

    private String mPath;
    private int mCountPerPage;
    private Type mItemType; //for gson

    private volatile boolean mIsLoading;

    private SparseArray<String> mVersions;
    private SparseArray<List<ContentType>> mPagedDatas;

    private IPagedLoader mLoader;

    public PagedModel(String path, int countPerPage, IPagedLoader loader, Type type){
        mPath = path;
        mCountPerPage = countPerPage;
        mLoader = loader;
        mPagedDatas = new SparseArray<>();
        mItemType = type;
    }

    public void destroy(){
        mVersions.clear();
        mPagedDatas.clear();
        mLoader.cancel();
        mIsLoading = false;

        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
                    String jsonStr = gson.toJson(mVersions,
                            new TypeToken<SparseArray<String>>(){}.getType());

                    FileManager.writeGlobalFile(mPath + File.separator + VERSION_FILE_NAME,
                            jsonStr.getBytes());

                }catch (Exception e){
                    LogUtil.e("save content version fail : " + e);
                }
            }
        });
    }

    public int getCount(){
        int pageCount = mPagedDatas.size();
        if(pageCount == 0){
            return 0;
        }
        return mCountPerPage * (pageCount - 1) + mPagedDatas.get(pageCount-1).size();
    }

    public ContentType getItem(int position){
        int pageNum = position / mCountPerPage;
        return mPagedDatas.get(pageNum).get(position % mCountPerPage);
    }

    public void loadNext(final Callback callback){
        if(mIsLoading){
            return;
        }
        mIsLoading = true;
        final int pageNum = mPagedDatas == null ? 0 : mPagedDatas.size();

        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                doLoad(pageNum, callback);
            }
        });
    }

    private void doLoad(final int pageNum, final Callback callback){
        String version = "";
        try {
            if(pageNum == 0){
                byte data[] = FileManager.readGlobalFile(mPath + File.separator + VERSION_FILE_NAME);
                if(data != null){
                    Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
                    mVersions = gson.fromJson(new String(data),
                            new TypeToken<SparseArray<String>>(){}.getType());
                }
            }
            if(mVersions != null && mVersions.get(pageNum) != null){
                version = mVersions.get(pageNum);
            }
            if(!FileManager.isExistGlobalFile(mPath + File.separator + pageNum)){
                version = "";
            }
        }catch (Exception e){
            LogUtil.e("load content version fail : " + e);
        }
        if(mVersions == null){
            mVersions = new SparseArray<>();
        }
        mLoader.load(pageNum, version, new IPagedLoaderCallback<ContentType>() {
            @Override
            public void onLoadSuccess(List<ContentType> dataList, String contentVersion) {
                ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

                mIsLoading = false;
                if(dataList == null || dataList.isEmpty()){
                    makeDataForPage(pageNum, callback);
                }else {
                    mVersions.put(pageNum, contentVersion);
                    mPagedDatas.put(pageNum, dataList);
                    saveContent(dataList, pageNum);

                    if(callback != null){
                        callback.onLoadSuccess();
                    }
                }
            }

            @Override
            public void onLoadFail() {
                ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

                mIsLoading = false;
                if(callback != null){
                    callback.onLoadFail();
                }
            }

            @Override
            public void onHasNoData() {
                ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

                mIsLoading = false;
                if(callback != null){
                    callback.onHasNoData();
                }
            }
        });
    }

    private void saveContent(final List<ContentType> dataList, final int pageNum){
        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
                    String strJson = gson.toJson(dataList, mItemType);

                    FileManager.writeGlobalFile(mPath + File.separator + pageNum, strJson.getBytes());
                }catch (Exception e){
                    LogUtil.e("save content error : " + e);
                }
            }
        });
    }

    private void makeDataForPage(final int pageNum, final Callback callback){
        ThreadManager.postTask(ThreadManager.TYPE_FILE, new Runnable() {
            @Override
            public void run() {
                byte data[] = FileManager.readGlobalFile(mPath + File.separator + pageNum);
                if(data != null && data.length != 0){
                    Gson gson = new GsonBuilder().setVersion(Constants.GSON_VERSION).create();
                    final List<ContentType> dataList = gson.fromJson(new String(data), mItemType);

                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            mPagedDatas.put(pageNum, dataList);
                            if(callback != null){
                                callback.onLoadSuccess();
                            }
                        }
                    });
                }else {
                    ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                        @Override
                        public void run() {
                            if(callback != null){
                                callback.onLoadFail();
                            }
                        }
                    });
                }
            }
        });
    }

    public interface Callback{
        void onLoadSuccess();
        void onLoadFail();
        void onHasNoData();
    }
}
