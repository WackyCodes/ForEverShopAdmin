package ean.ecom.eanshopadmin.other;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static ean.ecom.eanshopadmin.database.DBQuery.storageReference;

public class UpdateImages {


    public static String uploadImageLink;
    public static String uploadImageBgColor;

    public static boolean isDeletedFile = false;
    public static boolean isUploadSuccess = false;



    public static void uploadImageOnFirebaseStorage(final Context context, final Dialog dialog, final Uri imageUri,
                                                    final ImageView imageView, final String uploadPath, final String fileName){
        uploadImageLink = "";
        isUploadSuccess = false;
        if (imageUri != null){
            final StorageReference storageRef = storageReference.child( uploadPath + "/" + fileName + ".jpg" );

//            Glide.with( context ).asBitmap().load( imageUri ).optionalCenterCrop().into( new ImageViewTarget <Bitmap>( imageView  ){});

            Glide.with( context ).asBitmap().load( imageUri ).into( new ImageViewTarget <Bitmap>( imageView ){
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition <? super Bitmap> transition) {
                    super.onResourceReady( resource, transition );

//                    final TextView perText = dialog.findViewById( R.id.process_per_complete_text );
//                    perText.setVisibility( View.VISIBLE );

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = storageRef.putBytes(data);
                    uploadTask.addOnCompleteListener( new OnCompleteListener <UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                // Query To get Download Link...
                                storageRef.getDownloadUrl().addOnCompleteListener( new OnCompleteListener <Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task <Uri> task) {
                                        if (task.isSuccessful()){
                                            isUploadSuccess = true;
//                                                imageUri = task.getResult();
                                            uploadImageLink = task.getResult().toString();
                                            Glide.with( context ).load( uploadImageLink ).into( imageView );
                                            dialog.dismiss();
                                        }else{
                                            // Failed Query to getDownload Link...
                                            // TODO : Again Request to Get Download Link Or Query to Delete The Image....
                                            deleteImageFromFirebase(context, dialog, uploadPath, fileName);
                                            isUploadSuccess = false;
                                            dialog.dismiss();
                                            showToast( task.getException().getMessage().toString(), context );
                                        }
                                    }
                                } );
                            }else{
                                dialog.dismiss();
                            }
                        }
                    } ).addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            showToast( "Failed to upload..! ", context );
                        }
                    } ).addOnProgressListener(new OnProgressListener <UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int)((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
//                            perText.setText("Uploading " + progress + "% completed");
                        }
                    }).addOnPausedListener(new OnPausedListener <UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                            perText.setText( "Uploading Pause.! \n Check your net connection.!" );
                        }
                    });
                }
                @Override
                protected void setResource(@Nullable Bitmap resource) {
                    // Set Default image...
//                        profileCircleImage.setImageResource( R.drawable.profile_placeholder );
                }
            } );
        }
        else {
            dialog.dismiss();
            showToast("Please Select Image First...!!", context );
        }

    }
    public static void deleteImageFromFirebase(final Context context, @Nullable final Dialog dialog, String downloadPath, String fileName){
        isDeletedFile = false;
//        dialog.show();
        StorageReference deleteRef = storageReference.child(  downloadPath + "/" + fileName + ".jpg" );
        deleteRef.delete().addOnSuccessListener(new OnSuccessListener <Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (dialog != null)
                    dialog.dismiss();
                isDeletedFile = true;
                uploadImageLink = null;
                // After Delete from Server.. We have to  update on Database...
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (dialog != null)
                    dialog.dismiss();
                isDeletedFile = false;
                showToast( "Failed..!! "+ exception.getMessage(),context );
            }
        });

    }


    private static void showToast(String msg, Context context){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }


}
