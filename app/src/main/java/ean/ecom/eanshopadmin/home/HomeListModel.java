package ean.ecom.eanshopadmin.home;

import java.util.List;

import ean.ecom.eanshopadmin.model.BannerModel;
import ean.ecom.eanshopadmin.product.ProductModel;

public class HomeListModel {

    private int layoutType;
    private String layoutID;
    private Boolean isVisible;

    // --------------------------------
    public HomeListModel(int layoutType, String layoutID) {
        this.layoutType = layoutType;
        this.layoutID = layoutID;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(String layoutID) {
        this.layoutID = layoutID;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    // --------------- Banner Slider... and Category..-----------------

    private List<BannerModel> bannerModelList;

    public HomeListModel(int layoutType, String layoutID, Boolean isVisible, List <BannerModel> bannerModelList) {
        this.layoutType = layoutType;
        this.layoutID = layoutID;
        this.isVisible = isVisible;
        this.bannerModelList = bannerModelList;
    }

    public List <BannerModel> getBannerModelList() {
        return bannerModelList;
    }

    public void setBannerModelList(List <BannerModel> bannerModelList) {
        this.bannerModelList = bannerModelList;
    }

    // --------------- Banner/Strip Ad ...-----------------

    private BannerModel bannerModel;

    public HomeListModel(int layoutType, String layoutID, BannerModel bannerModel) {
        this.layoutType = layoutType;
        this.layoutID = layoutID;
        this.bannerModel = bannerModel;
    }

    public BannerModel getBannerModel() {
        return bannerModel;
    }

    public void setBannerModel(BannerModel bannerModel) {
        this.bannerModel = bannerModel;
    }

    // --------------- Product Layout ...-----------------

    private List<String> productIdList;
    // TODO : Product List...
    private String productLayoutTitle;
    private List<ProductModel> productModelList;


    public HomeListModel(int layoutType, String layoutID, String productLayoutTitle, List <String> productIdList, List<ProductModel> productModelList ) {
        this.layoutType = layoutType;
        this.layoutID = layoutID;
        this.productLayoutTitle = productLayoutTitle;
        this.productIdList = productIdList;
        this.productModelList = productModelList;
    }

    public String getProductLayoutTitle() {
        return productLayoutTitle;
    }

    public void setProductLayoutTitle(String productLayoutTitle) {
        this.productLayoutTitle = productLayoutTitle;
    }

    public List <String> getProductIdList() {
        return productIdList;
    }

    public void setProductIdList(List <String> productIdList) {
        this.productIdList = productIdList;
    }

    public List <ProductModel> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List <ProductModel> productModelList) {
        this.productModelList = productModelList;
    }
}
