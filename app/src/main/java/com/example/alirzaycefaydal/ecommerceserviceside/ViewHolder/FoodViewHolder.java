package com.example.alirzaycefaydal.ecommerceserviceside.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alirzaycefaydal.ecommerceserviceside.HomeActivity;
import com.example.alirzaycefaydal.ecommerceserviceside.Interface.ItemClickListener;
import com.example.alirzaycefaydal.ecommerceserviceside.R;

public class FoodViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{
    public TextView txtMenuName;
    public ImageView foodImageView;

    private ItemClickListener listener;

    public FoodViewHolder (View view){
        super(view);

        txtMenuName=view.findViewById(R.id.single_food_name);
        foodImageView=view.findViewById(R.id.single_food_image);

        view.setOnCreateContextMenuListener(this);
        view.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select an action");
        menu.add(0,0,getAdapterPosition(),HomeActivity.UPDATE);
        menu.add(0,1,getAdapterPosition(),HomeActivity.DELETE);
    }
}