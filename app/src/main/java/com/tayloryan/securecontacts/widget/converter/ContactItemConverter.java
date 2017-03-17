package com.tayloryan.securecontacts.widget.converter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.widget.AvatarView;
import com.tayloryan.securecontacts.widget.view.IListViewItemConverter;

/**
 * Created by taylor.yan on 1/21/17.
 */

public class ContactItemConverter implements IListViewItemConverter<ScContact> {
    @Override
    public int getViewRes() {
        return R.layout.contact_list_item;
    }

    @Override
    public void convert(ScContact scContact, View convertView, Context context, boolean selected) {
        AvatarView avatarView = (AvatarView) convertView.findViewById(R.id.contact_avatar);
        TextView contactTitle = (TextView) convertView.findViewById(R.id.contact_title);

        if (scContact.isHasAvatar()) {
            if (null != scContact.getPhotoUri()) {
                avatarView.setImageURI(scContact.getPhotoUri());
            } else {
                //TODO handle avatar with id
                avatarView.setImageResource(-1);
            }
        } else {
            avatarView.setFirstTextString(scContact.getFirstTextOfName());
        }

        contactTitle.setText(scContact.getName());
    }
}
