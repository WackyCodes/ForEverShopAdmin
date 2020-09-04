package ean.ecom.eanshopadmin.product.productview;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
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
import ean.ecom.eanshopadmin.product.update.specification.AddSpecificationModel;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.other.CheckInternetConnection;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.product.ProductModel;
import ean.ecom.eanshopadmin.product.ProductSubModel;
import ean.ecom.eanshopadmin.product.ProductViewInterface;
import ean.ecom.eanshopadmin.product.search.ProductSearchActivity;
import ean.ecom.eanshopadmin.product.specifications.ProductDetailsSpecificationModel;
import ean.ecom.eanshopadmin.product.update.UpdateDataRequest;
import ean.ecom.eanshopadmin.product.update.UpdateProductFragment;
import ean.ecom.eanshopadmin.product.update.specification.UpdateImage_SpFragment;

import static ean.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_EGG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_NON_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_OTHERS;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_IMAGES;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_PRICE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SPECIFICATION;
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
    private TextView productDetailsText;
//    private ConstraintLayout productDescriptionLayout;

//    private ViewPager productDescriptionViewPager;
//    private TabLayout productDescriptionIndicator;
    private LinearLayout specificationLayout;
    private RecyclerView specificationRecycler;

    public static String productID;
    public static TextView badgeCartCount;

    // Dialogs...
    private Dialog dialog;
    private boolean isAllowBack = true;

    // Product Specification ...
    private List <ProductDetailsSpecificationModel> productDetailsSpecificationModelList = new ArrayList <>();
    private String productDescription;

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
        }catch (NullPointerException e){
        }

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
//        productDescriptionLayout = findViewById( R.id.product_details_description_ConstLayout );
        productDetailsText = findViewById( R.id.product_details_text );

        pVegNonTypeImage = findViewById( R.id.product_veg_non_type_image );
        weightSpinnerLayout = findViewById( R.id.weight_spinner_layout );
        weightSpinner = findViewById( R.id.weight_spinner );
        weightText = findViewById( R.id.weight_text );

        //----------- Product Images ---
        productImagesViewPager = findViewById( R.id.product_images_viewpager );
        productImagesIndicator = findViewById( R.id.product_images_viewpager_indicator );

        // ---------- Product Description code----
//        productDescriptionViewPager = findViewById( R.id.product_detail_viewpager );
//        productDescriptionIndicator = findViewById( R.id.product_details_indicator );
        specificationLayout = findViewById( R.id.specification_layout );
        specificationRecycler = findViewById( R.id.specification_recycler );

        // Default Tab Layout Invisible
//        productDescriptionLayout.setVisibility( View.GONE );

        // set adapter with viewpager...
        productDetailsImagesAdapter = new ProductDetailsImagesAdapter( productImageList );
        productImagesViewPager.setAdapter( productDetailsImagesAdapter );
        // connect TabLayout with viewPager...
        productImagesIndicator.setupWithViewPager( productImagesViewPager, true );

        //----------- Product Description ---
