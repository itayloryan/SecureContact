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
import com.tayloryan.securecontacts.widget.AvatarView;

import java.util.List;


public class ConversationAdapter extends BaseAdapter {

    private Context context;
    private List<Conversation> conversations;

    public ConversationAdapter(Context context, List<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public Object getItem(int position) {
        return conversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.avatarView = (AvatarView) convertView.findViewById(R.id.contact_avatar);
            viewHolder.receiverText = (TextView) convertView.findViewById(R.id.receiver_text);
            viewHolder.contentText = (TextView) convertView.findViewById(R.id.content_text);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.time_text);
            viewHolder.unreadText = (TextView) convertView.findViewById(R.id.unread_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Conversation conversation = conversations.get(position);
        viewHolder.receiverText.setText(conversation.getReceiver());
        viewHolder.contentText.setText(conversation.getSnippet());
        viewHolder.timeText.setText(conversation.getDate());
        if (conversation.getUnreadCount() > 0) {
            viewHolder.unreadText.setVisibility(View.VISIBLE);
            viewHolder.unreadText.setText(conversation.getUnreadCount()  + "");
        } else {
            viewHolder.unreadText.setVisibility(View.GONE);
        }

        if (conversation.getPhotoUri()!= null) {
            viewHolder.avatarView.setImageURI(Uri.parse(conversation.getPhotoUri()));
        } else {
            viewHolder.avatarView.setImageResource(R.drawable.ic_avatar);
        }

        return convertView;
    }

    static class ViewHolder {
        public AvatarView avatarView;
        public TextView receiverText;
        public TextView contentText;
        public TextView timeText;
        public TextView unreadText;
    }
}
