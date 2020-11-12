package ean.ecom.eanshopadmin.addnew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.home.HomeFragment;
import ean.ecom.eanshopadmin.other.CheckInternetConnection;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.StaticMethods;
import ean.ecom.eanshopadmin.product.ProductModel;
import ean.ecom.eanshopadmin.product.ProductSubModel;
import ean.ecom.eanshopadmin.product.update.specification.AddSpecificationModel;

import static ean.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.GALLERY_CODE;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_EGG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_NON_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_OTHERS;
import static ean.ecom.eanshopadmin.other.StaticValues.READ_EXTERNAL_MEMORY_CODE;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class AddNewProductActivity extends AppCompatActivity {

    private final int MRP_CHANGED = 10;
    private final int SELLING_CHANGED = 11;
    private final int D_RS_CHANGED = 12;
    private final int D_PER_CHANGED = 13;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private DialogsClass dialogsClass = new DialogsClass();
    private Dialog dialog;
//    private Dialog perDialog;

    private TextView newProductIDText; // new_pro_id_text
    //            <!--    Section 1: Add Images..-->
    private LinearLayout secAddImageLayout; // sec_1_add_image_layout
    private RecyclerView newProImageRecycler; // new_pro_images_recycler
//    private Spinner newProMainImageSpin; // new_pro_main_image_spinner
    private Button newProUploadImageBtn; // new_prod_upload_images_btn

    // Image Adaptor... and List...
    public AddImageAdaptor imgAdaptor;
    public static List <String> productImageSelectList;
    public static List<UploadImageDataModel> uploadImageDataModelList;

//    public ArrayAdapter <String> dataAdapter ;
    //            <!-- Section 2: Add Information...-->
    private LinearLayout secAddInfoLayout; // sec_2_add_info_layout
    private EditText newProFullName; // new_pro_full_name
    private EditText newProdShortName; // new_pro_short_name
    private Switch sameNameSwitch; // new_pro_same_name_switch
    private EditText newProMrpRate; // new_pro_mrp_rate
    private EditText newProSellingPrice; // new_pro_selling_price
    private TextView newProPerDiscount; // new_pro_per_discount
    private TextView newProRsDiscount; // new_pro_rs_discount
    private EditText newProStockAvailable; // new_pro_stock_available
    private Spinner newProQtyTypeText;
    private EditText newProVersionWeight; // new_pro_version_weight_et
    private Switch newProCodSwitch; // new_pro_cod_switch\
    private Spinner newProVeganMark; // new_pro_veg_non_type

    private LinearLayout newProVeganLayoutSample; // new_pro_label_vegan_sample_layout
    private TextView newProDetailsSampleText;

    //            <!-- Section 3: Add Descriptions and Specifications...-->
//    private LinearLayout secAddDesSpecifyLayout; // sec_3_add_des_specific_layout
//    private Switch   useTabLayoutSwitch; // new_pro_tab_layout_switch_sec_3
//    private EditText newProDescription; // new_pro_description
//    private RecyclerView newProSpecificationRecycler; // new_pro_specification_recycler

//    private AddSpecificationAdaptor addSpecificationAdaptor = new AddSpecificationAdaptor();
//    private AddSpecificationFeatureAdaptor addSpecificationFeatureAdaptor;
//    // Specification... and feature list...
//    private List<AddSpecificationFeatureModel> specificationFeatureModelList;
//    private List<AddSpecificationModel> specificationModelList = new ArrayList <>();

    //           <!-- Section 4: Add Searching Tags...-->
    private LinearLayout secProDetailAndTagLayout; // sec_4_pro_details_and_tags_layout
    private EditText newProDetails; // new_pro_details

    private EditText newProDescription; // new_pro_description
    // Search Tags...
    private TextView searchTagsText;
    private ImageView searchTagVisibleBtn;
    private EditText searchTagEditText; // new_pro_searching_tags
    private TextView searchTagAdd_Btn;
    private TextView searchTagRemove_Btn;

    //    Submit Button...
    private Button newProSubmitBtn; // new_pro_upload_btn

    //==========
    private int tempVal = 0;
    private int tempVal2 = 0;
    private String tempStrVal;
    private int productLabelVeganMark = 0;
//    private String mainImageLink = null;
    private boolean isUploadImages = false;
    private String uploadProductID;
    private String productCat;
    private int catIndex;
    private int layIndex;
    private int productIndex;
    private String qtyTypeText = null;
    private String tagString;
    private int verCode = 1;

    private Switch offerSwitch;
    private EditText offerText;
    //    private DocumentSnapshot documentSnapshot;
    private boolean isUpdateRequest = false;

    // ----------------*** OnCreate Method ***------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_product );

        dialog = DialogsClass.getDialog( this );
//        productDetails.onSpecificationUpdate();

//        uploadProductID = getIntent().getStringExtra( "PRODUCT_ID" );
//        productCat = getIntent().getStringExtra( "PRODUCT_CAT" );
        catIndex = getIntent().getIntExtra( "CAT_INDEX", -1 );
        layIndex = getIntent().getIntExtra( "LAY_INDEX", -1 );
        isUpdateRequest = getIntent().getBooleanExtra( "UPDATE", false );

        productIndex = getIntent().getIntExtra( "PRO_INDEX", -1 ); // Product Index denote the Index of the current Product in the Root List...
        // Get Product Variant....

        newProductIDText = findViewById( R.id.new_pro_id_text );
//            <!--    Section 1: Add Images..-->
        secAddImageLayout = findViewById( R.id.sec_1_add_image_layout );
        newProImageRecycler = findViewById( R.id.new_pro_images_recycler );
//        newProMainImageSpin = findViewById( R.id.new_pro_main_image_spinner );
        newProUploadImageBtn = findViewById( R.id.new_prod_upload_images_btn );
//            <!-- Section 2: Add Information...-->
        secAddInfoLayout = findViewById( R.id.sec_2_add_info_layout );
        newProFullName = findViewById( R.id.new_pro_full_name );
        newProdShortName = findViewById( R.id.new_pro_short_name );
        sameNameSwitch = findViewById( R.id.new_pro_same_name_switch );
        newProMrpRate = findViewById( R.id.new_pro_mrp_rate );
        newProSellingPrice = findViewById( R.id.new_pro_selling_price );
        newProPerDiscount = findViewById( R.id.new_pro_per_discount );
        newProRsDiscount = findViewById( R.id.new_pro_rs_discount );
        newProStockAvailable = findViewById( R.id.new_pro_stock_available );
        newProVersionWeight = findViewById( R.id.new_pro_version_weight_et );
        newProQtyTypeText = findViewById( R.id.new_pro_qty_type );
        newProCodSwitch = findViewById( R.id.new_pro_cod_switch );
        newProVeganMark = findViewById( R.id.new_pro_veg_non_type );
        // Offer Switch...
        offerSwitch = findViewById(R.id.new_pro_offer_switch);
        offerText = findViewById(R.id.new_pro_offer_edit_text);
//            <!-- Section 3: Add Descriptions and Specifications...-->
//        secAddDesSpecifyLayout = findViewById( R.id.sec_3_add_des_specific_layout );
//        useTabLayoutSwitch = findViewById( R.id.new_pro_tab_layout_switch_sec_3 );
//        newProDescription = findViewById( R.id.new_pro_description );
//        newProSpecificationRecycler = findViewById( R.id.new_pro_specification_recycler );
//           <!-- Section 4: Add Searching Tags...-->
        secProDetailAndTagLayout = findViewById( R.id.sec_4_pro_details_and_tags_layout );
        newProDetails = findViewById( R.id.new_pro_details );
        newProDescription = findViewById( R.id.new_pro_description );

        searchTagEditText = findViewById( R.id.new_pro_searching_tags );
        searchTagAdd_Btn = findViewById( R.id.search_tag_add_text_button );
        searchTagRemove_Btn = findViewById( R.id.search_tag_remove_text_button );
        searchTagVisibleBtn = findViewById( R.id.search_tag_visibility_image_view );
        searchTagsText = findViewById( R.id.new_pro_searching_tags_text );
//        submit Button.. Assigning...
        newProSubmitBtn = findViewById( R.id.new_pro_upload_btn );

        newProVeganLayoutSample = findViewById( R.id.new_pro_label_vegan_sample_layout );
        newProDetailsSampleText = findViewById( R.id.new_pro_details_sample_textview );

        //------------------------------------------------------------------------------------

        if (isUpdateRequest){
//            newProCodSwitch.setVisibility( View.INVISIBLE );
            newProDetailsSampleText.setVisibility( View.GONE );
            newProVeganLayoutSample.setVisibility( View.GONE );
            newProDetails.setVisibility( View.GONE );

            try {
                verCode = homeCatListModelList.get( catIndex ).getHomeListModelList().get( layIndex )
                        .getProductModelList().get( productIndex ).getProductSubModelList().size() + 1;
                uploadProductID = homeCatListModelList.get( catIndex ).getHomeListModelList().get( layIndex ).getProductModelList().get( productIndex ).getpProductID();
//            homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex );
            }catch (IndexOutOfBoundsException e){
                showToast( "Exception Occur : "+ e.getMessage() + " Please Try Again!");
                finish();
                return;
            }
            getTagsFromDatabase();
            newProductIDText.setText( "Product ID : " +  uploadProductID );
        }
        else{
            // Add New Product Id...
            uploadProductID = StaticMethods.getRandomProductId( AddNewProductActivity.this );

            newProDetailsSampleText.setVisibility( View.VISIBLE );
            newProVeganLayoutSample.setVisibility( View.VISIBLE );
            newProDetails.setVisibility( View.VISIBLE );

            if (uploadProductID != null){
                checkForProductID( ); // Check Product ID is Exist or not...
            }else{
                showToast( "Please Try Again!" );
                finish();
            }
        }

//        newProductIDText.setText( "Product ID : " +  uploadProductID );

        imgAdaptor = new AddImageAdaptor();
        productImageSelectList = new ArrayList <>();
        uploadImageDataModelList = new ArrayList <>();
        productImageSelectList.add( "Select Image" );

        // Add Image Recycler....
        LinearLayoutManager imgLayoutManager = new LinearLayoutManager( this );
        imgLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
        newProImageRecycler.setLayoutManager( imgLayoutManager );
        newProImageRecycler.setAdapter( imgAdaptor );
        imgAdaptor.notifyDataSetChanged();

        // upload Image Btn...
        newProUploadImageBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productImageSelectList.size() - 1 == uploadImageDataModelList.size()){
                    if (productImageSelectList.size() > 1 ){
                        isUploadImages = true;
                        showToast( "Upload Successfully..! Please add new Image to upload again.!");
                    }else{
                        showToast( "Please add Images first.!!" );
                    }
                }
                else{
                    uploadProductImages();
                }
            }
        } );

        // Qty Type Text Adopter...
        ArrayAdapter<String> qtyTypeList = new ArrayAdapter<String>( this,
                android.R.layout.simple_spinner_item, getResources().getStringArray( R.array.qty_auto_text_list ) );
        newProQtyTypeText.setAdapter( qtyTypeList );
        newProQtyTypeText.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if (position > 0){
                    qtyTypeText = parent.getItemAtPosition( position ).toString();
                    if (position == 12){
                        newProVersionWeight.setText( "NONE" );
                        qtyTypeText = "NONE";
                    }else if( !TextUtils.isEmpty( newProVersionWeight.getText().toString() )){
                        if ( newProVersionWeight.getText().toString().toUpperCase().equals( "NONE" ) ){
                            newProVersionWeight.setText( "" );
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                qtyTypeText = null;
            }
        } );

        // ------------  Sec 2 -- Text Watcher...
        priceAndDiscountTextWatcher();
        selectProductVegNoNType();

        // ----------  Search Tags...
        searchTagVisibleBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set visibility...
                if (searchTagsText.getVisibility() == View.VISIBLE){
                    searchTagsText.setVisibility( View.GONE );
                    searchTagVisibleBtn.setImageResource( R.drawable.ic_visibility_off_black_24dp );
                }else{
                    if (tagString == null){
                        updateTagList( SHOP_ID );
                    }else
                        searchTagsText.setVisibility( View.VISIBLE );
                    searchTagVisibleBtn.setImageResource( R.drawable.ic_visibility_black_24dp );
                }
            }
        } );

        searchTagAdd_Btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNotEmptyEditText( searchTagEditText )){
                    updateTagList(searchTagEditText.getText().toString());
                    searchTagEditText.setText( "" );
                }
            }
        } );

        searchTagRemove_Btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNotEmptyEditText( searchTagEditText ) && tagString != null){
//                    updateTagList(searchTagEditText.getText().toString());
                    String rmTag = searchTagEditText.getText().toString().toLowerCase();
                    ArrayList<String> tempList = new ArrayList <>();
                    tempList.addAll( Arrays.asList( tagString.split( ", " ) ) );
                    tempList.remove( rmTag );
                    tagString = String.valueOf( tempList );
                    tagString = null;
                    for (String s: tempList){
                        if (tagString != null){
                            tagString = tagString + s.trim() + ", ";
                        }else{
                            tagString = s.trim() + ", ";
                        }
                    }
                    searchTagsText.setText( tagString );
                    searchTagEditText.setText( "" );
                }
            }
        } );
        // ----------  Search Tags...

        // Submit Button Click Action...
        newProSubmitBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if (isValidFieldsData() && CheckInternetConnection.isInternetConnected( AddNewProductActivity.this )){
//                    updateProduct(); // TODO: JOB..!
//                    getFinalTagList();
                    updateTagList( SHOP_ID );
                    createMapToUpload( uploadProductID, verCode );
                }else{
                    dialog.dismiss();
                }
            }
        } );

        // offer Switch
        offerSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    offerText.setVisibility( View.VISIBLE );
                    offerText.setText( "" );
                }else{
                    offerText.setVisibility( View.INVISIBLE );
                    offerText.setText( "" );
                }
            }
        } );

    }

    // ----------------*** OnCreate Method ***------------------------------------------------------
    private void showToast(String str){
        Toast.makeText( this, str, Toast.LENGTH_SHORT ).show();
    }
    private boolean isNotEmptyEditText(EditText editText){
        if (TextUtils.isEmpty( editText.getText().toString() )){
            editText.setError( "Required field..!" );
            return false;
        }else{
            return true;
        }
    }
    private boolean isValidSize(EditText editText, int size){
        if (editText.getText().toString().length() < size){
            editText.setError( "Require min length : "+ size + "..!");
            return false;
        }else{
            return true;
        }
    }


    // Validation For section 2...
    private boolean isValidFieldsData()
    {
        if (uploadImageDataModelList.size() == 0){
            dialog.dismiss();
            DialogsClass.alertDialog( this, null, "Add Product Images..!" ).show();
            return false;
        } else if( !isUploadImages ){
            dialog.dismiss();
            DialogsClass.alertDialog( this, null, "Upload Images first..!" ).show();
            return false;
        }
        if (isNotEmptyEditText( newProFullName ) && isNotEmptyEditText( newProdShortName )&&
                isNotEmptyEditText( newProMrpRate )&& isNotEmptyEditText( newProSellingPrice )&&
                isNotEmptyEditText( newProStockAvailable ) ){
            if (Integer.parseInt( newProMrpRate.getText().toString()) < Integer.parseInt( newProSellingPrice.getText().toString() )){
                dialog.dismiss();
                DialogsClass.alertDialog( this, null, "Selling price can not greater than MRP rate..!" ).show();
                return false;
            }
//            if (mainImageLink == null){
//                dialog.dismiss();
//                DialogsClass.alertDialog( this, null, "Select Main Image..!" ).show();
//                return false;
//            }

//            &&

            if(!isUpdateRequest){
                // Not Update request....
                if (productLabelVeganMark == 0){
                    DialogsClass.alertDialog( this, null, "Select Product Label..!" ).show();
                    return false;
                }
//                if (!isNotEmptyEditText( newProDetails )){
//                    showToast( "Enter Missing Fields!" );
//                    return false;
//                }

//                if (!isNotEmptyEditText( newProDescription )){
//                    showToast( "Enter Missing Fields!" );
//                    return false;
//                }

            }

            if (qtyTypeText == null){
                dialog.dismiss();
                DialogsClass.alertDialog( this, null, "Select Quantity Type..!" ).show();
                return false;
            }

//            if (StaticMethods.isValidWeight( newProVersionWeight, this ) && qtyTypeText!=null){
//                return true;
//            }else{
//                return false;
//            }

            if (isNotEmptyEditText(newProVersionWeight) &&  qtyTypeText!=null){ // Finally....
                return true;
            }else{
                DialogsClass.alertDialog( this, null, "Select Quantity Type..!" ).show();
                return false;
            }

        }
        else{
            dialog.dismiss();
            DialogsClass.alertDialog( this, null, "Add Missing Fields..!" ).show();
            return false;
        }
    }

    // Validation For section 2...
    // tag List...
    private void updateTagList(String tString){

        if (isNotEmptyEditText( newProFullName )){
            tString = tString + " " + newProFullName.getText().toString();
        }
        if (isNotEmptyEditText( newProdShortName )){
            tString = tString + " " + newProdShortName.getText().toString();
        }
        tString.replaceAll( "[^a-zA-Z0-9]", "" );

        if (tagString == null){
            tString = SHOP_DATA_MODEL.getShop_name().toLowerCase().replaceAll( " ", ", " ) + ", " + tString.toLowerCase().replaceAll( " ", ", " );
        }else{
            tString = tagString + ", "+  SHOP_DATA_MODEL.getShop_name().toLowerCase().replaceAll( " ", ", " ) + ", "
                    + tString.toLowerCase().replaceAll( " ", ", " );
        }

        String[] tagArray = tString.split( "," );
        tagString = StaticMethods.removeDuplicate( tagArray );
        searchTagsText.setText( tagString );
        searchTagsText.setVisibility( View.VISIBLE );
    }

    // Price and Discount && Veg- Non Veg TextWatcher...
    private void priceAndDiscountTextWatcher(){
        newProMrpRate.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( ! TextUtils.isEmpty( newProMrpRate.getText().toString().trim() ) && ! TextUtils.isEmpty( newProSellingPrice.getText().toString().trim() )){
                    if (Integer.parseInt( newProMrpRate.getText().toString().trim() ) < Integer.parseInt( newProSellingPrice.getText().toString().trim() )){
                        newProMrpRate.setError( "MRP can't be small from Selling Price..!" );
                    }else {
                        setAutoText( MRP_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if ( ! TextUtils.isEmpty( newProMrpRate.getText().toString().trim() ) && ! TextUtils.isEmpty( newProSellingPrice.getText().toString().trim() )){
                    if (Integer.parseInt( newProMrpRate.getText().toString().trim() ) < Integer.parseInt( newProSellingPrice.getText().toString().trim() )){
                        newProMrpRate.setError( "MRP can't be small from Selling Price..!" );
                    }else {
                        setAutoText( MRP_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
                    }
                }
            }
        } );
        newProSellingPrice.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if ( TextUtils.isEmpty( newProMrpRate.getText().toString().trim() )){
                    newProMrpRate.setError( "Enter MRP..!" );
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( ! TextUtils.isEmpty( newProMrpRate.getText().toString().trim() ) && ! TextUtils.isEmpty( newProSellingPrice.getText().toString().trim() )){
                    if (Integer.parseInt( newProMrpRate.getText().toString().trim() ) < Integer.parseInt( newProSellingPrice.getText().toString().trim() )){
                        newProSellingPrice.setError( "Selling Price should be less or equal from MRP.!" );
                    }
                }else {
                    newProMrpRate.setError( "Enter MRP..!" );
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if ( ! TextUtils.isEmpty( newProMrpRate.getText().toString().trim() ) && ! TextUtils.isEmpty( newProSellingPrice.getText().toString().trim() )){
                    if (Integer.parseInt( newProMrpRate.getText().toString().trim() ) < Integer.parseInt( newProSellingPrice.getText().toString().trim() )){
                        newProSellingPrice.setError( "Selling Price should be less or equal from MRP.!" );
                    }else{
                        // Code to set Value...
                        if (! TextUtils.isEmpty( newProSellingPrice.getText().toString().trim() ))
                            setAutoText( SELLING_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
                    }
                }else {
                    newProMrpRate.setError( "Enter MRP..!" );
                }
            }
        } );
//        newProRsDiscount.addTextChangedListener( new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////                if ( ! TextUtils.isEmpty( newProRsDiscount.getText().toString().trim() )){
//////                    tempVal2 = Integer.parseInt( newProRsDiscount.getText().toString() );
//////                }
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                setAutoText( D_RS_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (tempVal2 !=  Integer.parseInt( newProRsDiscount.getText().toString() )){
//                    setAutoText( D_RS_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
//                }
//            }
//        } );
//        newProPerDiscount.addTextChangedListener( new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////                if ( ! TextUtils.isEmpty( newProPerDiscount.getText().toString().trim() )){
////                    tempVal = Integer.parseInt( newProPerDiscount.getText().toString() );
////                }
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                setAutoText( D_PER_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (tempVal !=  Integer.parseInt( newProPerDiscount.getText().toString() )){
//                    setAutoText( D_PER_CHANGED, newProMrpRate, newProSellingPrice, newProPerDiscount, newProRsDiscount );
//                }
//            }
//        } );

        // Switch Checked... - Section 3..//
        sameNameSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if( isChecked ){
                    // Code to Show TabView...
                    newProdShortName.setText( newProFullName.getText().toString() );
                }else{
                    // Hide the TabView...
                    newProdShortName.setText( "" );
                }
            }
        } );

    }
    private void setAutoText(int reqType, EditText mrpEdTxt, EditText sellingEdTxt, TextView offPerEdTxt, TextView offRsEdTxt){
        double mrpValue = Integer.parseInt( mrpEdTxt.getText().toString().trim() );
        double sellValue = Integer.parseInt( sellingEdTxt.getText().toString().trim() );
        int offPerValue;
        int offRsValue;

        double offPer;

        // if MRP and Selling Entered by user...
        switch (reqType){
            case SELLING_CHANGED:
                if (isNotEmptyEditText( mrpEdTxt )){
                    offRsValue = (int) (mrpValue - sellValue);
                    offRsEdTxt.setText( String.valueOf( offRsValue ) );
//                    offPerValue = (offRsValue * 100)/mrpValue;
                    offPer = (double)((offRsValue/mrpValue) * 100);
                    if (String.valueOf( offPer ).length()>=4){
                        offPerEdTxt.setText( String.valueOf( offPer ).substring( 0, 4 ) );
                    }else{
                        offPerEdTxt.setText( String.valueOf( offPer ) );
                    }
                }
                break;
            case MRP_CHANGED:
                if (isNotEmptyEditText( sellingEdTxt )){
                    offRsValue = (int) (mrpValue - sellValue);
                    tempVal2 = offRsValue;
                    offRsEdTxt.setText( String.valueOf( offRsValue ) );
//                    offPerValue = (offRsValue * 100)/mrpValue;
                    offPer = (double)((offRsValue/mrpValue) * 100);
                    if (String.valueOf( offPer ).length()>=4){
                        offPerEdTxt.setText( String.valueOf( offPer ).substring( 0, 4 ) );
                    }else{
                        offPerEdTxt.setText( String.valueOf( offPer ) );
                    }
                }
                break;
            default:
                break;
        }
    }
    //  Veg- Non Veg
    private void selectProductVegNoNType(){
        // Select Banner Type...
        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray( R.array.product_vegan_mark ));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newProVeganMark.setAdapter(dataAdapter);
        newProVeganMark.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {

                if (position != 0){ // Product Type
                    switch (position){
                        case 1: // Pure Veg
                            productLabelVeganMark = PRODUCT_LACTO_VEG;
                            break;
                        case 2: // Non Veg
                            productLabelVeganMark = PRODUCT_LACTO_NON_VEG;
                            break;
                        case 3: // Egg
                            productLabelVeganMark = PRODUCT_LACTO_EGG;
                            break;
                        case 4: // Others
                            productLabelVeganMark = PRODUCT_OTHERS;
                            break;
                        default:
                            break;
                    }

                }else{
                    productLabelVeganMark = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//
            }
        } );
    }
    // Price and Discount TextWatcher...

    // Permission...
    private boolean isPermissionGranted(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( this.checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
                // TODO : YES
                return true;
            }else{
                // TODO : Requiest For Permission..!
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
        CropImage.activity(imageUri)
                .setGuidelines( CropImageView.Guidelines.ON)
//                .setAspectRatio( 1,1 )
                .setMultiTouchEnabled(true)
                .start(this);
    }
    public Uri getImageUri( Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
//        MediaStore.Images.Media.getContentUri(  );
        return Uri.parse(path);
    }
    private void startCompressImage(@NonNull Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.JPEG,50, stream);
        byte[] BYTE = stream.toByteArray();
        Bitmap newBitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);
