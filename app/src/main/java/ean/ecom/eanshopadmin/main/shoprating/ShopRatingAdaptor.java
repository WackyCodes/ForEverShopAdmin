package ean.ecom.eanshopadmin.main.shoprating;

/*
 * Copyright (c) 2020.
 * WackyCodes : Tech Services.
 * https://linktr.ee/wackycodes
 */

import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ean.ecom.eanshopadmin.R;
import ean.ecom.eanshopadmin.other.StaticMethods;

/**
 * Created by Shailendra (WackyCodes) on 20/09/2020 10:53
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class ShopRatingAdaptor extends RecyclerView.Adapter<ShopRatingAdaptor.ViewHolder> {

    private List<ShopRatingModel> ratingModelList;

    public ShopRatingAdaptor(List <ShopRatingModel> ratingModelList) {
        this.ratingModelList = ratingModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ratingItemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.shop_rating_layout_item, parent, false );
        return new ViewHolder( ratingItemView );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData( position );
    }

    @Override
    public int getItemCount() {
        return ratingModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView rate_1;
        private ImageView rate_2;
        private ImageView rate_3;
        private ImageView rate_4;
        private ImageView rate_5;

        private CircleImageView userImage;
        private TextView userName;
        private TextView ratingTitle;
        private TextView ratingText;
        private TextView ratingTime;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            rate_1 = itemView.findViewById( R.id.rating_star_1 );
            rate_2 = itemView.findViewById( R.id.rating_star_2 );
            rate_3 = itemView.findViewById( R.id.rating_star_3 );
            rate_4 = itemView.findViewById( R.id.rating_star_4 );
            rate_5 = itemView.findViewById( R.id.rating_star_5 );

            userImage = itemView.findViewById( R.id.user_image );
            userName = itemView.findViewById( R.id.user_name );
            ratingTitle = itemView.findViewById( R.id.rating_title );
            ratingText = itemView.findViewById( R.id.rating_text );
            ratingTime = itemView.findViewById( R.id.rating_time );

        }

        private void setData(int index){
            ShopRatingModel shopRatingModel = ratingModelList.get( index );

            // Load Image...
            Glide.with( itemView.getContext() ).load( shopRatingModel.getUserImage() )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) )
                    .into( userImage );

            // Set Other Text...
            userName.setText( shopRatingModel.getUserName() );
            ratingTitle.setText( shopRatingModel.getShopRatingTitle() );
            ratingText.setText( shopRatingModel.getShopRatingText() );

            // Set Time...
            ratingTime.setText(  "Rate " + StaticMethods.getTimeFromNow( shopRatingModel.getShopRatingDate(), shopRatingModel.getShopRatingTime() +":00" ));

            // Set Rating...
            setStar( shopRatingModel.getShopRatingStar() );

        }

        private void setStar(int rate){
            switch (rate){
                case 1:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rate_1.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_2.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorGray ) ) );
                        rate_3.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorGray ) ) );
                        rate_4.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorGray ) ) );
                        rate_5.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorGray ) ) );
                    }
                    break;
                case 2:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                        rate_1.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_2.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_3.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorGray ) ) );
                        rate_4.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorGray ) ) );
                        rate_5.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorGray ) ) );
                    }
                    break;
                case 3:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rate_1.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_2.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_3.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_4.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorGray ) ) );
                        rate_5.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorGray ) ) );
                    }
                    break;
                case 4:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rate_1.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_2.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_3.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_4.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_5.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorGray ) ) );
                    }
                    break;
                case 5:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rate_1.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_2.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_3.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_4.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                        rate_5.setImageTintList( ColorStateList.valueOf( itemView.getContext().getResources().getColor( R.color.colorPrimary ) ) );
                    }
                    break;
                default:
                    break;
            }
        }


    }



}
