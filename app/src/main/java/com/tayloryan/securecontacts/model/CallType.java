package com.tayloryan.securecontacts.model;

import android.provider.CallLog;

/**
 * Created by taylor.yan on 1/18/17.
 */

public enum CallType {

    CALL_OUT("呼出", CallLog.Calls.OUTGOING_TYPE),
    CALL_IN("呼入", CallLog.Calls.INCOMING_TYPE),
    CALL_MISSED("未接", CallLog.Calls.MISSED_TYPE),
    CALL_REJECT("拒接", CallLog.Calls.REJECTED_TYPE);

    private final String name;
    private final int type;


    CallType(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String  getCallTypeName (int type) {
        for (CallType callType : CallType.values()) {
            if (callType.type == type) {
                return callType.name;
            }
        }

        return CALL_REJECT.name;
    }
}
