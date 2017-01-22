package com.tayloryan.securecontacts;

import android.app.Application;
import android.content.Context;

import com.github.tamir7.contacts.Contacts;

import org.androidannotations.annotations.EApplication;


@EApplication
public class SecurityContactApplication extends Application {

    private static SecurityContactApplication context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Contacts.initialize(this);
    }

    public static Context getAppContext() {
        return SecurityContactApplication_.getInstance().getApplicationContext();
    }
}
