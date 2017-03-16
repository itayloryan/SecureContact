package com.tayloryan.securecontacts.ui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.event.ReadCallLogPermissionEvent;
import com.tayloryan.securecontacts.event.ShowNavigationBarEvent;
import com.tayloryan.securecontacts.event.HideNavigationBarEvent;
import com.tayloryan.securecontacts.ui.calllog.CallHistoryFragment;
import com.tayloryan.securecontacts.ui.contacts.ContactFragment;
import com.tayloryan.securecontacts.ui.message.MessageFragment;
import com.tayloryan.securecontacts.util.PermissionUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
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
//
//    @ViewById(R.id.bottom_navigation_bar)
//    protected BottomNavigationBar navigationBar;
    @ViewById(R.id.home_pager)
    protected ViewPager mPager;

    @ViewById(R.id.navigation_layout)
    protected TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        PermissionUtil.requestRequiredPermissions(this);
    }

    @AfterViews
    protected void afterViews() {
        initialNavigationBar();
        initialTab();
        updateToolBarByPosition(mTabToPosition.get(Tab.CALL_HISTORY));
    }

    private void initialNavigationBar() {

//        navigationBar
//                .setMode(BottomNavigationBar.MODE_FIXED)
//                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
//                .setBarBackgroundColor("#88B349")
//                .setActiveColor("#FFFFFF")
//                .setInActiveColor("#888888")
//                .addItem(new BottomNavigationItem(R.drawable.ic_call_tab, "Call"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_contacts_tab, "Contacts"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_message_tab, "Message"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_security_tab, "Security"))
//                .initialise();
//        navigationBar.setTabSelectedListener(mTabSelectedListener);
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

    private void updateToolBarByPosition(int position) {
        Tab tab = mPositionToTab[position];
        String title = "";
        switch (tab) {
            case CALL_HISTORY:
                title = "通话记录";
                break;
            case CONTACTS:
                title = "联系人";
                break;
            case MESSAGE:
                title = "短信";
                break;
            case SECURITY:
                title = "安全中心";
                break;
        }
//        ToolBarConfig.with(this)
//                .setTitle(title)
//                .configuration();
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


