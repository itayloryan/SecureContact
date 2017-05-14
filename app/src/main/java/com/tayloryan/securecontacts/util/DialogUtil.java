package com.tayloryan.securecontacts.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.widget.EncryptDialog;

/**
 * Created by 煌 on 2017/4/22.
 */

public class DialogUtil {

    public static void dismiss(Dialog... dialogs) {
        for (Dialog dialog : dialogs) {
            if (null != dialog) {
                dialog.dismiss();
            }
        }
    }

    public static AlertDialog createEditDialog(Context context, String title, String[] items,
                                               DialogInterface.OnClickListener onItemClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, onItemClickListener);
        return builder.create();
    }

    public static EncryptDialog createEncryptDialog(final Context context, String title, View.OnClickListener onPositiveClickListener) {
        final EncryptDialog dialog = new EncryptDialog(context);
        dialog.setTitle(title);
        dialog.setPositiveClickListener(onPositiveClickListener);
        dialog.setNagetiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                KeyboardUtil.hideSoftKeyBoard((Activity) context);
            }
        });
        return dialog;
    }

    public static AlertDialog createTipDialog(Context context, String title, String message,
                                              DialogInterface.OnClickListener positiviListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok_text, positiviListener);
        builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
