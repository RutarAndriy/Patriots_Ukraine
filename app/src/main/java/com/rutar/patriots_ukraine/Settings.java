package com.rutar.patriots_ukraine;

import android.view.*;
import android.widget.*;
import com.github.aakira.expandablelayout.*;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

// ................................................................................................

public class Settings {

private static TextView[] settings_titles = new TextView[7];
private static RelativeLayout[] settings_items = new RelativeLayout[7];
private static ExpandableRelativeLayout[] erl_array = new ExpandableRelativeLayout[7];

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void init_Settings (Patriots_Ukraine app) {

//vars.pref_settings  = app.getSharedPreferences("app_settings", app.MODE_PRIVATE);

init_Settings();                                                       // Ініціалізація налаштувань
restore_Settings();                                                      // Відновлення налаштувань

// Задання необхідної теми
if (vars.theme == 0) { vars.theme_now = (int) (Math.random() * 22) + 1; }
else                 { vars.theme_now = vars.theme; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void init_Settings() {

vars.theme = 0;
vars.language = 0;
vars.app_state = 1;
vars.theme_now = -1;
vars.orientation = 0;
vars.image_quality = 85;
vars.page_font_size = 18;

vars.toolbar_anim_show = R.anim.toolbar_show_01;
vars.toolbar_anim_hide = R.anim.toolbar_hide_01;
vars.refresh_anim_show = R.anim.refresh_show_01;
vars.refresh_anim_hide = R.anim.refresh_hide_01;
vars.preview_anim      = R.anim.preview_01;

vars.app_load_count = -1;
vars.app_menu_index = R.id.menu_news;

vars.AZ_sort = true;
vars.status_bar = true;
vars.show_preview = true;
vars.web_behavior = true;
vars.web_behavior_f = true;
vars.web_behavior_t = true;
vars.web_behavior_l = true;

vars.app_is_rate = false;
vars.app_debug_mode = false;
vars.can_be_favorite = false;
vars.news_list_done = true;

vars.page_is_favorite = false;
vars.page_is_reopen = false;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void save_Settings() {

vars.pref_settings.edit()

.putInt("app_language",          vars.language)
.putInt("app_theme",             vars.theme)
.putInt("app_orientation",       vars.orientation)
.putInt("app_page_font_size",    vars.page_font_size)
.putInt("app_toolbar_show_anim", vars.toolbar_anim_show)
.putInt("app_toolbar_hide_anim", vars.toolbar_anim_hide)
.putInt("app_page_show_anim",    vars.refresh_anim_show)
.putInt("app_page_hide_anim",    vars.refresh_anim_hide)
.putInt("app_preview_anim",      vars.preview_anim)

.putBoolean("app_status_bar",     vars.status_bar)
.putBoolean("app_show_preview",   vars.show_preview)
.putBoolean("app_web_behavior",   vars.web_behavior)
.putBoolean("app_web_behavior_f", vars.web_behavior_f)
.putBoolean("app_web_behavior_t", vars.web_behavior_t)
.putBoolean("app_web_behavior_l", vars.web_behavior_l)

.putInt("app_load_count", vars.app_load_count)
.putBoolean("app_AZ_sort", vars.AZ_sort)
.putBoolean("app_is_rate", vars.app_is_rate)

.apply();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void restore_Settings() {

vars.language          = vars.pref_settings.getInt("app_language",          vars.language);
vars.theme             = vars.pref_settings.getInt("app_theme",             vars.theme);
vars.orientation       = vars.pref_settings.getInt("app_orientation",       vars.language);
vars.page_font_size    = vars.pref_settings.getInt("app_page_font_size",    vars.page_font_size);
vars.toolbar_anim_show = vars.pref_settings.getInt("app_toolbar_show_anim", vars.toolbar_anim_show);
vars.toolbar_anim_hide = vars.pref_settings.getInt("app_toolbar_hide_anim", vars.toolbar_anim_hide);
vars.refresh_anim_show = vars.pref_settings.getInt("app_page_show_anim",    vars.refresh_anim_show);
vars.refresh_anim_hide = vars.pref_settings.getInt("app_page_hide_anim",    vars.refresh_anim_hide);
vars.preview_anim      = vars.pref_settings.getInt("app_preview_anim",      vars.preview_anim);

vars.status_bar     = vars.pref_settings.getBoolean("app_status_bar",     vars.status_bar);
vars.show_preview   = vars.pref_settings.getBoolean("app_show_preview",   vars.show_preview);
vars.web_behavior   = vars.pref_settings.getBoolean("app_web_behavior",   vars.web_behavior);
vars.web_behavior_f = vars.pref_settings.getBoolean("app_web_behavior_f", vars.web_behavior_f);
vars.web_behavior_t = vars.pref_settings.getBoolean("app_web_behavior_t", vars.web_behavior_t);
vars.web_behavior_l = vars.pref_settings.getBoolean("app_web_behavior_l", vars.web_behavior_l);

vars.app_load_count = vars.pref_settings.getInt("app_load_count", vars.app_load_count);

vars.AZ_sort     = vars.pref_settings.getBoolean("app_AZ_sort", vars.AZ_sort);
vars.app_is_rate = vars.pref_settings.getBoolean("app_is_rate", vars.app_is_rate);

vars.toolbar_anim_show = R.anim.toolbar_show_04;
vars.toolbar_anim_hide = R.anim.toolbar_hide_04;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void on_Settings_Item_Click (int id) {

int erl_id = 0;

switch (id) { case R.id.item_language:    erl_id = R.id.erl_language;    break;
              case R.id.item_theme:       erl_id = R.id.erl_theme;       break;
              case R.id.item_orientation: erl_id = R.id.erl_orientation; break;
              case R.id.item_share:       erl_id = R.id.erl_share;       break;
              case R.id.item_font:        erl_id = R.id.erl_font;        break;
              case R.id.item_animation:   erl_id = R.id.erl_animation;   break;
              case R.id.item_extra:       erl_id = R.id.erl_extra;       break; }

for (ExpandableRelativeLayout layout : erl_array) {

if (erl_id == layout.getId()) { layout.toggle();   }
else                          { layout.collapse(); }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

static void prepare_Settings_List (Patriots_Ukraine app) {

settings_items[0] = app.findViewById(R.id.item_language);
settings_items[1] = app.findViewById(R.id.item_theme);
settings_items[2] = app.findViewById(R.id.item_orientation);
settings_items[3] = app.findViewById(R.id.item_share);
settings_items[4] = app.findViewById(R.id.item_font);
settings_items[5] = app.findViewById(R.id.item_animation);
settings_items[6] = app.findViewById(R.id.item_extra);

erl_array[0] = app.findViewById(R.id.erl_language);
erl_array[1] = app.findViewById(R.id.erl_theme);
erl_array[2] = app.findViewById(R.id.erl_orientation);
erl_array[3] = app.findViewById(R.id.erl_share);
erl_array[4] = app.findViewById(R.id.erl_font);
erl_array[5] = app.findViewById(R.id.erl_animation);
erl_array[6] = app.findViewById(R.id.erl_extra);

on_Settings_Item_Click(0);

// ................................................................................................

/*int[] strings_titl = { R.string.settings_language_00,    R.string.settings_theme_00,
            R.string.settings_orientation_00, R.string.settings_share_00,
            R.string.settings_font_00,        R.string.settings_animation_00,
            R.string.settings_extra_00 };

for (int z = 0; z < settings_titles.length; z++) {
    settings_titles[z] = settings_items[z].findViewById(R.id.settings_title);
    settings_titles[z].setText(strings_titl[z]);
}*/

// ................................................................................................

int[] strings_res = { R.string.settings_language_01,    R.string.settings_theme_01,
                      R.string.settings_orientation_01, R.string.settings_share_01,
                      R.string.settings_font_01,        R.string.settings_animation_01,
                      R.string.settings_extra_01 };

int[] icons_res = { R.drawable.ic_language,         R.drawable.ic_theme_light,
                    R.drawable.ic_orientation_auto, R.drawable.ic_share,
                    R.drawable.ic_text_size,        R.drawable.ic_anim_show,
                    R.drawable.ic_favorite_off };

for (int a = 0; a < settings_items.length; a++) {

    // Пояснювальний напис
    ((TextView)settings_items[a].findViewById(R.id.settings_about))
                                .setText(strings_res[a]);

    // Іконка
    ((ImageView)settings_items[a].findViewById(R.id.settings_image))
                                 .setImageResource(icons_res[a]);

}




}

///////////////////////////////////////////////////////////////////////////////////////////////////

}