package ean.ecom.eanshopadmin.home.viewall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.product.ProductModel;
import ean.ecom.eanshopadmin.product.ProductSubModel;
import ean.ecom.eanshopadmin.product.horizontal.ProductHrGridAdaptor;

import static ean.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;
import static ean.ecom.eanshopadmin.other.StaticValues.VIEW_GRID_LAYOUT;
import static ean.ecom.eanshopadmin.other.StaticValues.VIEW_RECTANGLE_LAYOUT;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerProductLayout;
    private GridView gridProductLayout;


    private int crrShopCatIndex;
    private int layoutIndex;
    private int viewType;
    public static List <ProductModel> productModelList;
    public static List <String> totalProductsIdViewAll;

    // Adaptor...
    private  GridViewAllAdaptor gridViewAllAdaptor;
    private  ProductHrGridAdaptor horizontalViewAllAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_all );

        // Intent Value...
        crrShopCatIndex = getIntent().getIntExtra( "CAT_INDEX", -1 );
        layoutIndex = getIntent().getIntExtra( "LAY_INDEX", -1 );
        viewType = getIntent().getIntExtra( "VIEW_TYPE", -1 );

        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            String layoutTitle = getIntent().getStringExtra( "TITLE" );
            getSupportActionBar().setTitle( layoutTitle );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

        totalProductsIdViewAll = homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductIdList();
        productModelList = homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList();
        // -- Recycler View...
        recyclerProductLayout = findViewById( R.id.view_all_layout_recycler );
        // ----------- Grid View....
        gridProductLayout = findViewById( R.id.view_all_layout_grid );

        //-- condition..
        if (viewType == VIEW_RECTANGLE_LAYOUT){
            gridProductLayout.setVisibility( View.GONE );
            recyclerProductLayout.setVisibility( View.VISIBLE );
            setRecyclerProductLayout();
            if (totalProductsIdViewAll.size() > productModelList.size()){
                // Load Again Data...
                productModelList.clear();
                homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().clear();
                for (int i = 0; i < totalProductsIdViewAll.size(); i++){
                    loadGridProductData( totalProductsIdViewAll.get( i ));
                }
            }
        }else
        if (viewType == VIEW_GRID_LAYOUT ){
            recyclerProductLayout.setVisibility( View.GONE );
            gridProductLayout.setVisibility( View.VISIBLE );
            setGridProductLayout();
            if (totalProductsIdViewAll.size() > productModelList.size()){
                // Load Again Data...
                productModelList.clear();
                homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().clear();
                for (int i = 0; i < totalProductsIdViewAll.size(); i++){
                    loadGridProductData( totalProductsIdViewAll.get( i ));
                }
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return true;
    }

    private void setGridProductLayout() {
        gridViewAllAdaptor = new GridViewAllAdaptor( crrShopCatIndex, layoutIndex, viewType, productModelList );
        gridProductLayout.setAdapter( gridViewAllAdaptor );
        gridViewAllAdaptor.notifyDataSetChanged();
    }

    private void setRecyclerProductLayout(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerProductLayout.setLayoutManager( linearLayoutManager );
        horizontalViewAllAdaptor = new ProductHrGridAdaptor( crrShopCatIndex, layoutIndex, viewType, productModelList );
        recyclerProductLayout.setAdapter( horizontalViewAllAdaptor );
        horizontalViewAllAdaptor.notifyDataSetChanged();
    }

    // Load Product Data...
    private void loadGridProductData( final String productId ){
        DBQuery.firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .collection( "PRODUCTS" ).document( productId )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    // access the banners from database...
                    DocumentSnapshot documentSnapshot = task.getResult();

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

                        ProductModel productModel =  new ProductModel(
                                p_id,
                                p_main_name,
                                " ",
                                p_is_cod,
                                String.valueOf(p_no_of_variants),
                                p_weight_type,
                                p_veg_non_type,
                                productSubModelList
                        );

//                        productModelList.add( productModel );
                        homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().add( productModel );

//                        homeCatListModelList.get( catIndex ).getHomeListModelList().get( index ).getProductModelList().add( productModel );

                        if (viewType == VIEW_RECTANGLE_LAYOUT ){
                            if (horizontalViewAllAdaptor!= null)
                                horizontalViewAllAdaptor.notifyDataSetChanged();
                        }else{
                            if (gridViewAllAdaptor!= null)
                                gridViewAllAdaptor.notifyDataSetChanged();
                        }

                    }
                }
                else{
                    String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                }
            }
        } );

    }


}
