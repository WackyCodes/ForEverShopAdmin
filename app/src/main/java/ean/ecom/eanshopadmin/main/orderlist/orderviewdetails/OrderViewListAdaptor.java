package ean.ecom.eanshopadmin.main.orderlist.orderviewdetails;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.main.orderlist.OrderProductItemModel;
import ean.ecom.eanshopadmin.main.orderlist.OrderProductsModel;

/**
 * Created by Shailendra (WackyCodes) on 14/09/2020 23:43
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderViewListAdaptor extends RecyclerView.Adapter<OrderViewListAdaptor.ViewHolder> {

    private List<OrderProductsModel> orderViewListItemModelList;

    public OrderViewListAdaptor(List <OrderProductsModel> orderViewListItemModelList) {
        this.orderViewListItemModelList = orderViewListItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderListView =  LayoutInflater.from( parent.getContext() ).inflate(
                R.layout.order_view_list_item, parent, false );
        return new ViewHolder(orderListView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData( position );
    }

    @Override
    public int getItemCount() {
        return orderViewListItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView pName;
        private TextView pPrice;
        private TextView pMRP;
        private TextView pQTY;
        private TextView pVariant;
        private ImageView pImage;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            pImage = itemView.findViewById( R.id.product_image );
            pName = itemView.findViewById( R.id.product_name );
            pPrice = itemView.findViewById( R.id.product_selling_price );
            pMRP = itemView.findViewById( R.id.product_mrp_price );
            pQTY = itemView.findViewById( R.id.product_item_qty );
            pVariant = itemView.findViewById( R.id.product_veriant_type );

        }

        private void setData(int position){
            OrderProductsModel model = new OrderProductsModel();
            model.setData( (Map <String, Object>)  orderViewListItemModelList.get( position ) );

            Glide.with( itemView.getContext() ).load( model.getProductImage() ).apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( pImage );

            pName.setText( model.getProductName() );
            pPrice.setText( "Rs." + model.getProductSellingPrice() + "/-" );
            pMRP.setText( "Rs." + model.getProductMrpPrice() + "/-" );

            pQTY.setText( "QTY : "+ model.getProductQty() );

            if (model.getProductWeight().equals( "NONE" ) || model.getProductWeight().equals( "NONE-NONE" )){
                pVariant.setVisibility( View.INVISIBLE );
            }else{
                pVariant.setVisibility( View.VISIBLE );
                pVariant.setText( model.getProductWeight() );
            }

        }

    }


}
