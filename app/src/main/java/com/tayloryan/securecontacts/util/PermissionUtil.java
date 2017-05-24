package com.tayloryan.securecontacts.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.tayloryan.securecontacts.ScApp_;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class PermissionUtil {

    public static final int RESULT_PERMS_CODE = 0x00001;

    public static boolean hasPermission(@Nullable Context context, @NonNull String permission) {
        return context != null && (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean hasReadCallLogPermission() {
        return ScApp_.getInstance() != null && hasPermission(ScApp_.getInstance() , Manifest.permission.READ_CALL_LOG);
    }

    public static boolean hasReadContactPermission() {
        return ScApp_.getInstance()  != null && hasPermission(ScApp_.getInstance() , Manifest.permission.READ_CONTACTS);
    }

    public static boolean hasWriteContactPermission() {
        return ScApp_.getInstance()  != null && hasPermission(ScApp_.getInstance() , Manifest.permission.WRITE_CONTACTS);
    }

    public static boolean hasCallPermission() {
        return ScApp_.getInstance()  != null && hasPermission(ScApp_.getInstance() , Manifest.permission.CALL_PHONE);
    }

    public static boolean hasReadSmsPermission() {
        return ScApp_.getInstance()  != null && hasPermission(ScApp_.getInstance() , Manifest.permission.READ_SMS);
    }

    public static boolean hasSendSmsPermission() {
        return ScApp_.getInstance()  != null && hasPermission(ScApp_.getInstance() , Manifest.permission.SEND_SMS);
    }

    public static boolean hasReceiveSmsPermission() {
        return ScApp_.getInstance()  != null && hasPermission(ScApp_.getInstance() , Manifest.permission.RECEIVE_SMS);
    }

    public static void requestRequiredPermissions(Activity context) {

        String[] requiredPermissions = getRequiredPermissions();
        if (null == requiredPermissions || requiredPermissions.length == 0) {
            return;
        }

        ActivityCompat.requestPermissions(context, requiredPermissions, RESULT_PERMS_CODE);
    }

    private static String[] getRequiredPermissions() {
        List<String> permissionList = new ArrayList<String>();

        if (!hasReadCallLogPermission()) {
            permissionList.add(Manifest.permission.READ_CALL_LOG);
        }

        if (!hasReadContactPermission()) {
            permissionList.add(Manifest.permission.READ_CONTACTS);
        }

        if (!hasWriteContactPermission()) {
            permissionList.add(Manifest.permission.WRITE_CONTACTS);
        }

        if (!hasCallPermission()) {
            permissionList.add(Manifest.permission.CALL_PHONE);
        }

        if (!hasReadSmsPermission()) {
            permissionList.add(Manifest.permission.READ_SMS);
        }

        if (!hasSendSmsPermission()) {
            permissionList.add(Manifest.permission.SEND_SMS);
        }

        if (!hasReceiveSmsPermission()) {
            permissionList.add(Manifest.permission.RECEIVE_SMS);
        }

        if (permissionList.isEmpty()) {
            return null;
        }

        return permissionList.toArray(new String[permissionList.size()]);
    }
}
