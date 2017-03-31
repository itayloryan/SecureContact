package com.tayloryan.securecontacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;

import java.util.List;


public class MenuAdapter extends BaseAdapter {

    private final Context mContext;
    private List<String> mMenuItems;
    private int mMaxItemWidth;

    public MenuAdapter(Context context, List<String> items) {
        mContext = context;
        mMenuItems = items;
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_item_layout, null);
            viewHolder = new ViewHolder((TextView) convertView.findViewById(R.id.menu_item_text));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextView.setText(mMenuItems.get(position));
        return convertView;
    }

    public int getContentWidth() {
        if (mMaxItemWidth != 0) return mMaxItemWidth;

        for (int i = 0; i < getCount(); i++) {
            View contentView = getView(i, null, null);
            contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            int itemWidth = contentView.getMeasuredWidth();
            if (itemWidth > mMaxItemWidth) {
                mMaxItemWidth = itemWidth;
            }
        }

        return mMaxItemWidth;
    }

    static class ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView textView) {
            mTextView = textView;
        }
    }


}
