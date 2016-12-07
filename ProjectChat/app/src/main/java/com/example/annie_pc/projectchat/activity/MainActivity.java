package com.example.annie_pc.projectchat.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.annie_pc.projectchat.MyApplication;
import com.example.annie_pc.projectchat.R;
import com.example.annie_pc.projectchat.adapters.ViewPagerAdapter;
import com.example.annie_pc.projectchat.fragments.ContactsFragment;
import com.example.annie_pc.projectchat.fragments.RecentsFragment;
import com.example.annie_pc.projectchat.fragments.SettingsFragment;
import com.example.annie_pc.projectchat.model.Contact;
import com.example.annie_pc.projectchat.model.Message;
import com.example.annie_pc.projectchat.utils.RequestClient;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    public static final String DATA_SOURCE = "https://dl.dropboxusercontent.com/u/28030891/FreeUni/Android/assinments/contacts.json";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_recents,
            R.drawable.ic_contacts,
            R.drawable.ic_settings
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    private void getData() {
        if (dataExists())
            return;

        String url = DATA_SOURCE;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(final JSONObject response) {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        try {
                            JSONArray contacts = response.getJSONArray("contactList");
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject jsonObject = (JSONObject) contacts.get(i);
                                Contact contact = realm.createObject(Contact.class);
                                contact.setId(jsonObject.getString("id"));
                                contact.setDisplayName(jsonObject.getString("displayName"));
                                contact.setPhoneNumber(jsonObject.getString("phoneNumber"));
                                contact.setAvatarImg(jsonObject.getString("avatarImg"));
                                contact.setOnline(true);
                                contact.setLastMessageTimestamp(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestClient.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private boolean dataExists() {
        return Realm.getDefaultInstance().where(Contact.class).findAll().size() > 0;
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabIcons.length; i++)
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecentsFragment(), "Recents");
        adapter.addFragment(new ContactsFragment(), "Contacts");
        adapter.addFragment(new SettingsFragment(), "Settings");
        viewPager.setAdapter(adapter);
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
