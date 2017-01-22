package com.tayloryan.securecontacts.widget.converter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.model.Contact;
import com.tayloryan.securecontacts.widget.AvatarView;
import com.tayloryan.securecontacts.widget.view.IListViewItemConverter;

/**
 * Created by taylor.yan on 1/21/17.
 */

public class ContactItemConverter implements IListViewItemConverter<Contact> {
    @Override
    public int getViewRes() {
        return R.layout.contact_list_item;
    }

    @Override
    public void convet(Contact contact, View convertView, Context context, boolean selected) {
        AvatarView avatarView = (AvatarView) convertView.findViewById(R.id.contact_avatar);
        TextView contactTitle = (TextView) convertView.findViewById(R.id.contact_title);

        if (contact.isHasAvatar()) {
            if (null != contact.getPhoto_uri()) {
                avatarView.setImageURI(Uri.parse(contact.getPhoto_uri()));
            } else {
                //TODO handle avatar with id
                avatarView.setImageResource(-1);
            }
        } else {
            avatarView.setFirstTextString(contact.getFirstTextOfName());
        }

        contactTitle.setText(contact.getName());
    }
}
