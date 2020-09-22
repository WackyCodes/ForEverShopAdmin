package ean.ecom.eanshopadmin.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.addnew.AddNewImageActivity;
import ean.ecom.eanshopadmin.addnew.AddNewProductActivity;
import ean.ecom.eanshopadmin.database.DBQuery;
import ean.ecom.eanshopadmin.home.bannerslider.BannerSliderAdaptor;
import ean.ecom.eanshopadmin.home.viewall.ViewAllActivity;
import ean.ecom.eanshopadmin.home.viewall.ViewAllBannerSliderActivity;
import ean.ecom.eanshopadmin.model.BannerModel;
import ean.ecom.eanshopadmin.other.DialogsClass;
import ean.ecom.eanshopadmin.other.MyGridView;
import ean.ecom.eanshopadmin.other.MyImageView;
import ean.ecom.eanshopadmin.product.ProductModel;
import ean.ecom.eanshopadmin.product.ProductSubModel;
import ean.ecom.eanshopadmin.product.horizontal.ProductHrGridAdaptor;
import ean.ecom.eanshopadmin.product.productview.ProductDetails;

import static ean.ecom.eanshopadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanshopadmin.database.DBQuery.homeCatListModelList;
import static ean.ecom.eanshopadmin.home.HomeActivity.homeActivity;
import static ean.ecom.eanshopadmin.other.StaticMethods.getTwoDigitRandom;
import static ean.ecom.eanshopadmin.other.StaticMethods.showToast;
import static ean.ecom.eanshopadmin.other.StaticValues.BANNER_CLICK_TYPE_CATEGORY;
import static ean.ecom.eanshopadmin.other.StaticValues.BANNER_CLICK_TYPE_PRODUCT;
import static ean.ecom.eanshopadmin.other.StaticValues.GRID_PRODUCTS_LAYOUT_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_BANNER_SLIDER_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_CAT_LIST_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_HOME_STRIP_AD_CONTAINER;
import static ean.ecom.eanshopadmin.other.StaticValues.SHOP_ID;
import static ean.ecom.eanshopadmin.other.StaticValues.VIEW_GRID_LAYOUT;
import static ean.ecom.eanshopadmin.other.StaticValues.VIEW_HORIZONTAL_LAYOUT;
import static ean.ecom.eanshopadmin.other.StaticValues.VIEW_RECTANGLE_LAYOUT;

public class HomePageAdaptor extends RecyclerView.Adapter {

