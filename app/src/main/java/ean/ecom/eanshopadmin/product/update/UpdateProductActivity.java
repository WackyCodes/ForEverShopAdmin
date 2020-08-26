package ean.ecom.eanshopadmin.product.update;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.database.DBQuery;

import static ean.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class UpdateProductActivity extends AppCompatActivity {

    public static final int UPDATE_PRICE = 1;
    public static final int UPDATE_IMAGES = 2;
    public static final int UPDATE_WEIGHTS = 3;
    public static final int UPDATE_SPECIFICATIONS = 4;
    private int updateCode;
    private int listVariant;
    private String productID;

    private int crrShopCatIndex, layoutIndex , productIndex;

    private Button updateBtn;
    // Update Weight ...
    private EditText uWeight;
    private Spinner uWeightSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_update_product );

        productID = getIntent().getStringExtra( "PRODUCT_ID" );
        crrShopCatIndex = getIntent().getIntExtra( "HOME_CAT_INDEX", -1 );
        layoutIndex = getIntent().getIntExtra( "LAYOUT_INDEX", -1 );
        productIndex = getIntent().getIntExtra( "PRODUCT_INDEX", -1 );

        listVariant = getIntent().getIntExtra( "VARIANT_CODE", -1 );
        updateCode = getIntent().getIntExtra( "UPDATE_CODE", -1 );

        // Set Title on Action Menu...
        Toolbar toolbar = findViewById( R.id.x_ToolBar );
        setSupportActionBar( toolbar );
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( "Update Product" );

        }catch (NullPointerException e){ }

        // initialize...
        updateBtn = findViewById( R.id.update_button );

        // weight...
        uWeight = findViewById( R.id.update_weight );
        uWeightSpinner = findViewById( R.id.update_weight_spinner );

        // setuWeightSpinner...
        setuWeightSpinner();

        updateBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (updateCode){
                    case UPDATE_WEIGHTS:
                        if (isNotEmpty(uWeight) && weightType!=null){
                            setUpdateWeights();
                        }else {
                            if (weightType == null){
                                showToast( "Please Select weight type!");
                            }
                        }
                        break;
                    case UPDATE_PRICE:
                    case UPDATE_IMAGES:
                    case UPDATE_SPECIFICATIONS:
                        break;
                }
            }
        } );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else
            return super.onOptionsItemSelected( item );
    }

    // Weight... Update...
    private String weightType = null;
    private void setuWeightSpinner(){
        // Qty Type Text Adopter...
        ArrayAdapter <String> qtyTypeList = new ArrayAdapter<String>( this,
                android.R.layout.simple_spinner_item, getResources().getStringArray( R.array.qty_auto_text_list ) );
        uWeightSpinner.setAdapter( qtyTypeList );
        uWeightSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if (position > 0){
                    weightType = parent.getItemAtPosition( position ).toString();
                    if (position == 8){
                        uWeight.setText( "NONE" );
                        weightType = "NONE";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                weightType = null;
            }
        } );

    }


    private void setUpdateWeights(){
        Map<String, Object> updateMap = new HashMap <>();
        updateMap.put( "p_weight_"+(listVariant+1), uWeight.getText().toString() + "-" +weightType );
        DBQuery.updateProductData(null, productID, updateMap);

        showToast( "Update Successfully!" );
        if (crrShopCatIndex != -1 && layoutIndex != -1 && productIndex != -1 ){
            homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex )
                    .getProductSubModelList().get( listVariant ).setpWeight( uWeight.getText().toString() + "-" +weightType );
        }
        // Finish this activity...
        finish();
    }


    private boolean isNotEmpty(EditText editText){
        if (TextUtils.isEmpty( editText.getText().toString() )){
            editText.setError( "Required!" );
            return false;
        }else{
            return true;
        }
    }
    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

}
