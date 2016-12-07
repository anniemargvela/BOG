package com.example.annie_pc.projectchat.utils;

import java.util.Random;

import com.example.annie_pc.projectchat.adapters.RecentsAdapter;
import com.example.annie_pc.projectchat.model.Contact;
import io.realm.Realm;

public final class RandUtils {
    private static Random random = new Random();

    public synchronized static int randInt(int minValue, int maxValue) {
        return random.nextInt((maxValue - minValue) + 1) + minValue;
    }

    public synchronized static boolean randBoolean() {
        return random.nextBoolean();
    }

    public static Contact randContact() {
        Realm realm = Realm.getDefaultInstance();
        int numContacts = realm.where(Contact.class).findAll().size();

        String contactId = String.valueOf(randInt(1, numContacts));

        return realm.where(Contact.class).equalTo("id", contactId).findFirst();
    }
}
