package com.tayloryan.securecontacts.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.ui.calllog.CallHistoryFragment;

/**
 * Created by taylor.yan on 1/17/17.
 */

public class DialPad extends RelativeLayout {

    public interface HideDialPadCallBack {
        public void hideDialPad();
    }

    public interface DialButtonCallBack {
        void callTo(String phoneNumber);
    }

    private AutofitTextView mPhoneTextView;
    private View dial_btn_1;
    private View dial_btn_2;
    private View dial_btn_3;
    private View dial_btn_4;
    private View dial_btn_5;
    private View dial_btn_6;
    private View dial_btn_7;
    private View dial_btn_8;
    private View dial_btn_9;
    private View dial_btn_0;
    private View dial_btn_star;
    private View dial_btn_pound;
    private ImageButton mDialBtn;
    private ImageView mHideDialPadButton;
    private ImageButton mBackSpaceButton;

    private String mPhoneNumber;
    private String mLastCallNumber;

    private HideDialPadCallBack mHideDialPadCallBack;
    private DialButtonCallBack mDialButtonCallBack;

    public DialPad(Context context) {
        super(context);
        init();
    }

    public DialPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DialPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.dial_pad_layout, this);
        mPhoneTextView = (AutofitTextView) findViewById(R.id.phone_number_text);
        mPhoneTextView.addTextChangedListener(phoneNumberChangedListener);
        dial_btn_0 = findViewById(R.id.button_dial_zero);
        dial_btn_1 = findViewById(R.id.button_dial_1);
        dial_btn_2 = findViewById(R.id.button_dial_2);
        dial_btn_3 = findViewById(R.id.button_dial_3);
        dial_btn_4 = findViewById(R.id.button_dial_4);
        dial_btn_5 = findViewById(R.id.button_dial_5);
        dial_btn_6 = findViewById(R.id.button_dial_6);
        dial_btn_7 = findViewById(R.id.button_dial_7);
        dial_btn_8 = findViewById(R.id.button_dial_8);
        dial_btn_9 = findViewById(R.id.button_dial_9);
        dial_btn_star = findViewById(R.id.button_dial_star);
        dial_btn_pound = findViewById(R.id.button_dial_pound);
        mDialBtn = (ImageButton) findViewById(R.id.dial_button);
        mHideDialPadButton = (ImageView) findViewById(R.id.hide_dial_pad_btn);
        mBackSpaceButton = (ImageButton) findViewById(R.id.phone_backspace_btn);
        mDialBtn.setOnClickListener(mOnClickListener);
        mPhoneTextView.setText(mPhoneNumber);
        mHideDialPadButton.setOnClickListener(mOnClickListener);
        mBackSpaceButton.setOnClickListener(mOnClickListener);
        mBackSpaceButton.setOnLongClickListener(mOnLongClickListener);
        initialDialButton();
    }

    private TextWatcher phoneNumberChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void initialDialButton() {
        initButton(dial_btn_1, "1", "∞");
        initButton(dial_btn_2, "2", "ABC");
        initButton(dial_btn_3, "3", "DEF");
        initButton(dial_btn_4, "4", "GHI");
        initButton(dial_btn_5, "5", "JKL");
        initButton(dial_btn_6, "6", "MNO");
        initButton(dial_btn_7, "7", "PQRS");
        initButton(dial_btn_8, "8", "TUV");
        initButton(dial_btn_9, "9", "WXYZ");
        initButton(dial_btn_star, "*", "");
        initButton(dial_btn_0, "0", "+");
        initButton(dial_btn_pound, "#", "");
    }

    private void initButton(View buttonView, String num, String chars) {
        buttonView.setTag(num);
        buttonView.setOnClickListener(mDialNumberClickListener);

        TextView numTv = (TextView) buttonView.findViewById(R.id.dial_num);
        numTv.setText(num);

        TextView charsTv = (TextView) buttonView.findViewById(R.id.dial_char);
        charsTv.setText(chars);
    }

    public void setHideDialPadCallBack(HideDialPadCallBack callBack) {
        mHideDialPadCallBack = callBack;
    }

    public void setDialButtonCallBack(CallHistoryFragment dialButtonCallBack) {
        mDialButtonCallBack = dialButtonCallBack;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.hide_dial_pad_btn:
                    if (null != mHideDialPadCallBack) {
                        mHideDialPadCallBack.hideDialPad();
                    }
                    break;
                case R.id.dial_button:
                    if (!TextUtils.isEmpty(mPhoneNumber)) {
                        mLastCallNumber = mPhoneNumber;
                        if (null != mDialButtonCallBack) {
                            mDialButtonCallBack.callTo(mPhoneTextView.getText().toString());
                        }

                        mPhoneTextView.setText("");
                    } else {
                        mPhoneNumber = mLastCallNumber;
                        mPhoneTextView.setText(mPhoneNumber);
                    }
                    break;
                case R.id.phone_backspace_btn:
                    if (!TextUtils.isEmpty(mPhoneNumber)) {
                        mPhoneNumber = mPhoneNumber.substring(0, mPhoneNumber.length() - 1);
                        mPhoneTextView.setText(mPhoneNumber);
                    }
                    break;
            }
        }
    };

    private OnClickListener mDialNumberClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mPhoneNumber = mPhoneNumber == null ? "" : mPhoneNumber;
            mPhoneNumber += view.getTag();
            mPhoneTextView.setText(mPhoneNumber);
        }
    };

    private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (!TextUtils.isEmpty(mPhoneNumber)) {
                mPhoneNumber = "";
                mPhoneTextView.setText(mPhoneNumber);
            }
            return true;
        }
    };

//    @TextChange(R.id.phone_number_text)
//    protected void onTextChanged() {
//        String text = mPhoneTextView.getText().toString();
//        EventBus.getDefault().post();
//    }
}
