package com.tayloryan.securecontacts.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.tayloryan.securecontacts.model.Address;
import com.tayloryan.securecontacts.model.Email;
import com.tayloryan.securecontacts.model.MimeType;
import com.tayloryan.securecontacts.model.PhoneNumber;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.util.DataBaseUtil;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_COMPANY_ENCRYPTION;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_COMPANY_NAME;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_CONTACT_ID;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_CONTACT_LOOK_UP;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_ENCRYPTION;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_FIRST_LETTER_NAME;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_ID;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_JOB;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_JOB_ENCRYPTION;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_NAME;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_NAME_COLOR;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_NAME_ENCRYPTION;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_PHOTO_URI;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_PINYIN;
import static com.tayloryan.securecontacts.util.DataBaseUtil.FIELD_PINYIN_HEADER;
import static com.tayloryan.securecontacts.util.DataBaseUtil.TABLE_CONTACT_DATA_NAME;
import static com.tayloryan.securecontacts.util.DataBaseUtil.TABLE_CONTACT_NAME;


@EBean
public class ContactDaoImpl {

    private static final String TAG = "ContactDaoImpl";

    private static final String SQL_GET_ALL_CONTACTS = "SELECT * FROM " + TABLE_CONTACT_NAME +
            " WHERE user_id = ?";

    private static final String SQL_GET_CONTACT_DATA = "SELECT * FROM " + TABLE_CONTACT_DATA_NAME +
            " WHERE contact_id = ?";

//    private static final String SQL_GET_ALL_CONTACTS = "SELECT * FROM " + TABLE_CONTACT_NAME
//            + " LEFT JOIN " + TABLE_CONTACT_DATA_NAME + " ON "+TABLE_CONTACT_NAME +"._id = " +
//            TABLE_CONTACT_DATA_NAME + ".contact_id";

    public static final String SQL_CREATE_CONTACT = "INSERT INTO " + TABLE_CONTACT_NAME +
            " (name, pinyin, pinyin_header, user_id, look_up, name_color, first_letter_name, " +
            "company_name, job, photo_uri, encryption, name_encryption, job_encryption, company_encryption)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String SQL_CREATE_CONTACT_DATA = "INSERT INTO " + TABLE_CONTACT_DATA_NAME +
            " (contact_id, mimetype_id, data_type, data, encryption) VALUES (?, ?, ?, ?, ?)";

    public static final String SQL_SET_CONTACT_ENCRYPTION = "UPDATE contact SET encryption = ? WHERE _id = ? AND user_id = ?";

    public static final String SQL_SET_CONTACT_FIELD_ENCRYPTION = "UPDATE contact_data SET encryption = ? WHERE _id = ?";

    public static final String SQL_DELETE_CONTACT = "DELETE FROM contact WHERE _id = ?";
    public static final String SQL_DELETE_CONTACT_DATA = "DELETE FROM contact_data WHERE contact_id = ?";

    @Bean
    protected DataBaseUtil mDataBaseUtil;

    public List<ScContact> getAllContacts() {
        SQLiteDatabase database = mDataBaseUtil.getReadableDatabase();
        Cursor cursor;
        Cursor dataCursor;
        List<ScContact> contacts = new ArrayList<>();
        List<PhoneNumber> phoneNumbers;
        List<Email> emails;
        List<Address> addresses;
//        try {
            cursor = database.rawQuery(SQL_GET_ALL_CONTACTS, new String[] {BmobUser.getCurrentUser() == null ?
                    "" : BmobUser.getCurrentUser().getObjectId()});
            while (cursor.moveToNext()) {
                ScContact contact = new ScContact();
                phoneNumbers = new ArrayList<>();
                emails = new ArrayList<>();
                addresses = new ArrayList<>();
                contact.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
                contact.setUserId(BmobUser.getCurrentUser().getObjectId());
                contact.setEncryption(cursor.getString(cursor.getColumnIndex(FIELD_ENCRYPTION)));
                contact.setLookUpKey(cursor.getString(cursor.getColumnIndex(FIELD_CONTACT_LOOK_UP)));
                contact.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                contact.setNameBackColor(cursor.getInt(cursor.getColumnIndex(FIELD_NAME_COLOR)));
                contact.setFirstLetterOfName(cursor.getString(cursor.getColumnIndex(FIELD_FIRST_LETTER_NAME)));
                contact.setPinOfName(cursor.getString(cursor.getColumnIndex(FIELD_PINYIN)));
                contact.setPinHeaderOfName(cursor.getString(cursor.getColumnIndex(FIELD_PINYIN_HEADER)));
                contact.setPhotoUri(cursor.getString(cursor.getColumnIndex(FIELD_PHOTO_URI)));
                contact.setJob(cursor.getString(cursor.getColumnIndex(FIELD_JOB)));
                contact.setCompanyName(cursor.getString(cursor.getColumnIndex(FIELD_COMPANY_NAME)));
                contact.setNameEncryption(cursor.getString(cursor.getColumnIndex(FIELD_NAME_ENCRYPTION)));
                contact.setCompanyEncryption(cursor.getString(cursor.getColumnIndex(FIELD_COMPANY_ENCRYPTION)));
                contact.setJobEncryption(cursor.getString(cursor.getColumnIndex(FIELD_JOB_ENCRYPTION)));

                dataCursor = database.rawQuery(SQL_GET_CONTACT_DATA, new String[] {String.valueOf(contact.getId())});
                while(dataCursor.moveToNext()) {
                    int type = dataCursor.getInt(dataCursor.getColumnIndex("mimetype_id"));

                    int dataId = dataCursor.getInt(dataCursor.getColumnIndex("_id"));
                    String data = dataCursor.getString(dataCursor.getColumnIndex("data"));
                    int dataType = dataCursor.getInt(dataCursor.getColumnIndex("data_type"));
                    String encryption = dataCursor.getString(dataCursor.getColumnIndex("encryption"));

                    if (MimeType.PHONE_TYPE_ID == type) {
                        PhoneNumber.Type phoneType = dataType == -1 ? PhoneNumber.Type.UNKNOWN : PhoneNumber.Type.fromValue(dataType);
                        phoneNumbers.add(new PhoneNumber(dataId, data, phoneType, encryption));

                    }

                    if (MimeType.EMAIL_TYPE_ID == type) {
                        Email.Type emailType = dataType == -1 ? Email.Type.UNKNOWN : Email.Type.fromValue(dataType);
                        emails.add(new Email(dataId, data, emailType, encryption));
                    }

                    if (MimeType.ADDRESS_TYPE_ID == type) {
                        Address.Type addressType = dataType == -1 ? Address.Type.UNKNOWN : Address.Type.fromValue(dataType);
                        addresses.add(new Address(dataId, data, addressType, encryption));
                    }
                }
                dataCursor.close();
                contact.setPhoneNumbers(phoneNumbers);
                contact.setEmails(emails);
                contact.setAddresses(addresses);
                contacts.add(contact);
            }
            cursor.close();
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }

