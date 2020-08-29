package ean.ecom.eanshopadmin.main.orderlist.neworder;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import static ean.ecom.eanshopadmin.database.DBQuery.newOrderList;
import static ean.ecom.eanshopadmin.database.DBQuery.preparingOrderList;
import static ean.ecom.eanshopadmin.database.DBQuery.readyToDeliveredList;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_NEW_ORDER;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_PREPARING;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_READY_TO_DELIVER;

/**
 * Created by Shailendra (WackyCodes) on 04/08/2020 00:58
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class NewOrderTabAdaptor extends FragmentPagerAdapter {
    // New Order Fragment...
    public OrderViewPagerFragment newOrderFragment;
    public OrderViewPagerFragment preparingFragment;
    public OrderViewPagerFragment readyToDeliverFragment;

    private int totalTabs;

    NewOrderTabAdaptor(@NonNull FragmentManager fm, int totalTabs) {
        super( fm );
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                // Case for New Order List... ORDER_LIST_NEW_ORDER
                /* So Here we have to filter our list first..! and Set it in adaptor.../ Fragment.. */
                if (newOrderFragment == null){
                    newOrderFragment = new OrderViewPagerFragment(newOrderList, ORDER_LIST_NEW_ORDER );
                }
                if (newOrderFragment.orderViewPagerListAdaptor!=null){
                    newOrderFragment.orderViewPagerListAdaptor.notifyDataSetChanged();
                }

                return newOrderFragment;
            case 1:
                if (preparingFragment == null){
                    preparingFragment = new OrderViewPagerFragment(preparingOrderList, ORDER_LIST_PREPARING );
                }
                if (preparingFragment.orderViewPagerListAdaptor!=null){
                    preparingFragment.orderViewPagerListAdaptor.notifyDataSetChanged();
                }

                return preparingFragment;
            case 2:
                if (readyToDeliverFragment == null){
                    readyToDeliverFragment = new OrderViewPagerFragment(readyToDeliveredList, ORDER_LIST_READY_TO_DELIVER );
                }
                if (readyToDeliverFragment.orderViewPagerListAdaptor!=null){
                    readyToDeliverFragment.orderViewPagerListAdaptor.notifyDataSetChanged();
                }

                return readyToDeliverFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
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


    public static void setNoOrderText(int FragmentType, int visibility ){
        // Show the No Order Text.. If List size = 0;
        if ( NewOrderFragment.newOrderTabAdaptor != null){
            switch (FragmentType){
                case ORDER_LIST_NEW_ORDER:
                    NewOrderFragment.newOrderTabAdaptor.newOrderFragment.orderViewPagerListAdaptor.notifyDataSetChanged();
                    if (NewOrderFragment.newOrderTabAdaptor.newOrderFragment.noOrderText != null){
                        NewOrderFragment.newOrderTabAdaptor.newOrderFragment.noOrderText.setVisibility( visibility );
                    }
                    break;
                case ORDER_LIST_PREPARING:
                    NewOrderFragment.newOrderTabAdaptor.preparingFragment.orderViewPagerListAdaptor.notifyDataSetChanged();
                    if (NewOrderFragment.newOrderTabAdaptor.preparingFragment.noOrderText != null){
                        NewOrderFragment.newOrderTabAdaptor.preparingFragment.noOrderText.setVisibility( visibility );
                    }
                    break;
                case ORDER_LIST_READY_TO_DELIVER:
                    NewOrderFragment.newOrderTabAdaptor.readyToDeliverFragment.orderViewPagerListAdaptor.notifyDataSetChanged();
                    if (NewOrderFragment.newOrderTabAdaptor.readyToDeliverFragment.noOrderText != null){
                        NewOrderFragment.newOrderTabAdaptor.readyToDeliverFragment.noOrderText.setVisibility( visibility );
                    }
                    break;
                default:
                    break;
            }
        }

    }


}
