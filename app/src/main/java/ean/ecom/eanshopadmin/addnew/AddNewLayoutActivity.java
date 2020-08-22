package ean.ecom.eanshopadmin.addnew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.home.HomeFragment;
import ean.ecom.eanshopadmin.home.HomeListModel;
import ean.ecom.eanshopadmin.model.BannerModel;
import ean.ecom.eanshopadmin.other.CheckInternetConnection;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.UpdateImages;
import ean.ecom.eanshopadmin.product.ProductModel;
import ean.ecom.eanshopadmin.product.search.ProductSearchActivity;

import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.other.StaticMethods.getRandomNumAccordingToDate;
import static ean.ecom.eanshopadmin.other.StaticValues.BANNER_CLICK_TYPE_PRODUCT;
import static ean.ecom.eanshopadmin.other.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;
import static ean.ecom.eanshopadmin.other.StaticValues.GALLERY_CODE;
import static ean.ecom.eanshopadmin.other.StaticValues.GRID_PRODUCTS_LAYOUT_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.READ_EXTERNAL_MEMORY_CODE;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_BANNER_SLIDER_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_STRIP_AD_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;
import static ean.ecom.eanshopadmin.other.StaticValues.clipboardManager;

public class AddNewLayoutActivity extends AppCompatActivity implements View.OnClickListener {

    private Dialog dialog;

    // Add New Banner in Slider....
    private LinearLayout bannerDialogLayoutFrame;
    private ImageView bannerDialogBannerImage;
    private TextView bannerDialogAddImage;
    private EditText bannerClickLink;
    private Spinner bannerSelectType;
    private LinearLayout bannerClickLinkLayout;
    private LinearLayout bannerShopIDLayout;
    private TextView bannerDialogCancelBtn;
    private TextView bannerDialogOkBtn;
    private TextView bannerDialogUploadImage;

    private EditText bannerProductIdText;
    private TextView bannerNewProductBtn;

    private int bannerDialogType;
    private int bannerClickType = -1;
    private String bannerClickID;
    private Uri bannerImageUri = null;
    // Add New Banner in Slider....

    private int catIndex = -1;
//    private String categoryTitle;
    private String categoryID;
    private int layoutType;
    private int layoutIndex = -1;
    private String layoutId = null;
    public static boolean isTaskIsUpdate = false;
    private String fileCode;

    private List <HomeListModel> homePageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_layout );
        fileCode = getRandomNumAccordingToDate();
        dialog = DialogsClass.getDialog( this );

        catIndex = getIntent().getIntExtra( "CAT_INDEX", -1 );
        layoutType = getIntent().getIntExtra( "LAY_TYPE", -1 ); // TO know about which type of layout is requested ( Slider, Strip Ad, Or others..)
        layoutIndex = getIntent().getIntExtra( "LAY_INDEX", -1 ); // To know what is index of layout...( To use Update..
        isTaskIsUpdate = getIntent().getBooleanExtra( "TASK_UPDATE", false );
        // Assign Data...
        this.homePageList = DBQuery.homeCatListModelList.get( catIndex ).getHomeListModelList();
        this.categoryID = DBQuery.homeCatListModelList.get( catIndex ).getCatID();
//        this.categoryTitle = DBQuery.homeCatListModelList.get( catIndex ).getCatName();

