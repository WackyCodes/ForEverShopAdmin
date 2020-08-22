package ean.ecom.eanshopadmin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ean.ecom.eanshopadmin.main.orderlist.OrderListFragment;
import ean.ecom.eanshopadmin.main.orderlist.neworder.NewOrderFragment;

import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_NOTIFICATION;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_NOTIFY_NEW_ORDER;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_INCOME_RECORDS;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_ORDER_LIST;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SELLING_RECORDS;

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

        int fragment_no = getIntent().getIntExtra( "FRAGMENT_NO", -1 );

        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

        // Assign Variables...
        frameLayout = findViewById( R.id.set_fragment_frame_layout );

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
            case REQUEST_TO_VIEW_SELLING_RECORDS:
            case REQUEST_TO_NOTIFICATION:
                showToast("Content Not Found!");
                break;
            default:
                break;
        }

//        public static final int REQUEST_TO_VIEW_ORDER_LIST = 5;
//        public static final int REQUEST_TO_VIEW_INCOME_RECORDS = 6;
//        public static final int REQUEST_TO_VIEW_SELLING_RECORDS = 7;
//        public static final int REQUEST_TO_VIEW_SHOP_RATING = 8;
//        public static final int REQUEST_TO_VIEW_SERVICE_AREA = 9;
//        public static final int REQUEST_TO_VIEW_FAVORITE_CUST = 10;

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
