package ean.ecom.eanshopadmin.admin;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shailendra (WackyCodes) on 01/11/2020 02:16
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class AdminDataModel implements Parcelable {

    private int adminCode;
    private String adminEmail;
    private String adminMobile;
    private String adminPhoto;
    private String adminName;

    public AdminDataModel() {
    }

    protected AdminDataModel(Parcel in) {
        adminCode = in.readInt();
        adminEmail = in.readString();
        adminMobile = in.readString();
        adminPhoto = in.readString();
        adminName = in.readString();
    }

    public static final Creator <AdminDataModel> CREATOR = new Creator <AdminDataModel>() {
        @Override
        public AdminDataModel createFromParcel(Parcel in) {
            return new AdminDataModel( in );
        }

        @Override
        public AdminDataModel[] newArray(int size) {
            return new AdminDataModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt( adminCode );
        dest.writeString( adminEmail );
        dest.writeString( adminMobile );
        dest.writeString( adminPhoto );
        dest.writeString( adminName );
    }
}
