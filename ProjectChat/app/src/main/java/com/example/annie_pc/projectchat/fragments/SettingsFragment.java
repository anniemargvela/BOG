package com.example.annie_pc.projectchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.annie_pc.projectchat.R;
import com.example.annie_pc.projectchat.utils.SharedPreferencesUtils;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.settings_fragment, container, false);
        initControls(settingsView);
        return settingsView;
    }

    private void initControls(View view) {
        initNotificationSwitch(view);
        initSoundsSwitch(view);
    }

    private void initNotificationSwitch(View view) {
        final Switch notificationSwitch = (Switch) view.findViewById(R.id.notification_switch);
        notificationSwitch.setChecked(SharedPreferencesUtils.getNotificationsState(getActivity().getApplicationContext()));
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesUtils.saveNotificationsState(getActivity().getApplicationContext(), isChecked);
            }
        });
    }

    private void initSoundsSwitch(View view) {
        Switch soundsSwitch = (Switch) view.findViewById(R.id.sounds_switch);
        soundsSwitch.setChecked(SharedPreferencesUtils.getSoundsState(getActivity().getApplicationContext()));
        soundsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesUtils.saveSoundsState(getActivity().getApplicationContext(), isChecked);
            }
        });
    }
}
