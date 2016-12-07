package com.example.annie_pc.projectchat.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.example.annie_pc.projectchat.R;
import com.example.annie_pc.projectchat.model.ChatMessage;
import com.example.annie_pc.projectchat.model.Contact;
import com.example.annie_pc.projectchat.model.Message;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ChatAdapter extends BaseAdapter {

    private class ViewHolder {
        private ImageView image;
        private TextView message;

        public ViewHolder(ImageView image, TextView message) {
            this.image = image;
            this.message = message;
        }
    }

    private final Context context;
    private final Realm realm;
    private final Contact contact;
    private List<ChatMessage> chatMessages;

    public ChatAdapter(Context context, Realm realm, final Contact contact) {
        this.context = context;
        this.realm = realm;
        this.contact = contact;
        this.chatMessages = getChatMessages();
        this.realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                element.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Message message : contact.getMessages()) {
                            message.setSeen(true);
                        }
                    }
                });
                ChatAdapter.this.chatMessages = getChatMessages();
                ChatAdapter.this.notifyDataSetInvalidated();
            }
        });
    }

    private List<ChatMessage> getChatMessages() {
        RealmResults<Message> sortedMessages = contact.getMessages().sort("timestamp");
        return groupMessages(sortedMessages);
    }

    private List<ChatMessage> groupMessages(RealmResults<Message> sortedMessages) {
        List<ChatMessage> chatMessages = new ArrayList<>();

        for (Message message : sortedMessages) {
            if (chatMessages.isEmpty()) {
                addNewChatMessage(chatMessages, message);
            } else {
                if (message.isSent() == chatMessages.get(chatMessages.size() - 1).isSent()) {
                    appendChatMessage(chatMessages, message);
                } else {
                    addNewChatMessage(chatMessages, message);
                }
            }
        }

        return chatMessages;
    }

    private void appendChatMessage(List<ChatMessage> chatMessages, Message message) {
        ChatMessage chatMessage = chatMessages.get(chatMessages.size() - 1);
        chatMessage.setText(chatMessage.getText() + "\n" + message.getText());
    }

    private void addNewChatMessage(List<ChatMessage> chatMessages, Message message) {
        ChatMessage chatMessage = new ChatMessage(message.getText(), message.isSent());
        chatMessages.add(chatMessage);
    }


    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object[] viewAndHolder = getViewAndHolder(convertView);

        View rootView = (View) viewAndHolder[0];
        ViewHolder viewHolder = (ViewHolder) viewAndHolder[1];

        fillHolder(viewHolder, position);

        return rootView;
    }

    private void fillHolder(ViewHolder viewHolder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        if (chatMessage.isSent()) {
            Picasso.with(context)
                    .load(contact.getAvatarImg())
                    .placeholder(R.drawable.ic_person)
                    .fit()
                    .into(viewHolder.image);
            viewHolder.image.setVisibility(View.VISIBLE);
        } else {
            viewHolder.image.setVisibility(View.INVISIBLE);
        }

        viewHolder.message.setText(chatMessage.getText());
        viewHolder.message.setBackgroundResource(getBGColor(chatMessage.isSent()));
    }

    private int getBGColor(boolean sent) {
        return sent ? R.color.colorAccent : R.color.colorPrimary;
    }

    private Object[] getViewAndHolder(View convertView) {
        View rootView;
        ViewHolder viewHolder;

        if (convertView != null) {
            rootView = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            rootView = View.inflate(context, R.layout.chat_list_item, null);
            viewHolder = new ViewHolder(
                    (ImageView) rootView.findViewById(R.id.chat_list_item_image),
                    (TextView) rootView.findViewById(R.id.chat_list_item_message)
            );
            rootView.setTag(viewHolder);
        }

        return new Object[]{rootView, viewHolder};
    }
}
