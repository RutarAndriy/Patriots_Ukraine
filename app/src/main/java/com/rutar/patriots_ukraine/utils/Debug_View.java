package com.rutar.patriots_ukraine.utils;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.annotation.*;

import com.rutar.patriots_ukraine.R;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

// ................................................................................................
// Даний клас необхідний для постійного оновлення debug інформації, оскільки програма не має
// постійного циклу оновлення, а дані необхідно відображати якомога швидче

public class Debug_View extends View {

///////////////////////////////////////////////////////////////////////////////////////////////////
// by GV_FiQst

private int  mFPS = 0;          // the value to show
private int  mFPSCounter = 0;   // the value to count
private long mFPSTime = 0;      // last update time

private View debug_view;
private TextView debug_text_01;
private TextView debug_text_02;
private TextView debug_text_03;

// ................................................................................................

private static final String[] app_states = new String[]
{
    "Startup State",  "Startup State > News List",  "Startup State > News List > WebView",
    "Search State",   "Search State > Search List", "Search State > Search List > WebView",
    "Favorite State", "Favorite State > WebView",
    "Settings state",
    "About state"
};

///////////////////////////////////////////////////////////////////////////////////////////////////
// Конструктор

public Debug_View (Context context) {

super(context);

debug_view = app.findViewById(R.id.debug_view);

debug_text_01 = app.findViewById(R.id.debug_text_01);
debug_text_02 = app.findViewById(R.id.debug_text_02);
debug_text_03 = app.findViewById(R.id.debug_text_03);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Використання циклу перемальовування View замість циклу оновлення

@Override
protected void onDraw (Canvas canvas) {

super.onDraw(canvas);

if (debug_view.getVisibility() == VISIBLE) { update_Debug_Information(); }

invalidate();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Оновлення Debug інформації

@SuppressLint("DefaultLocale")
private void update_Debug_Information() {

if (SystemClock.uptimeMillis() - mFPSTime > 1000) { mFPSTime = SystemClock.uptimeMillis();
                                                    mFPS = mFPSCounter;
                                                    mFPSCounter = 0;  }

else { mFPSCounter++; }

debug_text_01.setText(get_App_State_Description());
debug_text_02.setText(String.format("Input lock: %s", vars.input_lock));
debug_text_03.setText(String.format("FPS: %d", mFPS));

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static String get_App_State_Description() {

switch (String.valueOf(vars.app_state)) {

    case "1.0": return app_states[0];
    case "1.1": return app_states[1];
    case "1.2": return app_states[2];

    case "2.0": return app_states[3];
    case "2.1": return app_states[4];
    case "2.2": return app_states[5];

    case "3.0": return app_states[6];
    case "3.1": return app_states[7];

    case "4.0": return app_states[8];
    case "5.0": return app_states[9];

    default: return "Wrong app state";

}
}

// Кінець класу <Debug_View> //////////////////////////////////////////////////////////////////////

}