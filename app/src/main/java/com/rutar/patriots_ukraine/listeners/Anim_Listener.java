package com.rutar.patriots_ukraine.listeners;

import android.util.*;
import android.view.*;
import android.view.animation.*;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rutar.patriots_ukraine.*;
import com.rutar.patriots_ukraine.utils.*;
import com.rutar.patriots_ukraine.other.*;
import com.rutar.patriots_ukraine.process.*;
import com.rutar.patriots_ukraine.custom_views.*;

import java.util.ArrayList;

import static com.rutar.patriots_ukraine.Variables.*;
import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

// ................................................................................................

public class Anim_Listener implements Animation.AnimationListener {

private int anim;
private int view_id;

///////////////////////////////////////////////////////////////////////////////////////////////////

public Anim_Listener (int view_id, int anim) { this.view_id = view_id;
                                               this.anim = anim; }

///////////////////////////////////////////////////////////////////////////////////////////////////

public void onAnimationStart (Animation animation) { Anim_Listener.anim_Start (view_id, anim); }
public void onAnimationEnd   (Animation animation) { Anim_Listener.anim_End   (view_id, anim); }
public void onAnimationRepeat(Animation animation) {}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод викликається перед початком анімації

// Особливі види анімаціЇ
// -1 - Анімація Тризуба
// -2 - Анімація логотипу програми (Тризуб + Вебсайт)

private static void anim_Start (int id, int anim) {

// Список новин завантажено або оновлено - відображаємо його
if (id == R.id.news_list &&
    anim == vars.refresh_anim_show) { vars.news_list.setVisibility(View.VISIBLE); }

// ................................................................................................
// Повернення до списку новин - відображаємо swipe_refresh
if (id == R.id.swipe_refresh &&
    anim == vars.refresh_anim_show) { vars.swipe_refresh.setVisibility(View.VISIBLE);
                                      Utility.toolbar_Views_Hide(); }

// ................................................................................................
// Повернення від списку новин до початкового стану програми
if (id == R.id.logo_layout &&
    anim == -2) { vars.swipe_refresh.setEnabled(false);
                  vars.layout_logo.setVisibility(View.VISIBLE); }

// ................................................................................................

Utility.update_Drawer_State();
Log.d(TAG, "anim_Start. View: " + Utility.get_Resource_Name(id) +
                     ". Anim: " + Utility.get_Resource_Name(anim));

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод викликається після завершення анімації

// Особливі види анімаціЇ
// -1 - Анімація Тризуба
// -2 - Анімація логотипу програми (Тризуб + Вебсайт)

private static void anim_End (int id, int anim) {

// Пошук виконано, відображаємо знайдені новини
if (id == R.id.search_layout &&
    anim == vars.refresh_anim_hide) { vars.search_text_field.setEnabled(true);
                                      vars.search_progress_bar.clearAnimation();
                                      vars.layout_search.setVisibility(View.GONE);
                                      Utility.dismiss_Snack_Bar(); }

// ................................................................................................
// Відображення меню пошуку новин
if (id == R.id.search_layout &&
    anim == vars.refresh_anim_show) { vars.news_array.clear();
                                      vars.news_adapter = new Recycler_Adapter(vars.news_array);
                                      vars.news_list.setAdapter(vars.news_adapter);
                                      vars.layout_search.setVisibility(View.VISIBLE); }

// ................................................................................................
// Приховування списку новин
if (id == R.id.news_list &&
    anim == vars.refresh_anim_hide) { vars.news_list.setVisibility(View.GONE);
                                      vars.news_list_done = false; }

// ................................................................................................
// Список новин готовий до відображення, вимикаємо анімацію swipe_refresh
if (id == R.id.news_list &&
    anim == vars.refresh_anim_show) { vars.swipe_refresh.setRefreshing(false);
                                      vars.news_list.setVisibility(View.VISIBLE);
                                      vars.news_list_done = true;
                                      vars.input_lock = false;
                                      Utility.dismiss_Snack_Bar();
                                      Process_Preview.get_Instance().resume_Thread();

                                      // Якщо мало новин, то показуємо кнопки "next" та "prev"
                                      if (vars.news_array.size() < 4) {
                                          vars.behavior_list.setState(BottomSheetBehavior
                                                            .STATE_COLLAPSED); }}

// ................................................................................................
// Сторінка новини завантажилася, приховуємо swipe_refresh
if (id == R.id.swipe_refresh &&
    anim == vars.refresh_anim_hide) { vars.input_lock = false;
                                      vars.swipe_refresh.setVisibility(View.GONE);

                                      int h_web = vars.web_view.getHeight();
                                      int h_content = vars.web_view.get_Vertical_Scroll_Range();

                                      // Якщо стаття не вміщається в екран, показуємо BehaviorWeb
                                      if (h_content == h_web &&
                                          vars.can_be_favorite &&
                                          Utility.is_Online(app)) {
                                          vars.behavior_web.setState(BottomSheetBehavior
                                                           .STATE_COLLAPSED); }}

// ................................................................................................
// Перехід із сторінки новини до списку новин (кнопка "назад")
if (id == R.id.swipe_refresh &&
    anim == vars.refresh_anim_show) { vars.page_is_reopen = false;

                                      // Якщо мало новин, то показуємо кнопки "next" та "prev"
                                      if (vars.app_state == 1.1f &&
                                          vars.news_array.size() < 4) {
                                          vars.behavior_list.setState(BottomSheetBehavior
                                                            .STATE_COLLAPSED); }}

// ................................................................................................
// На Тризуб натиснули - показуємо анімацію його зникнення
if (id == R.id.logo_app &&
    anim == R.anim.press_logo) { Utility.start_Anim(vars.layout_logo, -1); }

// ................................................................................................
// Тризуб зник - приховуємо його та завантажуємо список новин
if (id == R.id.logo_layout &&
    anim == -1) { vars.layout_logo.setVisibility(View.GONE);
                  vars.swipe_refresh.setEnabled(true);
                  View_Listener.swipe_listener.onRefresh(); }

// ................................................................................................
// Переходимо в режим пошуку з головного режиму
if (id == R.id.logo_layout &&
    vars.app_state == 2.0f) { vars.layout_logo.setVisibility(View.GONE); }

// ................................................................................................
// Переходимо в головний режим з режиму пошуку
if (id == R.id.logo_layout &&
    vars.app_state == 1.0f &&
    anim == vars.refresh_anim_show) { vars.news_array = new ArrayList<>();
                                      vars.news_adapter = new Recycler_Adapter(vars.news_array);
                                      vars.news_list.setAdapter(vars.news_adapter);
                                      vars.search_text_field.getText().clear();
                                      vars.layout_search.setVisibility(View.GONE);
                                      vars.layout_logo.setVisibility(View.VISIBLE); }

// ................................................................................................
// Заголовок приховано, змінюємо його елементи і відображаємо знову
if (anim == vars.toolbar_anim_hide) { Utility.toolbar_Views_Show(); }

// ................................................................................................
// Налаштування -> Головне меню
if (id == R.id.layout_settings &&
    anim == vars.refresh_anim_hide) { vars.layout_settings.setVisibility(View.GONE); }

// ................................................................................................
// Про програму -> Головне меню
if (id == R.id.layout_about &&
    anim == vars.refresh_anim_hide) { vars.layout_about.setVisibility(View.GONE); }

// ................................................................................................
// Завершення анімації заголовка - відображаємо діалогове вікно вибору дати
if (id == R.id.toolbar_text &&
   (vars.app_state == 1.0f ||
    vars.app_state == 1.1f)) { vars.dialog_index = Dialog.Dialog_Date_Picker;
                               Dialog.create_Dialog(app); }

// ................................................................................................
// Завершення анімації кнопки назад - виконуємо відповідну дію
if (id == R.id.toolbar_icon_left &&
   (vars.app_state == 1.2f ||
    vars.app_state == 2.2f ||
    vars.app_state == 3.1f)) {

    if (!vars.page_is_favorite) { Utility.delete_Dir(vars.page_file.getParentFile()); }

    if (vars.app_state == 3.1f &&
            !vars.page_is_favorite) { Utility.prepare_Favorite_List(); }

    if (vars.news_adapter == null) { Utility.prepare_News_List(); }

    vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);
    vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN);
    vars.web_view_scroll_position = 0;

    if      (vars.app_state == 1.2f) { vars.app_state = 1.1f; }
    else if (vars.app_state == 2.2f) { vars.app_state = 2.1f; }
    else if (vars.app_state == 3.1f) { vars.app_state = 3.0f; }

    vars.swipe_refresh.setVisibility(View.VISIBLE);
    Utility.start_Anim(vars.swipe_refresh, vars.refresh_anim_show);

}

// ................................................................................................
// Завершення анімації кнопки пошуку - переходимо до меню пошуку
else if (id == R.id.toolbar_icon_right &&
        (vars.app_state == 1.0f ||
         vars.app_state == 1.1f)) {

    vars.search_mode = true;
    vars.search_text_field.setText("");

    vars.layout_search.setVisibility(View.VISIBLE);

    if (vars.app_state == 1.0f) { Utility.start_Anim(vars.layout_logo, vars.refresh_anim_hide);   }
    else                        { Utility.start_Anim(vars.layout_search, vars.refresh_anim_show); }

    vars.app_state = 2.0f;
    Utility.toolbar_Views_Hide();

}

// ................................................................................................
// Завершення анімації кнопки новин - переходимо до меню новин
else if (id == R.id.toolbar_icon_right &&
        (vars.app_state == 2.0f ||
         vars.app_state == 2.1f)) {

    vars.search_mode = false;
    vars.layout_logo.setVisibility(View.VISIBLE);

    Utility.start_Anim(vars.layout_logo, vars.refresh_anim_show);

    vars.app_state = 1.0f;
    Utility.toolbar_Views_Hide();

}

// ................................................................................................
// Завершення анімації кнопки улюблених сторінок - відображаємо інформацію про них
else if (id == R.id.toolbar_icon_right &&
         vars.app_state == 3.0f) {

    vars.dialog_index = Dialog.Dialog_Favorite_Info;
    Dialog.create_Dialog(app);

}

// ................................................................................................
// Завершення анімації кнопки відновлення налаштувань - відображаємо діалогове вікно
else if (id == R.id.toolbar_icon_right &&
         vars.app_state == 4.0f) {

    vars.dialog_index = Dialog.Dialog_Default_Settings;
    Dialog.create_Dialog(app);

}

// ................................................................................................
// Завершення анімації кнопки перейти на сайт - відображаємо вікно браузерів
else if (id == R.id.toolbar_icon_right &&
         vars.app_state == 5.0f) {

    Utility.go_To_Site(null);

}

// ................................................................................................

Utility.update_Drawer_State();
Log.d(TAG, "anim_End. View: " + Utility.get_Resource_Name(id) +
                   ". Anim: " + Utility.get_Resource_Name(anim));

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}