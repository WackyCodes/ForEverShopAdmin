package ean.ecom.eanshopadmin.main.orderlist.orderviewdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.main.orderlist.OrderListAdaptor;
import ean.ecom.eanshopadmin.main.orderlist.OrderListModel;
import ean.ecom.eanshopadmin.main.orderlist.OrderProductItemModel;
import ean.ecom.eanshopadmin.main.orderlist.OrderProductsModel;
import ean.ecom.eanshopadmin.main.orderlist.neworder.NewOrderTabAdaptor;
import ean.ecom.eanshopadmin.main.orderlist.neworder.OrderViewPagerFragment;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.StaticMethods;

import static ean.ecom.eanshopadmin.database.DBQuery.newOrderList;
import static ean.ecom.eanshopadmin.database.DBQuery.orderListModelList;
import static ean.ecom.eanshopadmin.database.DBQuery.preparingOrderList;
import static ean.ecom.eanshopadmin.database.DBQuery.readyToDeliveredList;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_NEW_ORDER;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_OUT_FOR_DELIVERY;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_PREPARING;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_READY_TO_DELIVER;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_SUCCESS;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class OrderViewActivity extends AppCompatActivity implements OrderViewInteractor {

    private List <OrderListModel> orderListModelList = new ArrayList <>();
    private OrderListModel orderListModel;
    private OrderStatusUpdateQuery updateQuery = new OrderStatusUpdateQuery();


    private int LIST_TYPE = -1;
    private int ListIndex = -1;
    private String orderID = null;
    private Dialog dialog;


    // Variables...

    // Time and Date...
    private TextView orderDate; // order_date
    private TextView orderTime; // order_time

    // Order List...
    private RecyclerView orderListRecycler; // product_list_recycler
    // Bill
    private TextView totalItemsText; // total_items_tv
    private TextView totalItemsAmountText; // total_items_amount_tv

    // Order By And Address...
    private TextView orderByName; // order_by_name
    private TextView shippingName; // shipping_name
    private TextView shippingAddress; // shipping_address
    private TextView shippingPin; // shipping_address_pin
    private ImageView shippingImage; // shipping_address_image

    // Order Status...
    private ImageView orderStatusIcon ; // order_status_icon
    private TextView orderStatusText; // order_status_text_view

    private ImageView deliveryBoyViewBtn ; // view_delivery_boy_img_view

    // Order Action Btn...
    private TextView acceptOrderBtn; // accept_order_text_btn
    private TextView packingDoneBtn; // preparing_packing_text_btn

    private TextView outForDeliveryBtn; // out_for_delivery_text_btn
    private EditText deliveryOtpEditText; // delivery_otp_edit_text

    // Delivery Boy.. View....
    private LinearLayout searchingDeliveryBoyLayout; // searching_delivery_boy_layout

    private ConstraintLayout deliveryBoyViewLayout; // delivery_boy_const_layout
    private TextView deliveryBoyName; // delivery_by_name_text_view
    private TextView deliveryBoyStatus; // delivery_boy_status
    private TextView deliveryBoyProfileBtn; // delivery_boy_view_profile

    private Toolbar toolbar;
    private OrderProductsModel orderProductsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_view );
        orderProductsModel = new OrderProductsModel();

        //--------
        orderID = getIntent().getStringExtra( "ORDER_ID" );
        LIST_TYPE = getIntent().getIntExtra( "ORDER_STATUS", -1 );
        dialog = DialogsClass.getDialog( this );
        //---------------------------------------------------------------------------------
        orderDate = findViewById( R.id.order_date );
        orderTime = findViewById( R.id.order_time );

        // Order List...
        orderListRecycler = findViewById( R.id.product_list_recycler );
        // Bill
        totalItemsText = findViewById( R.id.total_items_tv );
        totalItemsAmountText = findViewById( R.id.total_items_amount_tv );

        // Order By And Address...
        orderByName = findViewById( R.id.order_by_name );
        shippingName = findViewById( R.id.shipping_name );
        shippingAddress = findViewById( R.id.shipping_address );
        shippingPin = findViewById( R.id.shipping_address_pin );
        shippingImage = findViewById( R.id.shipping_address_image );

        // Order Status...
        orderStatusIcon = findViewById( R.id.order_status_icon );
        orderStatusText = findViewById( R.id.order_status_text_view );

        deliveryBoyViewBtn = findViewById( R.id.view_delivery_boy_img_view );

        // Order Action Btn...
        acceptOrderBtn = findViewById( R.id.accept_order_text_btn );
        packingDoneBtn = findViewById( R.id.preparing_packing_text_btn );

        outForDeliveryBtn = findViewById( R.id.out_for_delivery_text_btn );
        deliveryOtpEditText = findViewById( R.id.delivery_otp_edit_text );

        // Delivery Boy.. View....
        searchingDeliveryBoyLayout = findViewById( R.id.searching_delivery_boy_layout );

        deliveryBoyViewLayout = findViewById( R.id.delivery_boy_const_layout );
        deliveryBoyName = findViewById( R.id.delivery_by_name_text_view );
        deliveryBoyStatus = findViewById( R.id.delivery_boy_status );
        deliveryBoyProfileBtn = findViewById( R.id.delivery_boy_view_profile );

        //---------------------------------------------------------------------------------

        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( orderID );
        }catch (NullPointerException ignored){ }

        shippingImage.setImageResource( R.drawable.ic_person_pin_circle_black_24dp );
