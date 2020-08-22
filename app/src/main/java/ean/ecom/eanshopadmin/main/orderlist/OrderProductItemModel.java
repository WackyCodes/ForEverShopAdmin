package ean.ecom.eanshopadmin.main.orderlist;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

/**
 * Created by Shailendra (WackyCodes) on 31/07/2020 20:10
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderProductItemModel {

    private String productID;
    private String productImage;
    private String productName;
    private String productSellingPrice;
    private String productQty;

    public OrderProductItemModel(String productID, String productImage, String productName, String productSellingPrice, String productQty) {
        this.productID = productID;
        this.productImage = productImage;
        this.productName = productName;
        this.productSellingPrice = productSellingPrice;
        this.productQty = productQty;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSellingPrice() {
        return productSellingPrice;
    }

    public void setProductSellingPrice(String productSellingPrice) {
        this.productSellingPrice = productSellingPrice;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

}
