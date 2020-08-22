package ean.ecom.eanshopadmin.product.specifications;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ean.ecom.eanshopadmin.R;

public class ProductDetailsSpecificationFragment extends Fragment {

    public static List <ProductDetailsSpecificationModel> productDetailsSpecificationModelList = new ArrayList <>();

    public ProductDetailsSpecificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment...
        View view = inflater.inflate( R.layout.fragment_product_details_specification, container, false );

        RecyclerView productSpecificationRecyclerView = view.findViewById( R.id.product_specification_recyclerView );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( view.getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );

        productSpecificationRecyclerView.setLayoutManager( linearLayoutManager );

        ProductDetailsSpecificationAdaptor productDetailsSpecificationAdaptor = new ProductDetailsSpecificationAdaptor( productDetailsSpecificationModelList );
        productSpecificationRecyclerView.setAdapter( productDetailsSpecificationAdaptor );

        productDetailsSpecificationAdaptor.notifyDataSetChanged();

        return  view;
    }


}
