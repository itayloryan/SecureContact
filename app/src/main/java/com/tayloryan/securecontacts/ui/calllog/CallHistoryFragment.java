package com.tayloryan.securecontacts.ui.calllog;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.adapter.GenericExpandableListAdapter;
import com.tayloryan.securecontacts.event.HideNavigationBarEvent;
import com.tayloryan.securecontacts.event.ReadCallLogPermissionEvent;
import com.tayloryan.securecontacts.event.ShowNavigationBarEvent;
import com.tayloryan.securecontacts.model.ScCallsLog;
import com.tayloryan.securecontacts.util.PermissionUtil;
import com.tayloryan.securecontacts.widget.DialPad;
import com.tayloryan.securecontacts.widget.PinnedHeaderExpandableListView;
import com.tayloryan.securecontacts.widget.converter.CallLogItemConverter;
import com.tayloryan.securecontacts.widget.view.ListViewGroup;
import com.tayloryan.securecontacts.widget.view.ListViewItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.fragment_call_history)
public class CallHistoryFragment extends Fragment implements DialPad.HideDialPadCallBack, DialPad.DialButtonCallBack {

    @ViewById(R.id.call_log_list)
    protected PinnedHeaderExpandableListView mListView;
    @ViewById(R.id.show_dialpad_btn)
    protected ImageButton mShowDialPadButton;
    @ViewById(R.id.dial_pad)
    protected DialPad mDialPad;

    private GenericExpandableListAdapter mListAdapter;
    private CallLogItemConverter mCallLogItemConverter = new CallLogItemConverter();

    public static CallHistoryFragment create() {
        return new CallHistoryFragment_();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @AfterViews
    protected void afterViews() {
        if (PermissionUtil.hasReadCallLogPermission()) {
            readCallLogs();
        }
        mDialPad.setHideDialPadCallBack(this);
        mDialPad.setDialButtonCallBack(this);
        mListView.setOnChildClickListener(onChildClickListener);
        mListView.setOnScrollListener(onScrollListener);
    }

    @Click(R.id.show_dialpad_btn)
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_dialpad_btn:
                mShowDialPadButton.setVisibility(View.GONE);
                mDialPad.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Background
    protected void readCallLogs() {
        List<ScCallsLog> callsLogs = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<String> phoneNumbers = new ArrayList<>();
        String sortOrder = CallLog.Calls.DEFAULT_SORT_ORDER;
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, null, null, null, sortOrder);

        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            if (phoneNumbers.contains(number)) {
                continue;
            }
            ScCallsLog scCallsLog = new ScCallsLog();
            scCallsLog.setCallerName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            scCallsLog.setCallNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
            scCallsLog.setCallTime(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)));
            scCallsLog.setCallNumber(number);
            phoneNumbers.add(number);
            //scCallsLog.setAvatarUri(mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI)));
            scCallsLog.setCallType(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            callsLogs.add(scCallsLog);

        }
        cursor.close();
        onPostReadCallLogs(callsLogs);
    }

    @UiThread
    public void onPostReadCallLogs(List<ScCallsLog> callsLogs) {
        List<ListViewGroup<?>> groups = new ArrayList<>();

        if (callsLogs.isEmpty()) {
            return;
        }

        ListViewGroup<ScCallsLog> group = new ListViewGroup<>(null);
        group.setHeaderVisible(false);
        for (ScCallsLog callLog : callsLogs) {
            ListViewItem callLogItem = new ListViewItem(callLog, mCallLogItemConverter);
            group.addItem(callLogItem);

        }
        groups.add(group);
        mListAdapter = new GenericExpandableListAdapter(getContext(), groups);
        mListView.setAdapter(mListAdapter);
        mListAdapter.notifyDataSetChanged();
        for (int i = 0; i < mListAdapter.getGroupCount(); i ++ ) {
            mListView.expandGroup(i);
        }
    }

    private ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            ListViewItem<ScCallsLog> item = (ListViewItem<ScCallsLog>) mListAdapter.getChild(groupPosition, childPosition);
            ScCallsLog callLog = item.getData();
            if (null != callLog) {
                String phoneNumber = callLog.getCallNumber();
                callTo(phoneNumber);
            }
            return false;
        }
    };

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mDialPad.getVisibility() == View.VISIBLE) {
                mDialPad.setVisibility(View.GONE);
                mShowDialPadButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void hideDialPad() {
        mDialPad.setVisibility(View.GONE);
        mShowDialPadButton.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReadCallLogPermissionEvent event) {
        readCallLogs();
    }

    @Override
    public void callTo(String phoneNumber) {
        Uri uri = Uri.parse("tel:"+phoneNumber);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(uri);
        startActivity(intent);
    }

}
