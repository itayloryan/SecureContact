package com.tayloryan.securecontacts.ui.contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.adapter.GenericExpandableListAdapter;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.util.KeyboardUtil;
import com.tayloryan.securecontacts.widget.ExpandedPinnedHeaderListView;
import com.tayloryan.securecontacts.widget.SlideBar;
import com.tayloryan.securecontacts.widget.converter.ContactItemConverter;
import com.tayloryan.securecontacts.widget.view.ListViewGroup;
import com.tayloryan.securecontacts.widget.view.ListViewItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by taylor.yan on 3/8/17.
 */

@EFragment(R.layout.fragment_contacts)
public class ContactFragment extends Fragment {

    @ViewById(R.id.contact_search_view)
    protected SearchView mSearchView;
    @ViewById(R.id.contacts_list)
    protected ExpandedPinnedHeaderListView mContactListView;

    @ViewById(R.id.contact_slide_bar)
    protected SlideBar mContactSlideBar;

    private TextView mOverLayout;

    private Map<String, Integer> mContactIndexer;

    private GenericExpandableListAdapter mContactListAdapter;

    private List<ListViewGroup<?>> mContactGroups = new ArrayList<>();

    private ContactItemConverter mContactItemConverter = new ContactItemConverter();

    private Handler mHandler = new Handler();

    public static Fragment create() {
        return new ContactFragment_();
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

    @AfterViews
    protected void afterViews() {
        readContacts();
        initSearchView();
        initOverlay();
        mContactListView.setOnScrollListener(mOnScrollListener);
        mContactSlideBar.setOnTouchingLetterChangedListener(mOnTouchingLetterChangedListener);
    }

    Comparator<ScContact> comparator=new Comparator<ScContact>() {
        @Override
        public int compare(ScContact t1, ScContact t2) {
            String a=t1.getFirstLetterOfName();
            String b=t2.getFirstLetterOfName();
            int flag=a.compareTo(b);
            if (flag==0){
                return a.compareTo(b);
            }else{
                return flag;
            }
        }
    };

    @Background
    protected void readContacts() {
        List<ScContact> mContactBeanList=new ArrayList<>();
        ScContact mContact;
        ContentResolver mContentResolver=getContext().getContentResolver();
        Uri uri= Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri=Uri.parse("content://com.android.contacts/data");

        Cursor cursor =mContentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        while (cursor.moveToNext()){
            mContact=new ScContact();
            String id=cursor.getString(cursor.getColumnIndex("_id"));
            String title=cursor.getString(cursor.getColumnIndex("display_name"));//获取联系人姓名
            String firstHeadLetter=cursor.getString(cursor.getColumnIndex("phonebook_label"));//这个字段保存了每个联系人首字的拼音的首字母
            mContact.setName(title);
            mContact.setFirstLetterOfName(firstHeadLetter);


            Cursor dataCursor=mContentResolver.query(dataUri,null,"raw_contact_id= ?",new String[]{id},null);
            while(dataCursor.moveToNext()){
                String type=dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
                if (type.equals("vnd.android.cursor.item/phone_v2")){//如果得到的mimeType类型为手机号码类型才去接收
                    String phoneNum=dataCursor.getString(dataCursor.getColumnIndex("data1"));//获取手机号码
                    mContact.setPhoneNumberMobile(phoneNum);
                    mContact.setPhotoUri(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)));
                }
            }
            dataCursor.close();
            if (mContact.getName()!=null&&mContact.getPhoneNumberMobile()!=null){
                mContactBeanList.add(mContact);
            }

        }
        cursor.close();
        Collections.sort(mContactBeanList, comparator);
        displayContacts(mContactBeanList);
    }

    @UiThread
    protected void displayContacts(List<ScContact> contactList) {
        mContactIndexer = new HashMap<>();
        ListViewGroup<ScContact> listViewGroup = null;
        int currentGroupPosition = 0;
        for (int i = 0; i < contactList.size(); i++) {
            String currentAlpha = contactList.get(i).getFirstLetterOfName();
            ListViewItem<ScContact> listViewItem = new ListViewItem<>(contactList.get(i), mContactItemConverter);

            String previewAlpha = (i - 1) >= 0 ? contactList.get(i - 1).getFirstLetterOfName() : "";
            if (!previewAlpha.equals(currentAlpha)){
                String firstAlpha = contactList.get(i).getFirstLetterOfName();
                listViewGroup = new ListViewGroup<>(firstAlpha);
                listViewGroup.addItem(listViewItem);
                mContactGroups.add(listViewGroup);
                mContactIndexer.put(firstAlpha, currentGroupPosition);
                currentGroupPosition++;
            } else {
                listViewGroup.addItem(listViewItem);
            }
        }
        mContactListAdapter = new GenericExpandableListAdapter(getContext(), mContactGroups);
        mContactListView.setAdapter(mContactListAdapter);
        mContactListAdapter.notifyDataSetChanged();
    }

    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mOverLayout = (TextView) inflater.inflate(R.layout.pop_up_indexer, null);
        mOverLayout.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager =
                (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(mOverLayout, lp);
    }

    private SlideBar.OnTouchingLetterChangedListener mOnTouchingLetterChangedListener = new SlideBar.OnTouchingLetterChangedListener() {
        @Override
        public void onTouchingLetterChanged(String s) {
            if (mContactIndexer.get(s) != null) {//判断当前选中的字母是否存在集合中
                int position = mContactIndexer.get(s);//如果存在集合中则取出集合中该字母对应所在的位置,再利用对应的setSelection，就可以实现点击选中相应字母，然后联系人就会定位到相应的位置
                mContactListView.setSelectedGroup(position);
                mOverLayout.setText(s);
                mOverLayout.setVisibility(View.VISIBLE);
                mHandler.removeCallbacks(mHideOverLayoutRunnable);
                // 延迟一秒后执行，让overlay为不可见
                mHandler.postDelayed(mHideOverLayoutRunnable, 1000);
            }
        }
    };

    private Runnable mHideOverLayoutRunnable = new Runnable() {
        @Override
        public void run() {
            mOverLayout.setVisibility(View.GONE);
        }
    };

    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            KeyboardUtil.hideSoftKeyBoard(getActivity());
            mSearchView.clearFocus();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };
}
