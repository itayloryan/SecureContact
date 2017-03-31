package com.tayloryan.securecontacts.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.adapter.MenuAdapter;
import com.tayloryan.securecontacts.ui.contacts.AddContactActivity_;


public class MenuWidget extends AppCompatImageView implements View.OnClickListener {

    private ListView mListView;
    private PopupWindow mMenuWindow;
    private MenuAdapter mMenuAdapter;
    private OnMenuItemSelectedListener mOnItemSelectedListener;

    public MenuWidget(Context context) {
        super(context);
        init();
    }

    public MenuWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    @Override
    public void onClick(View v) {
        initialPopupMenu();
        mMenuWindow.showAsDropDown(v);
    }

    public void setMenuAdapter(MenuAdapter menuAdapter) {
        mMenuAdapter = menuAdapter;
    }

    private void init() {
        setOnClickListener(this);
    }

    private void initialPopupMenu() {
        View popupWindow = LayoutInflater.from(getContext()).inflate(R.layout.menu_layout, null);
        mMenuWindow = new PopupWindow(popupWindow);
        mMenuWindow.setWidth(mMenuAdapter.getContentWidth());
        mMenuWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mMenuWindow.setFocusable(true);
        mMenuWindow.setOutsideTouchable(false);
        mMenuWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        mListView = (ListView) popupWindow.findViewById(R.id.menu_list_view);
        mListView.setAdapter(mMenuAdapter);
        mListView.setOnItemClickListener(mOnSortItemClickListener);
    }

    public void setOnItemSelectedListener(OnMenuItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    private AdapterView.OnItemClickListener mOnSortItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = null;
            String title = (String) mMenuAdapter.getItem(position);
            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onMenuItemSelected(title);
            }
//            if (title.equals("新建联系人")) {
//                intent = new Intent(getContext(), AddContactActivity_.class);
//            }
//            getContext().startActivity(intent);
            mMenuWindow.dismiss();
        }
    };

    public interface OnMenuItemSelectedListener {
        void onMenuItemSelected(String title);
    }

}
