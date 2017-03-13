package com.xxx.gogo.net.gson_adapter.response;

import com.google.gson.stream.JsonReader;
import com.xxx.gogo.net.NetworkResponse;

import java.io.IOException;

public class ResponseAdapterHelper {
    private Object mAdapter;

    public ResponseAdapterHelper(Object adapter){
        mAdapter = adapter;
    }

    public void read(JsonReader in, NetworkResponse.BaseResponse baseResponse)
        throws IOException{

        in.beginObject();

        while (in.hasNext()){
            switch (in.nextName()){
                case "key":
                    baseResponse.key = in.nextString();
                    break;
                case "data":
                    in.beginObject();
                    while (in.hasNext()){
                        switch (in.nextName()){
                            case "result":
                                baseResponse.result = in.nextInt();
                                break;
                            case "content":
                                ((BaseResponseAdapter)mAdapter).doRead(in);
                                break;
                        }
                    }
                    in.endObject();
                    break;
            }
        }

        in.endObject();
    }
}
