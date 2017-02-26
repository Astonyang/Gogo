package com.xxx.gogo.manager.order;

public class OrderEvent {
    public static class CheckOrderStateComplete{
        boolean isChanged;

        public CheckOrderStateComplete(boolean changed){
            isChanged = changed;
        }
    }
}
