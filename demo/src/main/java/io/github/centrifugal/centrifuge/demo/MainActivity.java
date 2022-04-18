package io.github.centrifugal.centrifuge.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import io.github.centrifugal.centrifuge.Client;
import io.github.centrifugal.centrifuge.ConnectedEvent;
import io.github.centrifugal.centrifuge.ConnectingEvent;
import io.github.centrifugal.centrifuge.DisconnectedEvent;
import io.github.centrifugal.centrifuge.ErrorEvent;
import io.github.centrifugal.centrifuge.EventListener;
import io.github.centrifugal.centrifuge.Options;
import io.github.centrifugal.centrifuge.PublicationEvent;
import io.github.centrifugal.centrifuge.ServerPublicationEvent;
import io.github.centrifugal.centrifuge.ServerSubscribedEvent;
import io.github.centrifugal.centrifuge.SubscribingEvent;
import io.github.centrifugal.centrifuge.SubscriptionErrorEvent;
import io.github.centrifugal.centrifuge.SubscribedEvent;
import io.github.centrifugal.centrifuge.Subscription;
import io.github.centrifugal.centrifuge.SubscriptionEventListener;
import io.github.centrifugal.centrifuge.UnsubscribedEvent;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MainActivity extends AppCompatActivity {

    private Client client;
    private boolean activityPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.text);

        Toast.makeText(this, "Create", Toast.LENGTH_SHORT).show();

        EventListener listener = new EventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onConnecting(Client client, ConnectingEvent event) {
                MainActivity.this.runOnUiThread(() -> tv.setText("Connecting: " + event.getReason()));
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onConnected(Client client, ConnectedEvent event) {
                MainActivity.this.runOnUiThread(() -> tv.setText("Connected with client ID " + event.getClient()));
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onDisconnected(Client client, DisconnectedEvent event) {
                MainActivity.this.runOnUiThread(() -> tv.setText("Disconnected: " + event.getReason()));
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onError(Client client, ErrorEvent event) {
                MainActivity.this.runOnUiThread(() -> tv.setText("Client error  " + event.getError().toString()));
            }
            @Override
            public void onSubscribed(Client client, ServerSubscribedEvent event) {
                MainActivity.this.runOnUiThread(() -> tv.setText("Subscribed to server-side channel " + event.getChannel()));
            }
            @Override
            public void onPublication(Client client, ServerPublicationEvent event) {
                String data = new String(event.getData(), UTF_8);
                MainActivity.this.runOnUiThread(() -> tv.setText("Message from " + event.getChannel() + ": " + data));
            }
        };

        Options opts = new Options();
//        opts.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0c3VpdGVfand0In0.hPmHsVqvtY88PvK4EmJlcdwNuKFuy3BGaF7dMaKdPlw");

        String endpoint = "ws://192.168.1.102:8000/connection/websocket?cf_protocol_version=v2";
        client = new Client(endpoint, opts, listener);
        client.connect();

        SubscriptionEventListener subListener = new SubscriptionEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSubscribing(Subscription sub, SubscribingEvent event) {
                MainActivity.this.runOnUiThread(() -> tv.setText("Subscribing to " + sub.getChannel() + ", reason: " + event.getReason()));
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onSubscribed(Subscription sub, SubscribedEvent event) {
                MainActivity.this.runOnUiThread(() -> tv.setText("Subscribed to " + sub.getChannel() + ", recovered: " + event.getRecovered().toString()));
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onUnsubscribed(Subscription sub, UnsubscribedEvent event) {
                MainActivity.this.runOnUiThread(() -> tv.setText("Unsubscribed from " + sub.getChannel() + ", reason: " + event.getReason()));
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onError(Subscription sub, SubscriptionErrorEvent event) {
                MainActivity.this.runOnUiThread(() -> tv.setText("Subscribe error " + sub.getChannel() + ": " + event.getError().toString()));
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onPublication(Subscription sub, PublicationEvent event) {
                String data = new String(event.getData(), UTF_8);
                MainActivity.this.runOnUiThread(() -> tv.setText("Message from " + sub.getChannel() + ": " + data));
            }
        };

        Subscription sub;
        try {
            sub = client.newSubscription("chat:index", subListener);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        sub.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityPaused = true;
        client.disconnect();
        Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (activityPaused) {
            client.connect();
            Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show();
            activityPaused = false;
        }
    }

//    @Override
//    public void onBackPressed(){
//        moveTaskToBack(true);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            client.close(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Destroyed", Toast.LENGTH_SHORT).show();
    }
}
