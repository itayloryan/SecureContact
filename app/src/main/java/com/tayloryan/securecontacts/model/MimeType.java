package com.tayloryan.securecontacts.model;

import java.io.Serializable;

public class MimeType implements Serializable {
    public static final String PHONE_TYPE = "vnd.android.cursor.item/phone_v2";
    public static final String EMAIL_TYPE = "vnd.android.cursor.item/email_v2";
    public static final String ADDRESS_TYPE = "vnd.android.cursor.item/postal-address_v2";

    public static final int PHONE_TYPE_ID = 1;
    public static final int EMAIL_TYPE_ID = 2;
    public static final int ADDRESS_TYPE_ID = 3;
}