//        layoutId = homePageList.get(  )

                // Add New Banner in Slider....
        bannerDialogLayoutFrame = findViewById( R.id.add_new_banner_item_LinearLay );
        bannerDialogBannerImage = findViewById( R.id.banner_image );
        bannerDialogAddImage = findViewById( R.id.change_banner );
        bannerClickLink = findViewById( R.id.banner_link_edit_text );
        bannerDialogUploadImage = findViewById( R.id.upload_banner );
        bannerDialogCancelBtn = findViewById( R.id.banner_cancel_txt );
        bannerDialogOkBtn = findViewById( R.id.banner_ok_txt );

        bannerSelectType = findViewById( R.id.select_banner_type_spinner );
        bannerProductIdText = findViewById( R.id.banner_product_id_text );
        bannerNewProductBtn = findViewById( R.id.banner_product_id_new );
        bannerClickLinkLayout = findViewById( R.id.banner_link_layout );
        bannerShopIDLayout = findViewById( R.id.banner_shop_id_layout );

        bannerDialogAddImage.setOnClickListener( this );
        bannerDialogUploadImage.setOnClickListener( this );
        bannerDialogCancelBtn.setOnClickListener( this );
        bannerDialogOkBtn.setOnClickListener( this );
        bannerNewProductBtn.setOnClickListener( this );
        // Add New Banner in Slider....

        if (isTaskIsUpdate){
            layoutId = homePageList.get( layoutIndex ).getLayoutID();
        }else{
            layoutId = getLayoutId();
        }
        switch (layoutType){
            case SHOP_HOME_BANNER_SLIDER_CONTAINER:
            case HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER:
            case GRID_PRODUCTS_LAYOUT_CONTAINER:
                dialog.dismiss();
                addNewLayout(layoutType);
                break;
            case SHOP_HOME_STRIP_AD_CONTAINER:
            case BANNER_SLIDER_CONTAINER_ITEM:
                dialog.dismiss();
                // show the Banner Dialog...
                bannerDialogLayoutFrame.setVisibility( View.VISIBLE );
                bannerDialogType = layoutType;
                bannerDialogBannerImage.setVisibility( View.VISIBLE );
                bannerDialogBannerImage.setImageResource( R.drawable.ic_panorama_black_24dp );
                UpdateImages.uploadImageLink = null;
                bannerImageUri = null;
                bannerProductIdText.setText( "" );
                break;
            default:
                break;
        }

        //-----------------------------
        if (clipboardManager == null){
            clipboardManager = (ClipboardManager)getSystemService( this.CLIPBOARD_SERVICE );
        }

        // Text ..
