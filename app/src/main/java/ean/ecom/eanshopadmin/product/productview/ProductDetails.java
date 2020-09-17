package ean.ecom.eanshopadmin.product.productview;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.addnew.AddNewProductActivity;
import ean.ecom.eanshopadmin.product.update.AddNewImageAdaptor;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.other.CheckInternetConnection;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.product.ProductModel;
import ean.ecom.eanshopadmin.product.ProductSubModel;
import ean.ecom.eanshopadmin.product.ProductViewInterface;
import ean.ecom.eanshopadmin.product.search.ProductSearchActivity;
import ean.ecom.eanshopadmin.product.update.UpdateDataRequest;
import ean.ecom.eanshopadmin.product.update.UpdateProductFragment;
import ean.ecom.eanshopadmin.product.update.specification.AddSpecificationFeatureModel;
import ean.ecom.eanshopadmin.product.update.specification.AddSpecificationModel;
import ean.ecom.eanshopadmin.product.update.specification.UpdateImage_SpFragment;

import static ean.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_EGG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_NON_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_OTHERS;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_DESCRIPTION;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_DETAILS;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_GUIDE_INFO;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_IMAGES;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_NAME;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_PRICE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SPECIFICATION;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_STOCKS;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_WEIGHT;

public class ProductDetails extends AppCompatActivity implements ProductViewInterface {
    public static AppCompatActivity productDetails;

    private ImageView pVegNonTypeImage; // product_veg_non_type_image
    private LinearLayout weightSpinnerLayout;// weight_spinner_layout
    private Spinner weightSpinner; //weight_spinner
    private TextView weightText; // weight_text

    // --- Product Details Image Layout...
    private ViewPager productImagesViewPager;
    private TabLayout productImagesIndicator;
    private TextView productName;
    private TextView productPrice;
    private TextView productCutPrice;
    private TextView productCODText; // product_item_cod_text
    private TextView productStocksText; // stock_text

    // create a list for testing...
    private List <String> productImageList = new ArrayList <>();
    private ProductDetailsImagesAdapter productDetailsImagesAdapter;

    private List<String> productVariantList = new ArrayList <>();

    // --- Product Details Image Layout...
    // Product Features | Description | Details | Specification...
    private List<ProductFeaturesModel> productFeaturesModelList = new ArrayList <>();

    // Features Layouts...
    private LinearLayout guideLayout;
    private TextView guideTextView;
    private LinearLayout detailsLayout;
    private TextView detailsTextView;
    private LinearLayout descriptionLayout;
    private TextView descriptionTextView;
    private LinearLayout specificationLayout;
    private RecyclerView specificationRecycler;

    public static String productID;
    public static TextView badgeCartCount;

    // Dialogs...
    private Dialog dialog;
    private boolean isAllowBack = true;

    private int crrShopCatIndex;
    private int layoutIndex;
    private int productIndex;

    private int currentVariant = 0;

    public static ProductModel pProductModel = null;
    private Boolean isUpdateAllowed = true;

