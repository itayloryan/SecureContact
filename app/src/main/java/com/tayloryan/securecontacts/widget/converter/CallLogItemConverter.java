package com.tayloryan.securecontacts.widget.converter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.model.CallsLog;
import com.tayloryan.securecontacts.widget.AvatarView;
import com.tayloryan.securecontacts.widget.view.IListViewItemConverter;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class CallLogItemConverter implements IListViewItemConverter<CallsLog> {

    @Override
    public int getViewRes() {
        return R.layout.call_log_list_item;
    }

    @Override
    public void convet(CallsLog callsLog, View convertView, Context context, boolean selected) {
        AvatarView avatarView = (AvatarView) convertView.findViewById(R.id.contact_avatar);
        TextView callTitle = (TextView) convertView.findViewById(R.id.contact_title);
        TextView callType = (TextView) convertView.findViewById(R.id.call_type_text);
        TextView callTime = (TextView) convertView.findViewById(R.id.call_time_text);
        TextView callNumber = (TextView) convertView.findViewById(R.id.phone_number_text);

        if (null != callsLog.getCallerName()) {
            callTitle.setText(callsLog.getCallerName());
            callNumber.setText(callsLog.getCallNumber());
        } else {
            callTitle.setText(callsLog.getCallNumber());
            callNumber.setText(callsLog.getCallNumber());
        }

        avatarView.setImageURI(callsLog.getAvatarUri());
        avatarView.setFirstTextString(callTitle.getText().toString().substring(0,1));
        callType.setText(callsLog.getCallType());
        callTime.setText(callsLog.getCallTime());
    }
}
