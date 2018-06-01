package com.example.daniel.table_food;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.daniel.table_food.Common.Common;
import com.example.daniel.table_food.Model.Request;
import com.example.daniel.table_food.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerViewOrder;
    public RecyclerView.LayoutManager layoutManagerOrder;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        recyclerViewOrder=findViewById(R.id.listOrders);
        recyclerViewOrder.setHasFixedSize(true);
        layoutManagerOrder= new LinearLayoutManager(this);
        recyclerViewOrder.setLayoutManager(layoutManagerOrder);
        loadOrders(Common.currentUser.getPhone());


    }

    private void loadOrders(String phone) {
        adapter= new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("Phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
            }
        };
        recyclerViewOrder.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Realizado";
        else if (status.equals("1"))
            return "En Camino";
        else
            return "Enviado";
    }

}