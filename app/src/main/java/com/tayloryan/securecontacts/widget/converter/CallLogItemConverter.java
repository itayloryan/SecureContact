package com.tayloryan.securecontacts.widget.converter;

import android.content.Context;
import android.graphics.Color;
import android.provider.CallLog;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.model.CallType;
import com.tayloryan.securecontacts.model.ScCallsLog;
import com.tayloryan.securecontacts.widget.AvatarView;
import com.tayloryan.securecontacts.widget.view.IListViewItemConverter;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class CallLogItemConverter implements IListViewItemConverter<ScCallsLog> {

    @Override
    public int getViewRes() {
        return R.layout.call_log_list_item;
    }

    @Override
    public void convert(ScCallsLog scCallsLog, View convertView, Context context, boolean selected) {
        AvatarView avatarView = (AvatarView) convertView.findViewById(R.id.contact_avatar);
        TextView callTitle = (TextView) convertView.findViewById(R.id.contact_title);
        TextView callType = (TextView) convertView.findViewById(R.id.call_type_text);
        if (CallLog.Calls.INCOMING_TYPE != scCallsLog.getCallType() && CallLog.Calls.OUTGOING_TYPE != scCallsLog.getCallType()) {
            callType.setTextColor(Color.parseColor("#FF0000"));
        } else {
            callType.setTextColor(Color.parseColor("#000000"));
        }
        TextView callTime = (TextView) convertView.findViewById(R.id.call_time_text);
        TextView callNumber = (TextView) convertView.findViewById(R.id.phone_number_text);

        if (null != scCallsLog.getCallerName()) {
            callTitle.setText(scCallsLog.getCallerName());
            callNumber.setText(scCallsLog.getCallNumber());
        } else {
            callTitle.setText(scCallsLog.getCallNumber());
            callNumber.setText(scCallsLog.getCallNumber());
        }

        avatarView.setImageURI(scCallsLog.getAvatarUri());
        avatarView.setFirstTextString(callTitle.getText().toString().substring(0,1));
        callType.setText(CallType.getCallTypeName(scCallsLog.getCallType()));
        callTime.setText(scCallsLog.getCallTime());
    }
}
