package com.tayloryan.securecontacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.widget.SectionedBaseAdapter;
import com.tayloryan.securecontacts.widget.view.ListViewGroup;

import java.util.List;

/**
 * Created by taylor.yan on 1/20/17.
 */

public class GenericPinnedListAdapter extends SectionedBaseAdapter {

    protected List<ListViewGroup<?>> mGroups;
    protected Context mContext;

    public GenericPinnedListAdapter(Context context, List<ListViewGroup<?>> groups) {
        mContext = context;
        mGroups = groups;
    }

    @Override
    public Object getItem(int section, int position) {
        return mGroups.get(section).getItem(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return mGroups.size();
    }

    @Override
    public int getCountForSection(int section) {
        return mGroups.get(section).getItemCount();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        return mGroups.get(section).getItem(position).convertView(convertView, mContext);
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        ListViewGroup<?> group = mGroups.get(section);
        if (!group.isHeaderVisible()) {
            return new View(mContext);
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_group_header, null);
            TextView indexText = (TextView) convertView.findViewById(R.id.list_group_header_text);
            indexText.setText(group.getTitle());
        }
        return convertView;
    }
}
