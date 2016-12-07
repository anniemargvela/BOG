package com.example.annie_pc.projectchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.annie_pc.projectchat.R;
import com.example.annie_pc.projectchat.adapters.ContactsAdapter;
import com.example.annie_pc.projectchat.adapters.RecentsAdapter;
import com.example.annie_pc.projectchat.model.Contact;
import com.example.annie_pc.projectchat.utils.IntentUtils;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class RecentsFragment extends Fragment {

    private RecentsAdapter adapter;

    public RecentsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View recentsView = inflater.inflate(R.layout.recents_fragment, container, false);
        initControls(recentsView);
        return recentsView;
    }

    private void initControls(View view) {
        initListView(view);
    }

    private void initListView(View view) {
        adapter = new RecentsAdapter(getContext(), Realm.getDefaultInstance());
        final ListView recentsListView = (ListView) view.findViewById(R.id.recents_list_view);
        recentsListView.setAdapter(adapter);
        recentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contactId = ((Contact) adapter.getItem(position)).getId();
                RecentsFragment.this.getContext().startActivity(IntentUtils.buildChatIntent(getContext(), contactId));
            }
        });
        Realm.getDefaultInstance().addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                recentsListView.setAdapter(adapter);
            }
        });
    }
}
