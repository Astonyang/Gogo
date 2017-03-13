package com.xxx.gogo.net.gson_adapter.request;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xxx.gogo.net.NetworkRequest;

import java.io.IOException;

public class OrderCommitRequestAdapter
        extends TypeAdapter<NetworkRequest.OrderCommitRequest>
        implements BaseRequestAdapter {

    @Override
    public void doWrite(JsonWriter out, NetworkRequest.UserRelatedRequest value)
            throws IOException {
        NetworkRequest.OrderCommitRequest realValue = (NetworkRequest.OrderCommitRequest)value;

        out.beginObject();

        //out.name("list").

        out.endObject();
    }

    @Override
    public NetworkRequest.OrderCommitRequest read(JsonReader in)
            throws IOException {
        return null;
    }

    @Override
    public void write(JsonWriter out, NetworkRequest.OrderCommitRequest value)
            throws IOException {
        RequestAdapterHelper.write(out, value, this);
    }
}
