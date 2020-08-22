package ean.ecom.eanshopadmin.main;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.other.DialogsClass;

import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_HOME;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_ORDER_LIST;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SHOP;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }
    private Dialog dialog;

    private FrameLayout mainFragmentLayout;

    // Home Fragment Layout item...
    private GridView homeGridView;
    public static List <MainFragmentModel> mainPageList = new ArrayList <>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_main, container, false );
        dialog = DialogsClass.getDialog( view.getContext() );

        homeGridView = view.findViewById( R.id.home_fragment_grid_view );


        // Home List...
        if (mainPageList.size() == 0){
            mainPageList.add( new MainFragmentModel( R.drawable.ic_home_black_24dp, "View Home", REQUEST_TO_VIEW_HOME ) );
            mainPageList.add( new MainFragmentModel( R.drawable.ic_devices_black_24dp, "Order List", REQUEST_TO_VIEW_ORDER_LIST ) );
            mainPageList.add( new MainFragmentModel( R.drawable.ic_account_balance_black_24dp, "Income Records", -1 ) );
            mainPageList.add( new MainFragmentModel( R.drawable.ic_assignment_black_24dp, "Selling Records", -1 ) );
            mainPageList.add( new MainFragmentModel( R.drawable.ic_store_black_24dp, "My Shop", REQUEST_TO_VIEW_SHOP ) );
            mainPageList.add( new MainFragmentModel( R.drawable.ic_stars_black_24dp, "Shop Rating", -1 ) );
            mainPageList.add( new MainFragmentModel( R.drawable.ic_map_black_24dp, "Service Area", -1 ) );
            mainPageList.add( new MainFragmentModel( R.drawable.ic_favorite_black_24dp, "Favorite Customers", -1 ) );
        }

        // Create and Set Adaptor...
        MainFragmentAdaptor mainFragmentAdaptor = new MainFragmentAdaptor();
        homeGridView.setAdapter( mainFragmentAdaptor );
        mainFragmentAdaptor.notifyDataSetChanged();


        return view;
    }

}
