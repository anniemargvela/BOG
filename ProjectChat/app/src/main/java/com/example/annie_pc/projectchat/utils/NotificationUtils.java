package com.example.annie_pc.projectchat.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.annie_pc.projectchat.MyApplication;
import com.example.annie_pc.projectchat.R;
import com.example.annie_pc.projectchat.activity.ChatActivity;
import com.example.annie_pc.projectchat.model.Contact;
import com.example.annie_pc.projectchat.model.Message;

public class NotificationUtils {

    public static void makeSound(Context context) {
        if (SharedPreferencesUtils.getSoundsState(context)) {
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(context, notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void makeNotification(Context context, Contact contact, Message message) {
        if (!SharedPreferencesUtils.getNotificationsState(context) || !MyApplication.isFolded())
            return;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_person)
                        .setContentTitle(contact.getDisplayName())
                        .setContentText(message.getText())
                        .setAutoCancel(true);
        Intent resultIntent = IntentUtils.buildChatIntent(context, contact.getId());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ChatActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(Integer.parseInt(contact.getId()), mBuilder.build());
    }

}
