package ean.ecom.eanshopadmin.main.orderlist.orderviewdetails;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

/**
 * Created by Shailendra (WackyCodes) on 14/09/2020 23:40
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderViewListItemModel {

    private String productName;
    private String productImage;
    private String productPrice;
    private String productMRP;
    private String productQty;
    private String productWeight;


    public OrderViewListItemModel(String productName, String productImage, String productPrice, String productMRP, String productQty, String productWeight) {
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productMRP = productMRP;
        this.productQty = productQty;
        this.productWeight = productWeight;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductMRP() {
        return productMRP;
    }

    public void setProductMRP(String productMRP) {
        this.productMRP = productMRP;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }
}
