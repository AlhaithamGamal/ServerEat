package com.example.servereat.holders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servereat.R;
import com.example.servereat.common.Common;
import com.example.servereat.interfaces.ItemClickListener;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener,View.OnLongClickListener {
    public TextView orderId,orderStatus,orderPhone,orderAddress,orderDate,orderTime,orderName;



    private ItemClickListener itemClickListener;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        orderId = itemView.findViewById(R.id.order_id);
        orderStatus = itemView.findViewById(R.id.order_status);
        orderPhone = itemView.findViewById(R.id.order_phone);
        orderAddress = itemView.findViewById(R.id.order_address);
        orderDate = itemView.findViewById(R.id.order_date);
        orderTime = itemView.findViewById(R.id.order_time);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select an action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true);
        return true;
    }


}
