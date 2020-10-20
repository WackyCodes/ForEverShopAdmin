package ean.ecom.eanshopadmin.main.orderlist.neworder;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.other.DialogsClass;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class NewOrderFragment extends Fragment {

    public static Fragment newOrderFragment;
    public static NewOrderTabAdaptor newOrderTabAdaptor;

    public NewOrderFragment() {
        // Required empty public constructor
    }

    private Dialog dialog;

    private ViewPager newOrderViewPager;
    private TabLayout newOrderTabLayoutIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_new_order, container, false );

        dialog = DialogsClass.getDialog( getContext() );
        // ----------
        newOrderViewPager = view.findViewById( R.id.new_order_view_pager );
        newOrderTabLayoutIndicator = view.findViewById( R.id.new_order_tab_layout );

        //----------- Product Description ---
        newOrderViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener( newOrderTabLayoutIndicator ) );
        newOrderTabLayoutIndicator.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                newOrderViewPager.setCurrentItem( tab.getPosition() );
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );

        newOrderTabAdaptor
                = new NewOrderTabAdaptor( getActivity().getSupportFragmentManager(), newOrderTabLayoutIndicator.getTabCount() );

        newOrderViewPager.setAdapter( newOrderTabAdaptor );
        newOrderTabAdaptor.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onResume() {
        if (newOrderTabAdaptor!=null){
            newOrderTabAdaptor.notifyDataSetChanged();
        }
        super.onResume();
    }

    /**  Order Status
     *          1. WAITING - ( For Accept )
     *          2. ACCEPTED - ( Preparing )
     *          3. PACKED - ( Waiting for Delivery ) READY_TO_DELIVERY
     *          4. PROCESS  - ( On Delivery ) OUT_FOR_DELIVERY
     *          5. SUCCESS - Success Full Delivered..!
     *          6. CANCELLED -  When Order has been cancelled by user...
     *          7. FAILED -  when PayMode Online and payment has been failed...
     *          8. PENDING - when Payment is Pending...
     *
     */

}
