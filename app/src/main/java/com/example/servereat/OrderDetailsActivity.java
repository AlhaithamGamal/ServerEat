package com.example.servereat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.servereat.common.Common;

public class OrderDetailsActivity extends AppCompatActivity {
    TextView order_id,order_phone,order_address,order_comment,order_status,order_total,order_date,order_time;
    RecyclerView lstFoods;
    String order_id_value;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        order_id = findViewById(R.id.order_detailid);
        order_phone = findViewById(R.id.order_detailphone);
        order_address = findViewById(R.id.order_detailaddress);
        order_comment = findViewById(R.id.order_detailcomment);
        order_status = findViewById(R.id.order_detailstatus);
        order_total = findViewById(R.id.order_detailtotal);
        order_date = findViewById(R.id.order_detaildate);
        order_time = findViewById(R.id.order_detailtime);
        lstFoods = (RecyclerView)findViewById(R.id.listfood);
        lstFoods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstFoods.setLayoutManager(layoutManager);
        if(getIntent() != null){
            order_id_value = getIntent().getStringExtra("OrderId");
            order_id.setText(order_id_value);
            order_phone.setText(Common.currentRequest.getPhone());
            order_address.setText(Common.currentRequest.getAddress());
            order_status.setText(Common.currentRequest.getStatus());
            order_total.setText(Common.currentRequest.getTotal());
            order_date.setText(Common.currentRequest.getAddress());
            order_time.setText(Common.currentRequest.getAddress());
            order_comment.setText(Common.currentRequest.getComment());
            OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(Common.currentRequest.getFoods());
            orderDetailAdapter.notifyDataSetChanged();
            lstFoods.setAdapter(orderDetailAdapter);

        }

    }
}
