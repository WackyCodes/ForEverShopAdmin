package ean.ecom.eanshopadmin.home.viewall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.List;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.home.bannerslider.BannerSliderAdaptor;
import ean.ecom.eanshopadmin.model.BannerModel;

public class ViewAllBannerSliderActivity extends AppCompatActivity {

    private RecyclerView viewAllRecycler;
    private Toolbar toolbar;

    private int catIndex;
    private int layoutIndex;
    private String catID;
//    private String layoutID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_all_banner_slider );

        catIndex = getIntent().getIntExtra( "CAT_INDEX", -1);
        layoutIndex = getIntent().getIntExtra( "LAY_INDEX", -1);

        catID = DBQuery.homeCatListModelList.get( catIndex ).getCatID();
//        layoutID =  DBQuery.homeCatListModelList.get( catIndex ).getHomeListModelList().get( layoutIndex ).getLayoutID();
        List <BannerModel> bannerList = DBQuery.homeCatListModelList.get( catIndex ).getHomeListModelList().get( layoutIndex ).getBannerModelList();

        viewAllRecycler = findViewById( R.id.view_all_layout_recycler );

        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }catch (NullPointerException ignored){ }

        // Layout... Set...
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        viewAllRecycler.setLayoutManager( layoutManager );

        BannerSliderAdaptor bannerItemAdaptor = new BannerSliderAdaptor( catIndex, layoutIndex ,catID,  bannerList , true );
        viewAllRecycler.setAdapter( bannerItemAdaptor );
        bannerItemAdaptor.notifyDataSetChanged();

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

}
