package ean.ecom.eanshopadmin.product.horizontal;

import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.addnew.newproduct.AddNewProductActivity;
import ean.ecom.eanshopadmin.product.ProductModel;
import ean.ecom.eanshopadmin.product.productview.ProductDetails;

import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.VIEW_GRID_LAYOUT;
import static ean.ecom.eanshopadmin.other.StaticValues.VIEW_HORIZONTAL_LAYOUT;
import static ean.ecom.eanshopadmin.other.StaticValues.VIEW_PRODUCT_SEARCH_LAYOUT;
import static ean.ecom.eanshopadmin.other.StaticValues.VIEW_RECTANGLE_LAYOUT;
import static ean.ecom.eanshopadmin.other.StaticValues.clipboardManager;

public class ProductHrGridAdaptor extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private int crrShopCatIndex;
    private int layoutIndex;
    private int viewType;
    private List <ProductModel> productModelList;

    public ProductHrGridAdaptor(int crrShopCatIndex, int layoutIndex, int viewType, List <ProductModel> productModelList) {
        this.crrShopCatIndex = crrShopCatIndex;
        this.layoutIndex = layoutIndex;
        this.viewType = viewType;
        this.productModelList = productModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (viewType) {
            case VIEW_HORIZONTAL_LAYOUT:
                return VIEW_HORIZONTAL_LAYOUT;
            case VIEW_RECTANGLE_LAYOUT:
                return VIEW_RECTANGLE_LAYOUT;
            case VIEW_GRID_LAYOUT:
                return VIEW_GRID_LAYOUT;
            case VIEW_PRODUCT_SEARCH_LAYOUT:
                return VIEW_PRODUCT_SEARCH_LAYOUT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO : Case...
        switch (viewType){
            case VIEW_HORIZONTAL_LAYOUT:
            case VIEW_RECTANGLE_LAYOUT:
            case VIEW_GRID_LAYOUT:
                View hrView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.product_square_layout_item, parent, false );
                return new HomeHorizontalViewHolder( hrView );
            case VIEW_PRODUCT_SEARCH_LAYOUT:
                View searchView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.product_rectanle_view_item, parent, false );
                return new RectangleViewHolder( searchView );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (viewType){
            case VIEW_HORIZONTAL_LAYOUT:
            case VIEW_RECTANGLE_LAYOUT:
            case VIEW_GRID_LAYOUT:
                // Square View....
                if (position < productModelList.size()){
                    String productId = productModelList.get( position ).getpProductID();
                    List<String> imgLink = productModelList.get( position ).getProductSubModelList().get( 0 ).getpImage();
                    String name = productModelList.get( position ).getProductSubModelList().get( 0 ).getpName();
                    String price = productModelList.get( position ).getProductSubModelList().get( 0 ).getpSellingPrice();
                    String cutPrice = productModelList.get( position ).getProductSubModelList().get( 0 ).getpMrpPrice();
                    String pStocks = productModelList.get( position ).getProductSubModelList().get( 0 ).getpStocks();
                    // TODO : Case...
                    ((HomeHorizontalViewHolder) holder).setHomeHrProduct( productId, imgLink, name, price, cutPrice, pStocks, position );
                }else{
                    ((HomeHorizontalViewHolder) holder).setAddNewProductView();
                }
                break;
            case VIEW_PRODUCT_SEARCH_LAYOUT:
                String productId = productModelList.get( position ).getpProductID();
                List<String> imgLink = productModelList.get( position ).getProductSubModelList().get( 0 ).getpImage();
                String name = productModelList.get( position ).getProductSubModelList().get( 0 ).getpName();
                String price = productModelList.get( position ).getProductSubModelList().get( 0 ).getpSellingPrice();
                String cutPrice = productModelList.get( position ).getProductSubModelList().get( 0 ).getpMrpPrice();
                ((RectangleViewHolder) holder).setData( productId, imgLink,  name, price, cutPrice, "",
                        productModelList.get( position ).getProductSubModelList().get( 0 ).getpStocks(), position );
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (viewType == VIEW_HORIZONTAL_LAYOUT ){
            return productModelList.size()+1;
        }else {
            return productModelList.size();
        }
    }

    public class HomeHorizontalViewHolder extends RecyclerView.ViewHolder {

        ImageView hrProductImage;
        TextView hrProductName;
        TextView hrProductPrice;
        TextView hrProductCutPrice;
        TextView hrProductOffPercentage;
        TextView hrProductStocks;

        ConstraintLayout productView;
        LinearLayout addNewProductView;

        public HomeHorizontalViewHolder(@NonNull View itemView) {
            super( itemView );

            hrProductImage = itemView.findViewById( R.id.hr_product_image );
            hrProductName = itemView.findViewById( R.id.hr_product_name );
            hrProductPrice = itemView.findViewById( R.id.hr_product_price );
            hrProductCutPrice = itemView.findViewById( R.id.hr_product_cut_price );
            hrProductOffPercentage = itemView.findViewById( R.id.hr_off_percentage );
            hrProductStocks = itemView.findViewById( R.id.stock_text );

            productView = itemView.findViewById( R.id.product_view_const_layout );
            addNewProductView = itemView.findViewById( R.id.product_view_linear_layout );

        }

        private void setHomeHrProduct(final String productId,List<String> imgLink, String name, String price, String cutPrice, String pStocks, final int index) {

            addNewProductView.setVisibility( View.GONE );
            productView.setVisibility( View.VISIBLE );

            hrProductName.setText( name );
            hrProductPrice.setText( "Rs." + price + "/-" );
            hrProductCutPrice.setText( "Rs." + cutPrice + "/-" );

            Glide.with( itemView.getContext() ).load( imgLink.get( 0 ) ).apply( new RequestOptions()
                    .placeholder( R.drawable.ic_photo_black_24dp ) ).into( hrProductImage );

            int perOff = ((Integer.parseInt( cutPrice ) - Integer.parseInt( price )) * 100) / Integer.parseInt( cutPrice );
            hrProductOffPercentage.setText( perOff + "% Off" );

            if (Integer.parseInt( pStocks )>0){
                hrProductStocks.setText( "In Stocks (" + pStocks + ")" );
            }else{
                hrProductStocks.setText( "Out Of Stocks" );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    hrProductStocks.setBackgroundTintList( itemView.getResources().getColorStateList( R.color.colorRed ) );
                }
            }

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent = new Intent( itemView.getContext(), ProductDetails.class );
                    productDetailIntent.putExtra( "PRODUCT_ID", productId );
                    productDetailIntent.putExtra( "HOME_CAT_INDEX", crrShopCatIndex );
                    productDetailIntent.putExtra( "LAYOUT_INDEX", layoutIndex );
                    productDetailIntent.putExtra( "PRODUCT_INDEX", index );
                    itemView.getContext().startActivity( productDetailIntent );

                }
            } );

        }

        private void setAddNewProductView(){
            addNewProductView.setVisibility( View.VISIBLE );
            productView.setVisibility( View.GONE );

            addNewProductView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addProduct = new Intent( itemView.getContext(), AddNewProductActivity.class );

                    addProduct.putExtra( "CAT_INDEX", crrShopCatIndex );
                    addProduct.putExtra( "LAY_INDEX", layoutIndex );
                    addProduct.putExtra( "UPDATE", false );

                    itemView.getContext().startActivity( addProduct );
                }
            } );

        }

    }

    public class RectangleViewHolder extends RecyclerView.ViewHolder{

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productCutPrice;
        TextView productOffPercentage;
        TextView productStockInfo;

        TextView productIDText;
        TextView addProductBtn;
        ImageView copyIDBtn;

        public RectangleViewHolder(@NonNull View itemView) {
            super( itemView );
            productImage = itemView.findViewById( R.id.hr_viewAll_product_image );
            productName = itemView.findViewById( R.id.hr_viewAll_product_name );
            productPrice = itemView.findViewById( R.id.hr_viewAll_product_price );
            productCutPrice = itemView.findViewById( R.id.hr_viewAll_product_cut_price );
            productOffPercentage = itemView.findViewById( R.id.hr_viewAll_product_off_per );
            productStockInfo = itemView.findViewById( R.id.hr_viewAll_product_stock_info );

            productIDText = itemView.findViewById( R.id.product_id_text );
            addProductBtn = itemView.findViewById( R.id.product_add_text_view );
            copyIDBtn = itemView.findViewById( R.id.product_id_copy_img_view );
        }

        private void setData(final String productID, List<String> imageLink, String pName, String pPrice, String pMRP, String pOffPer, String pStocks, final int index ){

            Glide.with( itemView.getContext() ).load( imageLink.get( 0 ) ).apply( new RequestOptions()
                    .placeholder( R.drawable.ic_photo_black_24dp ) ).into( productImage );

            productName.setText( pName );
            productPrice.setText( "Rs." + pPrice + "/-" );
            productCutPrice.setText( "Rs." + pMRP + "/-" );
            productIDText.setText( productID );

            if (Integer.parseInt( pStocks ) > 0) {
                productStockInfo.setText( "in Stock ("+ pStocks + ")" );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    productStockInfo.setBackgroundTintList( itemView.getResources().getColorStateList( R.color.colorGreen ) );
                }

            } else {
                productStockInfo.setText( "Out of Stock" );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    productStockInfo.setBackgroundTintList( itemView.getResources().getColorStateList( R.color.colorRed ) );
                }
            }

            // Copy Id....
            copyIDBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : Copy...
                    // Text ..
                    ClipData clipData = ClipData.newPlainText( "TEXT", productID );
                    clipboardManager.setPrimaryClip( clipData );
                    showToast( itemView.getContext(), "Copied!");
                    // TO ACCESS THE DATA...
        //        if (clipboardManager.hasPrimaryClip()){
        //            String data = clipboardManager.getPrimaryClip().getItemAt( 0 ).getText().toString();
        //        }
                }
            } );

            // Product Click...
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent productDetailIntent = new Intent( itemView.getContext(), ProductDetails.class );
                    productDetailIntent.putExtra( "PRODUCT_ID", productID );
                    productDetailIntent.putExtra( "PRODUCT_INDEX", index );

                }
            } );

        }

    }

}


