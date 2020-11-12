package ean.ecom.eanshopadmin.other;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import ean.ecom.eanshopadmin.R;


public class CheckInternetConnection implements OnInternetConnectListener.OnCheckingListener {

    private OnInternetConnectListener listener;

    public CheckInternetConnection(OnInternetConnectListener listener) {
        this.listener = listener;
    }

    private static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo;
        netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo( ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo( ConnectivityManager.TYPE_MOBILE);
            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else
            return false;
    }

    public static boolean isInternetConnected(Context context){
        if(!isConnected(context)){
            AlertDialog.Builder dialog = DialogsClass.alertDialog( context, "No Internet Connection!", null );
            dialog.show();
//            try{
//                do{
//                    if (!dialog.isShowing()){
//                        dialog.show();
//                    }
//                    if (isConnected( context )){
//                        dialog.dismiss();
//                    }
//                }while (!isConnected( context ));
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }

            if (isConnected( context )){
                return true;
            }else {
                return false;
            }
        }
        else{
           return true;
        }
    }

    public static void isInternetConnected(Context context,  OnInternetConnectListener listener){
        if(!isConnected(context)){
            listener.onInternetResponse( false );
        }
        else{
            listener.onInternetResponse( true );
        }
    }


    @Override
    public void onConnected(OnInternetConnectListener listener) {
//        Thread newThread = new Thread(new MyInternet( ));
//        newThread.start();
    }

    @Override
    public void onDisconnected(OnInternetConnectListener listener) {

    }

    /*
     public static AlertDialog.Builder alertDialog(Context c, @Nullable String title, @NonNull String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        if (title!=null)
            builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }
     */

}
