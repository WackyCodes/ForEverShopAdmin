package ean.ecom.eanshopadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ean.ecom.eanshopadmin.database.AdminQuery;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.home.HomeCatListModel;
import ean.ecom.eanshopadmin.home.HomeListModel;
import ean.ecom.eanshopadmin.launching.AuthActivity;
import ean.ecom.eanshopadmin.launching.WelcomeActivity;
import ean.ecom.eanshopadmin.main.MainFragment;
import ean.ecom.eanshopadmin.other.DialogsClass;

import static ean.ecom.eanshopadmin.database.AdminQuery.notificationModelList;
import static ean.ecom.eanshopadmin.database.DBQuery.currentUser;
import static ean.ecom.eanshopadmin.database.DBQuery.firebaseAuth;
import static ean.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;
import static ean.ecom.eanshopadmin.database.DBQuery.newOrderList;
import static ean.ecom.eanshopadmin.database.DBQuery.orderListModelList;
import static ean.ecom.eanshopadmin.database.DBQuery.preparingOrderList;
import static ean.ecom.eanshopadmin.database.DBQuery.readyToDeliveredList;
import static ean.ecom.eanshopadmin.other.StaticValues.ADMIN_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.CURRENT_CITY_NAME;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_NOTIFY_NEW_ORDER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;
import static ean.ecom.eanshopadmin.other.StaticValues.clipboardManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static AppCompatActivity mainActivity;
    public static MainFragment mainFragment;

    // FrameLayout...
    private FrameLayout mainFrameLayout;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public static TextView badgeNotifyCount;
    public static TextView badgeOrderCount;
    // Drawer...User info
    public static CircleImageView drawerImage;
    public static TextView drawerName;
    public static TextView drawerEmail;
    public static LinearLayout drawerCityLayout;
    public static TextView drawerCityTitle;
    public static TextView drawerCityName;

    public static TextView toolCityName;
    public static TextView toolUserName;

    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mainFrameLayout = findViewById( R.id.main_activity_frame_layout );

        mainActivity = this;
        dialog = DialogsClass.getDialog( this );
        // Assign ClipBoard Service...
        clipboardManager = (ClipboardManager)getSystemService( this.CLIPBOARD_SERVICE );

        toolbar = findViewById( R.id.appToolbar );
        drawer = findViewById( R.id.drawer_layout );
        navigationView = findViewById( R.id.nav_view );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( ADMIN_DATA_MODEL.getShopName() );
            getSupportActionBar().setSubtitle( SHOP_ID );
        }catch (NullPointerException ignored){ }

        // setNavigationItemSelectedListener()...
        navigationView.setNavigationItemSelectedListener( MainActivity.this );
        navigationView.getMenu().getItem( 0 ).setChecked( true );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this,drawer,toolbar,
                R.string.navigation_Drawer_Open,R.string.navigation_Drawer_close);
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        // Drawer Variable assign...
        drawerName = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_UserName );
        drawerEmail = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_userEmail );
        drawerCityName = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_user_city );

        drawerName.setText( ADMIN_DATA_MODEL.getAdminName() ); // Admin Name...
        drawerEmail.setText( ADMIN_DATA_MODEL.getAdminEmail() ); // Admin Email...
        drawerCityName.setText( ADMIN_DATA_MODEL.getShopAddress() ); // Shop Address...

        // Assign...
        toolCityName = findViewById( R.id.tool_user_city );
        toolUserName = findViewById( R.id.tool_user_name );

        if (ADMIN_DATA_MODEL.getAdminName() != null){
            toolUserName.setText( ADMIN_DATA_MODEL.getAdminName() );
        }
        if ( CURRENT_CITY_NAME!=null ){
            toolCityName.setText( CURRENT_CITY_NAME );
        }

        if (mainFragment == null){
            mainFragment = new MainFragment();
            setFragment( mainFragment );
        }else{
            setFragment( mainFragment );
        }

        if (homeCatListModelList.size() == 0){
            homeCatListModelList.add( new HomeCatListModel( "HOME", "Home", new ArrayList <HomeListModel>() ) );
            DBQuery.getHomeCatListQuery( MainActivity.this, null, null, "HOME", 0  );
        }


        // Call new Order List Data...
        DBQuery.loadNewOrderListData( this );

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Notification Loading...
//        AdminQuery.loadNotificationsQuery( this );
    }

    // --------  Menu And Navigation....
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main_activity_options, menu);

        // Check First whether any item in cart or not...
        // if any item has in cart...
        MenuItem cartItem = menu.findItem( R.id.menu_order_main );
        cartItem.setActionView( R.layout.badge_order_layout );
        badgeOrderCount = cartItem.getActionView().findViewById( R.id.badge_order_count_text );
        if (newOrderList.size()>0){
            badgeOrderCount.setVisibility( View.VISIBLE );
            badgeOrderCount.setText( newOrderList.size() + "" );
        }else{
            badgeOrderCount.setVisibility( View.GONE );
        }
        cartItem.getActionView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GOTO : New Order....
                Intent orderListIntent = new Intent( MainActivity.this, SetFragmentActivity.class );
                orderListIntent.putExtra( "FRAGMENT_NO", REQUEST_TO_NOTIFY_NEW_ORDER );
                startActivity( orderListIntent );
            }
        } );

        // notification badge...
        MenuItem notificationItem = menu.findItem( R.id.menu_notification_main );
        notificationItem.setActionView( R.layout.badge_notification_layout );
        badgeNotifyCount = notificationItem.getActionView().findViewById( R.id.badge_count );
        if (notificationModelList.size() > 0){
            badgeNotifyCount.setVisibility( View.VISIBLE );
            badgeNotifyCount.setText( String.valueOf( notificationModelList.size() ) );
        }else{
            badgeNotifyCount.setVisibility( View.GONE );
        }
        notificationItem.getActionView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent catIntent = new Intent( MainActivity.this, NotificationActivity.class);
