package com.rutar.patriots_ukraine;

import java.io.*;

import android.os.*;
import android.net.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.webkit.*;
import android.content.*;
import android.content.pm.*;
import android.view.animation.*;
import android.support.v7.app.*;
import android.support.annotation.*;

import static com.rutar.patriots_ukraine.Helper.*;
import static com.rutar.patriots_ukraine.Html_Processor.*;
import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

import android.annotation.SuppressLint;

///////////////////////////////////////////////////////////////////////////////////////////////////

public class Web_Activity extends AppCompatActivity {

private Custom_Web web_View;
private View error_layout;

private String page_dir = null;
private String page_url = null;
private String page_path = null;
private String html_path = null;

public static boolean is_favorite;
private boolean is_recreate = false;

///////////////////////////////////////////////////////////////////////////////////////////////////

@SuppressLint("SourceLockedOrientationActivity")
@Override
protected void onCreate (Bundle bundle) {

try { page_url = list_item.get_Page_Url();
      page_path = page_file.getParentFile().getPath();
      page_dir = "file://" + temp_folder_favor.getAbsolutePath() + "/";
      html_path = temp_folder_favor.getAbsolutePath() + "/" + list_item.get_Id(); }

catch (Exception e) { is_recreate = true; }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Якщо строка html є порожньою, значить іншій програмі знадобилася RAM

if (is_recreate) {

settings_reader = getPreferences(MODE_PRIVATE);

page_dir  = settings_reader.getString("page_dir",  null);
page_url  = settings_reader.getString("page_url",  null);
page_path = settings_reader.getString("page_path", null);
html_path = settings_reader.getString("html_path", null);

settings_theme       = settings_reader.getInt("s_theme", 0);
settings_orientation = settings_reader.getInt("s_orientation", 2);
settings_link        = settings_reader.getInt("s_link", 1);
settings_sociality   = settings_reader.getInt("s_sociality", 1);

html = get_HTML();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// В іншому випадку все нормально

else {

settings_reader = getPreferences(MODE_PRIVATE);
settings_writer = settings_reader.edit();

settings_writer.putString("page_dir", page_dir);
settings_writer.putString("page_url",  page_url);
settings_writer.putString("page_path", page_path);
settings_writer.putString("html_path", html_path);

settings_writer.putInt("s_theme", settings_theme);
settings_writer.putInt("s_orientation", settings_orientation);
settings_writer.putInt("s_link", settings_link);
settings_writer.putInt("s_sociality", settings_sociality);
settings_writer.apply();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

if (settings_theme == 0) { setTheme(R.style.FullScreen_Theme_Light); }
else                     { setTheme(R.style.FullScreen_Theme_Dark);  }

///////////////////////////////////////////////////////////////////////////////////////////////////

switch (settings_orientation) {

case 0:  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  break;
case 1:  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); break;
default: setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);      break;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

super.onCreate(bundle);
setContentView(R.layout.web_layout);

///////////////////////////////////////////////////////////////////////////////////////////////////

is_Favorite();
error_layout = findViewById(R.id.page_error);

web_View = (Custom_Web) findViewById(R.id.web_view);

web_View.getSettings().setJavaScriptEnabled(false);
web_View.setOnLongClickListener(on_long_click_listener);

load_URL_Data();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод ініціалізує webView або показує повідомлення при помилці

private void load_URL_Data() {

web_View.setWebViewClient(web_client);

///////////////////////////////////////////////////////////////////////////////////////////////////

if (is_recreate &&                                        // Інша програма закрила Патріоти України
   !html.equals("error")) { web_View.loadDataWithBaseURL(page_dir, html, null, null, null); }

else if (html.equals("error")) { error_layout.setVisibility(View.VISIBLE); }

else { web_View.loadDataWithBaseURL(page_dir, html, null, null, null); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач піктограм

public void on_View_Click (View view) {

view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press));

if (view.getId() == R.id.back) { onBackPressed(); }
if (view.getId() == R.id.link) { startActivity(new Intent(Intent.ACTION_VIEW,
                                 Uri.parse(page_url))); }

if (view.getId() == R.id.favorite &&
    error_layout.getVisibility() == View.GONE) { is_favorite =! is_favorite;
                                                 is_Favorite(); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач кнопки "Назад"

@Override
public void onBackPressed() {

super.onBackPressed();
overridePendingTransition(R.anim.start_activity, R.anim.close_activity);

web_View.clearHistory();
web_View.clearCache(true);

if (!is_recreate) { prepare_Favorite_List(); }
if (!is_favorite) { Utilities.delete_Dir(new File(page_path)); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод установлює улюблену сторінку

private void is_Favorite() {

if (is_favorite) { ((ImageView) findViewById(R.id.favorite))
                               .setImageResource(R.drawable.ic_favorite_on); }

else             { ((ImageView) findViewById(R.id.favorite))
                               .setImageResource(R.drawable.ic_favorite_off); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@NonNull
private String get_HTML() {

try {

InputStream is = new FileInputStream(new File(html_path, "page.html"));
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
// Прослуховувач довгих натискань на сторінку

private View.OnLongClickListener on_long_click_listener = new View.OnLongClickListener() {

@Override
public boolean onLongClick (View v) { return true; }

};

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод реагує на завершення ініціалізації сторінки та задає логіку посилань

private WebViewClient web_client = new WebViewClient() {

@Override
public void onPageFinished (WebView view, String url) {

super.onPageFinished(view, url);

final Handler handler = new Handler();
handler.postDelayed(new Runnable() {

@Override
public void run() {

    web_View.setVisibility(View.VISIBLE);

    if (settings_link == 0)      { findViewById(R.id.link_layout).setVisibility(View.VISIBLE); }
    else                         { findViewById(R.id.link_layout).setVisibility(View.GONE); }

    if (settings_sociality == 0) { findViewById(R.id.share_layout).setVisibility(View.VISIBLE); }
    else                         { findViewById(R.id.share_layout).setVisibility(View.GONE); }

}

}, 150);
}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public boolean shouldOverrideUrlLoading (WebView view, String url) {

//return false;

Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
view.getContext().startActivity(intent);
return true;

}

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);
        Log.e(TAG, "onPageCommitVisible is done");
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);

        //Log.e(TAG, "Height: " + web_View.getContentHeight());

        Log.e(TAG, "Progress " + view.getProgress() + "%");
    }
};

///////////////////////////////////////////////////////////////////////////////////////////////////

}