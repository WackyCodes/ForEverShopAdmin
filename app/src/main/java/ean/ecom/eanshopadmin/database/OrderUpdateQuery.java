package ean.ecom.eanshopadmin.database;

import android.app.Dialog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.eanshopadmin.main.orderlist.OrderListModel;
import ean.ecom.eanshopadmin.main.orderlist.OrderUpdateListener;
import ean.ecom.eanshopadmin.main.orderlist.neworder.NewOrderTabAdaptor;
import ean.ecom.eanshopadmin.main.orderlist.orderviewdetails.OrderViewInteractor;

import static ean.ecom.eanshopadmin.database.AdminQuery.getShopCollectionRef;
import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.database.DBQuery.preparingOrderList;
import static ean.ecom.eanshopadmin.database.DBQuery.readyToDeliveredList;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_ACCEPTED;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_OUT_FOR_DELIVERY;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_PREPARING;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_READY_TO_DELIVER;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_PACKED;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_PICKED;

/**
 * Created by Shailendra (WackyCodes) on 20/10/2020 23:51
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderUpdateQuery {

    // Query to Delivery Boy...  ------------------------
    public void setDeliveryDocument(final OrderUpdateListener listener, Map <String, Object> deliveryMap,
                                    final OrderListModel orderListModel, final int index ){

//        deliveryMap.put( "shop_geo_point", ADMIN_DATA_MODEL.getShopGeoPoint() );
//        deliveryMap.put( "shipping_geo_point", orderListModel.getShippingGeoPoint() );

        firebaseFirestore.collection( "DELIVERY" )
                .document( ADMIN_DATA_MODEL.getShopCityCode() )
                .collection( "DELIVERY" )
                .add( deliveryMap )
                .addOnCompleteListener( new OnCompleteListener <DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentReference> task) {
                        if (task.isSuccessful()){
                            String deliveryID = task.getResult().getId();
                            // Now we have to update Delivery Details in the Order Document...
                            Map <String, Object> updateMap = new HashMap <>();
                            updateMap.put( "delivery_status", ORDER_PACKED );
                            updateMap.put( "delivery_id", deliveryID );
                            orderListModel.setDeliveryID( deliveryID );
                            orderListModel.setDeliveryStatus( ORDER_PACKED );
                            updateOrderStatus( listener, orderListModel ,updateMap, index );

                        }else{
                            listener.onUpdateDeliveryFailed();
                        }
                    }
                } );

    }

    //  Update Order Status on the database.. ------------------------
    public void updateOrderStatus(final OrderUpdateListener listener,
                                  final OrderListModel orderListModel, final Map<String, Object> updateMap, final int index){
        getShopCollectionRef( "ORDERS" )
                .document( orderListModel.getOrderID() )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        String statusCode = updateMap.get( "delivery_status" ).toString();
                        if (task.isSuccessful()){
                            // TODO: Update in Local List...
                            if (statusCode.toUpperCase().equals( ORDER_ACCEPTED )){ // Preparing...
                                preparingOrderList.add( orderListModel );
                                NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_PREPARING, View.GONE );

                            }
                            else  if (statusCode.toUpperCase().equals( ORDER_PACKED )){ // Ready to Delivery...
                                readyToDeliveredList.add( orderListModel );
                                NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_READY_TO_DELIVER, View.GONE );
                            }
                            else  if (statusCode.toUpperCase().equals( ORDER_PICKED )){ // Out For Delivery...
                                // Update readyToDeliveredList
                                for ( OrderListModel model : readyToDeliveredList){
                                    if (model.getOrderID().equals( orderListModel.getOrderID() )){
                                        readyToDeliveredList.remove( model );
                                        break;
                                    }
                                }
                                // By Default Done...
                                if (readyToDeliveredList.size()==0)
                                    NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_READY_TO_DELIVER, View.VISIBLE );
                            }
                            listener.onOrderUpdateSuccess( statusCode, index );
                        }
                        else{
                            // Failed...
                            if (statusCode.toUpperCase().equals( ORDER_PACKED )){
                                listener.onOrderUpdateFailed( statusCode, orderListModel, updateMap, index );
                            }else{
                                listener.onOrderUpdateFailed( statusCode, null, null, index );
                            }

                        }
                    }
                } );
    }

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

    public void onCheckOTPQuery(final OrderUpdateListener listener, final int index, String deliveryId, final String verifyOtp) {
        firebaseFirestore.collection( "DELIVERY" )
                .document( ADMIN_DATA_MODEL.getShopCityCode() )
                .collection( "DELIVERY" )
                .document( deliveryId )
                .get()
                .addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            try{
                                if (task.getResult().get( "verify_otp" ).toString().equals( verifyOtp )){
                                    listener.otpVerificationResponse( 1, String.valueOf( index ) ); // Success
                                }else{
                                    listener.otpVerificationResponse( 2, "Not Matched!" ); // Not verified...
                                }
                            }catch (Exception e){
                                listener.otpVerificationResponse( 3, e.getMessage() ); // Exception..
                            }

                        }else{
                            listener.otpVerificationResponse( 0, task.getException().getMessage() ); // Failed...
                        }
                    }
                } );
    }



    public void updateDeliveryDocument(final OrderUpdateListener listener, final String orderID, String cityCode, String deliveryID , final Map <String, Object> updateMap){
        // Update in Delivery Document...
        firebaseFirestore.collection( "DELIVERY" )
                .document( cityCode ).collection( "DELIVERY" )
                .document( deliveryID )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
//                            onUpdateStatusQuery( listener, orderID, updateMap);
                            OrderListModel orderListModel = new OrderListModel();
                            orderListModel.setOrderID( orderID );
                            updateOrderStatus( listener, orderListModel ,updateMap, -1 );
                        }else{
                            listener.dismissDialog();
                            listener.showToast( "Failed... Please try again!" );
                        }
                    }
                } );


    }

    public void onUpdateStatusQuery(final OrderUpdateListener listener, final String orderID, final Map <String, Object> updateMap) {
        try{
            getShopCollectionRef( "ORDERS" )
                    .document( orderID )
                    .update( updateMap )
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                // Thread: Send Notification to User... -> It's added After OTP verification..
                            }else{
                                listener.dismissDialog();
//                                onUpdateStatusQuery( listener, orderID, updateMap);
                                listener.showToast( "Failed...! Check your internet connection..!" );
                            }
                        }
                    } );
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
