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

    private static final String DATABASE_NAME = "db_contact";
    private static final String TABLE_CALL_LOG_NAME = "call_log";
    private static final String TABLE_CONTACT_NAME = "contact";
    private static final String TABLE_MESSAGE_NAME = "message";

    private static final String SQL_CREATE_CALL_LOG_TABLE = "CREATE TABLE" +
            TABLE_CALL_LOG_NAME + "(" +
            "id INTEGER PRIMARY KEY," +
            "phone_number VARCHAR(50) NOT NULL" +
            "call_time VARCHAR(50) NOT NULL" +
            "call_type INTEGER NOT NULL" +
            "duration VARCHAR(50) NOT NULL)";

    public DataBaseUtil(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CALL_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
