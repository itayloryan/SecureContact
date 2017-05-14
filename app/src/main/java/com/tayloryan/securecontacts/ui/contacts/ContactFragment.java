package com.tayloryan.securecontacts.ui.contacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.adapter.ContactSearchAdapter;
import com.tayloryan.securecontacts.adapter.GenericExpandableListAdapter;
import com.tayloryan.securecontacts.event.ReloadContactsEvent;
import com.tayloryan.securecontacts.model.Address;
import com.tayloryan.securecontacts.model.Email;
import com.tayloryan.securecontacts.model.PhoneNumber;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.service.ContactService;
import com.tayloryan.securecontacts.util.DialogUtil;
import com.tayloryan.securecontacts.util.KeyboardUtil;
import com.tayloryan.securecontacts.util.MD5Util;
import com.tayloryan.securecontacts.widget.EncryptDialog;
import com.tayloryan.securecontacts.widget.ExpandedPinnedHeaderListView;
import com.tayloryan.securecontacts.widget.SlideBar;
import com.tayloryan.securecontacts.widget.converter.ContactItemConverter;
import com.tayloryan.securecontacts.widget.view.ListViewGroup;
import com.tayloryan.securecontacts.widget.view.ListViewItem;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@EFragment(R.layout.fragment_contacts)
public class ContactFragment extends Fragment {

    @Bean
    protected ContactService mContactService;

    @ViewById(R.id.contact_search_view)
    protected SearchView mSearchView;
    @ViewById(R.id.contacts_list)
    protected ExpandedPinnedHeaderListView mContactListView;

    @ViewById(R.id.contact_slide_bar)
    protected SlideBar mContactSlideBar;

    @ViewById(R.id.contact_list_container)
    protected RelativeLayout mContactListContainer;

    @ViewById(R.id.contact_search_list)
    protected ListView contactSearchList;

    @ViewById(R.id.no_contact_message)
    protected TextView noContactText;

    private TextView mOverLayout;

    private Map<String, Integer> mContactIndexer;

    private GenericExpandableListAdapter mContactListAdapter;

    private ContactItemConverter mContactItemConverter = new ContactItemConverter();

    private Handler mHandler = new Handler();

    private ContactSearchAdapter mContactSearchAdapter;

    private AlertDialog mEditDialog, mDeleteConfirmDialog;
    private EncryptDialog mEncryptDialog;
    private EncryptDialog mDecryptDialog;


    public static Fragment create() {
        return new ContactFragment_();
    }

    private void initSearchView() {
        mSearchView.setQueryHint("搜索联系人");
        mSearchView.setIconifiedByDefault(false);
        mSearchView.findViewById(R.id.search_src_text).clearFocus();
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(mOnQueryTextListener);

        SearchView.SearchAutoComplete textView = ( SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        textView.setTextColor(getContext().getResources().getColor(R.color.black3));
        textView.setCursorVisible(false);
        textView.clearFocus();
    }

    @AfterViews
    protected void afterViews() {
        readContacts();
        initSearchView();
        initOverlay();
        mContactListView.setOnScrollListener(mOnScrollListener);
        mContactListView.setOnChildClickListener(onChildClickListener);
        mContactListView.setOnItemLongClickListener(longClickListener);
        mContactSlideBar.setOnTouchingLetterChangedListener(mOnTouchingLetterChangedListener);
        contactSearchList.setOnItemClickListener(onItemClickListener);
        contactSearchList.setOnScrollListener(mOnScrollListener);
    }

    Comparator<ScContact> comparator=new Comparator<ScContact>() {
        @Override
        public int compare(ScContact t1, ScContact t2) {
            String a=t1.getPinOfName();
            String b=t2.getPinOfName();
            int flag=a.compareTo(b);
            if (flag==0){
                return a.compareTo(b);
            }else{
                return flag;
            }
        }
    };

    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (TextUtils.isEmpty(newText)) {
                mContactListContainer.setVisibility(View.VISIBLE);
                contactSearchList.setVisibility(View.GONE);
            } else {
                mContactListContainer.setVisibility(View.GONE);
                contactSearchList.setVisibility(View.VISIBLE);
                searchContacts(newText);
            }
            return false;
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final ScContact contact = (ScContact) mContactSearchAdapter.getItem(position);
            if (null == contact.getEncryption()) {
                startContactDetailActivity(contact);
            } else {
                String[] items = {"查看详细", "去除密码"};
                mEditDialog = DialogUtil.createEditDialog(getContext(), "请选择", items, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                View.OnClickListener positiveListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mDecryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (MD5Util.encrypt(p).equals(contact.getEncryption())) {
                                            startContactDetailActivity(contact);
                                            mDecryptDialog.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "密码错误，请重试。", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                };
                                mDecryptDialog = DialogUtil.createEncryptDialog(getContext(), "查看联系人信息", positiveListener);
                                mDecryptDialog.show();
                                break;

                            case 1:
                                View.OnClickListener decryptListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mDecryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (MD5Util.encrypt(p).equals(contact.getEncryption())) {
                                            encryptContact(contact, null);
                                            mDecryptDialog.dismiss();
                                            Toast.makeText(getContext(), "此联系人已解密", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "密码错误，请重试。", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                };
                                mDecryptDialog = DialogUtil.createEncryptDialog(getContext(), "解密联系人", decryptListener);
                                mDecryptDialog.show();
                                break;
                        }
                    }
                });

