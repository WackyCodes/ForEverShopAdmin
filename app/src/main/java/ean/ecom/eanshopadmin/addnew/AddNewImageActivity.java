package ean.ecom.eanshopadmin.addnew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.home.HomeFragment;
import ean.ecom.eanshopadmin.other.DialogsClass;

import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;
import static ean.ecom.eanshopadmin.database.DBQuery.storageReference;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.GALLERY_CODE;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

public class AddNewImageActivity extends AppCompatActivity implements View.OnClickListener {
    // This Activity only used for cat Image upload and update...
    // Add New Category...
    private ImageView addNewCatImage;
    private TextView addNewCatAddImgBtn;
    private TextView addNewCatDoneBtn;
    private TextView backBtn;
    private Uri tempCatURI = null;

    private String uploadPath;

    private String uploadCatID;
    private String layoutID;
    private int catIDNo;
    private int catLocalIndex;
    private int layoutIndex;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_image );
        dialog = DialogsClass.getDialog( this );

        uploadCatID = getIntent().getStringExtra( "UPLOAD_CAT_ID" );
        layoutID = getIntent().getStringExtra( "LAY_ID" );
        catIDNo = getIntent().getIntExtra( "CAT_NO", -1 );
        catLocalIndex = getIntent().getIntExtra( "LOCAL_CAT_INDEX", -1 );
        layoutIndex = getIntent().getIntExtra( "LOCAL_LAY_INDEX", -1 );

        uploadPath = "SHOPS/" + SHOP_ID + "/CAT_IMAGES/";

        homeCatListModelList.get( catLocalIndex ).getHomeListModelList().get( layoutIndex )
                .getBannerModelList().get( catIDNo ).getClickID();

        tempCatURI = null;

                // Add Category...
        addNewCatImage =  findViewById( R.id.add_new_cat_image );
        addNewCatAddImgBtn =  findViewById( R.id.add_new_cat_image_text );
        addNewCatDoneBtn =  findViewById( R.id.add_new_cat_add_text );
        backBtn =  findViewById( R.id.back_text );

        addNewCatAddImgBtn.setOnClickListener( this );
        // Cat Btn...
        backBtn.setOnClickListener( this );
        addNewCatDoneBtn.setOnClickListener( this );

    }

    @Override
    public void onClick(View view) {

        /// Add New Category...
          if (view == addNewCatAddImgBtn){
            // Request...
            Intent galleryIntent = new Intent( Intent.ACTION_PICK );
            galleryIntent.setType( "image/*" );
            startActivityForResult( galleryIntent, GALLERY_CODE );
        }
        else if (view == backBtn){
              onBackPressed();
        }else if (view == addNewCatDoneBtn){
            if ( tempCatURI != null){
                String name = homeCatListModelList.get( catLocalIndex ).getHomeListModelList().get( layoutIndex )
                        .getBannerModelList().get( catIDNo ).getClickID();
                uploadImageOnFirebaseStorage( this, tempCatURI, addNewCatImage, uploadPath, name  );
            }else{
                showToast( this, "Please Add Image First!" );
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Get Result of Image...
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == GALLERY_CODE ){
            if (resultCode == RESULT_OK){
                if (data != null){
                    Uri uri = data.getData();
                    startCropImageActivity(uri);
                }else{
                    showToast( this,  "Image not Found..!" );
                }
            }
        }
        // Get Response of cropped Image method....
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap( this.getContentResolver(), resultUri);
                    startCompressImage(bitmap );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showToast(  this, error.getMessage() );
            }
        }

    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines( CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start( this );

    }
//    public Uri getImageUri( Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
////        MediaStore.Images.Media.getContentUri(  );
//        return Uri.parse(path);
//    }

    private void startCompressImage(@NonNull Bitmap bitmap){
//        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] imageInByte = stream.toByteArray();
//        long sizeOfImage = imageInByte.length/1024;

        bitmap.compress( Bitmap.CompressFormat.JPEG,35, stream);
        byte[] BYTE = stream.toByteArray();
        Bitmap newBitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), newBitmap, "Title", null);
//        String imgLink = getImageUri(newBitmap).toString();
        tempCatURI = Uri.parse(path);
        Glide.with( this ).load( Uri.parse(path) ).into( addNewCatImage );

    }

    private void  updateCatOnDataBase(){

        int imageNo = catIDNo + 1;
//        Map <String, Object> uploadMap = new HashMap <>();
//        uploadMap.put( "cat_image_"+(catIDNo+1), uploadImageLink );

        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .collection( uploadCatID ).document( layoutID )
                .update( "cat_image_"+imageNo, uploadImageLink )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            homeCatListModelList.get( catLocalIndex ).getHomeListModelList().get( layoutIndex )
                                    .getBannerModelList().get( catIDNo ).setImageLink( uploadImageLink );
                            if (HomeFragment.homePageAdaptor != null)
                                HomeFragment.homePageAdaptor.notifyDataSetChanged();
                            showToast( AddNewImageActivity.this, "update successfully!" );
                            dialog.dismiss();
                            finish();
                        }else{
                            dialog.dismiss();
                            showToast( AddNewImageActivity.this, "Failed To update!" );
                        }

                    }
                } );

    }

    public String uploadImageLink = null;

    private void uploadImageOnFirebaseStorage(final Context context, final Uri imageUri,
                                                    final ImageView imageView, final String uploadPath, String fileName){
        dialog.show();
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
//                                                imageUri = task.getResult();
                                            uploadImageLink = task.getResult().toString();
                                            Glide.with( context ).load( uploadImageLink ).into( imageView );
                                            updateCatOnDataBase();
                                        }else{
                                            // Failed Query to getDownload Link...
                                            // TODO : Again Request to Get Download Link Or Query to Delete The Image....
//                                            deleteImageFromFirebase(context, dialog, uploadPath, fileName);
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

}
