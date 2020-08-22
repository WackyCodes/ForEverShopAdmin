package ean.ecom.eanshopadmin.home;

import java.util.List;

public class HomeCatListModel {

    private String catID;
    private String catName;

    private List<HomeListModel> homeListModelList;

    public HomeCatListModel(String catID, String catName, List<HomeListModel> homeListModelList) {
        this.catID = catID;
        this.catName = catName;
        this.homeListModelList = homeListModelList;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public List <HomeListModel> getHomeListModelList() {
        return homeListModelList;
    }

    public void setHomeListModelList(List <HomeListModel> homeListModelList) {
        this.homeListModelList = homeListModelList;
    }
}
