package ean.ecom.eanshopadmin.main.shop;

import android.app.Dialog;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

import ean.ecom.eanshopadmin.main.shop.UpdateShop;

import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

/**
 * Created by Shailendra (WackyCodes) on 09/09/2020 22:48
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class UpdateShopQuery implements UpdateShop.OnImageRequest , UpdateShop {

    @Override
    public void onImageUploadRequest(final OnImageRequestComplete onUploadImageFinisher, Uri fileUri, final StorageReference storageRef){

        final UploadTask uploadTask = storageRef.putFile(fileUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads...
                onUploadImageFinisher.showToast("Failed to upload..! Something went wrong");
                onUploadImageFinisher.dismissDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener <UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // On Upload Success...
                // Get Download Link...
                storageRef.getDownloadUrl().addOnCompleteListener( new OnCompleteListener <Uri>() {
                    @Override
                    public void onComplete(@NonNull Task <Uri> task) {
                        if (task.isSuccessful()){
                            // Get Download Image Link Success...
                            onUploadImageFinisher.onUploadImageFinished( task.getResult().toString(), true );
//                            onUploadImageFinisher.dismissDialog();
                        }else{
                            // Failed Query to getDownload Link...
                            onUploadImageFinisher.onUploadImageFinished( "", false );
                            onUploadImageFinisher.dismissDialog();
                            onUploadImageFinisher.showToast( task.getException().getMessage().toString() );
                        }
                    }
                } );
            }
        }).addOnProgressListener(new OnProgressListener <UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // Show Percentage...
//                onUploadImageFinisher.showDialog();
//                TextView perText = perDialog.findViewById( R.id.process_per_complete_text );
//                int progress = (int)((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
//                perText.setText( "Image " + (uploadedSize + 1) + " Uploading " + progress + "% completed");
            }
        }).addOnPausedListener(new OnPausedListener <UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                perText.setText( "Uploading Pause.! \n Check your net connection.!" );
                onUploadImageFinisher.showToast( "Uploading Pause.! \n Check your net connection.!" );
            }
        });
    }

    @Override
    public void onUpdateShopOnDatabase(final OnImageRequestComplete onImageRequestComplete, final Map <String, Object> updateMap) {

        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
//                            onImageRequestComplete.onUpdateFinish( true );
                            requestToUpdateHomeList( null, null, onImageRequestComplete, -1, updateMap);
                        }else{
                            onImageRequestComplete.dismissDialog();
                            onImageRequestComplete.onUpdateFinish( false );
                        }
                    }
                } );

    }

    @Override
    public void onUpdateShopRequest(@Nullable final Dialog dialog, final AddImageListener updateRequestListener,
                                    final int requestCode, final Map <String, Object> updateMap) {
        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
//                            updateRequestListener.onAddImageSuccess( requestCode );
//                            updateRequestListener.showToast( "Update Successfully!" );
                            requestToUpdateHomeList( dialog, updateRequestListener, null, requestCode, updateMap);
                        }else{
                            updateRequestListener.onAddImageFailed();
                            updateRequestListener.showToast( "Failed to update!" );
                            dialog.dismiss();
                        }

                    }
                } );

    }

    private void requestToUpdateHomeList(@Nullable final Dialog dialog, @Nullable final AddImageListener updateRequestListener,
                                         @Nullable final OnImageRequestComplete onImageRequestComplete, final int requestCode, final Map <String, Object> updateMap ){
        // One More Query Needed here...
        firebaseFirestore.collection( "HOME_PAGE" ).document( ADMIN_DATA_MODEL.getShopCityCode() )
                .collection( "SHOPS" ).document( SHOP_ID )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            // Update Successfully..!
                            if (onImageRequestComplete!=null){
                                onImageRequestComplete.onUpdateFinish( true );
                            }

                            if (updateRequestListener != null){
                                updateRequestListener.onAddImageSuccess( requestCode );
                                updateRequestListener.showToast( "Update Successfully!" );
                            }

                        }
                        else{
                            // Update Failed...!
                            if (onImageRequestComplete!=null){
                                onImageRequestComplete.dismissDialog();
                                onImageRequestComplete.onUpdateFinish( false );
                            }

                            if (updateRequestListener!=null){
                                updateRequestListener.onAddImageFailed();
                                updateRequestListener.showToast( "Failed to update!" );
                            }
                        }
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                    }
                } );
    }



}
