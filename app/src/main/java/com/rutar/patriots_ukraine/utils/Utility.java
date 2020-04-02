package com.rutar.patriots_ukraine.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.*;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rutar.patriots_ukraine.Variables;
import com.rutar.patriots_ukraine.other.News_Item;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.Settings;
import com.rutar.patriots_ukraine.custom_views.Dialog;
import com.rutar.patriots_ukraine.listeners.Anim_Listener;
import com.rutar.patriots_ukraine.other.Recycler_Adapter;
import com.rutar.patriots_ukraine.process.Process_Find_News;
import com.rutar.patriots_ukraine.process.Process_Preview;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;
import static com.rutar.patriots_ukraine.Variables.*;

public class Utility {

private static Resources res = null;
private static DateFormat date_format = new SimpleDateFormat("dd-MM-yyyy-hh:mm");

// ................................................................................................

private static int snackbar_show_time = 2500;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Перехід на сайт за посиланням

public static void go_To_Site (String url) {

Intent intent = null;
Dialog.extra_intents = new ArrayList<>();

if (url != null) { intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); }
else             { intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://patrioty.org.ua")); }

///////////////////////////////////////////////////////////////////////////////////////////////////

List <ResolveInfo> res_info = app.getPackageManager().queryIntentActivities(intent, 0);

if (!res_info.isEmpty()) {

    for (ResolveInfo info : res_info) {

        Intent target_intent = null;

        if (url != null) { target_intent = new Intent(Intent.ACTION_VIEW,
                           Uri.parse(url)); }

        else             { target_intent = new Intent(Intent.ACTION_VIEW,
                           Uri.parse("http://patrioty.org.ua")); }

        target_intent.setPackage(info.activityInfo.packageName);
        Dialog.extra_intents.add(target_intent);

    }
}

// Якщо відповідних програм немає - показуємо повідомлення про помилку
if (Dialog.extra_intents.isEmpty()) { vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN);
                                      Utility.show_Snack_Bar(Utility.get_String(R.string.error_apps_not_found), false); }

// В іншому випадку показуємо діалогове вікно вибору програми
else { vars.dialog_index = Dialog.Dialog_Open_With;
       Dialog.create_Dialog(app); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
//

public static void set_Toolbar_Title() {

if (!vars.search_text.isEmpty()) { vars.toolbar_title.setText(String
                                                     .format("\"%s\"", vars.search_text));
                                  return; }

vars.toolbar_title.setText(Utility.get_String(R.string.dialog_title_toolbar,
                                              "" + vars.date[0],
                                              "" + vars.month_names[vars.date[1]],
                                              "" + vars.date[2]));

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод дозволяє поділитися новиною у соціальних мережах

public static void share_News (int index) {

final Intent share_intent = new Intent();
share_intent.putExtra("app", "true");

switch (index) {

case 0: share_intent.putExtra("url", "https://www.facebook.com/sharer.php?u="
                                    + vars.news_item.get_Page_Url()); break;

case 1: share_intent.putExtra("url", "https://twitter.com/intent/tweet?text="
            + vars.news_item.get_Title() + "%20" + vars.news_item.get_Page_Url()
            + "&related=AddToAny,micropat"); break;

}

Dialog.extra_intents = Utility.get_Share_Intents(index);
Dialog.extra_intents.add(share_intent);
vars.share_index = index;

// Якщо немає додаткових програм - відкриваємо web_view
if (Dialog.extra_intents.size() == 1) {

Dialog.dismiss_Dialog();
vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN);

new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {

        vars.URL = share_intent.getStringExtra("url");
        vars.dialog_index = Dialog.Dialog_Share_Web_View;
        Dialog.create_Dialog(app);

    }

}, 500);
}

// Якщо є інші програми - показуємо діалогове вікно вибору
else { vars.dialog_index = Dialog.Dialog_Share;
       Dialog.create_Dialog(app); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Виділення вибраного пункту меню

public static void set_Menu_Selected (int id) {

if (id == R.id.logo_menu) { return; }

vars.app_menu_index = id;

vars.menu_news     .setBackgroundColor(vars.color_primary_dark);
vars.menu_favorite .setBackgroundColor(vars.color_primary_dark);
vars.menu_settings .setBackgroundColor(vars.color_primary_dark);
vars.menu_about    .setBackgroundColor(vars.color_primary_dark);
vars.menu_exit     .setBackgroundColor(vars.color_primary_dark);

switch (vars.app_menu_index) {

case R.id.menu_news:     vars.menu_news     .setBackgroundColor(vars.color_primary); break;
case R.id.menu_favorite: vars.menu_favorite .setBackgroundColor(vars.color_primary); break;
case R.id.menu_settings: vars.menu_settings .setBackgroundColor(vars.color_primary); break;
case R.id.menu_about:    vars.menu_about    .setBackgroundColor(vars.color_primary); break;
case R.id.menu_exit:     vars.menu_exit     .setBackgroundColor(vars.color_primary); break;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Пошук новин за запитом

public static void search_News() {

//vars.running_anim.add("search_news");
vars.input_lock = true;

final String search_world = vars.search_text_field.getText().toString();
final String search_text = Utility.get_String(R.string.search_word, search_world);

vars.search_text = search_world;

Handler handler = new Handler();
handler.postDelayed(new Runnable() {
    @Override
    public void run() {

        vars.swipe_refresh.setEnabled(false);
        vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN);
        vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);

        Utility.show_Snack_Bar(search_text, true);
        new Process_Find_News().execute(search_world);

    }
}, 1000);
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод завантажує зображення з файлової системи

public static Bitmap load_Bitmap_From_Storage (String name, File path)  {

try { File f = new File(path == null ? vars.folder_prev : path, name);
      return BitmapFactory.decodeStream(new FileInputStream(f)); }

catch (FileNotFoundException e) { Utility.print_Stack_Trace("Load Image Error", e);
                                  return null; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод зберігає зображення у файлову систему

public static void save_Image_To_Storage (String link, String path) throws Exception {

if (link.endsWith("noImage.png")) { return; }

try {

if (new File(vars.folder_prev, Utility.process_Name(link)).exists()) {

Bitmap bitmap = Utility.load_Bitmap_From_Storage(Utility.process_Name(link), null);

File file = new File(path);
if (!file.exists()) { file.mkdir(); }

OutputStream os = new BufferedOutputStream(new FileOutputStream(path + "/" + process_Name(link)));

bitmap.compress(Bitmap.CompressFormat.JPEG, vars.image_quality, os);
os.close();

}
}

catch (Exception e) { Log.e(vars.TAG, "Save image to storage Error"); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод завантажує файли по їх посиланню

public static Bitmap save_File_To_Storage (String link, String path) {

// Якщо файл - зображення jpg або png, то перед збереженням він стискається

Bitmap bitmap = null;
InputStream is = null;
OutputStream os = null;

if (!Utility.is_Online(app)) { return null; }

if (link.endsWith(".jpg") ||
    link.endsWith(".jpeg") ||
    link.endsWith(".png")) {

try {

    URL url = new URL(link);

    is = url.openStream();
    bitmap = BitmapFactory.decodeStream(is);

    //BitmapFactory.decode

    is.close();

    File file = new File(path);
    if (!file.exists()) { file.mkdir(); }

    os = new BufferedOutputStream(new FileOutputStream(path + "/" + process_Name(link)));

    bitmap.compress(Bitmap.CompressFormat.JPEG, vars.image_quality, os);
    os.close();

}

catch (Exception e) { bitmap = null;
                      Log.e(vars.TAG, "Download Error: " + e.fillInStackTrace()); }

finally { if (is != null) { try { is.close(); } catch (Exception e) {} }
          if (os != null) { try { os.close(); } catch (Exception e) {} } }

return bitmap;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо файл не є jpg або png зображенням - зберігаємо оригінальний файл

else {

try {

    File file = new File(path);
    if (!file.exists()) { file.mkdir(); }

    URL url = new URL(link);
    is = url.openStream();
    os = new BufferedOutputStream(new FileOutputStream(path + "/" + process_Name(link)));

    for (int b; (b = is.read()) != -1; ) { os.write(b); }

    os.close();
    is.close();

}

catch (Exception e)  { Log.e(vars.TAG, "Download Error: " + e.fillInStackTrace()); }

finally { if (is != null) { try { is.close(); } catch (Exception e) {} }
          if (os != null) { try { os.close(); } catch (Exception e) {} } }

return null;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void save_Video_Preview_To_Storage (Bitmap image, File file) {

try {

OutputStream os = new BufferedOutputStream(new FileOutputStream(file));

image.compress(Bitmap.CompressFormat.JPEG, vars.image_quality, os);
os.close();

}

catch (Exception e) { Log.e(vars.TAG, "Save image video preview Error"); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Перезаписування прев'ю відповідно до теми

public static void create_Video_Preview_Image (File file) {

String name = file.getName();
File path = new File(file.getParentFile().getPath());

// Завантаження прев'ю
Bitmap bitmap = Utility.load_Bitmap_From_Storage(name, path);

// Оброблення прев'ю
bitmap = Utility.process_Video_Preview(bitmap);

// Зберігання прев'ю
Utility.save_Video_Preview_To_Storage(bitmap, new File(path, "proc_" + name));

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static Bitmap get_Preview_Bitmap (Process_Preview.Queue_Object object) {

Bitmap result = null; // Результат обробки

// ................................................................................................

try {

String download_url = object.get_Object_Url();

// ................................................................................................
// Посилання яке починається із символів http:// позначає прев'ю, яке необхідно завантажити

if (download_url.startsWith("http://")) {

// ................................................................................................
// Якщо прев'ю у кеші - дістаємо його

if (new File(vars.folder_prev, Utility.process_Name(download_url)).exists()) {
    result = Utility.load_Bitmap_From_Storage(Utility.process_Name(download_url), null);
}

// ................................................................................................
// Якщо посилання закінчується символами "noImage.png", то стаття не має прев'ю

else if (download_url.endsWith("noImage.png")) { object.set_Preview_Not_Exist(true); }

// ................................................................................................
// Якщо зображення немає в кеші, то його необхідно завантажити по заданому url

else {

    try { result = Utility.save_File_To_Storage(download_url,
                   vars.folder_prev.getAbsolutePath()); }
    catch (Exception e) { Utility.print_Stack_Trace("Preview load error", e); }

     }
}

// ................................................................................................
// Посилання some_folder/some_preview позначає прев'ю улюбленої сторінки

else { result = Utility.load_Bitmap_From_Storage(download_url,
                new File(vars.folder_favor.getAbsolutePath())); }

}

// ................................................................................................
// У разі помилки bitmap стає зображенням im_preview_not_found

catch (Exception e) { result = null;
                      Utility.print_Stack_Trace("Download preview error", e); }

return result;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод отримує ім'я файлу із його посилання

public static String process_Name (String link) {

boolean is_video_preview = false;
if (link.contains("youtube.com/")) { is_video_preview = true; }

// ................................................................................................

String file_name = link.substring(link.lastIndexOf("/") + 1);

String tmp = link.substring(0, link.lastIndexOf("/"));
tmp = tmp.substring(tmp.lastIndexOf("/") + 1);

if (is_video_preview) { tmp = "video_prev_" + tmp; }

file_name = tmp + "_" + file_name;

int index = file_name.indexOf("?");
if (index != -1) { file_name = file_name.substring(0, index); }

return file_name;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Отримання xml ім'я ресурса

public static String get_Resource_Name (int res) {

try                 { return app.getResources().getResourceEntryName(res); }
catch (Exception e) { return  "custom, code " + res; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Отримання кольору в шістнадцятковому строковому форматі

public static String get_Color (int attribute) {

return "#" + Integer.toHexString(attribute).substring(2);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод видаляє папки разом із вкладеними файлами

public static void delete_Dir (File file) {

File[] contents = file.listFiles();

if (contents != null) { for (File f : contents) { delete_Dir(f); } }
file.delete();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод отримує розмір папки

public static float get_Folder_Size (File folder) {

long length = 0;
File[] files = folder.listFiles();

int count = files.length;

for (int i = 0; i < count; i++) {

if (files[i].isFile()) { String name = files[i].getName();
                         if (!name.equals("site.css")) { length += files[i].length(); } }

else { length += get_Folder_Size(files[i]); } }

return length;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Отримання id im_youtube відео за його посиланням

public static String get_Youtube_Video_Id (String url) {

try {

    if (!url.contains("http")) { url = "http:" + url; }
    if (url.contains("?")) { url = url.substring(0, url.lastIndexOf("?")); }

    if (url.contains("embed")) { url = url.substring(url.lastIndexOf("/") + 1); }
    else                       { url = url.substring(url.lastIndexOf("=") + 1); }

    return url;

}

catch (Exception e) { return "error"; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Редагування прев'ю до im_youtube відео

public static Bitmap process_Video_Preview (Bitmap bitmap) {

Drawable drawable = ContextCompat.getDrawable(app, R.drawable.im_youtube);

if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
    drawable = (DrawableCompat.wrap(drawable)).mutate();
}

// ................................................................................................

float scale = 0.25f;

float width  = bitmap.getWidth()  * scale;
float height = bitmap.getHeight() * scale;

float scale_factor = width > height ? width / drawable.getIntrinsicWidth() :
                                      height / drawable.getIntrinsicHeight();

int result_w = (int)(drawable.getIntrinsicWidth() * scale_factor);
int result_h = (int)(drawable.getIntrinsicHeight() * scale_factor);

Bitmap youtube_button = Bitmap.createBitmap(result_w, result_h, Bitmap.Config.ARGB_8888);

Canvas canvas = new Canvas(youtube_button);
drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
drawable.draw(canvas);

// ................................................................................................

bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

canvas = new Canvas(bitmap);
Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

canvas.drawBitmap(youtube_button,
                  bitmap.getWidth()/2 - youtube_button.getWidth()/2,
                  bitmap.getHeight()/2 - youtube_button.getHeight()/2, paint);

return bitmap;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Показати snack_bar

public static void show_Snack_Bar (final String text, boolean indefinite) {

vars.behavior_snack_bar_text.setText(text);
vars.behavior_snack_bar.setState(BottomSheetBehavior.STATE_COLLAPSED);

if (indefinite) { return; }

Handler handler = new Handler();
handler.postDelayed(new Runnable() {

    @Override
    public void run() { Utility.dismiss_Snack_Bar(); }

}, snackbar_show_time);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Сховати snack_bar

public static void dismiss_Snack_Bar() {

vars.behavior_snack_bar.setState(BottomSheetBehavior.STATE_HIDDEN);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Задати час відображення snack_bar'у

public static void set_Snack_Bar_Show_Time (int time) {

if (time > 0 && time < 10000) { snackbar_show_time = time; }
else                          { snackbar_show_time = 2500; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Відображення чи приховування елементів на web_behavior

public static void set_Web_Behavior_Context() {

int visibility = 0;

// ................................................................................................
// Faceboock

if (vars.web_behavior_f) { visibility = View.VISIBLE; }
else                         { visibility = View.GONE;    }

app.findViewById(R.id.share_facebook).setVisibility(visibility);

// ................................................................................................
// Twitter

if (vars.web_behavior_t) { visibility = View.VISIBLE; }
else                         { visibility = View.GONE;    }

app.findViewById(R.id.share_twitter).setVisibility(visibility);

// ................................................................................................
// Посилання на новину

if (vars.web_behavior_l) { visibility = View.VISIBLE; }
else                            { visibility = View.GONE;    }

app.findViewById(R.id.news_link).setVisibility(visibility);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Підготовка списку новин перед показом

public static void prepare_News_List() {

//app.news_adapter = new List_Adapter(app, Settings.news_array);
vars.news_adapter = new Recycler_Adapter(vars.news_array);
vars.news_list.setAdapter(vars.news_adapter);
vars.news_list.setVisibility(View.VISIBLE);

// Якщо мало новин, то показуємо кнопки "next" та "prev"
if ((vars.news_array != null && vars.news_array.size() < 4) &&
     vars.app_state == 1) { vars.behavior_list.setState(BottomSheetBehavior.STATE_COLLAPSED); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Підготовка списку улюблених сторінок перед показом

public static void prepare_Favorite_List() {

News_Item news_item = null;
vars.favor_array = new ArrayList<>();
File[] folders = vars.folder_favor.listFiles(new Folder_Filter());

vars.swipe_refresh.setEnabled(false);

if (folders.length == 0) { Animation animation = new AlphaAnimation(0, 1);
                           animation.setStartOffset(500);
                           animation.setDuration(700);
                           vars.error_list.setVisibility(View.VISIBLE);
                           vars.error_list.startAnimation(animation); }

else                     { vars.error_list.setVisibility(View.GONE); }

///////////////////////////////////////////////////////////////////////////////////////////////////

for (int z = 0; z < folders.length; z++) {

try {

    InputStream is = new FileInputStream(new File(folders[z].getAbsolutePath() + "/item.ini"));
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

    news_item = new News_Item();

    news_item.set_Id(reader.readLine().substring(3));
    news_item.set_Time(reader.readLine().substring(3));
    news_item.set_Title(reader.readLine().substring(3));
    news_item.set_Page_Url(reader.readLine().substring(3));
    news_item.set_Preview_Url(reader.readLine().substring(3));
    news_item.set_Milliseconds(reader.readLine().substring(3));

    reader.close();
    is.close();

    vars.favor_array.add(news_item);

}

catch (Exception e) { Utility.print_Stack_Trace("Prepare favorite list Error", e); }

}

Collections.sort(vars.favor_array, Utility.news_item_comparator);
//app.news_adapter = new List_Adapter(app, Settings.favor_array);
vars.news_adapter = new Recycler_Adapter(vars.favor_array);
vars.news_list.setAdapter(vars.news_adapter);
vars.news_list.setVisibility(View.VISIBLE);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void reset_Settings_List() {
    //app.findViewById(R.id.layout_settings).scrollTo(0, 0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static boolean is_NavigationBar_Available() {

boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

return (!(hasBackKey && hasHomeKey));

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void zoom_In_Web_View() {

for (int z = 0; z < 3; z++) { vars.web_view.zoomIn(); }

// do { app.web_view.zoomIn(); }
// while (app.web_view.canZoomIn());

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void zoom_Out_Web_View() {

do { vars.web_view.zoomOut(); }
while (vars.web_view.canZoomOut());

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void app_Is_Updated() {

convert_Old_Favorite_Items();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
//

public static void convert_Old_Favorite_Items() {

File file = null;
File[] files = vars.folder_favor.listFiles();

ArrayList <File> list = new ArrayList<>();

// ................................................................................................
// Складаємо список файлів, які необхідно обробити

for (int z = 0; z < files.length; z++) {

try { file = files[z];
      if (file.isDirectory()) { list.add(new File(file, "item.ini")); } }

catch (Exception e) { Log.e(Variables.TAG, "Get favorite file item.ini Error"); }

}

// ................................................................................................
// Переробляємо застарілі елементи

for (int q = 0; q < list.size(); q++) {

try {

file = list.get(q);

InputStream is = new FileInputStream(file);
BufferedReader reader = new BufferedReader(new InputStreamReader(is));

String id = reader.readLine();
String time = reader.readLine();
String title = reader.readLine();
String page_url = reader.readLine();
String preview_url = reader.readLine();
String milliseconds = reader.readLine();

reader.close();
is.close();

if (id.startsWith("1)")) { continue; }

id           = "1) " + id;
time         = "2) " + time;
title        = "3) " + title;
page_url     = "4) " + page_url;
preview_url  = "5) " + preview_url;
milliseconds = "6) " + Utility.get_News_Time(time.substring(3));

// ................................................................................................
// Записуємо результат у новий файл

String ini = id + "\n" +                                                                      // ID
             time + "\n" +                                                             // Time text
             title + "\n" +                                                                // Title
             page_url + "\n" +                                                          // Page URL
             preview_url + "\n" +                                                    // Preview URL
             milliseconds;                                                          // Milliseconds

FileWriter writer = new FileWriter(file);

writer.write(ini);
writer.flush();
writer.close();

Log.i(vars.TAG, "Rewrite item " + id.substring(3));

}

catch (Exception e) { Log.e(vars.TAG, "Rewrite item.ini Error"); }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Даний клас слугує фільтром папок і файлів

public static class Folder_Filter implements FilenameFilter {

@Override
public boolean accept (File directory, String file_name) {

    if (file_name.contains("item-")) { return true;  }
    else                             { return false; }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Компаратор порівнює дві дати

public static Comparator <News_Item> news_item_comparator = new Comparator <News_Item>() {

@Override
public int compare (News_Item o1, News_Item o2) {

try {

    Date date_1 = new Date(Long.parseLong(o1.get_Milliseconds()));
    Date date_2 = new Date(Long.parseLong(o2.get_Milliseconds()));

    if (vars.AZ_sort) { return date_2.compareTo(date_1); }
    else                  { return date_1.compareTo(date_2); }

}

catch (Exception e) { return 0; }

}
};

///////////////////////////////////////////////////////////////////////////////////////////////////

public static ArrayList <Intent> get_Share_Intents (int target) {

ArrayList <Intent> result = new ArrayList<Intent>();
PackageManager manager = app.getPackageManager();
Intent share = new Intent(Intent.ACTION_SEND);
share.setType("text/plain");

List <ResolveInfo> res_Info = manager.queryIntentActivities(share, 0);

if (!res_Info.isEmpty()){

for (ResolveInfo info : res_Info) {

    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    intent.setType("text/plain");

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Facebook

    if (target == 0 &&
       (info.activityInfo.packageName.contains("com.facebook.katana") ||           // facebook full
        info.activityInfo.packageName.contains("com.facebook.lite"))) {            // facebook lite

        intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
        intent.putExtra(Intent.EXTRA_TEXT, vars.news_item.get_Page_Url());

        result.add(intent);

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Twitter

    else if (target == 1 &&
           ((info.activityInfo.name.contains(".composer") &&
             info.activityInfo.packageName.contains("com.twitter.android")) ||      // Twitter Full
             info.activityInfo.packageName.contains("com.twitter.android.lite"))) { // Twitter Lite

        intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
        intent.putExtra(Intent.EXTRA_TEXT, vars.news_item.get_Page_Url());

        result.add(intent);

    }

}
}

return result;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void start_Anim (View view, int anim) {

Animation animation = null;

switch (anim) {

    // Анімація зникнення Тризуба
    case -1: animation = new AlphaAnimation(1, 0);
             animation.setStartOffset(600);
             animation.setDuration(900);
             break;

    //
    case -2: animation = new AlphaAnimation(0, 1);
             animation.setDuration(900);
             break;

    //
    default: animation = AnimationUtils.loadAnimation(app, anim);
             break;


}

animation.setAnimationListener(new Anim_Listener(view.getId(), anim));
view.startAnimation(animation);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Приховування toolbar елементів

public static void toolbar_Views_Hide() {

Utility.start_Anim(app.findViewById(R.id.toolbar_container), vars.toolbar_anim_hide);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Відображення toolbar елементів

public static void toolbar_Views_Show() {

switch (String.valueOf(vars.app_state)) {

case "1.0":
case "1.1": vars.toolbar_left.setImageResource(R.drawable.ic_menu);
            vars.toolbar_right.setImageResource(R.drawable.ic_search);
            Utility.set_Toolbar_Title();
            break;

case "2.0":
case "2.1": vars.toolbar_left.setImageResource(R.drawable.ic_menu);
            vars.toolbar_right.setImageResource(R.drawable.ic_news);
            if (vars.search_text.isEmpty())
                 { vars.toolbar_title.setText(Utility.get_String(R.string.dialog_title_search)); }
            else { vars.toolbar_title.setText(String.format("\"%s\"", vars.search_text)); }
            break;

case "3.0": vars.toolbar_left.setImageResource(R.drawable.ic_menu);
            vars.toolbar_right.setImageResource(R.drawable.ic_favorite_off);
            vars.toolbar_title.setText(Utility.get_String(R.string.dialog_favorite_pages));
            break;

case "4.0": vars.toolbar_left.setImageResource(R.drawable.ic_menu);
            vars.toolbar_right.setImageResource(R.drawable.ic_restore);
            vars.toolbar_title.setText(Utility.get_String(R.string.menu_settings));
            break;

case "5.0": vars.toolbar_left.setImageResource(R.drawable.ic_menu);
            vars.toolbar_right.setImageResource(R.drawable.ic_preview_on);
            vars.toolbar_title.setText(Utility.get_String(R.string.menu_about));
            break;

case "1.2":
case "2.2":
case "3.1": vars.toolbar_left.setImageResource(R.drawable.ic_back);
            vars.toolbar_right.setImageResource(vars.page_is_favorite ?
                R.drawable.ic_favorite_on :
                R.drawable.ic_favorite_off);
            vars.toolbar_title.setText(Utility.get_String(R.string.app_name));
            break;

}

Utility.start_Anim(app.findViewById(R.id.toolbar_container), vars.toolbar_anim_show);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void update_Drawer_State() {

switch (String.valueOf(vars.app_state)) {

    case "1.2": // Блокування Drawer'а при відображенні WebView
    case "2.2":
    case "3.1": vars.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;

    default: vars.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
             break;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static long get_News_Time (String date, String time) {

Calendar calendar = Calendar.getInstance();;

if (date == null) { date = "/all/" + new SimpleDateFormat("yyyy-MM-dd")
                                    .format(calendar.getTime()) + ".html"; }

String[] D = date.substring(date.indexOf("/", 1) + 1, date.lastIndexOf(".")).split("-");
String[] T = time.split(":");

calendar.set(Integer.valueOf(D[0]),     // Year
             Integer.valueOf(D[1]) - 1, // Month
             Integer.valueOf(D[2]),     // Day
             Integer.valueOf(T[0]),     // Hour
             Integer.valueOf(T[1]));    // Minute

return calendar.getTimeInMillis();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
//

public static long get_News_Time (String value) {

String[] date = value.split(" ");
String[] time = date[4].split(":");

int Y, M, D, h, m; // Рік, місяць, день, година, хвилина

Y = Integer.valueOf(date[3].substring(0, 4));
D = Integer.valueOf(date[1]);
h = Integer.valueOf(time[0]);
m = Integer.valueOf(time[1]);
M = 0;

for (int z = 0; z < vars.month_names.length; z++) {
    if (date[2].equals(vars.month_names[z])) { M = z; break; }
}

Calendar calendar = Calendar.getInstance();;
calendar.set(Y, M, D, h, m);

return calendar.getTimeInMillis();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Виведення помилки в лог

public static void set_Next_Previous_Buttons_Visibility() {

Calendar time_min = Calendar.getInstance();
Calendar time_max = Calendar.getInstance();
Calendar time_now = Calendar.getInstance();

Calendar time_next = Calendar.getInstance();
Calendar time_prev = Calendar.getInstance();

time_now.setTime(new Date(System.currentTimeMillis()));

time_min.set(2015, 10, 19);
time_max.set(time_now.get(Calendar.YEAR),
             time_now.get(Calendar.MONTH),
             time_now.get(Calendar.DAY_OF_MONTH));

time_now.set(vars.date[2], vars.date[1], vars.date[0]);

View prev = app.findViewById(R.id.get_prev);
View next = app.findViewById(R.id.get_next);

if (time_now.compareTo(time_min) <= 0) { prev.setVisibility(View.GONE); }
else                                   { prev.setVisibility(View.VISIBLE); }

if (time_now.compareTo(time_max) >= 0) { next.setVisibility(View.GONE); }
else                                   { next.setVisibility(View.VISIBLE); }

///////////////////////////////////////////////////////////////////////////////////////////////////

time_next.setTime(time_now.getTime());
time_prev.setTime(time_now.getTime());

time_next.add(Calendar.DATE, 1);
time_prev.add(Calendar.DATE, -1);

int day_next = time_next.get(Calendar.DAY_OF_MONTH);
int month_next = (time_next.get(Calendar.MONTH) + 1);

int day_prev = time_prev.get(Calendar.DAY_OF_MONTH);
int month_prev = (time_prev.get(Calendar.MONTH) + 1);

String string_next = (day_next   > 9 ? "" : "0") + day_next + "." +
                     (month_next > 9 ? "" : "0") + month_next;

String string_prev = (day_prev   > 9 ? "" : "0") + day_prev + "." +
                     (month_prev > 9 ? "" : "0") + month_prev;

((TextView)app.findViewById(R.id.get_prev_text)).setText(string_prev);
((TextView)app.findViewById(R.id.get_next_text)).setText(string_next);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Перегляд анімації

public static void start_Preview_Anim() {

int anim = R.anim.preview_empty;

switch (vars.settings_index) {

// ................................................................................................
// Анімація відкривання вікна

/*case 10:
switch (((RadioGroup)Dialog.get_Dialog().findViewById(R.id.bg_anim_open))
                                        .getCheckedRadioButtonId()) {

    case R.id.anim_open_01:  anim = R.anim.refresh_show_01;    break;
    case R.id.anim_open_02:  anim = R.anim.refresh_show_02;    break;
    case R.id.anim_open_03:  anim = R.anim.refresh_show_03;    break;
    case R.id.anim_open_04:  anim = R.anim.refresh_show_04;    break;
    case R.id.anim_open_05:  anim = R.anim.refresh_show_05;    break;
    case R.id.anim_open_06:  anim = R.anim.refresh_show_06;    break;
    case R.id.anim_open_07:  anim = R.anim.refresh_show_07;    break;
    case R.id.anim_open_off: anim = R.anim.refresh_show_empty; break;

}

break;*/

// ................................................................................................
// Анімація закривання вікна

/*case 11:
switch (((RadioGroup)Dialog.get_Dialog().findViewById(R.id.bg_anim_close))
                                        .getCheckedRadioButtonId()) {

    case R.id.anim_close_01:  anim = R.anim.refresh_hide_01;    break;
    case R.id.anim_close_02:  anim = R.anim.refresh_hide_02;    break;
    case R.id.anim_close_03:  anim = R.anim.refresh_hide_03;    break;
    case R.id.anim_close_04:  anim = R.anim.refresh_hide_04;    break;
    case R.id.anim_close_05:  anim = R.anim.refresh_hide_05;    break;
    case R.id.anim_close_06:  anim = R.anim.refresh_hide_06;    break;
    case R.id.anim_close_07:  anim = R.anim.refresh_hide_07;    break;
    case R.id.anim_close_off: anim = R.anim.refresh_hide_empty; break;

}

break;*/

// ................................................................................................
// Анімація для прев'ю

/*case 12:
switch (((RadioGroup)Dialog.get_Dialog().findViewById(R.id.bg_anim_preview))
                                        .getCheckedRadioButtonId()) {

    case R.id.anim_preview_01:  anim = R.anim.preview_01;    break;
    case R.id.anim_preview_02:  anim = R.anim.preview_02;    break;
    case R.id.anim_preview_03:  anim = R.anim.preview_03;    break;
    case R.id.anim_preview_04:  anim = R.anim.preview_04;    break;
    case R.id.anim_preview_05:  anim = R.anim.preview_05;    break;
    case R.id.anim_preview_06:  anim = R.anim.preview_06;    break;
    case R.id.anim_preview_07:  anim = R.anim.preview_07;    break;
    case R.id.anim_preview_off: anim = R.anim.preview_empty; break;

}

break;*/

}

// ................................................................................................

Utility.start_Anim(Dialog.get_Dialog().findViewById(R.id.dialog_preview), anim);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Перевірка доступності інтернету

public static boolean is_Online (Context context) {

ConnectivityManager cm = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);

NetworkInfo net_info = cm.getActiveNetworkInfo();
if (net_info != null && net_info.isConnected()) { return true; }

return false;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Виведення помилки в лог

public static void print_Stack_Trace (String title, Exception e) {

    StackTraceElement[] stack_Trace = e.getStackTrace();
    Log.e(vars.TAG, title + ": " + e.toString());

    for (int z = 0; z < stack_Trace.length; z++) { Log.e(vars.TAG, "caused by: " + stack_Trace[z]); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void get_App_State_By_Data() {

if ((vars.news_array == null || vars.news_array.isEmpty()) &&
    !vars.search_mode) { vars.app_state = 1.0f; }                       // Початковий стан програми

if (vars.news_array != null &&
    !vars.news_array.isEmpty() &&
    !vars.search_mode) { vars.app_state = 1.1f; }                        // Відобраення списку новин

if ((vars.news_array == null || vars.news_array.isEmpty()) &&
    vars.search_mode) { vars.app_state = 2.0f; }                     // Введення пошукового запиту

if (vars.news_array != null &&
    !vars.news_array.isEmpty() &&
    vars.search_mode) { vars.app_state = 2.1f; }                // Відобраення списку шуканих новин

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void close_Drawer_Layout() {

if (vars.drawer.isDrawerOpen(GravityCompat.START))
  { vars.drawer.closeDrawer(GravityCompat.START); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static String get_String (int id) { return get_String(id, (Object) null); }

// ................................................................................................

public static String get_String (int id, Object ... args) {

String result;
if (res == null) { res = app.getResources(); }

result = res.getString(id);

if (args != null) { result = String.format(result, args); }

return result;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void restart() {

Log.e(TAG, "Restart");

Settings.save_Settings();
Intent intent = app.getIntent();

app.finish();
app.overridePendingTransition( 0, 0);
app.startActivity(intent);
app.overridePendingTransition( 0, 0);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}
