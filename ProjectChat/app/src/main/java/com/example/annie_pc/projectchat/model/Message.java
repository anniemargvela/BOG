package com.example.annie_pc.projectchat.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Message extends RealmObject {
    @PrimaryKey
    private String id;
    private boolean sent;
    private String text;
    private boolean seen;
    private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static Message createSentMessage(Message m) {
        m.setId(UUID.randomUUID().toString());
        m.setSent(true);
        m.setSeen(false);
        m.setTimestamp(System.currentTimeMillis());
        return m;
    }

    public static Message createReceivedMessage(Message m) {
        m.setId(UUID.randomUUID().toString());
        m.setSent(false);
        m.setSeen(true);
        m.setTimestamp(System.currentTimeMillis());
        return m;
    }
}
