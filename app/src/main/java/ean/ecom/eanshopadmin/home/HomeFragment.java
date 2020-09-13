package ean.ecom.eanshopadmin.home;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.addnew.AddNewLayoutActivity;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.other.CheckInternetConnection;
import ean.ecom.eanshopadmin.other.DialogsClass;
import static ean.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;
import static ean.ecom.eanshopadmin.other.StaticValues.GRID_PRODUCTS_LAYOUT_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_BANNER_SLIDER_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_STRIP_AD_CONTAINER;

public class HomeFragment extends Fragment implements View.OnClickListener {


    public HomeFragment( int catIndex, String catID, String catName) {
        // Required empty public constructor
        this.catIndex = catIndex;
        this.catID = catID;
        this.catName = catName;
    }
    public static HomePageAdaptor homePageAdaptor;

    private FrameLayout homeFragmentFrame;
    private SwipeRefreshLayout homeFragmentSwipeRefresh;
    private RecyclerView homeFragmentRecycler;

    private int catIndex;
    private String catID;
    private String catName;

    // Add New Layout...
    private LinearLayout addNewLayoutBtn;
    private LinearLayout dialogAddLayout;
//    private ConstraintLayout newBannerSliderContainer; // add_banner_slider_layout
//    private ConstraintLayout newGridLayoutContainer; // add_grid_layout
//    private ConstraintLayout newStripAdLayout; // add_strip_ad_layout
    private LinearLayout addNewBannerSliderBtn; // add_banner_slider_layout_LinearLay
    private LinearLayout addNewProductHrLayBtn; //add_horizontal_layout_LinearLay
    private LinearLayout addNewGirdLayoutBtn; // add_grid_layout_LinearLay
    private LinearLayout addNewStripAdLayoutBtn; // add_strip_ad_layout_LinearLay
    private ImageView closeAddLayout; //close_add_layout

    private Context context;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_home, container, false );
        context = view.getContext();
        dialog = DialogsClass.getDialog( getContext() );

        homeFragmentFrame = view.findViewById( R.id.home_fragment_frame_layout );
        homeFragmentSwipeRefresh = view.findViewById( R.id.home_fragment_swipe_refresh_layout );
        homeFragmentRecycler = view.findViewById( R.id.home_fragment_recycler );

        // Set home Activity name...
        HomeActivity.homeCurrentCatID = catID;
        HomeActivity.homeCurrentCatIndex = catIndex;
//        catID = homeCatListModelList.get( catIndex ).getCatID();

        // Layout... Set...
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        homeFragmentRecycler.setLayoutManager( layoutManager );
        // TODO: Set Adaptor...
        homePageAdaptor = new HomePageAdaptor( catIndex, homeCatListModelList.get( catIndex ).getHomeListModelList());
        homeFragmentRecycler.setAdapter( homePageAdaptor );
        homePageAdaptor.notifyDataSetChanged();
        if (homeCatListModelList.get(catIndex).getHomeListModelList().size() == 0){
            // Load Our List First...
            dialog.show();
            DBQuery.getHomeCatListQuery( getContext(), dialog, null, catID, catIndex  );
        }
        // Refreshing the List...
        homeFragmentSwipeRefresh.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refreshing...
                if (CheckInternetConnection.isInternetConnected( getContext() )){
                    homeFragmentSwipeRefresh.setRefreshing( true );
                    homeCatListModelList.get( catIndex ).getHomeListModelList().clear();
                    DBQuery.getHomeCatListQuery( getContext(), null, homeFragmentSwipeRefresh, catID, catIndex  );
                }else{
                    homeFragmentSwipeRefresh.setRefreshing( false );
                }
            }
        } );

        // Add Layout...
        addNewLayoutBtn =  view.findViewById( R.id.add_new_layout );
        dialogAddLayout =  view.findViewById( R.id.dialog_add_layout );