//        ClipData clipData = ClipData.newPlainText( "TEXT", "WackyCodes" );
//        clipboardManager.setPrimaryClip( clipData );
//        if (clipboardManager.hasPrimaryClip()){
//            String data = clipboardManager.getPrimaryClip().getItemAt( 0 ).getText().toString();
//        }

        setSpinnerList();

    }

    public void setSpinnerList(){
        // Select Banner Type...
        ArrayAdapter <String> dataAdapter = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray( R.array.banner_type ));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bannerSelectType.setAdapter(dataAdapter);
        bannerSelectType.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                    switch (position){
//                        case 1:
//                            bannerClickLinkLayout.setVisibility( View.VISIBLE );
//                            bannerShopIDLayout.setVisibility( View.GONE );
//                            bannerClickType = BANNER_CLICK_TYPE_WEBSITE;
//                            bannerClickID = null;
//                            bannerClickLink.setText( "" );
//                            break;
                        case 1:
                            bannerClickLinkLayout.setVisibility( View.GONE );
                            bannerShopIDLayout.setVisibility( View.VISIBLE );
                            bannerClickType = BANNER_CLICK_TYPE_PRODUCT;
                            bannerClickID = null;
                            bannerProductIdText.setText( "" );
                            break;
                        default:
                            break;
                    }
                }
                else{
                    bannerClickID = null;
                    bannerClickType = -1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//
            }
        } );
        // Select Banner Type...

        // Text Watcher...
        bannerProductIdText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty( bannerProductIdText.getText().toString() )){
                    if(bannerProductIdText.getText().toString().length() >= 10){
                        // Check whether is Exist...
                        dialog.show();
                        checkForProductID( bannerProductIdText.getText().toString() );
                    }else{
                        bannerClickID = null;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );

    }

    // OnClick Item...
    @Override
    public void onClick(View v) {
        // --------- Add New Banner in Slider or Add ad layout of Strip or Banner...!
        if (v == bannerDialogAddImage){
            // Add Image..
            if (isPermissionGranted( )){
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                startActivityForResult( galleryIntent, GALLERY_CODE );
            }
        }
        else if (v == bannerDialogUploadImage){
            if (bannerImageUri != null && CheckInternetConnection.isInternetConnected( this )){
                Dialog perDialog = DialogsClass.getDialog( this );
                perDialog.show();
                switch (bannerDialogType){
                    case SHOP_HOME_STRIP_AD_CONTAINER:
                        UpdateImages.uploadImageOnFirebaseStorage( this, perDialog, bannerImageUri, bannerDialogBannerImage
                                , "SHOPS/" + SHOP_ID + "/ads"
                                , "strip_ad_"+ fileCode  );
                        break;
                    case BANNER_SLIDER_CONTAINER_ITEM:
                        UpdateImages.uploadImageOnFirebaseStorage( this, perDialog, bannerImageUri, bannerDialogBannerImage
                                , "SHOPS/" + SHOP_ID + "/banners"
                                , "banner_" +  fileCode );
                        break;
                    default:
                        break;
                }
            }else {
                showToast( "Please select Image First..!" );
            }
        }
        else if (v == bannerDialogCancelBtn){
            if (UpdateImages.uploadImageLink != null ){
                showAlertDialogForBanner();
            } else{
                finish(); // Finish this Activity..
            }
        }
        else if (v == bannerDialogOkBtn){
            if (UpdateImages.uploadImageLink != null && bannerClickType != -1  ){
                switch (bannerClickType){
                    case BANNER_CLICK_TYPE_PRODUCT:
                        if (bannerClickID != null){
//                            bannerClickID = bannerProductIdText.getText().toString().trim();
                            // Upload Data on Database...
                            addNewLayout( bannerDialogType );
                        }else{
                            bannerProductIdText.setError( "InCorrect!" );
                        }
                    default:
                        break;
                }

//                finish(); // Finish this Activity..
            } else
            if (UpdateImages.uploadImageLink == null){
                showToast( "Upload Image First..!" );
            }else
            if ( bannerClickType == -1 ){
                showToast( "Select Banner Type..!!" );
            }else{
                showToast( "Something Went Wrong ! Please Check Again!" );
            }
        }
        // --------- Add New Banner in Slider or Add ad layout of Strip or Banner...!
        else if (v == bannerNewProductBtn){
            //  New Product ID : GOTO search Product...
            Intent intent = new Intent( AddNewLayoutActivity.this, ProductSearchActivity.class );
            startActivity( intent );
        }
    }

    // ---------- getLayoutId
    private String getLayoutId(){
        String layout = null;
        int sizeOfList = homePageList.size();
        List<String> tempIdList = new ArrayList <>();

        for (int i = 0; i < sizeOfList; i++){
            tempIdList.add( homePageList.get( i ).getLayoutID() ); //  "layout_id", "layout_"+
        }

        for (int i = 0; i <= sizeOfList; i++){
            layout = "layout_"+ i;
            if ( !tempIdList.contains( layout )){
//                layoutId = layout;
                break;
            }
        }
        return layout;
    }
    private void showAlertDialogForBanner(){
        // Show Warning dialog...
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Do You Want to Cancel adding Layout.?" );
        builder.setCancelable( false );
        builder.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogA, int which) {
                dialogA.dismiss();
                dialog.show();
                switch (bannerDialogType){
                    case SHOP_HOME_STRIP_AD_CONTAINER:
                        UpdateImages.deleteImageFromFirebase( AddNewLayoutActivity.this, dialog
                                , "SHOPS/" + SHOP_ID + "/ads"
                                , "strip_ad_"+fileCode  );
                        break;

                    case BANNER_SLIDER_CONTAINER_ITEM:
                        UpdateImages.deleteImageFromFirebase( AddNewLayoutActivity.this, dialog
                                ,"SHOPS/" + SHOP_ID + "/banners"
                                , "banner_" +fileCode  );
                        break;
                    default:
                        break;
                }// Finish this Activity..
            }
        } ).setNegativeButton( "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogA, int which) {
                dialogA.dismiss();
            }
        } ).show();
    }
    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    private void addNewLayout(int type){
        switch (type){
            case SHOP_HOME_BANNER_SLIDER_CONTAINER:           //-- 1
                Map <String, Object> layoutMap = new HashMap <>();
                layoutMap.put( "index", homePageList.size() ); // int
                layoutMap.put( "layout_id", layoutId); // String
//                layoutMap.put( "layout_bg", "#DADADA" ); // For sample
                layoutMap.put( "is_visible", false ); // boolean
                layoutMap.put( "no_of_banners", 0 ); // int
                layoutMap.put( "type", SHOP_HOME_BANNER_SLIDER_CONTAINER ); // int
                dialog.show();
                uploadNewLayoutOnDatabase( layoutMap, SHOP_HOME_BANNER_SLIDER_CONTAINER, dialog );
                break;
            case BANNER_SLIDER_CONTAINER_ITEM:
                Map<String, Object> bSliderItem = new HashMap <>();
                // We have to add all the list data in this map  And Set this list on the database location...
                String bannerImgLink = UpdateImages.uploadImageLink;

                homePageList.get( layoutIndex ).getBannerModelList().add( new BannerModel( bannerClickType, bannerClickID, bannerImgLink, "", "banner_"+fileCode ) );

                int bannerNo =  homePageList.get( layoutIndex ).getBannerModelList().size();
//                    bSliderItem.put( "index", layoutIndex );
                bSliderItem.put( "layout_id", homePageList.get( layoutIndex ).getLayoutID() );
                bSliderItem.put( "banner_"+ bannerNo, bannerImgLink ); // String
                bSliderItem.put( "banner_click_id_"+ bannerNo, bannerClickID ); // String
                bSliderItem.put( "banner_click_type_"+ bannerNo, bannerClickType ); // int
                bSliderItem.put( "delete_id_"+ bannerNo, "banner_"+fileCode ); // String
                bSliderItem.put( "no_of_banners", bannerNo ); // int
                if (bannerNo == 3){
                    bSliderItem.put( "is_visible", true );
                }
                dialog.show();
                updateLayoutOnDatabase( bSliderItem, BANNER_SLIDER_CONTAINER_ITEM, dialog );
                break;
            case SHOP_HOME_STRIP_AD_CONTAINER:         //-- 2
                Map<String, Object> stripMap = new HashMap <>();
                stripMap.put( "index", homePageList.size() );
                stripMap.put( "layout_id", layoutId );
                stripMap.put( "is_visible", true );
                stripMap.put( "banner_image", UpdateImages.uploadImageLink );
                stripMap.put( "banner_click_id", bannerClickID );
                stripMap.put( "banner_click_type", bannerClickType );
                stripMap.put( "type", SHOP_HOME_STRIP_AD_CONTAINER );
                stripMap.put( "delete_id", "strip_ad_"+fileCode );
                stripMap.put( "extra_text", "New" );
                dialog.show();
                uploadNewLayoutOnDatabase( stripMap, SHOP_HOME_STRIP_AD_CONTAINER, dialog );
                break;
            case HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER:
            case GRID_PRODUCTS_LAYOUT_CONTAINER:
                addNewHrGridLayout( type );
            default:
                break;
        }
    }
    private void addNewHrGridLayout(final int view_type){
        /// Sample Button click...
        final Dialog quantityDialog = new Dialog( this );
        quantityDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        quantityDialog.setContentView( R.layout.dialog_single_edit_text );
        quantityDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        quantityDialog.setCancelable( false );
        final Button okBtn = quantityDialog.findViewById( R.id.dialog_ok_btn );
        final Button CancelBtn = quantityDialog.findViewById( R.id.dialog_cancel_btn );
        final EditText dialogEditText = quantityDialog.findViewById( R.id.dialog_editText );
        final TextView dialogTitle = quantityDialog.findViewById( R.id.dialog_title );
        dialogTitle.setText( "Enter Layout Name :" );
        dialogEditText.setHint( "Layout title/type" );
        quantityDialog.show();

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( TextUtils.isEmpty(dialogEditText.getText().toString().trim())) {
                    dialogEditText.setError( "Please Fill Require field..!" );
                }
                else {
                    quantityDialog.dismiss();
                    dialog.show();
                    final String layout_title = dialogEditText.getText().toString();
                    Map<String, Object> layoutMap = new HashMap <>();
                    layoutMap.put( "layout_title", layout_title );
                    layoutMap.put( "no_of_products", 0 );
                    layoutMap.put( "index", catIndex );
                    layoutMap.put( "layout_id", layoutId );
                    layoutMap.put( "layout_bg", "#DADADA" );
                    layoutMap.put( "visibility", false );
                    layoutMap.put( "type",view_type );
                    uploadNewLayoutOnDatabase(layoutMap, view_type, dialog);
                }
            }
        } );

        CancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityDialog.dismiss();
                finish();
            }
        } );

    }

    private void uploadNewLayoutOnDatabase(final Map<String, Object> layoutMap, final int view_type, final Dialog dialog ){
        // we are set our unique Id... Because we need this id to update data...
        final String documentId = layoutMap.get( "layout_id" ).toString();

        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .collection( categoryID ).document( documentId ).set( layoutMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            switch (view_type){
                                case SHOP_HOME_BANNER_SLIDER_CONTAINER:
                                    homePageList.add( new HomeListModel( view_type, documentId, true, new ArrayList <BannerModel>() ) );
                                    break;
                                case BANNER_SLIDER_CONTAINER_ITEM:
                                    // Notify Data Changed..  of Adaptor...
                                    break;
                                case SHOP_HOME_STRIP_AD_CONTAINER:
                                    homePageList.add( new HomeListModel( view_type, documentId, new BannerModel(
                                            Integer.parseInt( layoutMap.get( "banner_click_type" ).toString() ),
                                            layoutMap.get( "banner_click_id").toString(),
                                            layoutMap.get( "banner_image" ).toString(),
                                            layoutMap.get( "extra_text" ).toString(),
                                            layoutMap.get( "delete_id").toString()
                                    )) );
                                    break;
                                case HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER:
                                case GRID_PRODUCTS_LAYOUT_CONTAINER:
                                    homePageList.add( new HomeListModel( view_type, documentId,
                                            layoutMap.get( "layout_title" ).toString(), new ArrayList<String>(),  new ArrayList <ProductModel>()  ) );
                                    break;
                                default:
                                    break;
                            }
                            HomeFragment.homePageAdaptor.notifyDataSetChanged();
                            dialog.dismiss();
                            showToast( "Added Successfully..!" );

                            finish();
                        }else{
                            dialog.dismiss();
                            if (view_type == BANNER_SLIDER_CONTAINER_ITEM){
                                homePageList.get( layoutIndex ).getBannerModelList().remove( homePageList.get( layoutIndex ).getBannerModelList().size() -1 );
                            }
                            showToast( "failed..! Error : " + task.getException().getMessage() );
                        }
                    }
                } );
