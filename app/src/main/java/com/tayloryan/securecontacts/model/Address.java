package com.tayloryan.securecontacts.model;

import android.provider.ContactsContract;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Address extends BmobObject implements Serializable {

    private int id;
    private String address;
    private Type type;
    private String encryption;

    public Address(int id, String address, Type type, String encryption) {
        this.id = id;
        this.address = address;
        this.type = type;
        this.encryption = encryption;
    }

    public Address(String address, Type type, String encryption) {
        this.id = 0;
        this.address = address;
        this.type = type;
        this.encryption = encryption;
    }

    public enum Type {
        CUSTOM,
        HOME,
        WORK,
        OTHER,
        UNKNOWN;

        public static Type fromValue(int value) {
            switch (value) {
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM:
                    return CUSTOM;
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME:
                    return HOME;
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK:
                    return WORK;
                case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER:
                    return OTHER;
                default:
                    return UNKNOWN;
            }
        }

        public static int fromType(Type type) {
            switch (type) {
                case CUSTOM:
                    return ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM;
                case HOME:
                    return ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME;
                case WORK:
                    return ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK;
                case OTHER:
                    return ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER;
                case UNKNOWN:
                default:
                    return -1;
            }
        }
    }

    public static String addrTypeToString(Type type) {
        switch (type) {
            case CUSTOM:
                return "自定义";
            case HOME:
                return "住宅";
            case WORK:
                return "工作";
            case OTHER:
                return "其他";
            case UNKNOWN:
            default:
                return "未知";
        }
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Type getType() {
        return type;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }
}
