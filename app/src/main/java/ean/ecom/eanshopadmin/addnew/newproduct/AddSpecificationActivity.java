package ean.ecom.eanshopadmin.addnew.newproduct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.home.HomeFragment;

import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;
import static ean.ecom.eanshopadmin.product.productview.ProductDetails.pProductModel;

public class AddSpecificationActivity extends AppCompatActivity {

    public static List <AddSpecificationModel> specificationModelList;
    public static AddSpecificationAdaptor specificationAdaptor;

    private EditText descriptionEditText;
    private RecyclerView specificationRecycler;
    private Button uploadBtn;

    private ImageView homeBackBtn;
    private TextView productIDText;
    private TextView productVerText;
    private Spinner proCopyFromSpinner;

    //
    private String productID;
    private int productVerNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_specification );

//        productID = getIntent().getStringExtra( "PRODUCT_ID" );
        productVerNo = getIntent().getIntExtra( "PRODUCT_VER_NO", -1 );
        // Get Product ID...
        productID = pProductModel.getpProductID();
        specificationModelList = pProductModel.getProductSubModelList().get( productVerNo ).getpSpecificationList();

        descriptionEditText = findViewById( R.id.new_pro_description_etext );
        specificationRecycler = findViewById( R.id.new_pro_specifications_recycler );
        uploadBtn = findViewById( R.id.upload_specification_btn );

        // header....
        homeBackBtn = findViewById( R.id.home_back_imageview );
        productIDText = findViewById( R.id.pro_id_text );
        productVerText = findViewById( R.id.pro_ver_no_text );
        proCopyFromSpinner = findViewById( R.id.copy_from_spinner );

        /// Set Layout ...
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation( RecyclerView.VERTICAL );
        specificationRecycler.setLayoutManager( linearLayoutManager );
        // Adaptor...
        specificationAdaptor = new AddSpecificationAdaptor( specificationModelList );
        specificationRecycler.setAdapter( specificationAdaptor );
        specificationAdaptor.notifyDataSetChanged();

        // set Header...
        productIDText.setText( "Product ID : "+ productID );
        productVerText.setText( "Ver : " + productVerNo );

        // Upload Button...
        uploadBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty( descriptionEditText.getText().toString() )){
                    showToast(AddSpecificationActivity.this, "Please Fill Required Fields!");
                    descriptionEditText.setError( "Required!" );
                }
                else{
                    if (isListNotEmpty()){
                        // TODO: Upload Data...
                        showToast(AddSpecificationActivity.this, "Code Not Set Yet!");
                    }
                }
            }
        } );

        homeBackBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        } );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean isListNotEmpty(){

        if (specificationModelList.size() == 0){
            showToast(AddSpecificationActivity.this, "Please Add Features!");
            return false;
        }else{
            if (specificationModelList.get( 0 ).getSpHeading() == null){
                showToast(AddSpecificationActivity.this, "Please Enter Feateres title!");
                return false;
            }
            if (specificationModelList.get( 0 ).getSpecificationFeatureModelList().size() == 0){
                showToast(AddSpecificationActivity.this, "Please Add Some Features!");
                return false;
            }
            return true;
        }
    }

    private void updateSpecification(){

        String categoryID = "categoryID";
        String productID = "productID";

        Map <String, Object> updateMap = new HashMap <>();
//        int pListIndex = homeCatListModelList.get( catIndex ).getHomeListModelList().get( layIndex ).getProductIdList().size() + 1;
//        updateMap.put( "no_of_products", pListIndex  );
//        updateMap.put( "product_id_"+pListIndex, productID  );
        updateMap.put( "visibility", true  );
        updateMap.put( "specifications", specificationModelList );

        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( "PRODUCTS" ).document( productID )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
//                            dialog.dismiss();
//                            showToast( "Added Successfully..!" );
                            HomeFragment.homePageAdaptor.notifyDataSetChanged();
                            finish();
                        }
                        else{
//                            dialog.dismiss();
//                            showToast( "Product Added..! Error : " + task.getException().getMessage() );
                            finish();
                        }
                    }
                } );

    }


}
