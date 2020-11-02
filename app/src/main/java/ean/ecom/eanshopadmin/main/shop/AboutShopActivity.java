package ean.ecom.eanshopadmin.main.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.StaticValues;

import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_DATA_MODEL;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_TYPE_EGG;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_TYPE_NON_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_TYPE_NO_SHOW;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_TYPE_VEG;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_TYPE_VEG_NON;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_ADDRESS;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_DAY_SCHEDULE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_HELPLINE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_IMAGE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_LOGO;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_NAME;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_OPEN_CLOSE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_TAG_LINE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_TIME_SCHEDULE;
import static ean.ecom.eanshopadmin.other.StaticValues.UPDATE_SHOP_VEG_NON_CODE;

public class AboutShopActivity extends AppCompatActivity implements UpdateShop.AddImageListener{

    boolean isUpdateAllowed = true;
    boolean isAllowBack = true;
    //Update ...
    private ScrollView shopUpdateScrollView;
    private FrameLayout shopUpdateFrameLayout;

    private Dialog dialog;

    //...
    private ImageView shopBgImage; // shop_image
    private CircleImageView shopLogo; // shop_logo_circle_imageView
    private ImageView verifyImageTag; // verify_tag_image
    private TextView shopRatingText; // shop_rating_text
    private LinearLayout shopVegLayout; // shop_veg_layout
    private LinearLayout shopEggTypeLayout; // shop_veg_layout
    private LinearLayout shopNonVegLayout; // shop_non_veg_layout
    private TextView shopOpenCloseText; // shop_open_close_text
    private TextView shopOpenCloseTimeText; // shop_opening_timing
    private Switch shopOpenCloseSwitch; // shop_open_close_switch
    private TextView shopIDText; // shop_id_text
    private TextView shopNameText; // shop_name_text
    private TextView shopAddressText; // shop_address
    private TextView shopTypeCatText; // shop_category_text
    private TextView shopTagLine; // shop_tag_line_text
    private TextView shopHelpLineText; //
    private TextView shopOffDaysText; // weekly_off_days_text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_about_shop );
        dialog = DialogsClass.getDialog( this );
        // TODO : Set Shop Profile...

        // ...
        shopUpdateScrollView = findViewById( R.id.shop_update_dialog_scroll );
        shopUpdateFrameLayout = findViewById( R.id.shop_update_frame_layout );
        // Variable - Shop
        shopBgImage = findViewById( R.id.shop_image );
        shopLogo = findViewById( R.id.shop_logo_circle_imageView );
        verifyImageTag = findViewById( R.id.verify_tag_image );
        shopRatingText = findViewById( R.id.shop_rating_text );
        shopVegLayout = findViewById( R.id.shop_veg_layout );
        shopEggTypeLayout = findViewById( R.id.shop_egg_type_layout );
        shopNonVegLayout = findViewById( R.id.shop_non_veg_layout );
        shopOpenCloseText = findViewById( R.id.shop_open_close_text );
        shopOpenCloseTimeText = findViewById( R.id.shop_opening_timing );
        shopOpenCloseSwitch = findViewById( R.id.shop_open_close_switch );
        shopIDText = findViewById( R.id.shop_id_text );
        shopNameText = findViewById( R.id.shop_name_text );
        shopAddressText = findViewById( R.id.shop_address );
        shopTypeCatText = findViewById( R.id.shop_category_text );
        shopTagLine = findViewById( R.id.shop_tag_line_text );
        shopHelpLineText = findViewById( R.id.shop_help_line_number );
        shopOffDaysText = findViewById( R.id.weekly_off_days_text );

        // Set Title on Action Menu
        Toolbar toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try{
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar( ).setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException e){ }

        // Default Set...
        defaultView( false );

        isUpdateAllowed = false;
        // set shop Data..
        setShopData();

