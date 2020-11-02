package ean.ecom.eanshopadmin.database;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.admin.ShopDataModelClass;
import ean.ecom.eanshopadmin.home.HomeCatListModel;
import ean.ecom.eanshopadmin.home.HomeFragment;
import ean.ecom.eanshopadmin.home.HomeListModel;
import ean.ecom.eanshopadmin.main.orderlist.OrderListFragment;
import ean.ecom.eanshopadmin.main.orderlist.OrderListModel;
import ean.ecom.eanshopadmin.main.orderlist.OrderProductsModel;
import ean.ecom.eanshopadmin.model.BannerModel;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.StaticMethods;
import ean.ecom.eanshopadmin.product.ProductModel;

import static ean.ecom.eanshopadmin.MainActivity.badgeOrderCount;
import static ean.ecom.eanshopadmin.MainActivity.mainActivity;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.GRID_PRODUCTS_LAYOUT_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.NOT_VERIFIED;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_PREPARING;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_READY_TO_DELIVER;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_NOTIFY_NEW_ORDER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_BANNER_SLIDER_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_CAT_LIST_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_STRIP_AD_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

public class DBQuery {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public  static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

//    public static List<String>
    // Home List...
    public static List<HomeCatListModel> homeCatListModelList = new ArrayList <>();
    private static List<HomeListModel> homeListModelList;
    private static List<BannerModel> bannerModelList;
    // To Understand list inside list...
     /*

    < List>
        List<HomeCatListModel> homeCatListModelList
        catID

        < List>
            List<HomeListModel> homeListModelList
             < List>
                List<ProductModel> productModelList
                productLayId
                Product ID List
                layoutGridTitle

            </List>

        </List>

    </List>

     */
     // Order List...
    public static List <OrderListModel> orderListModelList = new ArrayList <>();
    private static OrderListModel orderListModel;
    private static List<OrderProductsModel> orderSubList;

    // new Order List...
    public static List <OrderListModel> newOrderList = new ArrayList <>();
    public static List <OrderListModel> preparingOrderList = new ArrayList <>();
    public static List <OrderListModel> readyToDeliveredList = new ArrayList <>();

    // yyyyMMddHHmmss
    private static final String fromIndex = StaticMethods.getDateUnder31(2).replace( "/", "" ).trim() + "000000";