//                startActivity( catIntent );
            }
        } );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
//            if (isFragmentIsMyCart){
////                setFragment( new HomeFragment(), M_HOME_FRAGMENT );
//                wCurrentFragment = M_HOME_FRAGMENT;
//                navigationView.getMenu().getItem( 0 ).setChecked( true );
//                mainActivityForCart.finish();
//            }
            return true;
        }else
        if (id == R.id.menu_order_main){
            return true;
        }else
        if (id == R.id.menu_notification_main){
//            Intent catIntent = new Intent( MainActivity.this, NotificationActivity.class);
//            startActivity( catIntent );
            return true;
        } else
            return super.onOptionsItemSelected( item );
    }

    int mainNavItemId;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer( GravityCompat.START );

        mainNavItemId = menuItem.getItemId();

        // ------- On Item Click...
        // Home Nav Option...
        if ( mainNavItemId == R.id.nav_home ){
            // index - 0
            getSupportActionBar().setTitle( R.string.app_name );
            return true;
        }else
            // Bottom Options...
            if ( mainNavItemId == R.id.menu_log_out ){
                // index - 5
                if (currentUser != null){
                    // TODO : Show Dialog to logOut..!
                    // Sign Out Dialog...
                    final Dialog signOut = new Dialog( MainActivity.this );
                    signOut.requestWindowFeature( Window.FEATURE_NO_TITLE );
                    signOut.setContentView( R.layout.dialog_sign_out );
                    signOut.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                    signOut.setCancelable( false );
                    ImageView imageView = signOut.findViewById( R.id.sign_out_image );
//                    Glide.with( this ).load( "sample" ).apply( new RequestOptions().placeholder( R.mipmap.logo_round ) ).into( imageView );
                    final Button signOutOkBtn = signOut.findViewById( R.id.sign_out_ok_btn );
                    Button signOutCancelBtn = signOut.findViewById( R.id.sign_out_cancel_btn );
                    signOut.show();

                    signOutOkBtn.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signOutOkBtn.setEnabled( false );
                            firebaseAuth.signOut();
                            currentUser = null;
                            navigationView.getMenu().getItem( 0 ).setChecked( true );
//                            navigationView.getMenu().getItem( 5 ).setEnabled( false );
                            signOut.dismiss();
                            // GOTO : AuthActivity...!
                            startActivity( new Intent( MainActivity.this, AuthActivity.class ) );

                            // Clean All The Data...
                            homeCatListModelList.clear();
                            orderListModelList.clear();
                            newOrderList.clear();
                            preparingOrderList.clear();
                            readyToDeliveredList.clear();
                            notificationModelList.clear();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!mainActivity.isDestroyed()){
                                    mainActivity.finish();
                                }
                            }
                            finish();
//                            finishAffinity();
                        }
                    } );
                    signOutCancelBtn.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signOut.dismiss();
                            // TODO : Sign Out
                        }
                    } );

                    return false;
                }
            }
        return false;
    }

    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add( mainFrameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }


}
