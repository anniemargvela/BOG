package com.example.annie_pc.projectchat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.example.annie_pc.projectchat.R;
import com.example.annie_pc.projectchat.model.Contact;
import com.example.annie_pc.projectchat.model.Message;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RecentsAdapter extends AbstractAdapter {

    private class ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView message;
        private TextView status;

        public ViewHolder(ImageView image, TextView name, TextView message, TextView status) {
            this.image = image;
            this.name = name;
            this.message = message;
            this.status = status;
        }
    }

    public RecentsAdapter(Context context, Realm realm) {
        super(context, realm);
    }

    @Override
    protected RealmResults<Contact> getContacts() {
        return realm.where(Contact.class).greaterThan("lastMessageTimestamp", 0).findAll()
                .sort("lastMessageTimestamp", Sort.DESCENDING);
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
        Contact contact = contacts.get(position);
        Picasso.with(context)
                .load(contact.getAvatarImg())
                .placeholder(R.drawable.ic_person)
                .fit()
                .into(viewHolder.image);
        viewHolder.name.setText(contact.getDisplayName());
        Message message = getMessage(contact);
        setMessageTypeface(viewHolder.message, message.isSeen());
        viewHolder.message.setText(getMessageText(message));
        viewHolder.status.setBackgroundColor(getStatusColor(contact.isOnline()));
    }

    private Message getMessage(Contact contact) {
        return contact.getMessages().sort("timestamp", Sort.DESCENDING).first();
    }

    private void setMessageTypeface(TextView message, boolean seen) {
        if (!seen)
            message.setTypeface(null, Typeface.BOLD);
    }

    private String getMessageText(Message message) {
        String messageText = message.isSent() ? "" : "You: ";
        messageText += message.getText();
        if (messageText.length() > 15) {
            messageText = messageText.substring(0, 15);
            messageText += "...";
        }
        return messageText;
    }

    private static int getStatusColor(boolean online) {
        return online ? Color.GREEN : Color.TRANSPARENT;
    }

    private Object[] getViewAndHolder(View convertView) {
        View rootView;
        ViewHolder viewHolder;

        if (convertView != null) {
            rootView = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            rootView = View.inflate(context, R.layout.recents_list_item, null);
            viewHolder = new ViewHolder(
                    (ImageView) rootView.findViewById(R.id.recents_list_item_image),
                    (TextView) rootView.findViewById(R.id.recents_list_item_name),
                    (TextView) rootView.findViewById(R.id.recents_list_item_message),
                    (TextView) rootView.findViewById(R.id.recents_list_item_status)
            );
            rootView.setTag(viewHolder);
        }

        return new Object[]{rootView, viewHolder};
    }
}
