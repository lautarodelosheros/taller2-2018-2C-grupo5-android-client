package com.comprame.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.MainActivity;
import com.comprame.R;
import com.comprame.library.rest.Headers;
import com.comprame.login.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMessagingManager {

    private MainActivity activity;
    private String registrationToken;

    public FirebaseMessagingManager(final MainActivity activity) {
        this.activity = activity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = activity.getString(R.string.default_notification_channel_id);
            String channelName = activity.getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                Object value = activity.getIntent().getExtras().get(key);
                Log.d("FirebaseMessagingMgr", "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]


        // Get token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FirebaseMessagingMgr", "getInstanceId failed", task.getException());
                            return;
                        }

                        registrationToken = task.getResult().getToken();

                        sendFirebaseToken();

                        Log.d("FirebaseMessagingMgr", "Firebase token: " + registrationToken);

                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("topicAll");
    }

    public void sendFirebaseToken() {
        App.appServer.post("/firebase/" + registrationToken
                , registrationToken
                , String.class
                , new Headers().authorization(Session.getInstance().getSessionToken()))
                .run((s) -> Log.d("FirebaseTokenListener", "Token sent correctly"),
                    (ex) -> Log.d("FirebaseTokenListener", ex.toString()));
    }
}
