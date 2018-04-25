package com.ap.flickr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ap.flickr.R;
import com.ap.flickr.data.SearchResponse;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by anike on 4/22/2018.
 */

public class PhotListAdapter extends RecyclerView.Adapter {
    List<SearchResponse.Photo> photos;
    Context context;

    public PhotListAdapter(List<SearchResponse.Photo> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_layout_photo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        String url = "https://farm" + String.valueOf(photos.get(position).getFarm()) + ".staticflickr.com/" + photos.get(position).getServer() + "/" + photos.get(position).getId() + "_" + photos.get(position).getSecret() + ".jpg";
        Glide.with(context)
                .load(url)
                .into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void appendPhotos(List<SearchResponse.Photo> photos) {
        this.photos.addAll(photos);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView;
        }
    }
}
