package com.tayloryan.securecontacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.widget.view.ListViewGroup;

import java.util.List;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class GenericExpandableListAdapter extends BaseExpandableListAdapter {

    protected List<ListViewGroup<?>> mGroups;
    protected Context mContext;

    public GenericExpandableListAdapter(Context context, List<ListViewGroup<?>> groups) {
        mContext = context;
        mGroups = groups;
    }

    public void setGroups(List<ListViewGroup<?>> groups) {
        this.mGroups = groups;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    public List<ListViewGroup<?>> getGroups() {
        return mGroups;
    }

    @Override
    public int getChildrenCount(int position) {
        return mGroups.get(position).getItemCount();
    }

    @Override
    public Object getGroup(int position) {
        return mGroups.get(position);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).getItem(childPosition);
    }

    @Override
    public long getGroupId(int position) {
        return position;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean isExpand, View convertView, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder = null;
        ListViewGroup<?> group = mGroups.get(position);
        if (!group.isHeaderVisible()) {
            return new View(mContext);
        }

        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_group_header, null);
            groupViewHolder.mTextView = (TextView) convertView.findViewById(R.id.list_group_header_text);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.mTextView.setText(group.getTitle());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        return mGroups.get(groupPosition).getItem(childPosition).convertView(convertView, mContext);
    }

    @Override
    public boolean isChildSelectable(int position, int i) {
        return true;
    }

    static class GroupViewHolder {

            public TextView mTextView;
    }
}
