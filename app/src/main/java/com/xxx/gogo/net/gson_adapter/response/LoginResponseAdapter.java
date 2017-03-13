package com.xxx.gogo.net.gson_adapter.response;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xxx.gogo.net.NetworkResponse;

import java.io.IOException;

public class LoginResponseAdapter
        extends TypeAdapter<NetworkResponse.LoginResponse>
        implements BaseResponseAdapter{

    private ResponseAdapterHelper mHelper = new ResponseAdapterHelper(this);

    private NetworkResponse.LoginResponse mResponse;

    @Override
    public NetworkResponse.LoginResponse read(JsonReader in) throws IOException {

        mResponse = new NetworkResponse.LoginResponse();

        mHelper.read(in, mResponse);

        return mResponse;
    }

    @Override
    public void write(JsonWriter out, NetworkResponse.LoginResponse value)
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
