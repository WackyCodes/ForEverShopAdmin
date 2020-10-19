package ean.ecom.eanshopadmin.main.orderlist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shailendra (WackyCodes) on 20/10/2020 00:09
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderProductsModel implements Parcelable {

    private int cartIndex;

    private String productID;
    private int productVariant;
    private String productImage;
    private String productName;
    private String productSellingPrice;
    private String productMrpPrice;
    private String productQty;
    private String productWeight;

    public OrderProductsModel() {
    }

    public OrderProductsModel(int cartIndex, String productID, int productVariant, String productImage, String productName, String productSellingPrice, String productMrpPrice, String productQty, String productWeight) {
        this.cartIndex = cartIndex;
        this.productID = productID;
        this.productVariant = productVariant;
        this.productImage = productImage;
        this.productName = productName;
        this.productSellingPrice = productSellingPrice;
        this.productMrpPrice = productMrpPrice;
        this.productQty = productQty;
        this.productWeight = productWeight;
    }

    public int getCartIndex() {
        return cartIndex;
    }

    public void setCartIndex(int cartIndex) {
        this.cartIndex = cartIndex;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(int productVariant) {
        this.productVariant = productVariant;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSellingPrice() {
        return productSellingPrice;
    }

    public void setProductSellingPrice(String productSellingPrice) {
        this.productSellingPrice = productSellingPrice;
    }

    public String getProductMrpPrice() {
        return productMrpPrice;
    }

    public void setProductMrpPrice(String productMrpPrice) {
        this.productMrpPrice = productMrpPrice;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public  Map <String, Object> toMap(){
        Map <String, Object> map = new HashMap <>();
        map.put( "cartIndex",  cartIndex);
        map.put( "productID",  productID);
        map.put( "productVariant",  productVariant);
        map.put( "productImage",  productImage);
        map.put( "productName",  productName);
        map.put( "productSellingPrice",  productSellingPrice);
        map.put( "productMrpPrice",  productMrpPrice);
        map.put( "productQty",  productQty);
        map.put( "productWeight",  productWeight);
        return map;
    }

    public void setData( Map <String, Object> map ){
//        this.cartIndex = Integer.parseInt( map.get( "cartIndex" ).toString() );
        this.productID =  map.get( "productID" ).toString();
        this.productVariant = Integer.parseInt( map.get( "productVariant" ).toString() );
        this.productImage =  map.get( "productImage" ).toString();
        this.productName =  map.get( "productName" ).toString();
        this.productSellingPrice =  map.get( "productSellingPrice" ).toString();
        this.productMrpPrice =  map.get( "productMrpPrice" ).toString();
        this.productQty =  map.get( "productQty" ).toString();
        this.productWeight =  map.get( "productWeight" ).toString();
    }


    protected OrderProductsModel(Parcel in) {
        cartIndex = in.readInt();
        productID = in.readString();
        productVariant = in.readInt();
        productImage = in.readString();
        productName = in.readString();
        productSellingPrice = in.readString();
        productMrpPrice = in.readString();
        productQty = in.readString();
        productWeight = in.readString();
    }

    public static final Creator <OrderProductsModel> CREATOR = new Creator <OrderProductsModel>() {
        @Override
        public OrderProductsModel createFromParcel(Parcel in) {
            return new OrderProductsModel( in );
        }

        @Override
        public OrderProductsModel[] newArray(int size) {
            return new OrderProductsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt( cartIndex );
        dest.writeString( productID );
        dest.writeInt( productVariant );
        dest.writeString( productImage );
        dest.writeString( productName );
        dest.writeString( productSellingPrice );
        dest.writeString( productMrpPrice );
        dest.writeString( productQty );
        dest.writeString( productWeight );
    }
}
