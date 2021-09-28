package com.rutar.patriots_ukraine;

import java.io.*;
import java.util.*;
import android.os.*;
import android.net.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.view.animation.*;

import com.nispok.snackbar.*;
import com.mikepenz.materialdrawer.*;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import javax.net.ssl.*;
import java.security.cert.*;

import static com.rutar.patriots_ukraine.Helper.*;
import static com.rutar.patriots_ukraine.Utilities.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Головний клас - початкове Activity

public class Patriots_Ukraine extends AppCompatActivity {

public static String html = "";
public static String site = "";

public static String css_file = "";
public static String exit_text = "";
public static String empty_title = "";
public static String progress_title_1 = "";
public static String progress_title_2 = "";
public static String date_picker_title = "";

public static String TAG = "Patriots Ukraine";

public static ArrayList <News_Item> news_array;
public static ArrayList <News_Item> favorite_array;

public static String[] rate_text = new String[6];
public static String[] days_names = new String[7];
public static String[] month_names = new String[12];

public static View logo;

public static ListView news_list;
public static ListView favorite_list;

public static TextView error_view;
public static TextView empty_view;

public static AlertDialog progress;
public static TextView progress_text;
public static Patriots_Ukraine patriots;

public static TextView menu_title;
public static Animation logo_animation;

public static int[] date = new int[3];
public static boolean anim_is_running = false;

public static long back_pressed = 0;
public static int app_load_count = 0;
public static int rate_load_count = 3;

public static boolean date_change = false;
public static boolean app_is_rated = false;
public static boolean show_rate_dialog = true;

public static boolean show_logo = true;

public static Handler handler;

public static View layout_main;
public static View layout_favorite;
public static View layout_settings;
public static View layout_about;

public static List_Adapter news_adapter;
public static List_Adapter favorite_adapter;

public static String year;

public static ContextWrapper context_Wrapper;

public static File temp_folder_prev;
public static File temp_folder_favor;

public static final String folder_name_prev = "preview";
public static final String folder_name_favor = "favorites";

public static News_Item list_item;

public static Utilities.Folder_Filter folder_filter;

public static int logo_visibility = 0;
public static int menu_index = 0;

public static int date_picker_text = 0;
public static int date_picker_background = 0;

public static Drawer drawer;

public static final int[] default_settings = new int[] { 0, -1, 2, 100, 0, 50, 0, 1 };

public static int settings_theme;
public static int settings_language;
public static int settings_orientation;
public static int settings_font_size;
public static int settings_preview;
public static int settings_quality;
public static int settings_link;
public static int settings_sociality;

public static SharedPreferences settings_reader;
public static SharedPreferences.Editor settings_writer;

public static String[] settings_text_theme;
public static String[] settings_text_language;
public static String[] settings_text_orientation;
public static String[] settings_text_font_size;
public static String[] settings_text_preview;
public static String[] settings_text_quality;
public static String[] settings_text_link;
public static String[] settings_text_sociality;

public static boolean settings_change = false;

public static int snackbar_text_color;
public static int snackbar_background;

public static String snackbar_command;

public static int seekbar_is_show = -1;
public static boolean is_restart = false;
public static boolean rate_is_show = false;
public static boolean info_is_show = false;
public static boolean repair_is_show = false;
public static boolean calendar_is_show = false;

public static int seekbar_font = 0;
public static int seekbar_quality = 0;

public static int page_margin;
public static int drawer_width;
public static int dialog_button_size;

public static View dialog_title_view;
public static View dialog_message_view;

public static TextView dialog_title;
public static TextView dialog_message;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Точка створення Activity

@Override
protected void onCreate (Bundle bundle) {

Log.wtf(TAG, "Bundle: " + bundle);
Log.wtf(TAG, "Last object: " + getLastCustomNonConfigurationInstance());

settings_change = false;

settings_reader = getPreferences(MODE_PRIVATE);
settings_writer = settings_reader.edit();

///////////////////////////////////////////////////////////////////////////////////////////////////

settings_theme       = settings_reader.getInt("settings_theme",       default_settings[0]);
settings_language    = settings_reader.getInt("settings_language",    default_settings[1]);
settings_orientation = settings_reader.getInt("settings_orientation", default_settings[2]);
settings_font_size   = settings_reader.getInt("settings_font_size",   default_settings[3]);
settings_preview     = settings_reader.getInt("settings_preview",     default_settings[4]);
settings_quality     = settings_reader.getInt("settings_quality",     default_settings[5]);
settings_link        = settings_reader.getInt("settings_link",        default_settings[6]);
settings_sociality   = settings_reader.getInt("settings_sociality",   default_settings[7]);

seekbar_font = settings_font_size;
seekbar_quality = settings_quality;

///////////////////////////////////////////////////////////////////////////////////////////////////

if (bundle != null) { is_restart = bundle.getBoolean("is_restart", false);
                      seekbar_is_show = bundle.getInt("seekbar_is_show", -1);
                      rate_is_show = bundle.getBoolean("rate_is_show", false);
                      info_is_show = bundle.getBoolean("info_is_show", false);
                      repair_is_show = bundle.getBoolean("repair_is_show", false);
                      calendar_is_show = bundle.getBoolean("calendar_is_show", false);
                      seekbar_font = bundle.getInt("seekbar_font", default_settings[3]);
                      seekbar_quality = bundle.getInt("seekbar_quality", default_settings[5]); }

///////////////////////////////////////////////////////////////////////////////////////////////////

app_is_rated   = settings_reader.getBoolean("app_is_rated", false);
app_load_count = settings_reader.getInt("app_load_count", 0) + (is_restart ? 0 : 1);
settings_writer.putInt("app_load_count", app_load_count);
settings_writer.commit();

///////////////////////////////////////////////////////////////////////////////////////////////////

if (settings_language == -1) {

String lang = Locale.getDefault().getLanguage();

if      (lang.equals("ru")) { settings_language = 1; }
else                        { settings_language = 0; }

settings_writer.putInt("settings_language", settings_language);
settings_writer.commit();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

Locale locale = null;

if (settings_language == 1) { locale = new Locale("ru"); }
else                        { locale = new Locale("uk"); }

Locale.setDefault(locale);
Configuration config = new Configuration();
config.locale = locale;

getBaseContext().getResources().updateConfiguration(config,
                 getBaseContext().getResources().getDisplayMetrics());

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
setContentView(R.layout.patriots_ukraine);

disable_Certificate_Validation();

///////////////////////////////////////////////////////////////////////////////////////////////////

context_Wrapper = new ContextWrapper(this);
patriots = this;

///////////////////////////////////////////////////////////////////////////////////////////////////

dialog_button_size = (int) (getResources().getDimension(R.dimen.dialog_button_size) /
                            getResources().getDisplayMetrics().density);
drawer_width = (int) (getResources().getDimension(R.dimen.menu_width) /
                      getResources().getDisplayMetrics().density);
page_margin = (int) (getResources().getDimension(R.dimen.base_page_margin) /
                     getResources().getDisplayMetrics().density);

reset_Dialog();

if (rate_is_show)     { Helper.show_Rate_Dialog(); }
if (info_is_show)     { Helper.show_Info_Dialog(); }
if (repair_is_show)   { Helper.show_Repair_Dialog(); }
if (calendar_is_show) { Helper.show_Calendar_Dialog(); }

if      (seekbar_is_show == 0) { Helper.show_Seekbar_Dialog(0); }
else if (seekbar_is_show == 1) { Helper.show_Seekbar_Dialog(1); }

///////////////////////////////////////////////////////////////////////////////////////////////////

init_Strings();
init_Settings_Strings();
set_Settings_Strings();
set_Settings_Images();

View progress_bar = LayoutInflater.from(this).inflate(R.layout.progress_bar, null);
progress_text = (TextView) progress_bar.findViewById(R.id.progress_text);

date[0] = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
date[1] = Calendar.getInstance().get(Calendar.MONTH);
date[2] = Calendar.getInstance().get(Calendar.YEAR);

///////////////////////////////////////////////////////////////////////////////////////////////////

if (bundle != null) {

    menu_index = bundle.getInt("menu_index", 0);
    logo_visibility = bundle.getInt("logo_visibility", 0);
    news_array = bundle.getParcelableArrayList("news_array");
    show_rate_dialog = bundle.getBoolean("show_rate_dialog", false);

    date[2] = bundle.getInt("date_Y", Calendar.getInstance().get(Calendar.YEAR));
    date[1] = bundle.getInt("date_M", Calendar.getInstance().get(Calendar.MONTH));
    date[0] = bundle.getInt("date_D", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

}

///////////////////////////////////////////////////////////////////////////////////////////////////

TypedValue typedValue = new TypedValue();
Resources.Theme theme = getTheme();

theme.resolveAttribute(R.attr.title_bar_background, typedValue, true);
date_picker_background = typedValue.data;

theme.resolveAttribute(R.attr.text_color_2, typedValue, true);
date_picker_text = typedValue.data;

theme.resolveAttribute(R.attr.text_color_1, typedValue, true);
snackbar_text_color = typedValue.data;

theme.resolveAttribute(R.attr.title_bar_background, typedValue, true);
snackbar_background = typedValue.data;

///////////////////////////////////////////////////////////////////////////////////////////////////

String version_name = "unknown";
String version_info = getResources().getString(R.string.version);

try { version_name = getPackageManager().getPackageInfo(getPackageName(), 0).versionName; }
catch (Exception e) { version_name = "1.0"; }

((TextView) findViewById(R.id.version_view)).setText(String.format(version_info, version_name));

///////////////////////////////////////////////////////////////////////////////////////////////////

layout_main     = findViewById(R.id.layout_main);
layout_favorite = findViewById(R.id.layout_favorite);
layout_settings = findViewById(R.id.layout_settings);
layout_about    = findViewById(R.id.layout_about);

layout_main.setVisibility(View.GONE);
layout_favorite.setVisibility(View.GONE);
layout_settings.setVisibility(View.GONE);
layout_about.setVisibility(View.GONE);

///////////////////////////////////////////////////////////////////////////////////////////////////

switch (menu_index) {

    case R.id.menu_favorite: layout_favorite.setVisibility(View.VISIBLE); break;
    case R.id.menu_settings: layout_settings.setVisibility(View.VISIBLE); break;
    case R.id.menu_about:    layout_about.setVisibility(View.VISIBLE);    break;

    default:                 layout_main.setVisibility(View.VISIBLE);     break;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

drawer = new DrawerBuilder().withActivity(this).withDrawerWidthDp(drawer_width)
        .withCustomView(LayoutInflater.from(patriots).inflate(R.layout.menu_layout, null)).build();

///////////////////////////////////////////////////////////////////////////////////////////////////

AlertDialog.Builder builder = new AlertDialog.Builder(this);
builder.setView(progress_bar);

progress = builder.create();
progress.setCancelable(false);

temp_folder_prev = context_Wrapper.getDir(folder_name_prev, Context.MODE_PRIVATE);
temp_folder_favor = context_Wrapper.getDir(folder_name_favor, Context.MODE_PRIVATE);

menu_title = (TextView)findViewById(R.id.menu_title);
error_view = (TextView)findViewById(R.id.error);
empty_view = (TextView)findViewById(R.id.empty);
logo = findViewById(R.id.logo);

if (logo_visibility != 0 &&
   (news_array == null || news_array.isEmpty())) { error_view.setVisibility(View.VISIBLE); }

///////////////////////////////////////////////////////////////////////////////////////////////////

folder_filter = new Utilities.Folder_Filter();

logo_animation = AnimationUtils.loadAnimation(patriots, R.anim.logo_animation);
logo_animation.setAnimationListener(animation_listener);

favorite_array = new ArrayList<>();
favorite_adapter = new List_Adapter(patriots, favorite_array);

news_list = (ListView) findViewById(R.id.news_list);
news_list.setOnItemClickListener(list_listener);

if (news_array == null) { news_adapter = new List_Adapter(patriots, new ArrayList<News_Item>()); }
else                    { news_adapter = new List_Adapter(patriots, news_array);  }

news_list.setAdapter(news_adapter);

favorite_list = (ListView) findViewById(R.id.favorite_list);
favorite_list.setOnItemClickListener(list_listener);
favorite_list.setAdapter(favorite_adapter);

///////////////////////////////////////////////////////////////////////////////////////////////////

prepare_Favorite_List();
set_Menu_Title();

///////////////////////////////////////////////////////////////////////////////////////////////////

handler = new Handler(){

@Override
public void handleMessage (Message msg) { show_Rate_Dialog(); }

};

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховування піктограм

public void on_View_Click (View view) {

if (!anim_is_running) {

if (view.getId() == R.id.logo_image ||
    view.getId() == R.id.menu_logo) {

view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_press));

switch (view.getId()) {

    case R.id.logo_image: logo.startAnimation(logo_animation);                            break;
    case R.id.menu_logo:  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(site))); break;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

else {

view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press));

switch (view.getId()) {

    case R.id.info:       show_Info_Dialog();   break;
    case R.id.repair:     show_Repair_Dialog(); break;
    case R.id.menu_title: show_Calendar_Dialog();      break;

    case R.id.menu_close:                        drawer.closeDrawer();  break;
    case R.id.menu_open: if (!settings_change) { drawer.openDrawer(); } break;

    case R.id.refresh: if (logo.getVisibility() == View.GONE) { refresh_News(); } break;

    case R.id.link: startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(site))); break;

}

}
}

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховування пунктів меню

