package com.xxx.gogo.loader;

import java.util.List;

public interface IPagedLoaderCallback<ContentType> {
    void onLoadSuccess(List<ContentType> dataList, String contentVersion);

    void onHasNoData();

    void onLoadFail();
}
