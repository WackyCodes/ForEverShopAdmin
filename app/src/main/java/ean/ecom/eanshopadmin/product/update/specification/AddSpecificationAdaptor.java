package ean.ecom.eanshopadmin.product.update.specification;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ean.ecom.eanshopadmin.R;

import static ean.ecom.eanshopadmin.product.update.specification.UpdateImage_SpFragment.specificationModelList;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

public class AddSpecificationAdaptor extends RecyclerView.Adapter<AddSpecificationAdaptor.ViewHolder>  {

//    specificationModelList

    //...not used
    public AddSpecificationAdaptor(List <AddSpecificationModel> specificationModelList ) {

    }

    // Call From Fragment...
    public AddSpecificationAdaptor(){
    }

    @NonNull
    @Override
    public AddSpecificationAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View spLayoutView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.add_new_product_specification_lay, parent, false );
        return new ViewHolder( spLayoutView );
    }
    @Override
    public void onBindViewHolder(@NonNull AddSpecificationAdaptor.ViewHolder holder, int position) {
        holder.setData( position );
    }
    @Override
    public int getItemCount() {
        if (specificationModelList.size() == 0){
            return 1;
        }else
            return specificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText sp_headingEdit;
        private TextView sp_headingText;
        private ImageButton sp_edit_btn;
        private ImageButton sp_delete_btn;
        private RecyclerView sp_feature_recycler;
        private EditText sp_feature_name;
        private EditText sp_feature_details;
        private TextView sp_feature_add_btn;
        private TextView sp_feature_done_btn;
        private TextView sp_new_layout_btn;

        private LinearLayout sp_add_done_feature_layout;
        private LinearLayout sp_add_new_layout;
        private LinearLayout sp_layout_top;
        private String title;

        private AddSpecificationFeatureAdaptor addSpecificationFeatureAdaptor;
        private List<AddSpecificationFeatureModel> specificationFeatureModelList;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            sp_headingEdit = itemView.findViewById( R.id.add_new_sp_title_editText );
            sp_headingText = itemView.findViewById( R.id.add_new_sp_title_TextView );
            sp_edit_btn = itemView.findViewById( R.id.add_new_edit_sp_layout );
            sp_delete_btn = itemView.findViewById( R.id.add_new_delete_sp_layout );
            sp_feature_recycler = itemView.findViewById( R.id.add_new_sp_recyclerView );
            sp_feature_name = itemView.findViewById( R.id.add_new_feature_name );
            sp_feature_details = itemView.findViewById( R.id.add_new_feature_details );
            sp_feature_add_btn = itemView.findViewById( R.id.add_new_feature_add_btn );
            sp_feature_done_btn = itemView.findViewById( R.id.add_new_sp_lay_done_btn );
            sp_new_layout_btn = itemView.findViewById( R.id.add_new_sp_lay_btn );

            sp_add_done_feature_layout = itemView.findViewById( R.id.add_new_add_done_layout );
            sp_add_new_layout = itemView.findViewById( R.id.add_new_sp_lay_btn_layout );
            sp_layout_top = itemView.findViewById( R.id.layout_first );
        }

        private void setData( final int position ){

            specificationFeatureModelList = new ArrayList <>();
            if (specificationModelList.size() > 0){
                // Visible...
                sp_edit_btn.setVisibility( View.VISIBLE );
                sp_delete_btn.setVisibility( View.VISIBLE );
                sp_layout_top.setVisibility( View.VISIBLE );
                sp_add_new_layout.setVisibility( View.GONE );

                specificationFeatureModelList = specificationModelList.get( position ).getSpecificationFeatureModelList();

                addSpecificationFeatureAdaptor = new AddSpecificationFeatureAdaptor(specificationModelList.get( position ).getSpecificationFeatureModelList(), position );

                title = specificationModelList.get( position ).getSpHeading();
                if (position != specificationModelList.size() - 1){
                    sp_add_done_feature_layout.setVisibility( View.GONE );
                    sp_headingEdit.setVisibility( View.GONE );
                    sp_headingText.setVisibility( View.VISIBLE );
                    sp_headingText.setText( title );
                }else {
                    sp_add_done_feature_layout.setVisibility( View.VISIBLE );
                    sp_headingEdit.setVisibility( View.VISIBLE );
                    sp_headingEdit.setText( title );
                    sp_headingText.setVisibility( View.GONE );
                }

            }
            else{
                // visible Bottom and invisible Top...
                sp_layout_top.setVisibility( View.GONE );
                sp_add_new_layout.setVisibility( View.VISIBLE );

                addSpecificationFeatureAdaptor = new AddSpecificationFeatureAdaptor( -1 );
            }
            //------------Recycler View...---------------
            LinearLayoutManager layoutManager = new LinearLayoutManager( itemView.getContext() );
            layoutManager.setOrientation( RecyclerView.VERTICAL );
            sp_feature_recycler.setLayoutManager( layoutManager );
            sp_feature_recycler.setAdapter( addSpecificationFeatureAdaptor );
            addSpecificationFeatureAdaptor.notifyDataSetChanged();
            //------------Recycler View...---------------

            // Click on Add Btn;
            sp_feature_add_btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check : isData fill on editText
                    if (isNotEmptyEditText( sp_feature_name ) && isNotEmptyEditText( sp_feature_details ) && isNotEmptyEditText( sp_headingEdit )){
                        // Add Data in list...
//                            specificationFeatureModelList.add( new AddSpecificationFeatureModel( sp_feature_name.getText().toString(),
//                                    sp_feature_details.getText().toString() ) );
                        if (specificationModelList.get( position ).getSpHeading() == null){
                            specificationModelList.get( position ).setSpHeading( title );
                        }
                        specificationModelList.get( position ).getSpecificationFeatureModelList().add(
                                new AddSpecificationFeatureModel( sp_feature_name.getText().toString(),
                                        sp_feature_details.getText().toString() ) );
                        sp_feature_name.setText( "" );
                        sp_feature_details.setText( "" );
                        addSpecificationFeatureAdaptor.notifyDataSetChanged();
                    }
                }
            } );
            // Click on Done Btn;
            sp_feature_done_btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // hide add_done_layout and visible edit and delete btn...
                    // check is exit data in list...
                    if (isNotEmptyEditText( sp_headingEdit ) && specificationFeatureModelList.size()>0){
                        title = sp_headingEdit.getText().toString();
                        sp_headingEdit.setVisibility( View.GONE );
                        sp_headingText.setVisibility( View.VISIBLE );
                        sp_headingText.setText( title );

                        specificationModelList.get( position ).setSpHeading( title );
                        addSpecificationFeatureAdaptor.notifyDataSetChanged();
                        sp_add_done_feature_layout.setVisibility( View.GONE );
                        //------
                        if (position == specificationModelList.size()-1){
                            sp_add_new_layout.setVisibility( View.VISIBLE );
                        }
                    }
                    else{
                        if (specificationFeatureModelList.size() == 0){
                            showToast( "Add Some feature first..!" );
                        }else{
                            showToast( "Add Heading or Feature Title.!" );
                        }
                    }
                }
            } );
            // Click on New Layout Btn;
            sp_new_layout_btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    specificationModelList.add( new AddSpecificationModel( "", new ArrayList <AddSpecificationFeatureModel>()) );
                    UpdateImage_SpFragment.specificationAdaptor.notifyDataSetChanged();
                    sp_layout_top.setVisibility( View.VISIBLE );
                    sp_add_new_layout.setVisibility( View.GONE );
                }
            } );
            // Click on edit Layout Btn;
            sp_edit_btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (specificationModelList.size() > 0){
                        // set Visibility of layout...
                        sp_headingEdit.setVisibility( View.VISIBLE );
                        sp_headingEdit.setText( title );
                        sp_headingText.setVisibility( View.GONE );
                        sp_add_done_feature_layout.setVisibility( View.VISIBLE );
                    }else {
                        showToast( "Add Data before editing..!" );
                    }
                }
            } );
            // Click on delete Layout Btn;
            sp_delete_btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete Layout...
                    if (specificationModelList.size() != 0){
                        specificationModelList.remove( position );
                        UpdateImage_SpFragment.specificationAdaptor.notifyDataSetChanged();
                    }else {
                        showToast( "Data not found..!" );
                    }
                }
            } );
        }

        private boolean isNotEmptyEditText(EditText editText){
            if (TextUtils.isEmpty( editText.getText().toString() )){
                editText.setError( "Required field..!" );
                return false;
            }else{
                return true;
            }
        }

        private void showToast(String msg){
            Toast.makeText( itemView.getContext(), msg, Toast.LENGTH_SHORT ).show();
        }
    }


    ///////////////////////////////////////  Features Adaptor  /////////////////////////////////////

    class AddSpecificationFeatureAdaptor  extends  RecyclerView.Adapter<AddSpecificationFeatureAdaptor.ViewHolder> {

        //    private List <AddSpecificationModel> specificationModelList;
        private List<AddSpecificationFeatureModel> specificationFeatureModelList;
        private int index;

        public AddSpecificationFeatureAdaptor(List <AddSpecificationFeatureModel> specificationFeatureModelList, int index) {
            this.specificationFeatureModelList = specificationFeatureModelList;
            this.index = index;
        }

        public AddSpecificationFeatureAdaptor(int index ) {
            this.index = index;
        }

        @NonNull
        @Override
        public AddSpecificationFeatureAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View spFeatureView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.product_detail_specification_item, parent, false );
            return new ViewHolder( spFeatureView );
        }
        @Override
        public void onBindViewHolder(@NonNull AddSpecificationFeatureAdaptor.ViewHolder holder, int position) {
            if(index >= 0){
//            String name = specificationModelList.get( index ).getSpecificationFeatureModelList().get( position ).getFeatureName();
//            String details = specificationModelList.get( index ).getSpecificationFeatureModelList().get( position ).getFeatureDetails();

                String name = specificationFeatureModelList.get( position ).getFeatureName();
                String details = specificationFeatureModelList.get( position ).getFeatureDetails();
                holder.setData( name, details, position );
            }
        }
        @Override
        public int getItemCount() {
            if (index >= 0)
                return specificationFeatureModelList.size();
            else
                return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView featureName;
            private TextView featureDetails;
            private ImageButton featureDeleteBtn;
            public ViewHolder(@NonNull View itemView) {
                super( itemView );
                featureName = itemView.findViewById( R.id.feature_name );
                featureDetails = itemView.findViewById( R.id.feature_value );
                featureDeleteBtn = itemView.findViewById( R.id.add_new_pro_sp_feature_delete_btn );
            }
            private void setData(String name, String detail, final int pos){
                featureDeleteBtn.setVisibility( View.VISIBLE );
                featureName.setText( name );
                featureDetails.setText( detail );
                featureDeleteBtn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        specificationFeatureModelList.remove( pos );
                        UpdateImage_SpFragment.specificationAdaptor.notifyDataSetChanged();
                        // :  addSpecificationFeatureAdaptor.notifyDataSetChanged();
                    }
                } );
            }
        }
    }


}
