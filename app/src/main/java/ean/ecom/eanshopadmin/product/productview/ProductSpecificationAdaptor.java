package ean.ecom.eanshopadmin.product.productview;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import ean.ecom.eanshopadmin.R;

/**
 * Created by Shailendra (WackyCodes) on 14/09/2020 10:27
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class ProductSpecificationAdaptor extends RecyclerView.Adapter<ProductSpecificationAdaptor.ViewHolder> {

    private List <Object> specificationModelList;

    public ProductSpecificationAdaptor(List <Object> specificationModelList) {
        this.specificationModelList = specificationModelList;
//        specificationModelList.addAll( model );
    }

    @NonNull
    @Override
    public ProductSpecificationAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.product_specification_layout_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdaptor.ViewHolder holder, int position) {
        holder.setData( position );
    }

    @Override
    public int getItemCount() {
        return specificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView spTitleText;
        private RecyclerView spRecycler;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            spTitleText = itemView.findViewById( R.id.specification_title );
            spRecycler = itemView.findViewById( R.id.specification_item_recycler );
        }

        private void setData( int position){
            // Set Layout...
            LinearLayoutManager layoutManager = new LinearLayoutManager( itemView.getContext() );
            layoutManager.setOrientation( RecyclerView.VERTICAL );
            spRecycler.setLayoutManager( layoutManager );

            List<Object> featureModelArrayList;
            // Get Map Object From List
            HashMap <String, Object> specificationModel = (HashMap <String, Object>)specificationModelList.get( position );

            // Set Data...
            spTitleText.setText( specificationModel.get( "spHeading" ).toString() );
            if (specificationModel.get( "specificationFeatureModelList" ) != null){
                // Get Feature List...
                featureModelArrayList = (List <Object>) specificationModel.get( "specificationFeatureModelList" );
                // Set Recycler...
                SpecificationFeatureAdaptor adaptor = new SpecificationFeatureAdaptor( featureModelArrayList );
                spRecycler.setAdapter( adaptor );
                adaptor.notifyDataSetChanged();
            }

        }

    }

    //--------------------- Set Features.... -------------------------
    private class SpecificationFeatureAdaptor extends RecyclerView.Adapter<SpecificationFeatureAdaptor.FeatureViewHolder>{

        private List <Object> featureModelList;

        public SpecificationFeatureAdaptor(List <Object> featureModelList) {
            this.featureModelList = featureModelList;
        }

        @NonNull
        @Override
        public SpecificationFeatureAdaptor.FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() )
                    .inflate( R.layout.product_detail_specification_item, parent, false );
            return new FeatureViewHolder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull SpecificationFeatureAdaptor.FeatureViewHolder holder, int position) {
//            String name =  specificationModelList.get( featureListIndex ).getSpecificationFeatureModelList().get( position ).getFeatureName();
//            String details =  specificationModelList.get( featureListIndex ).getSpecificationFeatureModelList().get( position ).getFeatureDetails();
            holder.setFeature( position );
        }

        @Override
        public int getItemCount() {
            return featureModelList.size();
        }

        public class FeatureViewHolder extends RecyclerView.ViewHolder {
            private TextView titleText;
            private TextView detailsText;
            public FeatureViewHolder(@NonNull View itemView) {
                super( itemView );
                titleText = itemView.findViewById( R.id.feature_name );
                detailsText = itemView.findViewById( R.id.feature_value );
            }
            private void setFeature( int pos ){
                HashMap<String, Object> featureModelHashMap = (HashMap <String, Object>)featureModelList.get( pos );
                titleText.setText( featureModelHashMap.get( "featureName" ).toString() );
                detailsText.setText( featureModelHashMap.get( "featureDetails" ).toString() );
            }
        }

    }


}
