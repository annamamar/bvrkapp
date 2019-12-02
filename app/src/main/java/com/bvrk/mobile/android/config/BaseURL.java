package com.bvrk.mobile.android.config;

    public class BaseURL {

    public static final String DO_APP_URL = "https://maxseat.tkdemo.xyz/";
    public static final String DO_APP_URL_REST = "test/";
    public static final String DO_APP_URL_REST_URL = DO_APP_URL+DO_APP_URL_REST;

    public static final String ORDER_TYPE_BULK = "Bulk";
    public static final String ORDER_TYPE_SINGLE = "Single";

    public static final String ORDER_TYPE_CLOSED  = "closed";
    public static final String ORDER_TYPE_PENDING = "pending";

    public static final String ORDER_USER_ADMIN = "orders_adapter";
    public static final String ORDER_USER_CLIENT = "client_adapter";
    public static final String ORDER_USER_VENDOR  = "vendor_adapter";


    public static final String ORDER_CLIENT_ADD = "Add New Client";
    public static final String ORDER_CLIENT_SELECT = "Select Client";

    public static final String ORDER_VENDOR_ADD = "Add New Vendor";
    public static final String ORDER_VENDOR_SELECT = "Select Vendor";


    public static final String DO_APP_REG_USER = DO_APP_URL_REST_URL+"reguser";
    public static final String DO_APP_CHECK_USER = DO_APP_URL_REST_URL+"checkuserkey";

    public static final String DO_APP_GET_MODELS_INFO = DO_APP_URL_REST_URL+"modelsinfo";

    public static final String DO_APP_GET_IMAGE = DO_APP_URL_REST_URL+"getimage";

    public static final String DO_APP_GET_MODEL = DO_APP_URL_REST_URL+"getmodel";
    public static final String DO_APP_SET_USER_DATA = DO_APP_URL_REST_URL+"loaduser";

    // LOCAL TEST
    public static final String LC_APP_URL = "http://192.168.1.90/xp/maxseat/";
    //public static final String LC_APP_URL = "http://192.168.43.50/xp/maxseat/";
    public static final String LC_APP_URL_REST = "test/";
    public static final String LC_APP_URL_REST_URL = LC_APP_URL+LC_APP_URL_REST;

    //
    public static final String LC_APP_REG_USER = LC_APP_URL_REST_URL+"reguser";
    public static final String LC_APP_CHECK_USER = LC_APP_URL_REST_URL+"checkuserkey";

    public static final String LC_APP_GET_MODELS_INFO = LC_APP_URL_REST_URL+"modelsinfo";

    public static final String LC_APP_GET_IMAGE = LC_APP_URL_REST_URL+"getimage";

    public static final String LC_APP_GET_MODEL = LC_APP_URL_REST_URL+"getmodel";
    public static final String LC_APP_SET_USER_DATA = LC_APP_URL_REST_URL+"loaduser";

}




