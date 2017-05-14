package com.tayloryan.securecontacts.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.ui.BaseActivity;
import com.tayloryan.securecontacts.ui.HomeActivity;
import com.tayloryan.securecontacts.ui.HomeActivity_;
import com.tayloryan.securecontacts.util.DialogUtil;
import com.tayloryan.securecontacts.util.KeyboardUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

@EActivity(R.layout.acticity_sign)
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.login_layout)
    protected LinearLayout mLoginLayout;
    @ViewById(R.id.sign_up_layout)
    protected LinearLayout mSignUpLayout;

    @ViewById(R.id.username_text)
    protected EditText mUserNameText;
    @ViewById(R.id.password_text)
    protected EditText mPasswordText;
    @ViewById(R.id.sign_username_text)
    protected EditText mSignUserNameText;
    @ViewById(R.id.sign_password_text)
    protected EditText mSignPasswordText;
    @ViewById(R.id.sign_password_text2)
    protected EditText mSignPasswordText2;

    @ViewById(R.id.segment_layout)
    protected RadioGroup mSegments;
    @ViewById(R.id.login_radio)
    protected RadioButton mLoginRadio;
    @ViewById(R.id.register_radio)
    protected RadioButton mSignUpRadio;

    @ViewById(R.id.login_btn)
    protected Button mLoginBtn;
    @ViewById(R.id.sign_up_btn)
    protected Button mSignUpBtn;

    private ProgressDialog mLoginDialog;



    @AfterViews
    protected void afterViews() {
        mSegments.setOnCheckedChangeListener(mOnCheckedChangeListener);
        initialLoginDialog();
    }

    private void initialLoginDialog() {
        mLoginDialog = new ProgressDialog(this);
        mLoginDialog.setCancelable(false);
        mLoginDialog.setMessage("正在登录请稍候...");
    }


    @Click({R.id.login_btn, R.id.sign_up_btn})
    protected void onClick(View view) {
        KeyboardUtil.hideSoftKeyBoard(this);
        switch (view.getId()) {
            case R.id.login_btn:
                startLogin();
                break;
            case R.id.sign_up_btn:
                startSignUp();
                break;
        }
    }

    private void startSignUp() {
        String userName = mSignUserNameText.getText().toString().trim();
        String password = mSignPasswordText.getText().toString().trim();
        String password2 = mSignPasswordText2.getText().toString().trim();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password2.equals(password)) {
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        BmobUser user = new BmobUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.signUp(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                handleSignupResult(bmobUser, e);
            }
        });
    }

    private void handleSignupResult(BmobUser bmobUser, BmobException e) {
        if (null == e) {
            mSignUserNameText.setText(null);
            mSignPasswordText.setText(null);
            mSignPasswordText2.setText(null);
            mLoginRadio.setChecked(true);
            mUserNameText.setText(bmobUser.getUsername());
        } else {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void handleLoginResult(BmobException e) {
        mLoginDialog.dismiss();
        if (null == e) {
            startActivity(new Intent(LoginActivity.this, HomeActivity_.class));
            LoginActivity.this.finish();
        } else {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startLogin() {
        mLoginDialog.show();
        String userName = mUserNameText.getText().toString().trim();
        String password = mPasswordText.getText().toString().trim();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            mLoginDialog.dismiss();
            return;
        }
        BmobUser user = new BmobUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.login(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                handleLoginResult(e);
            }
        });
    }

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if (R.id.login_radio == checkedId) {
                mLoginLayout.setVisibility(View.VISIBLE);
                mSignUpLayout.setVisibility(View.GONE);
            } else {
                mLoginLayout.setVisibility(View.GONE);
                mSignUpLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogUtil.dismiss(mLoginDialog);
        mLoginDialog = null;
    }
}
