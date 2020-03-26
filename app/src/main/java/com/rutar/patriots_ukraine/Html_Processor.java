package com.rutar.patriots_ukraine;

import java.io.*;
import java.text.*;
import java.util.*;

import org.json.*;
import org.jsoup.*;
import android.util.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import android.content.res.*;

import static com.rutar.patriots_ukraine.Utilities.*;
import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

public class Html_Processor {

///////////////////////////////////////////////////////////////////////////////////////////////////

public static File page_file;
private static String time_text;
public static String time_tmp;

private static ArrayList<String> files_links;

private static String[] remove_list = new String[]
    { "head",
      "footer",
      "comments",
      "left-sidebar",
      "right-sidebar",
      "social-buttons",
      "instagram-media",
      "widget-readMore",
      "ads-below-article",
      "ads-center-bottom" };

///////////////////////////////////////////////////////////////////////////////////////////////////

public static String process (News_Item item) {

copy_CSS_File();

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо сторінка знаходиться у кеші - дістаємо її, усі ресурси вже кешовані

page_file = new File(temp_folder_favor.getAbsolutePath() + "/" + item.get_Id(), "page.html");

if (page_file.exists()) {

Log.i(TAG, "html found");
Web_Activity.is_favorite = true;

try {

InputStream is = new FileInputStream(page_file);
BufferedReader reader = new BufferedReader(new InputStreamReader(is));

String line = reader.readLine();
StringBuilder result = new StringBuilder();

while (line != null) { result.append(line).append("\n");
                       line = reader.readLine(); }

return result.toString();

}

catch (Exception e) { return "error"; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо сторінки немає у кеші - завантажуємо її разом з усіма ресурсами
else {

Log.i(TAG, "html not found");
Web_Activity.is_favorite = false;

try {

files_links = new ArrayList<>();

///////////////////////////////////////////////////////////////////////////////////////////////////
// Видалення зайвих блоків

Document doc = Jsoup.connect(item.get_Page_Url()).get();

///////////////////////////////////////////////////////////////////////////////////////////////////

for (Element element : doc.getElementsByTag("p")) {

final Elements im = element.getElementsByTag("img");
final Elements fr = element.getElementsByTag("iframe");
final Elements ii = element.getElementsByClass("item-image");
final Elements vc = element.getElementsByClass("video-container");

if (im.isEmpty() && ii.isEmpty() && element.text().isEmpty()) { element.remove(); }
else if ((!vc.isEmpty() || !fr.isEmpty()) && element.text().isEmpty()) { element.remove(); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

doc.select("br").remove();


doc.getElementsByTag("video").remove();
doc.getElementsByTag("object").remove();
doc.getElementsByTag("script").remove();
doc.getElementsByTag("iframe").remove();
doc.getElementsByTag("script").remove();
doc.getElementsByTag("noscript").remove();
doc.getElementsByClass("left-sidebar").remove();
doc.getElementsByClass("header-banner").remove();

doc.select("[style*=background: #ff5658;]").remove();

doc.getElementsByClass("video-container").remove();
doc.getElementsByClass("main-menu container").remove();

doc.getElementsByAttributeValue("rel", "shortcut icon").remove();
doc.getElementsByAttributeValue("id", "more-items-infinite").remove();

for (String list : remove_list) { doc.getElementsByClass(list).remove(); }

Elements h2 = doc.getElementsByTag("h2");
for (int z = 0; z < h2.size(); z++) { if (z > 0) { h2.get(z).remove(); } }

// Видалення повідомлення про зміну сервісу коментування
doc.select("[style*=background: #ff5658;]").remove();

///////////////////////////////////////////////////////////////////////////////////////////////////
// Видалення посилання в шапці сторінки

try {

    Element time = doc.getElementsByClass("item-meta").get(0).getElementsByClass("time").get(0);
    time_text = time.getElementsByTag("a").get(0).text();

    time_text = time_text.replace("понеділок", days_names[0])
                         .replace("вівторок",  days_names[1])
                         .replace("середа",    days_names[2])
                         .replace("четвер",    days_names[3])
                         .replace("п’ятниця",  days_names[4])
                         .replace("субота",    days_names[5])
                         .replace("неділя",    days_names[6])
                         .replace("січень",   month_names[0])
                         .replace("лютий",    month_names[1])
                         .replace("березень", month_names[2])
                         .replace("квітень",  month_names[3])
                         .replace("травень",  month_names[4])
                         .replace("червень",  month_names[5])
                         .replace("липень",   month_names[6])
                         .replace("серпень",  month_names[7])
                         .replace("вересень", month_names[8])
                         .replace("жовтень",  month_names[9])
                         .replace("листопад", month_names[10])
                         .replace("грудень",  month_names[11]);

    time_tmp = time_text.substring(time_text.lastIndexOf(" ") + 1);
    time.text(time_text);

}

catch (Exception e) {}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Виправлення зображень

Elements images = doc.select("img");

for (int z = 0; z < images.size(); z++) {

Element image = images.get(z);

image.removeAttr("width");
image.removeAttr("height");

image.attr("style", "display: block; margin: auto;");

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Виправлення сторінок

try {

// Стандартна сторінка
try {

    Element block = doc.getElementsByClass("item-content").get(0).select("p").get(0);

    String title = block.select("img").get(0).attr("alt");
    if (title.isEmpty()) { title = empty_title; }

    Element img_Tag = block.getElementsByTag("img").get(0);
    block.select("img").remove();

    block = doc.getElementsByClass("item-content").get(0);
    block.prepend("<p class=\"item-image\"> " + img_Tag +
                  "<span class=\"item-image-sign\">" + title + "</span>" + " </p>");

}

// Нестандартна сторінка
catch (Exception e) {

    Element block = doc.getElementsByClass("item-content").get(0);
    Element image = block.select("img").get(0);

    String title = image.attr("alt");
    if (title.isEmpty()) { title = empty_title; }
    image.remove();

    block.prepend("<p class=\"item-image\">" + image +
                  "<span class=\"item-image-sign\">" + title + "</span></p>");

}
}

catch (Exception e) { Log.e(Patriots_Ukraine.TAG, "Title error");
                      print_Stack_Trace(e); }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Отримання посилань на ресурси сторінки

Elements img = doc.select("img[src]");
Elements lnk = doc.select("link[href], link[rel=stylesheet]");

///////////////////////////////////////////////////////////////////////////////////////////////////
// Змінюємо шляхи до ресурсів

for (Element element : img) {

String value_1 = element.attr("src");
String value_2 = process_Name(value_1);

files_links.add(element.absUrl("src"));
element.attr("src", "./" + item.get_Id() + "/" + value_2);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Змінюємо шлях до css файла

for (Element element : lnk) { element = element.attr("href", "./" + css_file); }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Завантаження ресурсів сторінки

for (int z = 0; z < files_links.size(); z++) { get_File(files_links.get(z),
                                               temp_folder_favor.getAbsolutePath() + "/" +
                                               item.get_Id()); }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Записуємо оброблену сторінку у файл page.html
// Записуємо дані про сторінку у файл item.ini
// Записуємо прев'ю у файл *.jpg або *.png

FileWriter writer = null;

try {

String ini = item.get_Id() + "\n" +
             time_text + "\n" +
             item.get_Title() + "\n" +
             item.get_Page_Url() + "\n" + "/" +
             item.get_Id() + "/" + process_Name(item.get_Preview_Url()) + "\n" +
             date[0] + "-" + date[1] + "-" + date[2] + "-" + time_tmp;

writer = new FileWriter(new File(temp_folder_favor.getAbsolutePath() +
                                   "/" + item.get_Id() + "/page.html"));

writer.write(doc.html());
writer.flush();
writer.close();

writer = new FileWriter(new File(temp_folder_favor.getAbsolutePath() +
                        "/" + item.get_Id() + "/item.ini"));

writer.write(ini);
writer.flush();
writer.close();

String preview_name = process_Name(item.get_Preview_Url());
save_Bitmap_To_Storage(load_Bitmap_From_Storage(preview_name, null),
                       preview_name, page_file.getParentFile());

}

catch (IOException e) { Log.e(TAG, "Error: " + e.getMessage()); }

finally { if (writer != null) { writer.close(); } }

///////////////////////////////////////////////////////////////////////////////////////////////////

return doc.html();

}

catch (Exception e) { print_Stack_Trace(e);
                      return "error"; }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static ArrayList <News_Item> get_News (String url) {

try {

final ArrayList <News_Item> news = new ArrayList<>();

JSONArray json_Array = new JSONArray(Jsoup.connect(url)
                          .ignoreContentType(true).execute().body());

for (int z = 0; z < json_Array.length(); z++) {

News_Item item = new News_Item();
JSONObject object = json_Array.getJSONObject(z);

item.set_Id("item-" + object.getString("iditem"));
item.set_Title(object.getString("title"));
item.set_Page_Url(object.getString("uri"));
item.set_Preview_Url("http://patrioty.org.ua" + object.getString("img"));

long publish_time = Long.parseLong(object.getString("datepublish") + "000");

Calendar calendar = Calendar.getInstance();
calendar.setTimeInMillis(publish_time);

item.set_Time(new SimpleDateFormat("HH:mm").format(calendar.getTime()));

news.add(item);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

return news;

}

catch (Exception e) { print_Stack_Trace(e);
                      return null; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static void copy_CSS_File() {

AssetManager manager = patriots.getAssets();

FileWriter fw = null;
InputStream in = null;
Scanner scanner = null;

try {

in = manager.open(css_file);
scanner = new Scanner(in);

String data = scanner.useDelimiter("\\A").next();

int font_size = (int) (patriots.getResources()
                      .getDimension(R.dimen.base_font_size) * settings_font_size / 100f);

data = data.replaceAll("#C0", get_Color(R.attr.text_color_2))              // Колір тексту сторінки
           .replaceAll("#C1", get_Color(R.attr.list_item_color_1))        // Фоновий колір сторінки
           .replaceAll("#C2", get_Color(R.attr.link_color))                      // Колір посилання
           .replaceAll("#C3", get_Color(R.attr.link_color))        // Колір використаного посилання
           .replaceAll("#C4", get_Color(R.attr.link_color))            // Колір активного посилання
           .replaceAll("#C5", get_Color(R.attr.text_color_2))             // Колір заголовку новини
           .replaceAll("#C6", get_Color(R.attr.text_color_2))          // Колір тексту - час і дата
           .replaceAll("#C7", get_Color(R.attr.time_bar_background))     // Колір фону - час і дата
           .replaceAll("#C8", get_Color(R.attr.text_color_2))              // Колір тексту - підпис
           .replaceAll("#C9", get_Color(R.attr.time_bar_background))         // Колір фону - підпис
           .replaceAll("#FS", String.valueOf(font_size))                           // Розмір шрифту
           .replaceAll("#M1", String.valueOf(page_margin))                               // Відступ
           .replaceAll("#M2", String.valueOf(page_margin));                              // Відступ

File outFile = new File(temp_folder_favor.getAbsolutePath(), css_file);

fw = new FileWriter(outFile);
fw.write(data);

}

catch (IOException e) { Log.e(TAG, "Failed to copy asset file: " + css_file, e); }

finally { if (in != null)      { try { in.close(); }
                                 catch (IOException e) {} }
          if (fw != null)      { try { fw.close(); }
                                 catch (IOException e) {} }
          if (scanner != null) { scanner.close(); } }
}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static String get_Color (int attribute) {

TypedValue typedValue = new TypedValue();
Patriots_Ukraine.patriots.getTheme().resolveAttribute(attribute, typedValue, true);

return "#" + Integer.toHexString(typedValue.data).substring(2);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}