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

    private Context context;
    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
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
        if (null == convertView) {
            if (1 == message.getType()) {
                convertView = LayoutInflater.from(context).inflate(R.layout.message_from_item_layout, null);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.message_to_item_layout, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.contentText = (TextView) convertView.findViewById(R.id.content_text);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.time_text);
            if (message.getType() == 5) {
                viewHolder.sendFailText = (TextView) convertView.findViewById(R.id.send_fail_text);
            } else {
                viewHolder.sendFailText = null;
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.contentText.setText(message.getBody());
        viewHolder.timeText.setText(message.getDate());
        if (message.getType() == 5) {
            viewHolder.sendFailText.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        public TextView contentText;
        public TextView timeText;
        public TextView sendFailText;
    }
}
