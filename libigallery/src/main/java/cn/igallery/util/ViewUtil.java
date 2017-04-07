package cn.igallery.util;

import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;


import java.util.List;

import cn.igallery.Library;


public class ViewUtil {

    // -------------------------layoutParams
    public static LayoutParams getLayoutParamsLinearLayoutWW() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public static LayoutParams getLayoutParamsLinearLayoutMM() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public static LayoutParams getLayoutParamsLinearLayoutMW() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public static LayoutParams getLayoutParamsLinearLayoutWM() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    public static RelativeLayout.LayoutParams getLayoutParamsRelativeLayoutWW() {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public static RelativeLayout.LayoutParams getLayoutParamsRelativeLayoutMM() {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    public static RelativeLayout.LayoutParams getLayoutParamsRelativeLayoutMW() {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public static RelativeLayout.LayoutParams getLayoutParamsRelativeLayoutWM() {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    public static FrameLayout.LayoutParams getLayoutParamsFrameLayoutWW() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public static FrameLayout.LayoutParams getLayoutParamsFrameLayoutMM() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    public static FrameLayout.LayoutParams getLayoutParamsFrameLayoutMW() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public static FrameLayout.LayoutParams getLayoutParamsFrameLayoutWM() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    // ------------------------layoutInflater
    public static LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(Library.getInstance().getApplication());
    }

    public static View inflate(int resource, ViewGroup root) {
        return getLayoutInflater().inflate(resource, root);
    }

    public static View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        return getLayoutInflater().inflate(resource, root, attachToRoot);
    }

    public static DisplayMetrics getDisplayMetrics() {
        return Library.getInstance().getApplication().getResources().getDisplayMetrics();
    }

    public static int getScreenWidth() {
        DisplayMetrics metrics = getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics metrics = getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static float getDensity() {
        return Library.getInstance().getApplication().getResources().getDisplayMetrics().density;
    }

    public static float getScaledDensity() {
        return Library.getInstance().getApplication().getResources().getDisplayMetrics().scaledDensity;
    }

    public static int sp2px(float sp) {
        final float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    public static int dp2px(float dp) {
        final float scale = getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(float px) {
        final float scale = getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int getScaleHeight(int originalWidth, int originalHeight, int scaleWidth) {
        return originalHeight * scaleWidth / originalWidth;
    }

    public static int getScaleWidth(int originalWidth, int originalHeight, int scaleHeight) {
        return originalWidth * scaleHeight / originalHeight;
    }

    /**
     * 判断当前线程是否是UI线程.
     *
     * @return
     */
    public static boolean isUIThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }



    /**
     * 重置listview高度，解决和scrollview嵌套问题
     *
     * @param listView
     */
    public static void resetListViewHeightBasedOnChildren(ListView listView) {
        int totalHeight = getListViewTotalHeight(listView);
        if (totalHeight > 0) {
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight;
            params.height += 5;
            listView.setLayoutParams(params);
        }
    }

    public static int getListViewTotalHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem != null) {
                listItem.measure(0, 0);
                int height = listItem.getMeasuredHeight();
                int dividerHeight = listView.getDividerHeight() * (listAdapter.getCount() - 1);
                totalHeight += (height + dividerHeight);
            }
        }
        return totalHeight;
    }

    public static void measureView(View v) {
        if (v == null) {
            return;
        }
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
    }

    public static int getViewHeight(View view) {
        int height = 0;
        if (view != null) {
            height = view.getHeight();
            if (height <= 0) {
                measureView(view);
                height = view.getMeasuredHeight();
            }
        }
        return height;
    }

    public static int getViewWidth(View view) {
        int width = 0;
        if (view != null) {
            width = view.getWidth();
            if (width <= 0) {
                measureView(view);
                width = view.getMeasuredWidth();
            }
        }
        return width;
    }

    public static void toggleEmptyMsgByList(List<? extends Object> list, View emptyView) {
        if (emptyView != null) {
            if (list != null && list.size() > 0) {
                hide(emptyView);
            } else {
                show(emptyView);
            }
        }
    }

    public static void toggleViewByList(List<? extends Object> list, View view) {
        if (view != null) {
            if (list != null && list.size() > 0) {
                show(view);
            } else {
                hide(view);
            }
        }
    }


    public static boolean setViewHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            params.height = height;
            view.setLayoutParams(params);
            return true;
        }
        return false;
    }

    public static boolean setViewWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            params.width = width;
            view.setLayoutParams(params);
            return true;
        }
        return false;
    }

    public static void hide(View view) {
        if (view != null && view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }

    public static void invisible(View view) {
        if (view != null && view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void show(View view) {
        if (view != null && view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static int[] getLocationInWindow(View view) {
        int[] location = null;
        if (view != null) {
            location = new int[2];
            view.getLocationInWindow(location);
        }
        return location;
    }

    public static int[] getLocationOnScreen(View view) {
        int[] location = null;
        if (view != null) {
            location = new int[2];
            view.getLocationOnScreen(location);
        }
        return location;
    }

}
