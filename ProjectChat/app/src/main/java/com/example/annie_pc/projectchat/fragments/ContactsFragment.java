package com.example.annie_pc.projectchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.annie_pc.projectchat.R;
import com.example.annie_pc.projectchat.adapters.ContactsAdapter;
import com.example.annie_pc.projectchat.model.Contact;
import com.example.annie_pc.projectchat.utils.IntentUtils;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;

public class ContactsFragment extends Fragment {

    private ContactsAdapter adapter;

    public ContactsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contactsView = inflater.inflate(R.layout.contacts_fragment, container, false);
        initControls(contactsView);
        return contactsView;
    }

    private void initControls(View view) {
        initListView(view);
        initSearchBar(view);
    }

    private void initListView(View view) {
        adapter = new ContactsAdapter(getContext(), Realm.getDefaultInstance());
        final ListView contactsListView = (ListView) view.findViewById(R.id.contacts_list_view);
        contactsListView.setAdapter(adapter);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contactId = ((Contact) adapter.getItem(position)).getId();
                ContactsFragment.this.getContext().startActivity(IntentUtils.buildChatIntent(getContext(), contactId));
            }
        });
    }

    private void initSearchBar(View view) {
        final EditText searchBar = (EditText) view.findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBar.removeTextChangedListener(this);
                adapter.filter(searchBar.getText().toString());
                searchBar.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
