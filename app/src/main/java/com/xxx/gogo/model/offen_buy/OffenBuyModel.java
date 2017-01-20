package com.xxx.gogo.model.offen_buy;

import com.xxx.gogo.model.goods.GoodsItemInfo;

public class OffenBuyModel {
    String[] groupStrings = {"牛肉", "羊肉", "鸡肉", "猪肉"};
    String[][] childStrings = {
            {"唐三藏", "孙悟空", "猪八戒", "沙和尚"},
            {"宋江", "林冲", "李逵", "鲁智深"},
            {"曹操", "刘备", "孙权", "诸葛亮", "周瑜"},
            {"贾宝玉", "林黛玉", "薛宝钗", "王熙凤"}
    };
    long[] GOODS_ID = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

    private static class InstanceHolder{
        private static OffenBuyModel sInstance = new OffenBuyModel();
    }

    public static OffenBuyModel getInstance(){
        return InstanceHolder.sInstance;
    }

    private OffenBuyModel(){
    }

    /**
     * must be called after ShopCartModel.load
     */
    public void load(){

    }

    public int getGroupCount() {
        return groupStrings.length;
    }

    public int getChildrenCount(int groupPosition) {
        return childStrings[groupPosition].length;
    }

    public Object getGroup(int groupPosition) {
        return groupStrings[groupPosition];
    }

    public Object getChild(int groupPosition, int childPosition) {
        return childStrings[groupPosition][childPosition];
    }

    public GoodsItemInfo getGoodsItem(int pos){
        GoodsItemInfo itemInfo = new GoodsItemInfo();
        itemInfo.id = GOODS_ID[pos];
        itemInfo.imgUrl = "";
        itemInfo.introduce = "Black meal";
        itemInfo.name = "Meal (kg)";
        itemInfo.price = 100;
        itemInfo.providerId = 10001;

        return itemInfo;
    }
}
