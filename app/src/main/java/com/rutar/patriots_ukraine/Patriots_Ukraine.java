package com.rutar.patriots_ukraine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.rutar.patriots_ukraine.listeners.Back_Key_Listener;
import com.rutar.patriots_ukraine.listeners.Behavior_Item_Listener;
import com.rutar.patriots_ukraine.listeners.Item_Listener;
import com.rutar.patriots_ukraine.listeners.Menu_Item_Listener;
import com.rutar.patriots_ukraine.listeners.Settings_Item_Listener;
import com.rutar.patriots_ukraine.listeners.Toolbar_Item_Listener;
import com.rutar.patriots_ukraine.utils.Custom_Context_Wrapper;
import com.rutar.patriots_ukraine.utils.Utility;
import com.rutar.patriots_ukraine.other.News_Item;
import com.rutar.patriots_ukraine.listeners.View_Listener;
import com.rutar.patriots_ukraine.custom_views.Dialog;

import java.io.File;
import java.util.ArrayList;

///////////////////////////////////////////////////////////////////////////////////////////////////

public class Patriots_Ukraine extends AppCompatActivity {

public static Variables vars;                                                  // Основні перемінні
public static Patriots_Ukraine app;                                    // Екземпляр основного класу

///////////////////////////////////////////////////////////////////////////////////////////////////
// Дана строка дозволяє використовувати векторні зображення на API < 21

static { AppCompatDelegate.setCompatVectorFromResourcesEnabled(true); }

// ................................................................................................

// Відновлення налаштувань до стандартних (з Settings класу)
// Немає діалогу оцінити програму
// Розмір шрифту статей
// Мова - укр/рос/eng
// Немає dimens для планшетів

// Оновлення списку новин
// Пошук новин Gk попередня дата - bug
// Debug view темних тем
// Зміна розміру тексту програми (глобально)
// Забрати зайві анімації тулбару
// Переробити вихід з програми
// Сторінка завантажується прокрученою - bug
// Більше інформації про помилки у снекбарі
// Стара стандартна тема
// Видалення налаштувань старих версій програми
// Збереження-відновлення стану програми
// Правильна обробка повороту екрану
// 25 квітня 1995 року -> 25.04.1995

// Забрати блокування програми при анімації
// Переробити appState - кожен стан програми тепер є окремим +
// Зробити налаштування дієвими
// Ввести час простою (timeout) для завантаження ресурсів
// Ввести чергу завантаження прев'ю
// Виведення повідомлення, коли немає відповідних програм для відкривання сторінок

// Повідомлення про конвертацію старих улюблених сторінок у новий формат
// Повідомлення про оновлення списку новин
// Дворівневий список при ландшафтній орієнтації
// Налаштування при ландшафтній орієнтації
// Відсортовано за зростанням/спаданням

/*

"1.0 - Startup state",
"1.1 - Startup state -> News List",
"1.2 - Startup state -> News List -> WebView",

"2.0 - Search state",
"2.1 - Search state -> Search List",
"2.2 - Search state -> Search List -> WebView",

"3.0 - Favorite state",
"3.1 - Favorite state -> WebView",

"4.0 - Settings state",

"5.0 - About state"

*/

private float сoef;
private Resources res;
private Configuration configuration;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Створення нового Activity

@SuppressLint("SourceLockedOrientationActivity")
@Override
protected void onCreate (Bundle bundle) {

app = this;

Settings.init_Settings(this);
load_Variables(bundle);
set_App_Theme();

///////////////////////////////////////////////////////////////////////////////////////////////////
// Задання орієнтації екрану

switch (vars.orientation) {

    case 1:  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);  break;
    case 2:  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); break;
    default: setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);             break;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

super.onCreate(bundle);



if (!vars.status_bar) { requestWindowFeature(Window.FEATURE_NO_TITLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                             WindowManager.LayoutParams.FLAG_FULLSCREEN); }

setContentView(R.layout.layout_main);

Initialization.init(this);
Dialog.init_Dialog(this);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Зміна стандартного завантажувача ресурсів