//        }else{
//            dialog.dismiss();
//        }

    }
    private void updateLayoutOnDatabase(final Map<String, Object> layoutMap, final int view_type, final Dialog dialog ){
        // we are set our unique Id... Because we need this id to update data...
        final String documentId = layoutMap.get( "layout_id" ).toString();

        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .collection( categoryID ).document( documentId ).update( layoutMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            switch (view_type){
                                case SHOP_HOME_BANNER_SLIDER_CONTAINER:
                                case BANNER_SLIDER_CONTAINER_ITEM:
                                    // Notify Data Changed..  of Adaptor...
                                    break;
                                default:
                                    break;
                            }
                            HomeFragment.homePageAdaptor.notifyDataSetChanged();
                            dialog.dismiss();
                            showToast( "Added Successfully..!" );

                            finish();
                        }
                        else{
                            dialog.dismiss();
                            if (view_type == BANNER_SLIDER_CONTAINER_ITEM){
                                homePageList.get( layoutIndex ).getBannerModelList().remove( homePageList.get( layoutIndex ).getBannerModelList().size() -1 );
                            }
                            showToast( "failed..! Error : " + task.getException().getMessage() );
                        }
                    }
                } );
//        }else{
//            dialog.dismiss();
//        }

    }

    // Permission... Crop And Set Image...
    private boolean isPermissionGranted(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( this.checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    startCompressImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showToast( error.getMessage() );
            }
        }

    }

    private void startCropImageActivity(Uri imageUri) {

        if (bannerDialogType == BANNER_SLIDER_CONTAINER_ITEM){
            CropImage.activity(imageUri)
                    .setGuidelines( CropImageView.Guidelines.ON)
                    .setAspectRatio( 2,1 )
                    .setMultiTouchEnabled(true)
                    .start(this);
        }else{
            CropImage.activity(imageUri)
                    .setGuidelines( CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(this);
        }

    }
    public Uri getImageUri( Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
//        MediaStore.Images.Media.getContentUri(  );
        return Uri.parse(path);
    }

    private void startCompressImage(@NonNull Bitmap bitmap){
//        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] imageInByte = stream.toByteArray();
//        long sizeOfImage = imageInByte.length/1024;

        bitmap.compress( Bitmap.CompressFormat.JPEG,35, stream);
        byte[] BYTE = stream.toByteArray();
        Bitmap newBitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

//        String imgLink = getImageUri(newBitmap).toString();
        bannerImageUri = getImageUri(newBitmap);
        UpdateImages.uploadImageLink = null;
        Glide.with( this ).load( bannerImageUri ).into( bannerDialogBannerImage );

    }

    //---------- Check Product Id is exist already or Not...
    private void checkForProductID( final String uploadProductID ){
        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( "PRODUCTS" ).document( uploadProductID )
                .addSnapshotListener( new EventListener <DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()){
                            // Regenerate Product ID...
                            bannerClickID = uploadProductID;
                        }else{
                            // Set Product ID...
                            bannerClickID = null;
                            bannerProductIdText.setError( "Not Found!" );
                        }
                        dialog.dismiss();
                    }
                } );
    }



}
