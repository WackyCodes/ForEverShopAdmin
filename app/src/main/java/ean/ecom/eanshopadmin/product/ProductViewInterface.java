package ean.ecom.eanshopadmin.product;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

/**
 * Created by Shailendra (WackyCodes) on 30/08/2020 09:50
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface ProductViewInterface {

    void onUpdateCompleted(int requestCode, boolean isSuccess);

    void showToastMessage(String msg);

}
