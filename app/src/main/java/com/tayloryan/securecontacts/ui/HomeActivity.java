package com.tayloryan.securecontacts.ui;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.event.ReadCallLogPermissionEvent;
import com.tayloryan.securecontacts.ui.calllog.CallHistoryFragment;
import com.tayloryan.securecontacts.ui.contacts.AddContactActivity_;
import com.tayloryan.securecontacts.ui.contacts.ContactFragment;
import com.tayloryan.securecontacts.ui.login.LoginActivity_;
import com.tayloryan.securecontacts.ui.message.MessageFragment;
import com.tayloryan.securecontacts.ui.security.SecurityActivity_;
import com.tayloryan.securecontacts.ui.security.SecurityFragment;
import com.tayloryan.securecontacts.util.DialogUtil;
import com.tayloryan.securecontacts.util.PermissionUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;

@EActivity(R.layout.activity_home)
public class HomeActivity extends BaseActivity {

    private enum Tab {
        CALL_HISTORY, CONTACTS, SECURITY
    }

    private final Map<Tab, Integer> mTabToPosition = new EnumMap<Tab, Integer>(Tab.class);
    private List<Tab> tabs = new ArrayList<>();
    private Tab[] mPositionToTab;
    private HomePagerAdapter homePagerAdapter;
    private PopupMenu mMenu;
    private AlertDialog mLogoutDialog;

    @ViewById(R.id.home_pager)
    protected ViewPager mPager;

    @ViewById(R.id.navigation_layout)
    protected TabLayout mTabLayout;

    @ViewById(R.id.menu_more)
    protected ImageView mMenuWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.requestRequiredPermissions(this);
    }

    @AfterViews
    protected void afterViews() {
        if (BmobUser.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity_.class));
            finish();
        }
        initialTab();
    }

    private void initialTab() {
        mTabToPosition.clear();
        mTabToPosition.put(Tab.CALL_HISTORY, 0);
        tabs.add(Tab.CALL_HISTORY);
        mTabToPosition.put(Tab.CONTACTS, 1);
        tabs.add(Tab.CONTACTS);
        mTabToPosition.put(Tab.SECURITY, 2);
        tabs.add(Tab.SECURITY);

        mPositionToTab = new Tab[tabs.size()];
        for (int i = 0; i <tabs.size(); i++) {
            mPositionToTab[i] = tabs.get(i);
        }

        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(homePagerAdapter);
        mTabLayout.setupWithViewPager(mPager);
        mTabLayout.getTabAt(0).setText("通话记录");
        mTabLayout.getTabAt(1).setText("联系人");
        mTabLayout.getTabAt(2).setText("短信");
        selectTab(Tab.CALL_HISTORY);

    }

    private void selectTab(Tab tab) {
        mPager.setCurrentItem(mTabToPosition.get(tab), false);
    }

    private class HomePagerAdapter extends FragmentPagerAdapter {

        private Map<Integer, Fragment> mFragmentMap = new HashMap<>();

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Tab tab = mPositionToTab[position];
            switch (tab) {
                case CALL_HISTORY:
                    fragment = CallHistoryFragment.create();
                    break;
                case CONTACTS:
                    fragment = ContactFragment.create();
                    break;
                case SECURITY:
                    fragment = MessageFragment.create();
                    break;
            }
            mFragmentMap.put(position, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mFragmentMap.remove(position);
            super.destroyItem(container, position, object);
        }
    }

    @Click(R.id.menu_more)
    protected void onClick(View view) {
        showMenu(view);
    }


    private void showMenu(View view) {
        mMenu = new PopupMenu(this, view);
        getMenuInflater().inflate(R.menu.menu_common, mMenu.getMenu());
        mMenu.setOnMenuItemClickListener(menuItemClickListener);
        mMenu.show();
    }

    private PopupMenu.OnMenuItemClickListener menuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = null;
            switch (item.getItemId()) {
                case R.id.add_contact:
                    intent = new Intent(HomeActivity.this, AddContactActivity_.class);
                    break;
                case R.id.logout:
                    DialogInterface.OnClickListener logout = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BmobUser.logOut();
                            startActivity(new Intent(HomeActivity.this, LoginActivity_.class));
                            HomeActivity.this.finish();
                        }
                    };
                    mLogoutDialog = DialogUtil.createTipDialog(HomeActivity.this, "提示", "确认退出?",logout);
                    mLogoutDialog.show();
                    break;
                case R.id.security:
                    intent = new Intent(HomeActivity.this, SecurityActivity_.class);
                    break;
            }
            if (null != intent) {
                startActivity(intent);
            }
            return false;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogUtil.dismiss(mLogoutDialog);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionUtil.RESULT_PERMS_CODE) {
            for(int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.READ_CALL_LOG)) {
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        EventBus.getDefault().post(new ReadCallLogPermissionEvent());
                        break;
                    }
                }
            }
        }
    }
}


