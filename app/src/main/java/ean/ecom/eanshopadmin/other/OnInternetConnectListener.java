package ean.ecom.eanshopadmin.other;

/**
 * Created by Shailendra (WackyCodes) on 03/11/2020 17:40
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface OnInternetConnectListener {

    void onInternetResponse( boolean isConnected );
//    boolean isConnected( boolean isConnected );

    interface OnCheckingListener{
        void onConnected(OnInternetConnectListener listener );
        void onDisconnected(OnInternetConnectListener listener );
    }

}
