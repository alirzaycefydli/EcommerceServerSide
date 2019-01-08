package com.example.alirzaycefaydal.ecommerceserviceside;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alirzaycefaydal.ecommerceserviceside.Common.Common;
import com.example.alirzaycefaydal.ecommerceserviceside.Interface.ItemClickListener;
import com.example.alirzaycefaydal.ecommerceserviceside.Models.Request;
import com.example.alirzaycefaydal.ecommerceserviceside.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class OrderStatusActivity extends AppCompatActivity {

    //vars
    private RecyclerView recyclerView;
    private DatabaseReference requestRef;
    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    //widgets
    private MaterialSpinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        recyclerView = findViewById(R.id.list_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadOrders();
    }

    private void loadOrders() {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requestRef, Request.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Request model) {

                holder.txtOrderAddress.setText(model.getAdress());
                holder.txtOrderPhone.setText(model.getPhone());
                holder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                holder.txtOrderId.setText(getRef(position).getKey());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent trackOrderIntent=new Intent(OrderStatusActivity.this,TrackingOrderActivity.class);
                        Common.currentRequest=model;
                        startActivity(trackOrderIntent);
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_order_layout, viewGroup, false);

                return new OrderViewHolder(view);
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {

        if (status.equals("0")) {
            return "Placed";
        } else if (status.equals("1")) {
            return "On Way-Shipping";
        } else {
            return "Shipped";
        }
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(HomeActivity.UPDATE)){

            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

        } else if (item.getTitle().equals(HomeActivity.DELETE)){

            showDeleteDialog(adapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteDialog(String key) {

        requestRef.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(OrderStatusActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OrderStatusActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showUpdateDialog(final String key, final Request item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(OrderStatusActivity.this);
        dialog.setTitle("Update order");

        View view = LayoutInflater.from(OrderStatusActivity.this).inflate(R.layout.update_order_layout, null);
        spinner=view.findViewById(R.id.status_spinner);
        spinner.setItems("Placed","On my way","Shipped");

        dialog.setView(view);

        final String localKey=key;


        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                requestRef.child(localKey).setValue(item);

                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
