package ean.ecom.eanshopadmin.product.update;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import android.app.Dialog;
import android.net.Uri;
import android.widget.Toast;

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

import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

/**
 * Created by Shailendra (WackyCodes) on 30/08/2020 09:56
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class UpdateDataRequest implements UpdateData.UpdateRequest, UpdateData.UpdateImageOnServerRequest{

    @Override
    public void onUpdateRequest(final OnUpdateFinisher onUpdateFinisher, String productID, Map <String, Object> updateMap) {
        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( "PRODUCTS" ).document( productID  )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            onUpdateFinisher.onUpdateFinished( true );
                        }else{
                            onUpdateFinisher.dismissDialog();
                            onUpdateFinisher.onUpdateFinished( false );
                        }
                    }
                } );
    }

    @Override
    public void onImageUploadRequest(final OnUploadImageFinisher onUploadImageFinisher, Uri fileUri, final StorageReference storageRef) {

        final UploadTask uploadTask = storageRef.putFile(fileUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
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
                            onUploadImageFinisher.onUploadImageFinished( task.getResult().toString() );
//                            onUploadImageFinisher.dismissDialog();
                        }else{
                            // Failed Query to getDownload Link...
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


}
