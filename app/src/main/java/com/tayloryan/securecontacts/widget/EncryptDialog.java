package com.tayloryan.securecontacts.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;

/**
 * Created by 煌 on 2017/4/28.
 */

public class EncryptDialog extends Dialog {

    private TextView mTitleText;
    private EditText mPasswordText;
    private Button mPositiveButton;
    private Button mNagetiveButton;


    public EncryptDialog(@NonNull Context context) {
        super(context, R.style.encrypt_dialog);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.encryprt_dialog_layout, null);
        mTitleText = (TextView) view.findViewById(R.id.dialog_title);
        mPasswordText = (EditText) view.findViewById(R.id.password_text);
        mPositiveButton = (Button) view.findViewById(R.id.button_ok);
        mNagetiveButton = (Button) view.findViewById(R.id.button_cancel);
        setContentView(view);
    }

    public EditText getPasswordEditText() {
        return mPasswordText;
    }

    public void setTitle(String title) {
        mTitleText.setText(title);
    }

    public void setPositiveClickListener(View.OnClickListener onClickListener) {
        mPositiveButton.setOnClickListener(onClickListener);
    }

    public void setNagetiveClickListener(View.OnClickListener onClickListener) {
        mNagetiveButton.setOnClickListener(onClickListener);
    }

    public String getPasswordText() {
        return mPasswordText.getText().toString().trim();
    }

}
