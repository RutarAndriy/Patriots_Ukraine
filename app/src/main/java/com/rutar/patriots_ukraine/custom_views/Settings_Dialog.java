package com.rutar.patriots_ukraine.custom_views;

import android.os.Handler;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.Settings;
import com.rutar.patriots_ukraine.utils.Utility;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

public class Settings_Dialog {

public static int settings_id = -1;

private static TextView dialog_message;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Створення діалогового вікна налаштувань

public static View create_Settings_Dialog_View (final Patriots_Ukraine app) {

View dialog_content = app.getLayoutInflater().inflate(R.layout.layout_dialog_content, null);

//((TextView)dialog_content.findViewById(R.id.button_positive)).setText(vars.dialog_buttons[0]);
//((TextView)dialog_content.findViewById(R.id.button_negative)).setText(vars.dialog_buttons[1]);
//((TextView)dialog_content.findViewById(R.id.button_neutral)) .setText(vars.dialog_buttons[10]);
//dialog_content.findViewById(R.id.button_neutral).setVisibility(View.GONE);

dialog_message = ((TextView)dialog_content.findViewById(R.id.dialog_message));
//dialog_message.setText(String.format(vars.settings[vars.settings_index - 1][0], ""));

// ................................................................................................

if (vars.settings_index == 8) {
    //dialog_message.setText(String.format(vars.settings[vars.settings_index - 1][0],
    //                                     "" + vars.page_font_size));
}

else if (vars.settings_index < 5) {
    //((TextView)dialog_content.findViewById(R.id.button_positive)).setText(vars.dialog_buttons[11]);
}

else if (vars.settings_index > 9) {
    dialog_content.findViewById(R.id.dialog_preview).setVisibility(View.VISIBLE);
    dialog_content.findViewById(R.id.button_neutral).setVisibility(View.VISIBLE);
}

// ................................................................................................


if      (vars.settings_index == 12) { ((ImageView)dialog_content
        .findViewById(R.id.dialog_preview)).setImageResource(R.drawable.ic_preview_icon); }
else if (vars.settings_index == 10 ||
         vars.settings_index == 11) { ((ImageView)dialog_content
        .findViewById(R.id.dialog_preview)).setImageResource(R.drawable.ic_menu); }

// ................................................................................................

View item = null;

switch (vars.settings_index) {

    case 1:  item = dialog_content.findViewById(R.id.settings_language);             break;
    case 2:  item = dialog_content.findViewById(R.id.settings_theme);                break;
    case 3:  item = dialog_content.findViewById(R.id.settings_orientation);          break;
    case 4:  item = dialog_content.findViewById(R.id.settings_statusbar);            break;
    case 5:  item = dialog_content.findViewById(R.id.settings_preview);              break;
    case 6:  item = dialog_content.findViewById(R.id.settings_web_behavior);         break;
    case 7:  item = dialog_content.findViewById(R.id.settings_web_behavior_content); break;
    case 8:  item = dialog_content.findViewById(R.id.settings_font_size);            break;
    case 9:  item = dialog_content.findViewById(R.id.settings_anim_toolbar);         break;
    case 10: item = dialog_content.findViewById(R.id.settings_anim_show);            break;
    case 11: item = dialog_content.findViewById(R.id.settings_anim_hide);            break;
    case 12: item = dialog_content.findViewById(R.id.settings_anim_preview);         break;

}

Settings_Dialog.prepare_Item_To_Show(item);
return dialog_content;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
//

public static void settings_Press_OK_Action() {

AppCompatDialog dialog = Dialog.get_Dialog();

// ................................................................................................

switch (vars.settings_index) {

// ................................................................................................

case 1: // Мова

switch (((RadioGroup)dialog.findViewById(R.id.bg_language)).getCheckedRadioButtonId()) {

    case R.id.lang_uk:    vars.language = 1; break;
    case R.id.lang_ru:    vars.language = 2; break;
    case R.id.lang_phone: vars.language = 0; break;

}

vars.pref_settings.edit().putInt("app_language", vars.language).apply();
break;

// ................................................................................................

case 2: // Тема

switch (((RadioGroup)dialog.findViewById(R.id.bg_theme)).getCheckedRadioButtonId()) {

    case R.id.theme_01:     vars.theme = 1; break;
    case R.id.theme_02:     vars.theme = 2; break;
    case R.id.theme_03:     vars.theme = 3; break;
    case R.id.theme_04:     vars.theme = 4; break;
    case R.id.theme_05:     vars.theme = 5; break;
    case R.id.theme_06:     vars.theme = 6; break;
    case R.id.theme_07:     vars.theme = 7; break;
    case R.id.theme_08:     vars.theme = 8; break;
    case R.id.theme_09:     vars.theme = 9; break;
    case R.id.theme_random: vars.theme = 0; break;

}

vars.pref_settings.edit().putInt("app_theme", vars.theme).apply();
break;

// ................................................................................................

case 3: // Орієнтація екрану

switch (((RadioGroup)dialog.findViewById(R.id.bg_orientation)).getCheckedRadioButtonId()) {

    case R.id.orientation_port: vars.orientation = 1; break;
    case R.id.orientation_land: vars.orientation = 2; break;
    case R.id.orientation_auto: vars.orientation = 0; break;

}

vars.pref_settings.edit().putInt("app_orientation", vars.orientation).apply();
break;

// ................................................................................................

case 4: // Статусбар

switch (((RadioGroup)dialog.findViewById(R.id.bg_statusbar)).getCheckedRadioButtonId()) {

    case R.id.statusbar_on:  vars.status_bar = true;  break;
    case R.id.statusbar_off: vars.status_bar = false; break;

}

vars.pref_settings.edit().putBoolean("app_statusbar", vars.status_bar).apply();
break;

// ................................................................................................

case 5: // Прев'ю

switch (((RadioGroup)dialog.findViewById(R.id.bg_preview)).getCheckedRadioButtonId()) {

    case R.id.preview_on:  vars.show_preview = true;  break;
    case R.id.preview_off: vars.show_preview = false; break;

}

vars.pref_settings.edit().putBoolean("app_show_preview", vars.show_preview).apply();
break;

// ................................................................................................

case 6: // Панель "поділитися новиною"

switch (((RadioGroup)dialog.findViewById(R.id.bg_web_behavior)).getCheckedRadioButtonId()) {

    case R.id.web_behavior_on:  vars.web_behavior = true;  break;
    case R.id.web_behavior_off: vars.web_behavior = false; break;

}

vars.pref_settings.edit().putBoolean("app_web_behavior", vars.web_behavior).apply();
break;

// ................................................................................................

case 7: // Налаштування панелі "поділитися новиною"

vars.web_behavior_f = ((CheckBox)dialog.findViewById(R.id.behavior_f))   .isChecked();
vars.web_behavior_t = ((CheckBox)dialog.findViewById(R.id.behavior_t))   .isChecked();
vars.web_behavior_l = ((CheckBox)dialog.findViewById(R.id.behavior_link)).isChecked();

vars.pref_settings.edit().putBoolean("app_web_behavior_f", vars.web_behavior_f).
                              putBoolean("app_web_behavior_t", vars.web_behavior_t).
                              putBoolean("app_web_behavior_l", vars.web_behavior_l).apply();

Utility.set_Web_Behavior_Context();
break;

// ................................................................................................

case 8: // Налаштування розміру шрифту статей

vars.page_font_size = ((SeekBar)dialog.findViewById(R.id.font_seekbar)).getProgress() + 15;

vars.pref_settings.edit().putInt("app_page_font_size", vars.page_font_size).apply();
break;

// ................................................................................................

case 9: // Анімація тулбару

switch (((RadioGroup)dialog.findViewById(R.id.bg_anim_toolbar)).getCheckedRadioButtonId()) {

    case R.id.anim_toolbar_01:  vars.toolbar_anim_show = R.anim.toolbar_show_01;
                                vars.toolbar_anim_hide = R.anim.toolbar_hide_01;
                                break;
    case R.id.anim_toolbar_02:  vars.toolbar_anim_show = R.anim.toolbar_show_02;
                                vars.toolbar_anim_hide = R.anim.toolbar_hide_02;
                                break;
    case R.id.anim_toolbar_03:  vars.toolbar_anim_show = R.anim.toolbar_show_03;
                                vars.toolbar_anim_hide = R.anim.toolbar_hide_03;
                                break;
    case R.id.anim_toolbar_off: vars.toolbar_anim_show = R.anim.toolbar_show_empty;
                                vars.toolbar_anim_hide = R.anim.toolbar_hide_empty;
                                break;

}

vars.pref_settings.edit().putInt("app_toolbar_anim_show", vars.toolbar_anim_show).apply();
vars.pref_settings.edit().putInt("app_toolbar_anim_hide", vars.toolbar_anim_hide).apply();
break;

// ................................................................................................

case 10: // Анімація відкривання вікна

switch (((RadioGroup)dialog.findViewById(R.id.bg_anim_open)).getCheckedRadioButtonId()) {

    case R.id.anim_open_01:  vars.refresh_anim_show = R.anim.refresh_show_01;    break;
    case R.id.anim_open_02:  vars.refresh_anim_show = R.anim.refresh_show_02;    break;
    case R.id.anim_open_03:  vars.refresh_anim_show = R.anim.refresh_show_03;    break;
    case R.id.anim_open_04:  vars.refresh_anim_show = R.anim.refresh_show_04;    break;
    case R.id.anim_open_05:  vars.refresh_anim_show = R.anim.refresh_show_05;    break;
    case R.id.anim_open_06:  vars.refresh_anim_show = R.anim.refresh_show_06;    break;
    case R.id.anim_open_07:  vars.refresh_anim_show = R.anim.refresh_show_07;    break;
    case R.id.anim_open_off: vars.refresh_anim_show = R.anim.refresh_show_empty; break;

}

vars.pref_settings.edit().putInt("app_page_show_anim", vars.refresh_anim_show).apply();
break;

// ................................................................................................

case 11: // Анімація закривання вікна

switch (((RadioGroup)dialog.findViewById(R.id.bg_anim_close)).getCheckedRadioButtonId()) {

    case R.id.anim_close_01:  vars.refresh_anim_hide = R.anim.refresh_hide_01;    break;
    case R.id.anim_close_02:  vars.refresh_anim_hide = R.anim.refresh_hide_02;    break;
    case R.id.anim_close_03:  vars.refresh_anim_hide = R.anim.refresh_hide_03;    break;
    case R.id.anim_close_04:  vars.refresh_anim_hide = R.anim.refresh_hide_04;    break;
    case R.id.anim_close_05:  vars.refresh_anim_hide = R.anim.refresh_hide_05;    break;
    case R.id.anim_close_06:  vars.refresh_anim_hide = R.anim.refresh_hide_06;    break;
    case R.id.anim_close_07:  vars.refresh_anim_hide = R.anim.refresh_hide_07;    break;
    case R.id.anim_close_off: vars.refresh_anim_hide = R.anim.refresh_hide_empty; break;

}

vars.pref_settings.edit().putInt("app_page_hide_anim", vars.refresh_anim_hide).apply();
break;

// ................................................................................................

case 12: // Анімація закривання вікна

switch (((RadioGroup)dialog.findViewById(R.id.bg_anim_preview)).getCheckedRadioButtonId()) {

    case R.id.anim_preview_01:  vars.preview_anim = R.anim.preview_01;    break;
    case R.id.anim_preview_02:  vars.preview_anim = R.anim.preview_02;    break;
    case R.id.anim_preview_03:  vars.preview_anim = R.anim.preview_03;    break;
    case R.id.anim_preview_04:  vars.preview_anim = R.anim.preview_04;    break;
    case R.id.anim_preview_05:  vars.preview_anim = R.anim.preview_05;    break;
    case R.id.anim_preview_06:  vars.preview_anim = R.anim.preview_06;    break;
    case R.id.anim_preview_07:  vars.preview_anim = R.anim.preview_07;    break;
    case R.id.anim_preview_off: vars.preview_anim = R.anim.preview_empty; break;

}

vars.pref_settings.edit().putInt("app_preview_anim", vars.preview_anim).apply();
break;

}

// ................................................................................................

//Settings.set_Settings_Item_Values(Patriots_Ukraine.app);

// Перезавантаження програми
if (vars.settings_index <= 4) {

    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() { Utility.restart(); }
    }, 500);

}

Dialog.dismiss_Dialog();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
//

private static void prepare_Item_To_Show (View item) {

//RadioGroup button_group = null;

switch (item.getId()) {

// ................................................................................................
// Мова

case R.id.settings_language:

switch (vars.language) {

    case 1:  ((RadioButton)item.findViewById(R.id.lang_uk))   .setChecked(true); break;
    case 2:  ((RadioButton)item.findViewById(R.id.lang_ru))   .setChecked(true); break;
    default: ((RadioButton)item.findViewById(R.id.lang_phone)).setChecked(true); break;

}

break;

// ................................................................................................
// Тема

case R.id.settings_theme:

switch (vars.theme) {

    case 1:  ((RadioButton)item.findViewById(R.id.theme_01))    .setChecked(true); break;
    case 2:  ((RadioButton)item.findViewById(R.id.theme_02))    .setChecked(true); break;
    case 3:  ((RadioButton)item.findViewById(R.id.theme_03))    .setChecked(true); break;
    case 4:  ((RadioButton)item.findViewById(R.id.theme_04))    .setChecked(true); break;
    case 5:  ((RadioButton)item.findViewById(R.id.theme_05))    .setChecked(true); break;
    case 6:  ((RadioButton)item.findViewById(R.id.theme_06))    .setChecked(true); break;
    case 7:  ((RadioButton)item.findViewById(R.id.theme_07))    .setChecked(true); break;
    case 8:  ((RadioButton)item.findViewById(R.id.theme_08))    .setChecked(true); break;
    case 9:  ((RadioButton)item.findViewById(R.id.theme_09))    .setChecked(true); break;
    default: ((RadioButton)item.findViewById(R.id.theme_random)).setChecked(true); break;

}

break;

// ................................................................................................
// Орієнтація екрану

case R.id.settings_orientation:

switch (vars.orientation) {

    case 0: ((RadioButton)item.findViewById(R.id.orientation_auto)).setChecked(true); break;
    case 1: ((RadioButton)item.findViewById(R.id.orientation_port)).setChecked(true); break;
    case 2: ((RadioButton)item.findViewById(R.id.orientation_land)).setChecked(true); break;

}

break;

// ................................................................................................
// Статусбар

case R.id.settings_statusbar:

if (vars.status_bar) { ((RadioButton)item.findViewById(R.id.statusbar_on)) .setChecked(true); }
else                     { ((RadioButton)item.findViewById(R.id.statusbar_off)).setChecked(true); }

break;

// ................................................................................................
// Прев'ю

case R.id.settings_preview:

if (vars.show_preview) { ((RadioButton)item.findViewById(R.id.preview_on)) .setChecked(true); }
else                       { ((RadioButton)item.findViewById(R.id.preview_off)).setChecked(true); }

break;

// ................................................................................................
// Панель "поділитися новиною"

case R.id.settings_web_behavior:

if (vars.web_behavior) { ((RadioButton)item.findViewById(R.id.web_behavior_on))
                                               .setChecked(true); }
else                       { ((RadioButton)item.findViewById(R.id.web_behavior_off))
                                               .setChecked(true); }

break;

// ................................................................................................
// Налаштування панелі "поділитися новиною"

case R.id.settings_web_behavior_content:

((CheckBox)item.findViewById(R.id.behavior_f))   .setChecked(vars.web_behavior_f);
((CheckBox)item.findViewById(R.id.behavior_t))   .setChecked(vars.web_behavior_t);
((CheckBox)item.findViewById(R.id.behavior_link)).setChecked(vars.web_behavior_l);

break;

// ................................................................................................
// Налаштування розміру шрифту статей

case R.id.settings_font_size:

SeekBar seekbar = item.findViewById(R.id.font_seekbar);

seekbar.setProgress(vars.page_font_size - 15);
seekbar.setOnSeekBarChangeListener(seekbar_lisener);

break;

// ................................................................................................
// Анімація тулбару

case R.id.settings_anim_toolbar:

switch (vars.toolbar_anim_show) {

    case R.anim.toolbar_show_01:    ((RadioButton)item.findViewById(R.id.anim_toolbar_01))
                                                      .setChecked(true); break;
    case R.anim.toolbar_show_02:    ((RadioButton)item.findViewById(R.id.anim_toolbar_02))
                                                      .setChecked(true); break;
    case R.anim.toolbar_show_03:    ((RadioButton)item.findViewById(R.id.anim_toolbar_03))
                                                      .setChecked(true); break;
    case R.anim.toolbar_show_empty: ((RadioButton)item.findViewById(R.id.anim_toolbar_off))
                                                      .setChecked(true); break;

}

break;

// ................................................................................................
// Анімація відкривання вікна

case R.id.settings_anim_show:

switch (vars.refresh_anim_show) {

    case R.anim.refresh_show_01:    ((RadioButton)item.findViewById(R.id.anim_open_01))
                                                      .setChecked(true); break;
    case R.anim.refresh_show_02:    ((RadioButton)item.findViewById(R.id.anim_open_02))
                                                      .setChecked(true); break;
    case R.anim.refresh_show_03:    ((RadioButton)item.findViewById(R.id.anim_open_03))
                                                      .setChecked(true); break;
    case R.anim.refresh_show_04:    ((RadioButton)item.findViewById(R.id.anim_open_04))
                                                      .setChecked(true); break;
    case R.anim.refresh_show_05:    ((RadioButton)item.findViewById(R.id.anim_open_05))
                                                      .setChecked(true); break;
    case R.anim.refresh_show_06:    ((RadioButton)item.findViewById(R.id.anim_open_06))
                                                      .setChecked(true); break;
    case R.anim.refresh_show_07:    ((RadioButton)item.findViewById(R.id.anim_open_07))
                                                      .setChecked(true); break;
    case R.anim.refresh_show_empty: ((RadioButton)item.findViewById(R.id.anim_open_off))
                                                      .setChecked(true); break;

}

break;

// ................................................................................................
// Анімація закривання вікна

case R.id.settings_anim_hide:

switch (vars.refresh_anim_hide) {

    case R.anim.refresh_hide_01:    ((RadioButton)item.findViewById(R.id.anim_close_01))
                                                      .setChecked(true); break;
    case R.anim.refresh_hide_02:    ((RadioButton)item.findViewById(R.id.anim_close_02))
                                                      .setChecked(true); break;
    case R.anim.refresh_hide_03:    ((RadioButton)item.findViewById(R.id.anim_close_03))
                                                      .setChecked(true); break;
    case R.anim.refresh_hide_04:    ((RadioButton)item.findViewById(R.id.anim_close_04))
                                                      .setChecked(true); break;
    case R.anim.refresh_hide_05:    ((RadioButton)item.findViewById(R.id.anim_close_05))
                                                      .setChecked(true); break;
    case R.anim.refresh_hide_06:    ((RadioButton)item.findViewById(R.id.anim_close_06))
                                                      .setChecked(true); break;
    case R.anim.refresh_hide_07:    ((RadioButton)item.findViewById(R.id.anim_close_07))
                                                      .setChecked(true); break;
    case R.anim.refresh_hide_empty: ((RadioButton)item.findViewById(R.id.anim_close_off))
                                                      .setChecked(true); break;

}

break;

// ................................................................................................
// Анімація для прев'ю

case R.id.settings_anim_preview:

switch (vars.preview_anim) {

    case R.anim.preview_01:    ((RadioButton)item.findViewById(R.id.anim_preview_01))
                                                 .setChecked(true); break;
    case R.anim.preview_02:    ((RadioButton)item.findViewById(R.id.anim_preview_02))
                                                 .setChecked(true); break;
    case R.anim.preview_03:    ((RadioButton)item.findViewById(R.id.anim_preview_03))
                                                 .setChecked(true); break;
    case R.anim.preview_04:    ((RadioButton)item.findViewById(R.id.anim_preview_04))
                                                 .setChecked(true); break;
    case R.anim.preview_05:    ((RadioButton)item.findViewById(R.id.anim_preview_05))
                                                 .setChecked(true); break;
    case R.anim.preview_06:    ((RadioButton)item.findViewById(R.id.anim_preview_06))
                                                 .setChecked(true); break;
    case R.anim.preview_07:    ((RadioButton)item.findViewById(R.id.anim_preview_07))
                                                 .setChecked(true); break;
    case R.anim.preview_empty: ((RadioButton)item.findViewById(R.id.anim_preview_off))
                                                 .setChecked(true); break;

}

break;

}

// ................................................................................................

item.setVisibility(View.VISIBLE);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач повзунка зміни розміру шрифту для статей

private static SeekBar.OnSeekBarChangeListener seekbar_lisener
         = new SeekBar.OnSeekBarChangeListener() {

@Override
public void onProgressChanged (SeekBar seekBar, int value, boolean b) {

//dialog_message.setText(String.format(vars.settings[7][0], "" + (value + 15)));

}

// ................................................................................................

@Override
public void onStartTrackingTouch (SeekBar seekBar) {}

@Override
public void onStopTrackingTouch (SeekBar seekBar) {}

};

///////////////////////////////////////////////////////////////////////////////////////////////////

}