public void on_Menu_Item_Click (View view) {

if (settings_change) { return; }

layout_main.setVisibility(View.GONE);
layout_favorite.setVisibility(View.GONE);
layout_settings.setVisibility(View.GONE);
layout_about.setVisibility(View.GONE);

menu_index = view.getId();

switch (view.getId()) {

    case R.id.menu_news: layout_main.setVisibility(View.VISIBLE);         break;
    case R.id.menu_favorite: layout_favorite.setVisibility(View.VISIBLE); break;
    case R.id.menu_settings: layout_settings.setVisibility(View.VISIBLE); break;
    case R.id.menu_about: layout_about.setVisibility(View.VISIBLE);       break;

    case R.id.menu_exit:

        layout_main.setVisibility(View.VISIBLE);

        if (show_rate_dialog && !app_is_rated &&
            app_load_count > rate_load_count &&
            is_Online(patriots)) { handler.sendEmptyMessageDelayed(0, 500); }

        else { Utilities.delete_Dir(temp_folder_prev);
               temp_folder_prev.mkdir();
               System.exit(0); }

        break;

}

drawer.closeDrawer();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховування налаштувань

public void on_Settings_Click (View view) {

switch (view.getId()) {

case R.id.settings_theme:       settings_theme += settings_theme             < 1 ? 1 : -1; break;
case R.id.settings_language:    settings_language += settings_language       < 1 ? 1 : -1; break;
case R.id.settings_orientation: settings_orientation += settings_orientation < 2 ? 1 : -2; break;
case R.id.settings_preview:     settings_preview += settings_preview         < 1 ? 1 : -1; break;
case R.id.settings_link:        settings_link += settings_link               < 1 ? 1 : -1; break;
case R.id.settings_sociality:   settings_sociality += settings_sociality     < 1 ? 1 : -1; break;

case R.id.settings_font_size:   show_Seekbar_Dialog(0); break;
case R.id.settings_quality:     show_Seekbar_Dialog(1); break;

}

show_Reboot_Snackbar();

set_Settings_Images();
set_Settings_Strings();
view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.settings_press));

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховування клавіші "Назад"

