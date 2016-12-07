package com.example.annie_pc.projectchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.annie_pc.projectchat.MyApplication;
import com.example.annie_pc.projectchat.R;
import com.example.annie_pc.projectchat.adapters.ChatAdapter;
import com.example.annie_pc.projectchat.generator.ChatGenerator;
import com.example.annie_pc.projectchat.model.Contact;
import com.example.annie_pc.projectchat.model.Message;
import com.example.annie_pc.projectchat.utils.RandUtils;
import com.example.annie_pc.projectchat.utils.SharedPreferencesUtils;
import io.realm.Realm;

public class ChatActivity extends AppCompatActivity {

    private Contact contact;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        if (intent.hasExtra("Bundle")) {
            Bundle bundle = intent.getBundleExtra("Bundle");
            String contactId = bundle.getString("contactId");
            this.contact = getContact(contactId);
        }

        initControls();
    }

    private void initControls() {
        initToolbar();
        initListView();
        initSendButton();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(contact.getDisplayName());
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initSendButton() {
        final EditText sendText = (EditText) findViewById(R.id.chat_send_text);
        assert sendText != null;
        final ImageButton sendButton = (ImageButton) findViewById(R.id.chat_send_btn);
        assert sendButton != null;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageText = sendText.getText().toString().trim();

                if (!messageText.isEmpty()) {
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Message m = realm.createObject(Message.class);
                            m.setText(messageText);
                            Message.createReceivedMessage(m);
                            contact.setLastMessageTimestamp(m.getTimestamp());
                            contact.getMessages().add(m);
                        }
                    });
                    sendText.setText("");
                    long timeout = RandUtils.randInt(1, 10) * 1000;
                    MyApplication.getHandler().postDelayed(new ChatGenerator(contact), timeout);
                }
            }
        });
    }

    private void initListView() {
        adapter = new ChatAdapter(this, Realm.getDefaultInstance(), contact);
        final ListView chatListView = (ListView) findViewById(R.id.chat_list_view);
        assert chatListView != null;
        chatListView.setAdapter(adapter);
    }

    private Contact getContact(String contactId) {
        Realm realm = Realm.getDefaultInstance();
        final Contact contact = realm.where(Contact.class).equalTo("id", contactId).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Message message : contact.getMessages()) {
                    message.setSeen(true);
                }
            }
        });
        return contact;
    }

    @Override
    protected void onResume() {
        MyApplication.setFolded(false);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MyApplication.setFolded(true);
        super.onPause();
    }
}
