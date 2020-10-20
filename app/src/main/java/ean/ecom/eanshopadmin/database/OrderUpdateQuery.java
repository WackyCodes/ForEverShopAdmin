package ean.ecom.eanshopadmin.database;

import android.app.Dialog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.eanshopadmin.main.orderlist.OrderListModel;
import ean.ecom.eanshopadmin.main.orderlist.OrderUpdateListener;
import ean.ecom.eanshopadmin.main.orderlist.neworder.NewOrderTabAdaptor;

import static ean.ecom.eanshopadmin.database.AdminQuery.getShopCollectionRef;
import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.database.DBQuery.preparingOrderList;
import static ean.ecom.eanshopadmin.database.DBQuery.readyToDeliveredList;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_ACCEPTED;
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

                            } else  if (statusCode.toUpperCase().equals( ORDER_PACKED )){ // Ready to Delivery...
                                readyToDeliveredList.add( orderListModel );
                                NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_READY_TO_DELIVER, View.GONE );

                            } else  if (statusCode.toUpperCase().equals( ORDER_PICKED )){ // Out For Delivery...
//                                readyToDeliveredList.remove( orderListModel );
                                // By Default Done...
                                if (readyToDeliveredList.size()==0)
                                    NewOrderTabAdaptor.setNoOrderText( ORDER_LIST_READY_TO_DELIVER, View.VISIBLE );
                            }
                            listener.onOrderUpdateSuccess( statusCode, index );
                        }else{
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


}
