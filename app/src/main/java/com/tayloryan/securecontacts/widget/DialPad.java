package com.tayloryan.securecontacts.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;

/**
 * Created by taylor.yan on 1/17/17.
 */

public class DialPad extends RelativeLayout {

    public interface HideDialPadCallBack {
        public void hideDialPad();
    }
    
    private AutofitTextView phoneText;
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
    private ImageButton dial_btn;
    private ImageView mHideDialPadButton;
    private ImageButton mBackSpaceButton;
    private ImageButton mSendSMSButton;

    private Context mContext;

    private HideDialPadCallBack mHideDialPadCallBack;
    
    public DialPad(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public DialPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public DialPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }
    
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.dial_pad_layout, this);
        phoneText = (AutofitTextView) findViewById(R.id.phone_number_text);
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
        dial_btn = (ImageButton) findViewById(R.id.dial_button);
        mHideDialPadButton = (ImageView) findViewById(R.id.hide_dial_pad_btn);
        mSendSMSButton = (ImageButton) findViewById(R.id.send_sms_btn);
        mBackSpaceButton = (ImageButton) findViewById(R.id.phone_backspace_btn);
        dial_btn.setOnClickListener(mOnClickListener);
        mHideDialPadButton.setOnClickListener(mOnClickListener);
        mSendSMSButton.setOnClickListener(mOnClickListener);
        mBackSpaceButton.setOnClickListener(mOnClickListener);
        initialDialButton();
    }
    
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

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.hide_dial_pad_btn:
                    if (null != mHideDialPadCallBack) {
                        mHideDialPadCallBack.hideDialPad();
                    }
                    break;
                case R.id.send_sms_btn:
                    //
                    break;
                case R.id.phone_backspace_btn:
                    //
                    break;
            }
        }
    };
    
    private OnClickListener mDialNumberClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

         }
    };
}
