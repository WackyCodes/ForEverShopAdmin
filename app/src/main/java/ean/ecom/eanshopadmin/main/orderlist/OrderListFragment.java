package ean.ecom.eanshopadmin.main.orderlist;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.StaticMethods;

import static ean.ecom.eanshopadmin.SetFragmentActivity.setFragmentActivity;
import static ean.ecom.eanshopadmin.database.DBQuery.orderListModelList;
import static ean.ecom.eanshopadmin.other.StaticValues.ORDER_LIST_CHECK;

/**
 * Created by Shailendra (WackyCodes) on 30/07/2020 23:02
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderListFragment extends Fragment {

    public OrderListFragment() {
        // Required empty public constructor
    }

    private Dialog dialog;
    private RecyclerView orderListRecycler;
    public static TextView no_order_text;
    public static TextView totalOrders;

    public static OrderListAdaptor orderListAdaptor;

    private Spinner orderFromSpinner;
    private Spinner sortBySpinner;

    private String orderFrom;
    private String sortBy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_order_list, container, false );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull( setFragmentActivity.getSupportActionBar() ).setTitle( "Order List" );
        }

        dialog = DialogsClass.getDialog( getContext() );
        // Assign ...
        orderListRecycler = view.findViewById( R.id.order_list_recycler );
        orderFromSpinner = view.findViewById( R.id.order_from_spinner );
        sortBySpinner = view.findViewById( R.id.sort_by_spinner );
        no_order_text = view.findViewById( R.id.no_order_text );
        totalOrders = view.findViewById( R.id.total_item_text );

        // Set Layout Manager..
        LinearLayoutManager layoutManager = new LinearLayoutManager( view.getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        orderListRecycler.setLayoutManager( layoutManager );

        // set Adaptor...
        orderListAdaptor = new OrderListAdaptor( orderListModelList, ORDER_LIST_CHECK );
        orderListRecycler.setAdapter( orderListAdaptor );
        orderListAdaptor.notifyDataSetChanged();

        // Spinner Set...
        setSpinner();

        return view;
    }

    private void setSpinner(){

        // Select Order From...
        ArrayAdapter <String> dataAdapter = new ArrayAdapter <String>( getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray( R.array.order_from ));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderFromSpinner.setAdapter(dataAdapter);
        orderFromSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                switch (position){
                    case 0: // Today's Order
//                        orderFrom = StaticMethods.getCrrDate(); // "yyyy/MM/dd"
//                        orderFrom =  StaticMethods.getDateUnder31(2).replace( "/", "" ).trim() + "000000";
                        orderFrom = StaticMethods.getCrrDate().replace( "/", "" ).trim() + "000000";
                        break;
                    case 1: // Last 2 days
//                        orderFrom = StaticMethods.getDateUnder31( 1 ); // in return value we include from last date to current date i.e. 2 days
                        orderFrom =  StaticMethods.getDateUnder31(1).replace( "/", "" ).trim() + "000000";
                        break;
                    case 2: // Last 5 days
//                        orderFrom = StaticMethods.getDateUnder31( 4 );
                        orderFrom =  StaticMethods.getDateUnder31(4).replace( "/", "" ).trim() + "000000";
                        break;
                    case 3: // Last 7 days
//                        orderFrom = StaticMethods.getDateUnder31( 6 );
                        orderFrom =  StaticMethods.getDateUnder31(6).replace( "/", "" ).trim() + "000000";
                        break;
                    case 4: // Last 30 days
//                        orderFrom = StaticMethods.getDateUnder31( 29 );
                        orderFrom =  StaticMethods.getDateUnder31(29).replace( "/", "" ).trim() + "000000";
                        break;
                    default:
                        break;
                }
                dialog.show();
                DBQuery.getOrderListQuery( dialog, orderFrom );
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//
            }
        } );
        // -----------

    }


}
