package com.tayloryan.securecontacts.ui.security;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.model.ScContact;
import com.tayloryan.securecontacts.service.ContactService;
import com.tayloryan.securecontacts.ui.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.bmob.v3.BmobUser;

@EFragment(R.layout.fragment_security)
public class SecurityFragment extends Fragment {

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
        initialUser();
    }

    public static SecurityFragment create() {
        return new SecurityFragment_();
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
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("请稍候");

        switch (view.getId()) {
            case R.id.user_text_layout:
                if (null == BmobUser.getCurrentUser()) {
                    Intent intent = new Intent(getActivity(), LoginActivity_.class);
                    startActivity(intent);
                }
                break;
            case R.id.import_from_sys_layout:
                mProgressDialog.setMessage("正在导入联系人...");
                mProgressDialog.show();
                importContactsFromSys();
            case R.id.download_from_cloud_layout:
                mProgressDialog.setMessage("正在恢复联系人...");
                mProgressDialog.show();
                importFromCloud();
            case R.id.upload_to_cloud_layout:
                mProgressDialog.setMessage("正在备份联系人...");
                mProgressDialog.show();
                uploadToCloud();
                break;
        }

    }

    @Background
    protected void uploadToCloud() {
    }

    @Background
    protected void importFromCloud() {
    }

    @Background
    protected void importContactsFromSys() {
        try {
            List<ScContact> contacts = contactService.importContactsFromSys();
            contactService.saveContacts(contacts);
            onPostImportFromSys();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    @UiThread
    protected void onPostImportFromSys() {
        mProgressDialog.dismiss();
        Toast.makeText(getContext(), "已成功导入联系人", Toast.LENGTH_SHORT).show();
    }
}
