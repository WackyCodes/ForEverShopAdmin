package ean.ecom.eanshopadmin.launching;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import ean.ecom.eanshopadmin.MainActivity;
import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.other.DialogsClass;

import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.launching.AuthActivity.authActivity;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_CAT_LIST_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

public class ShopSetUpFragment extends Fragment {

    public ShopSetUpFragment() {
        // Required empty public constructor
    }

    private FrameLayout shopSetUpFrame;

    private Dialog dialog;

    private EditText ownerName;
    private EditText shopHelpline;
    private EditText shopEmail;
    private EditText shopAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_shop_set_up, container, false );
        shopSetUpFrame = view.findViewById( R.id.shop_set_up_fragment_frame );
        dialog = DialogsClass.getDialog( getContext() );

        ownerName = view.findViewById( R.id.shop_owner_name );
        shopHelpline = view.findViewById( R.id.shop_helpline_number );
        shopEmail = view.findViewById( R.id.shop_email );
        shopAddress = view.findViewById( R.id.shop_address );
        Button updateContinueBtn = view.findViewById( R.id.update_and_continue_to_main_btn );

//        setHomePageSetup(view.getContext());

        updateContinueBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidData()){
                    dialog.show();
                    updateShopInfo(view.getContext());
                }
            }
        } );

        return view;
    }

    private boolean isValidData(){

        if (TextUtils.isEmpty( ownerName.getText().toString() )){
            ownerName.setError( "Required Field!" );
            return false;
        }else if (TextUtils.isEmpty( shopHelpline.getText().toString() )){
            shopHelpline.setError( "Required Field!" );
            return false;
        }else if (shopHelpline.getText().toString().length() < 10){
            shopHelpline.setError( "Incorrect Phone Number!" );
            return false;
        }else if (TextUtils.isEmpty( shopAddress.getText().toString() )){
            shopAddress.setError( "Required Field!" );
            return false;
        }else if (!isValidEmail( shopEmail )){
            return false;
        }else{
            return true;
        }
    }

    private boolean isValidEmail( EditText wReference ){
        String wEmail = wReference.getText().toString().trim();
        String emailRegex =
                "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        boolean bool = pat.matcher(wEmail).matches();

        if (TextUtils.isEmpty( wEmail )) {
            wReference.setError( "Please Enter Email! " );
            return false;
        } else if (!bool){
            wReference.setError( "Please Enter Valid Email! " );
            return false;
        }

        return true;
    }

    private void setHomePageSetup(final Context context){
        Map<String, Object> setUpMap = new HashMap <>();
        setUpMap.put( "type", SHOP_HOME_CAT_LIST_CONTAINER );
        setUpMap.put( "layout_id", "cat_layout" );
        setUpMap.put( "visibility", true );
        setUpMap.put( "no_of_cat", 0 );
        setUpMap.put( "index", 0 );

        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( "HOME" )
                .document( "cat_layout" )
                .set( setUpMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()){
                            Intent intent = new Intent( context, MainActivity.class );
                            context.startActivity( intent );
//                            getActivity().finish();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!authActivity.isDestroyed())
                                    authActivity.finish();
                            }
                        }else{

                        }
                    }
                } );

    }

    // Update Brief information of shop...
    private void updateShopInfo(final Context context){

        Map<String, Object> updateMap = new HashMap <>();
        updateMap.put( "shop_address", shopAddress.getText().toString() );
        updateMap.put( "shop_owner_name", ownerName.getText().toString() );
        updateMap.put( "shop_owner_mobile", shopHelpline.getText().toString() );
        updateMap.put( "shop_owner_email", shopEmail.getText().toString() );
        // add into DataModel...
        ADMIN_DATA_MODEL.setShopAddress( shopAddress.getText().toString() );

        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
//                        dialog.dismiss();
                        if (task.isSuccessful()){
                            // Home SetUp...
                            setHomePageSetup(context);

                        }else{
                            showToast(context, "Failed Update..!");
                        }
                    }
                } );

    }

     /*
        shopMap.put( "shop_address", " " );
        shopMap.put( "shop_landmark", "" );
        shopMap.put( "shop_map_latitude", "" );
        shopMap.put( "shop_map_longitude", "" );
        shopMap.put( "shop_name",sName ); // Not Required
        shopMap.put( "shop_owner_address", "" );
        shopMap.put( "shop_owner_name", "" );
        shopMap.put( "shop_owner_mobile", "" );
        shopMap.put( "shop_owner_email", "" );
        shopMap.put( "shop_image", "" ); // Images
        shopMap.put( "shop_logo", "" ); // Images
        shopMap.put( "tags", "" );
     */


}
