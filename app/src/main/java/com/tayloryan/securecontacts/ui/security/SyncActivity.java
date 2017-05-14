package com.tayloryan.securecontacts.ui.security;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.tayloryan.securecontacts.Constants;
import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.service.ContactService;
import com.tayloryan.securecontacts.ui.BaseActivity;
import com.tayloryan.securecontacts.util.ToolBarConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_sync)
public class SyncActivity extends BaseActivity {

    @Bean
    protected ContactService contactService;

    @ViewById(R.id.sync_btn)
    protected Button mSyncButton;

    @ViewById(R.id.contact_switch)
    protected SwitchCompat mContactSwitch;
    @ViewById(R.id.call_log_switch)
    protected SwitchCompat mCallLogSwitch;
    @ViewById(R.id.message_switch)
    protected SwitchCompat mMessageSwitch;

    private int mOption;
    private String mOptionString;

    private boolean mContactSyncOn;
    private boolean mCallLogSyncOn;
    private boolean mMessageSyncOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialView();

    }

    private void initialView() {
        mOption = getIntent().getIntExtra("EXTRA_SYNC_OPT", 0);
        switch(mOption) {
            case Constants.IMPORT_FROM_SYS:
                mOptionString = "从系统导入";
                break;
            case Constants.DOWNLOAD_FROM_CLOUD:
                mOptionString = "从云端导入";
                break;
            case Constants.UPLOAD_TO_CLOUD:
                mOptionString = "上传到云端";
                mSyncButton.setText("开始上传");
                break;
        }
    }

    @AfterViews
    protected void afterViews() {
        initToolBar();
    }

    private void initToolBar() {
        ToolBarConfig
                .with(this)
                .showBackButton(true)
                .setTitle(mOptionString)
                .configuration();
    }

    @CheckedChange({R.id.contact_switch, R.id.call_log_switch, R.id.message_switch})
    protected void onCheckedChang(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.contact_switch:
                mContactSyncOn = isChecked;
                break;
            case R.id.call_log_switch:
                mCallLogSyncOn = isChecked;
                break;
            case R.id.message_switch:
                mMessageSyncOn = isChecked;
                break;
        }
    }

    @Click({R.id.sync_btn})
    protected void onClick(View v) {
        if (mContactSyncOn) {
            importSysContacts();
        }
    }

    private void importSysContacts() {
        try {
            List<ScContact> contacts = contactService.importContactsFromSys();
            contactService.saveContacts(contacts);
            this.finish();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
        }
    }
}
