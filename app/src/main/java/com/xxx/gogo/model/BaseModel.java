package com.xxx.gogo.model;

public class BaseModel {
    public static final int STATE_INIT = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_LOADED = 2;

    protected int mState;

    protected BaseModel(){
        mState = STATE_INIT;
    }

    public int getState(){
        return mState;
    }
}
