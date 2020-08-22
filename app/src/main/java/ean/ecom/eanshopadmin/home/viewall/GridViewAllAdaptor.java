package ean.ecom.eanshopadmin.home.viewall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.product.ProductModel;
import ean.ecom.eanshopadmin.product.ProductSubModel;
import ean.ecom.eanshopadmin.product.productview.ProductDetails;

public class GridViewAllAdaptor extends BaseAdapter {

    private int crrShopCatIndex;
    private int layoutIndex;
    private int viewType;
    private List <ProductModel> productModelList;

    public GridViewAllAdaptor(int crrShopCatIndex, int layoutIndex, int viewType, List <ProductModel> productModelList) {
        this.crrShopCatIndex = crrShopCatIndex;
        this.layoutIndex = layoutIndex;
        this.viewType = viewType;
        this.productModelList = productModelList;
    }

    @Override
    public int getCount() {
        return productModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int i, View v, final ViewGroup viewGroup) {
//            product_square_layout_item

        ProductModel productModel = productModelList.get( i );
        final int verCode = 0;
        ProductSubModel productSubModel = productModel.getProductSubModelList().get( verCode );

        View view = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.product_square_layout_item, null );

        ConstraintLayout itemLayout = view.findViewById( R.id.product_view_const_layout );
        view.findViewById( R.id.product_view_linear_layout ).setVisibility( View.GONE ); // Add New Product INVISIBLE..
        itemLayout.setVisibility( View.VISIBLE );
        // Get Reference...
        ImageView img = view.findViewById( R.id.hr_product_image );
        TextView name = view.findViewById( R.id.hr_product_name );
        TextView price = view.findViewById( R.id.hr_product_price );
        TextView cutPrice = view.findViewById( R.id.hr_product_cut_price );
        TextView perOffText = view.findViewById( R.id.hr_off_percentage );

        // Set Data...
        Glide.with( viewGroup.getContext() ).load( productSubModel.getpImage().get( 0 ) )
                .apply( new RequestOptions()
                .placeholder( R.drawable.ic_photo_black_24dp ) ).into( img );

        name.setText( productSubModel.getpName() );
        price.setText( "Rs." + productSubModel.getpSellingPrice() + "/-" );
        cutPrice.setText( "Rs." + productSubModel.getpMrpPrice() + "/-" );
        int saveAmt = Integer.parseInt( productSubModel.getpMrpPrice() ) - Integer.parseInt( productSubModel.getpSellingPrice() );
        perOffText.setText( "Rs." + saveAmt + "save");

        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOnProductClick( viewGroup.getContext(), i );
            }
        } );

        return view;

    }

    private void addOnProductClick(Context context, int proIndex ){
        Intent productDetailIntent = new Intent( context, ProductDetails.class );
        productDetailIntent.putExtra( "PRODUCT_ID", productModelList.get( proIndex ).getpProductID() );
        productDetailIntent.putExtra( "HOME_CAT_INDEX", crrShopCatIndex );
        productDetailIntent.putExtra( "LAYOUT_INDEX", layoutIndex );
        productDetailIntent.putExtra( "PRODUCT_INDEX", proIndex );
        context.startActivity( productDetailIntent );
    }




}
