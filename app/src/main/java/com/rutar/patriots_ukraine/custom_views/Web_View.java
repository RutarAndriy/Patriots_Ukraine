package com.rutar.patriots_ukraine.custom_views;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.Settings;
import com.rutar.patriots_ukraine.Variables;
import com.rutar.patriots_ukraine.utils.Utility;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

public class Web_View extends WebView {

private long last_touch = -1;

///////////////////////////////////////////////////////////////////////////////////////////////////

public Web_View (Context context) { super(context); }
public Web_View (Context context, AttributeSet attrs) { super(context, attrs); }

public Web_View (Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
}

///////////////////////////////////////////////////////////////////////////////////////////////////

public int get_Vertical_Scroll_Range() { return super.computeVerticalScrollRange(); }

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onScrollChanged (int left, int top, int old_left, int old_top) {

super.onScrollChanged(left, top, old_left, old_top);

if (vars.app_state != 1.2f &&
    vars.app_state != 2.2f &&
    vars.app_state != 3.1f) { return; }

int height_content = (int) Math.floor(this.getContentHeight() * this.getScale());
int height_web_view = this.getMeasuredHeight();

if (height_content - getScrollY() - height_web_view < 3) {

    if (vars.web_behavior && vars.can_be_favorite && Utility.is_Online(app)) {
        vars.behavior_web.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}

else { vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN); }

// ................................................................................................

vars.web_view_scroll_position = ((double)  vars.web_view.getScrollY() /
                                 (double) (vars.web_view.computeVerticalScrollRange() -
                                           vars.web_view.getMeasuredHeight()) * 100);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static Custom_Web_View_Client get_Custom_Web_View_Client() {
    return new Custom_Web_View_Client();
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// WebViewClient

private static class Custom_Web_View_Client extends WebViewClient {

@Override
public boolean shouldOverrideUrlLoading (WebView view, final String url) {

new Handler().postDelayed(new Runnable() {

@Override
public void run() { Utility.go_To_Site(url); }

}, 500);

return true;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public void onPageFinished (WebView view, String url) {

super.onPageFinished(view, url);
vars.swipe_refresh.setRefreshing(false);
vars.web_view.setVisibility(GONE);
Utility.dismiss_Snackbar();

// ................................................................................................

new Handler().postDelayed(new Runnable() {

@Override
public void run() {

vars.web_view.setVisibility(VISIBLE);

double scroll_value = (double)(vars.web_view.computeVerticalScrollRange() -
                               vars.web_view.getMeasuredHeight()) / 100 *
                               vars.web_view_scroll_position;

vars.web_view.setScrollY((int)(scroll_value));

// ................................................................................................

Utility.start_Anim(vars.swipe_refresh, vars.refresh_anim_hide);

// Різні дії для відкритої та відновленої сторінки
if (!vars.page_is_reopen) { Utility.toolbar_Views_Hide(); }
else { Utility.start_Anim(vars.web_view, R.anim.refresh_show_02); }

}
}, 500);

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public boolean onTouchEvent (MotionEvent event) {

// ................................................................................................
// Реагування на дотики

if (event.getAction() == MotionEvent.ACTION_DOWN) {

// ................................................................................................
// Якщо натиснули двічі

if (System.currentTimeMillis() - last_touch < 300) {

// ................................................................................................
// Якщо сторінка є масштабована, то скидаємо масштабування
// Якщо сторінка не є масштабованою, то масштабуємо її

if (!canZoomOut()) { Utility.zoom_In_Web_View();  return true; }
else               { Utility.zoom_Out_Web_View(); return true; }

}

// ................................................................................................
// Записуємо час останнього дотику

else { last_touch = System.currentTimeMillis(); }

// ................................................................................................
// Приховуємо панель "поділитися новиною"

vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN);

}

// ................................................................................................

return super.onTouchEvent(event);
//return false;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}