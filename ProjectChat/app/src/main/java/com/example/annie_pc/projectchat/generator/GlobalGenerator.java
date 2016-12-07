package com.example.annie_pc.projectchat.generator;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.annie_pc.projectchat.model.Contact;
import com.example.annie_pc.projectchat.model.Message;
import com.example.annie_pc.projectchat.utils.NotificationUtils;
import com.example.annie_pc.projectchat.utils.RandUtils;
import io.realm.Realm;

public class GlobalGenerator implements Runnable {

    public static final long TIMEOUT = 60000;

    private Context context;
    private Handler handler;

    public GlobalGenerator(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void run() {
        Log.d("Handlers", "Global generator called...");

        final Contact contact = RandUtils.randContact();

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (RandUtils.randBoolean()) {
                    // Change status.
                    contact.setOnline(!contact.isOnline());
                } else {
                    // Generate message.
                    Message message = realm.createObject(Message.class);
                    message.setText("Hello bro");
                    Message.createSentMessage(message);
                    contact.setLastMessageTimestamp(message.getTimestamp());
                    contact.getMessages().add(message);

                    NotificationUtils.makeSound(context);
                    NotificationUtils.makeNotification(context, contact, message);
                }
            }
        });

        handler.postDelayed(this, TIMEOUT);
    }
}
