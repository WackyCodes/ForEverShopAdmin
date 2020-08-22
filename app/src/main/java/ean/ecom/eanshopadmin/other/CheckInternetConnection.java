package ean.ecom.eanshopadmin.other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static ean.ecom.eanshopadmin.other.DialogsClass.alertDialog;

public class CheckInternetConnection {

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
            alertDialog(context, "No Internet Connection !",
                    "Check Your Internet Connection.\nYou need to have Mobile Data or wifi..." ).show();
            return false;
        }
        else
            return true;
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
