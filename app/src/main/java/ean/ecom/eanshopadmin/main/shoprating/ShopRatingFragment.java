package ean.ecom.eanshopadmin.main.shoprating;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ean.ecom.eanshopadmin.R;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopRatingFragment extends Fragment {


    public ShopRatingFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_shop_rating, container, false );

        recyclerView = view.findViewById( R.id.shop_rating_recycler_view );
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerView.setLayoutManager( layoutManager );

        ShopRatingAdaptor adaptor = new ShopRatingAdaptor( new ArrayList <ShopRatingModel>() );

        /**
         * Just Need to Add Adaptor...
         * and TODO: Write Query to Get Data From Database...
         * and Set Data into List...
         */

        recyclerView.setAdapter( adaptor );

        return view;
    }

}
