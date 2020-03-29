package com.rutar.patriots_ukraine.listeners;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rutar.patriots_ukraine.custom_views.Dialog;
import com.rutar.patriots_ukraine.other.News_Item;
import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.process.Process_Get_News;
import com.rutar.patriots_ukraine.process.Process_Get_Page;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.Settings;
import com.rutar.patriots_ukraine.custom_views.Settings_Dialog;
import com.rutar.patriots_ukraine.utils.Utility;

import java.util.Calendar;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.app;
import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

public class Listener {

private static int debug_count;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач натискань кнопок

public static void on_Item_Click (View view, final Patriots_Ukraine app) {

int id = view.getId();
//if (!vars.running_anim.isEmpty()) { return; }

switch (view.getId()) {

    // Натискання на Тризуб
    case R.id.logo_app: Utility.start_Anim(vars.logo_app, R.anim.press_logo); return;

    // Ствердна відповідь у діалоговому вікні
    case R.id.button_positive:

        switch (vars.dialog_index) {

            // Вибір дати
            case Dialog.Dialog_Date_Picker:

                vars.date[0] = Dialog.tmp_date[0];
                vars.date[1] = Dialog.tmp_date[1];
                vars.date[2] = Dialog.tmp_date[2];

                vars.app_state = 1;
                vars.search_text = "";
                Dialog.dismiss_Dialog();
                Utility.set_Toolbar_Title();
                vars.swipe_refresh.setEnabled(true);
                Utility.draw_Debug();

                //if (vars.show_logo_layout) {
                //    Utility.start_Anim(vars.logo_app, R.anim.press_logo);
                //}

                //else { swipe_listener.onRefresh(); }
                break;

            // Видалення улюблених сторінок
            case Dialog.Dialog_Favorite_Info:

                Utility.delete_Dir(vars.folder_favor);
                vars.folder_favor.mkdir();

                Utility.prepare_Favorite_List();
                Dialog.dismiss_Dialog();
                break;

            // ОК у налаштуваннях
            //case Dialog.Dialog_Settings:
            //    Settings_Dialog.settings_Press_OK_Action();
            //    return;

        }

        break;

    // Заперечна відповідь у діалоговому вікні
    case R.id.button_negative: Dialog.dismiss_Dialog();
                               break;

    // Нейтральна відповідь у діалоговому вікні
    case R.id.button_neutral:

        switch (vars.dialog_index) {

            case -1: break;
            default: Utility.start_Preview_Anim(); break;

        }

        break;

    // Кнопка пошуку новин
    case R.id.search_button:

        if (vars.search_text_field.getText().toString().isEmpty()) {

            //Utility.show_Snackbar(vars.error_strings[8], false);
            return;

        }

        vars.search_text_field.setEnabled(false);
        vars.search_progress_bar.setVisibility(View.VISIBLE);
        vars.search_progress_bar.startAnimation(AnimationUtils
                               .loadAnimation(app, R.anim.search_progress));

        Utility.search_News();
        break;

    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12: vars.settings_index = view.getId();
             //vars.show_dialog = 3;
             break;

}

if (id > 12) { Utility.start_Anim(view, R.anim.press_button);   }
else         { Utility.start_Anim(view, R.anim.press_settings); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач елементів меню

public static void on_Menu_Item_Click (View view, Patriots_Ukraine app) {

//if (!vars.running_anim.isEmpty()) { return; }
vars.error_list.setVisibility(View.GONE);

switch (view.getId()) {

// ................................................................................................
// Елемент меню "Новини"

case R.id.menu_news:

/*if (!app.search_text.isEmpty()) { if (app.drawer.isDrawerOpen(GravityCompat.START))
                                    { app.drawer.closeDrawer(GravityCompat.START); }
                                  return; }*/

// ................................................................................................
// Звичайний стан програми, список знайдених новин є порожнім

if (vars.search_text.isEmpty()) {

vars.app_state = 1;
vars.swipe_refresh.setEnabled(true);

//if (vars.news_array.isEmpty()) { vars.logo_layout.setVisibility(View.VISIBLE);
//                                     vars.show_logo_layout = true; }

}

// ................................................................................................
// Список знайдених новин не є порожнім

else { vars.app_state = 3; }

// ................................................................................................

Utility.prepare_News_List();
Utility.reset_Settings_List();
vars.layout_about.setVisibility(View.GONE);
vars.layout_settings.setVisibility(View.GONE);
vars.layout_displayable.setVisibility(View.VISIBLE);
break;

// ................................................................................................
// Елемент меню "Улюблені"

case R.id.menu_favorite:

vars.app_state = 5;
Utility.reset_Settings_List();
Utility.prepare_Favorite_List();
vars.swipe_refresh.setEnabled(false);
vars.layout_about.setVisibility(View.GONE);
//vars.search_layout.setVisibility(View.GONE);
//vars.show_search_layout = false;
//vars.search_layout.setVisibility(View.GONE);
//vars.show_search_layout = false;
vars.layout_settings.setVisibility(View.GONE);
vars.layout_displayable.setVisibility(View.VISIBLE);
vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);

//vars.logo_layout.setVisibility(View.GONE);
//vars.show_logo_layout = false;

break;

// ................................................................................................
// Елемент меню "Налаштування"

case R.id.menu_settings:

vars.app_state = 7;
//vars.search_layout.setVisibility(View.GONE);
//vars.show_search_layout = false;
vars.layout_displayable.setVisibility(View.GONE);
vars.layout_about.setVisibility(View.GONE);
vars.layout_settings.setVisibility(View.VISIBLE);
break;

// ................................................................................................
// Елемент меню "Про грограму"

case R.id.menu_about:

vars.app_state = 8;
Utility.reset_Settings_List();
//vars.search_layout.setVisibility(View.GONE);
//vars.show_search_layout = false;
vars.layout_displayable.setVisibility(View.GONE);
vars.layout_settings.setVisibility(View.GONE);
vars.layout_about.setVisibility(View.VISIBLE);
break;

// ................................................................................................
// Елемент меню "Вихід"

case R.id.menu_exit: app.exit_App(); break;

// ................................................................................................
// Логотип

case R.id.logo_menu:

debug_count++;

if (debug_count == 3) { vars.app_debug_mode = true;
                        app.findViewById(R.id.debug_view).setVisibility(View.VISIBLE); }
if (debug_count >= 4) { debug_count = 0;
                        vars.app_debug_mode = false;
                        app.findViewById(R.id.debug_view).setVisibility(View.GONE); }

//vars.show_dialog = 4;

break;

}

// ................................................................................................

Utility.set_Menu_Selected(view.getId());
Utility.toolbar_Views_Hide();
//Utility.update_App_State();

if (vars.drawer.isDrawerOpen(GravityCompat.START)) { vars.drawer.closeDrawer(GravityCompat.START); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач toolbar елементів

public static void on_Toolbar_Item_Click (View view, Patriots_Ukraine app) {

//if (!vars.running_anim.isEmpty()) { return; }

if (vars.drawer.isDrawerOpen(GravityCompat.START))
{ vars.drawer.closeDrawer(GravityCompat.START); return; }

///////////////////////////////////////////////////////////////////////////////////////////////////

if (view.getId() == R.id.toolbar_text) {

/*switch (vars.app_state) {

    case 1: //vars.show_dialog = 1;
            break;

    case 3: //if (vars.show_search_layout) { return; }

            vars.search_text = "";
            //vars.show_search_layout = true;
            //vars.search_layout.setVisibility(View.VISIBLE);
            //Utility.start_Anim(vars.search_layout, vars.refresh_anim_show);
            Utility.toolbar_Views_Hide();
            return;

    case 5: vars.AZ_sort =! vars.AZ_sort;
            Utility.prepare_Favorite_List();
            view.startAnimation(AnimationUtils.loadAnimation(app, R.anim.press_logo));
            return;

}*/
}

///////////////////////////////////////////////////////////////////////////////////////////////////

else if (view.getId() == R.id.toolbar_icon_left) {

/*switch (vars.app_state) {

    case 2:
    case 4:
    case 6: if (!vars.page_is_favorite) { Utility.delete_Dir(vars.page_file.getParentFile()); }

            if (vars.app_state == 6 &&
               !vars.page_is_favorite) { Utility.prepare_Favorite_List(); }

            if (vars.news_adapter == null) { Utility.prepare_News_List(); }

            vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN);
            vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);
            vars.web_view_scroll_position = 0;
            vars.app_state--;
            Utility.draw_Debug();

            vars.swipe_refresh.setVisibility(View.VISIBLE);
            Utility.start_Anim(vars.swipe_refresh, vars.refresh_anim_show);
            return;

    default: if (vars.drawer.isDrawerOpen(GravityCompat.START))
                  { vars.drawer.closeDrawer(GravityCompat.START); }
             else { vars.drawer.openDrawer(GravityCompat.START);  }
             break;

}*/
}

///////////////////////////////////////////////////////////////////////////////////////////////////

else if (view.getId() == R.id.toolbar_icon_right) {

/*switch (vars.app_state) {

    case 2:
    case 4:
    case 6: ImageView image = (ImageView) view;
            if (vars.can_be_favorite) { vars.page_is_favorite =! vars.page_is_favorite; }

            if (vars.page_is_favorite) { image.setImageResource(R.drawable.ic_favorite_on); }
            else                      { image.setImageResource(R.drawable.ic_favorite_off); }

            Utility.start_Anim(image, R.anim.press_star);
            return;

    case 1: vars.app_state = 3;
            vars.search_text_field.setText("");

*//*            if (vars.show_logo_layout) {

                vars.search_layout.setVisibility(View.VISIBLE);
                vars.show_search_layout = true;
                Utility.start_Anim(vars.logo_layout, vars.refresh_anim_hide);

            }

            else {

                vars.search_layout.setVisibility(View.VISIBLE);
                vars.show_search_layout = true;
                Utility.start_Anim(vars.search_layout, vars.refresh_anim_show);

            }*//*

            Utility.toolbar_Views_Hide();
            return;

    case 3: vars.search_text = "";
            vars.app_state = 1;
            //vars.show_logo_layout = true;
            //vars.logo_layout.setVisibility(View.VISIBLE);
            //Utility.start_Anim(vars.logo_layout, vars.refresh_anim_show);
            Utility.toolbar_Views_Hide();
            return;

    case 5: //vars.show_dialog = 2;
            break;

    case 7: Toast.makeText(app, "Settings", Toast.LENGTH_SHORT).show(); break;
    case 8: Toast.makeText(app, "About", Toast.LENGTH_SHORT).show(); break;

}*/
}

Utility.start_Anim(view, R.anim.press_button);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач behavior елементів

public static void on_Behavior_Item_Click (View view, Patriots_Ukraine app) {

//if (!vars.running_anim.isEmpty()) { return; }

Calendar calendar = null;
view.startAnimation(AnimationUtils.loadAnimation(app, R.anim.press_button));

switch (view.getId()) {

    case R.id.share_facebook:    Utility.share_News(0); break;
    case R.id.share_twitter:     Utility.share_News(1); break;

    case R.id.news_link:              Utility.go_To_Site(vars.news_item.get_Page_Url()); break;

    ///////////////////////////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////////////////////////


}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач списку новин

public static void on_List_Item_Click (final News_Item item) {

//if (!vars.running_anim.isEmpty()) { return; }

if (vars.behavior_list.getState() == BottomSheetBehavior.STATE_COLLAPSED) {

    vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);
    return;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

//vars.running_anim.add("swipe_refresh");
vars.swipe_refresh.setRefreshing(true);
vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN);
vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);

//Utility.show_Snackbar(vars.other_strings[1], true);

Handler handler = new Handler();
handler.postDelayed(new Runnable() {

    @Override
    public void run() {

        vars.news_item = item;
        new Process_Get_Page().execute(vars.news_item);

    }
}, 1000);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
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
    //vars.running_anim.isEmpty() &&
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
vars.swipe_refresh.setRefreshing(true);
vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);

//Utility.show_Snackbar(vars.other_strings[0], true);
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

}