    private int catIndex; // category index
    private String catTitle; // category name or title
    private String catID;
    private List <HomeListModel> homePageList;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public HomePageAdaptor(int catIndex, List <HomeListModel> homePageList) {
        this.catIndex = catIndex;
        this.catTitle = homeCatListModelList.get( catIndex ).getCatName();
        this.catID = homeCatListModelList.get( catIndex ).getCatID();
        this.homePageList = homePageList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageList.get( position ).getLayoutType()) {
            case SHOP_HOME_BANNER_SLIDER_CONTAINER:           //-- 2
                return SHOP_HOME_BANNER_SLIDER_CONTAINER;
            case SHOP_HOME_STRIP_AD_CONTAINER:         //-- 1
                return SHOP_HOME_STRIP_AD_CONTAINER;
            case SHOP_HOME_CAT_LIST_CONTAINER:            //-- 5
                return SHOP_HOME_CAT_LIST_CONTAINER;
            case HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER:            //-- 3
                return HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER;
            case GRID_PRODUCTS_LAYOUT_CONTAINER:            //-- 4
                return GRID_PRODUCTS_LAYOUT_CONTAINER;
            // Add New Items...
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
                // Banner Slider...
            case SHOP_HOME_BANNER_SLIDER_CONTAINER:
                View bannerSliderView = LayoutInflater.from( parent.getContext() ).inflate(
                    R.layout.horizontal_recycler_layout, parent, false );
                return new BannerSliderViewHolder( bannerSliderView );
                //  Strip ad Layout
            case SHOP_HOME_STRIP_AD_CONTAINER:
                View stripAdView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.strip_ad_item_layout, parent, false );
                return new StripAdViewHolder( stripAdView );
                // Category Layout...
            case SHOP_HOME_CAT_LIST_CONTAINER:
                View catView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.category_item_layout, parent, false );
                return new CategoryViewHolder( catView );
            case HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER:
                View productsHrView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.horizontal_recycler_layout, parent, false );
                return new ProductHorizontalViewHolder( productsHrView );
            case GRID_PRODUCTS_LAYOUT_CONTAINER:
                View productsGridView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.products_grid_layout, parent, false );
                return new ProductGridViewHolder( productsGridView );
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (homePageList.get( position ).getLayoutType()){
            case SHOP_HOME_BANNER_SLIDER_CONTAINER:
                List<BannerModel> bannerList = homePageList.get( position ).getBannerModelList();
                String bannerLayoutId = homePageList.get( position ).getLayoutID();
                ((BannerSliderViewHolder)holder).setData( bannerLayoutId, bannerList, position );
                break;
            case SHOP_HOME_STRIP_AD_CONTAINER:
                BannerModel stripAdModel = homePageList.get( position ).getBannerModel();
                String layoutID = homePageList.get( position ).getLayoutID();
                String stripAdImg = stripAdModel.getImageLink();
                String clickID = stripAdModel.getClickID();
                int clickType = stripAdModel.getClickType();
                String deleteID = stripAdModel.getDeleteID();
                String extraText = stripAdModel.getNameOrExtraText();
                ((StripAdViewHolder)holder).setStripAdData( stripAdImg, layoutID, clickID, clickType, deleteID, extraText, position );
                break;
            case SHOP_HOME_CAT_LIST_CONTAINER:
                List<BannerModel> catList = homePageList.get( position ).getBannerModelList();
                String catLayoutId = homePageList.get( position ).getLayoutID();
                ((CategoryViewHolder)holder).setData( catLayoutId, catList, position );
                break;
            case HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER:
                List<ProductModel> productModelList = homePageList.get( position ).getProductModelList();
                List<String> productIDList = homePageList.get( position ).getProductIdList();
                String productLayId = homePageList.get( position ).getLayoutID();
                String layoutTitle = homePageList.get( position ).getProductLayoutTitle();
                ((ProductHorizontalViewHolder)holder).setData( productLayId, layoutTitle, productIDList, productModelList, position );
                break;
            case GRID_PRODUCTS_LAYOUT_CONTAINER:
                List<ProductModel> productModelGridList = homePageList.get( position ).getProductModelList();
                List<String> productIDGridList = homePageList.get( position ).getProductIdList();
                String productGridLayId = homePageList.get( position ).getLayoutID();
                String layoutGridTitle = homePageList.get( position ).getProductLayoutTitle();
                ((ProductGridViewHolder)holder).setData( productGridLayId, layoutGridTitle, productIDGridList, productModelGridList, position );
                break;
        }

    }

    @Override
    public int getItemCount() {
        return homePageList.size();
    }

    //============  Strip ad  View Holder ============
    public class StripAdViewHolder extends RecyclerView.ViewHolder{
        private MyImageView stripAdImage;
        private TextView indexNo;
        private ImageView editLayoutBtn;
        private int defaultColor;
        private int layoutPosition;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private Switch visibleBtn;
        private Dialog dialog;

        public StripAdViewHolder(@NonNull View itemView) {
            super( itemView );
            stripAdImage = itemView.findViewById( R.id.strip_ad_image );
            indexNo = itemView.findViewById( R.id.strip_ad_index );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            defaultColor = ContextCompat.getColor( itemView.getContext(), R.color.colorGray);
            dialog = DialogsClass.getDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
        }
        private void setStripAdData(String imgLink, final String layoutID, final String clickID, final int clickType, final String deleteID, String extraText, final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "Ad Banner \nposition : " + layoutPosition );
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                stripAdImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( colorCode ) ));
//            }
            // set Image Resource from database..
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_panorama_black_24dp ) ).into( stripAdImage );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete...
                    alertDialog( itemView.getContext(), index, layoutID, "SHOPS/" + SHOP_ID +"/ads",  deleteID );
                }
            } );

            // Click Listener...
            stripAdImage.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showToast( "Code Not Found!", itemView.getContext() );
