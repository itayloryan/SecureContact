package com.tayloryan.securecontacts.ui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.adapter.MenuAdapter;
import com.tayloryan.securecontacts.event.ReadCallLogPermissionEvent;
import com.tayloryan.securecontacts.event.ShowNavigationBarEvent;
import com.tayloryan.securecontacts.event.HideNavigationBarEvent;
import com.tayloryan.securecontacts.ui.calllog.CallHistoryFragment;
import com.tayloryan.securecontacts.ui.contacts.AddContactActivity;
import com.tayloryan.securecontacts.ui.contacts.AddContactActivity_;
import com.tayloryan.securecontacts.ui.contacts.ContactFragment;
import com.tayloryan.securecontacts.ui.message.MessageFragment;
import com.tayloryan.securecontacts.util.PermissionUtil;
import com.tayloryan.securecontacts.widget.MenuWidget;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_home)
public class HomeActivity extends BaseActivity {

    private enum Tab {
        CALL_HISTORY, CONTACTS, MESSAGE, SECURITY
    }

    private final Map<Tab, Integer> mTabToPosition = new EnumMap<Tab, Integer>(Tab.class);
    private List<Tab> tabs = new ArrayList<>();
    private Tab[] mPositionToTab;
    private HomePagerAdapter homePagerAdapter;
    private PopupWindow mMenuWindow;
    private MenuAdapter mMenuAdapter;

    @ViewById(R.id.home_pager)
    protected ViewPager mPager;

    @ViewById(R.id.navigation_layout)
    protected TabLayout mTabLayout;

    @ViewById(R.id.menu_more)
    protected MenuWidget mMenuWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        PermissionUtil.requestRequiredPermissions(this);
    }

    @AfterViews
    protected void afterViews() {
        initialMenu();
        initialTab();
    }

    private void initialTab() {
        mTabToPosition.clear();
        mTabToPosition.put(Tab.CALL_HISTORY, 0);
        tabs.add(Tab.CALL_HISTORY);
        mTabToPosition.put(Tab.CONTACTS, 1);
        tabs.add(Tab.CONTACTS);
        mTabToPosition.put(Tab.MESSAGE, 2);
        tabs.add(Tab.MESSAGE);
        mTabToPosition.put(Tab.SECURITY, 3);
        tabs.add(Tab.SECURITY);

        mPositionToTab = new Tab[tabs.size()];
        for (int i = 0; i <tabs.size(); i++) {
            mPositionToTab[i] = tabs.get(i);
        }

        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(homePagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
               // navigationBar.selectTab(position);
                //updateToolBarByPosition(position);
            }
        });
        mTabLayout.setupWithViewPager(mPager);
        mTabLayout.getTabAt(0).setText("通话记录");
        mTabLayout.getTabAt(1).setText("联系人");
        mTabLayout.getTabAt(2).setText("短信");
        mTabLayout.getTabAt(3).setText("安全中心");
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
                case MESSAGE:
                    fragment = MessageFragment.create();
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

    private void initialMenu() {
        List<String> menus = new ArrayList<>();
        menus.add("新建联系人");
        menus.add("安全中心");
        menus.add("关于");
        mMenuAdapter = new MenuAdapter(this, menus);
        mMenuWidget.setMenuAdapter(mMenuAdapter);
    }

    private MenuWidget.OnMenuItemSelectedListener mOnItemSelectedListener = new MenuWidget.OnMenuItemSelectedListener() {
        @Override
        public void onMenuItemSelected(String title) {
            Intent intent = null;
            if ("新建联系人".equals(title)) {
                intent = new Intent(HomeActivity.this, AddContactActivity_.class);
            }
            startActivity(intent);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HideNavigationBarEvent event) {
//        navigationBar.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShowNavigationBarEvent event) {
//        navigationBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private BottomNavigationBar.OnTabSelectedListener mTabSelectedListener = new BottomNavigationBar.OnTabSelectedListener() {
        @Override
        public void onTabSelected(int position) {
            mPager.setCurrentItem(position);
        }

        @Override
        public void onTabUnselected(int position) {

        }

        @Override
        public void onTabReselected(int position) {

        }
    };

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


