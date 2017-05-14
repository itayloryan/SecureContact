package com.tayloryan.securecontacts.model;

import android.net.Uri;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class ScContact extends BmobObject implements Serializable {

    private int mId;
    private String mUserId;
    private String mLookUpKey;
    private String mName;
    private String mPinYinOfName;
    private String mPinYinHeaderOfName;
    private String mCompanyName;
    private String mJob;
    private String mFirstLetterOfName;
    private String mEncryption;
    private String mNameEncryption;
    private String mJobEncryption;
    private String mCompanyEncryption;

    private List<PhoneNumber> phoneNumbers;
    private List<Email> emails;
    private List<Address> addresses;

    private String mPhotoUri;
    private int mNameBackColor;
    private String mSearchMatcher;

    public void cloneFrom(ScContact contact) {
        mId = contact.getId();
        mName = contact.getName();
        mUserId = contact.getUserId();
        mLookUpKey = contact.getLookUpKey();
        mPinYinOfName = contact.getPinOfName();
        mPinYinHeaderOfName = contact.getPinHeaderOfName();
        mFirstLetterOfName = contact.getFirstLetterOfName();
        mCompanyName = contact.getCompanyName();
        mJob = contact.getJob();
        mEncryption = contact.getEncryption();
        mNameEncryption = contact.getNameEncryption();
        mJobEncryption = contact.getJobEncryption();
        mCompanyEncryption = contact.getCompanyEncryption();
        phoneNumbers = contact.getPhoneNumbers();
        emails = contact.getEmails();
        addresses = contact.getAddresses();
        mNameBackColor = contact.getNameBackColor();
        mPhotoUri = String.valueOf(contact.getPhotoUri());
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    private boolean hasAvatar;

    public void setId(int id) {
        this.mId =id;
    }

    public int getId() {
        return mId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setLookUpKey(String lookUpKey) {
        this.mLookUpKey = lookUpKey;
    }

    public String getLookUpKey() {
        return mLookUpKey;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }


    public String getPinOfName() {
        return mPinYinOfName;
    }

    public void setPinOfName(String pinOfName) {
        mPinYinOfName = pinOfName;
    }

    public void setPinHeaderOfName(String mPinHeaderOfName) {
        this.mPinYinHeaderOfName = mPinHeaderOfName;
    }

    public String getPinHeaderOfName() {
        return mPinYinHeaderOfName;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }

    public String getJob() {
        return mJob;
    }

    public void setJob(String job) {
        mJob = job;
    }

    public Uri getPhotoUri() {
        return TextUtils.isEmpty(mPhotoUri)? null : Uri.parse(mPhotoUri);
    }

    public void setPhotoUri(String photoUri) {
        mPhotoUri = photoUri;
    }

    public String getFirstTextOfName() {
        if (!TextUtils.isEmpty(mName)) {
            String firstText = mName.substring(0, 1);
            return firstText.toUpperCase();
        }

        return "";
    }

    public void setFirstLetterOfName(String firstLetterOfName) {
        mFirstLetterOfName = firstLetterOfName;
    }

    public String getFirstLetterOfName() {
        return mFirstLetterOfName;
    }

    public void setNameBackColor(int nameBackColor) {
        mNameBackColor = nameBackColor;
    }

    public int getNameBackColor() {
        return mNameBackColor;
    }


    public String getEncryption() {
        return mEncryption;
    }

    public void setEncryption(String mEncrypt) {
        this.mEncryption = mEncrypt;
    }

    public String getNameEncryption() {
        return mNameEncryption;
    }

    public void setNameEncryption(String nameEncryption) {
        this.mNameEncryption = nameEncryption;
    }

    public String getJobEncryption() {
        return mJobEncryption;
    }

    public void setJobEncryption(String jobEncryption) {
        this.mJobEncryption = jobEncryption;
    }

    public String getCompanyEncryption() {
        return mCompanyEncryption;
    }

    public void setCompanyEncryption(String companyEncryption) {
        this.mCompanyEncryption = companyEncryption;
    }

    public void setSearchMatcher(String searchMatcher) {
        this.mSearchMatcher = searchMatcher;
    }

    public String getSearchMatcher() {
        return mSearchMatcher;
    }
}
