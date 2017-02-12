package com.xxx.gogo.model.order;

import java.util.ArrayList;

public class OrderNetDataSource {
    public void add(ArrayList<OrderItemInfo> orderList,
                    ArrayList<ArrayList<OrderItemDetailInfo>> detailList){

        int i = 0;
        for (OrderItemInfo orderItemInfo : orderList){
            //addItem(orderItemInfo, detailList.get(i++));
        }
    }

    public void delete(String orderId){

    }

    public void load(){
    }
}
