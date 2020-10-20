package ean.ecom.eanshopadmin.main.orderlist.neworder;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.main.orderlist.OrderListAdaptor;
import ean.ecom.eanshopadmin.main.orderlist.OrderListModel;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class OrderViewPagerFragment extends Fragment {

    public RecyclerView orderViewPagerRecyclerView;
    public TextView noOrderText;

    public OrderViewPagerFragment() {
        // Required empty public constructor
    }

    private List <OrderListModel> orderListModelList;
    private int listType;

    public OrderViewPagerFragment(List <OrderListModel> orderListModelList, int listType) {
        // Required empty public constructor
        this.orderListModelList = orderListModelList;
        this.listType = listType;
    }

    public static OrderListAdaptor orderViewPagerListAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_order_view_pager, container, false );

        orderViewPagerRecyclerView = view.findViewById( R.id.order_view_pager_recycler );
        noOrderText = view.findViewById( R.id.no_order_text );

        // Set Layout Manager..
        LinearLayoutManager layoutManager = new LinearLayoutManager( view.getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        orderViewPagerRecyclerView.setLayoutManager( layoutManager );

        orderViewPagerListAdaptor = new OrderListAdaptor( orderListModelList, listType );
        orderViewPagerRecyclerView.setAdapter( orderViewPagerListAdaptor );
        orderViewPagerListAdaptor.notifyDataSetChanged();

        if (orderListModelList.size() == 0){
            noOrderText.setVisibility( View.VISIBLE );
        }else{
            noOrderText.setVisibility( View.GONE );
        }

        return view;
    }

    @Override
    public void onResume() {
        if (orderViewPagerListAdaptor!=null){
            orderViewPagerListAdaptor.notifyDataSetChanged();
        }
        super.onResume();
    }


}
