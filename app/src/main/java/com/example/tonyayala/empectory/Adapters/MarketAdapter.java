package com.example.tonyayala.empectory.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tonyayala.empectory.Models.MarketPost;
import com.example.tonyayala.empectory.ProductDetailActivity;
import com.example.tonyayala.empectory.R;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ImageViewHolder> {

    Context mContext;
    List<MarketPost> mData;

    public MarketAdapter(Context context, List<MarketPost> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from( mContext ).inflate( R.layout.activity_market_content, viewGroup, false );

        return new ImageViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        holder.tvTittle.setText( mData.get( position ).getTittle() );

        holder.tvPrice.setText("$ " +  mData.get( position ).getPrice() );

        Glide.with( mContext ).load( mData.get( position ).getImg() ).into( holder.imgProduct );

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView tvTittle;
        TextView tvPrice;
        ImageView imgProduct;

        public ImageViewHolder(@NonNull View itemView) {
            super( itemView );

            tvTittle = itemView.findViewById( R.id.row_product_tittle );
            tvPrice = itemView.findViewById( R.id.row_product_price );
            imgProduct = itemView.findViewById( R.id.row_product_img );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent marketActivity = new Intent( mContext, ProductDetailActivity.class );
                    int position = getAdapterPosition();

                    marketActivity.putExtra( "tittle", mData.get( position ).getTittle() );
                    marketActivity.putExtra( "price", mData.get( position ).getPrice() );
                    marketActivity.putExtra( "productImage", mData.get( position ).getImg() );
                    marketActivity.putExtra( "exist", mData.get( position ).getExist() );
                    marketActivity.putExtra( "productKey", mData.get( position ).getProductkey() );
                    marketActivity.putExtra( "postKey", mData.get( position ).getPostKey() );
                    marketActivity.putExtra( "description", mData.get( position ).getDescription() );
                    marketActivity.putExtra( "userID", mData.get( position ).getUserID() );


                    mContext.startActivity( marketActivity );
                }
            } );

        }
    }
}
