package com.rutar.patriots_ukraine.custom_views;

import android.os.*;
import android.util.*;
import android.webkit.*;
import android.content.*;
import android.graphics.*;
import android.view.animation.*;

import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.utils.Utility;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;
import static com.rutar.patriots_ukraine.Variables.*;

public class Web_Share_View extends WebView {

private boolean start = true;
private boolean timeout = true;

///////////////////////////////////////////////////////////////////////////////////////////////////

    public Web_Share_View (Context context) { super(context); }
    public Web_Share_View (Context context, AttributeSet attrs) { super(context, attrs); }

    public Web_Share_View (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onSizeChanged (int new_w, int new_h, int old_w, int old_h) {

super.onSizeChanged(new_w, new_h, old_w, old_h);

float current_height = new_h / vars.density;

if (new_h > old_h && current_height > 50) {

    Dialog.share_loading.clearAnimation();
    Dialog.share_loading.setVisibility(GONE);

    timeout = false;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

public Custom_Web_View_Client get_Custom_Web_View_Client() {

return new Custom_Web_View_Client();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// WebViewClient

private class Custom_Web_View_Client extends WebViewClient {

@Override
public boolean shouldOverrideUrlLoading (WebView view, final String url) {

// Facebook опублікував новину
if (url.contains("facebook.com/dialog/close_window")) {

    Utility.show_Snack_Bar("Facebook is share", false);
    Dialog.dismiss_Dialog();
    return false; }

else {

    view.loadUrl(url);
    return true;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public void onLoadResource (WebView view, String url) {

Log.e(TAG, "onLoadResource");
Log.e(TAG, "" + url);

// Twitter опублікував новину
if (url.contains("api.twitter.com") &&
    url.contains("statuses/update.json")) {

Utility.show_Snack_Bar("Twitter is share", false);
Dialog.dismiss_Dialog(); }

super.onLoadResource(view, url);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public void onPageStarted (WebView view, String url, Bitmap favicon) {

super.onPageStarted(view, url, favicon);

if (start) {

Dialog.share_loading.setVisibility(VISIBLE);
Dialog.share_loading.startAnimation(AnimationUtils.loadAnimation(app, R.anim.share_loading));
start = false;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

new Handler().postDelayed(new Runnable() {

@Override
public void run() {

if (timeout) {

Dialog.dismiss_Dialog();
Utility.show_Snack_Bar(Utility.get_String(R.string.error_share_timeout), false);

}
}

}, 25000); // Час таймауту
}

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}