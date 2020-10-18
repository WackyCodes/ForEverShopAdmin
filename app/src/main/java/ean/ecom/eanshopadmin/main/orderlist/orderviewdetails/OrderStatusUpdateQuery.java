package ean.ecom.eanshopadmin.main.orderlist.orderviewdetails;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */


import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;


import static ean.ecom.eanshopadmin.database.AdminQuery.getShopCollectionRef;

/**
 * Created by Shailendra (WackyCodes) on 15/09/2020 18:06
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderStatusUpdateQuery implements OrderViewInteractor.OrderStatusUpdator{

    public static ListenerRegistration newOrderNotificationLR;


    @Override
    public void onUpdateStatusQuery(OrderViewInteractor orderStatusUpdator, Map <String, Object> updateMap) {

    }

    @Override
    public void onFindDeliveryBoyQuery(final OrderViewInteractor orderStatusUpdator, String orderID) {

        newOrderNotificationLR = getShopCollectionRef( "ORDERS" )
                .document( orderID )
                .addSnapshotListener( new EventListener <DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot!=null){
                            if(documentSnapshot.get( "delivery_by_uid" )!= null){
                                Map<String, Object> deliveryBoyInfo = new HashMap <>();

                                deliveryBoyInfo.put( "delivery_by_uid",  documentSnapshot.get( "delivery_by_uid" ).toString());
                                deliveryBoyInfo.put( "delivery_by_name",  documentSnapshot.get( "delivery_by_name" ).toString());
                                deliveryBoyInfo.put( "delivery_by_mobile",  documentSnapshot.get( "delivery_by_mobile" ).toString());
                                deliveryBoyInfo.put( "delivery_id",  documentSnapshot.get( "delivery_id" ).toString());
//                                deliveryBoyInfo.put( "delivery_date",  documentSnapshot.get( "delivery_date" ).toString());
//                                deliveryBoyInfo.put( "delivery_day",  documentSnapshot.get( "delivery_day" ).toString());
//                                deliveryBoyInfo.put( "delivery_time",  documentSnapshot.get( "delivery_time" ).toString());
                                orderStatusUpdator.onDeliveryBoyFound( deliveryBoyInfo );
                            }
                        }

                    }
                } );
    }


}

