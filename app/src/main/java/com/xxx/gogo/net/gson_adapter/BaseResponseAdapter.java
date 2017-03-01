package com.xxx.gogo.net.gson_adapter;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

public interface BaseResponseAdapter {
    void doRead(JsonReader in) throws IOException;
}
