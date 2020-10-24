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
import com.example.tonyayala.empectory.Models.Comment;
import com.example.tonyayala.empectory.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment>mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment,parent,false);

        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        Glide.with(mContext).load(mData.get(position).getUimg()).into(holder.imgUser);
        holder.tvName.setText(mData.get(position).getUname());
        holder.tvContetn.setText(mData.get(position).getContent());
        //Se añade la hora
        holder.tvDate.setText(timestampToString((Long)mData.get(position).getTimestamp()));


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        ImageView imgUser;
        TextView tvName;
        TextView tvContetn;
        TextView tvDate;

        public CommentViewHolder(View itemView){
            super(itemView);
            imgUser = itemView.findViewById(R.id.comment_user_img);
            tvName = itemView.findViewById(R.id.comment_username);
            tvContetn = itemView.findViewById(R.id.comment_content);
            tvDate = itemView.findViewById(R.id.comment_date);

        }
    }

    private String timestampToString(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return android.text.format.DateFormat.format("hh:mm",calendar).toString();

    }

}
