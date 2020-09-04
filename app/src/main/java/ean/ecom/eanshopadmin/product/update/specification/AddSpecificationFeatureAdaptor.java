package ean.ecom.eanshopadmin.product.update.specification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ean.ecom.eanshopadmin.R;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class AddSpecificationFeatureAdaptor  extends  RecyclerView.Adapter<AddSpecificationFeatureAdaptor.ViewHolder> {

//    private List <AddSpecificationModel> specificationModelList;
    private List<AddSpecificationFeatureModel> specificationFeatureModelList;
    private int index;

    public AddSpecificationFeatureAdaptor(List <AddSpecificationFeatureModel> specificationFeatureModelList, int index) {
        this.specificationFeatureModelList = specificationFeatureModelList;
        this.index = index;
    }

    public AddSpecificationFeatureAdaptor(int index ) {
        this.index = index;
    }

    @NonNull
    @Override
    public AddSpecificationFeatureAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View spFeatureView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.product_detail_specification_item, parent, false );
        return new ViewHolder( spFeatureView );
    }
    @Override
    public void onBindViewHolder(@NonNull AddSpecificationFeatureAdaptor.ViewHolder holder, int position) {
        if(index >= 0){
//            String name = specificationModelList.get( index ).getSpecificationFeatureModelList().get( position ).getFeatureName();
//            String details = specificationModelList.get( index ).getSpecificationFeatureModelList().get( position ).getFeatureDetails();

            String name = specificationFeatureModelList.get( position ).getFeatureName();
            String details = specificationFeatureModelList.get( position ).getFeatureDetails();
            holder.setData( name, details, position );
        }
    }
    @Override
    public int getItemCount() {
        if (index >= 0)
            return specificationFeatureModelList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView featureName;
        private TextView featureDetails;
        private ImageButton featureDeleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            featureName = itemView.findViewById( R.id.feature_name );
            featureDetails = itemView.findViewById( R.id.feature_value );
            featureDeleteBtn = itemView.findViewById( R.id.add_new_pro_sp_feature_delete_btn );
        }
        private void setData(String name, String detail, final int pos){
            featureDeleteBtn.setVisibility( View.VISIBLE );
            featureName.setText( name );
            featureDetails.setText( detail );
            featureDeleteBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    specificationFeatureModelList.remove( pos );
                    UpdateImage_SpFragment.specificationAdaptor.notifyDataSetChanged();
                    // :  addSpecificationFeatureAdaptor.notifyDataSetChanged();
                }
            } );
        }
    }
}