//                   Click Type
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
                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );

        }


    }
    //============  Strip ad  View Holder ============

    //==============  GridProduct Grid Layout View Holder =================
    public class CategoryViewHolder extends  RecyclerView.ViewHolder{
        private MyGridView gridLayout;
        private TextView gridLayoutTitle;
        private TextView indexNo;
        private Button gridLayoutViewAllBtn;
        private TextView warningText;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private ImageView editLayoutBtn;
        private Switch visibleBtn;
        private int temp = 0;
        private Dialog dialog;
        private int layoutPosition;
        private String layoutID;

        public CategoryViewHolder(@NonNull View itemView) {
            super( itemView );
            gridLayout = itemView.findViewById( R.id.category_my_grid_view );
            gridLayoutTitle = itemView.findViewById( R.id.gridLayoutTitle );
            indexNo = itemView.findViewById( R.id.gridIndexNo );
            gridLayoutViewAllBtn = itemView.findViewById( R.id.gridViewAllBtn );
            warningText = itemView.findViewById( R.id.grid_warning_text );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            dialog = DialogsClass.getDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
            gridLayoutViewAllBtn.setVisibility( View.INVISIBLE );
        }

        private void setData(final String layoutID, List<BannerModel> catList, final int index ){
            layoutPosition = 1 + index;
            indexNo.setText( "position : "+ layoutPosition );
            SetCategoryItem setCategoryItem = new SetCategoryItem( catList, index );
            gridLayout.setAdapter( setCategoryItem );
            setCategoryItem.notifyDataSetChanged();
            this.layoutID = layoutID;

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete...
//                    alertDialog( itemView.getContext(), index, layoutID, "/HOME/ads",  deleteID );
                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );
        }

        private class SetCategoryItem extends BaseAdapter {
            List<BannerModel> categoryTypeModelList;
            int index;
            public SetCategoryItem(List <BannerModel> categoryTypeModelList, int index) {
                this.categoryTypeModelList = categoryTypeModelList;
                this.index = index;
            }

            @Override
            public int getCount() {
                return categoryTypeModelList.size() + 1;
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
                View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.square_category_layout_item, null );
                LinearLayout updateLayout = view.findViewById( R.id.sq_update_layout );
                TextView updateImage = view.findViewById( R.id.sq_update_image );
                TextView updateName = view.findViewById( R.id.sq_update_text );
                if (position < categoryTypeModelList.size()){
                    ImageView itemImage = view.findViewById( R.id.sq_image_view );
                    TextView itemName =  view.findViewById( R.id.sq_text_view );
                    updateLayout.setVisibility( View.VISIBLE );
//                itemImage.setImageResource( Integer.parseInt( categoryTypeModelList.get( position ).getCatImage() ) );
                    Glide.with( itemView.getContext() ).load( categoryTypeModelList.get( position ).getImageLink() )
                            .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( itemImage );
                    itemName.setText( categoryTypeModelList.get( position ).getNameOrExtraText() );

                    itemImage.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onCategoryClick( parent.getContext(), categoryTypeModelList.get( position ).getClickID() );
                        }
                    } );

                    // Update...
                    updateName.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogForCategory( parent.getContext(), position, categoryTypeModelList.get( position ).getNameOrExtraText(), false  );
                        }
                    } );
                    updateImage.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // : Update Image....
                            Intent intent = new Intent( parent.getContext(), AddNewImageActivity.class );
                            intent.putExtra( "UPLOAD_CAT_ID", catID );
                            intent.putExtra( "LAY_ID", layoutID );
                            intent.putExtra( "CAT_NO", position );
                            intent.putExtra( "LOCAL_CAT_INDEX", catIndex );
                            intent.putExtra( "LOCAL_LAY_INDEX", index );
                            parent.getContext().startActivity( intent );
                        }
                    } );

                    return view;
                }else{
                    updateLayout.setVisibility( View.INVISIBLE );
                    ImageView itemImage = view.findViewById( R.id.sq_image_view );
                    TextView itemName =  view.findViewById( R.id.sq_text_view );
                    itemImage.setImageResource( R.drawable.ic_add_black_24dp );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        itemImage.setImageTintList( parent.getContext().getResources().getColorStateList( R.color.colorPrimary ) );
                    }
                    itemName.setText( "Add New" );
                    itemImage.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            showToast( "Code Not Found!", parent.getContext() );
                            dialogForCategory( parent.getContext(), categoryTypeModelList.size()+1, null, true  );
                        }
                    } );
                    return view;
                }
            }

            private void onCategoryClick(Context context, String docID){
//                Intent intent = new Intent( context, new  )
                int tempIndex = 0;
//                String catName = "";
                for (HomeCatListModel temp : homeCatListModelList){
                    if ( temp.getCatID().equals( docID ) ){
//                        catName = temp.getCatName();
                        replaceFragment( new HomeFragment( tempIndex, docID, temp.getCatName() ) );
                        break;
                    }
                    tempIndex++;
                }
            }

            private void dialogForCategory(Context context, final int catNo,@Nullable String updateCatName, final boolean isNew ){
                final Dialog progressDialog = new Dialog( context );
                progressDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                progressDialog.setContentView( R.layout.dialog_single_edit_text );
                progressDialog.setCancelable( false );
                progressDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );

                TextView  dTitle = progressDialog.findViewById( R.id.dialog_title );
                final EditText dEditText = progressDialog.findViewById( R.id.dialog_editText );
                Button  dOkBtn = progressDialog.findViewById( R.id.dialog_ok_btn );
                Button  dCancelBtn = progressDialog.findViewById( R.id.dialog_cancel_btn );

                dTitle.setText( "Enter Category Name" );
                if (isNew){
                    dEditText.setHint( "Category Name" );
                    dOkBtn.setText( "Add" );
                }else{
                    dEditText.setText( updateCatName );
                    dOkBtn.setText( "Update" );
                }

                dOkBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty( dEditText.getText().toString() )){
                            dEditText.setError( "Required!" );
                        }else if (isValidName(dEditText)){
                            dialog.show();
                            Map<String, Object> uploadMap = new HashMap <>();
                            if (isNew){
                                String catID = getNewCatID(dEditText.getText().toString());
                                homeCatListModelList.add( new HomeCatListModel( catID, dEditText.getText().toString(), new ArrayList <HomeListModel>()  ) );
                                categoryTypeModelList.add( new BannerModel(
                                        // Add in Local List...
                                        -1, catID,"",  dEditText.getText().toString(),
                                        "delete ID" ) );
                                // new
                                uploadMap.put( "no_of_cat", catNo );
                                uploadMap.put( "cat_id_"+catNo, catID );
                                uploadMap.put( "cat_image_"+catNo, "" );
                                uploadMap.put( "cat_name_"+catNo, dEditText.getText().toString() );
                                uploadMap.put( "cat_delete_id_"+catNo, catID );

                            }else{
                                categoryTypeModelList.get( catNo).setNameOrExtraText( dEditText.getText().toString() );
                                uploadMap.put( "cat_name_" + (catNo+1) , dEditText.getText().toString() );
                            }
                            // Request...
                            updateOnDocument(dialog, layoutID, uploadMap);
                            progressDialog.dismiss();
                        }
                    }
                } );
                dCancelBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.dismiss();
                    }
                } );
                progressDialog.show();
            }

        }

        private String getNewCatID(String catName){
            if (catName.length() <= 5 ){
                catName = catName.toUpperCase().replace( " ", "" )+ "_" + getTwoDigitRandom();
                return catName;
            }else{
                catName = catName.toUpperCase().replace( " ", "" );
                return catName.trim();
            }
        }

        private boolean isValidName( EditText editText){
            String catName = editText.getText().toString();
            if (catName.toUpperCase().equals( "HOME" ) || catName.toUpperCase().equals( "ADMINS" ) || catName.toUpperCase().equals( "ORDERS" ) ||
                    catName.toUpperCase().equals( "PRODUCTS" ) || catName.toUpperCase().equals( "NOTIFICATIONS" )){
                editText.setError( "This Name reserved!" );
                return false;
            }else{
                return true;
            }
        }

        private void replaceFragment(Fragment fragment){
            FragmentTransaction fragmentTransaction = homeActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
            fragmentTransaction.replace( HomeActivity.homeActivityFrame.getId(), fragment );
            fragmentTransaction.commit();
        }


    }
    //==============  GridProduct Grid Layout View Holder =================

    //============  Banner Slider View Holder ============
    public class BannerSliderViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView bannerRecyclerView;
        private TextView layoutTitle;
        private TextView indexNo;
        private Button layoutViewAllBtn;
        private TextView warningText;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private ImageView deleteLayoutBtn;
        private Switch visibleBtn;
        private int temp = 0;
        private Dialog dialog;
        private int layoutPosition;
        private String layoutID;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerRecyclerView = itemView.findViewById( R.id.horizontal_recycler );
            layoutTitle = itemView.findViewById( R.id.gridLayoutTitle );
            indexNo = itemView.findViewById( R.id.gridIndexNo );
            layoutViewAllBtn = itemView.findViewById( R.id.gridViewAllBtn );
            warningText = itemView.findViewById( R.id.grid_warning_text );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            deleteLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            dialog = DialogsClass.getDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
        }

        private void setData(final String layoutID, final List<BannerModel> bannerList, final int index){
            // TODO : Set Data....
            layoutPosition = 1 + index;
            indexNo.setText( "position : "+ layoutPosition);
            layoutTitle.setText( "Slider Banners " + " (" + bannerList.size() + ")" );
            if (bannerList.size()>=2){
                warningText.setVisibility( View.GONE );
                layoutViewAllBtn.setVisibility( View.VISIBLE );
            }else{
                warningText.setVisibility( View.VISIBLE );
                warningText.setText( "Add 2 or more banner to visible this layout to the customers!!" );
            }

            BannerSliderAdaptor bannerItemAdaptor = new BannerSliderAdaptor( catIndex, index ,catID,  bannerList , false );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            bannerRecyclerView.setLayoutManager( linearLayoutManager );
            bannerRecyclerView.setAdapter( bannerItemAdaptor );
            bannerItemAdaptor.notifyDataSetChanged();

            layoutViewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( itemView.getContext(), ViewAllBannerSliderActivity.class );
                    intent.putExtra( "CAT_INDEX", catIndex  );
                    intent.putExtra( "LAY_INDEX", index  );
                    itemView.getContext().startActivity( intent );
                }
            } );

            deleteLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete layout
                    if (bannerList.size() <= 1){
                        if (bannerList.size() == 0)
                            alertDialog( itemView.getContext(), index, layoutID, null,null  );
                        else
                            alertDialog( itemView.getContext(), index, layoutID, "SHOPS/" + SHOP_ID +"/banners", bannerList.get( 0 ).getDeleteID()  );
                    }else{
                        showToast( "You have more than 1 banner in this layout!", itemView.getContext() );
                    }
                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );



        }

    }
    //============  Banner Slider View Holder ============

    //============  Product Horizontal View Holder ============
    public class ProductHorizontalViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView productRecyclerView;
        private TextView layoutTitle;
        private TextView indexNo;
        private Button layoutViewAllBtn;
        private TextView warningText;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private ImageView deleteLayoutBtn;
        private ImageView editTitleBtn;// edit_title_image_btn
        private Switch visibleBtn;
        private int temp = 0;
        private Dialog dialog;
        private int layoutPosition;
        private String layoutID;
        private ProductHrGridAdaptor productHrGridAdaptor;
        List<ProductModel> tempProductModelList = new ArrayList <>();

        public ProductHorizontalViewHolder(@NonNull View itemView) {
            super( itemView );
            productRecyclerView = itemView.findViewById( R.id.horizontal_recycler );
            layoutTitle = itemView.findViewById( R.id.gridLayoutTitle );
            indexNo = itemView.findViewById( R.id.gridIndexNo );
            layoutViewAllBtn = itemView.findViewById( R.id.gridViewAllBtn );
            warningText = itemView.findViewById( R.id.grid_warning_text );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            deleteLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            editTitleBtn = itemView.findViewById( R.id.edit_title_image_btn );
            dialog = DialogsClass.getDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
        }

        private void setData(final String productLayId, final String layoutTitle, final List<String> productIDList, List<ProductModel> productModelList, final int index){
            indexNo.setText( "position : "+ (1+index) );
            this.layoutTitle.setText( layoutTitle );
            // Set Warning Text Visibility...
            if (productIDList.size()>=3){
                warningText.setVisibility( View.GONE );
            }else{
                warningText.setText( "Add Min 3 Product to Visible in the customer App!" );
                warningText.setVisibility( View.VISIBLE );
            }
            // View All Btn Visibility...
            if (productIDList.size()>=1){
                layoutViewAllBtn.setVisibility( View.VISIBLE );
            }else {
                layoutViewAllBtn.setVisibility( View.INVISIBLE );
            }

            // View All Button Click...
            layoutViewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent( itemView.getContext(), ViewAllActivity.class );
                    intent.putExtra( "CAT_INDEX", catIndex  );
                    intent.putExtra( "LAY_INDEX", index  );
                    intent.putExtra( "VIEW_TYPE", VIEW_RECTANGLE_LAYOUT  );
                    intent.putExtra( "TITLE", layoutTitle  );

                    itemView.getContext().startActivity( intent );
                }
            } );

            //--------------------------------------------------------------------------------------
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            productRecyclerView.setLayoutManager( linearLayoutManager );

            // Adaptor...
            productHrGridAdaptor = new ProductHrGridAdaptor( catIndex, index, VIEW_HORIZONTAL_LAYOUT, productModelList );
            productRecyclerView.setAdapter( productHrGridAdaptor );
            productHrGridAdaptor.notifyDataSetChanged();
            ////////

            int range = 0;
            if (productIDList.size() >= 6){
                range = 6;
            }else{
                range = productIDList.size();
            }

            for (int i = 0; i<range; i++){
                if (productModelList.size() > i){
                    productHrGridAdaptor.notifyDataSetChanged();
                }else{
                    loadProductData(index, productIDList.get( i ));
                }
            }

            //--------------------------------------------------------------------------------------
            // Delete Layout Btn Click...
            deleteLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete layout
                    if (productIDList.size() <= 1){
                        if (productIDList.size() == 0)
                            alertDialog( itemView.getContext(), index, layoutID, null,null  );
                        else
//                            alertDialog( itemView.getContext(), index, layoutID, "SHOPS/" + SHOP_ID +"/banners", bannerList.get( 0 ).getDeleteID()  );
                            showToast( "Code Not Found!", itemView.getContext() );
                    }else{
                        showToast( "You have more than 1 Product in this layout!", itemView.getContext() );
                    }
                }
            } );
            // Edit Title Btn Click
            editTitleBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogEditTitle( itemView.getContext(), index, productLayId, layoutTitle );
                }
            } );
            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );

        }

        // Load Product Data...
        private void loadProductData(final int index, final String productId){
            DBQuery.firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                    .collection( "PRODUCTS" ).document( productId )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        // access the banners from database...
                        DocumentSnapshot documentSnapshot = task.getResult();

                        if ( documentSnapshot.get( "p_no_of_variants" ) !=null ){
//                            String[] pImage;
                            int p_no_of_variants = Integer.valueOf( String.valueOf( (long) documentSnapshot.get( "p_no_of_variants" ) ) );
                            List<ProductSubModel> productSubModelList = new ArrayList <>();
                            for (int tempI = 1; tempI <= p_no_of_variants; tempI++){

                                // We can use Array...
                                ArrayList<String> Images = (ArrayList <String>) documentSnapshot.get( "p_image_" + tempI );
                                // add Data...
                                productSubModelList.add( new ProductSubModel(
                                        task.getResult().get( "p_name_"+tempI).toString(),
                                        Images,
                                        task.getResult().get( "p_selling_price_"+tempI).toString(),
                                        task.getResult().get( "p_mrp_price_"+tempI).toString(),
                                        task.getResult().get( "p_weight_"+tempI).toString(),
                                        task.getResult().get( "p_stocks_"+tempI).toString(),
                                        task.getResult().get( "p_offer_"+tempI).toString()
                                ) );
                            }
                            String p_id = task.getResult().get( "p_id").toString();
                            String p_main_name = task.getResult().get( "p_main_name" ).toString();
//                        String p_main_image = task.getResult().get( "p_main_image" ).toString();
                            String p_weight_type = task.getResult().get( "p_weight_type" ).toString();
                            int p_veg_non_type = Integer.valueOf( task.getResult().get( "p_veg_non_type" ).toString() );
                            Boolean p_is_cod = (Boolean) task.getResult().get( "p_is_cod" );


                            ProductModel productModel =  new ProductModel(
                                    p_id,
                                    p_main_name,
                                    " ",
                                    p_is_cod,
                                    String.valueOf(p_no_of_variants),
                                    p_weight_type,
                                    p_veg_non_type,
                                    productSubModelList
                            );

                            if (homeCatListModelList.get( catIndex ).getHomeListModelList().size() > index){
                                homeCatListModelList.get( catIndex ).getHomeListModelList().get( index ).getProductModelList().add( productModel );
                            }
//                            homeCatListModelList.get( catIndex ).getHomeListModelList().get( index ).setProductModelList( tempProductModelList );

                            if (productHrGridAdaptor != null) {
                                productHrGridAdaptor.notifyDataSetChanged();
                            }
                        }
                    }
                    else{
                        String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                    }
                }
            } );

        }

    }
    //============  Product Horizontal View Holder ============

    //============  Product Grid View Holder ============
    public class ProductGridViewHolder extends RecyclerView.ViewHolder{

        private GridLayout gridLayout;
        private TextView layoutTitle;
        private TextView indexNo;
        private Button layoutViewAllBtn;
        private TextView warningText;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private ImageView deleteLayoutBtn;
        private ImageView editTitleBtn;// edit_title_image_btn
        private Switch visibleBtn;
        private int temp = 0;
        private Dialog dialog;
        private int layoutPosition;
        private String layoutID;

        public ProductGridViewHolder(@NonNull View itemView) {
            super( itemView );
            gridLayout = itemView.findViewById( R.id.product_grid_layout );
            layoutTitle = itemView.findViewById( R.id.gridLayoutTitle );
            indexNo = itemView.findViewById( R.id.gridIndexNo );
            layoutViewAllBtn = itemView.findViewById( R.id.gridViewAllBtn );
            warningText = itemView.findViewById( R.id.grid_warning_text );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            deleteLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            editTitleBtn = itemView.findViewById( R.id.edit_title_image_btn );
            dialog = DialogsClass.getDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
        }

        private void setData(final String productLayId, final String layoutTitle, final List<String> productIDList, final List<ProductModel> productModelList, final int index){
            this.layoutTitle.setText( layoutTitle );
            indexNo.setText( "position : "+ (1+index) );
            // -------------------------------------------------------------------------------------
            warningText.setText( "Add min 4 products to make visible this layout to the customers.!" );
            int gridRange;
            if (productIDList.size()<3){
                gridRange = productIDList.size();
                warningText.setVisibility( View.VISIBLE );
            }
            else{
                gridRange = 3;
                if (productIDList.size()<4){
                    warningText.setVisibility( View.VISIBLE );
                }else{
                    warningText.setVisibility( View.GONE );
                }
            }

            // Load Product...
            if (productModelList.size() < gridRange){
                for (int i = productModelList.size(); i < gridRange; i++ ){
//                    loadGridProductData( index,  productIDList.get( i ) );
                    Thread newThread = new Thread(new LoadProductData( index, i, productIDList.get( i ) ));
                    newThread.start();
                }
            }

            // Set The Product and CLickListener...
            for (int i=0; i < gridRange; i++){
                if (productModelList.size() > i ){
                    ConstraintLayout itemLayout = gridLayout.getChildAt( i ).findViewById( R.id.product_view_const_layout );
                    gridLayout.getChildAt( i ).findViewById( R.id.product_view_linear_layout ).setVisibility( View.GONE ); // Add New Product INVISIBLE..
                    itemLayout.setVisibility( View.VISIBLE );
                    // Get Reference...
                    ImageView img = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_image );
                    TextView name = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_name );
                    TextView price = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_price );
                    TextView cutPrice = gridLayout.getChildAt( i ).findViewById( R.id.hr_product_cut_price );
                    TextView perOffText = gridLayout.getChildAt( i ).findViewById( R.id.hr_off_percentage );
                    TextView stocksText = gridLayout.getChildAt( i ).findViewById( R.id.stock_text );
                    // Set Data...
                    // Get SubModel....
                    ProductSubModel productSubModel = productModelList.get( i ).getProductSubModelList().get( 0 );

                    // Set img resource
                    Glide.with( itemView.getContext() ).load( productSubModel.getpImage().get( 0 )  )
                            .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( img );

                    name.setText( productSubModel.getpName() );

                    price.setText("Rs." + productSubModel.getpSellingPrice() +"/-" );
                    cutPrice.setText("Rs." + productSubModel.getpMrpPrice() +"/-" );
                    perOffText.setText( productSubModel.getpOffer() + "%Off" );
                    // Stocks Text...
                    if(Integer.parseInt( productSubModel.getpStocks() )>0){
                        stocksText.setText( "In stocks(" + productSubModel.getpStocks() + ")" );
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            stocksText.setBackgroundTintList( ColorStateList.valueOf( itemView.getResources().getColor( R.color.colorGreen ) ) );
                        }
                    }else{
                        stocksText.setText( "Out of Stocks" );
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            stocksText.setBackgroundTintList( ColorStateList.valueOf( itemView.getResources().getColor( R.color.colorRed ) ) );
                        }
                    }

                    // ClickListener...
                    itemLayout.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ( v == gridLayout.getChildAt( 0 ).findViewById( R.id.product_view_const_layout )){
                                if (productModelList.size()>0){
                                    addOnProductClick(  productModelList.get( 0 ).getpProductID(), index, 0 );
                                }
                            } else
                            if ( v == gridLayout.getChildAt( 1 ).findViewById( R.id.product_view_const_layout )){
                                if (productModelList.size()>1){
                                    addOnProductClick(  productModelList.get( 1 ).getpProductID(), index, 1 );
                                }
                            } else
                            if ( v == gridLayout.getChildAt( 2 ).findViewById( R.id.product_view_const_layout )){
                                if (productModelList.size()>2){
                                    addOnProductClick(  productModelList.get( 2 ).getpProductID(), index, 2 );
                                }
                            }
                        }
                    } );
                }
            }

            // Add new Product in Grid Layout...
            for (int k = 0; k < 4; k++ ) {
                ConstraintLayout itemLayout = gridLayout.getChildAt( k ).findViewById( R.id.product_view_const_layout );
                LinearLayout addNewItemLayout = gridLayout.getChildAt( k ).findViewById( R.id.product_view_linear_layout );
                if ( k < gridRange ){
                    itemLayout.setVisibility( View.VISIBLE );
                    addNewItemLayout.setVisibility( View.GONE );
                }else{
                    itemLayout.setVisibility( View.GONE );
                    addNewItemLayout.setVisibility( View.VISIBLE );
//                    addNewItemLayout.setLayoutParams( new ViewGroup.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT ) );
                }

                addNewItemLayout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewItem( index );
                    }
                } );

            }

            // -------------------------------------------------------------------------------------
            // Delete Layout Btn Click
            deleteLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete layout
                    if (productIDList.size() <= 1){
                        if (productIDList.size() == 0)
                            alertDialog( itemView.getContext(), index, layoutID, null,null  );
                        else
//                            alertDialog( itemView.getContext(), index, layoutID, "SHOPS/" + SHOP_ID +"/banners", bannerList.get( 0 ).getDeleteID()  );
                            showToast( "Code Not Found!", itemView.getContext() );
                    }else{
                        showToast( "You have more than 1 banner in this layout!", itemView.getContext() );
                    }
                }
            } );
            // Edit Title Btn Click
            editTitleBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogEditTitle( itemView.getContext(), index, productLayId, layoutTitle );
                }
            } );
            // View All Btn Click
            layoutViewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent( itemView.getContext(), ViewAllActivity.class );
                    intent.putExtra( "CAT_INDEX", catIndex  );
                    intent.putExtra( "LAY_INDEX", index  );
                    intent.putExtra( "VIEW_TYPE", VIEW_GRID_LAYOUT  );
                    intent.putExtra( "TITLE", layoutTitle  );

                    itemView.getContext().startActivity( intent );
                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );

        }

        private void addNewItem(int layIndex){
            Intent addProduct = new Intent( itemView.getContext(), AddNewProductActivity.class );
            addProduct.putExtra( "CAT_INDEX", catIndex );
            addProduct.putExtra( "LAY_INDEX", layIndex );
            addProduct.putExtra( "UPDATE", false );
            addProduct.putExtra( "PRO_INDEX", 0 );

            itemView.getContext().startActivity( addProduct );

        }