//        productDescriptionViewPager.addOnPageChangeListener(
//                new TabLayout.TabLayoutOnPageChangeListener( productDescriptionIndicator ) );
//        productDescriptionIndicator.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                productDescriptionViewPager.setCurrentItem( tab.getPosition() );
//            }
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        } );
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
            addProduct.putExtra( "PRO_INDEX", productIndex + 1 );
            startActivity( addProduct );
            return true;
        }else
        if (id == R.id.menu_stock_update){
            updateDataDialog( UPDATE_STOCKS, "Update Stocks", "Enter Stocks" );
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
            updateDataDialog( UPDATE_NAME, "Update Name", "Enter Product Name" );
            return true;
        }else
        if (id == R.id.menu_edit_details){
            updateDataDialog( UPDATE_DETAILS, "Update Details", "Enter Products Details" );
            return true;
        }else
        if (id == R.id.menu_edit_description){
            updateDataDialog( UPDATE_DESCRIPTION, "Update Description", "Enter Products Description" );
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
        if (id == R.id.menu_remove_products){
            return true;
        }
        else
        if (id == R.id.menu_update_weight){
            updateDialog( UPDATE_WEIGHT );

//            Intent updateActivity = new Intent( this, UpdateProductActivity.class );
//            updateActivity.putExtra( "PRODUCT_ID", productID );
//            updateActivity.putExtra( "HOME_CAT_INDEX", crrShopCatIndex );
//            updateActivity.putExtra( "LAYOUT_INDEX", layoutIndex );
//            updateActivity.putExtra( "PRODUCT_INDEX", productIndex );
//            updateActivity.putExtra( "VARIANT_CODE", currentVariant );
//            updateActivity.putExtra( "UPDATE_CODE", UPDATE_WEIGHTS );

//            startActivity( updateActivity );

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

                        if (!isUpdateAllowed){
                            /** Reload Model Data...*/
                            if ( documentSnapshot.get( "p_no_of_variants" ) !=null ){
//                            String[] pImage;
                                int p_no_of_variants = Integer.valueOf( String.valueOf( (long) documentSnapshot.get( "p_no_of_variants" ) ) );
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

                                // Set Product Data...
                                setProductData( 0 );
                                // Refresh Our other Details...
                                setOtherDetails();

                            }
                        }
                        // Set other Details of  Product Details Image Layout...
                      /**  if ((boolean)documentSnapshot.get( "use_tab_layout" )){
                            // use tab layout...
                            productDescriptionLayout.setVisibility( View.VISIBLE );
                            // TODO : set Description data..
                            productDescription = documentSnapshot.get( "product_description" ).toString() ;
                            // TODO : set Specification data...
                            for (long x = 1; x < (long) documentSnapshot.get( "pro_sp_head_num" )+1; x++){
                                productDetailsSpecificationModelList.add( new ProductDetailsSpecificationModel( 0,
                                        documentSnapshot.get( "pro_sp_head_" + x ).toString() ) );
                                for (long i = 1; i < (long)documentSnapshot.get( "pro_sp_sub_head_"+x+"_num" )+1; i++){
                                    productDetailsSpecificationModelList.add( new ProductDetailsSpecificationModel( 1,
                                            documentSnapshot.get( "pro_sp_sub_head_" + x + i ).toString()
                                            , documentSnapshot.get( "pro_sp_sub_head_d_" + x + i ).toString() ) );
                                }
                            }

//                            ProductDetailsDescriptionAdaptor productDetailsDescriptionAdaptor
//                                    = new ProductDetailsDescriptionAdaptor( getSupportFragmentManager()
//                                    , productDescriptionIndicator.getTabCount()
//                                    , productDescription
//                                    , productDetailsSpecificationModelList  );
//                            productDescriptionViewPager.setAdapter( productDetailsDescriptionAdaptor );
//                            productDetailsDescriptionAdaptor.notifyDataSetChanged();

                        }
                        else{
                            // don't use tabLayout...
                            productDescriptionLayout.setVisibility( View.GONE );
                        } */
                        productDetailsText.setText( documentSnapshot.get( "product_details" ).toString() );
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

        if (productDetailsImagesAdapter!=null){
            productDetailsImagesAdapter.notifyDataSetChanged();
        }else{
            productDetailsImagesAdapter = new ProductDetailsImagesAdapter( productImageList );
            productImagesViewPager.setAdapter( productDetailsImagesAdapter );
            productDetailsImagesAdapter.notifyDataSetChanged();
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
    // Update : Stocks, Name, Details...
    private void updateDataDialog( final int updateCode, String title, String hint ){
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
        getText.setText( "" );
        getText.setHint( hint );
        if (updateCode == UPDATE_DETAILS || updateCode == UPDATE_DESCRIPTION){
            getText.setLines( 3 );
            getText.setMaxLines( 6 );
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
                        case UPDATE_DETAILS:
                            updateMap.put( "product_details", updateData );
                            productDetailsText.setText( updateData );
                            // Details Update in local List...
                            if (crrShopCatIndex != -1 && layoutIndex != -1 && productIndex != -1 ){
                                homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex )
                                        .setpDetails( updateData );
                            }
                            break;
                        case UPDATE_DESCRIPTION:
                            updateMap.put( "p_description_"+variant, updateData );
                            // TODO : Update in local///
                            break;
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

    private final int UPDATE_STOCKS = 11;
    private final int UPDATE_NAME = 12;
    private final int UPDATE_DETAILS = 13;
    private final int UPDATE_DESCRIPTION = 10;

    // update Dialog...
    private void updateDialog(int requestType){
        updateProductLayout.setVisibility( View.VISIBLE );
        // Disable Back Pressed...
        backPressedUpdate(false);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (requestType == UPDATE_IMAGES){
            updateFrameLayout.removeAllViews();
            fragmentTransaction.replace( updateFrameLayout.getId(),
                    new UpdateImage_SpFragment( this, currentVariant, productID, UPDATE_IMAGES,
                            pProductModel.getProductSubModelList().get( currentVariant ).getpImage(), null) );

        }else if(requestType == UPDATE_SPECIFICATION){
            updateFrameLayout.removeAllViews();
            if (pProductModel.getProductSubModelList().get( currentVariant ).getpSpecificationList() == null){
                pProductModel.getProductSubModelList().get( currentVariant ).setpSpecificationList( new ArrayList <AddSpecificationModel>() );
            }
            fragmentTransaction.replace( updateFrameLayout.getId(),
                    new UpdateImage_SpFragment( this, currentVariant, productID, UPDATE_SPECIFICATION,
                            null,
                            pProductModel.getProductSubModelList().get( currentVariant ).getpSpecificationList() ) );
        }else{
            updateFrameLayout.removeAllViews();
            fragmentTransaction.replace( updateFrameLayout.getId(),
                    new UpdateProductFragment( this, new UpdateDataRequest(), requestType, currentVariant, productID ) );
        }

//        fragmentTransaction.notify();
        fragmentTransaction.commit(); //TODO : Fix this Error

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
                    ProductDetails.pProductModel.getProductSubModelList().get( currentVariant ).setpSpecificationList( UpdateImage_SpFragment.specificationModelList );
                    UpdateImage_SpFragment.specificationModelList = null; // To Release memory..
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

}
