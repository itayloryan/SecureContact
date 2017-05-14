package com.tayloryan.securecontacts.ui.login;

import android.view.View;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.ui.BaseActivity;
import com.tayloryan.securecontacts.util.ToolBarConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bmob.v3.BmobUser;

@EActivity(R.layout.activity_user)
public class UserActivity extends BaseActivity {

    @ViewById(R.id.user_text)
    protected TextView mUserText;

    private BmobUser mCurrentUser;

    @AfterViews
    protected void afterViews() {
        initToolBar();
        initialUser();
    }

    private void initialUser() {
        mCurrentUser = BmobUser.getCurrentUser();
        if (null != mCurrentUser) {
            mUserText.setText(mCurrentUser.getUsername());
        }
    }

    private void initToolBar() {
        ToolBarConfig.with(this)
                .showBackButton(true)
                .setTitle(R.string.user_center)
                .configuration();
    }

    @Click({R.id.logout_btn})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout_btn:
                BmobUser.logOut();
                break;
        }
    }
}
