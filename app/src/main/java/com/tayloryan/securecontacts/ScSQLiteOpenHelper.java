package com.tayloryan.securecontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ScSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_contact";
    public static final String TABLE_CALL_LOG_NAME = "call_log";
    public static final String TABLE_CONTACT_NAME = "contact";
    public static final String TABLE_CONTACT_DATA_NAME = "contact_data";
    public static final String TABLE_MIMETYPE_NAME = "mimetype";
    public static final String TABLE_MESSAGE_NAME = "message";

    private static final String SQL_CREATE_CALL_LOG_TABLE = "CREATE TABLE " +
            TABLE_CALL_LOG_NAME + "(" +
            "_id INTEGER PRIMARY KEY," +
            "phone_number VARCHAR(50) NOT NULL," +
            "call_time VARCHAR(50) NOT NULL," +
            "call_type INTEGER NOT NULL," +
            "duration VARCHAR(50) NOT NULL" +
            ")";

    private static final String SQL_CREATE_CONTACT_TABLE = "CREATE TABLE " +
            TABLE_CONTACT_NAME + "(" +
            "_id INTEGER PRIMARY KEY," +
            "user_id VARCHAR(50)," +
            "look_up VARCHAR(50)," +
            "name VARCHAR(50)," +
            "pinyin VARCHAR(50)," +
            "pinyin_header VARCHAR(50)," +
            "name_color INTEGER," +
            "first_letter_name VARCHAR(50)," +
            "company_name VARCHAR(255)," +
            "job VARCHAR(50)," +
            "photo_uri VARCHAR(50)," +
            "encryption VARCHAR(255)," +
            "name_encryption VARCHAR(255)," +
            "job_encryption VARCHAR(255)," +
            "company_encryption VARCHAR(255)" +
            ")";

    private static final String SQL_CREATE_CONTACT_DATA_TABLE = "CREATE TABLE " +
            TABLE_CONTACT_DATA_NAME + "(" +
            "_id INTEGER PRIMARY KEY," +
            "contact_id INTEGER," +
            "mimetype_id INTEGER," +
            "data_type INTEGER," +
            "data VARCHAR(255)," +
            "encryption VARCHAR(255)" +
            ")";

    private static final String SQL_CREATE_TABLE_MIMETYPE = "CREATE TABLE " +
            TABLE_MIMETYPE_NAME + "(" +
            "_id INTEGER PRIMARY KEY," +
            "mimetype VARCHAR(255)" +
            ")";

    //TODO message table
    private static final String SQL_CREATE_MESSAGE_TABLE = "CREATE TABLE " +
            TABLE_MESSAGE_NAME + "(" +
            "id INTEGER PRIMARY KEY," +
            "phone_number VARCHAR(50) NOT NULL" +
            "content VARCHAR(255)" +
            "call_type INTEGER NOT NULL" +
            "time VARCHAR(50) NOT NULL)";


    public ScSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CALL_LOG_TABLE);
        db.execSQL(SQL_CREATE_CONTACT_TABLE);
        db.execSQL(SQL_CREATE_CONTACT_DATA_TABLE);
        db.execSQL(SQL_CREATE_TABLE_MIMETYPE);

        ContentValues phoneValues = new ContentValues();
        phoneValues.put("_id", 1);
        phoneValues.put("mimetype", "vnd.android.cursor.item/phone_v2");
        db.insert(TABLE_MIMETYPE_NAME, null, phoneValues);

        ContentValues emailValues = new ContentValues();
        emailValues.put("_id", 2);
        emailValues.put("mimetype", "vnd.android.cursor.item/email_v2");
        db.insert(TABLE_MIMETYPE_NAME, null, emailValues);

        ContentValues addressValues = new ContentValues();
        addressValues.put("_id", 3);
        addressValues.put("mimetype", "vnd.android.cursor.item/postal-address_v2");
        db.insert(TABLE_MIMETYPE_NAME, null, addressValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