     private static CollectionReference getShopCollectionRef(String collectionName){
         return firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID ).collection( collectionName );
     }

    // Get City List... // Not Required...
    public static void getCityListQuery(){ /**
        areaCodeCityModelList.clear();
        firebaseFirestore.collection( "AREA_CODE" ).orderBy( "area_code" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String areaCode = String.valueOf( (long)documentSnapshot.get( "area_code" ) );
                        String areaName = documentSnapshot.get( "area_name" ).toString();
                        String cityName = documentSnapshot.get( "area_city" ).toString();
                        String cityCode = documentSnapshot.get( "area_city_code" ).toString();

                        areaCodeCityModelList.add( new AreaCodeCityModel( areaCode, areaName, cityName, cityCode ) );
                    }
                    if (MainActivity.selectAreaCityAdaptor != null){
                        MainActivity.selectAreaCityAdaptor.notifyDataSetChanged();
                    }

                }else{

                }
            }
        } ); */
    }

    // Get Shop Data...
    public static void getShopData(final String shopID){
        firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .get().addOnCompleteListener( task -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();

                        // Add All the shop Data into model...
                        SHOP_DATA_MODEL = documentSnapshot.toObject( ShopDataModelClass.class );

                        if (mainActivity != null){
                            mainActivity.getSupportActionBar().setTitle( SHOP_DATA_MODEL.getShop_name() );
                        }
                    }
                    else{
                        // Failed...
                    }
                } );

    }

    // Query to Load Fragment Data like homepage items etc...
    public static void getHomeCatListQuery(final Context context
            , @Nullable final Dialog dialog,  @Nullable final SwipeRefreshLayout swipeRefreshLayout
            , final String categoryID, final int index ) {
        //------------------------------------------------------
        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( categoryID ).orderBy( "index" ).get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {

                        if (task.isSuccessful()){
                            homeListModelList = new ArrayList <>();
                            HomeListModel homeListModel;
                            // add Data from snapshot...
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                int viewType = Integer.parseInt( String.valueOf( (long)documentSnapshot.get( "type" ) ) );
//                            showToast( context, "City : " + userCityName );
                                if ( viewType == SHOP_HOME_BANNER_SLIDER_CONTAINER) {
                                    /** add banners slider */
                                    String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                    Boolean visibility = documentSnapshot.getBoolean( "visibility" );
                                    bannerModelList = new ArrayList <>();
                                    long no_of_banners = (long) documentSnapshot.get( "no_of_banners" );
                                    for (long i = 1; i <= no_of_banners; i++) {
                                        // access the banners from database...
//                                        int clickType, String clickID, String imageLink, String nameOrExtraText, String deleteID

                                        bannerModelList.add( new BannerModel(
                                                Integer.parseInt( String.valueOf((long)documentSnapshot.get( "banner_click_type_" + i )) ),
                                                documentSnapshot.get( "banner_click_id_" + i ).toString(),
                                                documentSnapshot.get( "banner_" + i ).toString(),
                                                "Extra_Text",
                                                documentSnapshot.get( "delete_id_" + i ).toString() ));
                                    }
                                    // add the banners list in the home recycler list...
//                                    homeListModelList.add( new HomeListModel( SHOP_HOME_BANNER_SLIDER_CONTAINER, layout_id, visibility, bannerModelList ) );
                                    homeListModel = new HomeListModel( SHOP_HOME_BANNER_SLIDER_CONTAINER, layout_id, visibility, bannerModelList );
                                    if (homeCatListModelList.size() > 0){
                                        homeCatListModelList.get( index ).getHomeListModelList().add( homeListModel );
                                    }

                                } else
                                if ( viewType == SHOP_HOME_STRIP_AD_CONTAINER) {
                                    /**  for strip and banner ad */
                                    String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                    BannerModel bannerModel = new BannerModel(
                                            Integer.parseInt( String.valueOf( (long) documentSnapshot.get( "banner_click_type" ) ) ),
                                            documentSnapshot.get( "banner_click_id" ).toString(),
                                            documentSnapshot.get( "banner_image" ).toString(),
                                            documentSnapshot.get( "extra_text" ).toString(),
                                            documentSnapshot.get( "delete_id" ).toString()
                                            );
//                                    homeListModelList.add( new HomeListModel( SHOP_HOME_STRIP_AD_CONTAINER, layout_id, bannerModel ) );
                                    homeListModel = new HomeListModel( SHOP_HOME_STRIP_AD_CONTAINER, layout_id, bannerModel );
                                    if (homeCatListModelList.size() > 0){
                                        homeCatListModelList.get( index ).getHomeListModelList().add( homeListModel );
                                    }
                                } else
                                if ( viewType == HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER ||
                                        viewType == GRID_PRODUCTS_LAYOUT_CONTAINER) {
                                    /** : for horizontal products */
                                    String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                    String layout_title = documentSnapshot.get( "layout_title" ).toString();
//                                    productList = new ArrayList <>(); TODO:
                                    List<String> hrAndGridProductIdList = new ArrayList <>();
                                    long no_of_products = (long) documentSnapshot.get( "no_of_products" );
                                    for (long i = 1; i < no_of_products + 1; i++) {
                                        // Load Product Data List After set Adaptor... on View Time...
                                        // Below we load only product Id...
                                        hrAndGridProductIdList.add( documentSnapshot.get( "product_id_" + i ).toString() );
                                    }
                                    // add list in home fragment model
//                                    homeListModelList.add( new HomeListModel( viewType, layout_id, layout_title, hrAndGridProductIdList,
//                                            new ArrayList <ProductModel>() ) );
                                    homeListModel =  new HomeListModel( viewType, layout_id, layout_title, hrAndGridProductIdList,
                                            new ArrayList <ProductModel>() );
                                    if (homeCatListModelList.size() > 0){
                                        homeCatListModelList.get( index ).getHomeListModelList().add( homeListModel );
                                    }

                                } else
                                if ( viewType == SHOP_HOME_CAT_LIST_CONTAINER) { // TODO : Create New Type For SubCategory...
                                    String layout_id = documentSnapshot.get( "layout_id" ).toString();
                                    Boolean visibility = documentSnapshot.getBoolean( "visibility" );
                                    bannerModelList = new ArrayList <>();
                                    long no_of_cat = (long) documentSnapshot.get( "no_of_cat" );
                                    for (long i = 1; i <= no_of_cat; i++) {
                                        // access the banners from database...
                                        bannerModelList.add( new BannerModel(
                                                -1,
                                                documentSnapshot.get( "cat_id_"+i ).toString(),
                                                documentSnapshot.get( "cat_image_"+i ).toString(),
                                                documentSnapshot.get( "cat_name_"+i ).toString(),
                                                "delete ID" ));
                                        // To add Category id and Category Name in homeCatListModelList(Main List)...
                                        if ( swipeRefreshLayout == null){
                                            homeCatListModelList.add( new HomeCatListModel( documentSnapshot.get( "cat_id_"+i ).toString()
                                                    , documentSnapshot.get( "cat_name_"+i ).toString(), new ArrayList <HomeListModel>()  ) );
                                        }
                                    }
                                    // add the banners list in the home recycler list...
//                                    homeListModelList.add( new HomeListModel( SHOP_HOME_CAT_LIST_CONTAINER, layout_id, visibility, bannerModelList ) );
                                    homeListModel = new HomeListModel( SHOP_HOME_CAT_LIST_CONTAINER, layout_id, visibility, bannerModelList );
                                    if (homeCatListModelList.size() > 0){
                                        homeCatListModelList.get( index ).getHomeListModelList().add( homeListModel );
                                    }
                                }
                                // Load data without waste of time...
//                                homeFragmentAdaptor.notifyDataSetChanged();
//                                if (homeCatListModelList.size() > 0){
//                                    homeCatListModelList.get( index ).setHomeListModelList( homeListModelList );
//                                }
                                if (HomeFragment.homePageAdaptor != null){
                                    HomeFragment.homePageAdaptor.notifyDataSetChanged();
                                }
                            }
                            // At the last... When We add All Data in homeListModelList...
//                            homeCatListModelList.add( index, new HomeCatListModel( categoryID, "Cat_Name",  homeListModelList ) );
                            // Or..
//                            if (homeCatListModelList.size() > 0){
//                                homeCatListModelList.get( index ).setHomeListModelList( homeListModelList );
//                            }
                            if (HomeFragment.homePageAdaptor != null){
                                HomeFragment.homePageAdaptor.notifyDataSetChanged();
                            }

                            // $ Changes...
                            if (dialog!=null){
                                dialog.dismiss();
                            }

                            //swipeRefreshLayout
                            if (swipeRefreshLayout!=null){
                                swipeRefreshLayout.setRefreshing( false );
                            }

                            if (HomeFragment.homePageAdaptor != null){
                                HomeFragment.homePageAdaptor.notifyDataSetChanged();
                            }

                        }
                        else{
                            // $ Changes...
                            if (dialog!=null){
                                dialog.dismiss();
                            }

                            //swipeRefreshLayout
                            if (swipeRefreshLayout!=null){
                                swipeRefreshLayout.setRefreshing( false );
                            }

                        }

                    }
                } );


    }

    public static void getOrderListQuery(@Nullable final Dialog dialog, String fromDate){

        getShopCollectionRef( "ORDERS" )
                .orderBy( "index", Query.Direction.DESCENDING )
                .whereGreaterThanOrEqualTo( "index", fromDate )
//                .orderBy( "order_date" )
//                .whereGreaterThanOrEqualTo( "order_date", fromDate )
                .get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        // Task...
                        if (task.isSuccessful()){ // && task.getResult() != null
                            // Clear All Order from List...
                            orderListModelList.clear();
                            for ( DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                // Assign new OrderListModel...
                                orderListModel = new OrderListModel();

                                orderListModel.setOrderID( documentSnapshot.get( "order_id" ).toString() );
                                orderListModel.setDeliveryStatus( documentSnapshot.get( "delivery_status" ).toString() );
                                orderListModel.setPayMode( documentSnapshot.get( "pay_mode" ).toString() );

                                orderListModel.setDeliveryCharge( documentSnapshot.get( "delivery_charge" ).toString() );
                                // billing_amounts
                                orderListModel.setBillingAmounts( documentSnapshot.get( "billing_amounts" ).toString() );
                                // total_amounts
                                orderListModel.setProductAmounts( documentSnapshot.get( "total_amounts" ).toString() );

                                orderListModel.setCustAuthID( documentSnapshot.get( "order_by_auth_id" ).toString() );
                                orderListModel.setCustName( documentSnapshot.get( "order_by_name" ).toString() );
                                orderListModel.setCustMobile( documentSnapshot.get( "order_by_mobile" ).toString() );

                                orderListModel.setShippingName( documentSnapshot.get( "order_accepted_by" ).toString() );
                                orderListModel.setShippingAddress( documentSnapshot.get( "order_delivery_address" ).toString() );
                                orderListModel.setShippingPinCode( documentSnapshot.get( "order_delivery_pin" ).toString() );

                                orderListModel.setOrderDate( documentSnapshot.get( "order_date" ).toString() );
                                orderListModel.setOrderDay( documentSnapshot.get( "order_day" ).toString() );
                                orderListModel.setOrderTime( documentSnapshot.get( "order_time" ).toString() );

                                orderListModel.setDeliverySchedule( documentSnapshot.get( "delivery_schedule_time" ).toString() );

//                                long no_of_products = (long)documentSnapshot.get( "no_of_products" );

//                                for (long ind = 0; ind < no_of_products; ind++){
//                                    orderSubList.add( new OrderProductItemModel(
//                                            documentSnapshot.get( "product_id_"+ind ).toString(),
//                                            documentSnapshot.get( "product_image_"+ind ).toString(),
//                                            documentSnapshot.get( "product_name_"+ind ).toString(),
//                                            documentSnapshot.get( "product_price_"+ind ).toString(),
//                                            documentSnapshot.get( "product_qty_"+ind ).toString()
//                                    ) );
//                                }
                                orderSubList =  (ArrayList <OrderProductsModel>) documentSnapshot.getData().get( "products_list" );

                                orderListModel.setOrderProductItemsList( orderSubList );

                                // add Model Item in the List...
                                orderListModelList.add( orderListModel );

                                // TODO: add data in the list...
                                if (OrderListFragment.orderListAdaptor != null){
                                    OrderListFragment.orderListAdaptor.notifyDataSetChanged();
                                }

                                if (dialog != null){
                                    dialog.dismiss();
                                }
                            }
                            if (orderListModelList.size() == 0){
                                OrderListFragment.no_order_text.setVisibility( View.VISIBLE );
                                OrderListFragment.no_order_text.setText( "No Order Found!" );
                            }else{
                                OrderListFragment.no_order_text.setVisibility( View.GONE );
                            }

                        }else{
                            // Failed...
                        }
                        if (dialog != null){
                            dialog.dismiss();
                        }
                    }
                } );

    }

    // ------------------------  New Order Query ----------------------------
    // Get New Order List...
    public static ListenerRegistration newOrderNotificationLR;
    public static void getNewOrderQuery(final Context context){

//       showToast(context, fromIndex);

        newOrderNotificationLR = getShopCollectionRef( "ORDERS" )
                .orderBy( "index", Query.Direction.DESCENDING )
                .whereGreaterThanOrEqualTo( "index", fromIndex )
//                .limit(  ) //order_time. order_date
                .addSnapshotListener( new EventListener <QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null ) {
                            int cartCount = 0;
//                            newOrderList.clear();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                String deliveryStatus = documentSnapshot.get( "delivery_status" ).toString();
                                if (deliveryStatus.toUpperCase().equals( "WAITING" )){
                                    // Assign new OrderListModel...
                                    orderListModel = new OrderListModel();

                                    orderListModel.setDeliveryStatus( deliveryStatus );

                                    orderListModel.setOrderID( documentSnapshot.get( "order_id" ).toString() );
                                    orderListModel.setDeliveryStatus( documentSnapshot.get( "delivery_status" ).toString() );
                                    orderListModel.setPayMode( documentSnapshot.get( "pay_mode" ).toString() );

                                    orderListModel.setDeliveryCharge( documentSnapshot.get( "delivery_charge" ).toString() );
                                    // billing_amounts
                                    orderListModel.setBillingAmounts( documentSnapshot.get( "billing_amounts" ).toString() );
                                    // total_amounts
                                    orderListModel.setProductAmounts( documentSnapshot.get( "total_amounts" ).toString() );

                                    orderListModel.setCustAuthID( documentSnapshot.get( "order_by_auth_id" ).toString() );
                                    orderListModel.setCustName( documentSnapshot.get( "order_by_name" ).toString() );
                                    orderListModel.setCustMobile( documentSnapshot.get( "order_by_mobile" ).toString() );

                                    orderListModel.setShippingName( documentSnapshot.get( "order_accepted_by" ).toString() );
                                    orderListModel.setShippingAddress( documentSnapshot.get( "order_delivery_address" ).toString() );
                                    orderListModel.setShippingPinCode( documentSnapshot.get( "order_delivery_pin" ).toString() );

                                    orderListModel.setOrderDate( documentSnapshot.get( "order_date" ).toString() );
                                    orderListModel.setOrderDay( documentSnapshot.get( "order_day" ).toString() );
                                    orderListModel.setOrderTime( documentSnapshot.get( "order_time" ).toString() );

                                    orderListModel.setDeliverySchedule( documentSnapshot.get( "delivery_schedule_time" ).toString() );

//                                    long no_of_products = (long)documentSnapshot.get( "no_of_products" );

                                   /* for (long ind = 0; ind < no_of_products; ind++){
                                        OrderProductItemModel orderProductItemModel = new OrderProductItemModel(
                                                documentSnapshot.get( "product_id_"+ind ).toString(),
                                                documentSnapshot.get( "product_image_"+ind ).toString(),
                                                documentSnapshot.get( "product_name_"+ind ).toString(),
                                                documentSnapshot.get( "product_price_"+ind ).toString(),
                                                documentSnapshot.get( "product_qty_"+ind ).toString()
                                        );

                                        if (documentSnapshot.get( "product_mrp_"+ind ) != null){
                                            orderProductItemModel.setProductMRP(  documentSnapshot.get( "product_mrp_"+ind ).toString() );
                                        }else{
                                            orderProductItemModel.setProductMRP( "" );
                                        }

                                        if (documentSnapshot.get( "product_weight_"+ind ) != null){
                                            orderProductItemModel.setProductWeight(  documentSnapshot.get( "product_weight_"+ind ).toString() );
                                        }else{
                                            orderProductItemModel.setProductWeight( "NONE" );
                                        }

                                        orderSubList.add( orderProductItemModel );
                                    } */

                                    orderSubList =  (ArrayList <OrderProductsModel>) documentSnapshot.getData().get( "products_list" );

                                    orderListModel.setOrderProductItemsList( orderSubList );

//                                    newOrderList.add( orderListModel );
                                    // add Model in the new Order List...
//                                    if (!newOrderList.contains( orderListModel )){
//                                        newOrderList.add( orderListModel );
//                                        cartCount++;
//                                    }
                                    boolean isExist = false;
                                    for (OrderListModel tempModel : newOrderList){
                                        if (tempModel.getOrderID().equals( orderListModel.getOrderID() )){
                                            isExist = true;
                                            break;
                                        }
                                    }

                                    if (!isExist){
                                        newOrderList.add( orderListModel );
                                        cartCount++;
                                    }

                                }

                                if (badgeOrderCount != null)
                                    if (newOrderList.size()>0){
                                        badgeOrderCount.setVisibility( View.VISIBLE );
                                        badgeOrderCount.setText( newOrderList.size() + "" );
                                    }else{
                                        badgeOrderCount.setVisibility( View.GONE );
                                    }
                            }
                            if ( context!=null && cartCount > 0){
                                DialogsClass.setAlarmOnNotification( context, "New Order",
                                        "You have "+ cartCount +" new Orders!", REQUEST_TO_NOTIFY_NEW_ORDER );
                            }

                        }
                    }
                } );


    }
    // Get Preparing & Ready To Delivered Order List...
    private static void getAcceptedOrderList( final int getListType ){

//        case ORDER_LIST_NEW_ORDER:
//        case ORDER_LIST_PREPARING:
//        case ORDER_LIST_READY_TO_DELIVER:

        getShopCollectionRef( "ORDERS" )
                .orderBy( "index", Query.Direction.DESCENDING )
                .whereGreaterThanOrEqualTo( "index", fromIndex )
//                .limit(  ) //order_time. order_date
                .get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if ( task.isSuccessful() ) {
                            OrderListModel orderListModel;
                            String deliveryStatus = null;
                            if (getListType == ORDER_LIST_PREPARING){
                                preparingOrderList.clear();
                                deliveryStatus = "ACCEPTED";
                            }
                            else if (getListType == ORDER_LIST_READY_TO_DELIVER){
                                readyToDeliveredList.clear();
                                deliveryStatus = "PACKED";
                            }

                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                String delivery_status = documentSnapshot.get( "delivery_status" ).toString();
                                if (delivery_status.toUpperCase().equals( deliveryStatus )){
                                    // Assign new OrderListModel...
                                    orderListModel = new OrderListModel();

                                    orderListModel.setOrderID( documentSnapshot.get( "order_id" ).toString() );
                                    orderListModel.setDeliveryStatus( documentSnapshot.get( "delivery_status" ).toString() );
                                    orderListModel.setPayMode( documentSnapshot.get( "pay_mode" ).toString() );

                                    orderListModel.setDeliveryCharge( documentSnapshot.get( "delivery_charge" ).toString() );
                                    // billing_amounts
                                    orderListModel.setBillingAmounts( documentSnapshot.get( "billing_amounts" ).toString() );
                                    // total_amounts
                                    orderListModel.setProductAmounts( documentSnapshot.get( "total_amounts" ).toString() );

                                    orderListModel.setCustAuthID( documentSnapshot.get( "order_by_auth_id" ).toString() );
                                    orderListModel.setCustName( documentSnapshot.get( "order_by_name" ).toString() );
                                    orderListModel.setCustMobile( documentSnapshot.get( "order_by_mobile" ).toString() );

                                    orderListModel.setShippingName( documentSnapshot.get( "order_accepted_by" ).toString() );
                                    orderListModel.setShippingAddress( documentSnapshot.get( "order_delivery_address" ).toString() );
                                    orderListModel.setShippingPinCode( documentSnapshot.get( "order_delivery_pin" ).toString() );

                                    orderListModel.setOrderDate( documentSnapshot.get( "order_date" ).toString() );
                                    orderListModel.setOrderDay( documentSnapshot.get( "order_day" ).toString() );
                                    orderListModel.setOrderTime( documentSnapshot.get( "order_time" ).toString() );

                                    orderListModel.setDeliverySchedule( documentSnapshot.get( "delivery_schedule_time" ).toString() );

                                    if ( documentSnapshot.get( "delivery_id" ) != null){
                                        orderListModel.setDeliveryID( documentSnapshot.get( "delivery_id" ).toString()  );
                                    }else{
                                        orderListModel.setDeliveryID( null );
                                    }

//                                    long no_of_products = (long)documentSnapshot.get( "no_of_products" );

                                    orderSubList =  (ArrayList <OrderProductsModel>) documentSnapshot.getData().get( "products_list" );

                                    orderListModel.setOrderProductItemsList( orderSubList );

                                    // add Model in the new Order List...
                                    if (getListType == ORDER_LIST_PREPARING){
                                        preparingOrderList.add( orderListModel );
                                    }
                                    else if (getListType == ORDER_LIST_READY_TO_DELIVER){
                                        readyToDeliveredList.add( orderListModel );
                                    }

                                }

                            }

                        }
                    }
                } );
    }
    // ------------------------  New Order Query ----------------------------
    /**  Order Status
     *          1. WAITING - ( For Accept )
     *          2. ACCEPTED - ( Preparing )
     *          3. PACKED - ( Waiting for Delivery ) READY_TO_DELIVERY
     *          4. PROCESS  - ( On Delivery ) OUT_FOR_DELIVERY
     *          5. SUCCESS - Success Full Delivered..!
     *          6. CANCELLED -  When Order has been cancelled by user...
     *          7. FAILED -  when PayMode Online and payment has been failed...
     *          8. PENDING - when Payment is Pending...
     *
     */

    // Send Notification to User.... ------------------------
    // Use thread from StaticMethods...

    //...
    public void getDeliveryList(){
        // To get New Delivery List...
        firebaseFirestore.collection( "DELIVERY" )
                .document( SHOP_DATA_MODEL.getShop_city_name() )
                .collection( "DELIVERY" )
                .whereEqualTo( "SAMPOL", "SAMPOL" )
                .get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
//                                documentSnapshot.getId();
                            }
                        }else{

                        }
                    }
                } );
    }

    // ------------------------  Call All List of New Order Query ----------------------------
    // Load New Order List Data....
    public static void loadNewOrderListData(Context context){
        // New Orders Loading...
        if (newOrderList.size() == 0){
            getNewOrderQuery(context);
        }
        // Load Preparing Orders...
        if (preparingOrderList.size() == 0){
            getAcceptedOrderList( ORDER_LIST_PREPARING );
        }
        // Load Ready To Delivery Orders...
        if ( readyToDeliveredList.size() == 0){
            getAcceptedOrderList( ORDER_LIST_READY_TO_DELIVER );
        }
    }

    // ---------------- Update Product Data Start ----------------------------------
    public static void updateProductData( @Nullable final Dialog dialog, String productID, Map<String, Object> updateMap){
        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( "PRODUCTS" ).document( productID  )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){

                        }else{

                        }

                        if (dialog!=null){
                            dialog.dismiss();
                        }
                    }
                } );
    }
    // ---------------- Update Product Data End ----------------------------------




}