//        newBannerSliderContainer =  view.findViewById( R.id.add_banner_slider_layout );
//        newGridLayoutContainer =  view.findViewById( R.id.add_grid_layout );
//        newStripAdLayout = view.findViewById( R.id.add_strip_ad_layout );
        addNewBannerSliderBtn =  view.findViewById( R.id.add_banner_slider_layout_LinearLay );
        addNewProductHrLayBtn =  view.findViewById( R.id.add_horizontal_layout_LinearLay );
        addNewGirdLayoutBtn =  view.findViewById( R.id.add_grid_layout_LinearLay );
        addNewStripAdLayoutBtn =  view.findViewById( R.id.add_strip_ad_layout_LinearLay );
        closeAddLayout =  view.findViewById( R.id.close_add_layout );

        addNewLayoutBtn.setOnClickListener( this );
        closeAddLayout.setOnClickListener( this );

        addNewBannerSliderBtn.setOnClickListener( this );
        addNewProductHrLayBtn.setOnClickListener( this );
        addNewGirdLayoutBtn.setOnClickListener( this );
        addNewStripAdLayoutBtn.setOnClickListener( this );

        return view;
    }

    @Override
    public void onClick(View view) {

        // Add New Layout Button Click...
         if (view == addNewLayoutBtn){
             // TODO:
             setDialogVisibility(true);
         }else  if (view == closeAddLayout){
             // TODO:
             setDialogVisibility(false);
         }else  if (view == addNewBannerSliderBtn){
             // TODO:
             setDialogVisibility(false);
//             AddNewLayoutActivity.isHomePage = true;
             Intent intent = new Intent( getActivity(), AddNewLayoutActivity.class );
             intent.putExtra( "LAY_TYPE", SHOP_HOME_BANNER_SLIDER_CONTAINER );
             intent.putExtra( "LAY_INDEX", homeCatListModelList.get( catIndex ).getHomeListModelList().size() );
             intent.putExtra( "CAT_INDEX", catIndex );
//             intent.putExtra( "CAT_ID", catID );
//             intent.putExtra( "TASK_UPDATE", false );
             startActivity( intent );

         }else  if (view == addNewProductHrLayBtn){
             // TODO:
             setDialogVisibility(false);
             Intent intent = new Intent( getActivity(), AddNewLayoutActivity.class );
             intent.putExtra( "LAY_TYPE", HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER );
             intent.putExtra( "LAY_INDEX", homeCatListModelList.get( catIndex ).getHomeListModelList().size() );
             intent.putExtra( "CAT_INDEX", catIndex );
//             intent.putExtra( "CAT_ID", catID );
//             intent.putExtra( "TASK_UPDATE", false );
             startActivity( intent );

         }else  if (view == addNewGirdLayoutBtn){
             // TODO:
             setDialogVisibility(false);
             Intent intent = new Intent( getActivity(), AddNewLayoutActivity.class );
             intent.putExtra( "LAY_TYPE", GRID_PRODUCTS_LAYOUT_CONTAINER );
             intent.putExtra( "LAY_INDEX", homeCatListModelList.get( catIndex ).getHomeListModelList().size() );
             intent.putExtra( "CAT_INDEX", catIndex );
//             intent.putExtra( "CAT_ID", catID );
//             intent.putExtra( "TASK_UPDATE", false );
             startActivity( intent );

         }else  if (view == addNewStripAdLayoutBtn){
             // TODO:
             setDialogVisibility(false);
             Intent intent = new Intent( getActivity(), AddNewLayoutActivity.class );
             intent.putExtra( "LAY_TYPE", SHOP_HOME_STRIP_AD_CONTAINER );
             intent.putExtra( "LAY_INDEX", homeCatListModelList.get( catIndex ).getHomeListModelList().size() );
             intent.putExtra( "CAT_INDEX", catIndex );
//             intent.putExtra( "CAT_ID", catID );
//             intent.putExtra( "TASK_UPDATE", false );
             startActivity( intent );
         }

    }

    @Override
    public void onResume() {
        super.onResume();
        setDialogVisibility( false );
        if (homePageAdaptor!=null){
            homePageAdaptor.notifyDataSetChanged();
        }
    }

    private void setDialogVisibility(boolean isVisible){
        if(!isVisible){
            homeFragmentSwipeRefresh.setVisibility( View.VISIBLE );
            addNewLayoutBtn.setVisibility( View.VISIBLE );
            dialogAddLayout.setVisibility( View.GONE );
        }else{
            homeFragmentSwipeRefresh.setVisibility( View.GONE );
            addNewLayoutBtn.setVisibility( View.GONE );
            dialogAddLayout.setVisibility( View.VISIBLE );
        }
    }



}
