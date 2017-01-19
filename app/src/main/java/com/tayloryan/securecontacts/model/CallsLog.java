package com.tayloryan.securecontacts.model;

import android.net.Uri;
import android.provider.CallLog;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class CallsLog {

    private String callerName;
    private String callNumber;
    private String callTime;
    private int callType;
    private String callDuration;
    private String avatarUri;

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallTime() {
        if (null != callTime) {
            Long timeNow = System.currentTimeMillis();
            Date callDate = new Date(Long.parseLong(callTime));
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(callDate);
            int duration = (int)(timeNow - callDate.getTime())/1000/60/60/24;
            if (duration >= 1 ) {
                return new SimpleDateFormat("MM月dd日").format(callDate);
            } else {
                return new SimpleDateFormat("HH:mm").format(callDate);
            }

        }

        return "";
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getCallType() {
        String callTypeStr = "";
        switch (callType) {
            case CallLog.Calls.INCOMING_TYPE:
                callTypeStr =  "呼入";
            break;
            case CallLog.Calls.OUTGOING_TYPE:
                callTypeStr =  "呼出";
            break;
            case CallLog.Calls.MISSED_TYPE:
                callTypeStr =  "未接";
            break;
            case CallLog.Calls.REJECTED_TYPE:
                callTypeStr =  "拒接";
            break;
        }
        return callTypeStr;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public Uri getAvatarUri() {
        return TextUtils.isEmpty(avatarUri)? null : Uri.parse(avatarUri);
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}
