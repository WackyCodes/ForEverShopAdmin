package ean.ecom.eanshopadmin.admin;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class AdminDataModel {

    private int adminCode;

    private String adminEmail;
    private String adminMobile;
    private String adminPhoto;
    private String adminName;

    private String shopID;
    private String shopName;
    private String shopAddress;
    private String shopCategory;
    private String shopTagLine;

    private String[] shopCategories;

    private int shopVegNonCode;
    private boolean isServiceAvailable;
    private boolean isOpen;
    private String shopOpenTime;
    private String shopCloseTime;
    private String shopLogo;
    private String shopImage;

    private String shopRatingStars;
    private String shopRatingPeoples;
    private int verifyCode;

    private String shopAreaCode;
    private String shopAreaName;
    private String shopCity;
    private String shopCityCode;
    private String shopLandMark;

    private List<Boolean> shopDaysSchedule;

    // Contacts...
    private String shopOwnerName;
    private String shopOwnerAddress;
    private String shopOwnerMobile;
    private String shopOwnerEmail;
    private GeoPoint shopGeoPoint;

    private String shopHelpLine;
    private String shopEmail;
    private String shopWebsite;

    // Social Media Accounts...
    private String shopFacebook;
    private String shopInstagram;
    private String shopTwitter;

    // Licence...
    private boolean isLicenceAvailable;
    private int shopLicenceType;
    private String shopLicenceNumber;


    public AdminDataModel() {
    }

    public AdminDataModel(int adminCode, String adminEmail, String adminMobile, String adminPhoto) {
        this.adminCode = adminCode;
        this.adminEmail = adminEmail;
        this.adminMobile = adminMobile;
        this.adminPhoto = adminPhoto;
    }

    public int getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(int adminCode) {
        this.adminCode = adminCode;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile;
    }

    public String getAdminPhoto() {
        return adminPhoto;
    }

    public void setAdminPhoto(String adminPhoto) {
        this.adminPhoto = adminPhoto;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(String shopCategory) {
        this.shopCategory = shopCategory;
    }

    public String getShopTagLine() {
        return shopTagLine;
    }

    public void setShopTagLine(String shopTagLine) {
        this.shopTagLine = shopTagLine;
    }

    public String[] getShopCategories() {
        return shopCategories;
    }

    public void setShopCategories(String[] shopCategories) {
        this.shopCategories = shopCategories;
    }

    public int getShopVegNonCode() {
        return shopVegNonCode;
    }

    public void setShopVegNonCode(int shopVegNonCode) {
        this.shopVegNonCode = shopVegNonCode;
    }

    public boolean isServiceAvailable() {
        return isServiceAvailable;
    }

    public void setServiceAvailable(boolean serviceAvailable) {
        isServiceAvailable = serviceAvailable;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getShopOpenTime() {
        return shopOpenTime;
    }

    public void setShopOpenTime(String shopOpenTime) {
        this.shopOpenTime = shopOpenTime;
    }

    public String getShopCloseTime() {
        return shopCloseTime;
    }

    public void setShopCloseTime(String shopCloseTime) {
        this.shopCloseTime = shopCloseTime;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopRatingStars() {
        return shopRatingStars;
    }

    public void setShopRatingStars(String shopRatingStars) {
        this.shopRatingStars = shopRatingStars;
    }

    public String getShopRatingPeoples() {
        return shopRatingPeoples;
    }

    public void setShopRatingPeoples(String shopRatingPeoples) {
        this.shopRatingPeoples = shopRatingPeoples;
    }

    public int getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(int verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getShopAreaCode() {
        return shopAreaCode;
    }

    public void setShopAreaCode(String shopAreaCode) {
        this.shopAreaCode = shopAreaCode;
    }

    public String getShopAreaName() {
        return shopAreaName;
    }

    public void setShopAreaName(String shopAreaName) {
        this.shopAreaName = shopAreaName;
    }

    public String getShopCity() {
        return shopCity;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopCityCode() {
        return shopCityCode;
    }

    public void setShopCityCode(String shopCityCode) {
        this.shopCityCode = shopCityCode;
    }

    public String getShopLandMark() {
        return shopLandMark;
    }

    public void setShopLandMark(String shopLandMark) {
        this.shopLandMark = shopLandMark;
    }

    public List <Boolean> getShopDaysSchedule() {
        return shopDaysSchedule;
    }

    public void setShopDaysSchedule(List <Boolean>  shopDaysSchedule) {
        this.shopDaysSchedule = shopDaysSchedule;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getShopOwnerAddress() {
        return shopOwnerAddress;
    }

    public void setShopOwnerAddress(String shopOwnerAddress) {
        this.shopOwnerAddress = shopOwnerAddress;
    }

    public String getShopOwnerMobile() {
        return shopOwnerMobile;
    }

    public void setShopOwnerMobile(String shopOwnerMobile) {
        this.shopOwnerMobile = shopOwnerMobile;
    }

    public String getShopOwnerEmail() {
        return shopOwnerEmail;
    }

    public void setShopOwnerEmail(String shopOwnerEmail) {
        this.shopOwnerEmail = shopOwnerEmail;
    }

    public String getShopHelpLine() {
        return shopHelpLine;
    }

    public void setShopHelpLine(String shopHelpLine) {
        this.shopHelpLine = shopHelpLine;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getShopWebsite() {
        return shopWebsite;
    }

    public void setShopWebsite(String shopWebsite) {
        this.shopWebsite = shopWebsite;
    }

    public String getShopFacebook() {
        return shopFacebook;
    }

    public void setShopFacebook(String shopFacebook) {
        this.shopFacebook = shopFacebook;
    }

    public String getShopInstagram() {
        return shopInstagram;
    }

    public void setShopInstagram(String shopInstagram) {
        this.shopInstagram = shopInstagram;
    }

    public String getShopTwitter() {
        return shopTwitter;
    }

    public void setShopTwitter(String shopTwitter) {
        this.shopTwitter = shopTwitter;
    }

    public boolean isLicenceAvailable() {
        return isLicenceAvailable;
    }

    public void setLicenceAvailable(boolean licenceAvailable) {
        isLicenceAvailable = licenceAvailable;
    }

    public int getShopLicenceType() {
        return shopLicenceType;
    }

    public GeoPoint getShopGeoPoint() {
        return shopGeoPoint;
    }

    public void setShopGeoPoint(GeoPoint shopGeoPoint) {
        this.shopGeoPoint = shopGeoPoint;
    }

    public void setShopLicenceType(int shopLicenceType) {
        this.shopLicenceType = shopLicenceType;
    }

    public String getShopLicenceNumber() {
        return shopLicenceNumber;
    }

    public void setShopLicenceNumber(String shopLicenceNumber) {
        this.shopLicenceNumber = shopLicenceNumber;
    }
}
