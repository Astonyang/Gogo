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

    public int count; //购买的数量
    public int buyCount; //购买的次数，用于常购统计
    public long buyTime; //购买时间，用于常购统计

    public String generateId(){
        return "" + providerId + catId + secondaryCatId + id;
    }
}
