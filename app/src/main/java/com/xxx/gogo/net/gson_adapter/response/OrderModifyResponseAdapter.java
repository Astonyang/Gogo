package com.xxx.gogo.net.gson_adapter.response;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xxx.gogo.net.NetworkResponse;

import java.io.IOException;

public class OrderModifyResponseAdapter
        extends TypeAdapter<NetworkResponse.OrderModifyResponse>
        implements BaseResponseAdapter {
    private ResponseAdapterHelper mHelper = new ResponseAdapterHelper(this);

    private NetworkResponse.OrderModifyResponse mResponse =
            new NetworkResponse.OrderModifyResponse();

    @Override
    public void doRead (JsonReader in)throws IOException {
    }

    @Override
    public NetworkResponse.OrderModifyResponse read (JsonReader in)throws IOException {
        mHelper.read(in, mResponse);

        return mResponse;
    }

    @Override
    public void write (JsonWriter out, NetworkResponse.OrderModifyResponse value)
            throws IOException {
    }
}