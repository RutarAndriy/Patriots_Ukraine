package com.rutar.patriots_ukraine;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import android.net.*;
import android.util.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.content.res.*;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

public class Utilities {

///////////////////////////////////////////////////////////////////////////////////////////////////

private static DateFormat date_format = new SimpleDateFormat("dd-MM-yyyy-hh:mm");

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод завантажує файли по їх посиланню

public static void get_File (String link,
                             String path) throws Exception {


// Якщо файл - зображення jpg або png, то перед збереженням він стискається

InputStream is = null;
OutputStream os = null;

if (link.endsWith(".jpg") || link.endsWith(".png")) {

try {

HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
connection.connect();

is = connection.getInputStream();
Bitmap bitmap = BitmapFactory.decodeStream(is);

File file = new File(path);
if (!file.exists()) { file.mkdir(); }

os = new BufferedOutputStream(new FileOutputStream(path + "/" + process_Name(link)));

bitmap.compress(Bitmap.CompressFormat.JPEG, settings_quality, os);
os.close();

}

catch (Exception e) { Log.e(TAG, "Download Error: " + e.fillInStackTrace()); }

finally { if (is != null) { is.close(); }
          if (os != null) { os.close(); } }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо файл не є jpg або png зображенням - зберігаємо оригінальний файл

else {

try {

File file = new File(path);
if (!file.exists()) {
    file.mkdir();
}

URL url = new URL(link);
is = url.openStream();
os = new BufferedOutputStream(new FileOutputStream(path + "/" + process_Name(link)));

for (int b; (b = is.read()) != -1; ) { os.write(b); }

os.close();
is.close();

}

catch (Exception e)  { Log.e(TAG, "Download Error: " + e.fillInStackTrace()); }

finally { if (is != null) { is.close(); }
          if (os != null) { os.close(); } }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод отримує ім'я файлу із його посилання

public static String process_Name (String name) {

String file_name = name.substring(name.lastIndexOf("/") + 1);

int index = file_name.indexOf("?");
if (index != -1) { file_name = file_name.substring(0, index); }

return file_name;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод видаляє папки разом із вкладеними файлами

public static void delete_Dir (File file) {

File[] contents = file.listFiles();

if (contents != null) { for (File f : contents) { delete_Dir(f); } }
file.delete();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод зберігає прев'ю у файлову систему

public static boolean save_Bitmap_To_Storage (Bitmap image, String name, File path) {

FileOutputStream fos = null;
// Якщо path == null, то зберігаємо зображення у папку прев'ю

try {

fos = new FileOutputStream(new File(path == null ? temp_folder_prev : path, name));
image.compress(Bitmap.CompressFormat.JPEG, settings_quality, fos);

return true;

}

catch (Exception e) { Log.e(TAG, "Save Preview Error: " + e.getMessage());
                      return false; }

finally { if (fos != null) { try { fos.close(); }
                             catch (IOException e) { e.printStackTrace(); } } }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод завантажує прев'ю з файлової системи

public static Bitmap load_Bitmap_From_Storage (String name, File path)  {

try { File f = new File(path == null ? temp_folder_prev : path, name);
      return BitmapFactory.decodeStream(new FileInputStream(f)); }

catch (FileNotFoundException e) { e.printStackTrace();
                                  return null; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Перевірка доступності інтернету

public static boolean is_Online (Context context) {

ConnectivityManager cm = (ConnectivityManager) context
                         .getSystemService(Context.CONNECTIVITY_SERVICE);

NetworkInfo netInfo = cm.getActiveNetworkInfo();
if (netInfo != null && netInfo.isConnected()) { return true; }

return false;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static class Folder_Filter implements FilenameFilter {

@Override
public boolean accept (File directory, String file_name) {

    if (file_name.contains("item-")) { return true;  }
    else                             { return false; }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static Comparator <News_Item> news_item_comparator = new Comparator <News_Item>() {

@Override
public int compare (News_Item o1, News_Item o2) {

try {

Date date_1 = date_format.parse(o1.get_Date_Time_String());
Date date_2 = date_format.parse(o2.get_Date_Time_String());

return date_2.compareTo(date_1);

}

catch (Exception e) { return 0; }

}

};

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void init_Strings() {

Resources res = patriots.getResources();

css_file          = "site.css";

exit_text         = res.getString(R.string.exit_text);
empty_title       = res.getString(R.string.empty_title);
progress_title_1  = res.getString(R.string.progress_title_1);
progress_title_2  = res.getString(R.string.progress_title_2);
date_picker_title = res.getString(R.string.date_picker_title);

rate_text[0] = res.getString(R.string.rate_text_1);
rate_text[1] = res.getString(R.string.rate_text_2);
rate_text[2] = res.getString(R.string.rate_error);
rate_text[3] = res.getString(R.string.rate_yes);
rate_text[4] = res.getString(R.string.rate_later);
rate_text[5] = res.getString(R.string.rate_no);

days_names[0]  = res.getString(R.string.day_01);
days_names[1]  = res.getString(R.string.day_02);
days_names[2]  = res.getString(R.string.day_03);
days_names[3]  = res.getString(R.string.day_04);
days_names[4]  = res.getString(R.string.day_05);
days_names[5]  = res.getString(R.string.day_06);
days_names[6]  = res.getString(R.string.day_07);

month_names[0]  = res.getString(R.string.month_01);
month_names[1]  = res.getString(R.string.month_02);
month_names[2]  = res.getString(R.string.month_03);
month_names[3]  = res.getString(R.string.month_04);
month_names[4]  = res.getString(R.string.month_05);
month_names[5]  = res.getString(R.string.month_06);
month_names[6]  = res.getString(R.string.month_07);
month_names[7]  = res.getString(R.string.month_08);
month_names[8]  = res.getString(R.string.month_09);
month_names[9]  = res.getString(R.string.month_10);
month_names[10] = res.getString(R.string.month_11);
month_names[11] = res.getString(R.string.month_12);

year = res.getString(R.string.year);

site = "http://" + res.getString(R.string.site);
snackbar_command = res.getString(R.string.snackbar_command);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void init_Settings_Strings() {

Resources res = patriots.getResources();

settings_text_theme = new String[] {
        res.getString(R.string.settings_theme_title),
        res.getString(R.string.settings_theme_choice_1),
        res.getString(R.string.settings_theme_choice_2) };

settings_text_language = new String[] {
        res.getString(R.string.settings_language_title),
        res.getString(R.string.settings_language_choice_1),
        res.getString(R.string.settings_language_choice_2) };

settings_text_orientation = new String[] {
        res.getString(R.string.settings_orientation_title),
        res.getString(R.string.settings_orientation_choice_1),
        res.getString(R.string.settings_orientation_choice_2),
        res.getString(R.string.settings_orientation_choice_3) };

settings_text_font_size = new String[] { res.getString(R.string.settings_font_size_title) };

settings_text_preview = new String[] {
        res.getString(R.string.settings_preview_title),
        res.getString(R.string.settings_preview_choice_1),
        res.getString(R.string.settings_preview_choice_2) };

settings_text_quality = new String[] { res.getString(R.string.settings_quality_title) };

settings_text_link = new String[] {
        res.getString(R.string.settings_link_title),
        res.getString(R.string.settings_link_choice_1),
        res.getString(R.string.settings_link_choice_2) };

settings_text_sociality = new String[] {
        res.getString(R.string.settings_sociality_title),
        res.getString(R.string.settings_sociality_choice_1),
        res.getString(R.string.settings_sociality_choice_2) };

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void set_Menu_Title() { menu_title.setText(date[0] + " " +
                                                         month_names[date[1]] + " " +
                                                         date[2] + " " + year); }

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void set_Settings_Strings() {

// Тема
((TextView) patriots.findViewById(R.id.settings_theme_title)).setText(String
           .format(settings_text_theme[0], settings_theme == 0 ?
                   settings_text_theme[1] :
                   settings_text_theme[2]));

// Мова
((TextView) patriots.findViewById(R.id.settings_language_title)).setText(String
           .format(settings_text_language[0], settings_language == 0 ?
                   settings_text_language[1] :
                   settings_text_language[2]));

// Орієнтація
((TextView) patriots.findViewById(R.id.settings_orientation_title)).setText(String
           .format(settings_text_orientation[0],  settings_orientation == 0 ?
                   settings_text_orientation[1] : settings_orientation == 1 ?
                   settings_text_orientation[2] : settings_text_orientation[3]));

// Розмір шрифту
((TextView) patriots.findViewById(R.id.settings_font_size_title)).setText(String
           .format(settings_text_font_size[0], settings_font_size));

// Прев'ю
((TextView) patriots.findViewById(R.id.settings_preview_title)).setText(String
           .format(settings_text_preview[0], settings_preview == 0 ?
                   settings_text_preview[1] :
                   settings_text_preview[2]));

// Якість зображень
((TextView) patriots.findViewById(R.id.settings_quality_title)).setText(String
           .format(settings_text_quality[0], settings_quality));

// Посилання
((TextView) patriots.findViewById(R.id.settings_link_title)).setText(String
           .format(settings_text_link[0], settings_link == 0 ?
                   settings_text_link[1] :
                   settings_text_link[2]));

// Соціальні мережі
((TextView) patriots.findViewById(R.id.settings_sociality_title)).setText(String
           .format(settings_text_sociality[0], settings_sociality == 0 ?
                   settings_text_sociality[1] :
                   settings_text_sociality[2]));

    }

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void set_Settings_Images() {

// Тема
((ImageView) patriots.findViewById(R.id.settings_theme_image)).setImageResource(
             settings_theme == 0 ? R.drawable.ic_theme_light :
                                   R.drawable.ic_theme_dark);

// Орієнтація
((ImageView) patriots.findViewById(R.id.settings_orientation_image)).setImageResource(
             settings_orientation == 0 ? R.drawable.ic_orientation_portrait :
             settings_orientation == 1 ? R.drawable.ic_orientation_landscape :
                                         R.drawable.ic_orientation_auto);

// Прев'ю
((ImageView) patriots.findViewById(R.id.settings_preview_image)).setImageResource(
             settings_preview == 0 ? R.drawable.ic_preview_on :
                                     R.drawable.ic_preview_off);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void print_Stack_Trace (Exception e) {

StackTraceElement[] stack_Trace = e.getStackTrace();
Log.e(TAG, "Error: " + e.toString());

for (int z = 0; z < stack_Trace.length; z++) { Log.e(TAG, "caused by: " + stack_Trace[z]); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void save_Settings() {

settings_reader = patriots.getPreferences(MODE_PRIVATE);
settings_writer = settings_reader.edit();

settings_writer.putInt("settings_theme",       settings_theme);
settings_writer.putInt("settings_language",    settings_language);
settings_writer.putInt("settings_orientation", settings_orientation);
settings_writer.putInt("settings_font_size",   settings_font_size);
settings_writer.putInt("settings_preview",     settings_preview);
settings_writer.putInt("settings_quality",     settings_quality);
settings_writer.putInt("settings_link",        settings_link);
settings_writer.putInt("settings_sociality",   settings_sociality);

settings_writer.commit();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static float get_Folder_Size(File folder) {

long length = 0;
File[] files = folder.listFiles();

int count = files.length;

for (int i = 0; i < count; i++) { if (files[i].isFile()) { length += files[i].length(); }
                                  else { length += get_Folder_Size(files[i]); } }

return length;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}