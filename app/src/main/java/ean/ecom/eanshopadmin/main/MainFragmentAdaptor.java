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
import ean.ecom.eanshopadmin.main.favcutomers.FavCustomersFragment;
import ean.ecom.eanshopadmin.main.income.IncomeFragment;
import ean.ecom.eanshopadmin.main.selling.SellingFragment;
import ean.ecom.eanshopadmin.main.servicearea.ServiceAreaFragment;
import ean.ecom.eanshopadmin.main.shop.AboutShopActivity;
import ean.ecom.eanshopadmin.main.shoprating.ShopRatingFragment;
import ean.ecom.eanshopadmin.notification.NotificationFragment;

import static ean.ecom.eanshopadmin.main.MainFragment.mainPageList;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_NOTIFICATION;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_FAVORITE_CUST;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_HOME;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_INCOME_RECORDS;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_ORDER_LIST;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SELLING_RECORDS;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SERVICE_AREA;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SHOP;
import static ean.ecom.eanshopadmin.other.StaticValues.REQUEST_TO_VIEW_SHOP_RATING;

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
            case REQUEST_TO_VIEW_HOME:
                Intent viewHomeIntent = new Intent( context, HomeActivity.class );
                context.startActivity( viewHomeIntent );
                break;
            case REQUEST_TO_VIEW_ORDER_LIST:
                Intent orderListIntent = new Intent( context, SetFragmentActivity.class );
                orderListIntent.putExtra( "FRAGMENT_ID", REQUEST_TO_VIEW_ORDER_LIST );
                context.startActivity( orderListIntent );
                break;
            case REQUEST_TO_VIEW_INCOME_RECORDS:
                Intent incomeIntent = new Intent( context, SetFragmentActivity.class );
                incomeIntent.putExtra( "FRAGMENT_ID",  REQUEST_TO_VIEW_INCOME_RECORDS);
                context.startActivity( incomeIntent );
                break;
            case REQUEST_TO_VIEW_SELLING_RECORDS:
                Intent sellingIntent = new Intent( context, SetFragmentActivity.class );
                sellingIntent.putExtra( "FRAGMENT_ID",  REQUEST_TO_VIEW_SELLING_RECORDS);
                context.startActivity( sellingIntent );
                break;
            case REQUEST_TO_VIEW_SHOP:
                Intent shopProfileIntent = new Intent( context, AboutShopActivity.class );
                context.startActivity( shopProfileIntent );
                break;
            case REQUEST_TO_VIEW_SHOP_RATING:
                Intent ratingIntent = new Intent( context, SetFragmentActivity.class );
                ratingIntent.putExtra( "FRAGMENT_ID",  REQUEST_TO_VIEW_SHOP_RATING);
                context.startActivity( ratingIntent );
                break;
            case REQUEST_TO_VIEW_SERVICE_AREA:
                Intent areaIntent = new Intent( context, SetFragmentActivity.class );
                areaIntent.putExtra( "FRAGMENT_ID",  REQUEST_TO_VIEW_SERVICE_AREA);
                context.startActivity( areaIntent );
                break;
            case REQUEST_TO_VIEW_FAVORITE_CUST:
                Intent favCustIntent = new Intent( context, SetFragmentActivity.class );
                favCustIntent.putExtra( "FRAGMENT_ID",  REQUEST_TO_VIEW_FAVORITE_CUST);
                context.startActivity( favCustIntent );
                break;
            default:
                Toast.makeText( context, "Code not found!", Toast.LENGTH_SHORT ).show();
                break;
        }
    }


}
