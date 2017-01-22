package com.tayloryan.securecontacts.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Util for manually control the soft keyboard. Provide common functions:</br>
 * <ul>
 * <li>1. hide(hideSoftKeyBoard(Activity activityContext)) soft keyboard. </br>
 * <li>2. show(showSoftKeyBoard(Activity activityContext)) soft keyboard.</br>
 * </ul>
 */
public class KeyboardUtil {

    /**
     * The function to hide soft keyboard
     * 
     * @param activityContext activity mContext to control the keyboard.
     */
    public static void hideSoftKeyBoard(Activity activityContext) {
        InputMethodManager imm = (InputMethodManager) activityContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activityContext.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activityContext.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * The function to hide soft keyboard
     *
     */
    public static void hideSoftKeyBoard(EditText textField, Activity activityContext) {
        InputMethodManager imm = (InputMethodManager) activityContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (textField != null) {
            imm.hideSoftInputFromWindow(textField.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * The function to show soft keyboard
     * 
     * @param activityContext activity mContext to control the keyboard.
     */
    public static void showSoftKeyBoard(Activity activityContext) {
        InputMethodManager imm = (InputMethodManager) activityContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activityContext.getCurrentFocus() != null) {
            imm.showSoftInputFromInputMethod(activityContext.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void showSoftKeyBoard(EditText textField, Activity activityContext) {
        InputMethodManager imm = (InputMethodManager) activityContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activityContext.getCurrentFocus() != null) {
            imm.showSoftInput(textField, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void showSoftKeyBoardForced(Activity activityContext) {
        InputMethodManager imm = (InputMethodManager) activityContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,1);
    }

}
