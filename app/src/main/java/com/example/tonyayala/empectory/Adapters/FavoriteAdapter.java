package com.example.tonyayala.empectory.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tonyayala.empectory.Models.Favorite;
import com.example.tonyayala.empectory.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavViewHolder> {
    //Los datos se obtienen del objeto Like
    Context mContext;
    List<Favorite> mData;

    public FavoriteAdapter(Context mContext, List<Favorite> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from( mContext ).inflate( R.layout.row_favorite_item,viewGroup,false );

        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        holder.tvFavTittle.setText( mData.get( position ).getNameF() );

        Glide.with( mContext ).load( mData.get( position ).getPictureF() ).into( holder.imgFavPhoto );

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder{
        TextView tvFavTittle;
        ImageView imgFavPhoto;


        public FavViewHolder(@NonNull View itemView) {
            super( itemView );

            tvFavTittle = itemView.findViewById( R.id.row_favorite_tittle );
            imgFavPhoto = itemView.findViewById( R.id.row_favorite_img );

        }
    }
}
