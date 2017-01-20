package com.tayloryan.securecontacts.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.tayloryan.securecontacts.widget.SectionedBaseAdapter;

/**
 * Created by taylor.yan on 1/20/17.
 */

public class GenericPinnedListAdapter extends SectionedBaseAdapter {

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return 0;
    }

    @Override
    public int getCountForSection(int section) {
        return 0;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        return null;
    }
}
