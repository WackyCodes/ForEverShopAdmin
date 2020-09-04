package ean.ecom.eanshopadmin.product.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import java.util.HashMap;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.StaticMethods;
import ean.ecom.eanshopadmin.product.ProductViewInterface;
import ean.ecom.eanshopadmin.product.productview.ProductDetails;

import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_PRICE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_WEIGHT;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class UpdateProductFragment extends Fragment implements UpdateData.UpdateRequest.OnUpdateFinisher {
    private final int MRP_CHANGED = 10;
    private final int SELLING_CHANGED = 11;

    private ProductViewInterface rootListener;
    private UpdateData.UpdateRequest updateRequest;
    private int requestType;
    private int listVariant;
    private String productID;

    public UpdateProductFragment(ProductViewInterface rootActivity, UpdateData.UpdateRequest updateRequest, int requestType
            ,int listVariant, String productID) {
        // Required empty public constructor
        rootListener = rootActivity;
        this.updateRequest = updateRequest;
        this.requestType = requestType;
        this.listVariant = listVariant;
        this.productID = productID;
    }
    // Dialog...
    private Dialog dialog;

    // ---------------               ----------
    private Button updateBtn;
    // Update Weight ...
    private LinearLayout updateWeightLayout;
    private EditText uWeight;
    private Spinner uWeightSpinner;
    // update price..
    private LinearLayout updatePriceLayout;
    private EditText uMrpEditText; // new_pro_mrp_rate
    private EditText uSellingEditText; // new_pro_selling_price
    private TextView uDiscountPerText; // new_pro_per_discount
    private TextView uDiscountRsText; // new_pro_rs_discount

    // Header...
    private ImageView homeBackBtn;
    private TextView productIDText;
    private TextView productVerText;
    private TextView updateTitle;

    // ---------------              ----------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_update_product, container, false );
        dialog = DialogsClass.getDialog( getContext() );
        // initialize...
        updateBtn = view.findViewById( R.id.update_button );

        // weight...
        updateWeightLayout = view.findViewById( R.id.update_weight_layout );
        uWeight = view.findViewById( R.id.update_weight );
        uWeightSpinner = view.findViewById( R.id.update_weight_spinner );
        // Price..
        updatePriceLayout = view.findViewById( R.id.update_price_layout );
        uMrpEditText = view.findViewById( R.id.new_pro_mrp_rate );
        uSellingEditText = view.findViewById( R.id.new_pro_selling_price );
        uDiscountPerText = view.findViewById( R.id.new_pro_per_discount );
        uDiscountRsText = view.findViewById( R.id.new_pro_rs_discount );
        // header....
        homeBackBtn = view.findViewById( R.id.home_back_imageview );
        productIDText = view.findViewById( R.id.pro_id_text );
        productVerText = view.findViewById( R.id.pro_ver_no_text );
        updateTitle = view.findViewById( R.id.update_title );

        // set Header...
        productIDText.setText( "Product ID : "+ productID );
        productVerText.setText( "Ver : " + (listVariant + 1) );

        // Set Layout Visibility...
        setLayoutVisibility();
        // Update Price Interaction...
        setUpdateMrpSelling();
        // Weight updates Interaction...
        setuWeightSpinner();

        updateBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (requestType){
                    case UPDATE_WEIGHT:
                        if (isNotEmpty( uWeight )){
                            updateWeight();
                        }
                    case UPDATE_PRICE:
                        if (isValidPriceData()){
                            updatePrice();
                        }
                        break;
                    default:
                        break;
                }
            }
        } );

        homeBackBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootListener.onUpdateCompleted( requestType, false );
            }
        } );

        return view;
    }
    // ---------------------------- User Interaction -------------------------------
    // Update Weight...
    // Weight... Update...
    private String weightType = null;
    private void setuWeightSpinner(){
        // Qty Type Text Adopter...
        ArrayAdapter <String> qtyTypeList = new ArrayAdapter<String>( getContext(),
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

    // Price Update...
    private void setUpdateMrpSelling(){
        uMrpEditText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( ! TextUtils.isEmpty( uMrpEditText.getText().toString().trim() ) && ! TextUtils.isEmpty( uSellingEditText.getText().toString().trim() )){
                    if (Integer.parseInt( uMrpEditText.getText().toString().trim() ) < Integer.parseInt( uSellingEditText.getText().toString().trim() )){
                        uMrpEditText.setError( "MRP can't be small from Selling Price..!" );
                    }else {
                        setAutoText( MRP_CHANGED, uMrpEditText, uSellingEditText, uDiscountPerText, uDiscountRsText );
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if ( ! TextUtils.isEmpty( uMrpEditText.getText().toString().trim() ) && ! TextUtils.isEmpty( uSellingEditText.getText().toString().trim() )){
                    if (Integer.parseInt( uMrpEditText.getText().toString().trim() ) < Integer.parseInt( uSellingEditText.getText().toString().trim() )){
                        uMrpEditText.setError( "MRP can't be small from Selling Price..!" );
                    }else {
                        setAutoText( MRP_CHANGED, uMrpEditText, uSellingEditText, uDiscountPerText, uDiscountRsText );
                    }
                }
            }
        } );
        uSellingEditText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if ( TextUtils.isEmpty( uMrpEditText.getText().toString().trim() )){
                    uMrpEditText.setError( "Enter MRP..!" );
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( ! TextUtils.isEmpty( uMrpEditText.getText().toString().trim() ) && ! TextUtils.isEmpty( uSellingEditText.getText().toString().trim() )){
                    if (Integer.parseInt( uMrpEditText.getText().toString().trim() ) < Integer.parseInt( uSellingEditText.getText().toString().trim() )){
                        uSellingEditText.setError( "Selling Price should be less or equal from MRP.!" );
                    }
                }else {
                    uMrpEditText.setError( "Enter MRP..!" );
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if ( ! TextUtils.isEmpty( uMrpEditText.getText().toString().trim() ) && ! TextUtils.isEmpty( uSellingEditText.getText().toString().trim() )){
                    if (Integer.parseInt( uMrpEditText.getText().toString().trim() ) < Integer.parseInt( uSellingEditText.getText().toString().trim() )){
                        uSellingEditText.setError( "Selling Price should be less or equal from MRP.!" );
                    }else{
                        // Code to set Value...
                        if (! TextUtils.isEmpty( uSellingEditText.getText().toString().trim() ))
                            setAutoText( SELLING_CHANGED, uMrpEditText, uSellingEditText, uDiscountPerText, uDiscountRsText );
                    }
                }else {
                    uMrpEditText.setError( "Enter MRP..!" );
                }
            }
        } );
    }
    private void setAutoText(int reqType, EditText mrpEdTxt, EditText sellingEdTxt, TextView offPerEdTxt, TextView offRsEdTxt){
        double mrpValue = Integer.parseInt( mrpEdTxt.getText().toString().trim() );
        double sellValue = Integer.parseInt( sellingEdTxt.getText().toString().trim() );

        int offRsValue;
        double offPer;

        // if MRP and Selling Entered by user...
        switch (reqType){
            case SELLING_CHANGED:
                if (isNotEmpty( mrpEdTxt )){
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
                if (isNotEmpty( sellingEdTxt )){
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
            default:
                break;
        }
    }
    private boolean isValidPriceData(){
        if (isNotEmpty( uMrpEditText ) && isNotEmpty( uSellingEditText )){
            if (Integer.parseInt( uMrpEditText.getText().toString().trim() ) < Integer.parseInt( uSellingEditText.getText().toString().trim() )){
                showToast( "MRP can't be small from Selling Price..!" );
                return false;
            }else{
                return true;
            }
        }else {
            return false;
        }
    }

    //------------------------------- Update Request -------------------------------
    // product Price Updates...
    private void updatePrice(){
        dialog.show();
        //  : Update...
        int temIndex = (listVariant+1);
        Map <String, Object> updateMap = new HashMap <>();
        updateMap.put( "p_selling_price_" + temIndex, uSellingEditText.getText().toString() );
        updateMap.put( "p_mrp_price_" + temIndex, uMrpEditText.getText().toString() );
        updateMap.put( "p_off_per_" + temIndex, uDiscountPerText.getText().toString() );
        updateMap.put( "p_off_rupee_" + temIndex, uDiscountRsText.getText().toString() );
        updateMap.put( "p_last_update_time", StaticMethods.getCrrDate() + " " + StaticMethods.getCrrTime() );
        updateRequest.onUpdateRequest(  this, productID, updateMap);

    }
    // Weight Updates request...
    private void updateWeight(){
        Map<String, Object> updateMap = new HashMap <>();
        if (weightType.equals( "NONE" )){
            updateMap.put( "p_weight_"+(listVariant+1), weightType );
        }else{
            updateMap.put( "p_weight_"+(listVariant+1), uWeight.getText().toString() + "-" +weightType );
        }
        updateMap.put( "p_last_update_time", StaticMethods.getCrrDate() + " " + StaticMethods.getCrrTime() );
        updateRequest.onUpdateRequest(  this, productID, updateMap);
//        if (crrShopCatIndex != -1 && layoutIndex != -1 && productIndex != -1 ){
//            homeCatListModelList.get( crrShopCatIndex ).getHomeListModelList().get( layoutIndex ).getProductModelList().get( productIndex )
//                    .getProductSubModelList().get( listVariant ).setpWeight( uWeight.getText().toString() + "-" +weightType );
//        }
            // Finish this activity...
    }

    // On Finish Update...
    @Override
    public void onUpdateFinished(boolean isSuccess) {
        switch (requestType){
            case UPDATE_WEIGHT:
                ProductDetails.pProductModel.getProductSubModelList().get( listVariant ).setpWeight( uWeight.getText().toString() );
                break;
            case UPDATE_PRICE:
                ProductDetails.pProductModel.getProductSubModelList().get( listVariant ).setpMrpPrice( uMrpEditText.getText().toString() );
                ProductDetails.pProductModel.getProductSubModelList().get( listVariant ).setpSellingPrice( uSellingEditText.getText().toString() );
                break;
            default:
                break;
        }
        if (isSuccess){
            showToast( "Successfully Updates!" );
        }else{
            showToast( "Update Failed!" );
        }
        dismissDialog();
        rootListener.onUpdateCompleted(requestType, isSuccess);
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    //---------------------------------------------------------------
    private void setLayoutVisibility(){
        switch (requestType){
            case UPDATE_WEIGHT:
                updateWeightLayout.setVisibility( View.VISIBLE );
                updatePriceLayout.setVisibility( View.GONE );
                updateTitle.setText( "Update Weight" );
                break;
            case UPDATE_PRICE:
                updatePriceLayout.setVisibility( View.VISIBLE );
                updateWeightLayout.setVisibility( View.GONE );
                updateTitle.setText( "Update Prices" );
                break;
            default:
                break;
        }
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
        rootListener.showToastMessage( msg );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException( context.toString() + " must implement OnFragmentInteractionListener" );
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


}
