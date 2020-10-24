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
import com.example.tonyayala.empectory.Models.Post;
import com.example.tonyayala.empectory.PostDetailActivity;
import com.example.tonyayala.empectory.R;

import java.util.List;

    public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

        Context mContext;
        List<Post> mData;

        public PostAdapter(Context mContext, List<Post> mData) {
            this.mContext = mContext;
            this.mData = mData;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View row = LayoutInflater.from( mContext ).inflate( R.layout.row_post_item, viewGroup, false );

            return new MyViewHolder( row );
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.tvTittle.setText( mData.get( position ).getTittle() );

            holder.tvCount.setText( String.valueOf( mData.get( position ).getLike() ) );

            Glide.with( mContext ).load( mData.get( position ).getPicture() ).into( holder.imgPost );


            String userImg = mData.get( position ).getUserPhoto();
            if (userImg != null) {
                Glide.with( mContext ).load( mData.get( position ).getUserPhoto() ).into( holder.imgPostProfile );
            } else {
                Glide.with( mContext ).load( R.drawable.userphoto ).into( holder.imgPostProfile );
            }

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvTittle;
            TextView tvCount;
            ImageView imgPost;
            ImageView imgPostProfile;

            public MyViewHolder(@NonNull View itemView) {
                super( itemView );

                tvTittle = itemView.findViewById( R.id.row_post_tittle );
                tvCount = itemView.findViewById( R.id.row_post_count );
                imgPost = itemView.findViewById( R.id.row_post_img );
                imgPostProfile = itemView.findViewById( R.id.row_post_profile );

                itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent postDetailActivity = new Intent( mContext, PostDetailActivity.class );
                        int position = getAdapterPosition();

                        postDetailActivity.putExtra( "tittle", mData.get( position ).getTittle() );
                        postDetailActivity.putExtra( "postImage", mData.get( position ).getPicture() );
                        postDetailActivity.putExtra( "description", mData.get( position ).getDescription() );
                        postDetailActivity.putExtra( "phone", mData.get( position ).getPhone() );
                        postDetailActivity.putExtra( "email", mData.get( position ).getEmail() );
                        postDetailActivity.putExtra( "address", mData.get( position ).getAddress() );
                        postDetailActivity.putExtra( "facebook", mData.get( position ).getFacebook() );
                        postDetailActivity.putExtra( "postKey", mData.get( position ).getPostKey() );
                        postDetailActivity.putExtra( "userPhoto", mData.get( position ).getUserPhoto() );
                        postDetailActivity.putExtra( "userId", mData.get( position ).getUserId() );

                        long timestamp = (long) mData.get( position ).getTimeStamp();
                        postDetailActivity.putExtra( "postDate", timestamp );
                        mContext.startActivity( postDetailActivity );


                    }
                } );

            }
        }
    }
