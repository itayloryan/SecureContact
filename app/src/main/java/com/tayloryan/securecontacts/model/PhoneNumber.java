package com.tayloryan.securecontacts.model;


import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class PhoneNumber extends BmobObject implements Serializable {

    private final int id;
    private final String number;
    private final PhoneNumber.Type type;
    private String encryption;

    public PhoneNumber(int id, String number, PhoneNumber.Type type, String encryption) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.encryption = encryption;
    }

    public PhoneNumber(String number, PhoneNumber.Type type, String encryption) {
        this.id = 0;
        this.number = number;
        this.type = type;
        this.encryption = encryption;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return this.number;
    }

    public String getEncryption() {
        return this.encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public PhoneNumber.Type getType() {
        return this.type;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneNumber that = (PhoneNumber) o;

        return number.equals(that.number) &&  type == that.type &&
                !(encryption != null ? !encryption.equals(that.encryption) : that.encryption != null);
    }

    public int hashCode() {
        int result = this.number.hashCode();
        result = 31 * result + this.type.hashCode();
        result = 31 * result + (this.encryption != null?this.encryption.hashCode():0);
        return result;
    }

    public static String phoneTypeToString(Type type) {
        switch (type) {
            case CUSTOM:
                return "自定义";
            case HOME:
                return "住宅";
            case MOBILE:
                return "手机";
            case WORK:
                return "工作";
            case FAX_WORK:
                return "工作传真";
            case FAX_HOME:
                return "住宅传真";
            case PAGER:
                return "寻呼机";
            case OTHER:
                return "其他";
            case CALLBACK:
                return "回拨电话";
            case CAR:
                return "车载电话";
            case COMPANY_MAIN:
                return "公司主机";
            case ISDN:
                return "ISDN";
            case MAIN:
                return "主机";
            case OTHER_FAX:
                return "其他传真";
            case RADIO:
                return "无线装置";
            case TELEX:
                return "电报";
            case TTY_TDD:
                return "TTY TDD";
            case WORK_MOBILE:
                return "工作手机";
            case WORK_PAGER:
                return "工作寻呼机";
            case ASSISTANT:
                return "助理";
            case MMS:
                return "彩信";
            default:
                return "未知";
        }
    }

    public enum Type {
        CUSTOM,
        HOME,
        MOBILE,
        WORK,
        FAX_WORK,
        FAX_HOME,
        PAGER,
        OTHER,
        CALLBACK,
        CAR,
        COMPANY_MAIN,
        ISDN,
        MAIN,
        OTHER_FAX,
        RADIO,
        TELEX,
        TTY_TDD,
        WORK_MOBILE,
        WORK_PAGER,
        ASSISTANT,
        MMS,
        UNKNOWN;

        private Type() {
        }

        public static PhoneNumber.Type fromValue(int value) {
            switch(value) {
                case 0:
                    return CUSTOM;
                case 1:
                    return HOME;
                case 2:
                    return MOBILE;
                case 3:
                    return WORK;
                case 4:
                    return FAX_WORK;
                case 5:
                    return FAX_HOME;
                case 6:
                    return PAGER;
                case 7:
                    return OTHER;
                case 8:
                    return CALLBACK;
                case 9:
                    return CAR;
                case 10:
                    return COMPANY_MAIN;
                case 11:
                    return ISDN;
                case 12:
                    return MAIN;
                case 13:
                    return OTHER_FAX;
                case 14:
                    return RADIO;
                case 15:
                    return TELEX;
                case 16:
                    return TTY_TDD;
                case 17:
                    return WORK_MOBILE;
                case 18:
                    return WORK_PAGER;
                case 19:
                    return ASSISTANT;
                case 20:
                    return MMS;
                default:
                    return UNKNOWN;
            }
        }

        public static int fromType(Type type) {
            switch(type) {
                case CUSTOM:
                    return 0;
                case HOME:
                    return 1;
                case MOBILE:
                    return 2;
                case WORK:
                    return 3;
                case FAX_WORK:
                    return 4;
                case FAX_HOME:
                    return 5;
                case PAGER:
                    return 6;
                case OTHER:
                    return 7;
                case CALLBACK:
                    return 8;
                case CAR:
                    return 9;
                case COMPANY_MAIN:
                    return 10;
                case ISDN:
                    return 11;
                case MAIN:
                    return 12;
                case OTHER_FAX:
                    return 13;
                case RADIO:
                    return 14;
                case TELEX:
                    return 15;
                case TTY_TDD:
                    return 16;
                case WORK_MOBILE:
                    return 17;
                case WORK_PAGER:
                    return 18;
                case ASSISTANT:
                    return 19;
                case MMS:
                    return 20;
                case UNKNOWN:
                default:
                    return -1;
            }
        }
    }
}
