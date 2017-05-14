package com.tayloryan.securecontacts.model;

import android.provider.ContactsContract;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Email  extends BmobObject implements Serializable {

    private final int id;
    private final String email;
    private final Type type;
    private String encryption;

    public enum Type {
        CUSTOM,
        HOME,
        WORK,
        OTHER,
        MOBILE,
        UNKNOWN;

        public static Type fromValue(int value) {
            switch (value) {
                case ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM:
                    return CUSTOM;
                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                    return HOME;
                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                    return WORK;
                case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                    return OTHER;
                case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
                    return MOBILE;
                default:
                    return UNKNOWN;
            }
        }

        public static int fromType(Type type) {
            switch (type) {
                case CUSTOM:
                    return ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM;
                case HOME:
                    return ContactsContract.CommonDataKinds.Email.TYPE_HOME;
                case WORK:
                    return ContactsContract.CommonDataKinds.Email.TYPE_WORK;
                case OTHER:
                    return ContactsContract.CommonDataKinds.Email.TYPE_OTHER;
                case MOBILE:
                    return ContactsContract.CommonDataKinds.Email.TYPE_MOBILE;
                case UNKNOWN:
                default:
                    return -1;
            }
        }
    }

    public Email(int id, String email, Type type, String encryption) {
        this.id = id;
        this.email = email;
        this.type = type;
        this.encryption = encryption;
    }

    public Email(String email, Type type, String encryption) {
        this.id = 0;
        this.email = email;
        this.type = type;
        this.encryption = encryption;
    }

    public static String mailTypeToString(Type type) {
        switch (type) {
            case CUSTOM:
                return "自定义";
            case HOME:
                return "个人";
            case WORK:
                return "工作";
            case OTHER:
                return "其他";
            case MOBILE:
                return "手机邮件";
            case UNKNOWN:
            default:
                return "未知";
        }
    }

    public int getId() {
        return id;
    }

    /**
     * Gets the email email.
     *
     * @return email.
     */
    public String getAddress() {
        return email;
    }

    /**
     * Gets the type of email.
     *
     * @return type of email.
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the encryption. (null unless type = TYPE_CUSTOM)
     *
     * @return encryption.
     */
    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Email email = (Email) o;

        return this.email.equals(email.email) && type == email.type &&
                !(encryption != null ? !encryption.equals(email.encryption) : email.encryption != null);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (encryption != null ? encryption.hashCode() : 0);
        return result;
    }
}
