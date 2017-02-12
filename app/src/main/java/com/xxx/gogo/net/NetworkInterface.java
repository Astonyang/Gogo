package com.xxx.gogo.net;

public interface NetworkInterface {
    //String URL_PREFIX = "http://101.200.175.195:85/purchase/api/v1/mobile/customer/";
    String URL_PREFIX = "";

    String USER_FIND_PASSWORD_URL = URL_PREFIX + "";
    String USER_REGISTER_URL = URL_PREFIX + "regist.php";
    String USER_LOGIN_URL = URL_PREFIX + "login.php";

    //String USER_LOGIN_URL = "http://www.baidu.com";


    String QUERY_STORE_INFO_URL = URL_PREFIX + "getdetailinfo.php";

    String SEARCH_PROVIDER_URL = URL_PREFIX + "searchprovider.php";
    String LOAD_PROVIDER_URL = URL_PREFIX + "getproviderlist.php";
    String ADD_PROVIDER_URL = URL_PREFIX + "manageprovider.php";
    String DELETE_PROVIDER_URL = URL_PREFIX + "manageprovider.php";

    String FAVORITE_URL = URL_PREFIX + "";

    String COMMIT_ORDER_URL = URL_PREFIX + "order.php";
    String CANCEL_ORDER_URL = URL_PREFIX + "";
}
