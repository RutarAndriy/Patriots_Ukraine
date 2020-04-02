package com.rutar.patriots_ukraine.process;

import java.text.SimpleDateFormat;
import java.util.*;
import android.os.*;
import android.util.*;
import android.view.View;

import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.other.News_Item;
import com.rutar.patriots_ukraine.other.Recycler_Adapter;
import com.rutar.patriots_ukraine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;
import static com.rutar.patriots_ukraine.Variables.*;

// Даний клас отримує список новин по html
//////////////////////////////////////////////////////////////////////////////////////////////////

public class Process_Get_News extends AsyncTask <String, Void, Void > {

//////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected Void doInBackground (String... arg) {

try { vars.news_array = Process_Get_News.get_News(arg[0]);
      Thread.sleep(1000); }

catch (Exception e) { vars.news_array.clear();
                      Utility.print_Stack_Trace("Get_News do_In_Background error", e); }

return null;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onPostExecute (Void output) {

vars.app_state = 1.1f;
vars.news_list.setEnabled(true);
Utility.set_Next_Previous_Buttons_Visibility();

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо список новин не отримано, показуємо повідомлення про помилку
if (vars.news_array.isEmpty()) {

vars.app_state = 1.0f;

vars.news_adapter = new Recycler_Adapter(vars.news_array);
vars.news_list.setAdapter(vars.news_adapter);

vars.swipe_refresh.setRefreshing(false);
vars.news_list.setVisibility(View.VISIBLE);

if (Utility.is_Online(app)) { Utility.show_Snack_Bar(Utility.get_String(R.string.error_get_news), false); }
else                        { Utility.show_Snack_Bar(Utility.get_String(R.string.error_internet), false); }

new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {

        Utility.start_Anim(vars.layout_logo, -2);
        vars.input_lock = false;

    }
}, 600);

Log.e(TAG, "Get news result: Error");

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо список новин отримано, створюємо адаптер і відображаємо їх
else {

//app.news_adapter = new List_Adapter(app, Settings.news_array);
vars.news_adapter = new Recycler_Adapter(vars.news_array);
vars.news_list.setAdapter(vars.news_adapter);

Utility.start_Anim(vars.news_list, vars.refresh_anim_show);

Log.i(TAG, "Get news result: OK");

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static ArrayList <News_Item> get_News (String date) {

try {

Document doc = null;
String url = "http://patrioty.org.ua/all/" + date + ".html";

final ArrayList <News_Item> news = new ArrayList<>();

try { doc = Jsoup.connect(url).timeout(30000).get(); }
catch (Throwable throwable) { throwable.printStackTrace();
                              return new ArrayList <News_Item>(); }

Elements elements = doc.select(".archive-item[data-time]");
// Elements elements = doc.getElementsByClass("archive-item"); // Більше не працює

for (Element elem : elements) {

    News_Item item = new News_Item();

    String id = elem.attr("id");

    // Пропускаємо рекламні вставки
    if (!id.startsWith("item")) { continue; }

    String time = elem.getElementsByClass("time").first().text();
    String title = elem.getElementsByTag("a").first().text();
    String page_url = elem.getElementsByTag("a").first().absUrl("href");
    String preview_url = elem.getElementsByTag("img").first().absUrl("src");

    item.set_Id(id);
    item.set_Time(time);
    item.set_Title(title);
    item.set_Page_Url(page_url);
    item.set_Preview_Url(preview_url);

    news.add(item);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

// while (news.size() > 2) { news.remove(news.size()-1); }

return news;

}

catch (Exception e) { Utility.print_Stack_Trace("Get News Error", e);
return new ArrayList <News_Item>(); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод отримує список новин використовуючи JSON. На даний час не є актуальним.

private static ArrayList <News_Item> get_News_JSON (String date) {

try {

final ArrayList <News_Item> news = new ArrayList<>();
String url = "http://patrioty.org.ua/api/items-by-date.json?date=" + date;
JSONArray json_Array = new JSONArray(Jsoup.connect(url).ignoreContentType(true).execute().body());

for (int z = 0; z < json_Array.length(); z++) {

    News_Item item = new News_Item();
    JSONObject object = json_Array.getJSONObject(z);

    item.set_Id("item-" + object.getString("iditem"));
    item.set_Title(object.getString("title"));
    item.set_Page_Url(object.getString("uri"));
    item.set_Preview_Url("http://patrioty.org.ua" + object.getString("img"));

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(Long.parseLong(object.getString("datepublish") + "000")));
    calendar.add(Calendar.MILLISECOND, 7200000 - TimeZone.getDefault().getRawOffset());

    item.set_Time(new SimpleDateFormat("HH:mm").format(calendar.getTime()));
    news.add(item);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

return news;

}

catch (Exception e) { Utility.print_Stack_Trace("Get News Error", e);
return new ArrayList <News_Item>(); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}