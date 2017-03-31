package com.tayloryan.securecontacts.model;

import android.net.Uri;
import android.text.TextUtils;

import com.tayloryan.securecontacts.util.ColorUtil;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class ScContact {

    private String mName;
    private String mPinOfName;
    private String mCompanyName;
    private String mJob;
    private String mFirstLetterOfName;

    private String mAddressWork;
    private String mAddressHome;

    private String mPhoneNumberMobile;
    private String mPhoneNumberWork;
    private String mPhoneNumberHome;
    private String mPhoneNumberFaxWork;
    private String mPhoneNumberFaxHome;
    private String mPhoneNumberOther;

    private String mMailPersonal;
    private String mMailWork;

    private String mPhotoUri;
    private String mPhotoId;
    private int mPhotoResId;
    private int mNameBackColor;

    private boolean hasAvatar;

    public void setAvatarBackRes() {
        mPhotoResId = ColorUtil.getRandomColorDrawableRes();
    }

    public int getPhotoResId() {
        return mPhotoResId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPinOfName() {
        return mPinOfName;
    }

    public void setPinOfName(String pinOfName) {
        mPinOfName = pinOfName;
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

    public String getAddressWork() {
        return mAddressWork;
    }

    public void setAddressWork(String addressWork) {
        mAddressWork = addressWork;
    }

    public String getAddressHome() {
        return mAddressHome;
    }

    public void setAddressHome(String addressHome) {
        mAddressHome = addressHome;
    }

    public String getPhoneNumberMobile() {
        return mPhoneNumberMobile;
    }

    public void setPhoneNumberMobile(String phoneNumberMobile) {
        mPhoneNumberMobile = phoneNumberMobile;
    }

    public String getPhoneNumberWork() {
        return mPhoneNumberWork;
    }

    public void setPhoneNumberWork(String phoneNumberWork) {
        mPhoneNumberWork = phoneNumberWork;
    }

    public String getPhoneNumberHome() {
        return mPhoneNumberHome;
    }

    public void setPhoneNumberHome(String phoneNumberHome) {
        mPhoneNumberHome = phoneNumberHome;
    }

    public String getPhoneNumberFaxWork() {
        return mPhoneNumberFaxWork;
    }

    public void setPhoneNumberFaxWork(String phoneNumberFaxWork) {
        mPhoneNumberFaxWork = phoneNumberFaxWork;
    }

    public String getPhoneNumberFaxHome() {
        return mPhoneNumberFaxHome;
    }

    public void setPhoneNumberFaxHome(String phoneNumberFaxHome) {
        mPhoneNumberFaxHome = phoneNumberFaxHome;
    }

    public String getPhoneNumberOther() {
        return mPhoneNumberOther;
    }

    public void setPhoneNumberOther(String phoneNumberOther) {
        mPhoneNumberOther = phoneNumberOther;
    }

    public String getMailPersonal() {
        return mMailPersonal;
    }

    public void setMailPersonal(String mailPersonal) {
        mMailPersonal = mailPersonal;
    }

    public String getMailWork() {
        return mMailWork;
    }

    public void setMailWork(String mailWork) {
        mMailWork = mailWork;
    }

    public Uri getPhotoUri() {
        return TextUtils.isEmpty(mPhotoUri)? null : Uri.parse(mPhotoUri);
    }

    public void setPhotoUri(String photoUri) {
        mPhotoUri = photoUri;
    }

    public String getPhotoId() {
        return mPhotoId;
    }

    public void setPhotoId(String photoId) {
        mPhotoId = photoId;
    }

    public boolean isHasAvatar() {
        return mPhotoId != null || mPhotoUri != null;
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
}
