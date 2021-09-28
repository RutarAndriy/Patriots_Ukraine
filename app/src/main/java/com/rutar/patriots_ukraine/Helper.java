package com.rutar.patriots_ukraine;

import java.io.*;
import java.util.*;
import android.net.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.content.*;
import com.nispok.snackbar.*;
import android.view.animation.*;
import com.nispok.snackbar.listeners.*;
import com.wdullaer.materialdatetimepicker.date.*;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;

import static com.rutar.patriots_ukraine.Utilities.*;
import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Допоміжний клас, який містить прослуховувачі команд та
// методи головного класу для зручності читання коду

public class Helper {

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач списку новин

public static AdapterView.OnItemClickListener list_listener =
          new AdapterView.OnItemClickListener() {

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

        progress_text.setText(progress_title_2);
        progress.show();

        list_item = ((News_Item) parent.getItemAtPosition(position));

        new Async_Tasks.Site_Preparation().execute();

    }
};

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач анімації логотипу

public static Animation.AnimationListener animation_listener =
          new Animation.AnimationListener() {

    @Override
    public void onAnimationEnd (Animation animation) {

        logo.setVisibility(View.GONE);
        anim_is_running = false;
        logo_visibility = 1;
        refresh_News();

    }

@Override
public void onAnimationStart (Animation animation) { anim_is_running = true; }
@Override
public void onAnimationRepeat (Animation animation) {}

};

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач команд Snackbar

public static ActionClickListener snackbar_listener = new ActionClickListener() {

    @Override
    public void onActionClicked (Snackbar snackbar) {

        save_Settings();
        patriots.recreate();

    }

};

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач закриття діалогових вікон

public static AlertDialog.OnDismissListener dialog_dimiss_listener =
          new DialogInterface.OnDismissListener() {

    @Override
    public void onDismiss (DialogInterface dialog) {

        rate_is_show = false;
        info_is_show = false;
        repair_is_show = false;

    }

};

///////////////////////////////////////////////////////////////////////////////////////////////////
// Показ діалогового вікна оцінювання

public static void show_Rate_Dialog() {

reset_Dialog();
dialog_title.setText(R.string.app_name);
dialog_message.setText(rate_text[0] + " \"" +
                       patriots.getResources().getString(R.string.app_name)+ "\", " +
                       rate_text[1]);

///////////////////////////////////////////////////////////////////////////////////////////////////

AlertDialog.Builder builder = new AlertDialog.Builder(patriots);

builder.setCustomTitle(dialog_title_view)
       .setView(dialog_message_view)
       .setCancelable(false)

.setPositiveButton(rate_text[3], new DialogInterface.OnClickListener() {

    public void onClick (DialogInterface dialog, int id) {

        Uri uri = Uri.parse("market://details?id=" + patriots.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        try { patriots.startActivity(goToMarket);
              app_is_rated = true; }

        catch (Exception e) { Toast.makeText(patriots, rate_text[2], Toast.LENGTH_LONG).show(); }

        save_Rate_Settings();
        dialog.cancel();
        rate_is_show = false;

    }

})

.setNeutralButton(rate_text[4], new DialogInterface.OnClickListener() {

    public void onClick (DialogInterface dialog, int id) { dialog.cancel();
                                                          rate_is_show = false; }

})

.setNegativeButton(rate_text[5], new DialogInterface.OnClickListener() {

    public void onClick (DialogInterface dialog, int id) { app_is_rated = true;
                                                           save_Rate_Settings();
                                                           dialog.cancel();
                                                           rate_is_show = false; }

});

final AlertDialog dialog = builder.create();

///////////////////////////////////////////////////////////////////////////////////////////////////

dialog.setOnShowListener(new DialogInterface.OnShowListener() {

    @Override
    public void onShow (DialogInterface anInterface) {

        ((Button)(dialog.getButton(Dialog.BUTTON_POSITIVE))).setTextSize(dialog_button_size);
        ((Button)(dialog.getButton(Dialog.BUTTON_NEGATIVE))).setTextSize(dialog_button_size);
        ((Button)(dialog.getButton(Dialog.BUTTON_NEUTRAL))).setTextSize(dialog_button_size);

    }

});

///////////////////////////////////////////////////////////////////////////////////////////////////

dialog.setOnDismissListener(dialog_dimiss_listener);
dialog.show();

rate_is_show = true;
show_rate_dialog = false;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Оновлення списку новин

public static void refresh_News() {

error_view.setVisibility(View.GONE);

progress_text.setText(progress_title_1);
progress.show();

int y = date[2];
int m = date[1] + 1;
int d = date[0];

String url = "http://patrioty.org.ua/api/items-by-date.json?date=" + y + "-" +
             (m < 10 ? "0" + m : m) + "-" +
             (d < 10 ? "0" + d : d);

Log.e(TAG, url);

new Async_Tasks.Site_Parsing().execute(url);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Показ діалогового вікна вибору дати

public static void show_Calendar_Dialog() {

calendar_is_show = true;

Calendar now = Calendar.getInstance();
now.set(date[2], date[1], date[0]);
Calendar min = Calendar.getInstance();
min.set(2015, 10, 19);

DatePickerDialog date_picker_dialog = DatePickerDialog.newInstance(null,
                                      now.get(Calendar.YEAR),
                                      now.get(Calendar.MONTH),
                                      now.get(Calendar.DAY_OF_MONTH));

date_picker_dialog.setVersion(DatePickerDialog.Version.VERSION_2);
date_picker_dialog.setAccentColor(date_picker_background);
date_picker_dialog.setCancelColor(date_picker_text);
date_picker_dialog.setOkColor(date_picker_text);
date_picker_dialog.setMaxDate(Calendar.getInstance());
date_picker_dialog.setMinDate(min);

///////////////////////////////////////////////////////////////////////////////////////////////////

date_picker_dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {

    @Override
    public void onDateSet (DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (year != date[2] ||
            monthOfYear != date[1] ||
            dayOfMonth != date[0]) {

            date_change = true;
            calendar_is_show = false;

            date[0] = dayOfMonth;
            date[1] = monthOfYear;
            date[2] = year;

            set_Menu_Title();
            date_Set();

        }

    }
});

///////////////////////////////////////////////////////////////////////////////////////////////////

date_picker_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

    @Override
    public void onCancel(DialogInterface dialog) { calendar_is_show = false; }

});

///////////////////////////////////////////////////////////////////////////////////////////////////

date_picker_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

    @Override
    public void onDismiss(DialogInterface dialog) { calendar_is_show = false; }

});

