package wackycodes.ecom.eanshopadmin.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import wackycodes.ecom.eanshopadmin.R;

import static wackycodes.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;
import static wackycodes.ecom.eanshopadmin.other.StaticValues.FRAGMENT_HOME;

public class HomeActivity extends AppCompatActivity {
    public static AppCompatActivity homeActivity;
    public static FrameLayout homeActivityFrame;

    public static int homeCurrentFragment = FRAGMENT_HOME;
    public static int homeCurrentCatIndex = 0;
    public static String homeCurrentCatID = "HOME";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        homeActivity = this;

        homeActivityFrame = findViewById( R.id.home_activity_frame_layout );

        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

//        if (homeCatListModelList.size() == 0){
//            // DO Complete this process in MainActivity Background!
//        }

        // Set Frame...
        if (homeCurrentFragment == FRAGMENT_HOME){
            setForwardFragment( new HomeFragment( 0, "HOME", "Home" ) );
        }else{
            setForwardFragment( new HomeFragment( homeCurrentCatIndex, homeCurrentCatID, homeCatListModelList.get( homeCurrentCatIndex ).getCatName() ) );
        }
        //

    }

    @Override
    public void onBackPressed() {
        if (homeCurrentCatIndex == FRAGMENT_HOME){
            super.onBackPressed();
        }else{
            // Set Home Fragment...
            setBackFragment( new HomeFragment( 0, "HOME", "Home" ) );
        }

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
    private void setForwardFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add( homeActivityFrame.getId(),fragment );
        fragmentTransaction.commit();
    }

    // Fragment Transaction...
    private void setBackFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations( R.anim.slide_from_left, R.anim.slide_out_from_right );
        fragmentTransaction.add( homeActivityFrame.getId(),fragment );
        fragmentTransaction.commit();
    }



}
