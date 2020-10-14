package ean.ecom.eanshopadmin.main.shoprating;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private RecyclerView recyclerView;

    private TextView tvTotalRatings;
    private TextView tvNumOfRatings;
    private TextView tvNoRating;
    private LinearLayout layoutRating;

    public ShopRatingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_shop_rating, container, false );

        recyclerView = view.findViewById( R.id.shop_rating_recycler_view );
        tvTotalRatings = view.findViewById( R.id.total_rating_textView );
        tvNumOfRatings = view.findViewById( R.id.total_rating_peoples_textView );
        tvNoRating = view.findViewById( R.id.no_record_textView );
        layoutRating = view.findViewById( R.id.total_rating_layout );

        // Set data...
        tvNoRating.setVisibility( View.VISIBLE );
        layoutRating.setVisibility( View.GONE );

        LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity() );
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
