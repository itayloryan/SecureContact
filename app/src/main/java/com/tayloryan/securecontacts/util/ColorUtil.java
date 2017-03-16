package com.tayloryan.securecontacts.util;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.tayloryan.securecontacts.R;

import java.util.Random;

/**
 * Created by taylor.yan on 1/19/17.
 */

public class ColorUtil {

    private static int COLOR_GREEN = Color.parseColor("#88B349");
    private static int COLOR_GREEN_DARK = Color.parseColor("#228B22");
    private static int COLOR_BLUE = Color.parseColor("#1E90FF");
    private static int COLOR_BLUE_DARK = Color.parseColor("#4169E1");
    private static int COLOR_PINT = Color.parseColor("#FF69B4");
    private static int COLOR_PURPLE = Color.parseColor("#7B68EE");
    private static int COLOR_ORINGE = Color.parseColor("#FF4500");
    private static int COLOR_CORAL = Color.parseColor("#F08080");
    private static int COLOR_YELLOW = Color.parseColor("#FFA500");
    private static int COLOR_TEAL = Color.parseColor("#008080");

    private static int colors[] = {COLOR_GREEN, COLOR_GREEN_DARK, COLOR_BLUE, COLOR_BLUE_DARK, COLOR_PINT, COLOR_PURPLE, COLOR_ORINGE, COLOR_CORAL, COLOR_YELLOW, COLOR_TEAL};
    private static int drawableRes[] = {
            R.drawable.avatar_bg_cornflowerblue, R.drawable.avatar_bg_darkcyan, R.drawable.avatar_bg_darkgreen,
            R.drawable.avatar_bg_darkorchid, R.drawable.avatar_bg_dodgerblue, R.drawable.avatar_bg_indianred,
            R.drawable.avatar_bg_orange, R.drawable.avatar_bg_orangered, R.drawable.avatar_bg_primary,
            R.drawable.avatar_bg_slateblue};

    private static Random mRandom = new Random();

    public static int getRandowColor() {
        int index = mRandom.nextInt(colors.length);
        return colors[index];
    }

    public static int getRandomColorDrawableRes() {
        int index= mRandom.nextInt(drawableRes.length);
        return drawableRes[index];
    }

//    public static int getColorByName(String name) {
//        String nameAsc = PinYinUtil.getCnASCII(name);
//        int index = PinYinUtil.getCnASCII(name).s
//    }
}
