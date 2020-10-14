package ean.ecom.eanshopadmin.database;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.notification.NotificationModel;
import ean.ecom.eanshopadmin.other.DialogsClass;

import static ean.ecom.eanshopadmin.MainActivity.badgeNotifyCount;
import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_NOTIFICATION;

public class AdminQuery {

    public static ListenerRegistration userNotificationLR;

    public static List<NotificationModel> notificationModelList = new ArrayList <>();

    public static void loadAdminData(@Nullable final Context context, @Nullable final Dialog dialog, @NonNull String shopID, @NonNull String mobile){
        // TODO:
        firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .collection( "ADMINS" ).document( mobile )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    // TODO : Assign and Add data...
                }else{

                }
            }
        } );
        // END : loadAdminData
    }

    public static void updateAdminData(@Nullable final Context context, @Nullable final Dialog dialog
            , @NonNull String shopID, @NonNull String mobile, Map<String, Object> updateMap){
        firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .collection( "ADMINS" ).document( mobile )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            // TODO :
                        }else{

                        }
                    }
                } );
    }

    private static CollectionReference getUserCollectionRef(@NonNull String collectionName){
        return firebaseFirestore.collection( "SHOPS" ).document( ADMIN_DATA_MODEL.getShopID()  )
                .collection( "ADMINS" ).document( ADMIN_DATA_MODEL.getAdminMobile()  )
                .collection( collectionName );
    }

    public static CollectionReference getShopCollectionRef(@NonNull String collection){
        return firebaseFirestore.collection( "SHOPS" ).document( ADMIN_DATA_MODEL.getShopID() )
                .collection( collection );
    }

    // Notification ListenerRegistration
    public static void loadNotificationsQuery(@Nullable final Context context){
        userNotificationLR = getShopCollectionRef( "ORDERS" )
                .orderBy( "index", Query.Direction.DESCENDING ) // .limit( 10 ) //order_time. order_date
                .addSnapshotListener( new EventListener <QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null ) {
                            int cartCount = 0;
                            notificationModelList.clear();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                NotificationModel notificationModel = new NotificationModel(
                                        Integer.parseInt( documentSnapshot.get( "notify_type" ).toString()),
                                        documentSnapshot.get( "notify_id" ).toString(),
                                        documentSnapshot.get( "notify_click_id" ).toString(),
                                        documentSnapshot.get( "notify_image" ).toString(),
                                        documentSnapshot.get( "notify_title" ).toString(),
                                        documentSnapshot.get( "notify_body" ).toString(),
                                        documentSnapshot.get( "notify_date" ).toString(),
                                        documentSnapshot.get( "notify_time" ).toString(),
                                        documentSnapshot.getBoolean( "notify_is_read" )
                                );

                                notificationModelList.add( notificationModel );
                                if (!notificationModel.getNotifyIsRead()){
                                    cartCount = cartCount+1;
                                }
                            }

//                            if (NotificationActivity.notificationAdaptor!=null){
//                                NotificationActivity.notificationAdaptor.notifyDataSetChanged();
//                            }

                            if (badgeNotifyCount != null)
                                if (cartCount>0){
                                    badgeNotifyCount.setVisibility( View.VISIBLE );
                                    badgeNotifyCount.setText( cartCount + "" );
                                    if ( context!=null ){
                                        DialogsClass.setAlarmOnNotification( context, "Notification", "You have "+cartCount +" new notification!", REQUEST_TO_NOTIFICATION );
                                    }
                                }else{
                                    badgeNotifyCount.setVisibility( View.GONE );
                                }

                        }
                    }
                } );
    }


}
