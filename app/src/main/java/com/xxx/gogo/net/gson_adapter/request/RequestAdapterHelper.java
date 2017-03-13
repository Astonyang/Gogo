package com.xxx.gogo.net.gson_adapter.request;

import com.google.gson.stream.JsonWriter;
import com.xxx.gogo.BuildConfig;
import com.xxx.gogo.net.NetworkRequest;

import java.io.IOException;

public class RequestAdapterHelper {
    public static void write(JsonWriter out,
                             NetworkRequest.UserRelatedRequest value,
                             Object adapter) throws IOException {
        out.beginObject();

        out.name("platform").value("2");
        out.name("version").value(BuildConfig.VERSION_NAME);
        out.name("user_id").value(value.userId);
        out.name("password").value(value.password);

        ((BaseRequestAdapter)adapter).doWrite(out, value);

        out.endObject();
    }
}
