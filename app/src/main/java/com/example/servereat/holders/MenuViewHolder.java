package com.example.servereat.holders;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servereat.R;
import com.example.servereat.common.Common;
import com.example.servereat.interfaces.ItemClickListener;


public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
, View.OnCreateContextMenuListener
{
  public   TextView txtMenuName;
     public ImageView imgView;
   public  ItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtMenuName = itemView.findViewById(R.id.menu_name);
        imgView = itemView.findViewById(R.id.menu_image);
        itemView.setOnCreateContextMenuListener(this);
     itemView.setOnClickListener(this);



    }


    public  void setItemClickListener(ItemClickListener itmClickListener){
        this.itemClickListener = itmClickListener;


    }

    @Override
    public void onClick(View v ) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select an action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);



    }
}