        // set Shop Open Close Action...
        setShopOpenCloseSwitch();

    }

    private void defaultView(boolean isVisible){
        if (isVisible){
            shopUpdateScrollView.setVisibility( View.VISIBLE );
        }else{
            shopUpdateScrollView.setVisibility( View.GONE );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // set shop Data..
        setShopData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isUpdateAllowed){
            getMenuInflater().inflate( R.menu.shop_edit_menu_options,menu);
//            MenuItem cartItem = menu.findItem( R.id.menu_update_shop_address );
            return true;
        }else{
            return super.onCreateOptionsMenu( menu );
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.menu_update_shop_logo){
            setShopUpdateFrameLayout(new UpdateShopFragment(
                    this, new UpdateShopQuery(), UPDATE_SHOP_LOGO ) );
            return true;
        }else
        if (id == R.id.menu_update_shop_image){
            setShopUpdateFrameLayout(new UpdateShopFragment(
                    this, new UpdateShopQuery(), UPDATE_SHOP_IMAGE ) );
            return true;
        }else
        if (id == R.id.menu_update_shop_status){
            setShopUpdateFrameLayout(new UpdateShopFragment(
                    this, new UpdateShopQuery(), UPDATE_SHOP_TAG_LINE ) );
            return true;
        }else
        if (id == R.id.menu_update_shop_veg){
            setShopUpdateFrameLayout(new UpdateShopFragment(
                    this, new UpdateShopQuery(), UPDATE_SHOP_VEG_NON_CODE ) );
            return true;
        }else
        if (id == R.id.menu_update_shop_address){
            Intent addressIntent = new Intent(AboutShopActivity.this, AddAddressActivity.class);
            startActivity( addressIntent );
            return true;
        }else
        if (id == R.id.menu_update_shop_helpline){
            setShopUpdateFrameLayout(new UpdateShopFragment(
                    this, new UpdateShopQuery(), UPDATE_SHOP_HELPLINE ) );
            return true;
        }else
        if (id == R.id.menu_update_shop_name){
            setShopUpdateFrameLayout(new UpdateShopFragment(
                    this, new UpdateShopQuery(), UPDATE_SHOP_NAME ) );
            return true;
        }else
        if (id == R.id.menu_update_shop_time){
            setShopUpdateFrameLayout(new UpdateShopFragment(
                    this, new UpdateShopQuery(), UPDATE_SHOP_TIME_SCHEDULE ) );
            return true;
        }else
        if (id == R.id.menu_update_shop_schedule){
            setShopUpdateFrameLayout(new UpdateShopFragment(
                    this, new UpdateShopQuery(), UPDATE_SHOP_DAY_SCHEDULE ) );
            return true;
        }else {
            return super.onOptionsItemSelected( item );
        }

    }

    private void setShopData(){
        // Set Logo Image...
        Glide.with( this ).load( SHOP_DATA_MODEL.getShop_logo() ).apply(
                new RequestOptions().circleCrop().placeholder( R.drawable.ic_photo_black_24dp ) )
                .into( shopLogo );

        // Set shop Image...
        Glide.with( this ).load( SHOP_DATA_MODEL.getShop_image() ).apply(
                new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) )
                .into( shopBgImage );

        // Verify Shop
        if (SHOP_DATA_MODEL.getVerify_code() != null)
        if (Integer.parseInt( SHOP_DATA_MODEL.getVerify_code()  )== StaticValues.VERIFIED){
            verifyImageTag.setImageResource( R.drawable.ic_verified_user_black_24dp );
        }else{
            verifyImageTag.setImageResource( R.drawable.ic_fiber_new_black_24dp );
        }

        // Set Shop Rating...
        shopRatingText.setText( SHOP_DATA_MODEL.getShop_rating() );

        // Veg Non Veg...
        setVegNonLay();

        // Set Open And Close Time...
        setShopOpenCloseText();

        // shopOpenCloseTimeText
        shopOpenCloseTimeText.setText( SHOP_DATA_MODEL.getShop_open_time() + "-" + SHOP_DATA_MODEL.getShop_close_time() );

        // shopIDText
        shopIDText.setText( SHOP_DATA_MODEL.getShop_id() );

        // shopNameText
        shopNameText.setText( SHOP_DATA_MODEL.getShop_name() );

        // shopAddressText
        shopAddressText.setText( SHOP_DATA_MODEL.getShop_address() );

        // shopTypeCatText
        shopTypeCatText.setText( SHOP_DATA_MODEL.getShop_category_name() );

        // shopTagLine
        shopTagLine.setText( SHOP_DATA_MODEL.getShop_tag_line() );

        // set Helpline num..
        shopHelpLineText.setText( "+91 " + SHOP_DATA_MODEL.getShop_help_line() );
        // Set Days Schedule...
        setShopDaysSchedule();
        // set Update Allow...
        isUpdateAllowed = true;
    }

    // Veg Non Veg...
    private void setVegNonLay(){
        if (SHOP_DATA_MODEL.getShop_veg_non_type() != null)
            switch (Integer.parseInt( SHOP_DATA_MODEL.getShop_veg_non_type() )){
                case SHOP_TYPE_VEG:
                    shopVegLayout.setVisibility( View.VISIBLE );
                    shopNonVegLayout.setVisibility( View.GONE );
                    shopEggTypeLayout.setVisibility( View.GONE );
                    break;
                case SHOP_TYPE_NON_VEG:
                    shopVegLayout.setVisibility( View.GONE );
                    shopNonVegLayout.setVisibility( View.VISIBLE );
                    shopEggTypeLayout.setVisibility( View.GONE );
                    break;
                case SHOP_TYPE_VEG_NON:
                    shopVegLayout.setVisibility( View.VISIBLE );
                    shopNonVegLayout.setVisibility( View.VISIBLE );
                    shopEggTypeLayout.setVisibility( View.GONE );
                    break;
                case SHOP_TYPE_EGG:
                    shopVegLayout.setVisibility( View.GONE );
                    shopNonVegLayout.setVisibility( View.GONE );
                    shopEggTypeLayout.setVisibility( View.VISIBLE );
                    break;
                case SHOP_TYPE_NO_SHOW:
                    shopVegLayout.setVisibility( View.GONE );
                    shopNonVegLayout.setVisibility( View.GONE );
                    shopEggTypeLayout.setVisibility( View.GONE );
                    break;
                default:
                    break;
            }
    }
    // Set Open And Close Time...
    private void setShopOpenCloseText( ){
        // Set Open And Close Time...
        if (SHOP_DATA_MODEL.isIs_open()){
            shopOpenCloseText.setText( "OPEN" );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                shopOpenCloseText.setBackgroundTintList( ColorStateList.valueOf( getResources().getColor( R.color.colorGreen ) ) );
                shopOpenCloseSwitch.setBackgroundTintList( ColorStateList.valueOf( getResources().getColor( R.color.colorGreen ) ) );
            }
            // Set Switch...
            shopOpenCloseSwitch.setChecked( true );
            shopOpenCloseSwitch.setText( "OPEN" );

        }else{
            shopOpenCloseText.setText( "CLOSE" );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                shopOpenCloseText.setBackgroundTintList( ColorStateList.valueOf( getResources().getColor( R.color.colorRed ) ) );
                shopOpenCloseSwitch.setBackgroundTintList( ColorStateList.valueOf( getResources().getColor( R.color.colorRed ) ) );
            }
            // Set Switch...
            shopOpenCloseSwitch.setChecked( false );
            shopOpenCloseSwitch.setText( "CLOSE" );
        }
    }
    // set Shop Days Schedule..
    private void setShopDaysSchedule(){
        if (SHOP_DATA_MODEL.getShop_days_schedule() == null){
            return;
        }
        String offDays = null;
        int tempDayIndex = 0;
        String[] daysArray = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        for (Boolean isOpen : SHOP_DATA_MODEL.getShop_days_schedule()){
            if (!isOpen){
                if (offDays != null){
                    offDays = offDays + ", " + daysArray[tempDayIndex];
                }else{
                    offDays = daysArray[tempDayIndex];
                }
                tempDayIndex++;
            }else{
                tempDayIndex++;
            }
        }

        if (offDays != null){
            shopOffDaysText.setText( offDays );
        }else{
            shopOffDaysText.setText( "No Days" );
        }
    }

    // Open Close Switch Action...
    private void setShopOpenCloseSwitch(){
        shopOpenCloseSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isUpdateAllowed){
                    isUpdateAllowed = false;
                    dialog.show();
                    Map<String, Object> updateMap = new HashMap <>();
                    updateMap.put( "is_open", b );
                    new UpdateShopQuery().onUpdateShopRequest( dialog, AboutShopActivity.this, UPDATE_SHOP_OPEN_CLOSE, updateMap );
                    SHOP_DATA_MODEL.setIs_open( b );
                }
            }
        } );

    }

    private void backPressedUpdate(boolean isEnable){
        // Disable/Enable Back Pressed...
        getSupportActionBar( ).setDisplayHomeAsUpEnabled( isEnable );
        isAllowBack = isEnable;
        isUpdateAllowed = isEnable;
        invalidateOptionsMenu();
    }

    private void setShopUpdateFrameLayout(Fragment fragment){
        defaultView( true );
        backPressedUpdate( false );
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( shopUpdateFrameLayout.getId(), fragment );
        fragmentTransaction.commit();
    }

    @Override
    public void onAddImageSuccess(int requestCode) {
        // Default
        defaultView( false );
        backPressedUpdate( true );

        switch (requestCode){
            case UPDATE_SHOP_LOGO:
                Glide.with( this ).load( SHOP_DATA_MODEL.getShop_logo() ).apply(
                        new RequestOptions().circleCrop().placeholder( R.drawable.ic_photo_black_24dp ) )
                        .into( shopLogo );
                break;
            case UPDATE_SHOP_IMAGE:
                Glide.with( this ).load( SHOP_DATA_MODEL.getShop_image() ).apply(
                        new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) )
                        .into( shopBgImage );
                break;
            case UPDATE_SHOP_TAG_LINE:
                shopTagLine.setText( SHOP_DATA_MODEL.getShop_tag_line() );
                break;
            case UPDATE_SHOP_ADDRESS:
                shopAddressText.setText( SHOP_DATA_MODEL.getShop_address() );
                break;
            case UPDATE_SHOP_HELPLINE:
                shopHelpLineText.setText( "+91 " + SHOP_DATA_MODEL.getShop_help_line() );
                break;
            case UPDATE_SHOP_NAME:
                shopNameText.setText( SHOP_DATA_MODEL.getShop_name() );
                break;
            case UPDATE_SHOP_TIME_SCHEDULE:
                shopOpenCloseTimeText.setText( SHOP_DATA_MODEL.getShop_open_time() + "-" + SHOP_DATA_MODEL.getShop_close_time() );
                break;
            case UPDATE_SHOP_DAY_SCHEDULE:
                setShopDaysSchedule();
                break;
            case UPDATE_SHOP_VEG_NON_CODE:
                setVegNonLay();
                break;
            case UPDATE_SHOP_OPEN_CLOSE:
                setShopOpenCloseText();
                break;
            default:
                break;
        }

    }

    @Override
    public void onAddImageFailed() {
        defaultView( false );
        backPressedUpdate( true );
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }


    /**   private void checkShopExist(final Context context, String shopID){
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
                            verifyShop.setVisibility( View.GONE );
                        }
                    }
                } );
    } */



}
