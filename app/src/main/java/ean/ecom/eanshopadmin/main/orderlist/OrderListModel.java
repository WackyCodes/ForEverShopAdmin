package ean.ecom.eanshopadmin.main.orderlist;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

/**
 * Created by Shailendra (WackyCodes) on 31/07/2020 19:38
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderListModel {

    private String orderID;

    // User Information...
    private String custAuthID;
    private String custName;
    private String custMobile;

    private String payMode;

    // Order Timing...
    private String orderDate;
    private String orderDay;
    private String orderTime;

    // Billing ...
    private String deliveryCharge;
    private String billingAmounts;
    private String productAmounts;

    // Shipping Name And Address...
    private String shippingName;
    private String shippingAddress;
    private String shippingPinCode;
    private GeoPoint shippingGeoPoint;

    // Delivery Status...
    private String deliveryStatus;
    private String deliverySchedule;

    // Delivery Info and Address...
    private String deliveryID; // new
    private String deliveredDate;
    private String deliveredDay;
    private String deliveredTime;
    private String deliveredByAuthID;
    private String deliveredByName;
    private String deliveredByMobile;
    private String outForDeliveryOTP; // new

    //  Order Item List...
    private List<OrderProductItemModel> orderProductItemsList;

    public OrderListModel() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustAuthID() {
        return custAuthID;
    }

    public void setCustAuthID(String custAuthID) {
        this.custAuthID = custAuthID;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDay() {
        return orderDay;
    }

    public void setOrderDay(String orderDay) {
        this.orderDay = orderDay;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getBillingAmounts() {
        return billingAmounts;
    }

    public void setBillingAmounts(String billingAmounts) {
        this.billingAmounts = billingAmounts;
    }

    public String getProductAmounts() {
        return productAmounts;
    }

    public void setProductAmounts(String productAmounts) {
        this.productAmounts = productAmounts;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingPinCode() {
        return shippingPinCode;
    }

    public void setShippingPinCode(String shippingPinCode) {
        this.shippingPinCode = shippingPinCode;
    }

    public GeoPoint getShippingGeoPoint() {
        return shippingGeoPoint;
    }

    public void setShippingGeoPoint(GeoPoint shippingGeoPoint) {
        this.shippingGeoPoint = shippingGeoPoint;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliverySchedule() {
        return deliverySchedule;
    }

    public void setDeliverySchedule(String deliverySchedule) {
        this.deliverySchedule = deliverySchedule;
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getDeliveredDay() {
        return deliveredDay;
    }

    public void setDeliveredDay(String deliveredDay) {
        this.deliveredDay = deliveredDay;
    }

    public String getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(String deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public String getDeliveredByAuthID() {
        return deliveredByAuthID;
    }

    public void setDeliveredByAuthID(String deliveredByAuthID) {
        this.deliveredByAuthID = deliveredByAuthID;
    }

    public String getDeliveredByName() {
        return deliveredByName;
    }

    public void setDeliveredByName(String deliveredByName) {
        this.deliveredByName = deliveredByName;
    }

    public String getDeliveredByMobile() {
        return deliveredByMobile;
    }

    public void setDeliveredByMobile(String deliveredByMobile) {
        this.deliveredByMobile = deliveredByMobile;
    }

    public List <OrderProductItemModel> getOrderProductItemsList() {
        return orderProductItemsList;
    }

    public void setOrderProductItemsList(List <OrderProductItemModel> orderProductItemsList) {
        this.orderProductItemsList = orderProductItemsList;
    }

    public String getOutForDeliveryOTP() {
        return outForDeliveryOTP;
    }

    public void setOutForDeliveryOTP(String outForDeliveryOTP) {
        this.outForDeliveryOTP = outForDeliveryOTP;
    }

    /*

     Map <String, Object> orderDetailMap = new HashMap <>();

        orderDetailMap.put( "order_id",  orderID );

        orderDetailMap.put( "delivery_status", "WAITING" );
        orderDetailMap.put( "pay_mode", "COD" );

        orderDetailMap.put( "delivery_charge", deliveryCharge );
        orderDetailMap.put( "total_amounts", billingAmounts );
        orderDetailMap.put( "saving_amounts", savingAmounts );
        orderDetailMap.put( "billing_amounts", billingAmounts );

        // Put User Info and Address..
        orderDetailMap.put( "order_by_auth_id", orderByUserId );
        orderDetailMap.put( "order_by_name", orderByUserName );
        orderDetailMap.put( "order_by_mobile", orderByUserMobile );

        orderDetailMap.put( "order_accepted_by", orderDeliveredName );
        orderDetailMap.put( "order_delivery_address", orderDeliveryAddress );
        orderDetailMap.put( "order_delivery_pin", orderDeliveryPin );
        orderDetailMap.put( "order_date", orderDate );
        orderDetailMap.put( "order_day", orderDay );
        orderDetailMap.put( "order_time", orderTime );

        orderDetailMap.put( "delivery_schedule_time", deliverySchedule );

        // Get No_of_Product =
        orderDetailMap.put( "no_of_products", temCartItemModelList.size() );

        for (int x = 0; x < temCartItemModelList.size(); x++){
            orderDetailMap.put( "product_id_" + x, temCartItemModelList.get( x ).getProductID() );
            orderDetailMap.put( "product_image_" + x, temCartItemModelList.get( x ).getProductImage() );
            orderDetailMap.put( "product_name_" + x, temCartItemModelList.get( x ).getProductName() );
            orderDetailMap.put( "product_price_" + x, temCartItemModelList.get( x ).getProductSellingPrice() );
            orderDetailMap.put( "product_qty_" + x, temCartItemModelList.get( x ).getProductQty() );
        }

        // Put Delivery Info and Address..
//        orderDetailMap.put( "delivery_id", "" );
//        orderDetailMap.put( "delivery_date", "" );
//        orderDetailMap.put( "delivery_day", "" );
//        orderDetailMap.put( "delivery_time", "" );
//        orderDetailMap.put( "delivery_by_uid", "" );
//        orderDetailMap.put( "delivery_by_name", "" );
//        orderDetailMap.put( "delivery_by_mobile", "" );

     */


}
