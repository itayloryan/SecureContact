package com.tayloryan.securecontacts.ui.security;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tayloryan.securecontacts.Constants;
import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.event.ReloadContactsEvent;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.service.ContactService;
import com.tayloryan.securecontacts.ui.BaseActivity;
import com.tayloryan.securecontacts.ui.login.LoginActivity_;
import com.tayloryan.securecontacts.ui.login.UserActivity_;
import com.tayloryan.securecontacts.util.ToolBarConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;


@EActivity(R.layout.activity_security)
public class SecurityActivity extends BaseActivity {

    @ViewById(R.id.user_text_layout)
    protected LinearLayout mUserLayout;
    @ViewById(R.id.import_from_sys_layout)
    protected LinearLayout mImportFromSysLayout;
    @ViewById(R.id.download_from_cloud_layout)
    protected LinearLayout mDownloadFromCloudLayout;
    @ViewById(R.id.upload_to_cloud_layout)
    protected LinearLayout mUploadToCloudLayout;

    @ViewById(R.id.user_text)
    protected TextView mUserText;

    private BmobUser mCurrentUser;
    private ProgressDialog mProgressDialog;

    @Bean
    protected ContactService contactService;

    @AfterViews
    protected void afterViews() {
        initToolBar();
        initialUser();
    }

    private void initialUser() {
        mCurrentUser = BmobUser.getCurrentUser();
        if (null != mCurrentUser) {
            mUserText.setText(mCurrentUser.getUsername() + ("(已登录)"));
        }
    }

    @Click({R.id.user_text_layout, R.id.import_from_sys_layout,
            R.id.download_from_cloud_layout, R.id.upload_to_cloud_layout})
    protected void onClick(View view) {
        mProgressDialog = new ProgressDialog(SecurityActivity.this);
        mProgressDialog.setTitle("请稍候");

        switch (view.getId()) {
            case R.id.user_text_layout:
                if (null == BmobUser.getCurrentUser()) {
                    Intent intent = new Intent(this, LoginActivity_.class);
                    startActivity(intent);
                }
                break;
            case R.id.import_from_sys_layout:
                mProgressDialog.setMessage("正在导入联系人...");
                mProgressDialog.show();
                importContactsFromSys();
                break;
            case R.id.download_from_cloud_layout:
                mProgressDialog.setMessage("正在恢复联系人...");
                mProgressDialog.show();
                importFromCloud();
                break;
            case R.id.upload_to_cloud_layout:
                mProgressDialog.setMessage("正在备份联系人...");
                mProgressDialog.show();
                uploadToCloud();
                break;
        }

    }

    @Background
    protected void uploadToCloud() {
        contactService.uploadToCloud(uploadToCloudHandler);
        onPostUploadToCloud();
    }

    @UiThread
    protected void onPostUploadToCloud() {
        mProgressDialog.dismiss();
    }

    @Background
    protected void importFromCloud() {
        contactService.importFromCloud(importFromCloudHandler);
    }

    @Background
    protected void importContactsFromSys() {
//        try {
            List<ScContact> contacts = contactService.importContactsFromSys();
            contactService.saveContacts(contacts);
            onPostImportFromSys();
//        } catch (Exception e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
//        }
    }

    private void onPostImportFromSys() {
        mProgressDialog.dismiss();
        showToast("成功导入联系人！", Toast.LENGTH_SHORT);
        EventBus.getDefault().postSticky(new ReloadContactsEvent());
        finish();
    }

    private void initToolBar() {
        ToolBarConfig.with(this)
                .showBackButton(true)
                .setLogo(R.drawable.ic_security)
                .setTitle(R.string.security_center)
                .configuration();
    }

    private Handler importFromCloudHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.RETURN_SUCCESS:
                    showToast("联系人恢复成功！", Toast.LENGTH_SHORT);
                    EventBus.getDefault().postSticky(new ReloadContactsEvent());
                    finish();
                    break;
                case Constants.RETURN_ERROR:
                    BmobException exception = (BmobException) msg.obj;
                    showToast(exception.getMessage(), Toast.LENGTH_SHORT);
                    break;
            }
        }
    };

    private Handler uploadToCloudHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mProgressDialog.dismiss();
            switch (msg.what) {
                case Constants.RETURN_SUCCESS:
                    showToast("联系人云备份成功！", Toast.LENGTH_SHORT);
                    break;
                case Constants.RETURN_ERROR:
                    BmobException exception = (BmobException) msg.obj;
                    showToast(exception.getMessage(), Toast.LENGTH_SHORT);
                    break;
            }
        }
    };
}
