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
    private AppCompatActivity mActivity;
    private String mTitle = null;
    private int mTitleRes = -1;
    private int mLogoRes = -1;
    private int mLeftButtonRes = -1;
    private int mRightButtonRes = -1;
    private boolean mShowBackButton;
    private View.OnClickListener mOnRightButtonClickListener;

    private ToolBarConfig(AppCompatActivity activity) {
        mActivity = activity;
    }

    public static ToolBarConfig with(AppCompatActivity activity) {
        return new ToolBarConfig(activity);
    }

    public ToolBarConfig setTitle(int titleRes) {
        mTitleRes = titleRes;
        return this;
    }

    public ToolBarConfig setTitle(String title) {
        mTitle = title;
        return this;
    }

    public ToolBarConfig showBackButton(boolean showBackButton) {
        mShowBackButton = showBackButton;
        return this;
    }

    public ToolBarConfig setLogo(int logoRes) {
        mLogoRes = logoRes;
        return this;
    }

    public ToolBarConfig setLeftButtonRes(int leftButtonRes) {
        mLeftButtonRes = leftButtonRes;
        return this;
    }

    public ToolBarConfig setRightButtonRes(int rightButtonRes) {
        mRightButtonRes = rightButtonRes;
        return this;
    }

    public ToolBarConfig setOnRightButtonClickListener(View.OnClickListener onRightButtonClickListener) {
        mOnRightButtonClickListener = onRightButtonClickListener;
        return this;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            KeyboardUtil.hideSoftKeyBoard(mActivity);
            mActivity.finish();
        }
    };

    public void configuration() {
        mToolbar = (Toolbar) mActivity.findViewById(toolbar);
        ImageView mBack = (ImageView) mActivity.findViewById(R.id.back_button);
        if (mShowBackButton) {
            mBack.setVisibility(View.VISIBLE);
            mBack.setOnClickListener(mOnClickListener);
        } else {
            mBack.setVisibility(View.GONE);
        }

        ImageView mLogo = (ImageView) mActivity.findViewById(R.id.toolbar_logo);
        if (mLogoRes > 0) {
            mLogo.setVisibility(View.VISIBLE);
            mLogo.setImageResource(mLogoRes);
        } else {
            mLogo.setVisibility(View.GONE);
        }

        if (mTitleRes > 0) ((TextView) mActivity.findViewById(R.id.toolbar_title)).setText(mTitleRes);
        if (mLeftButtonRes > 0) {
            ((TextView) mActivity.findViewById(R.id.left_text_button)).setText(mLeftButtonRes);
            mActivity.findViewById(R.id.left_text_button).setVisibility(View.VISIBLE);
            mActivity.findViewById(R.id.left_text_button).setOnClickListener(mOnClickListener);
        }
        if (mRightButtonRes > 0) {
            TextView right = (TextView) mActivity.findViewById(R.id.right_text_button);
            right.setText(mRightButtonRes);
            right.setVisibility(View.VISIBLE);
            if (mOnRightButtonClickListener != null) {
                right.setOnClickListener(mOnRightButtonClickListener);
            }
        }
        if (null != mTitle) ((TextView) mActivity.findViewById(R.id.toolbar_title)).setText(mTitle);

        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mActivity.getSupportActionBar().setDisplayUseLogoEnabled(false);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

}