///////////////////////////////////////////////////////////////////////////////////////////////////

date_picker_dialog.dismissOnPause(true);
date_picker_dialog.show(patriots.getFragmentManager(), null);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Збереження налаштувань про оцінювання програми

public static void save_Rate_Settings() {

settings_reader = patriots.getPreferences(MODE_PRIVATE);
settings_writer = settings_reader.edit();

settings_writer.putBoolean("app_is_rated", app_is_rated);
settings_writer.putInt("app_load_count", app_load_count);
settings_writer.commit();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Підготовка списку улюблених сторінок перед показом

public static void prepare_Favorite_List() {

News_Item news_item = null;
favorite_array.removeAll(favorite_array);
File[] folders = temp_folder_favor.listFiles(folder_filter);

if (folders.length == 0) { empty_view.setVisibility(View.VISIBLE); }
else                     { empty_view.setVisibility(View.GONE); }

for (int z = 0; z < folders.length; z++) {

    try {

        InputStream is = new FileInputStream(new File(folders[z].getAbsolutePath() + "/item.ini"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        news_item = new News_Item();

        news_item.set_Id(reader.readLine());
        news_item.set_Time(reader.readLine());
        news_item.set_Title(reader.readLine());
        news_item.set_Page_Url(reader.readLine());
        news_item.set_Preview_Url(reader.readLine());
        news_item.set_Date_Time_String(reader.readLine());

        reader.close();
        is.close();

        favorite_array.add(news_item);

    }

    catch (Exception e) {}

}

Collections.sort(favorite_array, Utilities.news_item_comparator);
favorite_adapter.notifyDataSetChanged();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод викликається коли нову дату встановлено

private static void date_Set() {

if (logo.getVisibility() == View.GONE && date_change) {

    Utilities.delete_Dir(temp_folder_prev);
    temp_folder_prev.mkdir();
    refresh_News();
    date_change = false;

}

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Показати Snackbar з повідомленням про перезавантаження

public static void show_Reboot_Snackbar() {

if (settings_change) { return; }

settings_change = true;
patriots.findViewById(R.id.snackbar_margin).setVisibility(View.VISIBLE);

SnackbarManager.show(Snackbar.with(patriots)
               .color(snackbar_background)
               .textColor(snackbar_text_color)
               .actionLabel(snackbar_command)
               .swipeToDismiss(false)
               .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
               .actionListener(snackbar_listener), patriots);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Показати діалогове вікно зміни числових значень

public static void show_Seekbar_Dialog (final int variant) {

seekbar_is_show = variant;

final View seekbar = LayoutInflater.from(patriots).inflate(R.layout.dialog_seekbar, null);

final SeekBar seekbar_base = (SeekBar) seekbar.findViewById(R.id.seekbar_base);
final TextView seekbar_title = (TextView) seekbar.findViewById(R.id.seekbar_title);

switch (variant) {

case 0: seekbar_base.setMax(60);
        seekbar_title.setText(String.format(settings_text_font_size[0], seekbar_font));
        seekbar_base.setProgress(seekbar_font - 70);
        break;

case 1: seekbar_base.setMax(100);
        seekbar_title.setText(String.format(settings_text_quality[0], seekbar_quality));
        seekbar_base.setProgress(seekbar_quality);
        break;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

seekbar_base.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

@Override
public void onStartTrackingTouch (SeekBar seekBar) {}

@Override
public void onStopTrackingTouch (SeekBar seekBar) {}

@Override
public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {

switch (variant) {

case 0: settings_font_size = progress + 70;
        seekbar_title.setText(String.format(settings_text_font_size[0], settings_font_size));
        seekbar_base.setProgress(settings_font_size - 70);
        seekbar_font = settings_font_size;
        break;

case 1: settings_quality = progress;
        seekbar_title.setText(String.format(settings_text_quality[0], settings_quality));
        seekbar_base.setProgress(settings_quality);
        seekbar_quality = settings_quality;
        break;

}

}
});

///////////////////////////////////////////////////////////////////////////////////////////////////

reset_Dialog();
dialog_title.setText(variant == 0 ? R.string.seekbar_title_1 : R.string.seekbar_title_2);

AlertDialog.Builder builder = new AlertDialog.Builder(patriots);
builder.setCustomTitle(dialog_title_view);
builder.setView(seekbar);
builder.setPositiveButton(R.string.dialog_ok, null);

final AlertDialog dialog = builder.create();

dialog.setOnShowListener(new DialogInterface.OnShowListener() {

    @Override
    public void onShow (DialogInterface dihalog) {

        ((Button)(dialog.getButton(Dialog.BUTTON_POSITIVE))).setTextSize(dialog_button_size);

    }

});

///////////////////////////////////////////////////////////////////////////////////////////////////

dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

    @Override
    public void onDismiss(DialogInterface dialog) { seekbar_is_show = -1;
                                                    settings_font_size = seekbar_font;
                                                    settings_quality = seekbar_quality;
                                                    save_Settings();
                                                    set_Settings_Strings(); }

});

///////////////////////////////////////////////////////////////////////////////////////////////////

dialog.show();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Показ діалогового вікна відновлення стандартних налаштувань

public static void show_Repair_Dialog() {

repair_is_show = true;
AlertDialog.Builder builder = new AlertDialog.Builder(patriots);

reset_Dialog();
dialog_title.setText(R.string.repair_dialog_title);
dialog_message.setText(R.string.repair_dialog_text);

builder.setCustomTitle(dialog_title_view)
       .setView(dialog_message_view)
       .setPositiveButton(R.string.dialog_repair, new DialogInterface.OnClickListener() {

            @Override
            public void onClick (DialogInterface dialog, int which) {

                settings_theme       = default_settings[0];
                settings_language    = default_settings[1];
                settings_orientation = default_settings[2];
                settings_font_size   = default_settings[3];
                settings_preview     = default_settings[4];
                settings_quality     = default_settings[5];
                settings_link        = default_settings[6];
                settings_sociality   = default_settings[7];

                repair_is_show = false;

                save_Settings();
                set_Settings_Images();
                set_Settings_Strings();

                patriots.recreate();

            }

        })

       .setNegativeButton(R.string.dialog_back, null);

///////////////////////////////////////////////////////////////////////////////////////////////////

final AlertDialog dialog = builder.create();

dialog.setOnShowListener(new DialogInterface.OnShowListener() {

    @Override
    public void onShow (DialogInterface anInterface) {

        ((Button)(dialog.getButton(Dialog.BUTTON_POSITIVE))).setTextSize(dialog_button_size);
        ((Button)(dialog.getButton(Dialog.BUTTON_NEGATIVE))).setTextSize(dialog_button_size);

    }

});

dialog.setOnDismissListener(dialog_dimiss_listener);
dialog.show();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Показ діалогового вікна улюблених сторінок

public static void show_Info_Dialog() {

info_is_show = true;
AlertDialog.Builder builder = new AlertDialog.Builder(patriots);

String info = String.format(patriots.getResources().getString(R.string.info_dialog_text),
              favorite_array.size(), get_Folder_Size(temp_folder_favor)/1024/1024f);

reset_Dialog();
dialog_title.setText(R.string.info_dialog_title);
dialog_message.setText(info);

builder.setCustomTitle(dialog_title_view)
       .setView(dialog_message_view)
       .setPositiveButton(R.string.dialog_clean, new DialogInterface.OnClickListener() {

            @Override
            public void onClick (DialogInterface dialog, int which) {

                info_is_show = false;

                delete_Dir(temp_folder_favor);
                temp_folder_favor.mkdir();

                save_Settings();
                patriots.recreate();

            }

        })
       .setNegativeButton(R.string.dialog_back, null);

final AlertDialog dialog = builder.create();

dialog.setOnShowListener(new DialogInterface.OnShowListener() {

    @Override
    public void onShow (DialogInterface anInterface) {

        ((Button)(dialog.getButton(Dialog.BUTTON_POSITIVE))).setTextSize(dialog_button_size);
        ((Button)(dialog.getButton(Dialog.BUTTON_NEGATIVE))).setTextSize(dialog_button_size);

    }

});

dialog.setOnDismissListener(dialog_dimiss_listener);
dialog.show();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}
