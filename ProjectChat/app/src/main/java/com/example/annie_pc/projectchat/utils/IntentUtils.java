package com.example.annie_pc.projectchat.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.annie_pc.projectchat.activity.ChatActivity;

public class IntentUtils {

    public static Intent buildChatIntent(Context context, String contactId) {
        Intent chatIntent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("contactId", contactId);
        chatIntent.putExtra("Bundle", bundle);
        return chatIntent;
    }
}
