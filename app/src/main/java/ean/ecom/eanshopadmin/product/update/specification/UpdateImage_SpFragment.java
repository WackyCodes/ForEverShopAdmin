package ean.ecom.eanshopadmin.product.update.specification;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.StaticMethods;
import ean.ecom.eanshopadmin.product.ProductViewInterface;
import ean.ecom.eanshopadmin.product.productview.ProductDetails;
import ean.ecom.eanshopadmin.product.update.AddNewImageAdaptor;
import ean.ecom.eanshopadmin.product.update.UpdateData;
import ean.ecom.eanshopadmin.product.update.UpdateDataRequest;

import static android.app.Activity.RESULT_OK;
import static ean.ecom.eanshopadmin.database.DBQuery.storageReference;
import static ean.ecom.eanshopadmin.other.StaticValues.GALLERY_CODE;
import static ean.ecom.eanshopadmin.other.StaticValues.READ_EXTERNAL_MEMORY_CODE;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_IMAGES;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SPECIFICATION;
import static ean.ecom.eanshopadmin.product.update.AddNewImageAdaptor.uploadImageDataModelList;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class UpdateImage_SpFragment extends Fragment implements
        UpdateData.UpdateRequest.OnUpdateFinisher,
        UpdateData.ImageListUpdateRequest {

    private ProductViewInterface rootListener;

    private UpdateData.UpdateRequest updateRequest;

    //    private UpdateData.UpdateRequest updateRequest;
    private int listVariant;
    private String productID;
    private int updateCode;

    public static List <AddSpecificationModel> specificationModelList;

    //Images...
    private List<String> localImageList;
    private int updateImageFromIndex;

    public UpdateImage_SpFragment(ProductViewInterface rootActivity, int listVariant, String productID, int updateCode,
                                  @Nullable List<String> uploadImageDataModelList,
                                  @Nullable List <AddSpecificationModel> specificationModelList) {
        // Required empty public constructor
        this.rootListener = rootActivity;
        this.listVariant = listVariant;
        this.productID = productID;
        this.updateCode = updateCode;
        updateRequest = new UpdateDataRequest();

        if (uploadImageDataModelList != null){
            this.localImageList = uploadImageDataModelList;
            updateImageFromIndex = uploadImageDataModelList.size();
            //  pProductModel.getProductSubModelList().get( currentVariant ).getpImage()
        }
        if (specificationModelList != null){
            UpdateImage_SpFragment.specificationModelList = specificationModelList;
            // pProductModel.getProductSubModelList().get( currentVariant ).getpSpecificationList()
        }
    }

    // Adaptor...
    private AddNewImageAdaptor imageAdaptor;
    public static AddSpecificationAdaptor specificationAdaptor;

    // Fragment Class Variable...
    private RecyclerView recyclerView;
    private Button uploadBtn;
    private Button finishButton;

    private ImageView homeBackBtn;
    private TextView productIDText;
    private TextView productVerText;
    private TextView updateTitle;
    private Spinner proCopyFromSpinner;

    private Dialog dialog;
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate( R.layout.fragment_add_specification, container, false );
        dialog = DialogsClass.getDialog( getContext() );

        recyclerView = view.findViewById( R.id.new_pro_specifications_recycler );
        uploadBtn = view.findViewById( R.id.upload_specification_btn );
        finishButton = view.findViewById( R.id.update_finish_button );
        finishButton.setVisibility( View.GONE );
        // header....
        homeBackBtn = view.findViewById( R.id.home_back_imageview );
        productIDText = view.findViewById( R.id.pro_id_text );
        productVerText = view.findViewById( R.id.pro_ver_no_text );
        updateTitle = view.findViewById( R.id.update_title );
        proCopyFromSpinner = view.findViewById( R.id.copy_from_spinner );

        // set Header...
        productIDText.setText( "Product ID : "+ productID );
        productVerText.setText( "Variant : " + (listVariant + 1) );

        /// Set Layout ...
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        if (updateCode == UPDATE_SPECIFICATION){
            uploadBtn.setText( "Update Specifications" );
            updateTitle.setText( "Specification Updates" );
            linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
            recyclerView.setLayoutManager( linearLayoutManager );
            // Adaptor...
            specificationAdaptor = new AddSpecificationAdaptor( );
            recyclerView.setAdapter( specificationAdaptor );
            specificationAdaptor.notifyDataSetChanged();
        }else{
            uploadBtn.setText( "Upload Images" );
            updateTitle.setText( "Images Updates" );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            recyclerView.setLayoutManager( linearLayoutManager );
            // Adaptor...
            imageAdaptor = new AddNewImageAdaptor( this, localImageList, updateImageFromIndex );
            recyclerView.setAdapter( imageAdaptor );
            imageAdaptor.notifyDataSetChanged();
        }

        // Upload Button...
        uploadBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateCode == UPDATE_SPECIFICATION){
                    if (isListNotEmpty()){
                        //  Upload Data...
                        updateSpecificationClick();
                    }else{
                        showToast( "Code Not Set Yet!");
                    }
                }else{
                    if ( updateImageFromIndex < uploadImageDataModelList.size()){
                        onUploadImagesButtonClick();
                    }else{
                        showToast( "Already Updated..!" );
                    }
                }
            }
        } );

        homeBackBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
                rootListener.onUpdateCompleted( updateCode, false );
            }
        } );

        finishButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On Update Images...
                updateImageButtonClick();
                updateImageFromIndex = 0;
            }
        } );

        // Set Copy From Spinner
        setProCopyFromSpinner();

        return view;
    }

    // Set Copy From Spinner
    private void setProCopyFromSpinner(){
        List<String> productVarList = new ArrayList <>();
        productVarList.clear();
        productVarList.add( "Select" );
        for (int i = 0; i < ProductDetails.pProductModel.getProductSubModelList().size(); i++){
            productVarList.add( String.valueOf( 1+i ) );
        }
        // Qty Type Text Adopter...
        ArrayAdapter <String> qtyTypeList = new ArrayAdapter<String>( getContext(),
                android.R.layout.simple_spinner_item, productVarList );
        proCopyFromSpinner.setAdapter( qtyTypeList );
        proCopyFromSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if (position > 0 && position != (listVariant+1) ){
                    dialog.show();
                    setProCopyFromSpinnerData( position - 1 );
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        } );
    }
    // Set Data from Another Variant...
    private void setProCopyFromSpinnerData(int position){
        if (updateCode == UPDATE_SPECIFICATION){
            if (ProductDetails.pProductModel.getProductSubModelList().get( position ).getpSpecificationList() != null){
                UpdateImage_SpFragment.specificationModelList = ProductDetails.pProductModel.getProductSubModelList().get( position ).getpSpecificationList();
                // Adaptor...
//                specificationAdaptor = new AddSpecificationAdaptor( );
//                recyclerView.setAdapter( specificationAdaptor );
                specificationAdaptor.notifyDataSetChanged();
                dialog.dismiss();
            }else{
                showToast( "No Specification Found!" );
                dialog.dismiss();
            }
        }else{
            if (ProductDetails.pProductModel.getProductSubModelList().get( position ).getpImage() != null){
                localImageList = ProductDetails.pProductModel.getProductSubModelList().get( position ).getpImage();
                updateImageFromIndex =  localImageList.size();
                //  Notify the layout
                imageAdaptor = new AddNewImageAdaptor( this, localImageList, updateImageFromIndex );
                recyclerView.setAdapter( imageAdaptor );
                imageAdaptor.notifyDataSetChanged();
                dialog.dismiss();
            }else{
                showToast( "No Images Found!" );
                dialog.dismiss();
            }
        }
    }

    private boolean isListNotEmpty( ){

        if (specificationModelList.size() == 0){
            showToast( "Please Add Features!");
            return false;
        }else{
            if (specificationModelList.get( 0 ).getSpHeading() == null){
                showToast( "Please Enter Features title!");
                return false;
            }
            if (specificationModelList.get( 0 ).getSpecificationFeatureModelList().size() == 0){
                showToast( "Please Add Some Features!");
                return false;
            }
            return true;
        }
    }

    // Specification Update on Database...
    private void updateSpecificationClick(){
        if (!dialog.isShowing()){
            dialog.show();
        }
        Map <String, Object> updateMap = new HashMap <>();
//        int pListIndex = homeCatListModelList.get( catIndex ).getHomeListModelList().get( layIndex ).getProductIdList().size() + 1;
//        updateMap.put( "no_of_products", pListIndex  );
//        updateMap.put( "product_id_"+pListIndex, productID );
        updateMap.put( "visibility", true  );
        updateMap.put( "p_specification_"+(listVariant+1), specificationModelList );
        updateMap.put( "p_last_update_time", StaticMethods.getCrrDate() + " " + StaticMethods.getCrrTime() );
        updateRequest.onUpdateRequest( this, productID, updateMap );
    }
    // Image Uploads..
    private void onUploadImagesButtonClick(){
        for (int x = updateImageFromIndex; x < uploadImageDataModelList.size(); x++){
//             listVariant + "" + x;
            if (!dialog.isShowing()){
                dialog.show();
            }
            UploadImageThread uploadImageThread = new UploadImageThread( x, listVariant + "" + x );
            if (x == uploadImageDataModelList.size() -1){
                uploadImageThread.setPriority( Thread.NORM_PRIORITY );
            }else{
                uploadImageThread.setPriority( Thread.MAX_PRIORITY );
            }
            uploadImageThread.start();
        }
    }
    // Images Updates...
    private void updateImageButtonClick(){
        if (!dialog.isShowing()){
            dialog.show();
        }
        Map <String, Object> updateMap = new HashMap <>();
        updateMap.put( "p_image_"+(listVariant+1), uploadImageDataModelList );
        updateMap.put( "p_last_update_time", StaticMethods.getCrrDate() + " " + StaticMethods.getCrrTime() );
        updateRequest.onUpdateRequest( this, productID, updateMap );
    }

    // Override methods...---------------------------
    @Override
    public void onAddImageRequest() {
        if (isPermissionGranted( )){
            Intent galleryIntent = new Intent( Intent.ACTION_PICK );
            galleryIntent.setType( "image/*" );
            startActivityForResult( galleryIntent, GALLERY_CODE );
        }
    }
    @Override
    public void onRemoveImageRequest(int index) {
        uploadImageDataModelList.remove( index );
        if (index < updateImageFromIndex){
            updateImageFromIndex--;
            if (updateImageFromIndex == uploadImageDataModelList.size()){
                uploadBtn.setVisibility( View.GONE );
                finishButton.setVisibility( View.VISIBLE );
            }
        }
        imageAdaptor.notifyDataSetChanged();
    }

    public void showToast(String msg) {
        rootListener.showToastMessage( msg );
    }
    @Override
    public void onUpdateFinished(boolean isSuccess) {
        rootListener.onUpdateCompleted( updateCode, isSuccess );
        dismissDialog();
    }
    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }
    // Override methods...---------------------------

    // Permission...
    private boolean isPermissionGranted(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if ( getContext().checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
                // TODO : YES
                return true;
            }else{
                // TODO : Request For Permission..!
                requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_MEMORY_CODE );
                return false;
            }
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == READ_EXTERNAL_MEMORY_CODE){
            if (grantResults[0] !=  PackageManager.PERMISSION_GRANTED){
                showToast( "Permission granted..!" );
            }else{
                showToast( "Permission Denied..! Please Grant Permission first.!!");
            }
        }
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
//                    productImageLinkList.add( data.getData().toString() );
//                    uploadImageDataModelList.add( new UploadImageDataModel( data.getData().toString(), "") );
//                    isUploadImages = false;
//                    imgAdaptor.notifyDataSetChanged();
                }else{
                    showToast(  "Image not Found..!" );
                }
            }
        }
        // Get Response of cropped Image method....
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
//                Bitmap bitmap = result.getBitmap();
//                Glide.with( this ).load( resultUri ).into( croppedImage );
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resultUri);
                    startCompressImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast( "error : " + e.getMessage() );
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showToast( error.getMessage() );
            }else{
                showToast( "something went Wrong..!" );
            }
        }

    }
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines( CropImageView.Guidelines.ON)
//                .setAspectRatio( 1,1 )
                .setMultiTouchEnabled(true)
                .start( getContext(), this );
    }

    public Uri getImageUri( Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage( getActivity().getContentResolver(), inImage, "Title", null);
//        MediaStore.Images.Media.getContentUri(  );
        return Uri.parse(path);
    }
    private void startCompressImage(@NonNull Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.JPEG,50, stream);
        byte[] BYTE = stream.toByteArray();
        Bitmap newBitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);
        addNewImageInList( getImageUri(newBitmap).toString() );
