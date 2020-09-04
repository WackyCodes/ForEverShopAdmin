package ean.ecom.eanshopadmin.product;

import java.util.List;

import ean.ecom.eanshopadmin.product.update.specification.AddSpecificationModel;

public class ProductSubModel {

    private String pName;
    private List<String> pImage;
    private String pSellingPrice;
    private String pMrpPrice;
    private String pWeight;
    private String pStocks;
    private String pOffer;
    // Specification List...
    private List<AddSpecificationModel> pSpecificationList;

    public ProductSubModel(String pName, List<String> pImage, String pSellingPrice, String pMrpPrice, String pWeight, String pStocks, String pOffer) {
        this.pName = pName;
        this.pImage = pImage;
        this.pSellingPrice = pSellingPrice;
        this.pMrpPrice = pMrpPrice;
        this.pWeight = pWeight;
        this.pStocks = pStocks;
        this.pOffer = pOffer;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public List<String> getpImage() {
        return pImage;
    }

    public void setpImage(List<String> pImage) {
        this.pImage = pImage;
    }

    public String getpSellingPrice() {
        return pSellingPrice;
    }

    public void setpSellingPrice(String pSellingPrice) {
        this.pSellingPrice = pSellingPrice;
    }

    public String getpMrpPrice() {
        return pMrpPrice;
    }

    public void setpMrpPrice(String pMrpPrice) {
        this.pMrpPrice = pMrpPrice;
    }

    public String getpWeight() {
        return pWeight;
    }

    public void setpWeight(String pWeight) {
        this.pWeight = pWeight;
    }

    public String getpStocks() {
        return pStocks;
    }

    public void setpStocks(String pStocks) {
        this.pStocks = pStocks;
    }

    public String getpOffer() {
        return pOffer;
    }

    public void setpOffer(String pOffer) {
        this.pOffer = pOffer;
    }

    public List <AddSpecificationModel> getpSpecificationList() {
        return pSpecificationList;
    }

    public void setpSpecificationList(List <AddSpecificationModel> pSpecificationList) {
        this.pSpecificationList = pSpecificationList;
    }
}
