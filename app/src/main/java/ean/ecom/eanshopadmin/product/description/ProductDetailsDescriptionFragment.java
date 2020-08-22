package ean.ecom.eanshopadmin.product.description;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ean.ecom.eanshopadmin.R;

public class ProductDetailsDescriptionFragment extends Fragment {

    private TextView productDetailDescription;
    public static String productDescription;

    public ProductDetailsDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_product_details_description, container, false );

        productDetailDescription = view.findViewById( R.id.product_details_description );
        productDetailDescription.setText( productDescription );
//        productDetailDescription.notify();

        return view;
    }

}