@Override
protected void attachBaseContext (Context context) {
    super.attachBaseContext(Custom_Context_Wrapper.wrap(context));
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Призупинення програми

@Override
protected void onPause() {

if (vars.current_news_list_position != 0) {

vars.pref_settings.edit()
    .putInt("app_news_list_index", vars.current_news_list_index)
    .putInt("app_news_list_position", vars.current_news_list_position)
    .apply();

}

super.onPause();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Продовження програми

@Override
protected void onResume() {

vars.last_news_list_index =
vars.pref_settings.getInt("app_news_list_index", 0);

vars.last_news_list_position =
vars.pref_settings.getInt("app_news_list_position", 0);

//if (app.current_orientation == 0)
//    { app.news_list.setSelectionFromTop(Settings.last_news_list_index,
//                                        Settings.last_news_list_position); }

//else { app.news_list.setSelectionFromTop((Settings.last_news_list_index / 2),
//                                          Settings.last_news_list_position); }

// ................................................................................................

super.onResume();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Тимчасовий вихід із програми - наприклад, перехід за посиланням

@Override
protected void onSaveInstanceState (Bundle bundle) {

Settings.save_Settings();
save_Variables(bundle);

super.onSaveInstanceState(bundle);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Задання теми

private void set_App_Theme() {

vars.theme_now = 1;

switch (vars.theme_now) {

case 1:  setTheme(R.style.Theme_Default_Light);    break;
case 2:  setTheme(R.style.Theme_Default_Dark);    break;

case 3:  setTheme(R.style.Theme_Red_Light);    break;
case 4:  setTheme(R.style.Theme_Red_Dark); break;
case 5:  setTheme(R.style.Theme_Orange_Light);  break;
case 6:  setTheme(R.style.Theme_Orange_Dark);   break;
case 7:  setTheme(R.style.Theme_Yellow_Light);   break;
case 8:  setTheme(R.style.Theme_Yellow_Dark); break;
case 9:  setTheme(R.style.Theme_Lime_Light); break;
case 10: setTheme(R.style.Theme_Lime_Dark);  break;
case 11: setTheme(R.style.Theme_Green_Light); break;
case 12: setTheme(R.style.Theme_Green_Dark);    break;
case 13: setTheme(R.style.Theme_Teal_Light); break;
case 14: setTheme(R.style.Theme_Teal_Dark);  break;
case 15: setTheme(R.style.Theme_Blue_Light);   break;
case 16: setTheme(R.style.Theme_Blue_Dark);   break;
case 17: setTheme(R.style.Theme_Indigo_Light); break;
case 18: setTheme(R.style.Theme_Indigo_Dark); break;
case 19: setTheme(R.style.Theme_Violet_Light);  break;
case 20: setTheme(R.style.Theme_Violet_Dark); break;
case 21: setTheme(R.style.Theme_Grey_Light);  break;
case 22: setTheme(R.style.Theme_Gray_Dark); break;
case 23: setTheme(R.style.Theme_Black_Light);  break;
case 24: setTheme(R.style.Theme_Black_Dark); break;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void save_Variables (Bundle bundle) {

bundle.putFloat("app_state",      vars.app_state);

bundle.putInt("app_theme_now",  vars.theme_now);
bundle.putInt("app_menu_index", vars.app_menu_index);

bundle.putInt("app_dialog_index",   vars.dialog_index);
bundle.putInt("app_settings_index", vars.settings_index);

bundle.putString("app_html",        vars.html);
bundle.putString("app_search_text", vars.search_text);

bundle.putBoolean("app_page_is_reopen",     vars.page_is_reopen);
bundle.putBoolean("app_page_is_favorite",   vars.page_is_favorite);

bundle.putDouble("app_web_view_scroll_value",  vars.web_view_scroll_position);

// ................................................................................................

bundle.putParcelable("app_news_item", vars.news_item);
bundle.putParcelableArrayList("app_news_array", vars.news_array);

if (vars.page_file != null) { bundle.putString("app_page_file", vars.page_file.getPath()); }

// ................................................................................................

Log.i(Variables.TAG, "Save variables");

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void load_Variables (Bundle bundle) {

if (bundle == null) { return; }

if (vars.news_item == null)   { vars.news_item   = new News_Item();   }
if (vars.news_array == null)  { vars.news_array  = new ArrayList<>(); }
if (vars.favor_array == null) { vars.favor_array = new ArrayList<>(); }

// ................................................................................................

vars.app_state      = bundle.getInt("app_state");
vars.theme_now      = bundle.getInt("app_theme_now");
vars.app_menu_index = bundle.getInt("app_menu_index");

vars.dialog_index   = bundle.getInt("app_dialog_index");
vars.settings_index = bundle.getInt("app_settings_index");

vars.html        = bundle.getString("app_html");
vars.search_text = bundle.getString("app_search_text");

vars.page_is_reopen     = bundle.getBoolean("app_page_is_reopen");
vars.page_is_favorite   = bundle.getBoolean("app_page_is_favorite");

vars.web_view_scroll_position = bundle.getDouble("app_web_view_scroll_value");

// ................................................................................................

vars.news_item = bundle.getParcelable("app_news_item");
vars.news_array = bundle.getParcelableArrayList("app_news_array");

vars.page_file = new File(bundle.getString("app_page_file", "not_exist"));

// ................................................................................................

Log.i(Variables.TAG, "Load variables");

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховування кнопок та елементів меню

public void on_Item_Click (View view)
    { Item_Listener.on_Item_Click(view, this); }

public void on_Menu_Item_Click (View view)
    { Menu_Item_Listener.on_Menu_Item_Click(view, this); }

public void on_Toolbar_Item_Click (View view)
    { Toolbar_Item_Listener.on_Toolbar_Item_Click(view, this); }

public void on_Settings_Item_Click (View view)
    { Settings_Item_Listener.on_Settings_Item_Click(view, this); }

public void on_Behavior_Item_Click (View view)
    { Behavior_Item_Listener.on_Behavior_Item_Click(view, this); }

@Override
public void onBackPressed() { Back_Key_Listener.on_Back_Key_Click(this); }

///////////////////////////////////////////////////////////////////////////////////////////////////

public void exit_App() {

/*    if (show_rate_dialog && !app_is_rated &&
app_load_count > rate_load_count && is_Online(patriots)) { show_Rate_Dialog(); }*/

//if (false) { return; }

Utility.delete_Dir(vars.folder_prev);
vars.folder_prev.mkdir();

onBackPressed();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}
