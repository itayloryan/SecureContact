package com.tayloryan.securecontacts.util;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import static com.tayloryan.securecontacts.R.id.toolbar;
import com.tayloryan.securecontacts.R;


public class ToolBarConfig {

    private Toolbar mToolbar;
    private AppCompatActivity activity;
    private String mTitle = null;
    private int titleRes = -1;
    private int logoRes = -1;
    private boolean showBackButton;

    private ToolBarConfig(AppCompatActivity activity) {
        this.activity = activity;
    }

    public static ToolBarConfig with(AppCompatActivity activity) {
        return new ToolBarConfig(activity);
    }

    public ToolBarConfig setTitle(int titleRes) {
        this.titleRes = titleRes;
        return this;
    }

    public ToolBarConfig setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public ToolBarConfig showBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
        return this;
    }

    public ToolBarConfig setLogo(int logoRes) {
        this.logoRes = logoRes;
        return this;
    }

    public void configuration() {
        mToolbar = (Toolbar) activity.findViewById(toolbar);
        ImageView mBack = (ImageView) activity.findViewById(R.id.back_button);
        if (showBackButton) {
            mBack.setVisibility(View.VISIBLE);
            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardUtil.hideSoftKeyBoard(activity);
                    activity.finish();
                }
            });
        } else {
            mBack.setVisibility(View.GONE);
        }

        ImageView mLogo = (ImageView) activity.findViewById(R.id.toolbar_logo);
        if (logoRes > 0) {
            mLogo.setVisibility(View.VISIBLE);
            mLogo.setImageResource(logoRes);
        } else {
            mLogo.setVisibility(View.GONE);
        }

        if (titleRes > 0) ((TextView) activity.findViewById(R.id.toolbar_title)).setText(titleRes);
        if (null != mTitle) ((TextView) activity.findViewById(R.id.toolbar_title)).setText(mTitle);

        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayUseLogoEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

}
