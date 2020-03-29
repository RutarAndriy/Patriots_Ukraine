package com.rutar.patriots_ukraine.utils;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;

import com.rutar.patriots_ukraine.R;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

// ................................................................................................

public class FPS_View extends View {

///////////////////////////////////////////////////////////////////////////////////////////////////
// by GV_FiQst

private int  mFPS = 0;          // the value to show
private int  mFPSCounter = 0;   // the value to count
private long mFPSTime = 0;      // last update time

private View debug_view;

///////////////////////////////////////////////////////////////////////////////////////////////////

public FPS_View (Context context) {

super(context);
debug_view = app.findViewById(R.id.debug_view);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onDraw(Canvas canvas) {

super.onDraw(canvas);

if (debug_view.getVisibility() == VISIBLE) {

    if (SystemClock.uptimeMillis() - mFPSTime > 1000) {
        mFPSTime = SystemClock.uptimeMillis();
        mFPS = mFPSCounter;
        mFPSCounter = 0;
    } else {
        mFPSCounter++;
    }

((TextView) app.findViewById(R.id.debug_text_02)).setText("Input lock: "  + vars.input_lock);
((TextView) app.findViewById(R.id.debug_text_03)).setText("FPS: "  + mFPS);

}

invalidate();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}