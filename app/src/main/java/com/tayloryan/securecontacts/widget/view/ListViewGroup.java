package com.tayloryan.securecontacts.widget.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class ListViewGroup<T> {

    private String title;
    private boolean headerVisible;
    private List<ListViewItem<T>> items;

    public ListViewGroup(String title) {
        this.title = title;
        headerVisible = true;
        items = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getItemCount() {
        return items.size();
    }

    public void addItem(ListViewItem<T> item) {
        items.add(item);
    }

    public ListViewItem<T> getItem(int position) {
        return items.get(position);
    }

    public boolean isHeaderVisible() {
        return headerVisible;
    }

    public void setHeaderVisible(boolean headerVisible) {
        this.headerVisible = headerVisible;
    }
}
