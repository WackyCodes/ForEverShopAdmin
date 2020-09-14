package ean.ecom.eanshopadmin.main.orderlist.orderviewdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ean.ecom.eanshopadmin.R;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */


public class OrderViewActivity extends AppCompatActivity {



    // Variables...

    // Time and Date...
    private TextView orderDate; // order_date
    private TextView orderTime; // order_time

    // Order List...
    private RecyclerView orderListRecycler; // product_list_recycler
    // Bill
    private TextView totalItemsText; // total_items_tv
    private TextView totalItemsAmountText; // total_items_amount_tv

    // Order By And Address...
    private TextView orderByName; // order_by_name
    private TextView shippingName; // shipping_name
    private TextView shippingAddress; // shipping_address
    private TextView shippingPin; // shipping_address_pin

    // Order Status...
    private ImageView orderStatusIcon ; // order_status_icon
    private TextView orderStatusText; // order_status_text_view

    private ImageView deliveryBoyViewBtn ; // view_delivery_boy_img_view

    // Order Action Btn...
    private TextView acceptOrderBtn; // accept_order_text_btn
    private TextView packingDoneBtn; // preparing_packing_text_btn

    private TextView outForDeliveryBtn; // out_for_delivery_text_btn
    private EditText deliveryOtpEditText; // delivery_otp_edit_text

    // Delivery Boy.. View....
    private LinearLayout searchingDeliveryBoyLayout; // searching_delivery_boy_layout

    private ConstraintLayout deliveryBoyViewLayout; // delivery_boy_const_layout
    private TextView deliveryBoyName; // delivery_by_name_text_view
    private TextView deliveryBoyStatus; // delivery_boy_status
    private TextView deliveryBoyProfileBtn; // delivery_boy_view_profile


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_view );

    }





}
