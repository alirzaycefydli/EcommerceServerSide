package com.example.alirzaycefaydal.ecommerceserviceside.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.alirzaycefaydal.ecommerceserviceside.Models.Request;
import com.example.alirzaycefaydal.ecommerceserviceside.OrderStatusActivity;
import com.example.alirzaycefaydal.ecommerceserviceside.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener {

    private DatabaseReference orders;

    @Override
    public void onCreate() {
        super.onCreate();
        orders=FirebaseDatabase.getInstance().getReference().child("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        orders.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Request request=dataSnapshot.getValue(Request.class);
        if (request.getStatus().equals("0")){
            showNotification(dataSnapshot.getKey(),request);
        }
    }

    private void showNotification(String key, Request request) {
        Intent i=new Intent(getBaseContext(),OrderStatusActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(),0,i,0);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_ALL)
        .setTicker("AliRıza")
        .setContentInfo("New Order")
        .setContentText("You have new order "+key)
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentIntent(pendingIntent);

        NotificationManager manager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        int randomInt=new Random().nextInt(9999-1)+1;
        manager.notify(randomInt,builder.build());
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