                mEditDialog.show();
            }
        }
    };

    @Background
    protected void searchContacts(String queryString) {
        List<ScContact> results = new ArrayList<>();
        for (int i = 0; i < mContactListAdapter.getGroupCount(); i++) {
            for (int j = 0; j < mContactListAdapter.getGroupChildCount(i); j++) {
                ScContact contact = new ScContact();
                ListViewItem<ScContact> item = (ListViewItem<ScContact>) mContactListAdapter.getChild(i, j);
                contact.cloneFrom(item.getData());
                if (contact.getName().contains(queryString) || contact.getPinHeaderOfName().contains(queryString)
                        || contact.getPinOfName().contains(queryString)) {
                    contact.setSearchMatcher(contact.getNameEncryption() == null ?
                            contact.getName() : contact.getName().replaceAll(".", "*"));
                    results.add(contact);
                    continue;
                }

                if (contact.getCompanyName() != null && contact.getCompanyName().contains(queryString)) {
                    contact.setSearchMatcher(contact.getCompanyEncryption() != null?
                            contact.getCompanyName().replaceAll(".", "*") : contact.getCompanyName());
                    results.add(contact);
                    continue;
                }
                if (null != contact.getJob() && contact.getJob().contains(queryString)) {
                    contact.setSearchMatcher(contact.getJobEncryption() == null ?
                            contact.getJob() : contact.getJob().replaceAll(".", "*"));
                    results.add(contact);
                    continue;
                }
                for (PhoneNumber phone : contact.getPhoneNumbers()) {
                    if (phone.getNumber().contains(queryString)) {
                        contact.setSearchMatcher(phone.getEncryption() == null ?
                                phone.getNumber() : phone.getNumber().replaceAll(".", "*"));
                        results.add(contact);
                        break;
                    }
                }
                if (null != contact.getSearchMatcher()) {
                    continue;
                }

                for (Email email : contact.getEmails()) {
                    if (email.getAddress().contains(queryString)) {
                        contact.setSearchMatcher(email.getEncryption() == null ?
                                email.getAddress() : email.getAddress().replaceAll(".", "*"));
                        results.add(contact);
                        break;
                    }
                }

                if (null != contact.getSearchMatcher()) {
                    continue;
                }

                for (Address address : contact.getAddresses()) {
                    if (address.getAddress().contains(queryString)) {
                        contact.setSearchMatcher(address.getEncryption() == null ?
                                address.getAddress() : address.getAddress().replaceAll(".", "*"));
                        results.add(contact);
                        break;
                    }
                }
            }
        }
        displaySearchResult(results);
    }

    @UiThread
    protected void displaySearchResult(List<ScContact> results) {
        mContactSearchAdapter = new ContactSearchAdapter(getContext(), results);
        contactSearchList.setAdapter(mContactSearchAdapter);
        mContactSearchAdapter.notifyDataSetChanged();
    }

    @Background
    protected void encryptContact(ScContact contact, String password) {
        mContactService.encryptContact(contact.getId(), password);
        contact.setEncryption(MD5Util.encrypt(password));
        onPostEncryptContact();
    }

    @UiThread
    protected void onPostEncryptContact() {
        mContactListAdapter.notifyDataSetChanged();
        if (null != mContactSearchAdapter) {
            mContactSearchAdapter.notifyDataSetChanged();
        }
    }

    @Background
    protected void deleteContact(ScContact contact, int groupPosition, int childPosition) {
        mContactService.deleteContact(contact.getId());
        onPostDeleteContact(groupPosition, childPosition);
    }

    @UiThread
    protected void onPostDeleteContact(int groupPosition, int childPosition) {
        ((ListViewGroup<?>) mContactListAdapter.getGroup(groupPosition)).removeItem(childPosition);
        if (((ListViewGroup<?>) mContactListAdapter.getGroup(groupPosition)).getItemCount() < 1) {
            mContactListAdapter.getGroups().remove(groupPosition);
        }
        mContactListAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "联系人已删除", Toast.LENGTH_SHORT).show();
    }

    private ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            ListViewItem<ScContact> item = (ListViewItem<ScContact>) mContactListAdapter.getChild(groupPosition, childPosition);
            final ScContact contact = item.getData();
            if (null == contact.getEncryption()) {
                startContactDetailActivity(contact);
                return false;
            } else {
                String[] items = {"查看详细", "去除密码"};
                mEditDialog = DialogUtil.createEditDialog(getContext(), "请选择", items, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                View.OnClickListener positiveListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mDecryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (MD5Util.encrypt(p).equals(contact.getEncryption())) {
                                            startContactDetailActivity(contact);
                                            mDecryptDialog.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "密码错误，请重试。", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                };
                                mDecryptDialog = DialogUtil.createEncryptDialog(getContext(), "查看联系人信息", positiveListener);
                                mDecryptDialog.show();
                                break;

                            case 1:
                                View.OnClickListener decryptListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mDecryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (MD5Util.encrypt(p).equals(contact.getEncryption())) {
                                            encryptContact(contact, null);
                                            mDecryptDialog.dismiss();
                                            Toast.makeText(getContext(), "此联系人已解密", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "密码错误，请重试。", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                };
                                mDecryptDialog = DialogUtil.createEncryptDialog(getContext(), "解密联系人", decryptListener);
                                mDecryptDialog.show();
                                break;
                        }
                    }
                });

                mEditDialog.show();
            }
            return false;
        }
    };

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (null == view.getTag(R.id.group_position) || null == view.getTag(R.id.child_position)) {
                return true;
            }
            final int groupPosition = (int) view.getTag(R.id.group_position);
            final int childPosition = (int) view.getTag(R.id.child_position);
            ListViewItem<ScContact> item = (ListViewItem<ScContact>) mContactListAdapter.getChild(groupPosition, childPosition);
            final ScContact contact = item.getData();
            if (contact.getEncryption() == null) {
                String[] items = {"编辑", "删除", "加密"};
                mEditDialog = DialogUtil.createEditDialog(getContext(), contact.getName(), items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(getActivity(), AddContactActivity_.class);
                                intent.putExtra("EXTRA_CONTACT", contact);
                                startActivity(intent);
                                break;
                            case 1:
                                DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteContact(contact, groupPosition, childPosition);
                                    }
                                };
                                mDeleteConfirmDialog = DialogUtil.createTipDialog(getContext(),
                                        contact.getName(), "确认删除此联系人?", deleteListener);
                                mDeleteConfirmDialog.show();
                                break;
                            case 2:
                                View.OnClickListener positiveListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String p = mEncryptDialog.getPasswordText();
                                        if (TextUtils.isEmpty(p)) {
                                            Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        encryptContact(contact, p);
                                        mEncryptDialog.dismiss();
                                        Toast.makeText(getContext(), "此联系人已加密", Toast.LENGTH_SHORT).show();
                                    }
                                };
                                mEncryptDialog = DialogUtil.createEncryptDialog(getContext(), "对联系人：" + contact.getName() + " 加密", positiveListener);
                                mEncryptDialog.show();
                                break;
                        }
                    }
                });
                mEditDialog.show();
            } else {
                mContactListView.performItemClick(view, position, id);
            }
            return true;
        }
    };


    private void startContactDetailActivity(ScContact contact) {
        Intent intent = new Intent(getActivity(), ContactDetailActivity_.class);
        intent.putExtra("EXTRA_CONTACT", contact);
        startActivity(intent);
    }

    @Background
    protected void readContacts() {
        List<ScContact> mContactBeanList = mContactService.readAllContacts();
        Collections.sort(mContactBeanList, comparator);
        displayContacts(mContactBeanList);
    }

    @UiThread
    protected void displayContacts(List<ScContact> contactList) {
        if (contactList.size() < 1) {
            noContactText.setVisibility(View.VISIBLE);
            mContactListContainer.setVisibility(View.GONE);
        } else {
            noContactText.setVisibility(View.GONE);
            mContactListContainer.setVisibility(View.VISIBLE);

            List<ListViewGroup<?>> mContactGroups = new ArrayList<>();
            mContactIndexer = new HashMap<>();
            ListViewGroup<ScContact> listViewGroup = null;
            int currentGroupPosition = 0;
            for (int i = 0; i < contactList.size(); i++) {
                String currentAlpha = contactList.get(i).getFirstLetterOfName();
                ListViewItem<ScContact> listViewItem = new ListViewItem<>(contactList.get(i), mContactItemConverter);

                String previewAlpha = (i - 1) >= 0 ? contactList.get(i - 1).getFirstLetterOfName() : "";
                if (!previewAlpha.equals(currentAlpha)) {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(ContactFragment.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(ContactFragment.this);
        DialogUtil.dismiss(mEditDialog, mEncryptDialog, mDecryptDialog, mDeleteConfirmDialog);
        WindowManager windowManager =
                (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeView(mOverLayout);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReloadContactsEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        readContacts();
    }

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
