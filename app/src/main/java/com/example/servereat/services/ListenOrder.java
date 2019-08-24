package com.example.servereat.services;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.servereat.OrderStatusActivity;
import com.example.servereat.R;
import com.example.servereat.models.Requests;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase db;
    DatabaseReference orders;

    public ListenOrder() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();
        orders = db.getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        orders.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Requests request = dataSnapshot.getValue(Requests.class);
        ///Toast.makeText(this,"CHILD ADDED STATUS!!"+request.getStatus(),Toast.LENGTH_LONG).show();
        if(request.getStatus().equals("0"))
            showNotification(dataSnapshot.getKey(),request);

    }

    private void showNotification(String key, Requests request) {
        Intent intent = new Intent(getBaseContext(), OrderStatusActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("EDMTDEv")
                .setContentInfo("New Order")
                .setContentText("You have new order #"+key)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //give uniqued id for many notifications;
        int randomInt = new Random().nextInt(9999-1)*1;
        notificationManager.notify(randomInt,builder.build());



    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//        Requests request = dataSnapshot.getValue(Requests.class);
//        ///Toast.makeText(this,"CHILD ADDED STATUS!!"+request.getStatus(),Toast.LENGTH_LONG).show();
//        if(request.getStatus().equals("0"))
//            showNotification(dataSnapshot.getKey(),request);


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
