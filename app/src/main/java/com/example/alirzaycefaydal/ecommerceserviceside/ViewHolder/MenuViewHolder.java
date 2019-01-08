package com.example.alirzaycefaydal.ecommerceserviceside.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alirzaycefaydal.ecommerceserviceside.HomeActivity;
import com.example.alirzaycefaydal.ecommerceserviceside.Interface.ItemClickListener;
import com.example.alirzaycefaydal.ecommerceserviceside.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{
    public TextView txtMenuName;
    public ImageView imageView;

    private ItemClickListener listener;

    public MenuViewHolder (View view){
        super(view);

        txtMenuName=view.findViewById(R.id.menu_name);
        imageView=view.findViewById(R.id.menu_image);

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
