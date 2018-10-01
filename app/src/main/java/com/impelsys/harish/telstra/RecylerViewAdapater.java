package com.impelsys.harish.telstra;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecylerViewAdapater extends RecyclerView.Adapter<RecylerViewAdapater.RecylerViewHolder> {
    List<HashMap<String, String>> listdata;
    private Context context;

    public RecylerViewAdapater(Context context) {

        this.context = context;
    }

    @NonNull
    @Override
    public RecylerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_item, parent, false);
        return new RecylerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerViewHolder holder, int position) {
        HashMap<String, String> itemData = listdata.get(position);

        holder.tv_title.setText(itemData.get("title"));
        holder.tv_description.setText(itemData.get("description"));
       // holder.tv_title.setText(itemData.get("imageurl"));
        Picasso.with(context)
                .load(itemData.get("imageurl"))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.no_image)
                .into(holder.imv_image);
    }

    @Override
    public int getItemCount() {
        if (listdata != null)
            return listdata.size();
        return 0;
    }


    class RecylerViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_description;
        ImageView imv_image;

        public RecylerViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            imv_image = itemView.findViewById(R.id.imv_imageView);

        }
    }

    public void setListdata(List<HashMap<String, String>> listdata){
        this.listdata=listdata;
        notifyDataSetChanged();
    }

}


