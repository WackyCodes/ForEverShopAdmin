package ean.ecom.eanshopadmin.product.update;

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

/**
 * Created by Shailendra (WackyCodes) on 30/08/2020 10:09
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface UpdateData {

    interface UpdateRequest{
        interface OnUpdateFinisher{
            void onUpdateFinished(boolean isSuccess);
            void dismissDialog();
        }
        void onUpdateRequest(OnUpdateFinisher onUpdateFinisher, String productID, Map <String, Object> updateMap);
    }

    interface ImageListUpdateRequest{
        void onAddImageRequest();
        void onRemoveImageRequest(int index);
    }

    interface UpdateImageOnServerRequest{
        interface OnUploadImageFinisher{
            void onUploadImageFinished( String downloadLink );
            void dismissDialog();
            void showToast(String msg);
        }
        void onImageUploadRequest( OnUploadImageFinisher onUploadImageFinisher, Uri fileUri,  StorageReference storageRef );

    }


}
