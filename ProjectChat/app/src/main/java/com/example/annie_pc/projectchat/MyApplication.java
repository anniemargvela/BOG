package com.example.annie_pc.projectchat;

import android.app.Application;
import android.os.Handler;

import org.xml.sax.HandlerBase;

import com.example.annie_pc.projectchat.generator.GlobalGenerator;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private static Handler handler = new Handler();
    private static boolean folded = true;

    public static Handler getHandler() {
        return handler;
    }

    public static boolean isFolded() {
        return folded;
    }

    public static void setFolded(boolean folded) {
        MyApplication.folded = folded;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        getHandler().postDelayed(new GlobalGenerator(this, handler), GlobalGenerator.TIMEOUT);
    }
}
