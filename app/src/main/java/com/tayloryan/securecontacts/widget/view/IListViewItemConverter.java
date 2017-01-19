package com.tayloryan.securecontacts.widget.view;

import android.content.Context;
import android.view.View;

/**
 * Created by taylor.yan on 1/18/17.
 */

public interface IListViewItemConverter<T> {

    int getViewRes();
    void convet(T object, View convertView, final Context context, boolean selected);
}
