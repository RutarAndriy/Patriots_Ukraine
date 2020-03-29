package com.rutar.patriots_ukraine.process;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.Variables;
import com.rutar.patriots_ukraine.other.News_Item;
import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.Settings;
import com.rutar.patriots_ukraine.utils.Utility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;
import static com.rutar.patriots_ukraine.utils.Utility.*;
import static com.rutar.patriots_ukraine.Variables.*;

// Даний клас оброблює html сторінку та завантажує необхідні ресурси
///////////////////////////////////////////////////////////////////////////////////////////////////

public class Process_Get_Page extends AsyncTask <News_Item, Void, News_Item> {

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

@Override
protected News_Item doInBackground (News_Item... params) {

try { vars.html = Process_Get_Page.process(params[0]); }
catch (Exception e) { Log.e(TAG, "Error: " + e.getMessage()); }

return params[0];

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onPostExecute (News_Item result) {

super.onPostExecute(result);

switch (String.valueOf(vars.app_state)) {

    case "1.1": vars.app_state = 1.2f; break; // Відприття новини
    case "2.1": vars.app_state = 2.2f; break; // Відкриття шуканої новини
    default:    vars.app_state = 3.1f; break; // Відкриття улюбленої новини

}

// ................................................................................................

String page_dir = "file://" + vars.folder_favor.getAbsolutePath() + "/";

if (vars.html.equals("error")) { vars.can_be_favorite = false;
                                 vars.error_page.setVisibility(View.VISIBLE);
                                 vars.web_view.loadDataWithBaseURL(page_dir, "",
                                                                   null, null, null); }

else { vars.can_be_favorite = true;
       vars.error_page.setVisibility(View.GONE);
       vars.web_view.clearCache(true);
       vars.web_view.loadDataWithBaseURL(page_dir, vars.html, null, null, null); }

Utility.draw_Debug();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static String process (News_Item item) {

copy_Resources();
String time_text = "";

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо сторінка знаходиться у кеші - дістаємо її, усі ресурси вже кешовані

vars.page_file = new File(vars.folder_favor.getAbsolutePath() + "/" + item.get_Id(), "page.html");

if (vars.page_file.exists()) {

Log.i(TAG, "html found");
vars.page_is_favorite = true;

// ................................................................................................

try {

    InputStream is = new FileInputStream(vars.page_file);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

    String line = reader.readLine();
    StringBuilder result = new StringBuilder();

    while (line != null) { result.append(line).append("\n");
        line = reader.readLine(); }

    Process_Get_Page.create_Video_Previews(item);
    return result.toString();

}

catch (Exception e) { return "error"; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо сторінки немає у кеші - завантажуємо її разом з усіма ресурсами

else {

Log.i(TAG, "html not found");
vars.page_is_favorite = false;

try {

ArrayList<String> files_links = new ArrayList<>();

// Якщо немає інтернету, то кидаємо виключення
if (!Utility.is_Online(app)) { throw new Exception("App is not online"); }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Отримання html сторінки для парсингу

Document doc = Jsoup.connect(item.get_Page_Url()).get();

// Видалення зайвих html блоків

doc.select("br").remove();
doc.getElementsByTag("object").remove();
doc.getElementsByTag("script").remove();
doc.getElementsByTag("noscript").remove();
doc.getElementsByClass("left-sidebar").remove();
doc.getElementsByClass("header-banner").remove();
doc.select("[style*=background: #ff5658;]").remove();
doc.getElementsByClass("main-menu container").remove();
doc.getElementsByAttributeValue("rel", "shortcut icon").remove();
doc.getElementsByAttributeValue("id", "more-items-infinite").remove();

Elements h2 = doc.getElementsByTag("h2");
for (int z = 0; z < h2.size(); z++) { if (z > 0) { h2.get(z).remove(); } }

for (String list : remove_list) { doc.getElementsByClass(list).remove(); }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Виправлення im_youtube відео

try {

Elements iframe = doc.getElementsByTag("iframe");

Log.d(TAG, "Found " + iframe.size() + " iframe's");

for (int q = 0; q < iframe.size(); q++) {

    String video_id = "null";
    Element element = iframe.get(q);

    element.text(""); // Видаяємо сміття з тегів
    Attributes attr = element.attributes();
    List<Attribute> attr_list = attr.asList();

    for (int z = 0; z < attr_list.size(); z++) {

        Attribute at = attr_list.get(z);

        if (at.getValue().contains("youtube.com")) {
            video_id = Utility.get_Youtube_Video_Id(at.getValue());
        }

    element.removeAttr(at.getKey());

    }

    if (!video_id.equals("null")) {

    element.tagName("img");

    element.attr("src", "http://img.youtube.com/vi/" + video_id + "/0.jpg");
    element.attr("alt", "Відео: www.youtube.com");

    element.wrap("<a href='" + "http://www.youtube.com/watch?v=" + video_id + "'></a>");

    }

}
}

catch (Exception e) { }

///////////////////////////////////////////////////////////////////////////////////////////////////

                for (Element element : doc.getElementsByTag("p")) {

                    final Elements im = element.getElementsByTag("img");
                    final Elements fr = element.getElementsByTag("iframe");
                    final Elements ii = element.getElementsByClass("item-image");
                    final Elements vc = element.getElementsByClass("video-container");

                    if (im.isEmpty() && ii.isEmpty() && element.text().isEmpty()) { element.remove(); }
                    else if ((!vc.isEmpty() || !fr.isEmpty()) && element.text().isEmpty()) { element.remove(); }

                }

                doc.getElementsByTag("video").remove();
                doc.getElementsByTag("iframe").remove();
                doc.getElementsByClass("video-container").remove();

///////////////////////////////////////////////////////////////////////////////////////////////////
// Видалення посилання в шапці сторінки

                try {

                    Element time = doc.getElementsByClass("item-meta").get(0).getElementsByClass("time").get(0);
                    time_text = time.getElementsByTag("a").get(0).text();

                    time_text = time_text.replace("понеділок", Utility.get_String(R.string.day_01))
                            .replace("вівторок",  Utility.get_String(R.string.day_02))
                            .replace("середа",    Utility.get_String(R.string.day_03))
                            .replace("четвер",    Utility.get_String(R.string.day_04))
                            .replace("п’ятниця",  Utility.get_String(R.string.day_05))
                            .replace("субота",    Utility.get_String(R.string.day_06))
                            .replace("неділя",    Utility.get_String(R.string.day_07))
                            .replace("січень",   Utility.get_String(R.string.month_01))
                            .replace("лютий",    Utility.get_String(R.string.month_02))
                            .replace("березень", Utility.get_String(R.string.month_03))
                            .replace("квітень",  Utility.get_String(R.string.month_04))
                            .replace("травень",  Utility.get_String(R.string.month_05))
                            .replace("червень",  Utility.get_String(R.string.month_06))
                            .replace("липень",   Utility.get_String(R.string.month_07))
                            .replace("серпень",  Utility.get_String(R.string.month_08))
                            .replace("вересень", Utility.get_String(R.string.month_09))
                            .replace("жовтень",  Utility.get_String(R.string.month_10))
                            .replace("листопад", Utility.get_String(R.string.month_11))
                            .replace("грудень",  Utility.get_String(R.string.month_12));

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
                        if (title.isEmpty()) { title = Utility.get_String(R.string.error_empty_title); }
                        else { title = process_Image_Title(title); }

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
                        if (title.isEmpty()) { title = Utility.get_String(R.string.error_empty_title); }
                        else { title = process_Image_Title(title); }
                        image.remove();

                        block.prepend("<p class=\"item-image\">" + image +
                                "<span class=\"item-image-sign\">" + title + "</span></p>");


                    }

                    ///////
                    // Якщо в зображенні статті відео, то даємо посилання на нього

                    try {

                        Element item_image = doc.getElementsByClass("item-image").first();

                        Element item_img = item_image.getElementsByTag("img").first();

                        if (item_img != null) {

                            String src_attr = item_img.attr("src");

                            if (src_attr.contains("http://img.im_youtube.com/vi/")) {

                                src_attr = src_attr.substring(0, src_attr.lastIndexOf("/0.jpg"));
                                src_attr = src_attr.substring(src_attr.lastIndexOf("/") + 1);

                                Log.e(vars.TAG, "Attrib: " + src_attr);

                                item_image.wrap("<a href='" + "http://www.im_youtube.com/watch?v=" + src_attr + "'></a>");

                            }

                        }

                    }

                    catch (Exception e) {  }

                }

                catch (Exception e) {
                    print_Stack_Trace("Title error", e); }

// ................................................................................................
// Задаємо парамеетри для viewport

Element viewport = doc.getElementsByAttributeValue("name", "viewport").first();
viewport.attr("content", "width=device-width,init-scale=1,minimum-scale=1");

// ................................................................................................
// Видаляємо пусті блоки

Elements dives = doc.getElementsByClass("item-content").first()
                    .getElementsByTag("div");

for (int z = 0; z < dives.size(); z++) { Element div = dives.get(z);
                                         if (div.getAllElements().size() == 1) { div.remove(); } }

// ................................................................................................
// Видаляємо зайві елементи

doc.getElementsByAttributeValue("name", "csrf-param").remove();
doc.getElementsByAttributeValue("name", "csrf-token").remove();

doc.getElementsByAttributeValue("property", "og:url").remove();
doc.getElementsByAttributeValue("property", "og:type").remove();
doc.getElementsByAttributeValue("property", "og:title").remove();
doc.getElementsByAttributeValue("property", "og:image").remove();
doc.getElementsByAttributeValue("property", "og:locale").remove();
doc.getElementsByAttributeValue("property", "fb:app_id").remove();
doc.getElementsByAttributeValue("property", "og:site_name").remove();
doc.getElementsByAttributeValue("property", "og:description").remove();

doc.getElementsByAttributeValue("itemprop", "image").remove();
doc.getElementsByAttributeValue("itemprop", "headline").remove();
doc.getElementsByAttributeValue("itemprop", "description").remove();

doc.getElementsByAttribute("itemscope").remove();

// Виправлення багу сторінки із Гройсманом :)
doc.getElementsByAttributeValue("href", "<a href=").remove();

// Розкоментуй для повного облому :)
// doc.getElementsByAttributeValue("rel", "stylesheet").remove();

///////////////////////////////////////////////////////////////////////////////////////////////////
// Отримання посилань на ресурси сторінки

Elements img = doc.select("img[src]");
Elements lnk = doc.selectFirst("head").select("link[href]");

///////////////////////////////////////////////////////////////////////////////////////////////////
// Змінюємо шляхи до ресурсів

for (Element element : img) {

    String value_1 = element.attr("src");
    String value_2 = process_Name(value_1);

    // Прев'ю після пост обробки - із логотипом youtube кольору активної теми
    if (value_1.contains("youtube.com/")) { value_2 = "proc_" + value_2; }

    files_links.add(element.absUrl("src"));
    element.attr("src", "./" + item.get_Id() + "/" + value_2);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Змінюємо шлях до css файла

for (Element element : lnk) {

    element.clearAttributes();
    element.attr("href", "./site.css");
    element.attr("rel", "stylesheet");

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Завантаження ресурсів сторінки

for (int z = 0; z < files_links.size(); z++) {

save_File_To_Storage(files_links.get(z),
               vars.folder_favor.getAbsolutePath() + "/" + item.get_Id()); }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Записуємо оброблену сторінку у файл page.html
// Записуємо дані про сторінку у файл item.ini
// Записуємо прев'ю у файл *.jpg або *.png

FileWriter writer = null;
String preview_url = null;

try {

Log.d(TAG, "Preview URL: " + item.get_Preview_Url());

if (item.get_Preview_Url().endsWith("noImage.png")) { preview_url = "http://noImage.png"; }
else { preview_url = "/" + item.get_Id() + "/" + process_Name(item.get_Preview_Url()); }

String ini = "1) " + item.get_Id() + "\n" +                                                   // ID
             "2) " + time_text + "\n" +                                                // Time text
             "3) " + item.get_Title() + "\n" +                                             // Title
             "4) " + item.get_Page_Url() + "\n" +                                       // Page URL
             "5) " + preview_url + "\n" +                                            // Preview URL
             "6) " + Utility.get_News_Time(time_text);                              // Milliseconds

// Якщо немає директорії, то створюємо її
File dir = new File(vars.folder_favor.getAbsolutePath() + "/" + item.get_Id());
if (!dir.exists()) { dir.mkdir(); }

writer = new FileWriter(new File(dir, "page.html"));

writer.write(doc.html());
writer.flush();
writer.close();

writer = new FileWriter(new File(dir, "item.ini"));

writer.write(ini);
writer.flush();
writer.close();

try { save_Image_To_Storage(item.get_Preview_Url(),
                            vars.folder_favor.getAbsolutePath() + "/" + item.get_Id()); }

catch (Exception e) { Log.e(TAG, "Can't save preview to storage"); }

}

catch (IOException e) { Utility.print_Stack_Trace("Write html and ini file Error", e); }

finally { if (writer != null) { writer.close(); } }

///////////////////////////////////////////////////////////////////////////////////////////////////

Process_Get_Page.create_Video_Previews(item);
return doc.html();

}

catch (Exception e) { print_Stack_Trace("Error" , e);
                      return "error"; }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Створюємо прев'ю для відео

private static void create_Video_Previews (News_Item news_item) {

try {

File[] files_in_folder = new File(vars.folder_favor
                        .getAbsolutePath() + "/" + news_item.get_Id()).listFiles();

for (int z = 0; z < files_in_folder.length; z++) {

File file = files_in_folder[z];
if (file.getName().startsWith("video_prev_")) { Utility.create_Video_Preview_Image(file); }

}
}

catch (Exception e) { Log.d(vars.TAG, "Rewrite video preview error"); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static void copy_Resources() {

AssetManager manager = app.getAssets();

FileWriter fw = null;
InputStream in = null;
Scanner scanner = null;

try {

in = manager.open("site.css");
scanner = new Scanner(in);

String data = scanner.useDelimiter("\\A").next();

int font_size = vars.page_font_size;
int page_margin = vars.current_orientation * 1;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Заміна елементів строки на значення змінних

data = data.replaceAll("#C1", get_Color(vars.color_background))           // Фоновий колір сторінки
           .replaceAll("#C2", get_Color(vars.color_text_light))             // Світлий колір тексту
           .replaceAll("#C3", get_Color(vars.color_text_dark))               // Темний колір тексту
           .replaceAll("#C4", get_Color(vars.color_primary))                 // Основний колір теми
           .replaceAll("#C5", get_Color(vars.color_link))                        // Колір посилання
           .replaceAll("#FS", String.valueOf(font_size))                           // Розмір шрифту
           .replaceAll("#MG", String.valueOf(page_margin))               // Відступи зліва і справа
           .replaceAll("#FT", Variables.FONT_NAME);                               // Шрифт сторінки

File outFile = new File(vars.folder_favor.getAbsolutePath(), "site.css");

fw = new FileWriter(outFile);
fw.write(data);

}

catch (IOException e) { Log.e(vars.TAG, "Failed to copy asset file: site.css", e); }

finally { if (in != null) { try { in.close(); }
                            catch (IOException e) {} }
          if (fw != null) { try { fw.close(); }
                            catch (IOException e) {} }
          if (scanner != null) { scanner.close(); } }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Видалення з заголовка прев'ю занадто довгих слів

private static String process_Image_Title (String title) {

String result = "";
String[] split = title.split(" ");

for (int z = 0; z < split.length; z++) {

    if (split[z].length() > 40) { split[z] = split[z].substring(0, 40) + "..."; }
    result += split[z] + (z != split.length - 1 ? " " : "");

}

return result;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}