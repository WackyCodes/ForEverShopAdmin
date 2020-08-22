package ean.ecom.eanshopadmin.product.productview;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.addnew.newproduct.AddNewProductActivity;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.other.CheckInternetConnection;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.product.ProductModel;
import ean.ecom.eanshopadmin.product.ProductSubModel;
import ean.ecom.eanshopadmin.product.search.ProductSearchActivity;
import ean.ecom.eanshopadmin.product.specifications.ProductDetailsSpecificationModel;

import static ean.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_EGG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_NON_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_LACTO_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.PRODUCT_OTHERS;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

public class ProductDetails extends AppCompatActivity {
    public static AppCompatActivity productDetails;

    private ImageView pVegNonTypeImage; // product_veg_non_type_image
    private LinearLayout weightSpinnerLayout;// weight_spinner_layout
    private Spinner weightSpinner; //weight_spinner

    // --- Product Details Image Layout...
    private ViewPager productImagesViewPager;
    private TabLayout productImagesIndicator;
    private ConstraintLayout productDescriptionLayout;
    private TextView productName;
    private TextView productPrice;
    private TextView productCutPrice;
    private TextView productCODText; // product_item_cod_text

    // create a list for testing...
    private List <String> productImageList = new ArrayList <>();
    private ProductDetailsImagesAdapter productDetailsImagesAdapter;

    private List<String> productVariantList = new ArrayList <>();

    // --- Product Details Image Layout...
    private TextView productDetailsText;

    private ViewPager productDescriptionViewPager;
    private TabLayout productDescriptionIndicator;

    public static String productID;
    public static TextView badgeCartCount;

    // Dialogs...
    private Dialog dialog;

    // Product Specification ...
    private List <ProductDetailsSpecificationModel> productDetailsSpecificationModelList = new ArrayList <>();
    private String productDescription;

    private int crrShopCatIndex;
    private int layoutIndex;
    private int productIndex;

    private int currentVariant = 0;

    public static ProductModel pProductModel = null;
    private Boolean isUpdateAllowed = true;

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
//                productID = "k2SGQbneH477j6X18l6a";
                dialog.dismiss();
                Toast.makeText( this, "Product Not found.!", Toast.LENGTH_SHORT ).show();
                finish();
            }
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( productID );
        }catch (NullPointerException e){
        }

        // --- Product Details Image Layout...
        productName = findViewById( R.id.product_item_name );
        productPrice = findViewById( R.id.product_item_price );
        productCutPrice = findViewById( R.id.product_item_cut_price );
        productCODText = findViewById( R.id.product_item_cod_text );

        // --- Product Details Image Layout...
        productDescriptionLayout = findViewById( R.id.product_details_description_ConstLayout );
        productDetailsText = findViewById( R.id.product_details_text );

        pVegNonTypeImage = findViewById( R.id.product_veg_non_type_image );
        weightSpinnerLayout = findViewById( R.id.weight_spinner_layout );
        weightSpinner = findViewById( R.id.weight_spinner );

        //----------- Product Images ---
        productImagesViewPager = findViewById( R.id.product_images_viewpager );
        productImagesIndicator = findViewById( R.id.product_images_viewpager_indicator );

        // ---------- Product Description code----
        productDescriptionViewPager = findViewById( R.id.product_detail_viewpager );
        productDescriptionIndicator = findViewById( R.id.product_details_indicator );
        // Default Tab Layout Invisible
        productDescriptionLayout.setVisibility( View.GONE );

        // set adapter with viewpager...
        productDetailsImagesAdapter = new ProductDetailsImagesAdapter( productImageList );
        productImagesViewPager.setAdapter( productDetailsImagesAdapter );
        // connect TabLayout with viewPager...
        productImagesIndicator.setupWithViewPager( productImagesViewPager, true );

        //----------- Product Description ---
        productDescriptionViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener( productDescriptionIndicator ) );
        productDescriptionIndicator.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDescriptionViewPager.setCurrentItem( tab.getPosition() );
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );
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

    private void setOtherDetails(){
        // set Product VegNon Veg...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setVegNonData();
        }
        if (pProductModel.getpIsCOD()){
            productCODText.setVisibility( View.VISIBLE );
            productCODText.setText( "Cash On Delivery Available" );
        }else{
            productCODText.setVisibility( View.GONE );
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
        if(productVariantList.size() > 0){
            setWeightSpinner();
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
            startActivity( addProduct );
            return true;
        }else
        if (id == R.id.menu_stock_update){

        }else
        if (id == R.id.menu_price_update){

        }else
        if (id == R.id.menu_cod_update){

        }else
        if (id == R.id.menu_edit_name){

        }else
        if (id == R.id.menu_edit_details){

        }else
        if (id == R.id.menu_update_images){

        }else
        if (id == R.id.menu_update_specifications){

        }else
        if (id == R.id.menu_remove_products){

        }
//        else
//        if (id == R.id.menu_update_images){
//
//        }


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


}
