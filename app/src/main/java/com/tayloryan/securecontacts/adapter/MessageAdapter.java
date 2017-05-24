package com.tayloryan.securecontacts.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.model.Conversation;
import com.tayloryan.securecontacts.model.Message;
import com.tayloryan.securecontacts.widget.AvatarView;

import java.util.List;


public class MessageAdapter extends BaseAdapter {

    public static final int MESSAGE_TYPE_FROM = 1;
    public static final int MESSAGE_SEND_FAIL = 5;

    private Context context;
    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        Message message = messages.get(position);
        TextView sendFailText = null;
        if (null == convertView) {
            if (MESSAGE_TYPE_FROM == message.getType()) {
                convertView = LayoutInflater.from(context).inflate(R.layout.message_from_item_layout, null);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.message_to_item_layout, null);
                sendFailText = (TextView) convertView.findViewById(R.id.send_fail_text);
            }
            viewHolder = new ViewHolder();
            viewHolder.contentText = (TextView) convertView.findViewById(R.id.content_text);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.time_text);
            viewHolder.sendFailText = sendFailText;
            if (message.getType() == MESSAGE_SEND_FAIL) {
                viewHolder.sendFailText.setVisibility(View.VISIBLE);
            } else if (null != viewHolder.sendFailText) {
                viewHolder.sendFailText.setVisibility(View.GONE);

            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.contentText.setText(message.getBody());
        viewHolder.timeText.setText(message.getDate());

        return convertView;
    }

    static class ViewHolder {
        public TextView contentText;
        public TextView timeText;
        public TextView sendFailText;
    }
}
