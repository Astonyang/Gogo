package com.xxx.gogo.model.goods;

import com.xxx.gogo.loader.IPagedLoader;
import com.xxx.gogo.loader.IPagedLoaderCallback;
import com.xxx.gogo.utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;

public class GoodsNetDataSource implements IPagedLoader{
    private String mProviderId;
    private String mParentCatId;
    private String mCatId;

    public GoodsNetDataSource(String providerId, String parentCatId, String catId){
        mProviderId = providerId;
        mParentCatId = parentCatId;
        mCatId = catId;
    }

    @Override
    public void load(final int pageNum, String contentVersion,
                     final IPagedLoaderCallback callback) {
        ThreadManager.postTask(ThreadManager.TYPE_WORKER, new Runnable() {
            @Override
            public void run() {
                ThreadManager.postTask(ThreadManager.TYPE_UI, new Runnable() {
                    @Override
                    public void run() {
                        if(pageNum == 4){
                            callback.onHasNoData();
                        }else {
                            //callback.onLoadSuccess(null, "");
                            callback.onLoadSuccess(getGoodsList(pageNum), "");
                        }
                    }
                });
            }
        }, 1000);
    }

    @Override
    public void cancel() {

    }

    private List<GoodsItemInfo> getGoodsList(int pageNum){
        int count = pageNum == 3 ? 10 : 20;
        List<GoodsItemInfo> list = new ArrayList<>();
        for (int i = 0; i < count; ++i){
            list.add(getGoods(i + pageNum * 20));
        }
        return list;
    }

    private GoodsItemInfo getGoods(int position){
        GoodsItemInfo itemInfo = new GoodsItemInfo();
        itemInfo.id = GID[position % GID.length];
        itemInfo.imgUrl = "";
        itemInfo.introduce = "Black meal";
        itemInfo.name = "Meal (kg)";
        itemInfo.price = 98.2;
        itemInfo.providerId = Long.valueOf(mProviderId);
        itemInfo.largeImgUrl = "";
        itemInfo.spec = "mm kk dkpdo d";

        return itemInfo;
    }

    int GID[] = {100001, 100002, 100003, 100004, 100005, 100006, 100007, 100008, 100009, 1100000};

}
