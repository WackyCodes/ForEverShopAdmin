package ean.ecom.eanshopadmin.main.orderlist;

import androidx.annotation.Nullable;

import java.util.Map;

import ean.ecom.eanshopadmin.main.orderlist.orderviewdetails.OrderViewInteractor;

/**
 * Created by Shailendra (WackyCodes) on 20/10/2020 23:45
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface OrderUpdateListener {

    void onOrderUpdateSuccess( String updateValue, int index );
    void onOrderUpdateFailed(String updateValue, @Nullable OrderListModel orderListModel, @Nullable Map <String, Object> updateMap,  int index );

    void onUpdateDeliveryFailed( );

    void otpVerificationResponse(int responseCode, @Nullable String responseMessage);

    void dismissDialog();
    void showDialog();
    void showToast(String msg);


}
