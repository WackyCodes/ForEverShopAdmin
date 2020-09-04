package ean.ecom.eanshopadmin.product.specifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.product.update.specification.AddSpecificationFeatureModel;
import ean.ecom.eanshopadmin.product.update.specification.AddSpecificationModel;

public class ProductSpecificationAdaptor extends RecyclerView.Adapter<ProductSpecificationAdaptor.ViewHolder>   {

    private List <AddSpecificationModel> productSpecificationList;

    public ProductSpecificationAdaptor(List <AddSpecificationModel> productSpecificationList) {
        this.productSpecificationList = productSpecificationList;
    }

    @NonNull
    @Override
    public ProductSpecificationAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.pro_specification_lay_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdaptor.ViewHolder holder, int position) {
        String  title = productSpecificationList.get( position ).getSpHeading();
        List<AddSpecificationFeatureModel> list = productSpecificationList.get( position ).getSpecificationFeatureModelList();
        holder.setData( title, list );
    }

    @Override
    public int getItemCount() {
        return productSpecificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView spTitle;
        private RecyclerView spRecycler;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            spTitle = itemView.findViewById( R.id.specification_title );
            spRecycler = itemView.findViewById( R.id.specification_recycler );
        }

        private void setData(String heading, List<AddSpecificationFeatureModel> list){

            spTitle.setText( heading );

            LinearLayoutManager layoutManager = new LinearLayoutManager( itemView.getContext() );
            layoutManager.setOrientation( RecyclerView.VERTICAL );
            spRecycler.setLayoutManager( layoutManager );

            FeaturesAdaptor adaptor = new FeaturesAdaptor( list );
            spRecycler.setAdapter( adaptor );
            adaptor.notifyDataSetChanged();
        }

    }

    class FeaturesAdaptor extends RecyclerView.Adapter<FeaturesAdaptor.Holder>{

        List<AddSpecificationFeatureModel> list;

        public FeaturesAdaptor(List <AddSpecificationFeatureModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public FeaturesAdaptor.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() )
                    .inflate( R.layout.product_detail_specification_item, parent, false );
            return new Holder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.featureName.setText( list.get(position).getFeatureName());
            holder.featureValue.setText( list.get( position ).getFeatureDetails() );
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder{
            TextView featureName;
            TextView featureValue;
            public Holder(@NonNull View itemView) {
                super( itemView );
                featureName = itemView.findViewById( R.id.feature_name );
                featureValue = itemView.findViewById( R.id.feature_value );
            }

        }

    }

}
