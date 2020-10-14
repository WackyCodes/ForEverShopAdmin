package ean.ecom.eanshopadmin.notification;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class NotificationModel {

    private int type;

    public NotificationModel(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /// Simple Notification...

    // 1. Type..
    private String notifyID;
    private String notifyClickID;
    private String notifyImage;
    private String notifyTitle;
    private String notifyBody;
    private String notifyDate;
    private String notifyTime;
    private Boolean notifyIsRead;
    private String notifyOtherText;

    public NotificationModel(int type, String notifyID, String notifyClickID, String notifyImage, String notifyTitle, String notifyBody, String notifyDate, String notifyTime, Boolean notifyIsRead) {
        this.type = type;
        this.notifyID = notifyID;
        this.notifyClickID = notifyClickID;
        this.notifyImage = notifyImage;
        this.notifyTitle = notifyTitle;
        this.notifyBody = notifyBody;
        this.notifyDate = notifyDate;
        this.notifyTime = notifyTime;
        this.notifyIsRead = notifyIsRead;
    }

    public String getNotifyID() {
        return notifyID;
    }

    public void setNotifyID(String notifyID) {
        this.notifyID = notifyID;
    }

    public String getNotifyClickID() {
        return notifyClickID;
    }

    public void setNotifyClickID(String notifyClickID) {
        this.notifyClickID = notifyClickID;
    }

    public String getNotifyImage() {
        return notifyImage;
    }

    public void setNotifyImage(String notifyImage) {
        this.notifyImage = notifyImage;
    }

    public String getNotifyTitle() {
        return notifyTitle;
    }

    public void setNotifyTitle(String notifyTitle) {
        this.notifyTitle = notifyTitle;
    }

    public String getNotifyBody() {
        return notifyBody;
    }

    public void setNotifyBody(String notifyBody) {
        this.notifyBody = notifyBody;
    }

    public String getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(String notifyDate) {
        this.notifyDate = notifyDate;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Boolean getNotifyIsRead() {
        return notifyIsRead;
    }

    public void setNotifyIsRead(Boolean notifyIsRead) {
        this.notifyIsRead = notifyIsRead;
    }

    public String getNotifyOtherText() {
        return notifyOtherText;
    }

    public void setNotifyOtherText(String notifyOtherText) {
        this.notifyOtherText = notifyOtherText;
    }
}
