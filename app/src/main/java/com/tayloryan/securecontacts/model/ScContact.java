package com.tayloryan.securecontacts.model;

import android.text.TextUtils;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class ScContact {

    private String name;
    private String namePinYin;
    private String company;
    private String job;

    private String address_work;
    private String address_home;

    private String phoneNumber_mobile;
    private String phoneNumber_work;
    private String phoneNumber_home;
    private String phoneNumber_fax_work;
    private String phoneNumber_fax_home;
    private String phoneNumber_other;

    private String mail_personal;
    private String mail_work;
    private String mail_other;

    private String photo_uri;
    private String photo_id;

    private boolean hasAvatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePinYin() {
        return namePinYin;
    }

    public void setNamePinYin(String namePinYin) {
        this.namePinYin = namePinYin;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAddress_work() {
        return address_work;
    }

    public void setAddress_work(String address_work) {
        this.address_work = address_work;
    }

    public String getAddress_home() {
        return address_home;
    }

    public void setAddress_home(String address_home) {
        this.address_home = address_home;
    }

    public String getPhoneNumber_mobile() {
        return phoneNumber_mobile;
    }

    public void setPhoneNumber_mobile(String phoneNumber_mobile) {
        this.phoneNumber_mobile = phoneNumber_mobile;
    }

    public String getPhoneNumber_work() {
        return phoneNumber_work;
    }

    public void setPhoneNumber_work(String phoneNumber_work) {
        this.phoneNumber_work = phoneNumber_work;
    }

    public String getPhoneNumber_home() {
        return phoneNumber_home;
    }

    public void setPhoneNumber_home(String phoneNumber_home) {
        this.phoneNumber_home = phoneNumber_home;
    }

    public String getPhoneNumber_fax_work() {
        return phoneNumber_fax_work;
    }

    public void setPhoneNumber_fax_work(String phoneNumber_fax_work) {
        this.phoneNumber_fax_work = phoneNumber_fax_work;
    }

    public String getPhoneNumber_fax_home() {
        return phoneNumber_fax_home;
    }

    public void setPhoneNumber_fax_home(String phoneNumber_fax_home) {
        this.phoneNumber_fax_home = phoneNumber_fax_home;
    }

    public String getPhoneNumber_other() {
        return phoneNumber_other;
    }

    public void setPhoneNumber_other(String phoneNumber_other) {
        this.phoneNumber_other = phoneNumber_other;
    }

    public String getMail_personal() {
        return mail_personal;
    }

    public void setMail_personal(String mail_personal) {
        this.mail_personal = mail_personal;
    }

    public String getMail_work() {
        return mail_work;
    }

    public void setMail_work(String mail_work) {
        this.mail_work = mail_work;
    }

    public String getMail_other() {
        return mail_other;
    }

    public void setMail_other(String mail_other) {
        this.mail_other = mail_other;
    }

    public String getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(String photo_uri) {
        this.photo_uri = photo_uri;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public boolean isHasAvatar() {
        return photo_id != null || photo_uri != null;
    }

    public String getFirstTextOfName() {
        if (!TextUtils.isEmpty(name)) {
            return name.substring(0, 1);
        }

        return "";
    }
}
