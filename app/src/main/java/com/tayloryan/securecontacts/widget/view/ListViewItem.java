package com.tayloryan.securecontacts.widget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class ListViewItem<T> {

    private IListViewItemConverter<T> mConverter;
    private T mData;
    private boolean mSelected;

    public ListViewItem(T data, IListViewItemConverter converter) {
        this.mData = data;
        this.mConverter = converter;
    }

    public View convertView(View convertView, Context context) {
        int viewRes = mConverter.getViewRes();
        if (convertView == null || (Integer)convertView.getTag() != viewRes) {
            convertView = LayoutInflater.from(context).inflate(viewRes, null);
            convertView.setTag(viewRes);
        }

        mConverter.convert(mData, convertView, context, mSelected);
        return convertView;
    }
}
