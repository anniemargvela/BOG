package com.example.annie_pc.projectchat.generator;

import android.util.Log;

import com.example.annie_pc.projectchat.MyApplication;
import com.example.annie_pc.projectchat.model.Contact;
import com.example.annie_pc.projectchat.model.Message;
import io.realm.Realm;

public class ChatGenerator implements Runnable {

    private Contact contact;

    public ChatGenerator(Contact contact) {
        this.contact = contact;
    }

    @Override
    public void run() {
        Log.d("Handlers", "Chat Generator called...");

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Message m = realm.createObject(Message.class);
                m.setText("Don't worry, be happy");
                Message.createSentMessage(m);
                contact.setLastMessageTimestamp(m.getTimestamp());
                contact.getMessages().add(m);
            }
        });

        MyApplication.getHandler().removeCallbacks(this);
    }
}
