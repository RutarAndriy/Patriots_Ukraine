package com.rutar.patriots_ukraine.listeners;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.process.Process_Get_News;
import com.rutar.patriots_ukraine.utils.Utility;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.app;
import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

public class View_Listener {

//////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач прокручування списку

public static RecyclerView.OnScrollListener scroll_listener =
          new RecyclerView.OnScrollListener() {

int total_item_count;
int visible_item_count;
int first_visible_position;

// ................................................................................................
// Прокручування списку

@Override
public void onScrolled (@NonNull RecyclerView recyclerView, int dx, int dy) {

super.onScrolled(recyclerView, dx, dy);

if (dy > 0) { // Перевірка на скрол вниз

visible_item_count = vars.recycler_layout_manager.getChildCount();
total_item_count = vars.recycler_layout_manager.getItemCount();
first_visible_position = vars.recycler_layout_manager.findFirstVisibleItemPosition();

if (visible_item_count + first_visible_position >= total_item_count &&          // Останній елемент
   !recyclerView.canScrollVertically(1))                 // Немає можливості скролити вниз
     { vars.news_list.stopScroll();
       vars.news_list_reach_end = true;  }
else { vars.news_list_reach_end = false; }

}

else { vars.news_list_reach_end = false; }

// ................................................................................................

if (vars.app_state == 1 &&
   !vars.input_lock &&
    vars.search_text.isEmpty() &&
    vars.news_list_reach_end) { vars.behavior_list.setState(BottomSheetBehavior.STATE_COLLAPSED); }
else                         { vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);    }

// ................................................................................................

View child = vars.recycler_layout_manager.getChildAt(0);
vars.current_news_list_position = (child == null) ? 0 :
                                      (child.getTop() - vars.recycler_layout_manager.getPaddingTop());

// Визначення першого видимого елемента для портретної та ландшафтної орієнтації
/*if (app.current_orientation == 0) { Settings.current_news_list_index =
                                    view.getFirstVisiblePosition(); }
else                              { Settings.current_news_list_index =
                                    view.getFirstVisiblePosition() * 2; }*/

}
};

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач свайпу оновлення

public static SwipeRefreshLayout.OnRefreshListener swipe_listener
        = new SwipeRefreshLayout.OnRefreshListener() {

@Override
public void onRefresh() {

vars.news_list.setEnabled(false);
//vars.running_anim.add("swipe_refresh");
vars.input_lock = true;
vars.swipe_refresh.setRefreshing(true);
vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);

Utility.show_Snack_Bar(Utility.get_String(R.string.progress_get_news), true);
Utility.start_Anim(vars.news_list, vars.refresh_anim_hide);

int d = vars.date[0];
int m = vars.date[1] + 1;
int y = vars.date[2];

String date = y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d);
new Process_Get_News().execute(date);

}

};

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховування натискання клавіш на клавіатурі

public static TextView.OnEditorActionListener editor_action_listener
        = new TextView.OnEditorActionListener() {

public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

if (vars.done_press_search &&
   ((event != null && (event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER))
                   || (actionId == EditorInfo.IME_ACTION_DONE))) {

app.findViewById(R.id.search_button).performClick();

}

return false;

}
};

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач нижньої панелі

public static BottomSheetBehavior.BottomSheetCallback behavior_listener =
          new BottomSheetBehavior.BottomSheetCallback() {

@Override
public void onStateChanged (@NonNull View bottom_Sheet, int newState) {

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public void onSlide (@NonNull View bottom_Sheet, float slideOffset) {

//lp_list.height = 680 + -680 + (int)(bottom_Sheet.getY());
//app.news_list.setLayoutParams(lp_list);

// 680 - 0
// 580 - 100

//app.web_view.setTranslationY(-960 + bottom_Sheet.getY());

//tv.setText("Text: " + bottomSheet.getY());

}
};

///////////////////////////////////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////////////////////////////////////////////////////////

}