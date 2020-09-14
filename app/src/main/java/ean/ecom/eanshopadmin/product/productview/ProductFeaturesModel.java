package ean.ecom.eanshopadmin.product.productview;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import java.util.List;

/**
 * Created by Shailendra (WackyCodes) on 14/09/2020 10:33
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class ProductFeaturesModel {

    private String productDetails;
    private String productDescription;

    private List<Object> specificationModelList;

    public ProductFeaturesModel(String productDetails, String productDescription, List <Object> specificationModelList) {
        this.productDetails = productDetails;
        this.productDescription = productDescription;
        this.specificationModelList = specificationModelList;
    }



    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public List <Object> getSpecificationModelList() {
        return specificationModelList;
    }

    public void setSpecificationModelList(List <Object> specificationModelList) {
        this.specificationModelList = specificationModelList;
    }
}
