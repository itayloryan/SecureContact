package com.tayloryan.securecontacts.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.model.ScContact;

import java.util.List;

public class ContactSearchAdapter extends BaseAdapter {

    private List<ScContact> contacts;
    private Context context;

    public ContactSearchAdapter(Context context, List<ScContact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_search_item, null);
            TextView contactNameText = (TextView) convertView.findViewById(R.id.contact_name_text);
            TextView searchMatcherText = (TextView) convertView.findViewById(R.id.search_matcher_text);
            viewHolder = new ViewHolder(contactNameText, searchMatcherText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ScContact contact = (ScContact) getItem(position);
        if (contact.getNameEncryption() != null || contact.getEncryption() != null) {
            viewHolder.contactNameText.setText(contact.getName().replaceAll(".", "*"));
        } else {
            viewHolder.contactNameText.setText(contact.getName());
        }

        if (contact.getSearchMatcher().equals(contact.getName())) {
            viewHolder.contactSearchMatcherText.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.contactSearchMatcherText.setVisibility(View.VISIBLE);
            viewHolder.contactSearchMatcherText.setText(contact.getSearchMatcher());
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView contactNameText;
        public TextView contactSearchMatcherText;

        public ViewHolder(TextView contactNameText, TextView contactSearchMatcherText) {
            this.contactNameText = contactNameText;
            this.contactSearchMatcherText = contactSearchMatcherText;
        }
    }
}
