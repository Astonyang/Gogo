package com.xxx.gogo.model.goods;

import com.xxx.gogo.model.BaseModel;
import com.xxx.gogo.utils.ThreadManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GoodsCategoryModel extends BaseModel
        implements GoodsCategoryLocalDataSource.Callback{
    private GoodsCategoryLocalDataSource mDataSource;
    private WeakReference<Callback> mCb;

    private List<GoodsCategoryItemInfo> mItemList;

    GoodsCategoryModel(Callback callback, String providerId){
        mCb = new WeakReference<>(callback);
        mItemList = new ArrayList<>();
        mDataSource = new GoodsCategoryLocalDataSource(this, providerId);
    }

    public void load(){
        if(mState != STATE_LOADING){
            mDataSource.load();
            mState = STATE_LOADING;
        }
    }

    public int getCategoryCount(){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        return mItemList.size();
    }

    public String getCategoryName(int position){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        return mItemList.get(position).name;
    }

    public String getCategoryId(int position){
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        return String.valueOf(mItemList.get(position).id);
    }

    @Override
    public void onFail(boolean fromLocal) {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        if(mCb != null && mCb.get() != null && mItemList.isEmpty() && !fromLocal){
            mCb.get().onFail();
        }
        mState = STATE_LOADED;
    }

    @Override
    public void onSuccess(List<GoodsCategoryItemInfo> infoList) {
        ThreadManager.currentlyOn(ThreadManager.TYPE_UI);

        mItemList = infoList;
        if(mCb != null && mCb.get() != null){
            mCb.get().onSuccess();
        }
        mState = STATE_LOADED;
    }

    public interface Callback{
        void onSuccess();
        void onFail();
    }
}
