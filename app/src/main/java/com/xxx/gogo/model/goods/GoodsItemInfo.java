package com.xxx.gogo.model.goods;

public class GoodsItemInfo {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_UNDERCARRIAGE = 1;

    public String imgUrl;
    public String name;
    public String introduce;
    public double price;
    public long providerId;
    public long catId;
    public long secondaryCatId;
    public long id;

    // 是否被下架
    // 正常：STATE_NORMAL
    // 已下架：STATE_UNDERCARRIAGE
    public int state;

    public int count; //购买的数量
    public int buyCount; //购买的次数，用于常购统计
    public long buyTime; //购买时间，用于常购统计

    public String generateId(){
        return "" + providerId + catId + secondaryCatId + id;
    }
}
