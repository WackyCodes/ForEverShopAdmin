package ean.ecom.eanshopadmin.launching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import ean.ecom.eanshopadmin.MainActivity;
import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.other.CheckInternetConnection;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.StaticMethods;
import ean.ecom.eanshopadmin.other.StaticValues;

import static ean.ecom.eanshopadmin.database.DBQuery.currentUser;
import static ean.ecom.eanshopadmin.database.DBQuery.firebaseAuth;
import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;
import static ean.ecom.eanshopadmin.other.StaticValues.STORAGE_PERMISSION;

public class WelcomeActivity extends AppCompatActivity {
    public static AppCompatActivity welcomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_welcome );
        welcomeActivity = this;
        if( CheckInternetConnection.isInternetConnected( this ) ){ // CheckInternetConnection.isInternetConnected( this )
            firebaseFirestore.collection( "PERMISSION" ).document( "APP_USE_PERMISSION" )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        boolean isAllowed = task.getResult().getBoolean( StaticValues.APP_VERSION );
                        if ( isAllowed ){
                            askStoragePermission();
//                            checkCurrentUser();
                        }else{
                            DialogsClass.getMessageDialog( WelcomeActivity.this
                                    , "Sorry, Permission denied..!"
                                    , "You Don't have permission to use this App. If you have any query, Please contact App founder..!\\n Thank You!" );
                            finish();
                        }
                    }else {
                        showToast( "Failed..!" );
                        finish();
                    }
                }
            } );
        }

    }

    private void checkCurrentUser(){
        // Load Area List...
//        DBQuery.getCityListQuery(); // Not Required!
        // Load Shop List.. > In main Activity...
        if (currentUser != null){
            String userMobile = StaticMethods.readFileFromLocal(this, "mobile" );
            String shopID = StaticMethods.readFileFromLocal(this, "shop" );

            if (userMobile != null && shopID != null){
                SHOP_ID = shopID.trim();
                ADMIN_DATA_MODEL.setAdminMobile( userMobile.trim() );
                checkAdminPermission();
            }else{
                firebaseAuth.signOut();
                startActivity( new Intent( WelcomeActivity.this, AuthActivity.class ) );
                finish();
            }

        }else{
            startActivity( new Intent( WelcomeActivity.this, AuthActivity.class ) );
            finish();
        }

    }

    int round = 0;
    private void checkAdminPermission(  ){
        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .collection( "ADMINS" ).document( ADMIN_DATA_MODEL.getAdminMobile() )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
//                    boolean is_allowed = (boolean)task.getResult().get( "is_allowed" );
                    if ( documentSnapshot.get( "is_allowed" )!= null &&  (boolean)documentSnapshot.get( "is_allowed" )){
//                        String admin_address = task.getResult().get( "admin_address" ).toString();
                        String admin_email = documentSnapshot.get( "admin_email" ).toString();
                        String admin_code = documentSnapshot.get( "admin_code" ).toString();
                        String admin_name = documentSnapshot.get( "admin_name" ).toString();
                        String admin_photo = documentSnapshot.get( "admin_photo" ).toString();

                        ADMIN_DATA_MODEL.setAdminEmail( admin_email );
                        ADMIN_DATA_MODEL.setAdminCode( Integer.parseInt( admin_code ) );
                        ADMIN_DATA_MODEL.setAdminName( admin_name );
                        ADMIN_DATA_MODEL.setAdminPhoto( admin_photo );
                        // Load Shop Data...
                        DBQuery.getShopData( SHOP_ID );
                        // Start Activity ..
                        Intent intent = new Intent( WelcomeActivity.this, MainActivity.class );
                        startActivity( intent );
                        finish();

                    }else{
                        if (documentSnapshot.get( "is_allowed" )== null){
                            showToast( "Something went Wrong! Failed!" );
                            finish();
//                            if (round < 3)
//                                checkAdminPermission();
//                            else{
////                            }
//                            round++;
                        }else{
                            deniedDialog();
                        }
                    }
                }else{
                    deniedDialog();
                }
            }
        } );

    }

    private void deniedDialog(){
        firebaseAuth.signOut();
        DialogsClass.alertDialog( WelcomeActivity.this, "Permission denied!", "You have not permission to use this app." );
    }

    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }
    /// Storage Permission...
    public void askStoragePermission(){
        if(ContextCompat.checkSelfPermission( WelcomeActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission( WelcomeActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
            checkCurrentUser();
        }else {
            requestStoragePermission();
        }
    }
    private void requestStoragePermission(){

        if(ActivityCompat.shouldShowRequestPermissionRationale( this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )
                || ActivityCompat.shouldShowRequestPermissionRationale( this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE )){

            new AlertDialog.Builder( this )
                    .setTitle( "Storage Permission" )
                    .setMessage( "Storage permission is needed, because of File Storage will be required!" )
                    .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions( WelcomeActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    }, STORAGE_PERMISSION );
                        }
                    } ).setNegativeButton( "Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
//                    requestStoragePermission();
                    finish();
                }
            } ).create().show();
        }else{
            ActivityCompat.requestPermissions( WelcomeActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, STORAGE_PERMISSION );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== STORAGE_PERMISSION){
            if(grantResults.length>0
                    && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED
            ){
                showToast( "Permission is GRANTED..." );
                checkCurrentUser();
            }
            else{
                showToast( "Permission DENIED!" );
                requestStoragePermission();
            }
        }
    }



}
