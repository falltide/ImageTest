package com.weizhixiang.imagetest.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.weizhixiang.imagetest.R;
import com.weizhixiang.imagetest.data.image;

import java.util.List;

public class DetaileAdapter extends RecyclerView.Adapter<DetaileAdapter.MyViewHolder> {
    private List<image> images;

    public DetaileAdapter(List<image> images){
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detaile_item,viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        Uri uri = Uri.parse(images.get(i).getUrl());
        viewHolder.detaile_image.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView detaile_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            detaile_image = itemView.findViewById(R.id.load_image);
        }
    }
}
