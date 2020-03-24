package com.rutar.patriots_ukraine;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Toast;

public class Custom_Web extends WebView {

    public Custom_Web(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Custom_Web(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Custom_Web(Context context) {
        super(context);
    }

/*    @Override
    public void invalidate() {
        super.invalidate();

        if (getContentHeight() > 0) {
            // WebView has displayed some content and is scrollable.

            //Toast.makeText(Patriots_Ukraine.patriots, "Done", Toast.LENGTH_SHORT).show();
            Log.e(Patriots_Ukraine.TAG, "Invalidate, " + "\n" +
                    "height: " + getContentHeight() + "\n" + "");

            startAnimation(AnimationUtils.loadAnimation(Patriots_Ukraine.patriots, R.anim.test));

        }
    }*/
}
