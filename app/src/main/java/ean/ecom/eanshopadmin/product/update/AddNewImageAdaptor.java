package ean.ecom.eanshopadmin.product.update;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ean.ecom.eanshopadmin.R;

import static ean.ecom.eanshopadmin.product.update.specification.UpdateImage_SpFragment.uploadImageDataModelList;

/**
 * Created by Shailendra (WackyCodes) on 04/09/2020 05:11
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class AddNewImageAdaptor  extends RecyclerView.Adapter<AddNewImageAdaptor.ViewHolder>  {

    private UpdateData.ImageListUpdateRequest updateImageRequest;
    private int updateImageFromIndex;

    public AddNewImageAdaptor(UpdateData.ImageListUpdateRequest updateImageRequest, List <String> uploadImageDataModelList, int updateImageFromIndex) {
        this.updateImageRequest = updateImageRequest;
        this.updateImageFromIndex = updateImageFromIndex;
    }

    public AddNewImageAdaptor(UpdateData.ImageListUpdateRequest updateImageRequest, int updateImageFromIndex) {
        this.updateImageRequest = updateImageRequest;
        this.updateImageFromIndex = updateImageFromIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View imageView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.new_pro_image_item, parent, false );
        return new ViewHolder( imageView );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int size = uploadImageDataModelList.size();
        if (position < size){
            holder.setData( uploadImageDataModelList.get( position ), position );
        }else if (size < 8){
            holder.addNewImage();
        }
    }

    @Override
    public int getItemCount() {
        if (uploadImageDataModelList.size() < 8){
            return uploadImageDataModelList.size() + 1;
        }else{
            return uploadImageDataModelList.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemImageNo;
        private ImageButton editImage;
        private LinearLayout addNewImage;
        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            itemImage = itemView.findViewById( R.id.image_view );
            addNewImage = itemView.findViewById( R.id.add_image_layout );
            editImage = itemView.findViewById( R.id.edit_imgBtn );
            itemImageNo = itemView.findViewById( R.id.pro_image_no );
        }
        private void setData(String imgLink, final int position){
            int imgNo = position + 1;
            addNewImage.setVisibility( View.GONE );
            editImage.setVisibility( View.GONE );
            itemImage.setVisibility( View.VISIBLE );
            itemImageNo.setVisibility( View.VISIBLE );
            itemImageNo.setText( String.valueOf( imgNo ) );

            if (position == uploadImageDataModelList.size()-1){
                editImage.setVisibility( View.VISIBLE );
            }

            if ( position < updateImageFromIndex){
                // Use Link...
                Glide.with( itemView.getContext() ).load( imgLink ).apply( new RequestOptions().placeholder( R.drawable.ic_phone_black_24dp ) ).into( itemImage );
            }else {
                // Use Uri...
                Glide.with( itemView.getContext() ).load( Uri.parse( imgLink ) ).into( itemImage );
            }

            editImage.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove Image...
                    updateImageRequest.onRemoveImageRequest( position );
                }
            } );
        }
        private void addNewImage(){
            editImage.setVisibility( View.GONE );
            itemImage.setVisibility( View.GONE );
            itemImageNo.setVisibility( View.GONE );
            addNewImage.setVisibility( View.VISIBLE );
            addNewImage.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add New Image...
                    updateImageRequest.onAddImageRequest();
                }
            } );
        }

    }
}
