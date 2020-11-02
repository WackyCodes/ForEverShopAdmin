package ean.ecom.eanshopadmin.main.shop;


import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
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

import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.other.DialogsClass;

import static android.app.Activity.RESULT_OK;
import static ean.ecom.eanshopadmin.database.DBQuery.storageReference;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.GALLERY_CODE;
import static ean.ecom.eanshopadmin.other.StaticValues.READ_EXTERNAL_MEMORY_CODE;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_TYPE_EGG;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_TYPE_NON_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_TYPE_NO_SHOW;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_TYPE_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_TYPE_VEG_NON;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_ADDRESS;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_DAY_SCHEDULE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_HELPLINE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_IMAGE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_LOGO;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_NAME;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_TAG_LINE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_TIME_SCHEDULE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_VEG_NON_CODE;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class UpdateShopFragment extends Fragment implements UpdateShop.OnImageRequest.OnImageRequestComplete {

    private UpdateShop.AddImageListener addImageListener;
    private UpdateShop.OnImageRequest onImageRequest;
    private int requestCode;

    public UpdateShopFragment(UpdateShop.AddImageListener addImageListener, UpdateShop.OnImageRequest onImageRequest, int requestCode) {
        // Required empty public constructor
        this.addImageListener = addImageListener;
        this.onImageRequest = onImageRequest;
        this.requestCode = requestCode;
        this.uploadImagePathUri = null;
    }

    private Dialog dialog;
    private int shopVgNonCode = 0;

    private ImageView backBtn;
    private TextView updateButton;
    //<!--        Update Image...--> // Layout 1
    private LinearLayout updateImageLayout;
    private TextView addNewImageBtn;
    private ImageView imageView;
    private Uri uploadImagePathUri;

    //  Update Status | TagLine | Address | helpLine | Name --> // Layout 2
    private LinearLayout updateShopInfoLayout;
    private TextView updateShopTitle;
    private EditText updateShopEditText;

    // Day Schedule... // Layout 3
    private LinearLayout dayScheduleLayout; // day_schedule_layout
    // Layout 4
    private LinearLayout timeScheduleLayout; // time_schedule_layout

    private TextView fromOpenTime;
    private TextView toCloseTime;

    // Veg Non Veg Update... // Layout 5
    private LinearLayout vegNonUpdateLayout;
    private Spinner vegNonUpdateSpinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_add_new_image, container, false );
        dialog = DialogsClass.getDialog( getContext() );

        backBtn = view.findViewById( R.id.home_back_imageview );

        updateImageLayout = view.findViewById( R.id.add_image_layout );
        imageView = view.findViewById( R.id.image_view );
        addNewImageBtn = view.findViewById( R.id.add_new_item );
        updateButton = view.findViewById( R.id.upload_image_text );

        updateShopInfoLayout = view.findViewById( R.id.update_shop_layout );
        updateShopTitle = view.findViewById( R.id.update_shop_title_text );
        updateShopEditText = view.findViewById( R.id.update_shop_edit_text );

        dayScheduleLayout = view.findViewById( R.id.day_schedule_layout );
        timeScheduleLayout = view.findViewById( R.id.time_schedule_layout );

        fromOpenTime = view.findViewById( R.id.from_time_text );
        toCloseTime = view.findViewById( R.id.to_time_text );

        vegNonUpdateLayout = view.findViewById( R.id.shop_veg_non_layout );
        vegNonUpdateSpinner = view.findViewById( R.id.shop_veg_non_type_spinner );

        // Set Layout Visibility...
        setLayoutVisibility();

        // Veg Non Veg Selector...
        selectProductVegNoNType();

        // time Select Action..
        setActionTimeSchedule();

        addNewImageBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted( )){
                    Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                    galleryIntent.setType( "image/*" );
                    startActivityForResult( galleryIntent, GALLERY_CODE );
                }
            }
        } );

        updateButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpdateButtonClick();
            }
        } );

        backBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Upload Image Cancel..!
                addImageListener.onAddImageFailed();
            }
        } );

        return view;
    }

    // Layout Visibility
    private void setLayoutVisibility(){
        switch (requestCode){
            case UPDATE_SHOP_LOGO:
            case UPDATE_SHOP_IMAGE:
                setLayoutVisibility( 1 );
                break;
            case UPDATE_SHOP_TAG_LINE:
                updateShopTitle.setText( "Update Tag Line" );
                setLayoutVisibility(2);
                break;
//            case UPDATE_SHOP_ADDRESS:
//                updateShopTitle.setText( "Update Shop Address" );
//                setLayoutVisibility(2);
//                break;
            case UPDATE_SHOP_HELPLINE:
                updateShopTitle.setText( "Update helpline number" );
                updateShopEditText.setInputType( InputType.TYPE_CLASS_NUMBER );
                setLayoutVisibility(2);
                break;
            case UPDATE_SHOP_NAME:
                updateShopTitle.setText( "Update Shop Name" );
                setLayoutVisibility(2);
                break;
            case UPDATE_SHOP_DAY_SCHEDULE:
                setLayoutVisibility(3);
                break;
            case UPDATE_SHOP_TIME_SCHEDULE:
                setLayoutVisibility(4);
                break;
            case UPDATE_SHOP_VEG_NON_CODE:
                setLayoutVisibility(5);
                break;
            default:
                break;
        }
    }
    private void setLayoutVisibility(int layoutNo){
        switch (layoutNo){
            case 1: // Visible Image Layout
                updateImageLayout.setVisibility( View.VISIBLE );
                updateShopInfoLayout.setVisibility( View.GONE );
                dayScheduleLayout.setVisibility( View.GONE );
                timeScheduleLayout.setVisibility( View.GONE );
                vegNonUpdateLayout.setVisibility( View.GONE );
                break;
            case 2: // Visible EditText Layout
                updateImageLayout.setVisibility( View.GONE );
                updateShopInfoLayout.setVisibility( View.VISIBLE );
                dayScheduleLayout.setVisibility( View.GONE );
                timeScheduleLayout.setVisibility( View.GONE );
                vegNonUpdateLayout.setVisibility( View.GONE );
                break;
            case 3: // visible day Schedule Layout..
                updateImageLayout.setVisibility( View.GONE );
                updateShopInfoLayout.setVisibility( View.GONE );
                dayScheduleLayout.setVisibility( View.VISIBLE );
                timeScheduleLayout.setVisibility( View.GONE );
                vegNonUpdateLayout.setVisibility( View.GONE );
                break;
            case 4: // visible Time Schedule...
                updateImageLayout.setVisibility( View.GONE );
                updateShopInfoLayout.setVisibility( View.GONE );
                dayScheduleLayout.setVisibility( View.GONE );
                timeScheduleLayout.setVisibility( View.VISIBLE );
                vegNonUpdateLayout.setVisibility( View.GONE );
                break;
            case 5: // visible Veg Non Layout
                updateImageLayout.setVisibility( View.GONE );
                updateShopInfoLayout.setVisibility( View.GONE );
                dayScheduleLayout.setVisibility( View.GONE );
                timeScheduleLayout.setVisibility( View.GONE);
                vegNonUpdateLayout.setVisibility( View.VISIBLE );
                break;
            default:
                break;
        }
    }

    private void setUpdateButtonClick(){
        switch (requestCode){
            case UPDATE_SHOP_LOGO:
            case UPDATE_SHOP_IMAGE:
                if (uploadImagePathUri !=null){
                    String uploadPath = "SHOPS/" + SHOP_ID + "/SHOP/";
                    if (requestCode == UPDATE_SHOP_LOGO){
                        uploadImageOnDatabase( uploadImagePathUri, uploadPath, "shop_logo" );
                    }else{
                        uploadImageOnDatabase( uploadImagePathUri, uploadPath, "shop_image" );
                    }
                }else{
                    showToast( "Please Add Image!" );
                }
                break;
            case UPDATE_SHOP_TAG_LINE:
                if (isValidEditText( updateShopEditText )){
                    onUploadShopInfo( "shop_tag_line" );
                }
                break;
//            case UPDATE_SHOP_ADDRESS:
//                if (isValidEditText( updateShopEditText )){
//                    onUploadShopInfo( "shop_address" );
//                }
//                break;
            case UPDATE_SHOP_HELPLINE:
                if (isValidEditText( updateShopEditText )){
                    onUploadShopInfo( "shop_help_line" );
                }
                break;
            case UPDATE_SHOP_NAME:
                if (isValidEditText( updateShopEditText )){
                    onUploadShopInfo( "shop_name" );
                }
                break;
            case UPDATE_SHOP_DAY_SCHEDULE: // Day Schedule...
                onUploadDaySchedule(getView());
                break;
            case UPDATE_SHOP_TIME_SCHEDULE:
                if (dialog!=null){
                    dialog.show();
                }
                Map <String, Object> timeMap = new HashMap <>();
                timeMap.put( "shop_open_time", fromOpenTime.getText().toString() );
                timeMap.put( "shop_close_time", toCloseTime.getText().toString() );
                onImageRequest.onUpdateShopOnDatabase( this,  timeMap );
                break;
            case UPDATE_SHOP_VEG_NON_CODE:
                if (dialog!=null){
                    dialog.show();
                }
                Map <String, Object> updateMap = new HashMap <>();
                updateMap.put( "shop_veg_non_type", String.valueOf( shopVgNonCode ) );
                onImageRequest.onUpdateShopOnDatabase( this,  updateMap );
                break;
            default:
                break;
        }
    }

    private void onUploadShopInfo(String updateField){
        if (dialog!=null){
            dialog.show();
        }
        Map <String, Object> updateMap = new HashMap <>();
        updateMap.put( updateField, updateShopEditText.getText().toString() );
        onImageRequest.onUpdateShopOnDatabase( this,  updateMap );
    }

    // Map : Day Schedule...
    private void onUploadDaySchedule(View view){
        if (dialog!=null){
            dialog.show();
        }

        List<Boolean> dayScheduleList = new ArrayList <>();

        Switch sunday_switch = view.findViewById( R.id.sunday_switch );
        Switch monday_switch = view.findViewById( R.id.monday_switch );
        Switch tuesday_switch = view.findViewById( R.id.tuesday_switch );
        Switch wednesday_switch = view.findViewById( R.id.wednesday_switch );
        Switch thursday_switch = view.findViewById( R.id.thursday_switch );
        Switch friday_switch = view.findViewById( R.id.friday_switch );
        Switch saturday_switch = view.findViewById( R.id.saturday_switch );
        // Sunday
        if (sunday_switch.isChecked()){
            dayScheduleList.add( true );
        }else{
            dayScheduleList.add( false );
        }
        // Monday
        if (monday_switch.isChecked()){
            dayScheduleList.add( true );
        }else{
            dayScheduleList.add( false );
        }
        // Tuesday
        if (tuesday_switch.isChecked()){
            dayScheduleList.add( true );
        }else{
            dayScheduleList.add( false );
        }
        // Wednesday
        if (wednesday_switch.isChecked()){
            dayScheduleList.add( true );
        }else{
            dayScheduleList.add( false );
        }
        // Thursday
        if (thursday_switch.isChecked()){
            dayScheduleList.add( true );
        }else{
            dayScheduleList.add( false );
        }
        // Friday
        if (friday_switch.isChecked()){
            dayScheduleList.add( true );
        }else{
            dayScheduleList.add( false );
        }
        // Saturday
        if (saturday_switch.isChecked()){
            dayScheduleList.add( true );
        }else{
            dayScheduleList.add( false );
        }

        Map <String, Object> updateMap = new HashMap <>();
        updateMap.put( "shop_days_schedule", dayScheduleList );
        onImageRequest.onUpdateShopOnDatabase( this,  updateMap );
        // Update in Local...
        SHOP_DATA_MODEL.setShop_days_schedule( dayScheduleList );
    }

    // shop Time Schedule Action  button..
    private void setActionTimeSchedule(){
        fromOpenTime.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTimePicker( fromOpenTime );
            }
        } );
        toCloseTime.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTimePicker( toCloseTime );
            }
        } );
    }

    //  Veg- Non Veg Spinner Set
    private void selectProductVegNoNType(){
        // Select Banner Type...
        ArrayAdapter <String> dataAdapter = new ArrayAdapter <String>( getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray( R.array.shop_veg_non_list ));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vegNonUpdateSpinner.setAdapter(dataAdapter);
        vegNonUpdateSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if (position != 0){ // Product Type
                    switch (position){
                        case 1: // Pure Veg .. ADMIN_DATA_MODEL.getShopVegNonCode()
                             shopVgNonCode = SHOP_TYPE_VEG;
                            break;
                        case 2: // Non Veg
                            shopVgNonCode = SHOP_TYPE_NON_VEG;
                            break;
                        case 3: // Veg + NonVeg
                            shopVgNonCode = SHOP_TYPE_VEG_NON;
                            break;
                        case 4: // Egg
                            shopVgNonCode = SHOP_TYPE_EGG;
                            break;
                        case 5: // Others
                            shopVgNonCode = SHOP_TYPE_NO_SHOW;
                            break;
                        default:
                            break;
                    }

                }else{
                    shopVgNonCode = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//
            }
        } );
    }

    @Override
    public void onUploadImageFinished(String uploadLink, boolean isSuccess) {
        // Complete...
        if (isSuccess){
            Map <String, Object> updateMap = new HashMap <>();
//            updateMap.put( "shop_id", "akdjn" );

            if (requestCode == UPDATE_SHOP_LOGO){
                updateMap.put( "shop_logo", uploadLink );
                SHOP_DATA_MODEL.setShop_logo( uploadLink);
            }else
            if(requestCode == UPDATE_SHOP_IMAGE){
                updateMap.put( "shop_image", uploadLink );
                SHOP_DATA_MODEL.setShop_image( uploadLink);
            }
            onImageRequest.onUpdateShopOnDatabase( this,  updateMap );
        }else{
            dismissDialog();
            addImageListener.onAddImageFailed();
        }
    }

    @Override
    public void onUpdateFinish(boolean isSuccess) {

        if (isSuccess){
            switch (requestCode){
                case UPDATE_SHOP_TAG_LINE:
                    SHOP_DATA_MODEL.setShop_tag_line( updateShopEditText.getText().toString());
                    break;
                case UPDATE_SHOP_ADDRESS:
                    SHOP_DATA_MODEL.setShop_address( updateShopEditText.getText().toString());
                    break;
                case UPDATE_SHOP_HELPLINE:
                    SHOP_DATA_MODEL.setShop_help_line( updateShopEditText.getText().toString());
                    break;
                case UPDATE_SHOP_NAME:
                    SHOP_DATA_MODEL.setShop_name( updateShopEditText.getText().toString());
                    break;
                case UPDATE_SHOP_VEG_NON_CODE:
                    SHOP_DATA_MODEL.setShop_veg_non_type( String.valueOf( shopVgNonCode ) );
                    break;
                case UPDATE_SHOP_TIME_SCHEDULE:
                    SHOP_DATA_MODEL.setShop_open_time( fromOpenTime.getText().toString() );
                    SHOP_DATA_MODEL.setShop_close_time( toCloseTime.getText().toString() );
                    break;
                default:
                    break;
            }
            showToast( "Update Successfully!" );
            dismissDialog();
            addImageListener.onAddImageSuccess( requestCode );
        }else{
            dismissDialog();
            addImageListener.onAddImageFailed();
        }

    }

    @Override
    public void showToast(String msg) {
        addImageListener.showToast( msg );
    }

    @Override
    public void dismissDialog() {
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    // Send Query....
    private void uploadImageOnDatabase(Uri uri, String uploadPath, String fileName){
        if (dialog!=null){
            dialog.show();
        }
        StorageReference storageRef = storageReference.child( uploadPath + "/" + fileName + ".jpg" );
        onImageRequest.onImageUploadRequest( this, uri, storageRef );
    }

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
        addNewImageInList( getImageUri(newBitmap) );
//        bannerImageUri = getImageUri(newBitmap);
    }
    // Add New Image in List...
    private void addNewImageInList(Uri imageLink){
        // if User Add New Images...
        Glide.with( getContext() ).load( imageLink ).into( imageView );
        uploadImagePathUri = imageLink;
    }

    // Check Validation..
    private boolean isValidEditText(EditText editText){
        if (TextUtils.isEmpty( editText.getText().toString() )){
            editText.setError( "Required!" );
            return false;
        }else{
            return true;
        }
    }

    //dialog Time Picker...
    private void getTimePicker(final TextView textView){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog( getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(selectedHour > 12) {
                    selectedHour = selectedHour - 12;
                    textView.setText(selectedHour + ":" + selectedMinute + " PM" );
                }
                else
                {
                    textView.setText(selectedHour + ":" + selectedMinute + " AM" );
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

}
