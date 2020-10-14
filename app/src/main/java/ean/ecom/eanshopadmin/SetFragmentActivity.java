package ean.ecom.eanshopadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ean.ecom.eanshopadmin.main.favcutomers.FavCustomersFragment;
import ean.ecom.eanshopadmin.main.income.IncomeFragment;
import ean.ecom.eanshopadmin.main.orderlist.OrderListFragment;
import ean.ecom.eanshopadmin.main.orderlist.neworder.NewOrderFragment;
import ean.ecom.eanshopadmin.main.selling.SellingFragment;
import ean.ecom.eanshopadmin.main.servicearea.ServiceAreaFragment;
import ean.ecom.eanshopadmin.main.shop.AboutShopActivity;
import ean.ecom.eanshopadmin.main.shoprating.ShopRatingFragment;
import ean.ecom.eanshopadmin.notification.NotificationFragment;

import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_NOTIFICATION;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_NOTIFY_NEW_ORDER;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_FAVORITE_CUST;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_INCOME_RECORDS;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_ORDER_LIST;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SELLING_RECORDS;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SERVICE_AREA;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SHOP;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SHOP_RATING;

/**
 * Created by Shailendra (WackyCodes) on 30/07/2020 21:02
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */

public class SetFragmentActivity extends AppCompatActivity {
    public static AppCompatActivity setFragmentActivity;

    private FrameLayout frameLayout;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_set_fragment );
        setFragmentActivity = this;

        int fragment_no = getIntent().getIntExtra( "FRAGMENT_ID", -1 );

        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

        // Assign Variables...
        frameLayout = findViewById( R.id.set_fragment_frame_layout );

        // Set Fragment...
        setFrameLayout(fragment_no);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return true;
    }

    private void setFrameLayout(int fragment_no){
        switch (fragment_no){
            case REQUEST_TO_VIEW_ORDER_LIST:
                setFragment(new OrderListFragment());
                getSupportActionBar().setTitle( "Order List" );
                break;
            case REQUEST_TO_NOTIFY_NEW_ORDER:
                if (NewOrderFragment.newOrderFragment == null){
                    NewOrderFragment.newOrderFragment = new NewOrderFragment();
                }
                setFragment( NewOrderFragment.newOrderFragment );
                getSupportActionBar().setTitle( "New Orders" );
                break;
            case REQUEST_TO_VIEW_INCOME_RECORDS:
                setFragment(new IncomeFragment());
                getSupportActionBar().setTitle( "Income List" );
                break;
            case REQUEST_TO_VIEW_SELLING_RECORDS:
                setFragment(new SellingFragment());
                getSupportActionBar().setTitle( "Selling List" );
                break;
            case REQUEST_TO_VIEW_SHOP_RATING:
                setFragment(new ShopRatingFragment());
                getSupportActionBar().setTitle( "Shop Ratings" );
                break;
            case REQUEST_TO_VIEW_SERVICE_AREA:
                setFragment(new ServiceAreaFragment());
                getSupportActionBar().setTitle( "Service Areas" );
                break;
            case REQUEST_TO_VIEW_FAVORITE_CUST:
                setFragment(new FavCustomersFragment());
                getSupportActionBar().setTitle( "Favorite Customers" );
                break;
            case REQUEST_TO_NOTIFICATION:
                setFragment(new NotificationFragment());
                getSupportActionBar().setTitle( "Notification" );
                break;
            default:
                break;
        }
    }

    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add( frameLayout.getId(),fragment );
        fragmentTransaction.commit();
    }

    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

}
