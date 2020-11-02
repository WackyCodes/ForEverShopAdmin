package ean.ecom.eanshopadmin.main.orderlist;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.database.OrderUpdateQuery;
import ean.ecom.eanshopadmin.main.orderlist.neworder.NewOrderTabAdaptor;
import ean.ecom.eanshopadmin.main.orderlist.orderviewdetails.OrderViewActivity;
import ean.ecom.eanshopadmin.notification.UserNotificationModel;
import ean.ecom.eanshopadmin.other.StaticMethods;

import static ean.ecom.eanshopadmin.database.DBQuery.readyToDeliveredList;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_CANCELLED;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_FAILED;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_PENDING;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_PROCESS;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_SUCCESS;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_WAITING;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_ACCEPTED;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_CHECK;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_NEW_ORDER;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_PREPARING;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_READY_TO_DELIVER;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_PACKED;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_PICKED;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

/**
 * Created by Shailendra (WackyCodes) on 31/07/2020 21:21
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderListAdaptor extends RecyclerView.Adapter {

    private List <OrderListModel> orderListModelList;
    private int listType;
//    private View fragmentView;

    public OrderListAdaptor(List <OrderListModel> orderListModelList, int listType) {
        this.orderListModelList = orderListModelList;
        this.listType = listType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (listType){
            case ORDER_LIST_CHECK:
                View orderListView =  LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.order_list_layout_item, parent, false );
                return new OrderListViewHolder(orderListView);

            case ORDER_LIST_NEW_ORDER:
            case ORDER_LIST_PREPARING:
            case ORDER_LIST_READY_TO_DELIVER:
                View newOrderListView =  LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.new_order_list_layout_item, parent, false );
                return new NewOrderListViewHolder(newOrderListView);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderListModel orderListModel = orderListModelList.get( position );
        OrderProductsModel orderProductsModel = new OrderProductsModel();
        orderProductsModel.setData( (Map <String, Object>) orderListModel.getOrderProductItemsList().get( 0 ) );

        String orderID = orderListModel.getOrderID();
        String pName = orderProductsModel.getProductName();
        String oItemsAmounts = orderListModel.getProductAmounts();
        String oStatus = orderListModel.getDeliveryStatus();
        String oDate = orderListModel.getOrderDate();
        String oTime = orderListModel.getOrderTime();
        int oTotalItems = orderListModel.getOrderProductItemsList().size();
        String pImage = orderProductsModel.getProductImage();
        switch (listType){
            case ORDER_LIST_CHECK:
                // TODO : Set Data...
                ((OrderListViewHolder)holder).setData( orderID, pName, oItemsAmounts, oStatus, oDate, oTime, oTotalItems, pImage );
                break;
            case ORDER_LIST_NEW_ORDER:
            case ORDER_LIST_PREPARING:
            case ORDER_LIST_READY_TO_DELIVER:
                ((NewOrderListViewHolder)holder).setData( orderID, pName, oItemsAmounts, oStatus, oDate, oTime, oTotalItems, pImage, position );
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        if ( listType == ORDER_LIST_CHECK ){
            OrderListFragment.totalOrders.setText( "("+orderListModelList.size()+")" );
        }
        return orderListModelList.size();
    }

    //-------------------------------------------------
    public class OrderListViewHolder extends RecyclerView.ViewHolder {

        private TextView orderId;
        private TextView productName;
        private TextView orderItemsAmount;
        private TextView orderStatus;
        private TextView orderTime;
        private TextView totalItems;
        private ImageView productImage;

        public OrderListViewHolder(@NonNull View itemView) {
            super( itemView );

            orderId = itemView.findViewById( R.id.order_id );
            productName = itemView.findViewById( R.id.product_name );
            orderItemsAmount = itemView.findViewById( R.id.product_items_amount );
            orderStatus = itemView.findViewById( R.id.order_status );
            orderTime = itemView.findViewById( R.id.order_time );
            totalItems = itemView.findViewById( R.id.product_qty );
            productImage = itemView.findViewById( R.id.product_image );

        }

        private void setData(final String orderID, String pName, String oItemsAmounts, String oStatus, String oDate, String oTime, int oTotalItems, String pImage ){

            // set Image Resource from database..
            // Current Date : "yyyy/MM/dd"
            // Current Time : "HH:mm"

            Glide.with( itemView.getContext() ).load( pImage )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( productImage );

            orderId.setText( "Order ID : " + orderID );
            productName.setText( pName );
            orderItemsAmount.setText( "Rs." + oItemsAmounts +"/-" );
            orderStatus.setText( oStatus );
            orderTime.setText( "Order " + StaticMethods.getTimeFromNow( oDate, oTime +":00" ) );
            totalItems.setText( String.valueOf( oTotalItems ) );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showToast(itemView.getContext(), "Code Not Found!");
                    Intent orderViewIntent = new Intent( itemView.getContext(), OrderViewActivity.class );
                    orderViewIntent.putExtra( "ORDER_ID" , orderID );
                    orderViewIntent.putExtra( "ORDER_STATUS" , ORDER_LIST_CHECK);
                    orderViewIntent.putExtra( "ORDER_STATUS_STRING" , oStatus);
                    itemView.getContext().startActivity( orderViewIntent );
                }
            } );

        }


    }

    public class NewOrderListViewHolder extends RecyclerView.ViewHolder implements OrderUpdateListener{
        private TextView orderId;
        private TextView productName;
        private TextView orderItemsAmount;
        private TextView orderStatus;
        private TextView orderTime;
        private TextView totalItems;
        private ImageView productImage;
        // New Order Action Layout...
        private LinearLayout newOrderActionLayout;
        private TextView acceptOrderBtn;
        // Order Preparing Text Button...
        private TextView packingTextBtn;
        // Out For Delivery Layout...
        private LinearLayout outForDeliveryLayout;
        private EditText outForDeliveryPinEt;
        private TextView outForDeliveryBtn;
        //

        private OrderUpdateQuery orderUpdateQuery;
        private Dialog dialog;
        private final OrderProductsModel orderProductsModel = new OrderProductsModel();

        public NewOrderListViewHolder(@NonNull View itemView) {
            super( itemView );
            orderId = itemView.findViewById( R.id.order_id );
            productName = itemView.findViewById( R.id.product_name );
            orderItemsAmount = itemView.findViewById( R.id.product_items_amount );
            orderStatus = itemView.findViewById( R.id.order_status );
            orderTime = itemView.findViewById( R.id.order_time );
            totalItems = itemView.findViewById( R.id.product_qty );
            productImage = itemView.findViewById( R.id.product_image );
            // ---
            newOrderActionLayout = itemView.findViewById( R.id.new_order_action_layout );
            acceptOrderBtn = itemView.findViewById( R.id.order_accept_text );
            // ------
            packingTextBtn = itemView.findViewById( R.id.preparing_packing_text_btn );
            // ---
            outForDeliveryLayout = itemView.findViewById( R.id.new_order_out_for_delivery_layout );
            outForDeliveryPinEt = itemView.findViewById( R.id.out_for_delivery_pin_et );
            outForDeliveryBtn = itemView.findViewById( R.id.out_for_delivery_text_btn );

        }

        private void setData(final String orderID, String pName, String oItemsAmounts, String oStatus
                , String oDate, String oTime, int oTotalItems, String pImage, final int index ){
            // setData...
            orderProductsModel.setData( (Map <String, Object>)  orderListModelList.get( index ).getOrderProductItemsList().get( 0 ) );

            orderUpdateQuery = new OrderUpdateQuery();
            dialog = new Dialog( itemView.getContext() );
            // Decide based On List Type...
            switch (listType){
                case ORDER_LIST_NEW_ORDER:
                    newOrderActionLayout.setVisibility( View.VISIBLE );
                    packingTextBtn.setVisibility( View.GONE );
                    outForDeliveryLayout.setVisibility( View.GONE );
                    break;
                case ORDER_LIST_PREPARING:
                    newOrderActionLayout.setVisibility( View.GONE );
                    packingTextBtn.setVisibility( View.VISIBLE );
                    outForDeliveryLayout.setVisibility( View.GONE );
                    break;
                case ORDER_LIST_READY_TO_DELIVER:
                    newOrderActionLayout.setVisibility( View.GONE );
                    packingTextBtn.setVisibility( View.GONE );
                    outForDeliveryLayout.setVisibility( View.VISIBLE );
                    break;
                default:
                    break;
            }

            // set Image Resource from database...
            Glide.with( itemView.getContext() ).load( pImage )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( productImage );

            orderId.setText( "Order ID : " + orderID );
            productName.setText( pName );
            orderItemsAmount.setText( "Rs." + oItemsAmounts +"/-" );
            orderStatus.setText( oStatus );
            orderTime.setText( "Order " + StaticMethods.getTimeFromNow( oDate, oTime +":00" ) );
            totalItems.setText( String.valueOf( oTotalItems ) );

            itemView.setOnClickListener( view -> {
//                    showToast(itemView.getContext(), "Code Not Found!");
                Intent orderViewIntent = new Intent( itemView.getContext(), OrderViewActivity.class );
                orderViewIntent.putExtra( "ORDER_ID" , orderID );
                orderViewIntent.putExtra( "ORDER_STATUS" , listType);
                itemView.getContext().startActivity( orderViewIntent );
            } );

            // Accept Order Action...
            acceptOrderBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAcceptOrderBtn( index );
                }
            } );
            // Packing Order Action...
            packingTextBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPackingTextBtn( index );
                }
            } );
            // Out For Delivery Action...
            outForDeliveryBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty( outForDeliveryPinEt.getText().toString() )){
                        outForDeliveryPinEt.setError( "Required!" );
                    }
                    else if(outForDeliveryPinEt.getText().toString().length() < 4){
                        outForDeliveryPinEt.setError( "Wrong Pin!" );
                    }else{
                        showDialog();
                   //     getDeliveryOtp( orderListModelList.get( index ).getDeliveryID(), otpPin, orderProductsModel );
                        orderUpdateQuery.onCheckOTPQuery( NewOrderListViewHolder.this, index,
                                orderListModelList.get( index ).getDeliveryID(), outForDeliveryPinEt.getText().toString()  );
                    }
                }
            } );

        }

        private void setAcceptOrderBtn(int index){
            showDialog();
            // -------------------------
            Map <String, Object> updateMap = new HashMap <>();
            updateMap.put( "delivery_status", ORDER_ACCEPTED );
            orderListModelList.get( index ).setDeliveryStatus( ORDER_ACCEPTED );
//            DBQuery.updateOrderStatus( null, orderListModelList.get( index ) ,updateMap ); // TODO : UPDATE

            orderUpdateQuery.updateOrderStatus( this, orderListModelList.get( index ), updateMap, index );

            // For Notify User...
            sendNotificationToUser( index, "Your Order preparing to pack" );

        }

        private void setPackingTextBtn(int index){
            showDialog();
            // For Notify User...
            sendNotificationToUser( index,  "Your Order has been packed! Waiting for delivery..." );

            queryToDeliveryBoy( index );

        }

        private void queryToDeliveryBoy(int index){

            final String vOTP = StaticMethods.getOTPDigitRandom(); // 4 Digit...
            orderListModelList.get( index ).setOutForDeliveryOTP( vOTP );

            OrderListModel orderListModel = orderListModelList.get( index );

            Map <String, Object> deliveryMap = new HashMap <>();
            deliveryMap.put( "order_id", orderListModel.getOrderID() );
            deliveryMap.put( "delivery_status", ORDER_ACCEPTED );
            deliveryMap.put( "verify_otp", vOTP );
            deliveryMap.put( "accepted_date", StaticMethods.getCrrDate() );
            deliveryMap.put( "accepted_time", StaticMethods.getCrrTime() );
            deliveryMap.put( "shop_id", SHOP_ID );
            deliveryMap.put( "shop_name", SHOP_DATA_MODEL.getShop_name() );
            deliveryMap.put( "shop_logo", SHOP_DATA_MODEL.getShop_logo() );
            deliveryMap.put( "shop_address", SHOP_DATA_MODEL.getShop_address() );
            deliveryMap.put( "shop_helpline", SHOP_DATA_MODEL.getShop_help_line() );
            deliveryMap.put( "shop_pin", SHOP_DATA_MODEL.getShop_area_code() );
            deliveryMap.put( "shipping_address", orderListModel.getShippingAddress() );
            deliveryMap.put( "shipping_pin", orderListModel.getShippingPinCode() );
            deliveryMap.put( "shop_geo_point", SHOP_DATA_MODEL.getShop_geo_point() );
            deliveryMap.put( "shipping_geo_point", orderListModel.getShippingGeoPoint() );

            // set document in delivery section and then we update in order document...
//            DBQuery.setDeliveryDocument( null, deliveryMap, orderListModelList.get( index ));

            orderUpdateQuery.setDeliveryDocument( this, deliveryMap, orderListModelList.get( index ), index );

        }

        private void sendNotificationToUser( int index, String title){
            try{
                // Create Map For Notify User...
                String notifyId = StaticMethods.getFiveDigitRandom();
                UserNotificationModel notificationModel = new UserNotificationModel();
                notificationModel.setNotify_type( 1 );
                notificationModel.setNotify_id( notifyId );
                notificationModel.setNotify_click_id(  orderListModelList.get( index ).getOrderID()  );
                notificationModel.setNotify_image( orderProductsModel.getProductImage()  );
                notificationModel.setNotify_title( title );
                notificationModel.setNotify_body( orderProductsModel.getProductName() );
                // Notify User...
//            DBQuery.sentNotificationToUser(  orderListModelList.get( index ).getCustAuthID(), notificationModel.getMap() );

                Thread notifyThread = new Thread( new StaticMethods.SendUserNotification( notifyId, orderListModelList.get( index ).getCustAuthID(), notificationModel.getMap()));
                notifyThread.start();
            }catch (Exception e){ // Out of bound in the list...
                e.printStackTrace();
            }
        }

        @Override
        public void onOrderUpdateSuccess(String updateValue, int index) {
            dismissDialog();
            if (updateValue.toUpperCase().equals( ORDER_ACCEPTED )){ // Preparing...
                // ------------ notify...
                orderListModelList.remove( index );
                // Show the No Order Text.. If List size = 0;
                if (orderListModelList.size() == 0){
                    NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_NEW_ORDER, View.VISIBLE );
                }

            } else  if (updateValue.toUpperCase().equals( ORDER_PACKED )){ // Ready to Delivery...
                // remove...
                orderListModelList.remove( index );
                // Show the No Order Text.. If List size = 0;
                if (orderListModelList.size() == 0){
                    NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_PREPARING, View.VISIBLE );
                }

            } else  if (updateValue.toUpperCase().equals( ORDER_PICKED )){ // Out For Delivery...
                // By Default Done...
                showToast( "Successful, Out for Delivery..!" );
                dismissDialog();
            }
            // Notify Data Changed...
            OrderListAdaptor.this.notifyDataSetChanged();
        }

        @Override
        public void onOrderUpdateFailed(String updateValue,@Nullable OrderListModel orderListModel,@Nullable Map<String, Object> updateMap, int index) {
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
                    // Thread : Send Notification To user
                    sendNotificationToUser( temIndex, "Your Order has been out for delivery!" );

                    //  : UPDATE in the Order List...
                    Map <String, Object> updateMap  = new HashMap <>();
                    updateMap.put( "delivery_status", ORDER_PICKED );
                    // Update In Delivery Document...
                    orderUpdateQuery.updateDeliveryDocument(NewOrderListViewHolder.this
                            , orderListModelList.get( temIndex ).getOrderID()
                            , SHOP_DATA_MODEL.getShop_city_code()
                            , readyToDeliveredList.get( temIndex ).getDeliveryID(), updateMap );

//                    dismissDialog();
                    break;
                case 2: // NOT VERIFIED
                    showToast( "Not Matched! Please try again!" );
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
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }

        @Override
        public void showDialog() {
            if (!dialog.isShowing()){
                dialog.show();
            }
        }

        @Override
        public void showToast(String msg) {
            Toast.makeText( itemView.getContext(), msg, Toast.LENGTH_SHORT ).show();
        }
    }

    /**  Order Status
     *          1. WAITING - ( For Accept )
     *          2. ACCEPTED - ( Preparing )
     *          3. PACKED - ( Waiting for Delivery ) READY_TO_DELIVERY
     *          4. PROCESS  - When Any Delivery Boy Accept to Delivering...
     *          5. PICKED - ( On Delivery ) OUT_FOR_DELIVERY...
     *          6. SUCCESS - Success Full Delivered..!
     *          7. CANCELLED -  When Order has been cancelled by user...
     *          8. FAILED -  when PayMode Online and payment has been failed...
     *          9. PENDING - when Payment is Pending...
     *
     */

}


