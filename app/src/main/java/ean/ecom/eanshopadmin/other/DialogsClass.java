package ean.ecom.eanshopadmin.other;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import ean.ecom.eanshopadmin.MainActivity;
import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.SetFragmentActivity;

import static ean.ecom.eanshopadmin.other.StaticValues.CHANNEL_ID;

public class DialogsClass {

    public static Dialog getMessageDialog(Context context, @NonNull String title, @NonNull String message){
//         Single Ok Button ...
        final Dialog dialog = new Dialog( context );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialog.setContentView( R.layout.dialog_message_ok_layout );
        dialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        dialog.setCancelable( false );
        Button okBtn = dialog.findViewById( R.id.dialog_ok_btn );
        TextView titleText = dialog.findViewById( R.id.dialog_title );
        TextView messageText = dialog.findViewById( R.id.dialog_message );
        titleText.setText( title );
        messageText.setText( message );
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        } );
        return dialog;
    }

    public static Dialog getDialog(Context context){
        // ---- Progress Dialog...
        Dialog progressDialog = new Dialog( context );
        progressDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        progressDialog.setContentView( R.layout.dialog_circle_process );
        progressDialog.setCancelable( false );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getDialog.getWindow().setBackgroundDrawable( context.getDrawable( R.drawable.x_ractangle_layout ) );
        }
        progressDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
//        getDialog.show();
        return progressDialog;
        // ---- Progress Dialog...
    }

    public static Dialog NoInternetDialog(Context c){
        Dialog progressDialog = new Dialog( c );
        progressDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        progressDialog.setContentView( R.layout.no_internet_dialog );
        progressDialog.setCancelable( false );
        progressDialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        return progressDialog;
    }

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


    public static void setAlarmOnNotification(Context context, String notifyTitle, String notifyBody , int notifyType){
        // Default Sound..
        Uri alarmSound = RingtoneManager. getDefaultUri (RingtoneManager.TYPE_NOTIFICATION );
//        MediaPlayer mp = MediaPlayer. create ( context, alarmSound );
//        mp.start();

        // Vibrate...
        Vibrator vibrator = (Vibrator) context.getSystemService( Context.VIBRATOR_SERVICE );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate( VibrationEffect.createOneShot( 200, VibrationEffect.DEFAULT_AMPLITUDE ) );
        }else{
            vibrator.vibrate( 200 );
        }

        // create Builder and set message...
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "default" )
                        .setSmallIcon( R.mipmap.logo_round )
                        .setContentTitle( notifyTitle )
                        .setContentText( notifyBody ) ;
        // Set Notification Sound...
        mBuilder.setSound( alarmSound, AudioManager.STREAM_NOTIFICATION );

        // Create Intent and Set Action...
        Intent intent;
        if (MainActivity.mainActivity != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!MainActivity.mainActivity.isDestroyed()){
                    intent = new Intent( context, SetFragmentActivity.class );
                    intent.putExtra( "FRAGMENT_NO", notifyType ); // REQUEST_TO_NOTIFY_NEW_ORDER
                    final PendingIntent resultIntent = PendingIntent.getActivity(
                            context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                    );

                    mBuilder.addAction(  R.drawable.ic_notifications_black_24dp,"view", resultIntent );
                    mBuilder.setContentIntent( resultIntent );
                }
            }
            else{
                intent = new Intent( context, SetFragmentActivity.class );
                intent.putExtra( "FRAGMENT_NO", notifyType );
                final PendingIntent resultIntent = PendingIntent.getActivity(
                        context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                );

                mBuilder.addAction(  R.drawable.ic_notifications_black_24dp,"view", resultIntent );
                mBuilder.setContentIntent( resultIntent );
            }
        }
        else{
            intent = new Intent( context, MainActivity.class );
            final PendingIntent resultIntent = PendingIntent.getActivity(
                    context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
            );

            mBuilder.addAction(  R.drawable.ic_notifications_black_24dp,"view", resultIntent );
            mBuilder.setContentIntent( resultIntent );
        }

        // To cancel/ remove the notification if user click..
        mBuilder.setAutoCancel( true );

        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService( Context.NOTIFICATION_SERVICE );
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, notifyTitle, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            // Set Channel ID...
            mBuilder.setChannelId( CHANNEL_ID );
        }

        mNotificationManager.notify( 2 , mBuilder.build());
    }
    public static void setUserNotification(Context context, String notifyTitle, String notifyBody , int notifyType){
        // Default Sound..
        Uri alarmSound = RingtoneManager. getDefaultUri (RingtoneManager.TYPE_NOTIFICATION );
//        MediaPlayer mp = MediaPlayer. create ( context, alarmSound );
//        mp.start();

        // Vibrate...
        Vibrator vibrator = (Vibrator) context.getSystemService( Context.VIBRATOR_SERVICE );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate( VibrationEffect.createOneShot( 200, VibrationEffect.DEFAULT_AMPLITUDE ) );
        }else{
            vibrator.vibrate( 200 );
        }

        // create Builder and set message...
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "default" )
                        .setSmallIcon( R.mipmap.logo_round )
                        .setContentTitle( notifyTitle )
                        .setContentText( notifyBody ) ;
        // Set Notification Sound...
        mBuilder.setSound( alarmSound, AudioManager.STREAM_NOTIFICATION );

        // Create Intent and Set Action...
        Intent intent;
        if (MainActivity.mainActivity != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!MainActivity.mainActivity.isDestroyed()){
                    intent = new Intent( context, SetFragmentActivity.class );
                    intent.putExtra( "FRAGMENT_NO", notifyType ); // REQUEST_TO_NOTIFY_NEW_ORDER
                    final PendingIntent resultIntent = PendingIntent.getActivity(
                            context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                    );
                    mBuilder.addAction(  R.drawable.ic_notifications_black_24dp,"view", resultIntent );
                    mBuilder.setContentIntent( resultIntent );
                }
            }
            else{
                intent = new Intent( context, SetFragmentActivity.class );
                intent.putExtra( "FRAGMENT_NO", notifyType );
                final PendingIntent resultIntent = PendingIntent.getActivity(
                        context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                );
                mBuilder.addAction(  R.drawable.ic_notifications_black_24dp,"view", resultIntent );
                mBuilder.setContentIntent( resultIntent );
            }
        }
        else{
            intent = new Intent( context, MainActivity.class );
            final PendingIntent resultIntent = PendingIntent.getActivity(
                    context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
            );
            mBuilder.addAction(  R.drawable.ic_notifications_black_24dp,"view", resultIntent );
            mBuilder.setContentIntent( resultIntent );
        }

//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE );
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService( Context.NOTIFICATION_SERVICE );
        mNotificationManager.notify( 1 , mBuilder.build());
    }


    private void setActionOnNotification(Context context){

        Intent intent = new Intent( context, MainActivity.class );

        final PendingIntent resultIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
        );

        // Now Set it On Builder...
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "default" );
        mBuilder.addAction( R.mipmap.logo_round, "Add", resultIntent );
        mBuilder.setContentIntent( resultIntent );

    }

  /*  private void createNotificationChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    */
}
