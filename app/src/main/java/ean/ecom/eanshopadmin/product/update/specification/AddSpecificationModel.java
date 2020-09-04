package ean.ecom.eanshopadmin.product.update.specification;

import java.util.List;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class AddSpecificationModel {
    private String spHeading;
    private List <AddSpecificationFeatureModel> specificationFeatureModelList;

    public AddSpecificationModel(String spHeading, List <AddSpecificationFeatureModel> specificationFeatureModelList) {
        this.spHeading = spHeading;
        this.specificationFeatureModelList = specificationFeatureModelList;
    }

    public String getSpHeading() {
        return spHeading;
    }

    public void setSpHeading(String spHeading) {
        this.spHeading = spHeading;
    }

    public List <AddSpecificationFeatureModel> getSpecificationFeatureModelList() {
        return specificationFeatureModelList;
    }

    public void setSpecificationFeatureModelList(List <AddSpecificationFeatureModel> specificationFeatureModelList) {
        this.specificationFeatureModelList = specificationFeatureModelList;
    }
}
