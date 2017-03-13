package com.xxx.gogo.net.gson_adapter.request;

import com.google.gson.stream.JsonWriter;
import com.xxx.gogo.net.NetworkRequest;

import java.io.IOException;

public interface BaseRequestAdapter {
    void doWrite(JsonWriter out, NetworkRequest.UserRelatedRequest value) throws IOException;

}
