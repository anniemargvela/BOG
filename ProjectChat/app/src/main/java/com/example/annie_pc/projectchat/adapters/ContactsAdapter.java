package com.example.annie_pc.projectchat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.example.annie_pc.projectchat.R;
import com.example.annie_pc.projectchat.model.Contact;
import io.realm.Realm;
import io.realm.RealmResults;

public class ContactsAdapter extends AbstractAdapter {

    private class ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView status;

        public ViewHolder(ImageView image, TextView name, TextView status) {
            this.image = image;
            this.name = name;
            this.status = status;
        }
    }

    public ContactsAdapter(Context context, Realm realm) {
        super(context, realm);
    }

    @Override
    protected RealmResults<Contact> getContacts() {
        return realm.where(Contact.class).findAll().sort("displayName");
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
        viewHolder.status.setBackgroundColor(getStatusColor(contact.isOnline()));
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
            rootView = View.inflate(context, R.layout.contacts_list_item, null);
            viewHolder = new ViewHolder(
                    (ImageView) rootView.findViewById(R.id.contacts_list_item_image),
                    (TextView) rootView.findViewById(R.id.contacts_list_item_name),
                    (TextView) rootView.findViewById(R.id.contacts_list_item_status)
            );
            rootView.setTag(viewHolder);
        }

        return new Object[]{rootView, viewHolder};
    }

    public void filter(String s) {
        if (s != null) {
            contacts = realm.where(Contact.class).beginsWith("displayName", s).findAll().sort("displayName");
            notifyDataSetChanged();
        }
    }

}
