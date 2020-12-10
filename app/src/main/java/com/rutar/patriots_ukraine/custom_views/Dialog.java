package com.rutar.patriots_ukraine.custom_views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.prolificinteractive.materialcalendarview.*;
import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.utils.Utility;

import java.util.*;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

public class Dialog {

public static final int Dialog_Rate             = 0;
public static final int Dialog_Share            = 1;
public static final int Dialog_Open_With        = 2;
public static final int Dialog_Date_Picker      = 3;
public static final int Dialog_Favorite_Info    = 4;
public static final int Dialog_Share_Web_View   = 5;
public static final int Dialog_Default_Settings = 6;

private static TextView dialog_header;
private static LinearLayout dialog_inside;
private static RelativeLayout dialog_layout;

public static GradientDrawable drawable_header;
public static GradientDrawable drawable_inside;

private static AppCompatDialog dialog;
public static ImageView share_loading;


public static int[] tmp_date = new int[3];
public static ArrayList <Intent> extra_intents;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Ініціалізація компонентів, нобхідних для діалогових вікон

public static void init_Dialog (Patriots_Ukraine app) {

TypedValue typedValue = new TypedValue();
Resources.Theme theme = app.getTheme();

theme.resolveAttribute(R.attr.color_background, typedValue, true);
int color_1 = typedValue.data;

theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
int color_2 = typedValue.data;

float radius = app.getResources().getDimension(R.dimen.dialog_corner_radius);
int stroke = (int) app.getResources().getDimension(R.dimen.stroke_width_positive);

drawable_header = new GradientDrawable();
drawable_header.setShape(GradientDrawable.RECTANGLE);
drawable_header.setCornerRadii(new float[] { radius, radius, radius, radius, 0, 0, 0, 0 });
drawable_header.setColor(color_2);

drawable_inside = new GradientDrawable();
drawable_inside.setShape(GradientDrawable.RECTANGLE);
drawable_inside.setCornerRadii(new float[] { 0, 0, 0, 0, radius, radius, radius, radius });
drawable_inside.setColor(color_1);
drawable_inside.setStroke(stroke, color_2);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Створення діалогового вікна

@SuppressLint("ResourceType")
public static void create_Dialog (final Patriots_Ukraine app) {

dialog_layout = (RelativeLayout) app.getLayoutInflater().inflate(R.layout.layout_dialog, null);
dialog_inside = dialog_layout.findViewById(R.id.dialog_inside);
dialog_header = dialog_layout.findViewById(R.id.dialog_title);

dialog_inside.setBackgroundDrawable(drawable_inside);
dialog_header.setBackgroundDrawable(drawable_header);

View dialog_context = null;

switch (vars.dialog_index) {

    // Оцінити програму
    case Dialog_Rate:             dialog_context = create_Rate_View(app);            break;

    // Вибір програми для того щоб поділитися новиною
    case Dialog_Share:            dialog_context = create_Share_View(app);           break;

    // Відкрити у програмі
    case Dialog_Open_With:        dialog_context = create_Open_With_View(app);       break;

    // Діалог вибору дати
    case Dialog_Date_Picker:      dialog_context = create_Date_Picker_View(app);     break;

    // Інформація про улюблені вкладки
    case Dialog_Favorite_Info:    dialog_context = create_Favorite_Info_View(app);   break;

    // Діалогове вікно браузера, аби ділитися новиною
    case Dialog_Share_Web_View:   dialog_context = create_Share_Web_View(app);       break;

    // Діалогове вікно браузера, аби ділитися новиною
    case Dialog_Default_Settings: dialog_context = create_Restore_Settings_View(app); break;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

dialog_inside.addView(dialog_context);

dialog = new AppCompatDialog(app);
dialog.setContentView(dialog_layout);

dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

    @Override
    public void onDismiss (DialogInterface dialog) { vars.dialog_index = -1; }

});

dialog.getWindow().setBackgroundDrawable(app.getResources()
                  .getDrawable(R.xml.dialog_shape));

dialog.show();
Window window = dialog.getWindow();

switch (vars.dialog_index) {

    // WebView
    case Dialog_Share_Web_View: window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                                 ViewGroup.LayoutParams.WRAP_CONTENT);
                                break;
    // Інші діалогові вікна
    default: window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                              ViewGroup.LayoutParams.WRAP_CONTENT);
             break;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Отримання діалогового вікна

public static AppCompatDialog get_Dialog() { return dialog; }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Знищення діалогового вікна

public static void dismiss_Dialog() {

if (dialog != null) { dialog.dismiss(); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Вікно "поділитися новиною"

private static View create_Share_View (final Patriots_Ukraine app) {

String title = "";

switch (vars.share_index) {

    case 0: title = Utility.get_String(R.string.dialog_title_share,
                    Utility.get_String(R.string.dialog_title_share_f)); break;
    case 1: title = Utility.get_String(R.string.dialog_title_share,
                    Utility.get_String(R.string.dialog_title_share_t)); break;

}

dialog_header.setText(title);
View dialog_content = app.getLayoutInflater().inflate(R.layout.layout_dialog_content, null);

((TextView)dialog_content.findViewById(R.id.button_negative)).setText(Utility.get_String(R.string.dialog_back));

dialog_content.findViewById(R.id.dialog_message).setVisibility(View.GONE);
dialog_content.findViewById(R.id.button_neutral).setVisibility(View.GONE);
dialog_content.findViewById(R.id.button_positive).setVisibility(View.GONE);

ListView list = (ListView) dialog_content.findViewById(R.id.dialog_list);
list.setVisibility(View.VISIBLE);

// ................................................................................................

String[] texts = new String[extra_intents.size()];
Drawable[] images = new Drawable[extra_intents.size()];

for (int z = 0; z < extra_intents.size(); z++) {

    List <ResolveInfo> res_info = app.getPackageManager()
                                     .queryIntentActivities(extra_intents.get(z), 0);

    if (!res_info.isEmpty()) {

        try {

            ResolveInfo info = res_info.get(0);
            texts[z] = info.activityInfo.loadLabel(app.getPackageManager()).toString();

            // Змінюємо назви інтентів
            switch (info.activityInfo.packageName) {
                case "com.facebook.katana":      texts[z] = "Facebook";      break;
                case "com.facebook.lite":        texts[z] = "Facebook Lite"; break;
                case "com.twitter.android":      texts[z] = "Twitter";       break;
                case "com.twitter.android.lite": texts[z] = "Twitter Lite";  break;
            }

            images[z] = app.getPackageManager().getApplicationIcon(info.activityInfo.packageName);

        }

        catch (Exception e) {  }
    }

    if (extra_intents.get(z).getStringExtra("app") != null) {

        texts[z] = Utility.get_String(R.string.app_name);
        images[z] = app.getResources().getDrawable(R.mipmap.icon);

    }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

ArrayList <Map <String, Object>> data = new ArrayList <Map <String, Object>>();
Map <String, Object> map;

for (int i = 0; i < texts.length; i++) {

    map = new HashMap <String, Object>();
    map.put("text", texts[i]);
    map.put("image", images[i]);
    data.add(map);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

SimpleAdapter sAdapter = new SimpleAdapter(app, data,
                                           R.layout.layout_share_item,
                                           new String[] { "text", "image" },
                                           new int[] { R.id.share_text, R.id.share_image });

sAdapter.setViewBinder(new My_View_Binder());
list.setAdapter(sAdapter);

///////////////////////////////////////////////////////////////////////////////////////////////////

list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    @Override
    public void onItemClick (AdapterView<?> parent, View view, final int position, long id) {

        final Intent intent = extra_intents.get(position);
        vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN);

        if (intent.getStringExtra("app") != null) {

            Dialog.dismiss_Dialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    vars.URL = intent.getStringExtra("url");
                    vars.dialog_index = Dialog_Share_Web_View;
                    Dialog.create_Dialog(app);

                }

            }, 500);
        }

        else {

            app.startActivity(intent);
            Dialog.dismiss_Dialog();

        }

    }

});

