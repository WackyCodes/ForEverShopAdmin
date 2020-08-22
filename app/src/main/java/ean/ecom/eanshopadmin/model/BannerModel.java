package ean.ecom.eanshopadmin.model;

public class BannerModel {

    //...
    private int clickType;
    private String clickID;
    private String imageLink;
    private String nameOrExtraText;
    private String deleteID;

    public BannerModel(int clickType, String clickID, String imageLink, String nameOrExtraText, String deleteID) {
        this.clickType = clickType;
        this.clickID = clickID;
        this.imageLink = imageLink;
        this.nameOrExtraText = nameOrExtraText;
        this.deleteID = deleteID;
    }

    public int getClickType() {
        return clickType;
    }

    public void setClickType(int clickType) {
        this.clickType = clickType;
    }

    public String getClickID() {
        return clickID;
    }

    public void setClickID(String clickID) {
        this.clickID = clickID;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getNameOrExtraText() {
        return nameOrExtraText;
    }

    public void setNameOrExtraText(String nameOrExtraText) {
        this.nameOrExtraText = nameOrExtraText;
    }

    public String getDeleteID() {
        return deleteID;
    }

    public void setDeleteID(String deleteID) {
        this.deleteID = deleteID;
    }
}
