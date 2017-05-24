package com.tayloryan.securecontacts.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tayloryan.securecontacts.util.BmobConfig;
import com.tayloryan.securecontacts.util.PermissionUtil;

import cn.bmob.v3.Bmob;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(String message, int length) {
        Toast.makeText(this, message, length).show();
    }

    public void callTo(String phoneNumber) {
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(uri);
        if (PermissionUtil.hasCallPermission()) {
            startActivity(intent);
        } else {
            showToast("暂无拨打电话权限，请前往设置查看。", Toast.LENGTH_SHORT);
        }

    }
}
