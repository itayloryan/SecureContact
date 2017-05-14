package com.tayloryan.securecontacts;

import android.app.Application;
import android.content.Context;

import com.github.tamir7.contacts.Contacts;
import com.tayloryan.securecontacts.util.BmobConfig;

import org.androidannotations.annotations.EApplication;

import cn.bmob.v3.Bmob;


@EApplication
public class ScApp extends Application {

    private static ScApp context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Bmob.initialize(this, BmobConfig.APP_ID);
    }

    public static Context getAppContext() {
        return ScApp_.getInstance().getApplicationContext();
    }
}
