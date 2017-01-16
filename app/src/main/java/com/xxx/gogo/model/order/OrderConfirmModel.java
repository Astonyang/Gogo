package com.xxx.gogo.model.order;

public class OrderConfirmModel {

    public static class ConfirmGoodsItem {
        public String name;
        public String imgUrl;
        public double unitPrice;
        public int count;
    }

    public ConfirmGoodsItem getItem(int pos){
        ConfirmGoodsItem item = new ConfirmGoodsItem();
        item.imgUrl = "";
        item.unitPrice = 99.9;
        item.count = 2;
        item.name = "Food";

        return item;
    }
}
