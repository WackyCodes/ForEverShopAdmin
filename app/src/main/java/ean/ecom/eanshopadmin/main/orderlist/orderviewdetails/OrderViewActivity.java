package ean.ecom.eanshopadmin.main.orderlist.orderviewdetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import ean.ecom.eanshopadmin.database.OrderUpdateQuery;
import ean.ecom.eanshopadmin.main.orderlist.OrderListAdaptor;
import ean.ecom.eanshopadmin.main.orderlist.OrderListModel;
import ean.ecom.eanshopadmin.main.orderlist.OrderProductItemModel;
import ean.ecom.eanshopadmin.main.orderlist.OrderProductsModel;
import ean.ecom.eanshopadmin.main.orderlist.OrderUpdateListener;
import ean.ecom.eanshopadmin.main.orderlist.neworder.NewOrderTabAdaptor;
import ean.ecom.eanshopadmin.main.orderlist.neworder.OrderViewPagerFragment;
import ean.ecom.eanshopadmin.notification.UserNotificationModel;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.StaticMethods;
import ean.ecom.eanshopadmin.other.StaticValues;

import static ean.ecom.eanshopadmin.database.DBQuery.newOrderList;
import static ean.ecom.eanshopadmin.database.DBQuery.orderListModelList;
import static ean.ecom.eanshopadmin.database.DBQuery.preparingOrderList;
import static ean.ecom.eanshopadmin.database.DBQuery.readyToDeliveredList;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_ACCEPTED;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_NEW_ORDER;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_OUT_FOR_DELIVERY;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_PREPARING;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_READY_TO_DELIVER;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_SUCCESS;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_PACKED;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_PICKED;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class OrderViewActivity extends AppCompatActivity implements OrderViewInteractor, OrderUpdateListener {

    private List <OrderListModel> orderListModelList = new ArrayList <>();
    private OrderListModel orderListModel;
    private OrderStatusUpdateQuery updateQuery = new OrderStatusUpdateQuery();
    private OrderUpdateQuery orderUpdateQuery = new OrderUpdateQuery();

    private int LIST_TYPE = -1;
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
         getListIndex(orderID); // Assign the index //

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
                for ( OrderListModel orderListModel : newOrderList ){
                    if (orderListModel.getOrderID().equals( orderID )){
                        if (this.orderListModel == null){
                            this.orderListModel = orderListModel;
                            setActivityData();
                        }
                        index = newOrderList.indexOf( orderListModel );
                        break;
                    }
                }
                return index;
            case ORDER_LIST_PREPARING:
                for ( OrderListModel orderListModel : preparingOrderList ){
                    if (orderListModel.getOrderID().equals( orderID )){
                        if (this.orderListModel == null){
                            this.orderListModel = orderListModel;
                            setActivityData();
                        }
                        index = preparingOrderList.indexOf( orderListModel );
                        break;
                    }
                }
                return index;
            case ORDER_LIST_READY_TO_DELIVER:
                for ( OrderListModel orderListModel : readyToDeliveredList ){
                    if (orderListModel.getOrderID().equals( orderID )){
                        if (this.orderListModel == null){
                            this.orderListModel = orderListModel;
                            setActivityData();
                        }
                        index = readyToDeliveredList.indexOf( orderListModel );
                        break;
                    }
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
                // For Notify User...
                // update delivery list : Automatic Update after List Update..
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
        if (deliveryBoyInfo != null){
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
        }else{
            orderListModel.setDeliveredByAuthID( null );
            orderListModel.setDeliveredByMobile( null );
            orderListModel.setDeliveredByName( null );
            orderListModel.setDeliveryID( null );
            if (deliveryBoyViewLayout.getVisibility() == View.VISIBLE){
                deliveryBoyViewLayout.setVisibility( View.GONE );
                searchingDeliveryBoyLayout.setVisibility( View.VISIBLE );
            }else{
                searchingDeliveryBoyLayout.setVisibility( View.VISIBLE );
            }
        }

    }

    @Override
    public void onOrderUpdateSuccess(String updateValue, int index) {
        dismissDialog();
        if (updateValue.toUpperCase().equals( ORDER_ACCEPTED )){ // Preparing...
            // Remove...
            newOrderList.remove( index );

            // Update In Local Model//
            updateOrderStatusInModel( ORDER_ACCEPTED );

            // Show the No Order Text.. If List size = 0;
            if (newOrderList.size() == 0){
                NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_NEW_ORDER, View.VISIBLE );
            }
            // Notify Data Changed...
            if (OrderViewPagerFragment.orderViewPagerListAdaptor != null){
                OrderViewPagerFragment.orderViewPagerListAdaptor.notifyDataSetChanged();
            }

        }
        else  if (updateValue.toUpperCase().equals( ORDER_PACKED )){ // Ready to Delivery...
            preparingOrderList.remove( index );
            // Update In Local Model//
            updateOrderStatusInModel( ORDER_PACKED );

            // Show the No Order Text.. If List size = 0;
            if (preparingOrderList.size() == 0){
                NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_PREPARING, View.VISIBLE );
            }
            // Notify Data Changed...

            if (OrderViewPagerFragment.orderViewPagerListAdaptor != null){
                OrderViewPagerFragment.orderViewPagerListAdaptor.notifyDataSetChanged();
            }

        }
        else if (updateValue.toUpperCase().equals( ORDER_PICKED )){ // Out For Delivery...
            // By Default Done...
            dismissDialog();
            LIST_TYPE = ORDER_LIST_OUT_FOR_DELIVERY;
            setOrderStatusLayout();
        }

    }

    @Override
    public void onOrderUpdateFailed(String updateValue, @Nullable OrderListModel orderListModel, @Nullable Map <String, Object> updateMap, int index) {
        dismissDialog();
        if (updateValue.toUpperCase().equals( ORDER_ACCEPTED )){ // Preparing...
            showToast( "Failed to update... Check Your internet connection!" );
        } else  if (updateValue.toUpperCase().equals( ORDER_PACKED )){ // Ready to Delivery...
            showDialog();
            showToast( "Please Wait... Check Your internet connection!" );
            orderUpdateQuery.updateOrderStatus( this, orderListModel, updateMap, index );
        } else  if (updateValue.toUpperCase().equals( ORDER_PICKED )){ // Out For Delivery...
            // By Default Done...
            showToast( "Failed to update... Check Your internet connection!" );
        }
    }

    @Override
    public void onUpdateDeliveryFailed() {
        dismissDialog();
        showToast( "Failed to update ! Please Check Your internet connection and try again!" );
    }

    @Override
    public void otpVerificationResponse(int responseCode, @Nullable String responseMessage) {
        switch (responseCode){
            case 1: // VERIFIED
                int temIndex = Integer.parseInt( responseMessage );
                showToast(   "Successfully verified! Please give products to the delivery boy!" );
                orderProductsModel.setData( (Map <String, Object>) readyToDeliveredList.get( temIndex ).getOrderProductItemsList().get( 0 ) );
                //  : UPDATE in the Order List...
                Map <String, Object> updateMap  = new HashMap <>();
                updateMap.put( "delivery_status", ORDER_PICKED );
                // Update In Delivery Document...
                orderUpdateQuery.updateDeliveryDocument(OrderViewActivity.this, orderID, ADMIN_DATA_MODEL.getShopCityCode(),
                        readyToDeliveredList.get( temIndex ).getDeliveryID(), updateMap );
                // Update In Shop Order document... After Update in document...
                // Thread : Send Notification To user
                sendNotificationToUser( temIndex, orderProductsModel, readyToDeliveredList,"Your Order has been out for delivery!" );
//                dismissDialog();
                break;
            case 2: // NOT VERIFIED
                showToast( "Please try again!" );
                deliveryOtpEditText.setError( "Not Matched!" );
                dismissDialog();
                break;
            case 0: // FAILED
            case 3: // EXCEPTION
                showToast( "Failed : " + responseMessage );
                dismissDialog();
                break;
            default:
                dismissDialog();
                break;
        }
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
        showDialog();

        // -------------------------
        Map <String, Object> updateMap = new HashMap <>();
        updateMap.put( "delivery_status", ORDER_ACCEPTED );
        newOrderList.get( index ).setDeliveryStatus( ORDER_ACCEPTED );
//        DBQuery.updateOrderStatus( dialog, newOrderList.get( index ) ,updateMap ); // TODO : UPDATE

        orderUpdateQuery.updateOrderStatus( this, newOrderList.get( index ) ,updateMap, index );

        orderProductsModel.setData( (Map <String, Object>) newOrderList.get( index ).getOrderProductItemsList().get( 0 ) );
        // For Notify User...
        sendNotificationToUser( index, orderProductsModel, newOrderList,"Your Order preparing to pack" );

    }
    // set Packing Done Btn Click Listener...
    private void setPackingTextBtn(int index){
        showDialog();
        orderProductsModel.setData( (Map <String, Object>) preparingOrderList.get( index ).getOrderProductItemsList().get( 0 ) );

        // Query to Find Delivery Boy....
        queryToDeliveryBoy( index );
        // Notify User...
        sendNotificationToUser( index, orderProductsModel,  preparingOrderList,"Your Order has been packed! Waiting for delivery..." );

    }

    private void queryToDeliveryBoy(int index){
        final String vOTP = StaticMethods.getOTPDigitRandom(); // 4 Digit...
        preparingOrderList.get( index ).setOutForDeliveryOTP( vOTP );

        OrderListModel orderListModel = preparingOrderList.get( index );

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
        deliveryMap.put( "shop_geo_point", ADMIN_DATA_MODEL.getShopGeoPoint() );
        deliveryMap.put( "shipping_geo_point", orderListModel.getShippingGeoPoint() );

//        DBQuery.setDeliveryDocument( dialog, deliveryMap, newOrderList.get( index ));

        orderUpdateQuery.setDeliveryDocument( this,  deliveryMap, preparingOrderList.get( index ), index );

    }

    // Out For Delivery Action...
    private void setOutForDeliveryBtn(String otpPin, int index){
        showDialog();
//        Call Methods..
        orderUpdateQuery.onCheckOTPQuery( this, index, readyToDeliveredList.get( index ).getDeliveryID(), otpPin  );
    }


    private void sendNotificationToUser( int index, OrderProductsModel orderProductsModel, List<OrderListModel> orderListModelList, String title){
        // Create Map For Notify User...
        String notifyId =  StaticMethods.getFiveDigitRandom();
        UserNotificationModel notificationModel = new UserNotificationModel();
        notificationModel.setNotify_type( 1 );
        notificationModel.setNotify_id( notifyId );
        notificationModel.setNotify_click_id(  orderListModelList.get( index ).getOrderID()  );
        notificationModel.setNotify_image( orderProductsModel.getProductImage()  );
        notificationModel.setNotify_title( title );
        notificationModel.setNotify_body( orderProductsModel.getProductName() );
        // Notify User...
//        DBQuery.sentNotificationToUser(  orderListModelList.get( index ).getCustAuthID(), notificationModel.getMap() );

        Thread notifyThread = new Thread( new StaticMethods.SendUserNotification( notifyId, orderListModelList.get( index ).getCustAuthID(), notificationModel.getMap()));
        notifyThread.start();
    }



}
