package com.xxx.gogo.net.gson_adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xxx.gogo.net.NetworkResponse;

import java.io.IOException;

public class RegisterResponseAdapter
        extends TypeAdapter<NetworkResponse.RegisterResponse>
        implements BaseResponseAdapter{

    private ResponseAdapterHelper<RegisterResponseAdapter> mHelper =
            new ResponseAdapterHelper<>(this);

    private NetworkResponse.RegisterResponse mResponse;

    @Override
    public NetworkResponse.RegisterResponse read(JsonReader in) throws IOException {

        mResponse = new NetworkResponse.RegisterResponse();

        mHelper.read(in, mResponse);

        return mResponse;
    }

    @Override
    public void write(JsonWriter out, NetworkResponse.RegisterResponse value)
            throws IOException {

    }

    @Override
    public void doRead(JsonReader in) throws IOException {
        in.beginObject();

        if(in.nextName().equals("customer_id")){
            mResponse.userId = in.nextString();
        }

        in.endObject();
    }
}
