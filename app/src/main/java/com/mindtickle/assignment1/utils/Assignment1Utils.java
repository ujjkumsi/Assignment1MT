package com.mindtickle.assignment1.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by beyonder on 14/5/17.
 */

public class Assignment1Utils {

        private static int screenWidth = 0;
        private static int screenHeight = 0;

        public static int dpToPx(int dp) {
            return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        }

        public static int getScreenHeight(Context c) {
            if (screenHeight == 0) {
                WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                screenHeight = size.y;
            }
            return screenHeight;
        }

        public static int getScreenWidth(Context c) {
            if (screenWidth == 0) {
                WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                screenWidth = size.x;
            }

            return screenWidth;
        }

        public static boolean isAndroid5() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        }

        public static int getActionBarHeight(Context c){
            TypedValue tv = new TypedValue();
            int actionBarHeight = 0;
            if (c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,c.getResources().getDisplayMetrics());
            }
            return actionBarHeight;
        }
}