    private FrameLayout updateFrameLayout;
    private LinearLayout updateProductLayout;

    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_product_details );

        productDetails = this;
        pProductModel = null;
        productImageList.clear();

        // TODO : get product ID through Intent ...
        productID = getIntent().getStringExtra( "PRODUCT_ID" );
        crrShopCatIndex = getIntent().getIntExtra( "HOME_CAT_INDEX", -1 );
        layoutIndex = getIntent().getIntExtra( "LAYOUT_INDEX", -1 );
        productIndex = getIntent().getIntExtra( "PRODUCT_INDEX", -1 );

        if (productIndex != -1){
            isUpdateAllowed = true;
            // This is for layout product click...
            if (crrShopCatIndex != -1 && layoutIndex != -1 ){
                // If User come from product Layout...
                pProductModel = homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex );
            }else{
                // If User Come From Searching Page....
                pProductModel = ProductSearchActivity.searchProductList.get( productIndex );
            }

        }else{
            // If User Come from Banner Click...
            isUpdateAllowed = false;
            // TODO: Reload The Product Details...
        }

        dialog = DialogsClass.getDialog( ProductDetails.this );
        dialog.show();
        // ---- Progress Dialog...
        // Set Title on Action Menu
        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        try{
            // To test We assign a default PRODUCT_ID ...
            if (productID.isEmpty()){
//                productID = "";
                dialog.dismiss();
                Toast.makeText( this, "Product Not found.!", Toast.LENGTH_SHORT ).show();
                finish();
            }
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( productID );
        }catch (NullPointerException e){ showToast( e.getMessage() ); }

        // Update Product..
        updateProductLayout = findViewById( R.id.update_product_layout );
        updateFrameLayout = findViewById( R.id.update_product_frame );
        // --- Product Details Image Layout...
        productName = findViewById( R.id.product_item_name );
        productPrice = findViewById( R.id.product_item_price );
        productCutPrice = findViewById( R.id.product_item_cut_price );
        productCODText = findViewById( R.id.product_item_cod_text );
        // Set Default Visible...
        productCODText.setVisibility( View.VISIBLE );

        productStocksText = findViewById( R.id.stock_text );

        // --- Product Details Image Layout...

        pVegNonTypeImage = findViewById( R.id.product_veg_non_type_image );
        weightSpinnerLayout = findViewById( R.id.weight_spinner_layout );
        weightSpinner = findViewById( R.id.weight_spinner );
        weightText = findViewById( R.id.weight_text );

        //----------- Product Images ---
        productImagesViewPager = findViewById( R.id.product_images_viewpager );
        productImagesIndicator = findViewById( R.id.product_images_viewpager_indicator );

        // ---------- Product Description code----
        // ---------- Features ---
        guideLayout = findViewById( R.id.product_guide_layout );
        guideTextView = findViewById( R.id.product_guide_text );
        detailsLayout = findViewById( R.id.product_details_layout );
        detailsTextView = findViewById( R.id.product_details_text );
        descriptionLayout = findViewById( R.id.product_description_layout );
        descriptionTextView = findViewById( R.id.product_description_text );
        specificationLayout = findViewById( R.id.product_specification_layout );
        specificationRecycler = findViewById( R.id.product_specification_recycler );


        // set adapter with viewpager...
        productDetailsImagesAdapter = new ProductDetailsImagesAdapter( productImageList );
        productImagesViewPager.setAdapter( productDetailsImagesAdapter );
        // connect TabLayout with viewPager...
        productImagesIndicator.setupWithViewPager( productImagesViewPager, true );

        // Retrieve details from database...----------------
        getProductDetails();
        // SetData...
        if (pProductModel != null){
            setProductData( 0 );
            setOtherDetails();
        }

        // Refresh Option Menu...
        invalidateOptionsMenu();

    }

    @Override
    public void onBackPressed() {
        if (isAllowBack){
            super.onBackPressed();
        }else{

        }
    }

    private void backPressedUpdate(boolean isEnable){
        // Disable/Enable Back Pressed...
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( isEnable );
        isAllowBack = isEnable;
        isUpdateAllowed = isEnable;
        invalidateOptionsMenu();
    }

    private void setOtherDetails(){
        // set Product VegNon Veg...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setVegNonData();
        }
        if (pProductModel.getpIsCOD()){
            productCODText.setText( "Cash On Delivery Available" );
            productCODText.setTextColor( getResources().getColor( R.color.colorBlack) );
        }else{
            productCODText.setText( "COD Not Available" );
            productCODText.setTextColor( getResources().getColor( R.color.colorRed) );
        }

        // Add Weight Data in List...
        if (pProductModel.getProductSubModelList().get( currentVariant ).getpWeight()!=null){
            productVariantList.clear();
            for (int pWIndex = 0; pWIndex < pProductModel.getProductSubModelList().size(); pWIndex++ ) {
                productVariantList.add( pProductModel.getProductSubModelList().get( pWIndex ).getpWeight() );
            }
            weightSpinnerLayout.setVisibility( View.VISIBLE );
        }else{
            weightSpinnerLayout.setVisibility( View.GONE );
        }
        //  Set Weight Spinner...
        if(productVariantList.size() > 1){
            setWeightSpinner();
            weightText.setVisibility( View.GONE );
        }else{
            weightText.setVisibility( View.VISIBLE );
            weightSpinner.setVisibility( View.GONE );
            if (productVariantList.size()>0){
                weightText.setText( productVariantList.get( 0 ) );
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isUpdateAllowed){
            getMenuInflater().inflate( R.menu.menu_product_details_edit_options,menu);
            MenuItem cartItem = menu.findItem( R.id.menu_add_another_varient );
            return true;
        }else{
            return super.onCreateOptionsMenu( menu );
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        if (id == R.id.menu_add_another_varient){
            // Add Another Version....
            Intent addProduct = new Intent( this, AddNewProductActivity.class );

            addProduct.putExtra( "CAT_INDEX", crrShopCatIndex );
            addProduct.putExtra( "LAY_INDEX", layoutIndex );
            addProduct.putExtra( "UPDATE", true );
            addProduct.putExtra( "PRO_INDEX", productIndex );
//            homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex );
            startActivity( addProduct );
            return true;
        }else
        if (id == R.id.menu_stock_update){
            updateDialog( UPDATE_STOCKS );
            return true;
        }else
        if (id == R.id.menu_price_update){
            updateDialog( UPDATE_PRICE );
            return true;
        }else
        if (id == R.id.menu_cod_update){
            updateCOD();
            return true;
        }else
        if (id == R.id.menu_edit_name){
            updateDialog( UPDATE_NAME );
            return true;
        }else
        if (id == R.id.menu_edit_details){
            updateDialog( UPDATE_DETAILS );
            return true;
        }else
        if (id == R.id.menu_edit_description){
            updateDialog( UPDATE_DESCRIPTION );
            return true;
        }else
        if (id == R.id.menu_update_images){
            updateDialog(UPDATE_IMAGES);
            return true;
        }else
        if (id == R.id.menu_update_specifications){
            updateDialog(UPDATE_SPECIFICATION);
            return true;
        }else
        if (id == R.id.menu_update_guide_info){
            updateDialog(UPDATE_GUIDE_INFO);
            return true;
        }else
        if (id == R.id.menu_remove_products){
            return true;
        }
        else
        if (id == R.id.menu_update_weight){
            updateDialog( UPDATE_WEIGHT );
            return true;
        }

            return super.onOptionsItemSelected( item );
    }

    private void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private boolean isInternetConnected(){
        return CheckInternetConnection.isInternetConnected( this );
    }

    // -------  Get Product Details From DataBase ------
    private void getProductDetails(){
        // TODO: Retrieve details from database...----------------
        if (isInternetConnected()){
            DBQuery.firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                    .collection( "PRODUCTS" ).document( productID )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        int p_no_of_variants = 0;
                        if ( documentSnapshot.get( "p_no_of_variants" ) !=null ){
                            p_no_of_variants = Integer.valueOf( String.valueOf( (long) documentSnapshot.get( "p_no_of_variants" ) ) );
                        }

                        if (!isUpdateAllowed && p_no_of_variants>0){
                            /** Reload Model Data...*/

                            List<ProductSubModel> productSubModelList = new ArrayList <>();
                            for (int tempI = 1; tempI <= p_no_of_variants; tempI++){

                                // We can use Array...
                                ArrayList<String> Images = (ArrayList <String>) documentSnapshot.get( "p_image_" + tempI );
                                // add Data...
                                productSubModelList.add( new ProductSubModel(
                                        task.getResult().get( "p_name_"+tempI).toString(),
                                        Images,
                                        task.getResult().get( "p_selling_price_"+tempI).toString(),
                                        task.getResult().get( "p_mrp_price_"+tempI).toString(),
                                        task.getResult().get( "p_weight_"+tempI).toString(),
                                        task.getResult().get( "p_stocks_"+tempI).toString(),
                                        task.getResult().get( "p_offer_"+tempI).toString()
                                ) );
                            }
                            String p_id = task.getResult().get( "p_id").toString();
                            String p_main_name = task.getResult().get( "p_main_name" ).toString();
//                        String p_main_image = task.getResult().get( "p_main_image" ).toString();
                            String p_weight_type = task.getResult().get( "p_weight_type" ).toString();
                            int p_veg_non_type = Integer.valueOf( task.getResult().get( "p_veg_non_type" ).toString() );
                            Boolean p_is_cod = (Boolean) task.getResult().get( "p_is_cod" );

                            pProductModel = new ProductModel(
                                    p_id,
                                    p_main_name,
                                    " ",
                                    p_is_cod,
                                    String.valueOf(p_no_of_variants),
                                    p_weight_type,
                                    p_veg_non_type,
                                    productSubModelList
                            );

                        }
                        // Set other Details of  Product Details Image Layout...
                        // Load Product - Details | Description  | Features...
                        if (p_no_of_variants>0)
                            for (int tempX = 1; tempX <= p_no_of_variants; tempX++){
                            // add product Specifications..
                            List<Object> specificationModelList;
                            // Details...
                            String pDetails;
                            // Description...
                            String pDescription;
                            // Guide
                            String pGuide;
                            if (documentSnapshot.get( "p_specification_"+tempX ) != null){
                                specificationModelList = (List <Object>) documentSnapshot.getData().get( "p_specification_" + tempX );
                                //  Arrays.asList( tags )
//                                    specificationModelList = (ArrayList <AddSpecificationModel>) documentSnapshot.get("p_specification_" + tempX );
                            }else{
                                specificationModelList = new ArrayList <>();
                            }

                            if (documentSnapshot.get( "p_description_"+tempX ) != null){
                                pDescription = documentSnapshot.get( "p_description_"+tempX ).toString();
                            }else{
                                pDescription = "";
                            }
                            if (documentSnapshot.get( "p_details_"+tempX ) != null){ // p_details_
                                pDetails = documentSnapshot.get( "p_details_"+tempX ).toString();
                            }else{
                                pDetails = "";
                            }
                            if (documentSnapshot.get( "p_guide_"+tempX ) != null){ // p_details_
                                pGuide = documentSnapshot.get( "p_guide_"+tempX ).toString();
                            }else{
                                pGuide = "";
                            }

                            productFeaturesModelList.add( new ProductFeaturesModel(
                                    pDetails, pDescription, specificationModelList
                            ) );

                            pProductModel.getProductSubModelList().get( (tempX-1) ).setpDescription( pDescription );
                            pProductModel.getProductSubModelList().get( (tempX-1) ).setpDetails( pDetails );
                            pProductModel.getProductSubModelList().get( (tempX-1) ).setpGuideInfo( pGuide ); // p_guide_
                            pProductModel.getProductSubModelList().get( (tempX-1) ).setpSpecificationList( specificationModelList );

                        }

                        // Set Product Data...
                        setProductData( 0 );
                        // Refresh Our other Details...
                        setOtherDetails();

                        dialog.dismiss();
                    }
                    else{
                        String error = task.getException().getMessage();
                        showToast(error);
                        dialog.dismiss();
                    }
                }
            } );
        }else{
            dialog.dismiss();
        }

    }

    // --- Set Product Data ---------
    private void setProductData(int variantIndex){
        // Set ImageLayout Data
        productImageList.clear();
        ProductSubModel productSubModel =  pProductModel.getProductSubModelList().get( variantIndex );
        productImageList.addAll( productSubModel.getpImage() );
        productName.setText( productSubModel.getpName() );
        productPrice.setText( "Rs." + productSubModel.getpSellingPrice() + "/-" );
        productCutPrice.setText( "Rs." + productSubModel.getpMrpPrice() + "/-" );
        if (Integer.parseInt( productSubModel.getpStocks() )>0){
            productStocksText.setText( "In stocks ("+ productSubModel.getpStocks() + ")");
            productStocksText.setTextColor( getResources().getColor( R.color.colorPrimary ) );
        }else{
            productStocksText.setText( "Out of Stocks" );
            productStocksText.setTextColor( getResources().getColor( R.color.colorRed ) );
        }

        // Add Images Variant...
        productDetailsImagesAdapter = new ProductDetailsImagesAdapter( productImageList );
        productImagesViewPager.setAdapter( productDetailsImagesAdapter );
        productDetailsImagesAdapter.notifyDataSetChanged();
        // Set Details | Descriptions | Specifications //
        if (productFeaturesModelList.size() > 0){
            setFeaturesLayout(variantIndex);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setVegNonData(){
        // Set veg Non Image...
        if ( pProductModel.getpVegNonType() == PRODUCT_LACTO_VEG){
            pVegNonTypeImage.setImageTintList( this.getColorStateList( R.color.colorGreen )  );
            pVegNonTypeImage.setBackgroundTintList( this.getColorStateList(  R.color.colorGreen ) );
        }else if( pProductModel.getpVegNonType() == PRODUCT_LACTO_NON_VEG){
            pVegNonTypeImage.setImageTintList( this.getColorStateList( R.color.colorRed )  );
            pVegNonTypeImage.setBackgroundTintList( this.getColorStateList(  R.color.colorRed ) );
        }else if( pProductModel.getpVegNonType() == PRODUCT_LACTO_EGG){
            pVegNonTypeImage.setImageTintList( this.getColorStateList( R.color.colorYellow )  );
            pVegNonTypeImage.setBackgroundTintList( this.getColorStateList(  R.color.colorYellow ) );
        }else if( pProductModel.getpVegNonType() == PRODUCT_OTHERS){
            pVegNonTypeImage.setVisibility( View.GONE );
        }
    }
    private void setWeightSpinner(){
        // Set city code Spinner
        ArrayAdapter <String> weightAdaptor = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item, productVariantList);
        weightAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdaptor);
        weightSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                currentVariant = position;
                setProductData( currentVariant );
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//                if (cityList.size() == 1 || ! isUploadImages)
//                    showToast( "Upload Images first..!" );
            }
        } );
    }
    // Set Details | Descriptions | Specifications //
    private void setFeaturesLayout(int variantIndex){
        // Details...
        if (productFeaturesModelList.get( variantIndex ).getProductDetails()!= null
                && productFeaturesModelList.get( variantIndex ).getProductDetails().trim().length() > 8){
            detailsLayout.setVisibility( View.VISIBLE );
            detailsTextView.setText( productFeaturesModelList.get( variantIndex ).getProductDetails() );
        }else{
            detailsLayout.setVisibility( View.GONE );
        }
        // Description...
        if (productFeaturesModelList.get( variantIndex ).getProductDescription()!= null
                &&  productFeaturesModelList.get( variantIndex ).getProductDescription().trim().length() > 8 ){
            descriptionLayout.setVisibility( View.VISIBLE );
            descriptionTextView.setText( productFeaturesModelList.get( variantIndex ).getProductDescription() );
        }else{
            descriptionLayout.setVisibility( View.GONE );
        }
        // Specification...
        if (productFeaturesModelList.get( variantIndex ).getSpecificationModelList()!= null
                && productFeaturesModelList.get( variantIndex ).getSpecificationModelList().size()!=0){
            specificationLayout.setVisibility( View.VISIBLE );
            // set Specifications... layout
            LinearLayoutManager layoutManager = new LinearLayoutManager( this );
            layoutManager.setOrientation( RecyclerView.VERTICAL );
            specificationRecycler.setLayoutManager( layoutManager );
            // Adaptor...
            ProductSpecificationAdaptor specificationAdaptor = new ProductSpecificationAdaptor( productFeaturesModelList.get( variantIndex ).getSpecificationModelList() );
            specificationRecycler.setAdapter( specificationAdaptor );
            specificationAdaptor.notifyDataSetChanged();
        }else{
            specificationLayout.setVisibility( View.GONE );
        }

        // Guide...
        if (pProductModel.getProductSubModelList().get( variantIndex ).getpGuideInfo()!= null
                && pProductModel.getProductSubModelList().get( variantIndex ).getpGuideInfo().trim().length() > 6){
            guideLayout.setVisibility( View.VISIBLE );
            guideTextView.setText( productFeaturesModelList.get( variantIndex ).getProductDetails() );
        }else{
            guideLayout.setVisibility( View.GONE );
        }

    }
    // ---------------------------------- Update Product -------------------------------------------
    // Update COD : ON / OFF
    private void updateCOD( ){
        AlertDialog.Builder builder;
        if (pProductModel.getpIsCOD()){
            builder = DialogsClass.alertDialog( this, "COD Disable.?", "Do You want to Disable Cash On Delivery On this Product?" );
        }else{
            builder = DialogsClass.alertDialog( this, "COD Enable.?", "Do You want to Enable Cash On Delivery On this Product?" );
        }
//        AlertDialog.Builder builder = DialogsClass.alertDialog( this, dTitle, dBody );
        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show Dialog...
                dialog.show();
                //Check Condition to create Map
                Map<String, Object> updateMap = new HashMap <>();

                if (pProductModel.getpIsCOD()){
                    updateMap.put( "p_is_cod", false );

                    pProductModel.setpIsCOD(false);
                    productCODText.setText( "COD Not Available" );
                    productCODText.setTextColor( getResources().getColor( R.color.colorRed) );
                    // Stocks Update in local List...
                    if (crrShopCatIndex != -1 && layoutIndex != -1 && productIndex != -1 ){
                        homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex )
                                .setpIsCOD( false );
                    }
                }
                else{
                    updateMap.put( "p_is_cod", true );

                    pProductModel.setpIsCOD(true);
                    productCODText.setText( "Cash On Delivery Available" );
                    productCODText.setTextColor( getResources().getColor( R.color.colorBlack) );
                    // Stocks Update in local List...
                    if (crrShopCatIndex != -1 && layoutIndex != -1 && productIndex != -1 ){
                        homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex )
                                .setpIsCOD( true );
                    }
                }
                // Update On Database....
                DBQuery.updateProductData(dialog, productID, updateMap);
                // Dismiss Alert Dialog...
                dialogInterface.dismiss();
            }
        } );
        builder.setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );
        builder.show();
    }
    // update Dialog...
    private void updateDialog(int requestType){
        updateProductLayout.setVisibility( View.VISIBLE );
        // Disable Back Pressed...
        backPressedUpdate(false);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        updateFrameLayout.removeAllViews();

        switch (requestType){
            case UPDATE_IMAGES:
                fragmentTransaction.replace( updateFrameLayout.getId(),
                        new UpdateImage_SpFragment( this, currentVariant, productID, UPDATE_IMAGES,
                                pProductModel.getProductSubModelList().get( currentVariant ).getpImage(), null) );
                break;
            case UPDATE_SPECIFICATION:
                fragmentTransaction.replace( updateFrameLayout.getId(),
                        new UpdateImage_SpFragment( this, currentVariant, productID, UPDATE_SPECIFICATION,
                                null, productFeaturesModelList.get( currentVariant ).getSpecificationModelList()
                        ) );

                //  pProductModel.getProductSubModelList().get( currentVariant ).getpSpecificationList()
                break;
            case UPDATE_STOCKS:
            case UPDATE_PRICE:
            case UPDATE_WEIGHT:
                fragmentTransaction.replace( updateFrameLayout.getId(),
                        new UpdateProductFragment( this, new UpdateDataRequest(), requestType, currentVariant, productID, null ) );
                break;
            case UPDATE_NAME:
                fragmentTransaction.replace( updateFrameLayout.getId(),
                        new UpdateProductFragment( this, new UpdateDataRequest(), requestType, currentVariant, productID,
                                pProductModel.getProductSubModelList().get( currentVariant ).getpName() ) );
                break;
            case UPDATE_DETAILS:
                fragmentTransaction.replace( updateFrameLayout.getId(),
                        new UpdateProductFragment( this, new UpdateDataRequest(), requestType, currentVariant, productID,
                                productFeaturesModelList.get( currentVariant ).getProductDetails() ) );
                break;
            case UPDATE_DESCRIPTION:
                fragmentTransaction.replace( updateFrameLayout.getId(),
                        new UpdateProductFragment( this, new UpdateDataRequest(), requestType, currentVariant, productID,
                                productFeaturesModelList.get( currentVariant ).getProductDescription() ) );
                break;
            case UPDATE_GUIDE_INFO:
                fragmentTransaction.replace( updateFrameLayout.getId(),
                        new UpdateProductFragment( this, new UpdateDataRequest(), requestType, currentVariant, productID,
                                pProductModel.getProductSubModelList().get( currentVariant ).getpGuideInfo() ));
                break;
            default:
                break;
        }

//        fragmentTransaction.notify();
        fragmentTransaction.commit();

    }

    private void closeUpdateProductDialog(){
        updateProductLayout.setVisibility( View.GONE );
        backPressedUpdate(true);
    }

    @Override
    public void onUpdateCompleted(int requestType, boolean isSuccess) {

        if (isSuccess){
            switch (requestType){
                // TODO : In Local List...
                case UPDATE_WEIGHT:
                case UPDATE_PRICE:
                    break;
                case UPDATE_IMAGES:
                    ProductDetails.pProductModel.getProductSubModelList().get( currentVariant ).setpImage( AddNewImageAdaptor.uploadImageDataModelList );
                    AddNewImageAdaptor.uploadImageDataModelList = null;// To Release memory..
                    break;
                case UPDATE_SPECIFICATION:
                    List<Object> tempObjList = getSpecificationObjectList();
                    ProductDetails.pProductModel.getProductSubModelList().get( currentVariant ).setpSpecificationList( tempObjList );
                    productFeaturesModelList.get( currentVariant ).setSpecificationModelList( tempObjList );
                    break;
                case UPDATE_DETAILS:
                    productFeaturesModelList.get( currentVariant ).setProductDetails(
                            ProductDetails.pProductModel.getProductSubModelList().get( currentVariant ).getpDetails() );
//                    setFeaturesLayout( currentVariant ); //already Called in setProductData()
                    break;
                case UPDATE_DESCRIPTION:
                    productFeaturesModelList.get( currentVariant ).setProductDescription(
                            ProductDetails.pProductModel.getProductSubModelList().get( currentVariant ).getpDescription() );
//                    setFeaturesLayout( currentVariant ); // already Called in setProductData()
                    break;
                default:
                    break;
            }
            // Reset Data...
            setProductData(currentVariant);

            // update Weight...
            if (requestType == UPDATE_IMAGES){
                setOtherDetails();
            }

        }
        // Update in local...
        closeUpdateProductDialog();
    }

    @Override
    public void showToastMessage(String msg) {
        showToast( msg );
    }

    private List<Object> getSpecificationObjectList(){
        List<Object> specificationList = new ArrayList <>();
        for ( AddSpecificationModel addSpecificationModel : UpdateImage_SpFragment.specificationModelList ){
            HashMap <String, Object> specificationMap = new HashMap <>();
            // Add Features For One Model...
            List<Object> featureObjectList = new ArrayList <>();
            for ( AddSpecificationFeatureModel featureModel : addSpecificationModel.getSpecificationFeatureModelList() ){
                HashMap <String, Object> featureMap = new HashMap <>();
                featureMap.put( "featureName", featureModel.getFeatureName() );
                featureMap.put( "featureDetails", featureModel.getFeatureDetails() );
                featureObjectList.add( featureMap );
            }

            // Add Data into Model Map..
            specificationMap.put( "spHeading", addSpecificationModel.getSpHeading() );
            specificationMap.put( "specificationFeatureModelList", featureObjectList );

            specificationList.add( specificationMap );
        }
        // UpdateImage_SpFragment.specificationModelList = null; // To Release memory..
        return specificationList;
    }

