package com.tayloryan.securecontacts.ui.calllog;

import android.Manifest;
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

    private Cursor mCursor;
    private GenericExpandableListAdapter mListAdapter;
    private List<ListViewGroup<ScCallsLog>> callLogGroup = new ArrayList<>();
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
        mListAdapter = new GenericExpandableListAdapter(getActivity(), (List<ListViewGroup<?>>) (Object) callLogGroup);
        mListView.setAdapter(mListAdapter);
    }

    @Click(R.id.show_dialpad_btn)
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_dialpad_btn:
                EventBus.getDefault().post(new HideNavigationBarEvent());
                mShowDialPadButton.setVisibility(View.GONE);
                mDialPad.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Background
    protected void readCallLogs() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String sortOrder = CallLog.Calls.DEFAULT_SORT_ORDER;
        mCursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, sortOrder);
        onPostReadCallLogs(mCursor);
    }

    @UiThread
    public void onPostReadCallLogs(Cursor cursor) {
        ListViewGroup<ScCallsLog> group = new ListViewGroup<>("call_log");
        List<String> phoneNumbers = new ArrayList<>();
        group.setHeaderVisible(false);
        if (cursor == null) {
            return;
        }

        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            String number = mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.NUMBER));
            if (phoneNumbers.contains(number)) {
                continue;
            }
            ScCallsLog scCallsLog = new ScCallsLog();
            scCallsLog.setCallerName(mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            scCallsLog.setCallNumber(mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.NUMBER)));
            scCallsLog.setCallTime(mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.DATE)));
            scCallsLog.setCallNumber(number);
            phoneNumbers.add(number);
            //scCallsLog.setAvatarUri(mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI)));
            scCallsLog.setCallType(mCursor.getInt(mCursor.getColumnIndex(CallLog.Calls.TYPE)));
            ListViewItem callLogItem = new ListViewItem(scCallsLog, mCallLogItemConverter);
            group.addItem(callLogItem);
        }
        cursor.close();
        callLogGroup.add(group);
        mListAdapter.notifyDataSetChanged();
        for (int i = 0; i < mListAdapter.getGroupCount(); i ++ ) {
            mListView.expandGroup(i);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void hideDialPad() {
        mDialPad.setVisibility(View.GONE);
        mShowDialPadButton.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new ShowNavigationBarEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReadCallLogPermissionEvent event) {
        readCallLogs();
    }

    @Override
    public void callTo(String phoneNumber) {
        Uri uri=Uri.parse("tel:"+phoneNumber);
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(uri);
        getActivity().startActivity(intent);
    }

    public void onEvent() {

    }


}