//        searchingDeliveryBoyLayout.setVisibility( View.GONE );
//        deliveryBoyViewLayout.setVisibility( View.GONE );

        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        orderListRecycler.setLayoutManager( layoutManager );

        // On Button Clicked...!
        onButtonClicked();

        // Get Sample Index and Set Data...
        ListIndex = getListIndex(orderID);

    }

    private void onButtonClicked(){
        // Accept Order Click Btn...
        acceptOrderBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                int list_index = getListIndex( orderID );
                if (list_index < 0 || list_index > newOrderList.size()){
                    dialog.dismiss();
                    showToast(  "Order Item Not found!" );
                }else{
                    setAcceptOrderBtn( list_index );
                    // Change List type...
                    LIST_TYPE = ORDER_LIST_PREPARING;
                    setOrderStatusLayout();
                }
            }
        } );
        // Packed Order Click Btn...
        packingDoneBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                int list_index = getListIndex( orderID );
                if (list_index < 0 || list_index > preparingOrderList.size()){
                    dialog.dismiss();
                    showToast(  "Order Item Not found!" );

                }else{
                    setPackingTextBtn( list_index );

                    // Change List type...
                    LIST_TYPE = ORDER_LIST_READY_TO_DELIVER;
                    setOrderStatusLayout();
                }
            }
        } );
        // Out For Delivery Click Btn...
        outForDeliveryBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty( deliveryOtpEditText.getText().toString() )){
                    deliveryOtpEditText.setError( "Required!" );
                }
                else {
                    dialog.show();
                    int list_index = getListIndex( orderID );

                    if (list_index < 0 || list_index > readyToDeliveredList.size()){
                        dialog.dismiss();
                        showToast(  "Order Item Not found!" );
                    }else{
                        setOutForDeliveryBtn(  deliveryOtpEditText.getText().toString() , list_index );
                    }
                }
            }
        } );
        // Visibility for Delivery View Click Btn...
        deliveryBoyViewBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibilityDeliveryBoyLayout();
            }
        } );

        // Delivery Boy Profile View Btn...
        deliveryBoyProfileBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(  "Details not Found!" );
            }
        } );

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
        if ( item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    // new Order List...
    private void setNewOrderList(){
        switch (LIST_TYPE){
            case ORDER_LIST_NEW_ORDER:
                orderListModelList = newOrderList;
                break;
            case ORDER_LIST_PREPARING:
                orderListModelList = preparingOrderList;
                break;
            case ORDER_LIST_READY_TO_DELIVER:
                orderListModelList = readyToDeliveredList;
                break;
            default:
                break;
        }
    }

    // get Order ID index...
    private int getListIndex( String orderID ){
        int index = -1;
        switch (LIST_TYPE){
            case ORDER_LIST_NEW_ORDER:
                index = 0;
                for ( OrderListModel orderListModel : newOrderList ){
                    if (orderListModel.getOrderID().equals( orderID )){
                        if (this.orderListModel == null){
                            this.orderListModel = orderListModel;
                            setActivityData();
                        }
                        break;
                    }
                    index ++;
                }
                return index;
            case ORDER_LIST_PREPARING:
                index = 0;
                for ( OrderListModel orderListModel : preparingOrderList ){
                    if (orderListModel.getOrderID().equals( orderID )){
                        if (this.orderListModel == null){
                            this.orderListModel = orderListModel;
                            setActivityData();
                        }
                        break;
                    }
                    index ++;
                }
                return index;
            case ORDER_LIST_READY_TO_DELIVER:
                index = 0;
                for ( OrderListModel orderListModel : readyToDeliveredList ){
                    if (orderListModel.getOrderID().equals( orderID )){
                        if (this.orderListModel == null){
                            this.orderListModel = orderListModel;
                            setActivityData();
                        }
                        break;
                    }
                    index ++;
                }
                return index;
            default:
                return index;
        }
    }
    // Set Activity Data...
    private void setActivityData(){

        // Date
        orderDate.setText( orderListModel.getOrderDate() );
//        Time
        String timeDuration = StaticMethods.getTimeFromNow(  orderListModel.getOrderDate(),  orderListModel.getOrderTime() +":00" );
        if(timeDuration.substring( 3 ).equals( orderListModel.getOrderDate() )){
            orderTime.setText( "at " + orderListModel.getOrderTime() );
        }else{
            orderTime.setText( "Order " + timeDuration );
        }

//        orderListRecycler
        OrderViewListAdaptor viewListAdaptor = new OrderViewListAdaptor( orderListModel.getOrderProductItemsList() );
        orderListRecycler.setAdapter( viewListAdaptor );
        viewListAdaptor.notifyDataSetChanged();

        totalItemsText.setText( "(" + orderListModel.getOrderProductItemsList().size() + ")" );
        totalItemsAmountText.setText( "Rs." + orderListModel.getProductAmounts() );

        orderByName.setText( orderListModel.getCustName() );
        shippingName.setText( orderListModel.getShippingName() );
        shippingAddress.setText( orderListModel.getShippingAddress() );
        shippingPin.setText( orderListModel.getShippingPinCode() );

        setOrderStatusLayout();
    }

    // Set Order Status Icon And Text...
    private void setOrderStatusLayout(){
        acceptOrderBtn.setVisibility( View.GONE );
        packingDoneBtn.setVisibility( View.GONE );
        deliveryOtpEditText.setVisibility( View.GONE );
        outForDeliveryBtn.setVisibility( View.GONE );

        // Decide based On List Type...
        switch (LIST_TYPE){
            case ORDER_LIST_NEW_ORDER:
                acceptOrderBtn.setVisibility( View.VISIBLE );
                setOrderStatusTextIcon("Waiting...", R.drawable.ic_watch_later_black_24dp, R.color.colorPrimary );
                break;
            case ORDER_LIST_PREPARING:
                packingDoneBtn.setVisibility( View.VISIBLE );
                setOrderStatusTextIcon("Packing...", R.drawable.ic_av_packing_timer_black_24dp, R.color.colorSecondary );
                break;
            case ORDER_LIST_READY_TO_DELIVER:
                deliveryOtpEditText.setVisibility( View.VISIBLE );
                outForDeliveryBtn.setVisibility( View.VISIBLE );
                setOrderStatusTextIcon("Packed...", R.drawable.ic_card_packed_travel_black_24dp, R.color.colorBlue );
                break;
            case ORDER_LIST_OUT_FOR_DELIVERY:
                // Update In Local Model//
                updateOrderStatusInModel("PICKED");
                setOrderStatusTextIcon("Way On Delivery", R.drawable.ic_local_shipping_black_24dp, R.color.colorPrimary );
                break;
            case ORDER_LIST_SUCCESS:
                setOrderStatusTextIcon("Delivered Successfully!", R.drawable.ic_check_circle_black_24dp, R.color.colorGreen );
                break;
            default:
                break;
        }

    }
    private void setOrderStatusTextIcon( String statusText, int iconId, int iconColoId ){
        // Order Status...
        orderStatusIcon.setImageResource( iconId );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            orderStatusIcon.setImageTintList( this.getResources().getColorStateList( iconColoId  ) );
        }
        orderStatusText.setText( statusText );

    }

    // Set Visibility Delivery Boy Info...
    private void setVisibilityDeliveryBoyLayout(){
        if (searchingDeliveryBoyLayout.getVisibility() == View.VISIBLE || deliveryBoyViewLayout.getVisibility() == View.VISIBLE)
        {
            if(searchingDeliveryBoyLayout.getVisibility() == View.VISIBLE){
                searchingDeliveryBoyLayout.setVisibility( View.GONE );
            }else if (deliveryBoyViewLayout.getVisibility() == View.VISIBLE){
                deliveryBoyViewLayout.setVisibility( View.GONE );
            }
            setDeliveryBoyViewBtn(false);
        }else{
            // Check and send Request...
            if (orderListModel.getDeliveredByName() != null && orderListModel.getDeliveredByAuthID() != null){
                deliveryBoyViewLayout.setVisibility( View.VISIBLE );
                // Set Data...
                setDeliveryData();
            }else{
                requestToFindDeliveryBoy();
            }
            setDeliveryBoyViewBtn(true);
        }
    }
    // Set Delivery Data...
    private void setDeliveryData(){
        deliveryBoyName.setText( orderListModel.getDeliveredByName() );
        if (LIST_TYPE == ORDER_LIST_OUT_FOR_DELIVERY  ){
            deliveryBoyStatus.setText( "On the Way" );
        }else if(LIST_TYPE == ORDER_LIST_SUCCESS){
            deliveryBoyStatus.setText( "Delivered" );
        }else{
            deliveryBoyStatus.setText( "Upcoming..." );
        }

    }
    // Set Visibility Btn.
    private void setDeliveryBoyViewBtn(boolean setDown){
        if (setDown){
            deliveryBoyViewBtn.setImageResource( R.drawable.ic_keyboard_arrow_down_black_24dp );
        }else{
            deliveryBoyViewBtn.setImageResource( R.drawable.ic_keyboard_arrow_up_black_24dp );
        }
    }

    // Request To find Delivery Boy...
    private void requestToFindDeliveryBoy(){
        if (deliveryBoyViewLayout.getVisibility() == View.VISIBLE){
            deliveryBoyViewLayout.setVisibility( View.GONE );
            searchingDeliveryBoyLayout.setVisibility( View.VISIBLE );
        }else{
            searchingDeliveryBoyLayout.setVisibility( View.VISIBLE );
        }
        if ( orderListModel.getDeliveryID() != null ){
            // send Request...
            updateQuery.onFindDeliveryBoyQuery( this, orderID );
        }
    }

    private void updateOrderStatusInModel(String updateStatus){
        orderListModel.setDeliveryStatus( updateStatus );
    }

    //----------------------- Override Methods...
    @Override
    public void onDeliveryBoyFound(Map<String, Object> deliveryBoyInfo) {
        orderListModel.setDeliveredByAuthID( deliveryBoyInfo.get( "delivery_by_uid" ).toString() );
        orderListModel.setDeliveredByMobile( deliveryBoyInfo.get( "delivery_by_mobile" ).toString() );
        orderListModel.setDeliveredByName( deliveryBoyInfo.get( "delivery_by_name" ).toString() );
        orderListModel.setDeliveryID( deliveryBoyInfo.get( "delivery_id" ).toString() );

        if (searchingDeliveryBoyLayout.getVisibility() == View.VISIBLE){
            deliveryBoyViewLayout.setVisibility( View.VISIBLE );
            searchingDeliveryBoyLayout.setVisibility( View.GONE );
        }else{
            deliveryBoyViewLayout.setVisibility( View.VISIBLE );
        }
        setDeliveryData();
    }

    @Override
    public void onUpdateOrderStatus(int updateCode) {
        dismissDialog();
        LIST_TYPE = updateCode;
        setOrderStatusLayout();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void showDialog() {
        dialog.show();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    //----------------------- Action Button Query --------------------------------------------------
    // Accept Order Click Listener...
    private void setAcceptOrderBtn(int index ){
        if (!dialog.isShowing()){
            dialog.show();
        }
        orderProductsModel.setData( (Map <String, Object>) newOrderList.get( index ).getOrderProductItemsList().get( 0 ) );
        // Create Map For Notify User...
        Map <String, Object> notifyMap = new HashMap <>();
        notifyMap.put( "index", StaticMethods.getRandomIndex() );
        notifyMap.put( "notify_type", 1 );
        notifyMap.put( "notify_id", StaticMethods.getFiveDigitRandom() );
        notifyMap.put( "notify_click_id", newOrderList.get( index ).getOrderID() );
        notifyMap.put( "notify_image",  orderProductsModel.getProductImage() );
        notifyMap.put( "notify_title", "Your Order preparing to pack" );
        notifyMap.put( "notify_body", orderProductsModel.getProductName() );
        notifyMap.put( "notify_date", StaticMethods.getCrrDate() );
        notifyMap.put( "notify_time", StaticMethods.getCrrTime() );
        notifyMap.put( "notify_is_read", false );
        // Notify User...
        DBQuery.sentNotificationToUser( newOrderList.get( index ).getCustAuthID(), notifyMap );

//           Query To Find Delivery Boy... After that Update On Order Document...
        queryToDeliveryBoy(index);

    }
    private void queryToDeliveryBoy(int index){
        final String vOTP = StaticMethods.getOTPDigitRandom(); // 4 Digit...
        newOrderList.get( index ).setOutForDeliveryOTP( vOTP );

        OrderListModel orderListModel = newOrderList.get( index );

        Map <String, Object> deliveryMap = new HashMap <>();
        deliveryMap.put( "order_id", orderListModel.getOrderID() );
        deliveryMap.put( "delivery_status", "ACCEPTED" );
        deliveryMap.put( "verify_otp", vOTP );
        deliveryMap.put( "accepted_date", StaticMethods.getCrrDate() );
        deliveryMap.put( "accepted_time", StaticMethods.getCrrTime() );
        deliveryMap.put( "shop_id", SHOP_ID );
        deliveryMap.put( "shop_name", ADMIN_DATA_MODEL.getShopName() );
        deliveryMap.put( "shop_logo", ADMIN_DATA_MODEL.getShopLogo() );
        deliveryMap.put( "shop_address", ADMIN_DATA_MODEL.getShopAddress() );
        deliveryMap.put( "shop_helpline", ADMIN_DATA_MODEL.getShopHelpLine() );
        deliveryMap.put( "shop_pin", ADMIN_DATA_MODEL.getShopAreaCode() );
        deliveryMap.put( "shipping_address", orderListModel.getShippingAddress() );
        deliveryMap.put( "shipping_pin", orderListModel.getShippingPinCode() );

        DBQuery.setDeliveryDocument( dialog, deliveryMap, newOrderList.get( index ));

        newOrderList.remove( index );

        // Show the No Order Text.. If List size = 0;
//        if (newOrderList.size() == 0){
//            NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_NEW_ORDER, View.VISIBLE );
//        }
        // Notify Data Changed...
        /**
        if (OrderViewPagerFragment.orderViewPagerListAdaptor != null){
            OrderViewPagerFragment.orderViewPagerListAdaptor.notifyDataSetChanged();
        } */
        // Update In Local Model//
        updateOrderStatusInModel("ACCEPTED");
    }

    // set Packing Done Btn Click Listener...
    private void setPackingTextBtn(int index){
        if (!dialog.isShowing()){
            dialog.show();
        }
        orderProductsModel.setData( (Map <String, Object>) preparingOrderList.get( index ).getOrderProductItemsList().get( 0 ) );
        Map <String, Object> updateMap = new HashMap <>();
        updateMap.put( "delivery_status", "PACKED" );
        preparingOrderList.get( index ).setDeliveryStatus( "PACKED" );
        DBQuery.updateOrderStatus( dialog, preparingOrderList.get( index ) ,updateMap );

        // Create Map For Notify User...
        Map <String, Object> notifyMap = new HashMap <>();
        notifyMap.put( "index", StaticMethods.getRandomIndex() );
        notifyMap.put( "notify_type", 1 );
        notifyMap.put( "notify_id", StaticMethods.getFiveDigitRandom() );
        notifyMap.put( "notify_click_id", preparingOrderList.get( index ).getOrderID() );
        notifyMap.put( "notify_image",  orderProductsModel.getProductImage() );
        notifyMap.put( "notify_title", "Your Order has been packed! Waiting for delivery..." );
        notifyMap.put( "notify_body",  orderProductsModel.getProductName() );
        notifyMap.put( "notify_date", StaticMethods.getCrrDate() );
        notifyMap.put( "notify_time", StaticMethods.getCrrTime() );
        notifyMap.put( "notify_is_read", false );
        // Notify User...
        DBQuery.sentNotificationToUser( preparingOrderList.get( index ).getCustAuthID(), notifyMap );

        preparingOrderList.remove( index );
        // Show the No Order Text.. If List size = 0;
//        if (preparingOrderList.size() == 0){
//            NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_PREPARING, View.VISIBLE );
//        }
        // Notify Data Changed...

        // Update In Local Model//
        updateOrderStatusInModel("PACKED");
    }

    // Out For Delivery Action...
    private void setOutForDeliveryBtn(String otpPin, int index){
        if (!dialog.isShowing()){
            dialog.show();
        }
        getDeliveryOtp( readyToDeliveredList.get( index ).getDeliveryID(), otpPin );
    }

    private void getDeliveryOtp(String deliveryId, final String verifyOtp){
        DBQuery.firebaseFirestore.collection( "DELIVERY" )
                .document( ADMIN_DATA_MODEL.getShopCityCode() )
                .collection( "DELIVERY" )
                .document( deliveryId )
                .get( ).addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String otp = task.getResult().get( "verify_otp" ).toString();
                    if (otp.equals( verifyOtp )){
                        showToast(   "Successfully matched! Please give products to the delivery boy!" );
                        //  : UPDATE in the Order List...
                        Map <String, Object> updateMap  = new HashMap <>();
                        updateMap.put( "delivery_status", "PICKED" );
                        updateQuery.onUpdateStatusQuery( OrderViewActivity.this, orderID, updateMap );
                        // Dismiss dialog after update on Order...

                    }else{
                        deliveryOtpEditText.setError( "Not Matched!" );
                        dialog.dismiss();
                        showToast(  "Incorrect! Ask Correct OTP from delivery boy and Try again..." );
                    }

                }else{
                    dialog.dismiss();
                    showToast(  "Something Went Wrong! Error : "+ task.getException().getMessage() );
                }
            }
        } );

    }



}
