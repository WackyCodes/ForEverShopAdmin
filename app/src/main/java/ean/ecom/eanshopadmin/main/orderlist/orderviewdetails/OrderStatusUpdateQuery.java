package ean.ecom.eanshopadmin.main.orderlist.orderviewdetails;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import android.view.View;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import ean.ecom.eanshopadmin.other.DialogsClass;

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
    public void onFindDeliveryBoyQuery(OrderViewInteractor orderStatusUpdator) {

        newOrderNotificationLR = getShopCollectionRef( "ORDERS" )
                .document( "OrderID" )
                .addSnapshotListener( new EventListener <DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot!=null){




                        }

                    }
                } );
    }
}
