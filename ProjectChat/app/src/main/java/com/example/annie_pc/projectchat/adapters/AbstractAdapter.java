package com.example.annie_pc.projectchat.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.annie_pc.projectchat.model.Contact;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public abstract class AbstractAdapter extends BaseAdapter {

    protected Context context;
    protected Realm realm;
    protected RealmResults<Contact> contacts;

    public AbstractAdapter(Context context, Realm realm) {
        this.context = context;
        this.realm = realm;
        this.realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                notifyDataSetChanged();
            }
        });
        this.contacts = getContacts();
    }

    protected abstract RealmResults<Contact> getContacts();

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