//        bannerImageUri = getImageUri(newBitmap);

        uploadImageDataModelList.add( new UploadImageDataModel( getImageUri(newBitmap).toString(), "") );
        isUploadImages = false;
        imgAdaptor.notifyDataSetChanged();

    }

    // ----------------  Image Adaptor and Model Class ---------------------------------------------
    private class AddImageAdaptor extends RecyclerView.Adapter<AddImageAdaptor.ViewHolder> {
        public AddImageAdaptor() { }

        @NonNull
        @Override
        public AddImageAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View imageView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.new_pro_image_item, parent, false );
            return new ViewHolder( imageView );
        }

        @Override
        public void onBindViewHolder(@NonNull AddImageAdaptor.ViewHolder holder, int position) {
            // Data set...
            int size = uploadImageDataModelList.size();
            if (position < size){
                holder.setData( uploadImageDataModelList.get( position ).getImgLink(), position );
            }else if (size < 8){
                holder.addNewImage();
            }

        }

        @Override
        public int getItemCount() {
            if (uploadImageDataModelList.size() < 8){
                return uploadImageDataModelList.size() + 1;
            }else{
                return uploadImageDataModelList.size();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView itemImage;
            private TextView itemImageNo;
            private ImageButton editImage;
            private LinearLayout addNewImage;
            public ViewHolder(@NonNull View itemView) {
                super( itemView );
                itemImage = itemView.findViewById( R.id.image_view );
                addNewImage = itemView.findViewById( R.id.add_image_layout );
                editImage = itemView.findViewById( R.id.edit_imgBtn );
                itemImageNo = itemView.findViewById( R.id.pro_image_no );
            }
            private void setData(String imgLink, final int position){
                int imgNo = position + 1;
                addNewImage.setVisibility( View.GONE );
                editImage.setVisibility( View.VISIBLE );
                itemImage.setVisibility( View.VISIBLE );
                itemImageNo.setVisibility( View.VISIBLE );
                itemImageNo.setText( String.valueOf( imgNo ) );

                if ( position < productImageSelectList.size()-1){
                    // Use Link...
                    Glide.with( itemView.getContext() ).load( imgLink ).apply( new RequestOptions().placeholder( R.drawable.ic_phone_black_24dp ) ).into( itemImage );
                }else {
                    // Use Uri...
                    Glide.with( itemView.getContext() ).load( Uri.parse( imgLink ) ).into( itemImage );
                }

                editImage.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO : Action....
                        removeImageFromList( position );
                    }
                } );
            }
            private void addNewImage(){
                editImage.setVisibility( View.GONE );
                itemImage.setVisibility( View.GONE );
                itemImageNo.setVisibility( View.GONE );
                addNewImage.setVisibility( View.VISIBLE );
                addNewImage.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPermissionGranted( )){
                            Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                            galleryIntent.setType( "image/*" );
                            startActivityForResult( galleryIntent, GALLERY_CODE );
                        }
                    }
                } );
            }
        }
    }
    private class UploadImageDataModel {
        String imgLink;
        String imgName;

        public UploadImageDataModel(String imgLink, String imgName) {
            this.imgLink = imgLink;
            this.imgName = imgName;
        }

        public String getImgLink() {
            return imgLink;
        }

        public void setImgLink(String imgLink) {
            this.imgLink = imgLink;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }
    }

    //----------
    private String getNewImageName(){
        int tempVer = ( verCode )- 1;
        String newName = "";
        int sizeofList = uploadImageDataModelList.size();
        for (int i = 0; i < sizeofList; i++ ){
            newName = tempVer + "" + i;
            if( ! newName.equals( uploadImageDataModelList.get( i ).getImgName() ) ){
//                newName = String.valueOf( productIndex ) + String.valueOf( i );
                uploadImageDataModelList.get( i ).setImgName( newName );
                break;
            }else if ( i == sizeofList - 1){
                newName = tempVer + "" + sizeofList; // String.valueOf( sizeofList );
            }
        }

        return newName;
    }
    public void uploadProductImages(){
        // Upload Method...
        dialog.show();
//        final TextView perText = perDialog.findViewById( R.id.process_per_complete_text );
//        perText.setVisibility( View.VISIBLE );
        for (int x = productImageSelectList.size() - 1; x < uploadImageDataModelList.size(); x++){
            tempStrVal = getNewImageName();
            productImageSelectList.add( "" );
            StorageReference storageRef = storageReference.child( "SHOPS/" + SHOP_ID + "/products/" + uploadProductID + "/" + tempStrVal + ".jpg" );
            uploadImageAsItAs( Uri.parse( uploadImageDataModelList.get( x ).getImgLink() ), storageRef, tempStrVal, x);
        }
    }
    private void uploadImageAsItAs( Uri fileUri,  final StorageReference storageRef, final String fileName, final int uploadedSize ){

        final UploadTask uploadTask = storageRef.putFile(fileUri);
//        final int num = uploadedSize + 1;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                showToast( "Failed to upload..! Something went wrong");
//                perDialog.dismiss();
                dialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener <UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                updateListData( storageRef, fileName, uploadedSize);
            }
        }).addOnProgressListener(new OnProgressListener <UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                int num = uploadedSize + 1;
                dialog.show();
//                TextView perText = perDialog.findViewById( R.id.process_per_complete_text );
                int progress = (int)((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
//                perText.setText( "Image " + (uploadedSize + 1) + " Uploading " + progress + "% completed");
            }
        }).addOnPausedListener(new OnPausedListener <UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                TextView perText = perDialog.findViewById( R.id.process_per_complete_text );
//                perText.setText( "Uploading Pause.! \n Check your net connection.!" );
            }
        });
    }
    private void updateListData( final StorageReference storageReference, final String fileName, final int uploadedSize){

        storageReference.getDownloadUrl().addOnCompleteListener( new OnCompleteListener <Uri>() {
            @Override
            public void onComplete(@NonNull Task <Uri> task) {
                if (task.isSuccessful()){
//                            dialog.dismiss();
//                            uploadImageLink = task.getResult().toString();
//                            int num = uploadedSize + 1;
                    uploadImageDataModelList.get( uploadedSize ).setImgLink( task.getResult().toString() );
                    uploadImageDataModelList.get( uploadedSize ).setImgName( fileName );
//                            AddNewProductActivity.productImageLinkList.add( uploadedSize,  task.getResult().toString() );
                    productImageSelectList.set( (uploadedSize + 1), "Image " + (uploadedSize + 1) );
//                    dataAdapter.notifyDataSetChanged();
//                    perDialog.dismiss();
                    dialog.dismiss();
                    if ( uploadedSize == uploadImageDataModelList.size() - 1 ){
                        showToast( "Upload Images Successfully..!" );
                        isUploadImages = true;
                        imgAdaptor.notifyDataSetChanged();
                    }
                }else{
                    // Failed Query to getDownload Link...
//                    perDialog.dismiss();
                    dialog.dismiss();
                    showToast( task.getException().getMessage().toString() );
                }
            }
        } );

    }
    private void removeImageFromList( int position ){

        if ( position < productImageSelectList.size()-1){
            // Meaning We also have to delete from database...
            int tempSize = productImageSelectList.size() - 2;
            productImageSelectList.clear();
            productImageSelectList.add( "Select Image" );
            for (int i = 1; i <= tempSize; i++){
                productImageSelectList.add( "Image "+ i );
//                dataAdapter.notifyDataSetChanged();
            }
        }
        uploadImageDataModelList.remove( position );
        imgAdaptor.notifyDataSetChanged();
    }
    //----------

    //---------- Check Product Id is exist already or Not...
    private void checkForProductID( ){
        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( "PRODUCTS" ).document( uploadProductID )
                .addSnapshotListener( new EventListener <DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()){
                            // Regenerate Product ID...
                            uploadProductID = StaticMethods.getRandomProductId( AddNewProductActivity.this );
                            if (uploadProductID != null){
                                checkForProductID(  ); // Check Product ID is Exist or not...
                            }else{
                                showToast( "Something went wrong, Please Try Again!" );
                                finish();
                            }
                        }else{
                            // Set Product ID...
                            newProductIDText.setText( "Product ID : " +  uploadProductID );
                        }
                    }
                } );
    }

    private void getTagsFromDatabase(){
        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( "PRODUCTS" ).document( uploadProductID )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                ArrayList<String> tags = (ArrayList <String>) task.getResult().get( "tags" );
                String tagsString = tags.get( 0 );
                for (String s: tags){
                    tagsString = tagsString + ", " + s;
                }

                tagString = tagsString;
            }
        } );
    }

    // --------------- Upload Data on Database.. ---------------------------------------------------
    private void createMapToUpload(String pID, int verNo){
        String productID = pID;
//        int verNo = 1;
        Map <String, Object> updateMap = new HashMap <>();
        updateMap.clear();

        // Image List....
        List<String> imageList = new ArrayList <>();
        for (UploadImageDataModel model : uploadImageDataModelList){
            imageList.add( model.getImgLink() );
        }

        // Searching Tag...
        String [] sTags = tagString.split( ", " );
        List<String> tagList = new ArrayList <>();
        for ( String s : sTags){
            tagList.add( s.toLowerCase().trim() );
        }
//        tagList.addAll( Arrays.asList( sTags ) );

        /** a_current_state :
         1. N - Created Product but Not add details yet.!
         2. Y - Added Details and Update...
         3. V - Visible to search...
         4. I - InVisible : Stop from searching etc...
         */
        Boolean p_is_cod = false;
        if (newProCodSwitch.isChecked()){
            p_is_cod = true;
        }

        // Product SubList..-----------------------------------------------
        updateMap.put( "p_no_of_variants", verNo );
        // first version...
        updateMap.put( "p_name_"+verNo, newProdShortName.getText().toString() );
        updateMap.put( "p_selling_price_"+verNo,  newProSellingPrice.getText().toString().trim()  );
        updateMap.put( "p_mrp_price_"+verNo, newProMrpRate.getText().toString().trim() );

        updateMap.put( "p_stocks_"+verNo, newProStockAvailable.getText().toString() );
        updateMap.put( "p_offer_"+verNo, offerText.getText().toString() );
        updateMap.put( "p_off_per_"+verNo, newProPerDiscount.getText().toString() );
        updateMap.put( "p_off_rupee_"+verNo, newProRsDiscount.getText().toString() );
        updateMap.put( "p_extra_amount_"+verNo, "" );

        if (qtyTypeText.equals( "NONE" )){
            updateMap.put( "p_weight_"+verNo, qtyTypeText );
        }else{
            updateMap.put( "p_weight_"+verNo, newProVersionWeight.getText().toString() + "-" +qtyTypeText );
        }
        // Description and Specifications...
        updateMap.put( "p_description_"+verNo, newProDescription.getText().toString() );
        updateMap.put( "p_specification_"+verNo, new ArrayList<AddSpecificationModel>()  );
        // Details ... Guides
        updateMap.put( "p_details_"+verNo, newProDetails.getText().toString()  );//newProDetails.getText().toString()
        updateMap.put( "p_guide_"+verNo, ""  ); // newProDetails.getText().toString()

        // Images...
        updateMap.put( "p_image_"+verNo, imageList );
        // Tags...
        updateMap.put( "tags", tagList );

        // time of update...
        updateMap.put( "p_last_update_time", StaticMethods.getCrrDate() + " " + StaticMethods.getCrrTime() );
        // Product SubList..-----------------------------------------------

        ProductSubModel productSubModel = new ProductSubModel(
                newProFullName.getText().toString(),
                imageList,
                newProSellingPrice.getText().toString().trim(),
                newProMrpRate.getText().toString().trim(),
                newProVersionWeight.getText().toString() + qtyTypeText,
                newProStockAvailable.getText().toString(),
                ""
        );

         /*
        Check => isUpdate ?
            YES =>
              Update On local List And Product Database...
            NO =>
                Update on Product List Database and Product Collection and local list...
         */
        if (isUpdateRequest){
            // Ad Another Version...
            updateProductAnotherVersion(productID,  updateMap, productSubModel );
        }
        else{
            // To add in Local List... New Product...
            updateMap.put( "a_current_state", "Y" );  // Check the current State
            updateMap.put( "a_no_of_uses", 1 ); // Check No. Of Uses...
            updateMap.put( "p_id", productID );
            // Primary - fields...
            updateMap.put( "p_main_name", newProFullName.getText().toString() );
            updateMap.put( "p_main_image", imageList.get( 0 ) );
            updateMap.put( "p_weight_type", qtyTypeText );
            updateMap.put( "p_veg_non_type", String.valueOf( productLabelVeganMark ) );
            updateMap.put( "p_offer_code", "" );
            updateMap.put( "p_is_cod", p_is_cod );

            updateMap.put( "product_details", newProDetails.getText().toString() );
            updateMap.put( "use_tab_layout", false );

            updateMap.put( "p_add_date_time", StaticMethods.getCrrDate() + " " + StaticMethods.getCrrTime() );

            if (qtyTypeText.toUpperCase().equals( "NONE" )){
                updateMap.put( "p_show_weight_"+verNo,  false );
            }else{
                updateMap.put( "p_show_weight_"+verNo,  true );
            }

            List<ProductSubModel> tempPSubList = new ArrayList <>();
            tempPSubList.add( productSubModel );

            ProductModel productModel = new ProductModel(
                    productID,
                    newProFullName.getText().toString() ,
                    newProDetails.getText().toString(),
                    p_is_cod,
                    String.valueOf(verNo),
                    qtyTypeText,
                    productLabelVeganMark,
                    tempPSubList
            );

            // Upload On The Database...
            uploadProduct( productID,  updateMap, productModel);
        }

    }

    /// Add New Products....
    private void uploadProduct(final String productID, Map<String, Object> updateMap, final ProductModel productModel){

        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( "PRODUCTS" ).document( productID ).set( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
//                    dialog.dismiss();
                            // add in Local List...
                            if (isUpdateRequest){
                                dialog.dismiss();
                                showToast( "Update Product Successfully..!" );
                            }
                            updateProductOnDatabase(  productID, productModel );
                        }else{
                            dialog.dismiss();
                            showToast( "Failed to Add Product..!" );
                        }
                    }
                } );
    }

    private void updateProductOnDatabase(  final String productID, final ProductModel productModel ){
        Map <String, Object> updateMap = new HashMap <>();
        int pListIndex = homeCatListModelList.get( catIndex ).getHomeListModelList().get( layIndex ).getProductIdList().size() + 1;
        updateMap.put( "no_of_products", pListIndex  );
        updateMap.put( "product_id_"+pListIndex, productID  );
        if (pListIndex == 4){
            updateMap.put( "visibility", true  );
        }

        homeCatListModelList.get( catIndex ).getHomeListModelList().get( layIndex ).getProductIdList().add( productID ); // Add product ID in the main List

        if (pListIndex < 4){
            homeCatListModelList.get( catIndex ).getHomeListModelList().get( layIndex ).getProductModelList().add( productModel ); // Add Product in the main List
        }

        // we are set our unique Id... Because we need this id to update data...
//        String documentId = layoutMap.get( "layout_id" ).toString();
        String categoryID = homeCatListModelList.get( catIndex ).getCatID();
        String documentId = homeCatListModelList.get( catIndex ).getHomeListModelList().get( layIndex ).getLayoutID(); // layout_id

        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .collection( categoryID ).document( documentId ).update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                            showToast( "Added Successfully..!" );
                            HomeFragment.homePageAdaptor.notifyDataSetChanged();
                            finish();
                        }
                        else{
                            dialog.dismiss();
                            showToast( "Product Added..! Error : " + task.getException().getMessage() );
                            finish();
                        }
                    }
                } );

    }
    /// Add Another Version...
    private void updateProductAnotherVersion(final String productID, Map<String, Object> updateMap, final ProductSubModel productSubModel){
        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( "PRODUCTS" ).document( productID )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            // Add Version... In Local List..
                            homeCatListModelList.get( catIndex ).getHomeListModelList().get( layIndex )
                                    .getProductModelList().get( productIndex ).getProductSubModelList().add( productSubModel );
                            homeCatListModelList.get( catIndex ).getHomeListModelList().get( layIndex )
                                    .getProductModelList().get( productIndex ).setpNumOfProducts( String.valueOf( verCode ) );
                            // Notify DataChanged...
                            dialog.dismiss();
                            if (HomeFragment.homePageAdaptor != null)
                                HomeFragment.homePageAdaptor.notifyDataSetChanged();
                            showToast( "Added Successfully!" );
                            finish();
                        }else{
                            showToast( "Process failed!" );
                            dialog.dismiss();
                        }
                    }
                } );
    }

    // CollectionID : CAT_ID
    // Documents ID : layout_id
    // Updates...
    // 1. no_of_products
    // 2. product_id_
    // 3. visibility
    /* Local List...
     homeListModelList.add( new HomeListModel( viewType, layout_id, layout_title, hrAndGridProductIdList,
                                            new ArrayList <ProductModel>() ) );
     */
     /*
    < List>
        List<HomeCatListModel> homeCatListModelList
        catID
        <List>
            List<HomeListModel> homeListModelList
             < List>
                List<ProductModel> productModelList
                productLayId
                Product ID List
                layoutGridTitle
            </List>
        </List>
    </List>

     */


}
