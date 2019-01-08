package com.example.alirzaycefaydal.ecommerceserviceside.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.alirzaycefaydal.ecommerceserviceside.HomeActivity;
import com.example.alirzaycefaydal.ecommerceserviceside.Interface.ItemClickListener;
import com.example.alirzaycefaydal.ecommerceserviceside.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderAddress, txtOrderPhone;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderAddress = itemView.findViewById(R.id.single_order_address);
        txtOrderId = itemView.findViewById(R.id.single_order_id);
        txtOrderStatus = itemView.findViewById(R.id.single_order_status);
        txtOrderPhone = itemView.findViewById(R.id.single_order_phone);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {


        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select an action");
        menu.add(0,0,getAdapterPosition(),HomeActivity.UPDATE);
        menu.add(0,1,getAdapterPosition(),HomeActivity.DELETE);
    }
}
