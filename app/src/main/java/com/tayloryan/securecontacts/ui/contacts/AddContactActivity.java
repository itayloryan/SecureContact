package com.tayloryan.securecontacts.ui.contacts;

import android.view.View;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.ui.BaseActivity;
import com.tayloryan.securecontacts.util.ToolBarConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_add_contact)
public class AddContactActivity extends BaseActivity {



    @AfterViews
    protected void afterViews() {
        initToolBar();
    }

    private void initToolBar() {
        ToolBarConfig.with(this)
                .showBackButton(false)
                .setLeftButtonRes(R.string.cancel_text)
                .setTitle(R.string.add_contact_text)
                .setRightButtonRes(R.string.save_text)
                .setOnRightButtonClickListener(mSaveButtonClickListener)
                .configuration();
    }

    private View.OnClickListener mSaveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
