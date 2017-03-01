package com.xxx.gogo.loader;

public interface IPagedLoader {
    void load(int pageNum, String contentVersion, IPagedLoaderCallback callback);
    void cancel();
}
