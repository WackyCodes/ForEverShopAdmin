package ean.ecom.eanshopadmin.home.bannerslider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.addnew.AddNewLayoutActivity;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.model.BannerModel;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.UpdateImages;
import ean.ecom.eanshopadmin.product.productview.ProductDetails;

import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.other.StaticValues.BANNER_CLICK_TYPE_CATEGORY;
import static ean.ecom.eanshopadmin.other.StaticValues.BANNER_CLICK_TYPE_PRODUCT;
import static ean.ecom.eanshopadmin.other.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;

public class BannerSliderAdaptor  extends RecyclerView.Adapter<BannerSliderAdaptor.ViewHolder> {

    private int catIndex;
    private int layoutIndex;
    private String catOrCollectionID;
    private List <BannerModel> bannerModelList;
    private boolean isViewAll;

    public BannerSliderAdaptor(int catIndex, int layoutIndex, String catOrCollectionID, List <BannerModel> bannerModelList, boolean isViewAll) {
        this.catIndex = catIndex;
        this.layoutIndex = layoutIndex;
        this.catOrCollectionID = catOrCollectionID;
        this.bannerModelList = bannerModelList;
        this.isViewAll = isViewAll;
    }

    @NonNull
    @Override
    public BannerSliderAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bannerView =  LayoutInflater.from( parent.getContext() ).inflate( R.layout.banner_slider_item_layout, parent, false );
        return new ViewHolder( bannerView );
    }

    @Override
    public void onBindViewHolder(@NonNull BannerSliderAdaptor.ViewHolder holder, int position) {
        if (position < bannerModelList.size() ){
            String imgLink = bannerModelList.get( position ).getImageLink();
//            String bgColor = bannerAndCatModelList.get( position ).getTitleOrBgColor();
            String clickID = bannerModelList.get( position ).getClickID();
            int clickType = bannerModelList.get( position ).getClickType();
            holder.setData( imgLink, clickID, clickType, position);
        }
        if ( !isViewAll && position == bannerModelList.size() ){
            holder.setAddNew();
        }
    }

    @Override
    public int getItemCount() {
        if (isViewAll){
            return bannerModelList.size();
        }else {
            return bannerModelList.size()+1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView bannerImage;
        private LinearLayout addNewItemLayout;
        private LinearLayout bannerUpdateLayout;

        private ImageView bannerUpBtn;
        private ImageView bannerDownBtn;
        private ImageView bannerDeleteBtn;
        private Dialog dialog;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerImage = itemView.findViewById( R.id.banner_slider_Image );
            addNewItemLayout = itemView.findViewById( R.id.add_new_item_Linearlayout );

            bannerUpBtn = itemView.findViewById( R.id.banner_index_up_img_view );
            bannerDownBtn = itemView.findViewById( R.id.banner_index_down_img_view );
            bannerDeleteBtn = itemView.findViewById( R.id.banner_delete_img_view );
            bannerUpdateLayout = itemView.findViewById( R.id.banner_update_layout );
            dialog = DialogsClass.getDialog( itemView.getContext() );
        }

        private void setData(String imgLink, final String clickID, final int clickType, final int index ){
            if (isViewAll){
                bannerUpdateLayout.setVisibility( View.VISIBLE );
            }else{
                bannerUpdateLayout.setVisibility( View.GONE );
            }

            bannerImage.setVisibility( View.VISIBLE );
            addNewItemLayout.setVisibility( View.GONE );
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_panorama_black_24dp ) ).into( bannerImage );
            // Set Up Down Indicator ...
            setIndexUpDownVisibility( index, bannerUpBtn, bannerDownBtn);

            bannerImage.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch ( clickType ){
                        case BANNER_CLICK_TYPE_PRODUCT:
                            Intent productDetailIntent = new Intent( itemView.getContext(), ProductDetails.class );
                            productDetailIntent.putExtra( "PRODUCT_ID", clickID );
//                            productDetailIntent.putExtra( "HOME_CAT_INDEX", catIndex );
//                            productDetailIntent.putExtra( "LAYOUT_INDEX", layoutIndex );
//                            productDetailIntent.putExtra( "PRODUCT_INDEX", proIndex );
                            itemView.getContext().startActivity( productDetailIntent );
                            break;
                        case BANNER_CLICK_TYPE_CATEGORY:
                        default:
                            break;
                    }
//                    Toast.makeText( itemView.getContext(), "Method is not written", Toast.LENGTH_SHORT ).show();
                }
            } );

            bannerUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update...
                    dialog.show();
                    updateIndex(true, index );
                }
            } );
            bannerDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update...
                    dialog.show();
                    updateIndex(false, index );
                }
            } );

            // DELETE Banner...
            bannerDeleteBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = DialogsClass.alertDialog( itemView.getContext(),
                            "Alert!", "Do You want to delete this banner?" );
                    alertDialog.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            dialog.show();
                            UpdateImages.deleteImageFromFirebase( itemView.getContext(), dialog
                                    , "SHOPS/" + SHOP_ID + "/banners"
                                    , bannerModelList.get( index ).getDeleteID()  );
                            // Update...
                            Map<String, Object > updateMap = new HashMap <>();
