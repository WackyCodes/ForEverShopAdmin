package ean.ecom.eanshopadmin.other;

import android.content.ClipboardManager;

import ean.ecom.eanshopadmin.admin.AdminDataModel;

public class StaticValues {


    public static String APP_VERSION = "em-1-01"; // ev-1-01
    public static final String CHANNEL_ID = "e_an_shop_admin";

    public static final String DEFAULT_CITY_NAME = "BHOPAL";
    public static String CURRENT_CITY_NAME = "BHOPAL";
    public static String CURRENT_CITY_CODE = "BHOPAL";
    public static final AdminDataModel ADMIN_DATA_MODEL = new AdminDataModel();

    public static String SHOP_ID =  ADMIN_DATA_MODEL.getShopName();

    // ClipBoard..
    public static ClipboardManager clipboardManager;

    public static final int ADMIN_ROOT_FOUNDER = 11;
    public static final int ADMIN_ROOT_MANAGER = 12;
    public static final int ADMIN_SHOP_FOUNDER = 13;
    public static final int ADMIN_SHOP_MANAGER = 14;
    public static final int ADMIN_DELIVERY_BOY = 15;


    public static final int STORAGE_PERMISSION = 1;
    // Other Values....
    public static final int ID_UPDATE = 51;
    public static final int ID_DELETE = 52;
    public static final int ID_CLICK = 53;
    public static final int ID_MOVE = 54;
    public static final int ID_COPY = 55;

    // File Code
    public static final int GALLERY_CODE = 121;
    public static final int READ_EXTERNAL_MEMORY_CODE = 122;
    public static final int UPDATE_CODE = 123;
    public static final int ADD_CODE = 124;
    public static final int UPLOAD_CODE = 125;
    public static final int UPDATE_EMAIL = 1;
    public static final int UPDATE_PASS = 2;

    // Common Main Home Container...
//    public static final int BANNER_SLIDER_LAYOUT_CONTAINER = 2;
//    public static final int STRIP_AD_LAYOUT_CONTAINER = 1;
//    public static final int CATEGORY_ITEMS_LAYOUT_CONTAINER = 5;
    public static final int SHOP_ITEMS_LAYOUT_CONTAINER = 6;

    public static final int BANNER_SLIDER_CONTAINER_ITEM = 20; // not stored in DBQuery...

    // Path = "SHOPS" > (SHOP_ID) > "HOME" > (order By.. )
    public static final int SHOP_HOME_STRIP_AD_CONTAINER = 1;
    public static final int SHOP_HOME_BANNER_SLIDER_CONTAINER = 2;
    public static final int SHOP_HOME_CAT_LIST_CONTAINER = 5;
    public static final int HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER = 3;
    public static final int GRID_PRODUCTS_LAYOUT_CONTAINER = 4;

    public static final int ADD_NEW_PRODUCT_ITEM = 7;
    public static final int ADD_NEW_STRIP_AD_LAYOUT = 8;
    public static final int ADD_NEW_HORIZONTAL_ITEM_LAYOUT = 9;
    public static final int ADD_NEW_GRID_ITEM_LAYOUT = 10;

    // Banner Click Type...
    public static final int BANNER_CLICK_TYPE_WEBSITE = 1;
    public static final int BANNER_CLICK_TYPE_SHOP = 2;
    public static final int BANNER_CLICK_TYPE_CATEGORY = 3;
    public static final int BANNER_CLICK_TYPE_NONE = 4;
    public static final int BANNER_CLICK_TYPE_PRODUCT = 5;

    // Shop Veg
    public static final int SHOP_TYPE_VEG = 1;
    public static final int SHOP_TYPE_NON_VEG = 2;
    public static final int SHOP_TYPE_VEG_NON = 3;
    public static final int SHOP_TYPE_NO_SHOW = 4;

    // Veg/NonVeg Label...
    public static final int PRODUCT_LACTO_VEG = 1;
    public static final int PRODUCT_LACTO_NON_VEG = 2;
    public static final int PRODUCT_LACTO_EGG = 5;
    public static final int PRODUCT_OTHERS = 4;

    // Main Page To another...
    public static final int REQUEST_TO_ADD_SHOP = 1;
    public static final int REQUEST_TO_EDIT_SHOP = 2;
    public static final int REQUEST_TO_VIEW_SHOP = 3;
    public static final int REQUEST_TO_VIEW_HOME = 4;
    public static final int REQUEST_TO_VIEW_ORDER_LIST = 5;
    public static final int REQUEST_TO_VIEW_INCOME_RECORDS = 6;
    public static final int REQUEST_TO_VIEW_SELLING_RECORDS = 7;
    public static final int REQUEST_TO_VIEW_SHOP_RATING = 8;
    public static final int REQUEST_TO_VIEW_SERVICE_AREA = 9;
    public static final int REQUEST_TO_VIEW_FAVORITE_CUST = 10;
    public static final int REQUEST_TO_NOTIFY_NEW_ORDER = 11;
    public static final int REQUEST_TO_NOTIFICATION = 12;

    // Fragment Code And Activity Code..
    public static final int FRAGMENT_HOME = 0;
    public static final int FRAGMENT_CATEGORY = 1;

    public static final int ACTIVITY_MAIN = 9;

    // For Product List Layout view...
    public static final int VIEW_HORIZONTAL_LAYOUT = 0;
    public static final int VIEW_RECTANGLE_LAYOUT = 1;
    public static final int VIEW_GRID_LAYOUT = 2;
    public static final int VIEW_PRODUCT_SEARCH_LAYOUT = 3;

    // Order List Type for Local Use...
    public static final int ORDER_LIST_CHECK = 0;
    public static final int ORDER_LIST_NEW_ORDER = 1;
    public static final int ORDER_LIST_PREPARING = 2;
    public static final int ORDER_LIST_READY_TO_DELIVER = 3;

}
