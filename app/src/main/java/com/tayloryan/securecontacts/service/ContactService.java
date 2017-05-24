package com.tayloryan.securecontacts.service;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import com.tayloryan.securecontacts.Constants;
import com.tayloryan.securecontacts.ScApp_;
import com.tayloryan.securecontacts.dao.impl.ContactDaoImpl;
import com.tayloryan.securecontacts.model.Address;
import com.tayloryan.securecontacts.model.Email;
import com.tayloryan.securecontacts.model.MimeType;
import com.tayloryan.securecontacts.model.PhoneNumber;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.util.ColorUtil;
import com.tayloryan.securecontacts.util.MD5Util;
import com.tayloryan.securecontacts.util.PinYinUtil;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

@EBean
public class ContactService {

    @Bean
    protected ContactDaoImpl contactDao;

    public List<ScContact> importContactsFromSys() {

        List<ScContact> contacts = new ArrayList<>();
        List<PhoneNumber> phoneNumbers;
        List<Email> emails;
        List<Address> addresses;

        ContentResolver mContentResolver= ScApp_.getAppContext().getContentResolver();
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        while (cursor.moveToNext()) {
            phoneNumbers = new ArrayList<>();
            emails = new ArrayList<>();
            addresses = new ArrayList<>();

            ScContact mContact = new ScContact();
            mContact.setNameBackColor(ColorUtil.getRandomColorDrawableRes( new Random().nextInt(10)));
            String id=cursor.getString(cursor.getColumnIndex("_id"));
            String title=cursor.getString(cursor.getColumnIndex("display_name"));
            String firstHeadLetter=cursor.getString(cursor.getColumnIndex("phonebook_label"));
            mContact.setName(title);
            mContact.setPinOfName(PinYinUtil.getPingYin(title));
            mContact.setPinHeaderOfName(PinYinUtil.getPinYinHeadChar(title));
            mContact.setFirstLetterOfName(firstHeadLetter);


            Cursor dataCursor=mContentResolver.query(dataUri,null,"raw_contact_id= ?",new String[]{id},null);
            while(dataCursor.moveToNext()) {
                mContact.setLookUpKey(dataCursor.getString(dataCursor.getColumnIndex("lookup")));
                mContact.setPhotoUri(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)));
                String type=dataCursor.getString(dataCursor.getColumnIndex("mimetype"));

                String data = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                int dataType = dataCursor.getInt(dataCursor.getColumnIndex("data2"));

                mContact.setPhotoUri(cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.PHOTO_URI)));

                if (MimeType.PHONE_TYPE.equals(type)) {
                    com.tayloryan.securecontacts.model.PhoneNumber.Type phoneType = dataType == -1 ?
                            com.tayloryan.securecontacts.model.PhoneNumber.Type.UNKNOWN : com.tayloryan.securecontacts.model.PhoneNumber.Type.fromValue(dataType);
                    phoneNumbers.add(new com.tayloryan.securecontacts.model.PhoneNumber(data, phoneType, null));

                }

                if (MimeType.EMAIL_TYPE.equals(type)) {
                    Email.Type emailType = dataType == -1 ? Email.Type.UNKNOWN : Email.Type.fromValue(dataType);
                    emails.add(new Email(data, emailType, null));
                }

                if (MimeType.ADDRESS_TYPE.equals(type)) {
                    Address.Type addressType = dataType == -1 ? Address.Type.UNKNOWN : Address.Type.fromValue(dataType);
                    addresses.add(new Address(data, addressType, null));
                }

                if (ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                        .equals(type)) {
                    mContact.setCompanyName(dataCursor.getString(dataCursor.getColumnIndex("data1")));
                    mContact.setJob(dataCursor.getString(dataCursor.getColumnIndex("data4")));
                }
            }
            dataCursor.close();
            mContact.setPhoneNumbers(phoneNumbers);
            mContact.setEmails(emails);
            mContact.setAddresses(addresses);
            contacts.add(mContact);
        }
        cursor.close();
        return contacts;
    }

    public void saveContacts(List<ScContact> contacts) {
        List<ScContact> localContacts = readAllContacts();
        for (ScContact contact:contacts) {
            if (localContacts.size() > 0) {
                for (ScContact localContact : localContacts) {
                    if (!localContact.equals(contact)) {
                        saveContact(contact);
                    }
                }
            } else {
                saveContacts(contacts);
            }

        }

    }

    public void saveContact(ScContact contact) {
//        if (!contactDao.getContactByLookUpKey(contact.getLookUpKey())) {
            contactDao.saveContact(contact);
//        }
    }

    public List<ScContact> readAllContacts() {
        return  contactDao.getAllContacts();
    }

    public void encryptContact(int contactId, String pwd) {
        contactDao.setContactEncryption(contactId, null == pwd ? null : MD5Util.encrypt(pwd));
    }

    public void encryptContactField(int dataId, String pwd) {
        contactDao.setContactFieldEncryption(dataId, null == pwd ? null : MD5Util.encrypt(pwd));
    }

    public void deleteContact(int contactId) {
        contactDao.deleteContact(contactId);
    }

    public void importFromCloud(final Handler handler) {
        BmobQuery<ScContact> contactQuery = new BmobQuery<>();
        contactQuery.addWhereEqualTo("mUserId", BmobUser.getCurrentUser().getObjectId());
        contactQuery.findObjects(new FindListener<ScContact>() {
            @Override
            public void done(List<ScContact> contacts, BmobException e) {
                if (null == e) {
                    saveContacts(contacts);
                    handler.sendEmptyMessage(Constants.RETURN_SUCCESS);
                } else {
                    Message message = new Message();
                    message.what = Constants.RETURN_ERROR;
                    message.obj = e;
                    handler.sendMessage(message);
                }
            }
        });

    }

    public void uploadToCloud(final Handler handler) {
        List<ScContact> contacts = readAllContacts();
        for (ScContact contact : contacts) {
            contact.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (null != e) {
                        Log.d("添加成功：", s == null? "" : s);
                        handler.sendEmptyMessage(Constants.RETURN_SUCCESS);
                    } else {
                        Log.d("error:", e.getMessage());
                        Message message = new Message();
                        message.what = Constants.RETURN_ERROR;
                        message.obj = e;
                        handler.sendMessage(message);
                    }
                }
            });

        }
    }

}