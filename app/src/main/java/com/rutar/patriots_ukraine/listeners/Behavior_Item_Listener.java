package com.rutar.patriots_ukraine.listeners;

import java.util.*;
import android.view.*;
import android.view.animation.*;

import com.rutar.patriots_ukraine.*;
import com.rutar.patriots_ukraine.utils.*;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;
import static com.rutar.patriots_ukraine.listeners.View_Listener.*;

// ................................................................................................

public class Behavior_Item_Listener {

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void on_Behavior_Item_Click (View view, Patriots_Ukraine app) {

if (vars.input_lock) { return; }

view.startAnimation(AnimationUtils.loadAnimation(app, R.anim.press_button));
Calendar calendar;

// ................................................................................................

switch (view.getId()) {

case R.id.share_facebook: Utility.share_News(0); break;
case R.id.share_twitter:  Utility.share_News(1); break;

case R.id.news_link:      Utility.go_To_Site(vars.news_item.get_Page_Url()); break;

// ................................................................................................

case R.id.get_prev:

    calendar = Calendar.getInstance();
    calendar.set(vars.date[2], vars.date[1], vars.date[0]);
    calendar.add(Calendar.DAY_OF_MONTH, -1);

    vars.date[0] = calendar.get(Calendar.DAY_OF_MONTH);
    vars.date[1] = calendar.get(Calendar.MONTH);
    vars.date[2] = calendar.get(Calendar.YEAR);

    Utility.set_Toolbar_Title();
    swipe_listener.onRefresh();

break;

// ................................................................................................

case R.id.get_next:

    calendar = Calendar.getInstance();
    calendar.set(vars.date[2], vars.date[1], vars.date[0]);
    calendar.add(Calendar.DAY_OF_MONTH, 1);

    vars.date[0] = calendar.get(Calendar.DAY_OF_MONTH);
    vars.date[1] = calendar.get(Calendar.MONTH);
    vars.date[2] = calendar.get(Calendar.YEAR);

    Utility.set_Toolbar_Title();
    swipe_listener.onRefresh();

break;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

}