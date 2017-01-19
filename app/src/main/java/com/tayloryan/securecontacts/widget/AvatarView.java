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

/**
 * Created by taylor.yan on 1/18/17.
 */

public class AvatarView extends RelativeLayout {

    private CircleImageView mAvatarImage;
    private TextView mFisrtText;

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
        mFisrtText = (TextView) findViewById(R.id.avatar_first_text);

        if (null != mAvatarBitmap) {
            mAvatarImage.setImageBitmap(mAvatarBitmap);
        } else if (null != mAvatarDrawable) {
            mAvatarImage.setImageDrawable(mAvatarDrawable);
        } else if (mAvatarResId > 0) {
            mAvatarImage.setImageResource(mAvatarResId);
        } else if (null != mAvatarUri) {
            mAvatarImage.setImageURI(mAvatarUri);
        } else {
            mFisrtText.setVisibility(VISIBLE);
            mAvatarImage.setVisibility(GONE);
            mFisrtText.setText(mFirstTextString);
        }
    }

    public void setImageBitmap(Bitmap bm) {
        mAvatarBitmap = bm;
    }

    public void setImageDrawable(Drawable drawable) {
        mAvatarDrawable = drawable;
    }

    public void setImageResource(@DrawableRes int resId) {
        mAvatarResId = resId;
    }

    public void setImageURI(Uri uri) {
        mAvatarUri = uri;
    }


    public String getFirstTextString() {
        return mFirstTextString;
    }

    public void setFirstTextString(String firstTextString) {
        mFirstTextString = firstTextString;
    }
}