return dialog_content;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Вікно "відкрити за допомогою"

private static View create_Open_With_View (final Patriots_Ukraine app) {

View result = create_Share_View(app);
dialog_header.setText(Utility.get_String(R.string.dialog_title_open_in_browser));

return result;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Вікно "вибір дати"

private static View create_Date_Picker_View (final Patriots_Ukraine app) {

dialog_header.setText(Utility.get_String(R.string.dialog_title_date_picker));

View dialog_content = app.getLayoutInflater().inflate(R.layout.layout_dialog_content, null);

((TextView)dialog_content.findViewById(R.id.button_positive)).setText(Utility.get_String(R.string.dialog_goto));
((TextView)dialog_content.findViewById(R.id.button_negative)).setText(Utility.get_String(R.string.dialog_back));

dialog_content.findViewById(R.id.dialog_message).setVisibility(View.GONE);
dialog_content.findViewById(R.id.button_neutral).setVisibility(View.GONE);
dialog_content.findViewById(R.id.calendar_view).setVisibility(View.VISIBLE);

MaterialCalendarView calendar = (MaterialCalendarView)
                                 dialog_content.findViewById(R.id.calendar_view);

calendar.state().edit()
        .setFirstDayOfWeek(Calendar.MONDAY)
        .setMinimumDate(CalendarDay.from(2015, 10, 19))
        .setMaximumDate(new Date(System.currentTimeMillis()))
        .setCalendarDisplayMode(CalendarMode.MONTHS)
        .commit();

CalendarDay current_date = CalendarDay.from(vars.date[2], vars.date[1], vars.date[0]);

calendar.setCurrentDate(current_date);
calendar.setDateSelected(current_date, true);
calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

if (vars.theme_now == 2 ||
    vars.theme_now == 9) { calendar.setDateTextAppearance(R.style.Date_Picker_Dark); }
else                         { calendar.setDateTextAppearance(R.style.Date_Picker_Light); }

///////////////////////////////////////////////////////////////////////////////////////////////////

calendar.setOnDateChangedListener(new OnDateSelectedListener() {
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget,
                               @NonNull CalendarDay date, boolean selected) {

        tmp_date[0] = date.getDay();
        tmp_date[1] = date.getMonth();
        tmp_date[2] = date.getYear();

    }
});

///////////////////////////////////////////////////////////////////////////////////////////////////

return dialog_content;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Вікно "інформація про улюблені сторінки"

private static View create_Favorite_Info_View (final Patriots_Ukraine app) {

dialog_header.setText(Utility.get_String(R.string.dialog_favorite_pages));

View dialog_content = app.getLayoutInflater().inflate(R.layout.layout_dialog_content, null);

String info = String.format(Utility.get_String(R.string.dialog_message_info),
                            vars.favor_array.size(),
                            Utility.get_Folder_Size(vars.folder_favor)/1024/1024f);

((TextView)dialog_content.findViewById(R.id.button_positive)).setText(Utility.get_String(R.string.dialog_clean));
((TextView)dialog_content.findViewById(R.id.button_negative)).setText(Utility.get_String(R.string.dialog_back));
((TextView)dialog_content.findViewById(R.id.dialog_message)).setText(info);

dialog_content.findViewById(R.id.button_neutral).setVisibility(View.GONE);

return dialog_content;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Вікно "відновити стандартні налаштування"

private static View create_Restore_Settings_View (final Patriots_Ukraine app) {

dialog_header.setText(Utility.get_String(R.string.dialog_title_repair));

View dialog_content = app.getLayoutInflater().inflate(R.layout.layout_dialog_content, null);

((TextView)dialog_content.findViewById(R.id.button_positive)).setText(Utility.get_String(R.string.dialog_repair));
((TextView)dialog_content.findViewById(R.id.button_negative)).setText(Utility.get_String(R.string.dialog_back));
((TextView)dialog_content.findViewById(R.id.dialog_message)).setText(Utility.get_String(R.string.dialog_message_restore));

dialog_content.findViewById(R.id.button_neutral).setVisibility(View.GONE);

return dialog_content;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Вікно "оцінити програму"

private static View create_Rate_View (final Patriots_Ukraine app) {

dialog_header.setText(Utility.get_String(R.string.dialog_favorite_pages));

View dialog_content = app.getLayoutInflater().inflate(R.layout.layout_dialog_content, null);

((TextView)dialog_content.findViewById(R.id.button_positive)).setText(Utility.get_String(R.string.dialog_repair));
((TextView)dialog_content.findViewById(R.id.button_negative)).setText(Utility.get_String(R.string.dialog_back));
((TextView)dialog_content.findViewById(R.id.dialog_message)).setText(Utility.get_String(R.string.dialog_message_rate));

dialog_content.findViewById(R.id.button_neutral).setVisibility(View.GONE);

return dialog_content;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Вікно браузера, аби поділитися новиною

private static View create_Share_Web_View (final Patriots_Ukraine app) {

String title = "";

switch (vars.share_index) {

    case 0: title = Utility.get_String(R.string.dialog_title_share,
            Utility.get_String(R.string.dialog_title_share_f)); break;
    case 1: title = Utility.get_String(R.string.dialog_title_share,
            Utility.get_String(R.string.dialog_title_share_t)); break;

}

dialog_header.setText(title);
View dialog_content = app.getLayoutInflater().inflate(R.layout.layout_dialog_share, null);

share_loading = dialog_content.findViewById(R.id.share_loading);

Web_Share_View web_view = dialog_content.findViewById(R.id.share_web_view);
web_view.setWebViewClient(web_view.get_Custom_Web_View_Client());
web_view.setBackgroundColor(vars.color_background);
web_view.getSettings().setJavaScriptEnabled(true);
web_view.loadUrl(vars.URL);

return dialog_content;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Додатковий елемент, необхідний для діалогових вікон

private static class My_View_Binder implements SimpleAdapter.ViewBinder {

@Override
public boolean setViewValue (View view, Object data, String textRepresentation) {

if ((view instanceof ImageView) & (data instanceof Drawable)) {

    ImageView image_view = (ImageView) view;
    Drawable drawable = (Drawable) data;
    image_view.setImageDrawable(drawable);
    return true;

}

return false;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

}