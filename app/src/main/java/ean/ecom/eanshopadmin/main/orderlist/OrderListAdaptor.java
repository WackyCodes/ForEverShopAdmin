package ean.ecom.eanshopadmin.main.orderlist;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.main.orderlist.neworder.NewOrderFragment;
import ean.ecom.eanshopadmin.main.orderlist.neworder.NewOrderTabAdaptor;
import ean.ecom.eanshopadmin.other.StaticMethods;

import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_CHECK;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_NEW_ORDER;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_PREPARING;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_READY_TO_DELIVER;
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

        String orderID = orderListModel.getOrderID();
        String pName = orderListModel.getOrderProductItemsList().get( 0 ).getProductName();
        String oItemsAmounts = orderListModel.getProductAmounts();
        String oStatus = orderListModel.getDeliveryStatus();
        String oDate = orderListModel.getOrderDate();
        String oTime = orderListModel.getOrderTime();
        int oTotalItems = orderListModel.getOrderProductItemsList().size();
        String pImage = orderListModel.getOrderProductItemsList().get( 0 ).getProductImage();
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

        private void setData( String orderID, String pName, String oItemsAmounts, String oStatus,  String oDate, String oTime, int oTotalItems, String pImage ){

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
                    showToast(itemView.getContext(), "Code Not Found!");
                }
            } );

        }

    }

    public class NewOrderListViewHolder extends RecyclerView.ViewHolder{
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

        private void setData(String orderID, String pName, String oItemsAmounts, String oStatus, String oDate, String oTime, int oTotalItems, String pImage, final int index ){

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

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast(itemView.getContext(), "Code Not Found!");
                }
            } );

            // Accept Order Action...
            acceptOrderBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    }else if(outForDeliveryPinEt.getText().toString().length() < 4){
                        outForDeliveryPinEt.setError( "Wrong Pin!" );
                    }else{
                        setOutForDeliveryBtn(outForDeliveryPinEt.getText().toString(), index);
                    }
                }
            } );

        }

        private void setAcceptOrderBtn(int index){
            // Create Map For Notify User...
            Map <String, Object> notifyMap = new HashMap <>();
            notifyMap.put( "index", StaticMethods.getRandomIndex() );
            notifyMap.put( "notify_type", 1 );
            notifyMap.put( "notify_id", StaticMethods.getFiveDigitRandom() );
            notifyMap.put( "notify_click_id", orderListModelList.get( index ).getOrderID() );
            notifyMap.put( "notify_image",  orderListModelList.get( index ).getOrderProductItemsList().get( 0 ).getProductImage() );
            notifyMap.put( "notify_title", "Your Order preparing to pack" );
            notifyMap.put( "notify_body",  orderListModelList.get( index ).getOrderProductItemsList().get( 0 ).getProductName() );
            notifyMap.put( "notify_date", StaticMethods.getCrrDate() );
            notifyMap.put( "notify_time", StaticMethods.getCrrTime() );
            notifyMap.put( "notify_is_read", false );
            // Notify User...
            DBQuery.sentNotificationToUser(  orderListModelList.get( index ).getCustAuthID(), notifyMap );

//           Query To Find Delivery Boy... After that Update On Order Document...
            queryToDeliveryBoy(index);

        }

        private void setPackingTextBtn(int index){

            Map <String, Object> updateMap = new HashMap <>();
            updateMap.put( "delivery_status", "PACKED" );
            orderListModelList.get( index ).setDeliveryStatus( "PACKED" );
            DBQuery.updateOrderStatus( null, orderListModelList.get( index ) ,updateMap );

            // Create Map For Notify User...
            Map <String, Object> notifyMap = new HashMap <>();
            notifyMap.put( "index", StaticMethods.getRandomIndex() );
            notifyMap.put( "notify_type", 1 );
            notifyMap.put( "notify_id", StaticMethods.getFiveDigitRandom() );
            notifyMap.put( "notify_click_id", orderListModelList.get( index ).getOrderID() );
            notifyMap.put( "notify_image",  orderListModelList.get( index ).getOrderProductItemsList().get( 0 ).getProductImage() );
            notifyMap.put( "notify_title", "Your Order has been packed! Waiting for delivery..." );
            notifyMap.put( "notify_body",  orderListModelList.get( index ).getOrderProductItemsList().get( 0 ).getProductName() );
            notifyMap.put( "notify_date", StaticMethods.getCrrDate() );
            notifyMap.put( "notify_time", StaticMethods.getCrrTime() );
            notifyMap.put( "notify_is_read", false );
            // Notify User...
            DBQuery.sentNotificationToUser( orderListModelList.get( index ).getCustAuthID(), notifyMap );

            orderListModelList.remove( index );
            // Show the No Order Text.. If List size = 0;
            if (orderListModelList.size() == 0){
                NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_PREPARING, View.VISIBLE );
            }
            // Notify Data Changed...
            OrderListAdaptor.this.notifyDataSetChanged();

        }

        private void setOutForDeliveryBtn(String otpPin, int index){

            getDeliveryOtp( orderListModelList.get( index ).getDeliveryID(), otpPin );

        }

        private void queryToDeliveryBoy(int index){
            final String vOTP = StaticMethods.getOTPDigitRandom(); // 4 Digit...
            orderListModelList.get( index ).setOutForDeliveryOTP( vOTP );

            OrderListModel orderListModel = orderListModelList.get( index );

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

            DBQuery.setDeliveryDocument( null, deliveryMap, orderListModelList.get( index ));

            orderListModelList.remove( index );
            // Show the No Order Text.. If List size = 0;
            if (orderListModelList.size() == 0){
                NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_NEW_ORDER, View.VISIBLE );
            }
            // Notify Data Changed...
            OrderListAdaptor.this.notifyDataSetChanged();

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
                                    // TODO: Out For Delivery...
                                    showToast(  itemView.getContext(), "Otp Matched!" );

                                }else{
                                    outForDeliveryPinEt.setError( "Not Matched!" );
                                }

                            }else{

                            }
                        }
                    } );

        }

    }


}


