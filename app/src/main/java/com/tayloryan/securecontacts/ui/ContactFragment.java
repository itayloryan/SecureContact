package com.tayloryan.securecontacts.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.adapter.GenericPinnedListAdapter;
import com.tayloryan.securecontacts.widget.PinnedHeaderListView;
import com.tayloryan.securecontacts.widget.converter.ContactItemConverter;
import com.tayloryan.securecontacts.widget.view.ListViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Phone.*;

/**
 * Created by taylor.yan on 1/20/17.
 */

@EFragment(R.layout.fragment_contacts)
public class ContactFragment extends Fragment {

    @ViewById(R.id.contact_search_view)
    protected SearchView mSearchView;
    @ViewById(R.id.contacts_list)
    protected PinnedHeaderListView mListView;

    private List<ListViewGroup<Contact>> mGroups;
    private GenericPinnedListAdapter mListAdapter;
    private ContactItemConverter mContactConverter = new ContactItemConverter();

    public static Fragment create() {
        return new ContactFragment_();
    }

    @AfterViews
    protected void afterViews() {
        initSearchView();
        readContacts();
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

    @Background
    protected void readContacts() {
//        Cursor mContactCursor = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.SORT_KEY_PRIMARY);
//        String phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
//
//        for (mContactCursor.moveToFirst(); !mContactCursor.isAfterLast(); mContactCursor.moveToNext()) {
//            Contact contact = new Contact();
//            int id = mContactCursor.getInt(mContactCursor.getColumnIndex(ContactsContract.Contacts._ID));
//            String phoneSelectionArgs [] = {String.valueOf(id)};
//            contact.setName(mContactCursor.getString(mContactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
//            contact.setNamePinYin(PinyinHelper.toHanyuPinyinStringArray(contact.getFirstTextOfName()));
//            contact.setPhoto_uri(mContactCursor.getString(mContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)));
//
//            Cursor mPhoneCursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, phoneSelection, phoneSelectionArgs, null);
//
//            for (mPhoneCursor.moveToFirst(); !mPhoneCursor.isAfterLast(); mPhoneCursor.moveToNext()) {
//                int phoneType = mPhoneCursor.getInt(mPhoneCursor.getColumnIndex(TYPE));
//                String number =  mPhoneCursor.getString(mPhoneCursor.getColumnIndex(NUMBER));
//                switch (phoneType) {
//                    case TYPE_HOME: contact.setPhoneNumber_home(number); break;
//                    case TYPE_MOBILE: contact.setPhoneNumber_mobile(number); break;
//                    case TYPE_WORK: contact.setPhoneNumber_work(number); break;
//                    case TYPE_FAX_WORK: contact.setPhoneNumber_fax_work(number); break;
//                    case TYPE_FAX_HOME: contact.setPhoneNumber_fax_home(number); break;
//                    case TYPE_OTHER: contact.setPhoneNumber_other(number); break;
//                    default: contact.setPhoneNumber_other(number); break;
//                }
//            }
//
//            ListViewItem contactItem = new ListViewItem(contact, mContactConverter);
//            group.addItem(callLogItem);
//        }
        List<Contact> contacts = Contacts.getQuery().find();
    }

}
