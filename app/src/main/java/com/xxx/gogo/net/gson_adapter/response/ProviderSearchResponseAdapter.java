package com.xxx.gogo.net.gson_adapter.response;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xxx.gogo.net.NetworkResponse;

import java.io.IOException;

public class ProviderSearchResponseAdapter
        extends TypeAdapter<NetworkResponse.ProviderSearchResponse>
        implements BaseResponseAdapter{

    private ResponseAdapterHelper mHelper = new ResponseAdapterHelper(this);

    private NetworkResponse.ProviderSearchResponse mResponse =
            new NetworkResponse.ProviderSearchResponse();

    @Override
    public void doRead(JsonReader in) throws IOException {
    }

    @Override
    public NetworkResponse.ProviderSearchResponse read(JsonReader in) throws IOException {
        mHelper.read(in, mResponse);

        return mResponse;
    }

    @Override
    public void write(JsonWriter out, NetworkResponse.ProviderSearchResponse value)
            throws IOException {

    }
}
