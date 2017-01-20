package com.xxx.gogo.model.goods;

public class GoodsItemInfo {
    public String imgUrl;
    public String name;
    public String introduce;
    public double price;
    public long providerId;
    public long catId;
    public long secondaryCatId;
    public long id;

    public int count;

    public String generateId(){
        return "" + providerId + catId + secondaryCatId + id;
    }
}
