package com.example.annie_pc.projectchat.model;

public class ChatMessage {

    private String text;
    private boolean sent;

    public ChatMessage(String text, boolean sent) {
        this.text = text;
        this.sent = sent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