@Override
public void onBackPressed() {

if (settings_change) { return; }

if (show_rate_dialog && !app_is_rated &&
    app_load_count > rate_load_count && is_Online(patriots)) { show_Rate_Dialog(); }

///////////////////////////////////////////////////////////////////////////////////////////////////

else {

if (back_pressed + 1500 > System.currentTimeMillis()) { Utilities.delete_Dir(temp_folder_prev);
                                                        temp_folder_prev.mkdir();
                                                        System.exit(0); }

else { back_pressed = System.currentTimeMillis();
       SnackbarManager.show(Snackbar.with(this).color(snackbar_background)
                                               .text(exit_text)
                                               .textColor(snackbar_text_color)
                                               .swipeToDismiss(false)
                                               .duration(800)); }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Збереження даних при повороті або перезавантаженні

@Override
protected void onSaveInstanceState (Bundle bundle) {

super.onSaveInstanceState(bundle);

bundle.putInt("date_D", date[0]);
bundle.putInt("date_M", date[1]);
bundle.putInt("date_Y", date[2]);

bundle.putBoolean("is_restart", true);
bundle.putInt("menu_index", menu_index);
bundle.putInt("seekbar_font", seekbar_font);
bundle.putBoolean("rate_is_show", rate_is_show);
bundle.putBoolean("info_is_show", info_is_show);
bundle.putInt("seekbar_quality", seekbar_quality);
bundle.putInt("seekbar_is_show", seekbar_is_show);
bundle.putInt("logo_visibility", logo_visibility);
bundle.putBoolean("repair_is_show", repair_is_show);
bundle.putBoolean("show_rate_dialog", show_rate_dialog);
bundle.putBoolean("calendar_is_show", calendar_is_show);
bundle.putParcelableArrayList("news_array", news_array);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Точка відновлення Activity

@Override
protected void onResume() {

super.onResume();

if (logo_visibility == 0) { logo.setVisibility(View.VISIBLE); }
else                      { logo.setVisibility(View.GONE); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Оновлення діалогових вікон

public static void reset_Dialog() {

dialog_title_view = LayoutInflater.from(patriots).inflate(R.layout.dialog_title, null);
dialog_message_view = LayoutInflater.from(patriots).inflate(R.layout.dialog_custom, null);

dialog_title = (TextView) dialog_title_view.findViewById(R.id.dialog_title);
dialog_message = (TextView) dialog_message_view.findViewById(R.id.dialog_message);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Вимкнення перевірки сертифікатів

private void disable_Certificate_Validation() {

TrustManager[] trust_all_certs = new TrustManager[] {
    new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() { return null; }
        public void checkClientTrusted (X509Certificate[] certs, String authType) { }
        public void checkServerTrusted (X509Certificate[] certs, String authType)
                                        throws CertificateException {
            if (certs.length == 0) { throw new CertificateException("Certificates not found"); }
        }
    }
};

// Install the all-trusting trust manager
try { SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trust_all_certs, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); }
catch (Exception e) { Log.e(TAG, "Set default SSL socket factory error"); }

// Create all-trusting host name verifier
HostnameVerifier hosts_valid_names = new HostnameVerifier() {
    public boolean verify (String hostname, SSLSession session) {
        return hostname.contains("patrioty.org.ua");
    }
};

// Install the all-trusting host verifier
HttpsURLConnection.setDefaultHostnameVerifier(hosts_valid_names);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}