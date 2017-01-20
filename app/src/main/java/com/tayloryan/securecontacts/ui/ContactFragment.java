package com.tayloryan.securecontacts.ui;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.widget.PinnedHeaderListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by taylor.yan on 1/20/17.
 */

@EFragment(R.layout.fragment_contacts)
public class ContactFragment extends Fragment {

    @ViewById(R.id.contact_search_view)
    protected SearchView mSearchView;
    @ViewById(R.id.contacts_list)
    protected PinnedHeaderListView mListView;

    public static Fragment create() {
        return new ContactFragment_();
    }

    @AfterViews
    protected void afterViews() {
        initSearchView();
    }

    private void initSearchView() {
        mSearchView.setQueryHint("搜索联系人");
        mSearchView.setIconifiedByDefault(false);
        mSearchView.findViewById(R.id.search_src_text).clearFocus();
        mSearchView.clearFocus();

        // Adjusting the color of text in search widget
//        int searchPlateId = mSearchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
//        View searchPlate = mSearchView.findViewById(searchPlateId);
//
//        if (searchPlate != null) {
//            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
//            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
//            if (searchText != null) {
//                searchText.clearFocus();
//                searchText.setCursorVisible(false);
//            }
//        }
    }
}
