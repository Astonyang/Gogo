package com.xxx.gogo.manager;

import com.squareup.otto.Bus;

public class BusFactory {
    private static final Bus bus = new Bus();

    public static Bus getBus(){
        return bus;
    }
}
