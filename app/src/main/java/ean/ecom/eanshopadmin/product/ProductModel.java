package ean.ecom.eanshopadmin.product;

import java.util.List;

public class ProductModel {
    // Product ID
    private String pProductID;

    private String pMainName;
    private String pDetails;
    private Boolean pIsCOD;
    private String pNumOfProducts;
    private String pWeightType;
    private int pVegNonType;

    // Sub Model...
    private List <ProductSubModel> productSubModelList;

    public ProductModel(String pProductID, String pMainName, String pDetails, Boolean pIsCOD, String pNumOfProducts, String pWeightType, int pVegNonType, List <ProductSubModel> productSubModelList) {
        this.pProductID = pProductID;
        this.pMainName = pMainName;
        this.pDetails = pDetails;
        this.pIsCOD = pIsCOD;
        this.pNumOfProducts = pNumOfProducts;
        this.pWeightType = pWeightType;
        this.pVegNonType = pVegNonType;
        this.productSubModelList = productSubModelList;
    }

    public String getpProductID() {
        return pProductID;
    }

    public void setpProductID(String pProductID) {
        this.pProductID = pProductID;
    }

    public String getpMainName() {
        return pMainName;
    }

    public void setpMainName(String pMainName) {
        this.pMainName = pMainName;
    }

    public String getpDetails() {
        return pDetails;
    }

    public void setpDetails(String pDetails) {
        this.pDetails = pDetails;
    }

    public Boolean getpIsCOD() {
        return pIsCOD;
    }

    public void setpIsCOD(Boolean pIsCOD) {
        this.pIsCOD = pIsCOD;
    }

    public String getpNumOfProducts() {
        return pNumOfProducts;
    }

    public void setpNumOfProducts(String pNumOfProducts) {
        this.pNumOfProducts = pNumOfProducts;
    }

    public String getpWeightType() {
        return pWeightType;
    }

    public void setpWeightType(String pWeightType) {
        this.pWeightType = pWeightType;
    }

    public int getpVegNonType() {
        return pVegNonType;
    }

    public void setpVegNonType(int pVegNonType) {
        this.pVegNonType = pVegNonType;
    }

    public List <ProductSubModel> getProductSubModelList() {
        return productSubModelList;
    }

    public void setProductSubModelList(List <ProductSubModel> productSubModelList) {
        this.productSubModelList = productSubModelList;
    }
}
