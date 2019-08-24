package com.example.servereat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.servereat.common.Common;
import com.example.servereat.holders.OrderViewHolder;
import com.example.servereat.interfaces.ItemClickListener;
import com.example.servereat.models.Order;
import com.example.servereat.models.Requests;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class OrderStatusActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Requests, OrderViewHolder> adapter;
    FirebaseDatabase db;
    DatabaseReference requestsRef;
    MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        db = FirebaseDatabase.getInstance();
        requestsRef = db.getReference("Requests");
        recyclerView = findViewById(R.id.listorders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadOrders();
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Requests, OrderViewHolder>(Requests.class, R.layout.order_layout, OrderViewHolder.class, requestsRef) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, final Requests requests, int position) {
                orderViewHolder.orderId.setText(adapter.getRef(position).getKey());
                orderViewHolder.orderStatus.setText(Common.convertCodeToStatus(requests.getStatus().toString()));
                orderViewHolder.orderAddress.setText(requests.getAddress());
                orderViewHolder.orderPhone.setText(requests.getPhone());
                orderViewHolder.orderDate.setText(requests.getDate());
                orderViewHolder.orderTime.setText(requests.getTime());
                orderViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if(!isLongClick) {
                            Intent intent = new Intent(OrderStatusActivity.this, TrackingOrderActivity.class);
                            Common.currentRequest = requests;
                            startActivity(intent);
                        }
                        else{ Intent orderDetail = new Intent(OrderStatusActivity.this, OrderDetailsActivity.class);
                            Common.currentRequest = requests;
                            orderDetail.putExtra("OrderId",adapter.getRef(position).getKey());
                            startActivity(orderDetail);

                        }

                    }
                });


            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if ((item.getTitle().equals(Common.UPDATE))) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)) {
            deleteDialog(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteDialog(String key) {
        requestsRef.child(key).removeValue();
        Toast.makeText(OrderStatusActivity.this,"Deleted",Toast.LENGTH_LONG).show();
    }

    private void showUpdateDialog(String key, final Requests item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatusActivity.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout,null);
        spinner = view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","On my way","Shipped");
        alertDialog.setView(view);
        final String localKey = key;
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                requestsRef.child(localKey).setValue(item);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();


    }
}
