package com.xxx.gogo.net.gson_adapter.response;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xxx.gogo.net.NetworkResponse;

import java.io.IOException;

public class GoodsCategoryLoadResponseAdapter
        extends TypeAdapter<NetworkResponse.GoodsCategoryLoadResponse>
        implements BaseResponseAdapter {
    private ResponseAdapterHelper mHelper = new ResponseAdapterHelper(this);

    private NetworkResponse.GoodsCategoryLoadResponse mResponse =
            new NetworkResponse.GoodsCategoryLoadResponse();

    @Override
    public void doRead (JsonReader in)throws IOException {
    }

    @Override
    public NetworkResponse.GoodsCategoryLoadResponse read (JsonReader in)throws IOException {
        mHelper.read(in, mResponse);

        return mResponse;
    }

    @Override
    public void write (JsonWriter out, NetworkResponse.GoodsCategoryLoadResponse value)
            throws IOException {
    }
}