//                          updateMap.put( "delete_id_"+ (index + 1), bannerAndCatModelList.get( index ).getLayoutId() ); // String
                            updateMap.put( "no_of_banners", (bannerModelList.size() - 1) ); // int

                            // update... For list
                            int tempIndex = 0;
                            for (int pos = 0; pos < bannerModelList.size(); pos++){
                                if (pos == index ){
                                    continue;
                                }else{
                                    tempIndex = tempIndex + 1;
                                }
                                updateMap.put( "banner_"+  tempIndex,  bannerModelList.get( ( pos ) ).getImageLink()  ); // String image
                                updateMap.put( "banner_click_id_"+ tempIndex,  bannerModelList.get( ( pos ) ).getClickID()  ); // String
                                updateMap.put( "banner_click_type_"+ tempIndex,  bannerModelList.get( ( pos ) ).getClickType() ); // int
                                updateMap.put( "delete_id_"+ tempIndex, bannerModelList.get( ( pos ) ).getDeleteID() ); // String
                            }

                            if (bannerModelList.size() <= 2){
                                updateMap.put( "is_visible", false );
                            }
                            // Remove from Local...
                            bannerModelList.remove( index );
                            // Update On Database...
                            updateOnDatabase(itemView.getContext(), dialog, updateMap);
                        }
                    } );

                    alertDialog.setNegativeButton( "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }
                    } );
                    alertDialog.show();
                }
            } );

        }

        private void setAddNew(){
            bannerUpdateLayout.setVisibility( View.GONE );
            bannerImage.setVisibility( View.GONE );
            addNewItemLayout.setVisibility( View.VISIBLE );
            addNewItemLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent( itemView.getContext(), AddNewLayoutActivity.class );
                    intent.putExtra( "LAY_TYPE", BANNER_SLIDER_CONTAINER_ITEM );
                    intent.putExtra( "LAY_INDEX", layoutIndex );
                    intent.putExtra( "TASK_UPDATE", true );
                    intent.putExtra( "CAT_INDEX", catIndex );
                    itemView.getContext().startActivity( intent );
                }
            } );
        }

        private void setIndexUpDownVisibility( int index, ImageView indexUpBtn,  ImageView indexDownBtn){
            if (bannerModelList.size()>1){
                indexUpBtn.setVisibility( View.VISIBLE );
                indexDownBtn.setVisibility( View.VISIBLE );
                if (index == 0){
                    indexUpBtn.setVisibility( View.INVISIBLE );
                }else if (index == bannerModelList.size()-1){
                    indexDownBtn.setVisibility( View.INVISIBLE );
                }
            }else{
                indexUpBtn.setVisibility( View.INVISIBLE );
                indexDownBtn.setVisibility( View.INVISIBLE );
            }
        }
        private void updateIndex(boolean isMoveUp, int index){
            if (isMoveUp){
                // GoTo Up...
                createMapForUpdateIndex( index, index -1 );
            }else{
                // Goto Down...
                createMapForUpdateIndex( index, index +1 );
            }
        }

        private void createMapForUpdateIndex( int indexUp, int indexDown){
            // IndexUp : Which is Going to Up... After Update...
            // IndexDown : Which is Going to Down...
            Map <String, Object > updateMap = new HashMap <>();
            // UpIndex...
            updateMap.put( "banner_"+ (indexUp+1) , bannerModelList.get( ( indexDown ) ).getImageLink() ); // String image
            updateMap.put( "banner_click_id_"+ (indexUp+1),  bannerModelList.get( ( indexDown ) ).getClickID()  ); // String
            updateMap.put( "banner_click_type_"+ (indexUp+1),  bannerModelList.get( ( indexDown ) ).getClickType()  ); // int
            updateMap.put( "delete_id_"+ (indexUp+1), bannerModelList.get( ( indexDown ) ).getDeleteID() ); // String
            // Down Index..
            updateMap.put( "banner_"+ (indexDown+1) ,  bannerModelList.get( ( indexUp ) ).getImageLink()  ); // String image
            updateMap.put( "banner_click_id_"+ (indexDown+1),  bannerModelList.get( ( indexUp ) ).getClickID()  ); // String
            updateMap.put( "banner_click_type_"+ (indexDown+1),  bannerModelList.get( ( indexUp ) ).getClickType() ); // int
            updateMap.put( "delete_id_"+ (indexDown+1), bannerModelList.get( ( indexUp ) ).getDeleteID() ); // String
            // Update in local list...
            Collections.swap( bannerModelList, indexUp, indexDown );
            // Request to DataBase...
            updateOnDatabase( itemView.getContext(), dialog, updateMap );

        }

    }

    private void showToast(Context context, String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }


    private void updateOnDatabase(final Context context, final Dialog dialog, Map<String, Object> updateMap){
        // Map...
        String layoutID = DBQuery.homeCatListModelList.get( catIndex ).getHomeListModelList().get( layoutIndex ).getLayoutID();
        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .collection( catOrCollectionID ).document( layoutID )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            showToast( context, "Update Successfully!" );
                        }else{
                            showToast( context, "Failed!, Something went wrong" );
                        }
                        BannerSliderAdaptor.this.notifyDataSetChanged(); // Notify Data Changed...
                        dialog.dismiss();
                    }
                } );

    }

}
