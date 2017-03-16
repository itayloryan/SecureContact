package com.tayloryan.securecontacts.widget;


import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.widget.view.ListViewGroup;

public class ExpandedPinnedHeaderListView extends PinnedHeaderExpandableListView implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {

    public ExpandedPinnedHeaderListView(Context context) {
        super(context);
    }

    public ExpandedPinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandedPinnedHeaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void expandGroup() {
        for (int i = 0; i < getExpandableListAdapter().getGroupCount(); i++) {
            expandGroup(i);
        }
        setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        }, false);
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                expandGroup();
            }
        });
    }

    @Override
    public View getPinnedHeader() {
        View headerView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_group_header, null);
        headerView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        TextView textView = (TextView) headerView.findViewById(R.id.list_group_header_text);
        ExpandableListAdapter adapter = getExpandableListAdapter();
        if (null != adapter) {
            ListViewGroup<?> listViewGroup = (ListViewGroup<?>) adapter.getGroup(firstVisibleGroupPos);
            if (null == headerView || !listViewGroup.isHeaderVisible()) {
                headerView.setVisibility(GONE);
            } else {
                textView.setText(listViewGroup.getTitle());
            }
        } else {
            headerView.setVisibility(GONE);
        }
    }
}
