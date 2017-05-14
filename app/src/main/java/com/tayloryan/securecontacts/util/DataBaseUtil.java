package com.tayloryan.securecontacts.util;

import android.database.sqlite.SQLiteDatabase;

import com.tayloryan.securecontacts.ScApp;
import com.tayloryan.securecontacts.ScSQLiteOpenHelper;

import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class DataBaseUtil {

    public static final String DATABASE_NAME = "db_contact";
    public static final String TABLE_CALL_LOG_NAME = "call_log";
    public static final String TABLE_CONTACT_NAME = "contact";
    public static final String TABLE_CONTACT_DATA_NAME = "contact_data";
    public static final String TABLE_MIMETYPE_NAME = "mimetype";
    public static final String TABLE_MESSAGE_NAME = "message";

    public static final String FIELD_ID = "_id";
    public static final String FIELD_PHONE_NUMBER = "phone_number";
    public static final String FIELD_CALL_TIME = "call_time";
    public static final String FIELD_CALL_TYPE = "call_type";
    public static final String FIELD_DURATION = "duration";

    public static final String FIELD_NAME = "name";
    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_PINYIN = "pinyin";
    public static final String FIELD_PINYIN_HEADER = "pinyin_header";
    public static final String FIELD_NAME_COLOR = "name_color";
    public static final String FIELD_FIRST_LETTER_NAME = "first_letter_name";
    public static final String FIELD_PHONE_MOBILE = "phone_number_mobile";
    public static final String FIELD_PHONE_MOBILE2 = "phone_number_mobile2";
    public static final String FIELD_PHONE_WORK = "phone_number_work";
    public static final String FIELD_PHONE_HOME = "phone_number_home";
    public static final String FIELD_PHONE_FAX = "phone_number_fax";
    public static final String FIELD_PHONE_OTHER = "phone_number_other";
    public static final String FIELD_COMPANY_NAME = "company_name";
    public static final String FIELD_JOB = "job";
    public static final String FIELD_PHOTO_URI = "photo_uri";
    public static final String FIELD_ADDR_HOME = "add_home";
    public static final String FIELD_ADDR_WORK = "add_work";
    public static final String FIELD_MAIL_PERSON = "mail_person";
    public static final String FIELD_MAIL_WORK = "mail_work";

    public static final String FIELD_CONTACT_ID = "contact_id";
    public static final String FIELD_CONTACT_LOOK_UP = "look_up";
    public static final String FIELD_ENCRYPTION = "encryption";
    public static final String FIELD_NAME_ENCRYPTION = "name_encryption";
    public static final String FIELD_PHONE_NUMBER_MOBILE_ENCRYPT = "phone_number_mobile_encrypt";
    public static final String FIELD_PHONE_NUMBER_MOBILE2_ENCRYPT = "phone_number_mobile2_encrypt";
    public static final String FIELD_PHONE_WORK_ENCRYPT = "phone_number_work_encrypt";
    public static final String FIELD_PHONE_HOME_ENCRYPT = "phone_number_home_encrypt";
    public static final String FIELD_PHONE_FAX_ENCRYPT = "phone_number_fax_encrypt";
    public static final String FIELD_PHONE_OTHER_ENCRYPT = "phone_number_other_encrypt";
    public static final String FIELD_COMPANY_ENCRYPTION = "company_encryption";
    public static final String FIELD_JOB_ENCRYPTION = "job_encryption";
    public static final String FIELD_PHOTO_URI_ENCRYPT = "photo_uri_encrypt";
    public static final String FIELD_ADDR_HOME_ENCRYPT = "add_home_encrypt";
    public static final String FIELD_ADDR_WORK_ENCRYPT = "add_work_encrypt";
    public static final String FIELD_MAIL_PERSON_ENCRYPT = "mail_person_encrypt";
    public static final String FIELD_MAIL_WORK_ENCRYPT = "mail_work_encrypt";


    private ScSQLiteOpenHelper sqLiteOpenHelper = new ScSQLiteOpenHelper(ScApp.getAppContext());

    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        return sqLiteDatabase;
    }

    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        return sqLiteDatabase;
    }
}
