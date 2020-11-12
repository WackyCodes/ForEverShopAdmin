package ean.ecom.eanshopadmin.notification;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.eanshopadmin.other.StaticMethods;

/**
 * Created by Shailendra (WackyCodes) on 21/10/2020 00:26
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class UserNotificationModel {


    private String index;

    private int notify_type;
    private String notify_id;
    private String notify_click_id;
    private String notify_image;
    private String notify_title;
    private String notify_body;

    private String notify_date;
    private String notify_time;
    private Timestamp notify_timestamp;
    private boolean notify_is_read;

    public UserNotificationModel() {
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(int notify_type) {
        this.notify_type = notify_type;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getNotify_click_id() {
        return notify_click_id;
    }

    public void setNotify_click_id(String notify_click_id) {
        this.notify_click_id = notify_click_id;
    }

    public String getNotify_image() {
        return notify_image;
    }

    public void setNotify_image(String notify_image) {
        this.notify_image = notify_image;
    }

    public String getNotify_title() {
        return notify_title;
    }

    public void setNotify_title(String notify_title) {
        this.notify_title = notify_title;
    }

    public String getNotify_body() {
        return notify_body;
    }

    public void setNotify_body(String notify_body) {
        this.notify_body = notify_body;
    }

    public String getNotify_date() {
        return notify_date;
    }

    public void setNotify_date(String notify_date) {
        this.notify_date = notify_date;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public boolean isNotify_is_read() {
        return notify_is_read;
    }

    public void setNotify_is_read(boolean notify_is_read) {
        this.notify_is_read = notify_is_read;
    }

    public Timestamp getNotify_timestamp() {
        return notify_timestamp;
    }

    public void setNotify_timestamp(Timestamp notify_timestamp) {
        this.notify_timestamp = notify_timestamp;
    }

    public Map <String, Object> getMap(){
        Map <String, Object> notifyMap = new HashMap <>();

        notifyMap.put( "index",  StaticMethods.getRandomIndex() );

        notifyMap.put( "notify_type",  getNotify_type() );
        notifyMap.put( "notify_id",  getNotify_id() );
        notifyMap.put( "notify_click_id",  getNotify_click_id() );
        notifyMap.put( "notify_image",  getNotify_image() );
        notifyMap.put( "notify_title",  getNotify_title() );
        notifyMap.put( "notify_body",  getNotify_body() );

//        notifyMap.put( "notify_date", StaticMethods.getCrrDate() );
//        notifyMap.put( "notify_time", StaticMethods.getCrrTime() );
        notifyMap.put( "notify_timestamp", StaticMethods.getCrrTimeStamp() );
        notifyMap.put( "notify_is_read", false );

        return notifyMap;
    }


}
