package com.tayloryan.securecontacts.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.tayloryan.securecontacts.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by taylor.yan on 3/31/17.
 */

@EViewGroup(R.layout.contact_info_item_layout)
public class ContactsBaseItemWidget extends LinearLayout {

    @ViewById(R.id.contact_info_content)
    protected EditText mContentText;

    @ViewById(R.id.contact_info_tag)
    protected Spinner mContactTagSpinner;

    @ViewById(R.id.close_image)
    protected ImageView mCloseImage;

    public boolean mIsClosedByClick;

    private ArrayAdapter<String> mSpinnerAdapter;
    private String[] data;


    public ContactsBaseItemWidget(Context context) {
        super(context);
    }

    public ContactsBaseItemWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsBaseItemWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    protected void afterViews() {
        init();
    }

    private void init() {
        mSpinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.menu_item_layout, data);
        mContactTagSpinner.setAdapter(mSpinnerAdapter);
        mContactTagSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
    }

    private void setSpinnerRes(String[] items) {
        if (null != items) {
            data = items;
        }
    }

    public String getContactContentText() {
        return mContentText.getText().toString().trim();
    }

    public String getContentTag() {
        return mContentText.getTag().toString();
    }

    @Click(R.id.close_image)
    protected void onClick() {
        this.setVisibility(GONE);
        mIsClosedByClick = true;
    }

    private AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mContentText.setTag(data[position]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
