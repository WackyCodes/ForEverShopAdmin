package ean.ecom.eanshopadmin.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.SetFragmentActivity;
import ean.ecom.eanshopadmin.home.HomeActivity;
import ean.ecom.eanshopadmin.main.shop.AboutShopActivity;

import static ean.ecom.eanshopadmin.main.MainFragment.mainPageList;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_HOME;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_ORDER_LIST;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SHOP;

public class MainFragmentAdaptor extends BaseAdapter {

    public MainFragmentAdaptor() {
    }

    @Override
    public int getCount() {
        return mainPageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.main_grid_view_item, null );
        ImageView itemImage = view.findViewById( R.id.image );
        TextView itemName =  view.findViewById( R.id.name );

        itemImage.setImageResource( mainPageList.get( position ).getImage() );
        itemName.setText( mainPageList.get( position ).getName() );

        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast(parent.getContext(), "Code Not found!");
                setOnClick( parent.getContext(), mainPageList.get( position ).getID() );
            }
        } );

        return view;

    }

    private void setOnClick(Context context, int ID){
        switch (ID){
            case REQUEST_TO_VIEW_SHOP:
                Intent shopProfileIntent = new Intent( context, AboutShopActivity.class );
                context.startActivity( shopProfileIntent );
                break;
            case REQUEST_TO_VIEW_HOME:
                Intent viewHomeIntent = new Intent( context, HomeActivity.class );
                context.startActivity( viewHomeIntent );
                break;
            case REQUEST_TO_VIEW_ORDER_LIST:
                Intent orderListIntent = new Intent( context, SetFragmentActivity.class );
                orderListIntent.putExtra( "FRAGMENT_NO", REQUEST_TO_VIEW_ORDER_LIST );
                context.startActivity( orderListIntent );
                break;
            default:
                Toast.makeText( context, "Code not found!", Toast.LENGTH_SHORT ).show();
                break;
        }
    }

}
