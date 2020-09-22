package ean.ecom.eanshopadmin.main.shoprating;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

/**
 * Created by Shailendra (WackyCodes) on 20/09/2020 10:53
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class ShopRatingModel {

    private String userAuthID;
    private String userImage;
    private String userName;
    private int shopRatingStar;
    private String shopRatingTitle;
    private String shopRatingText;
    private String shopRatingDate;
    private String shopRatingTime;

    public ShopRatingModel(String userAuthID, String userImage, String userName, int shopRatingStar, String shopRatingTitle, String shopRatingText) {
        this.userAuthID = userAuthID;
        this.userImage = userImage;
        this.userName = userName;
        this.shopRatingStar = shopRatingStar;
        this.shopRatingTitle = shopRatingTitle;
        this.shopRatingText = shopRatingText;
    }

    public ShopRatingModel(String userAuthID, String userImage, String userName,
                           int shopRatingStar, String shopRatingTitle, String shopRatingText, String shopRatingDate, String shopRatingTime) {
        this.userAuthID = userAuthID;
        this.userImage = userImage;
        this.userName = userName;
        this.shopRatingStar = shopRatingStar;
        this.shopRatingTitle = shopRatingTitle;
        this.shopRatingText = shopRatingText;
        this.shopRatingDate = shopRatingDate;
        this.shopRatingTime = shopRatingTime;
    }

    public String getUserAuthID() {
        return userAuthID;
    }

    public void setUserAuthID(String userAuthID) {
        this.userAuthID = userAuthID;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getShopRatingStar() {
        return shopRatingStar;
    }

    public void setShopRatingStar(int shopRatingStar) {
        this.shopRatingStar = shopRatingStar;
    }

    public String getShopRatingTitle() {
        return shopRatingTitle;
    }

    public void setShopRatingTitle(String shopRatingTitle) {
        this.shopRatingTitle = shopRatingTitle;
    }

    public String getShopRatingText() {
        return shopRatingText;
    }

    public void setShopRatingText(String shopRatingText) {
        this.shopRatingText = shopRatingText;
    }

    public String getShopRatingDate() {
        return shopRatingDate;
    }

    public void setShopRatingDate(String shopRatingDate) {
        this.shopRatingDate = shopRatingDate;
    }

    public String getShopRatingTime() {
        return shopRatingTime;
    }

    public void setShopRatingTime(String shopRatingTime) {
        this.shopRatingTime = shopRatingTime;
    }
}