        return contacts;
    }

    public int getContactId() {
        int id = -1;
        SQLiteDatabase database = mDataBaseUtil.getReadableDatabase();
        String sql = "SELECT last_insert_rowid() FROM " + TABLE_CONTACT_NAME ;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                id = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getContactId: " + e.getMessage());
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return id;

    }

    public boolean getContactByLookUpKey(String lookUpKey) {
        SQLiteDatabase database = mDataBaseUtil.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CONTACT_NAME + " WHERE look_up = ?";
        Cursor cursor = database.rawQuery(sql, new String[] {lookUpKey});
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }

    }

    public void saveContact(ScContact contact) {
        SQLiteDatabase database = mDataBaseUtil.getWritableDatabase();
        try {
            database.execSQL(SQL_CREATE_CONTACT, new Object[] {
                    contact.getName(),
                    contact.getPinOfName(),
                    contact.getPinHeaderOfName(),
                    BmobUser.getCurrentUser() == null ? "" : BmobUser.getCurrentUser().getObjectId(),
                    contact.getLookUpKey(),
                    contact.getNameBackColor(),
                    contact.getFirstLetterOfName(),
                    contact.getCompanyName(),
                    contact.getJob(),
                    contact.getPhotoUri(),
                    contact.getEncryption(),
                    contact.getNameEncryption(),
                    contact.getJobEncryption(),
                    contact.getCompanyEncryption()
            });

            int contactId = getContactId();

            for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
                database.execSQL(SQL_CREATE_CONTACT_DATA, new Object[] {
                        contactId, MimeType.PHONE_TYPE_ID, phoneNumber.getType().fromType(phoneNumber.getType()),
                        phoneNumber.getNumber(), phoneNumber.getEncryption()
                });
            }

            for (Email email : contact.getEmails()) {
                database.execSQL(SQL_CREATE_CONTACT_DATA, new Object[] {
                        contactId, MimeType.EMAIL_TYPE_ID, email.getType().fromType(email.getType()),
                        email.getAddress(), email.getEncryption()
                });
            }

            for (Address address : contact.getAddresses()) {
                database.execSQL(SQL_CREATE_CONTACT_DATA, new Object[] {
                        contactId, MimeType.ADDRESS_TYPE_ID, address.getType().fromType(address.getType()),
                        address.getAddress(), address.getEncryption()
                });
            }

        }catch (Exception e) {
            Log.e(TAG, "saveContact: " +e.getMessage());
        }
    }

    public void setContactEncryption(int contactId, String password) {
        SQLiteDatabase database = mDataBaseUtil.getWritableDatabase();
        database.execSQL(SQL_SET_CONTACT_ENCRYPTION, new Object[] {password, contactId, BmobUser.getCurrentUser().getObjectId()});
    }

    public void setContactFieldEncryption(int dataId, String password) {
        SQLiteDatabase database = mDataBaseUtil.getWritableDatabase();
        database.execSQL(SQL_SET_CONTACT_FIELD_ENCRYPTION, new Object[] {password, dataId});
    }

    public void deleteContact(int contactId) {
        SQLiteDatabase database = mDataBaseUtil.getWritableDatabase();
        database.execSQL(SQL_DELETE_CONTACT, new Object[] {contactId});
        database.execSQL(SQL_DELETE_CONTACT_DATA, new Object[] {contactId});
    }

}
