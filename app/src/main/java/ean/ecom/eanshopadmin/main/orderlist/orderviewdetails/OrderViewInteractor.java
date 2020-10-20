package ean.ecom.eanshopadmin.main.orderlist.orderviewdetails;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import android.app.VoiceInteractor;
import android.speech.tts.Voice;

import java.util.Map;

/**
 * Created by Shailendra (WackyCodes) on 15/09/2020 18:06
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface OrderViewInteractor {

    void onDeliveryBoyFound( Map<String, Object> deliveryBoyInfo );
    void onUpdateOrderStatus( int updateCode );
    void dismissDialog();
    void showDialog();
    void showToast(String msg);

    interface OrderStatusUpdator{
        void onUpdateStatusQuery(OrderViewInteractor orderStatusUpdator, String orderID, Map<String, Object> updateMap);
        void onFindDeliveryBoyQuery(OrderViewInteractor orderStatusUpdator, String orderID);
    }

}