//        addOnProductClick(  gridLayoutProductIdList.get( 0 ), itemView.getContext(), index );
        private void addOnProductClick(String productId, int layoutIndex, int proIndex ){
            Intent productDetailIntent = new Intent( itemView.getContext(), ProductDetails.class );
            productDetailIntent.putExtra( "PRODUCT_ID", productId );
            productDetailIntent.putExtra( "HOME_CAT_INDEX", catIndex );
            productDetailIntent.putExtra( "LAYOUT_INDEX", layoutIndex ); // homePageList.get( position )
            productDetailIntent.putExtra( "PRODUCT_INDEX", proIndex );
            itemView.getContext().startActivity( productDetailIntent );
        }

    }
    //============  Product Grid View Holder =======================================================

    // Load Product Data...
    private class LoadProductData implements Runnable{
        private int index, childIndex;
        private String productId;

        public LoadProductData(int index, int childIndex, String productId) {
            this.index = index;
            this.childIndex = childIndex;
            this.productId = productId;
        }

        @Override
        public void run() {
            DBQuery.firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                    .collection( "PRODUCTS" ).document( productId )
                    .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        // access the banners from database...
                        DocumentSnapshot documentSnapshot = task.getResult();

                        if ( documentSnapshot.get( "p_no_of_variants" ) !=null ){
//                            String[] pImage;
                            int p_no_of_variants = Integer.valueOf( String.valueOf( (long) documentSnapshot.get( "p_no_of_variants" ) ) );
                            List<ProductSubModel> productSubModelList = new ArrayList <>();
                            for (int tempI = 1; tempI <= p_no_of_variants; tempI++){

                                // We can use Array...
                                ArrayList<String> Images = (ArrayList <String>) documentSnapshot.get( "p_image_" + tempI );
                                // add Data...
                                productSubModelList.add( new ProductSubModel(
                                        task.getResult().get( "p_name_"+tempI).toString(),
                                        Images,
                                        task.getResult().get( "p_selling_price_"+tempI).toString(),
                                        task.getResult().get( "p_mrp_price_"+tempI).toString(),
                                        task.getResult().get( "p_weight_"+tempI).toString(),
                                        task.getResult().get( "p_stocks_"+tempI).toString(),
                                        task.getResult().get( "p_offer_"+tempI).toString()
                                ) );
                            }
                            String p_id = task.getResult().get( "p_id").toString();
                            String p_main_name = task.getResult().get( "p_main_name" ).toString();
//                        String p_main_image = task.getResult().get( "p_main_image" ).toString();
                            String p_weight_type = task.getResult().get( "p_weight_type" ).toString();
                            int p_veg_non_type = Integer.valueOf( task.getResult().get( "p_veg_non_type" ).toString() );
                            Boolean p_is_cod = (Boolean) task.getResult().get( "p_is_cod" );

                            ProductModel productModel =  new ProductModel(
                                    p_id,
                                    p_main_name,
                                    " ",
                                    p_is_cod,
                                    String.valueOf(p_no_of_variants),
                                    p_weight_type,
                                    p_veg_non_type,
                                    productSubModelList
                            );

                            if ( homeCatListModelList.get( catIndex ).getHomeListModelList().size()>0)
                                homeCatListModelList.get( catIndex ).getHomeListModelList().get( index ).getProductModelList().add( productModel );

                            HomeFragment.homePageAdaptor.notifyDataSetChanged();

                        }
                    }
                    else{
                        String error = task.getException().getMessage();
//                                    showToast(error);
//                                    dialog.dismiss();
                    }
                }
            } );

        }
    }
    // Dialog to Edit Layout Title...
    private void showDialogEditTitle(final Context context, final int index, final String layoutId, String catTitle ){
        final Dialog dialog = DialogsClass.getDialog( context );
        final Dialog uDialog = new Dialog( context );
        uDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        uDialog.setContentView( R.layout.dialog_single_edit_text );
        uDialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        uDialog.setCancelable( false );
        Button okBtn = uDialog.findViewById( R.id.dialog_ok_btn );
        Button cancelBtn = uDialog.findViewById( R.id.dialog_cancel_btn );
        TextView titleText = uDialog.findViewById( R.id.dialog_title );
        titleText.setText( "Edit Title" );
        final EditText getText = uDialog.findViewById( R.id.dialog_editText );
        if (catTitle!=null){
            getText.setText( catTitle );
        }else{
            getText.setHint( "Enter title here" );
        }
        // show the Dialog..
        uDialog.show();
        // action Button...
        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uDialog.dismiss();
            }
        } );
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty( getText.getText().toString() )) {
                    // Show Dialog...
                    dialog.show();
                    //Check Condition to create Map
                    Map <String, Object> updateMap = new HashMap <>();
                    updateMap.put( "layout_title", getText.getText().toString() );
                    updateOnDocument( dialog, layoutId, updateMap );
                    // Update in Local List..
                    homePageList.get( index ).setProductLayoutTitle( getText.getText().toString() );
                    // Dismiss the title Dialog...
                    uDialog.dismiss();
                }else{
                    getText.setError( "Required!" );
                }
            }
        } );
    }
    // Update Index of Layouts...
    private void setIndexUpDownVisibility( int index, ImageView indexUpBtn,  ImageView indexDownBtn){
        if (homePageList.size()>1){
            indexUpBtn.setVisibility( View.VISIBLE );
            indexDownBtn.setVisibility( View.VISIBLE );
            if (index == 0){
                indexUpBtn.setVisibility( View.INVISIBLE );
            }else if (index == homePageList.size()-1){
                indexDownBtn.setVisibility( View.INVISIBLE );
            }
        }else{
            indexUpBtn.setVisibility( View.INVISIBLE );
            indexDownBtn.setVisibility( View.INVISIBLE );
        }
    }
    private void updateIndex(boolean isMoveUp, int index, Dialog dialog){
        dialog.show();
        if (isMoveUp){
            // GoTo Up...
            updateIndexOnDatabase(index, (index - 1), dialog );
        }else{
            // Goto Down..
            updateIndexOnDatabase(index, (index + 1), dialog );
        }
    }
    private void updateIndexOnDatabase(final int startInd, final int endInd, final Dialog dialog){
        String[] layoutId = new String[]{ homePageList.get( startInd ).getLayoutID(), homePageList.get( endInd ).getLayoutID() };

//        Collections.swap( homePageList, startInd, endInd );
        Collections.swap( homeCatListModelList.get( catIndex ).getHomeListModelList(), startInd, endInd );
        // TODO : Notify...
        HomeFragment.homePageAdaptor.notifyDataSetChanged();

        for ( String tempId : layoutId){
//            if (!dialog.isShowing()){
//                dialog.show();
//            }
            Map <String, Object> updateMap = new HashMap <>();
            updateMap.clear();
            if (tempId.equals( layoutId[0] )){
                updateMap.put( "index", ( endInd ) );
            }else{
                updateMap.put( "index", ( startInd ) );
            }
            updateOnDocument(dialog, tempId, updateMap);
        }

    }
    // Update Query On Database in Requested Layout...
    private void updateOnDocument(final Dialog dialog, String layoutId, Map <String, Object> updateMap){
        firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                .collection( catID ).document( layoutId ).update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        dialog.dismiss();
                        HomeFragment.homePageAdaptor.notifyDataSetChanged();
                    }
                } );
    }

    private void showToast(String msg, Context context){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }
    // Request to Delete Any Layout From Database
    private void alertDialog(final Context context, final int index, final String layoutId
            , @Nullable final String deletePath, @Nullable final String deleteID){
        AlertDialog.Builder alertD = new AlertDialog.Builder( context );
        alertD.setTitle( "Do You want to delete this Layout.?" );
        alertD.setMessage( "If you delete this layout, you will loose all the inside data of the layout.!" );
        alertD.setCancelable( false );
        alertD.setPositiveButton( "DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final Dialog pDialog = DialogsClass.getDialog( context );
                pDialog.show();
                // Query to delete the Layout...!
                firebaseFirestore.collection( "SHOPS" ).document( SHOP_ID )
                        .collection( catID ).document( layoutId ).delete()
                        .addOnCompleteListener( new OnCompleteListener <Void>() {
                            @Override
                            public void onComplete(@NonNull Task <Void> task) {
                                if (task.isSuccessful()){
                                    showToast( "Deleted Layout Successfully.!", context );
                                    // : Update in local list..!
                                    homePageList.remove( index );
                                    // Notify Data Changed...
                                    HomeFragment.homePageAdaptor.notifyDataSetChanged();
                                    // Delete Process...
                                    if (deleteID != null){
//                                        UpdateImages.deleteImageFromFirebase( context, null
//                                                , CURRENT_CITY_CODE + deletePath
//                                                , deleteID  );
                                    }
                                }else {
                                    showToast( "Failed.! Something went wrong.!", context );
                                }
                                pDialog.dismiss();
                            }
                        } );

            }
        } );
        alertD.setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );
        alertD.show();
    }


}
