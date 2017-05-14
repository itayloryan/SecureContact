package com.tayloryan.securecontacts.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tayloryan.securecontacts.R;
import com.tayloryan.securecontacts.util.ColorUtil;

import java.util.Random;

/**
 * Created by taylor.yan on 1/18/17.
 */

public class AvatarView extends RelativeLayout {

    private CircleImageView mAvatarImage;
    private TextView mFirstText;

    private Bitmap mAvatarBitmap;
    private Drawable mAvatarDrawable;
    private int mAvatarResId;
    private Uri mAvatarUri;

    private String mFirstTextString;


    public AvatarView(Context context) {
        super(context);
        init();
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.avatar_view_layout, this);
        mAvatarImage = (CircleImageView) findViewById(R.id.avator_image);
        mFirstText = (TextView) findViewById(R.id.avatar_first_text);
    }

    public void setImageBitmap(Bitmap bm) {
        mAvatarBitmap = bm;
        updateAvavtar(bm);
    }

    private void updateAvavtar(Bitmap bm) {
        mAvatarImage.setImageBitmap(bm);
        mFirstText.setVisibility(GONE);
    }

    private void updateAvavtar(@DrawableRes int resId) {
        mAvatarImage.setImageResource(resId);
        mAvatarImage.setVisibility(VISIBLE);
        mFirstText.setVisibility(GONE);
    }

    public void setImageDrawable(Drawable drawable) {
        mAvatarDrawable = drawable;
        updateAvavtar(drawable);
    }

    private void updateAvavtar(Drawable drawable) {
        mAvatarImage.setImageDrawable(mAvatarDrawable);
    }

    public void setImageResource(@DrawableRes int resId) {
        mAvatarResId = resId;
        updateAvavtar(resId);
    }

    public void setImageURI(Uri uri) {
        if (null != uri) {
            mAvatarUri = uri;
            updateAvavtar(uri);
        }
    }

    private void updateAvavtar(Uri uri) {
        mAvatarImage.setImageURI(uri);
        mAvatarImage.setVisibility(VISIBLE);
        mFirstText.setVisibility(GONE);
    }


    public String getFirstTextString() {
        return mFirstTextString;
    }

    public void setFirstTextString(String firstTextString) {
        mFirstTextString = firstTextString;
        updateText();
    }

    public void setFirstTextColor(@DrawableRes int resid) {
        mFirstText.setBackgroundResource(resid);
    }

    private void updateText() {
        mFirstText.setVisibility(VISIBLE);
        mAvatarImage.setVisibility(GONE);
        mFirstText.setText(mFirstTextString);
     }
}
