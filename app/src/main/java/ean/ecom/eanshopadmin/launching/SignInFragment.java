package ean.ecom.eanshopadmin.launching;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.eanshopadmin.MainActivity;
import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.database.AdminQuery;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.StaticMethods;

import static ean.ecom.eanshopadmin.database.DBQuery.currentUser;
import static ean.ecom.eanshopadmin.database.DBQuery.firebaseAuth;
import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.launching.AuthActivity.authActivity;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

public class SignInFragment extends Fragment {

    private FrameLayout parentFrameLayout;
    private Dialog dialog;

    // ----
    private EditText signInShopID;
    private ImageView verifyShop;
    private EditText signInMobile;
    private ImageView verifyMobile;

    private ConstraintLayout signInLayout;
    private EditText signInPassword;
    private ImageView visiblePassIcon;
    private TextView signInForgetPassword;

    private LinearLayout createPassLayout;
    private EditText signUpPass1;
    private EditText signUpPass2;
    //---------
    private EditText signInEmail;

    private Button signInUpBtn;
    //---------

    private ScrollView signInUpScrollView;
    private LinearLayout shopSetUpLayout;
    private TextView homePageSetupBtn;

    public SignInFragment() {
        // Required empty public constructor
    }

    private String adminEmail = null;
    private boolean isAuth = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_sign_in, container, false );

        parentFrameLayout = view.findViewById( R.id.sign_in_frameLayout);
        dialog = DialogsClass.getDialog( getContext() );

        signInShopID = view.findViewById( R.id.sign_in_shop_id );
        verifyShop = view.findViewById( R.id.verify_shop_image );
        signInMobile = view.findViewById( R.id.sign_in_mobile );
        verifyMobile = view.findViewById( R.id.verify_mobile_image );

        signInLayout = view.findViewById( R.id.password_const_layout );
        signInPassword = view.findViewById( R.id.sign_in_password );
        visiblePassIcon = view.findViewById( R.id.visible_pass_icon );
        signInForgetPassword = view.findViewById( R.id.sign_in_forget_password );

        signInEmail = view.findViewById( R.id.sign_in_email );

        createPassLayout = view.findViewById( R.id.create_pass_layout );
        signUpPass1 = view.findViewById( R.id.sign_up_password_1 );
        signUpPass2 = view.findViewById( R.id.sign_up_password_2 );

        signInUpBtn = view.findViewById( R.id.sign_in_up_btn );

        // After SignUp SignIn...
        signInUpScrollView = view.findViewById( R.id.sign_in_up_scroll_view );
        shopSetUpLayout = view.findViewById( R.id.home_page_set_up_layout );
        homePageSetupBtn = view.findViewById( R.id.home_page_set_up_text );
        signInUpScrollView.setVisibility( View.VISIBLE );
        shopSetUpLayout.setVisibility( View.GONE );

        // Default Visibility...
        verifyShop.setVisibility( View.INVISIBLE );
        signInLayout.setVisibility( View.GONE );
        verifyMobile.setVisibility( View.INVISIBLE );
        signInForgetPassword.setVisibility( View.GONE );
        createPassLayout.setVisibility( View.GONE );

        // Check Text Watcher...
        textWatcher( view );

        // OnClick...
        signInUpBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                if ( isDataFilled() ){
                    if (isValid()){
                        if (adminEmail != null){
                            if (isAuth){
                                // Login...
                                adminLogIn( view.getContext(), adminEmail, signInPassword.getText().toString() );
                            }else{
                                // Sign In... Authenticate...
                                adminSignIn( view.getContext(), adminEmail, signUpPass1.getText().toString() );
                            }
                        }else{
                            dialog.dismiss();
                            DialogsClass.alertDialog( getContext(), "Alert!", "This Mobile Number is not Registered Yet!" ).show();
                        }
                    }else{
                        dialog.dismiss();
                    }
                }else{
                    dialog.dismiss();
                }
            }
        } );

        signInForgetPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if ( isDataFilled() ){
                    AlertDialog.Builder builder = DialogsClass.alertDialog( view.getContext(), "Reset password?", "To reset your password, we will send an email on your registered email. Click on \'Get Email\'" );
                    builder.setPositiveButton( "Get Email", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            dialog.show();
                            getMailToResetPassword(view.getContext(), adminEmail);
                        }
                    } );
                    builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    } );
                    builder.show();
                }else{
                    dialog.dismiss();
                }
            }
        } );

        // Home Page SetUp > Continue..>> Btn Click
        homePageSetupBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set Up Home Page....
                setFragment( new ShopSetUpFragment( signInShopID.getText().toString().trim(), signInMobile.getText().toString().trim() ) );
            }
        } );

        // Visibility...
        visiblePassIcon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signInPassword.getTransformationMethod().equals( PasswordTransformationMethod.getInstance() )){
                    // hide password...
                    signInPassword.setTransformationMethod( HideReturnsTransformationMethod.getInstance() );
                    visiblePassIcon.setImageResource( R.drawable.ic_visibility_off_black_24dp );
                }else{
                    // Show Password...
                    signInPassword.setTransformationMethod( PasswordTransformationMethod.getInstance() );
                    visiblePassIcon.setImageResource( R.drawable.ic_visibility_black_24dp );
                }
            }
        } );

        return view;
    }

    private void textWatcher(final View view){
        // Shop ID text Watcher...
        signInShopID.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() == 9){
                    // Process To Check on server...
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signInShopID.setBackgroundTintList( view.getContext().getResources().getColorStateList( R.color.colorGreen ) );
                    }
                    // Check For existence....
                    checkShopExist( view.getContext(), charSequence.toString().trim() );
                    // If Shop Id Changes...
                    if ( !TextUtils.isEmpty( signInMobile.getText().toString() )){
                        dialog.show();
                        // Check For existence....
                        checkUserExist( view.getContext(), signInShopID.getText().toString(), signInMobile.getText().toString() );
                    }

                }else{
                    verifyShop.setVisibility( View.INVISIBLE );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signInShopID.setBackgroundTintList( view.getContext().getResources().getColorStateList( R.color.colorRed ) );
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );
        // Mobile TextWatcher...
        signInMobile.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 10 ){
                    // Process To Check on server...
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signInMobile.setBackgroundTintList( view.getContext().getResources().getColorStateList( R.color.colorGreen ) );
                    }
                    if ( !TextUtils.isEmpty( signInShopID.getText().toString() )){
                        dialog.show();
                        // Check For existence....
                        checkUserExist( view.getContext(), signInShopID.getText().toString(), signInMobile.getText().toString() );
                    }else{
                        signInShopID.setError( "Enter Shop ID" );
                    }

                }else{
                    verifyMobile.setVisibility( View.INVISIBLE );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signInMobile.setBackgroundTintList( view.getContext().getResources().getColorStateList( R.color.colorRed ) );
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );

    }

    private void setVisibility( boolean isAuthenticate){
//        verifyShop.setVisibility( View.GONE );
        if (isAuthenticate){
            signInLayout.setVisibility( View.VISIBLE );
            signInForgetPassword.setVisibility( View.VISIBLE );
            createPassLayout.setVisibility( View.GONE );
        }else{
            signInLayout.setVisibility( View.GONE );
            signInForgetPassword.setVisibility( View.GONE );
            createPassLayout.setVisibility( View.VISIBLE );
        }

    }

    // Check Valid...
    private boolean isValid(){
        if (isAuth){
            if (TextUtils.isEmpty( signInPassword.getText().toString() )){
                signInPassword.setError( "Required!" );
                return false;
            }else {
                return true;
            }
        }else{
            if (TextUtils.isEmpty( signUpPass1.getText().toString() )){
                signUpPass1.setError( "Required!" );
                return false;
            }else if (TextUtils.isEmpty( signUpPass2.getText().toString() )){
                signUpPass2.setError( "Required!" );
                return false;
            }else  if ( !signUpPass2.getText().toString().equals( signUpPass1.getText().toString()  ) ){
                signUpPass2.setError( "Not Matched!" );
                signUpPass1.setError( "Not Matched!" );
                return false;
            }else {
                return true;
            }
        }
    }

    private boolean isDataFilled(){
        if (TextUtils.isEmpty( signInShopID.getText().toString() )){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                signInShopID.setBackgroundTintList( getContext().getResources().getColorStateList( R.color.colorRed ) );
            }
            signInShopID.setError( "Required!" );
            return false;
        }else if (TextUtils.isEmpty( signInMobile.getText().toString() )){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                signInMobile.setBackgroundTintList( getContext().getResources().getColorStateList( R.color.colorRed ) );
            }
            signInMobile.setError( "Required!" );
            return false;
        }else {
            return true;
        }
    }

    private void checkShopExist(final Context context, String shopID){
        firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .addSnapshotListener( new EventListener <DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()){
                            verifyShop.setVisibility( View.VISIBLE );
                        }else{
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                signInShopID.setBackgroundTintList( context.getResources().getColorStateList( R.color.colorRed ) );
                            }
                            signInShopID.setError( "Shop ID not found!" );
                            verifyShop.setVisibility( View.INVISIBLE );
                        }
                    }
                } );
    }

    private void checkUserExist(final Context context, String shopID, String mobile){

        firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .collection( "ADMINS" ).document( mobile )
                .addSnapshotListener( new EventListener <DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()){
//                            String authID = documentSnapshot.get( "auth_id" ).toString();
                            if (documentSnapshot.get( "auth_id" ) != null ){
                                // Show Pass...
                                adminEmail = documentSnapshot.get( "admin_email" ).toString();
                                setVisibility( true );
                                isAuth = true;
                            }else{
                                // Show Create Pass..
                                adminEmail = documentSnapshot.get( "admin_email" ).toString();
                                isAuth = false;
                                setVisibility( false );
                            }
                            verifyMobile.setVisibility( View.VISIBLE );
                            dialog.dismiss();
                        }else{
                            verifyMobile.setVisibility( View.INVISIBLE );
                            adminEmail = null;
                            isAuth = false;
                            DialogsClass.alertDialog( context, "Alert!", "This Mobile Number is not Registered Yet!" ).show();
                            dialog.dismiss();
                        }
                    }
                } );

    }

    // Log In...
    private void adminLogIn(final Context context, String email, String password){
        firebaseAuth.signInWithEmailAndPassword( email, password )
                .addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task <AuthResult> task) {
                        if (task.isSuccessful()){
                            // Assign Current User...
                            currentUser = firebaseAuth.getCurrentUser();
                            // Success...
                            SHOP_ID = signInShopID.getText().toString().trim();
                            ADMIN_DATA_MODEL.setAdminMobile( signInMobile.getText().toString().trim() );
                            // Go to Next Activity...
                            checkAdminPermission( context );
                        }else{
                            dialog.dismiss();
                            showToast( context, "Something Went Wrong!" );
                        }
                    }
                } );
    }
    // Authenticate...
    private void adminSignIn( final Context context, final String email, String password){
        firebaseAuth.createUserWithEmailAndPassword( email, password )
                .addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task <AuthResult> task) {
                        if (task.isSuccessful()){
                            // Assign Current User...
                            currentUser = firebaseAuth.getCurrentUser();
                            // Success...
                            Map<String, Object> updateMap = new HashMap <>();
                            updateMap.put( "auth_id", firebaseAuth.getCurrentUser().getUid() );
                            AdminQuery.updateAdminData( null, null, signInShopID.getText().toString(), signInMobile.getText().toString(), updateMap );
                            // Add More Details...
                            SHOP_ID = signInShopID.getText().toString().trim();
                            ADMIN_DATA_MODEL.setAdminMobile( signInMobile.getText().toString().trim() );
                            checkAdminPermission( context );
                        }else{
                            currentUser = null;
                            dialog.dismiss();
                            showToast( context, task.getException().getMessage() + ": " + email );
                        }
                    }
                } );
    }

    private void checkAdminPermission( final Context context ){
        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .collection( "ADMINS" ).document( ADMIN_DATA_MODEL.getAdminMobile() )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Boolean is_allowed = task.getResult().getBoolean( "is_allowed" );
                    if (is_allowed){
//                        String admin_address = task.getResult().get( "admin_address" ).toString();
                        String admin_email = task.getResult().get( "admin_email" ).toString();
                        String admin_code = task.getResult().get( "admin_code" ).toString();
                        String admin_name = task.getResult().get( "admin_name" ).toString();
                        String admin_photo = task.getResult().get( "admin_photo" ).toString();

                        ADMIN_DATA_MODEL.setAdminEmail( admin_email );
                        ADMIN_DATA_MODEL.setAdminCode( Integer.parseInt( admin_code ) );
                        ADMIN_DATA_MODEL.setAdminName( admin_name );
                        ADMIN_DATA_MODEL.setAdminPhoto( admin_photo );

                        // Load Shop Data...
                        DBQuery.getShopData( SHOP_ID );

                        checkForSetUp( context );

                    }
                    else{
                        deniedDialog();
                        dialog.dismiss();
                    }

                }else{
                    deniedDialog();
                    dialog.dismiss();
                }
            }
        } );

    }

    private void deniedDialog(){
        firebaseAuth.signOut();
        DialogsClass.alertDialog( getContext(), "Permission denied!", "You have not permission to use this app." );
    }
    private void writeDataInLocal(Context context, String shopID, String mobile){
        SHOP_ID = shopID;
        ADMIN_DATA_MODEL.setAdminMobile( mobile );
        try{
            StaticMethods.writeFileInLocal( context, "shop", shopID);
            StaticMethods.writeFileInLocal( context, "mobile", mobile );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Forget Password Method...
    private void getMailToResetPassword(final Context context, final String email) {

        DBQuery.firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()) {
                            DialogsClass.alertDialog( context, null,"We've send an email on your registered email address - " + email + " \nTo Reset your password, Please Check Your Email.!" ).show();
                        }
                        else if (task.isCanceled()){
                            DialogsClass.alertDialog( context, "Alert","Can't Send Email..! Error Occurred.!"  ).show();
                        }else {
                            DialogsClass.alertDialog( context, null,"Can't Send Email..! Try Again..1" ).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    // Check for Shop SetUp...
    private void checkForSetUp(final Context context){
        firebaseFirestore
                .collection( "SHOPS" ).document( SHOP_ID )
                .collection( "HOME" )
                .addSnapshotListener( new EventListener <QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null){
                            if (queryDocumentSnapshots.size() == 0){
                                signInUpScrollView.setVisibility( View.GONE );
                                shopSetUpLayout.setVisibility( View.VISIBLE );
                                dialog.dismiss();
                            }
                            else{
                                // Write in Local file
                                writeDataInLocal( context, signInShopID.getText().toString().trim(), signInMobile.getText().toString() );

                                // GOTO MAIN activity...
                                dialog.dismiss();
                                Intent intent = new Intent( context, MainActivity.class );
                                context.startActivity( intent );
                                authActivity.finish();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    if (!authActivity.isDestroyed())
                                        authActivity.finish();
                                }
                            }
                        }else{
                            //
                            dialog.dismiss();
                            signInUpScrollView.setVisibility( View.GONE );
                            shopSetUpLayout.setVisibility( View.VISIBLE );
                        }
                    }
                } );
    }

    // Fragment Transaction...
    public void setFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
        parentFrameLayout.removeAllViews();
        fragmentTransaction.replace( parentFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }



}
