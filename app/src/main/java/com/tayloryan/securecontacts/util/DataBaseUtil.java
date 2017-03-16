package com.tayloryan.securecontacts.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.androidannotations.annotations.EBean;

/**
 * Created by taylor.yan on 3/8/17.
 */

@EBean(scope = EBean.Scope.Singleton)
public class DataBaseUtil extends SQLiteOpenHelper {


    public DataBaseUtil(Context context) {
        super(context, "db_contact", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
