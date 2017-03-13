package com.xxx.gogo.net.gson_adapter.response;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xxx.gogo.net.NetworkResponse;

import java.io.IOException;

public class OrderQueryStateResponseAdapter
        extends TypeAdapter<NetworkResponse.OrderQueryStateResponse>
        implements BaseResponseAdapter {
    private ResponseAdapterHelper mHelper = new ResponseAdapterHelper(this);

    private NetworkResponse.OrderQueryStateResponse mResponse =
            new NetworkResponse.OrderQueryStateResponse();

    @Override
    public void doRead (JsonReader in)throws IOException {
    }

    @Override
    public NetworkResponse.OrderQueryStateResponse read (JsonReader in)throws IOException {
        mHelper.read(in, mResponse);

        return mResponse;
    }

    @Override
    public void write (JsonWriter out, NetworkResponse.OrderQueryStateResponse value)
            throws IOException {
    }
}