//        bannerImageUri = getImageUri(newBitmap);
    }
    // Add New Image in List...
    private void addNewImageInList(String imageLink){
        uploadImageDataModelList.add( imageLink );
        imageAdaptor.notifyDataSetChanged();
        // if User Add New Images...
        if (uploadBtn.getVisibility() != View.VISIBLE){
            uploadBtn.setVisibility( View.VISIBLE );
            finishButton.setVisibility( View.GONE );
        }
    }

    // Thread... TO Upload Images...
    private class UploadImageThread extends Thread implements UpdateData.UpdateImageOnServerRequest.OnUploadImageFinisher {
        private int uriIndex;
        private String uploadImageName;
        private UpdateData.UpdateImageOnServerRequest updateImageOnServerRequest;

        public UploadImageThread(int uriIndex, String uploadImageName) {
            this.uriIndex = uriIndex;
            this.uploadImageName = uploadImageName;
            updateImageOnServerRequest = new UpdateDataRequest();
        }

        public void run(){
            updateImageOnServerRequest.onImageUploadRequest( this, Uri.parse( uploadImageDataModelList.get( uriIndex ) ),
                    storageReference.child( "SHOPS/" + SHOP_ID + "/products/" + productID + "/" + uploadImageName + ".jpg" ));
        }

        @Override
        public void onUploadImageFinished(String downloadLink) {
            uploadImageDataModelList.set( uriIndex, downloadLink );
            imageAdaptor.notifyDataSetChanged();
            // Update Index...
            updateImageFromIndex = updateImageFromIndex + 1;
            if (uriIndex == uploadImageDataModelList.size() - 1){
                dismissDialog();
                // Set Button Visibility
                uploadBtn.setVisibility( View.GONE );
                finishButton.setVisibility( View.VISIBLE );
            }
        }

        @Override
        public void dismissDialog() {
            dialog.dismiss();
        }

        @Override
        public void showToast(String msg) {
            rootListener.showToastMessage( msg );
        }
    }

}
