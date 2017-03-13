package com.xxx.gogo.net.gson_adapter.response;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xxx.gogo.net.NetworkResponse;

import java.io.IOException;

public class GoodsLoadResponseAdapter
        extends TypeAdapter<NetworkResponse.GoodsLoadResponse>
        implements BaseResponseAdapter {
    private ResponseAdapterHelper mHelper = new ResponseAdapterHelper(this);

    private NetworkResponse.GoodsLoadResponse mResponse =
            new NetworkResponse.GoodsLoadResponse();

    @Override
    public void doRead (JsonReader in)throws IOException {
    }

    @Override
    public NetworkResponse.GoodsLoadResponse read (JsonReader in)throws IOException {
        mHelper.read(in, mResponse);

        return mResponse;
    }

    @Override
    public void write (JsonWriter out, NetworkResponse.GoodsLoadResponse value)
            throws IOException {
    }
}