//------------------- Not Usable.. --------------------------
    /**
    // Update : Stocks, Name, Details...
    private void updateDataDialog( final int updateCode, String title, String text, String hint ){
        final int variant = currentVariant + 1;
        final Dialog uDialog = new Dialog( this );
        uDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        uDialog.setContentView( R.layout.dialog_single_edit_text );
        uDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        uDialog.setCancelable( false );
        Button okBtn = uDialog.findViewById( R.id.dialog_ok_btn );
        Button cancelBtn = uDialog.findViewById( R.id.dialog_cancel_btn );
        TextView titleText = uDialog.findViewById( R.id.dialog_title );
        final EditText getText = uDialog.findViewById( R.id.dialog_editText );
        // Set Dialog Body...
        okBtn.setText( "UPDATE" );
        titleText.setText( title );
        getText.setText( text );
        getText.setHint( hint );
        if (updateCode == UPDATE_DETAILS || updateCode == UPDATE_DESCRIPTION){
            getText.setLines( 3 );
            getText.setMaxLines( 8 );
            getText.setSingleLine( false );
            getText.setHorizontalScrollBarEnabled( true );
        }else{
            getText.setMaxLines( 1 );
            getText.setSingleLine( true );
        }
        if (updateCode == UPDATE_STOCKS ){
            getText.setInputType( InputType.TYPE_CLASS_NUMBER );
        }else{
            getText.setInputType( InputType.TYPE_CLASS_TEXT );
        }
        // show the Dialog..
        uDialog.show();
        // action Button...
        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uDialog.dismiss();
            }
        } );

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty( getText.getText().toString() )){
                    // Show Dialog...
                    dialog.show();
                    //Check Condition to create Map
                    Map<String, Object> updateMap = new HashMap <>();

                    String updateData =  getText.getText().toString();
                    switch (updateCode){
                        case UPDATE_STOCKS:
                            updateMap.put( "p_stocks_"+variant, String.valueOf( updateData ) );
                            productStocksText.setText( "In stocks ("+ updateData + ")");
                            productStocksText.setTextColor( getResources().getColor( R.color.colorPrimary ) );
                            // Stocks Update in local List...
                            if (crrShopCatIndex != -1 && layoutIndex != -1 && productIndex != -1 ){
                                homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex )
                                        .getProductSubModelList().get( currentVariant ).setpStocks( updateData );
                            }
                            break;
                        case UPDATE_NAME:
                            updateMap.put( "p_name_"+variant, updateData );
                            productName.setText( updateData );
                            // Name Update in local List...
                            if (crrShopCatIndex != -1 && layoutIndex != -1 && productIndex != -1 ){
                                homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex )
                                        .getProductSubModelList().get( currentVariant ).setpName( updateData );
                            }
                            break;
//                        case UPDATE_DETAILS:
//                            updateMap.put( "product_details", updateData );
//                            // Details Update in local List...
//                            if (crrShopCatIndex != -1 && layoutIndex != -1 && productIndex != -1 ){
//                                homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex )
//                                        .setpDetails( updateData );
//                            }
//                            break;
//                        case UPDATE_DESCRIPTION:
//                            updateMap.put( "p_description_"+variant, updateData );
//                            // TODO : Update in local///
//                            break;
                        default:
                            break;
                    }

                    // Update On Database....
                    DBQuery.updateProductData(dialog, productID, updateMap);
                    // Dismiss the update Dialog...
                    uDialog.dismiss();
                }else{
                    getText.setError( "Required!" );
                }

            }
        } );

    }

     */



}
