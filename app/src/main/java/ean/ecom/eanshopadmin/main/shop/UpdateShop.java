package ean.ecom.eanshopadmin.main.shop;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import android.app.Dialog;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.firebase.storage.StorageReference;

import java.util.Map;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

/**
 * Created by Shailendra (WackyCodes) on 09/09/2020 19:25
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface UpdateShop {


    interface AddImageListener{
        void onAddImageSuccess( int requestCode ); // download Link
        void onAddImageFailed();
        void showToast(String msg);
    }


    interface OnImageRequest{

        interface OnImageRequestComplete{
            void onUploadImageFinished(String imageLink, boolean isSuccess);
            void onUpdateFinish(boolean isSuccess);
            void showToast(String msg);
            void dismissDialog();
        }

        void onImageUploadRequest(OnImageRequestComplete onImageRequestComplete, Uri fileUri, StorageReference storageRef);

        void onUpdateShopOnDatabase(OnImageRequestComplete onImageRequestComplete,  Map <String, Object> updateMap);


    }

    void onUpdateShopRequest(@Nullable Dialog dialog, AddImageListener updateRequestListener, int requestCode, Map <String, Object> updateMap);


}
