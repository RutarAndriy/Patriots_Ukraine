package com.rutar.patriots_ukraine.process;

import java.text.*;
import java.util.*;
import android.os.*;
import android.util.*;
import android.view.View;

import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.other.News_Item;
import com.rutar.patriots_ukraine.other.Recycler_Adapter;
import com.rutar.patriots_ukraine.utils.Utility;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

// Даний клас отримує список новин по html
//////////////////////////////////////////////////////////////////////////////////////////////////

public class Process_Find_News extends AsyncTask <String, Void, Void > {

//////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected Void doInBackground (String... arg) {

try { vars.news_array = Process_Find_News.find_News(arg[0]);
      Thread.sleep(1000); }

catch (Exception e) { vars.news_array.clear();
                      Utility.print_Stack_Trace("Find_News do_In_Background error", e); }

return null;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onPostExecute (Void output) {

vars.news_list.setEnabled(true);
vars.input_lock = false;
//vars.running_anim.remove("search_news");

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо список новин за запитом не отримано, показуємо повідомлення про помилку
if (vars.news_array.isEmpty()) {

//app.news_adapter = new List_Adapter(app, Settings.news_array);
vars.news_adapter = new Recycler_Adapter(vars.news_array);
vars.news_list.setAdapter(vars.news_adapter);

//app.swipe_refresh.setRefreshing(false);
vars.news_list.setVisibility(View.VISIBLE);

if (Utility.is_Online(app)) { Utility.show_Snackbar(Utility.get_String(R.string.error_find_news), false); }
else                        { Utility.show_Snackbar(Utility.get_String(R.string.error_internet), false); }

vars.search_text = "";
//Utility.set_Toolbar_Title();

Handler handler = new Handler();
handler.postDelayed(new Runnable() {
    @Override
    public void run() {

vars.search_text_field.setEnabled(true);
vars.search_progress_bar.clearAnimation();
vars.search_progress_bar.setVisibility(View.INVISIBLE);
    }
}, 600);

Log.e(vars.TAG, "Find news result: Error");

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо список новин за запитом отримано, створюємо адаптер і відображаємо їх
else {

//app.news_adapter = new List_Adapter(app, Settings.news_array);
vars.news_adapter = new Recycler_Adapter(vars.news_array);
vars.news_list.setAdapter(vars.news_adapter);

Utility.toolbar_Views_Hide();
Utility.start_Anim(vars.layout_search, vars.refresh_anim_hide);

Log.i(vars.TAG, "Find news result: OK");

}

//app.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static ArrayList <News_Item> find_News (String search) {

try {

String date = null;
Document doc = null;
String url = "http://patrioty.org.ua/search?s=" + search;

final ArrayList <News_Item> news = new ArrayList<>();

try { doc = Jsoup.connect(url).timeout(30000).get(); }
catch (Throwable throwable) { throwable.printStackTrace();
                              return new ArrayList <News_Item>(); }

Elements elements = doc.getElementsByClass("archive-items").first().getAllElements();

for (int z = 1; z < elements.size(); z++) {

Element element = elements.get(z);

// Якщо елемент вказує на дату то перезаписуємо змінну date
if (element.className().equals("date")) {

    date = element.getElementsByAttribute("href").first().attr("href");

}

// Якщо елемент містить новину, то оброблюємо його
else if (element.className().equals("archive-item")) {

    News_Item item = new News_Item();
    long milliseconds = Utility.get_News_Time(date,
                                element.getElementsByClass("time").first().text());

    // Заголовок
    String title = element.getElementsByTag("a").first().text();

    // Час публікації новини
    String time = "";

    // URL для сторінки
    String page_url = element.getElementsByTag("a").first().absUrl("href");

    // URL для прев'ю
    String preview_url = element.getElementsByAttribute("src").first().absUrl("src");

    // ID сторінки
    String id = "item" + page_url.substring(page_url.lastIndexOf("-"), page_url.lastIndexOf("."));

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(milliseconds));
    time = new SimpleDateFormat("dd.MM.yyyy, HH:mm").format(calendar.getTime());

    // Задаємо параметри елемента новини
    item.set_Id(id);
    item.set_Time(time);
    item.set_Title(title);
    item.set_Page_Url(page_url);
    item.set_Preview_Url(preview_url);

    // Перевірка визнчає, чи показувати невідредаговані новини
    if (milliseconds <= System.currentTimeMillis()) { news.add(item); }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

Log.d(vars.TAG, "Found some news, count: " + news.size());

return news;

}

catch (Exception e) { Utility.print_Stack_Trace("Find News Error", e);
                      return new ArrayList <News_Item>(); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}