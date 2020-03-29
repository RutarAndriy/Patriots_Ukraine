package com.rutar.patriots_ukraine;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rutar.patriots_ukraine.custom_views.Dialog;
import com.rutar.patriots_ukraine.custom_views.Web_View;
import com.rutar.patriots_ukraine.listeners.View_Listener;
import com.rutar.patriots_ukraine.utils.FPS_View;
import com.rutar.patriots_ukraine.utils.Utility;

import java.util.Calendar;
import java.util.Date;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

public class Initialization {

private static GradientDrawable next_prev_drawable;
private static GradientDrawable share_circle_drawable;

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void init (final Patriots_Ukraine app) {

init_Strings(app);
init_Variables(app);

init_View(app);
init_Colors(app);
init_Drawer(app);

init_Swipe_Refresh(app);
init_Next_Prev_Drawable(app);
init_Share_Circle_Drawable(app);

Settings.prepare_Settings_List(app);

init_App_State(app);

// Програма оновилася
if (!vars.pref_settings.contains("app_theme")) { Utility.app_Is_Updated(); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static void init_Variables (Patriots_Ukraine app) {

final WindowManager manager = app.getWindowManager();
final ContextWrapper wrapper = new ContextWrapper(app);

vars.density = app.getResources().getDisplayMetrics().density;

vars.display_width  = manager.getDefaultDisplay().getWidth();
vars.display_height = manager.getDefaultDisplay().getHeight();

vars.folder_prev  = wrapper.getDir(vars.folder_name_prev,  Context.MODE_PRIVATE);
vars.folder_favor = wrapper.getDir(vars.folder_name_favor, Context.MODE_PRIVATE);

//vars.running_anim = new ArraySet<>();

Calendar today = Calendar.getInstance();
today.setTime(new Date(System.currentTimeMillis()));

vars.date[0] = today.get(Calendar.DAY_OF_MONTH);
vars.date[1] = today.get(Calendar.MONTH);
vars.date[2] = today.get(Calendar.YEAR);

Dialog.tmp_date[0] = vars.date[0];
Dialog.tmp_date[1] = vars.date[1];
Dialog.tmp_date[2] = vars.date[2];

Utility.draw_Debug();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static void init_View (final Patriots_Ukraine app) {

vars.menu_news     = app.findViewById(R.id.menu_news);
vars.menu_favorite = app.findViewById(R.id.menu_favorite);
vars.menu_settings = app.findViewById(R.id.menu_settings);
vars.menu_about    = app.findViewById(R.id.menu_about);
vars.menu_exit     = app.findViewById(R.id.menu_exit);

// Ввімкнення розділювача на телефонах із наекранними кнопками
if (Utility.is_NavigationBar_Available()) {
    app.findViewById(R.id.bottom_pane_01).setVisibility(View.VISIBLE);
    app.findViewById(R.id.bottom_pane_02).setVisibility(View.VISIBLE);
    app.findViewById(R.id.bottom_pane_03).setVisibility(View.VISIBLE);
}

vars.layout_displayable = app.findViewById(R.id.layout_displayable);
vars.layout_settings    = app.findViewById(R.id.layout_settings);
vars.layout_about       = app.findViewById(R.id.layout_about);

vars.layout_settings.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
    @Override
    public void onLayoutChange(View view,
                               int left, int top, int right, int bottom,
                               int oldLeft, int oldTop, int oldRight, int oldBottom) {




    }
});

((CoordinatorLayout)vars.layout_displayable).addView(new FPS_View(app));

vars.error_list = app.findViewById(R.id.favorite_list_empty);
vars.error_page = app.findViewById(R.id.web_view_error);

vars.toolbar_title = app.findViewById(R.id.toolbar_text);
vars.toolbar_left  = app.findViewById(R.id.toolbar_icon_left);
vars.toolbar_right = app.findViewById(R.id.toolbar_icon_right);

vars.search_text_field = app.findViewById(R.id.search_text_field);
vars.search_text_field.setOnEditorActionListener(View_Listener.editor_action_listener);

// STATE_COLLAPSED - Привідкритий стан
// STATE_EXPANDED  - Відкритий стан
// STATE_HIDDEN    - Прихований стан

vars.behavior_web = BottomSheetBehavior.from((FrameLayout)
                   app.findViewById(R.id.bottom_sheet_web));

vars.behavior_list = BottomSheetBehavior.from((FrameLayout)
                    app.findViewById(R.id.bottom_sheet_list));

vars.behavior_snackbar = BottomSheetBehavior.from((FrameLayout)
                        app.findViewById(R.id.bottom_sheet_snackbar));

vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN);
vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);
vars.behavior_snackbar.setState(BottomSheetBehavior.STATE_HIDDEN);

vars.behavior_web.setBottomSheetCallback(View_Listener.behavior_listener);
vars.behavior_list.setBottomSheetCallback(View_Listener.behavior_listener);

vars.recycler_layout_manager = new LinearLayoutManager(app);

vars.news_list = app.findViewById(R.id.news_list);
vars.news_list.setLayoutManager(vars.recycler_layout_manager);
vars.news_list.setOnScrollListener(View_Listener.scroll_listener);

// ................................................................................................
// Визначення орієнтації екрану

if (vars.display_width < vars.display_height) { vars.current_orientation = 0; } // Портретна
else                                          { vars.current_orientation = 1; } // Ландшафтна

// ................................................................................................

vars.layout_logo = app.findViewById(R.id.logo_layout);
vars.layout_search = app.findViewById(R.id.search_layout);
vars.logo_app = app.findViewById(R.id.logo_app);

// ................................................................................................

vars.web_view = app.findViewById(R.id.web_view);

vars.web_view.getSettings().setSupportZoom(true);
vars.web_view.getSettings().setUseWideViewPort(true);
vars.web_view.getSettings().setAppCacheEnabled(false);
vars.web_view.getSettings().setBuiltInZoomControls(true);
vars.web_view.getSettings().setDisplayZoomControls(false);
vars.web_view.getSettings().setLoadWithOverviewMode(true);
vars.web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
vars.web_view.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
vars.web_view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

vars.web_view.setBackgroundColor(vars.color_background);

vars.web_view.setWebViewClient(Web_View.get_Custom_Web_View_Client());
vars.web_view.setOnLongClickListener(new View.OnLongClickListener() {

    @Override
    public boolean onLongClick (View v) { return true; }

});

// ................................................................................................

vars.search_progress_bar = app.findViewById(R.id.search_progress_bar);

vars.circle_background = new GradientDrawable();
vars.circle_background.setShape(GradientDrawable.RECTANGLE);
vars.circle_background.setCornerRadii(new float[] { 5, 5, 5, 5, 0, 0, 0, 0 });
vars.circle_background.setColor(vars.color_primary_dark);

vars.lp_web = vars.web_view.getLayoutParams();

vars.app_debug_mode = true;
if (vars.app_debug_mode) { app.findViewById(R.id.debug_view).setVisibility(View.VISIBLE); }

// ................................................................................................

try {

final PackageInfo info = app.getPackageManager()
                            .getPackageInfo(app.getPackageName(), 0);

vars.version_name = info.versionName;
vars.version_code = info.versionCode;

}

catch (Exception e) { vars.version_name = "1.0"; }

((TextView)app.findViewById(R.id.version_view)).setText(Utility
              .get_String(R.string.app_version, vars.version_name));

// ................................................................................................

Utility.set_Next_Previous_Buttons_Visibility();
Utility.set_Web_Behavior_Context();
Utility.set_Toolbar_Title();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Ініціалізація кольорів

public static void init_Colors (Patriots_Ukraine app) {

Resources.Theme theme = app.getTheme();
TypedValue typedValue = new TypedValue();

theme.resolveAttribute(R.attr.color_link, typedValue, true);
vars.color_link = typedValue.data;

theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
vars.color_accent = typedValue.data;

theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
vars.color_primary = typedValue.data;

theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
vars.color_primary_dark = typedValue.data;

theme.resolveAttribute(R.attr.color_text_dark, typedValue, true);
vars.color_text_dark = typedValue.data;

theme.resolveAttribute(R.attr.color_text_light, typedValue, true);
vars.color_text_light = typedValue.data;

theme.resolveAttribute(R.attr.color_background, typedValue, true);
vars.color_background = typedValue.data;

theme.resolveAttribute(R.attr.color_list_item_dark, typedValue, true);
vars.color_list_item_dark = typedValue.data;

theme.resolveAttribute(R.attr.color_list_item_light, typedValue, true);
vars.color_list_item_light = typedValue.data;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static void init_Drawer (Patriots_Ukraine app) {

vars.drawer = app.findViewById(R.id.drawer_layout);
NavigationView drawer_view = app.findViewById(R.id.drawer);

NavigationMenuView navigationMenuView = (NavigationMenuView) drawer_view.getChildAt(0);
if (navigationMenuView != null) {
    navigationMenuView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
}

ViewGroup.LayoutParams layoutParams = drawer_view.getLayoutParams();

if (vars.current_orientation == 1) { layoutParams.width = (int)(vars.display_width * 0.5);  }
else                              { layoutParams.width = (int)(vars.display_width * 0.75); }

drawer_view.setLayoutParams(layoutParams);
Utility.set_Menu_Selected(vars.app_menu_index);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void init_App_State (Patriots_Ukraine app) {

vars.layout_displayable.setVisibility(View.GONE);
vars.layout_settings.setVisibility(View.GONE);
vars.layout_search.setVisibility(View.GONE);
vars.layout_about.setVisibility(View.GONE);
vars.layout_logo.setVisibility(View.GONE);

vars.swipe_refresh.setEnabled(false);
app.findViewById(R.id.toolbar_container).setVisibility(View.VISIBLE);

// ................................................................................................

switch (String.valueOf(vars.app_state)) {

// ................................................................................................

case "1.0": // Початковий стан програми

vars.layout_displayable.setVisibility(View.VISIBLE);
vars.layout_logo.setVisibility(View.VISIBLE);

break;

// ................................................................................................

case "1.1": // Відображення списку новин

vars.layout_displayable.setVisibility(View.VISIBLE);
vars.swipe_refresh.setEnabled(true);
Utility.prepare_News_List();

break;

// ................................................................................................

case "1.2": // Відображення конкретно вибраної новини
case "3.1":

vars.layout_displayable.setVisibility(View.VISIBLE);
init_WebView_State(app);

break;

// ................................................................................................

case "2.0": // Відображеня поля пошуку та результату пошуку
case "2.1":

//vars.layout_displayable.setVisibility(View.VISIBLE);
vars.layout_search.setVisibility(View.VISIBLE);

break;

// ................................................................................................

case "2.2": // Відображення знайденої новини

vars.layout_displayable.setVisibility(View.VISIBLE);
init_WebView_State(app);

break;

// ................................................................................................

case "3.0": // Відображення списку улюблених сторінок

vars.layout_displayable.setVisibility(View.VISIBLE);
Utility.prepare_Favorite_List();

break;

// ................................................................................................

case "4.0": // Відображення налаштувань

vars.layout_settings.setVisibility(View.VISIBLE);

break;

// ................................................................................................

case "5.0": // Відображення інформації про програму

vars.layout_about.setVisibility(View.VISIBLE);

break;

}

// ................................................................................................

}

private static void init_WebView_State (Patriots_Ukraine app) {

vars.page_is_reopen = true;
vars.swipe_refresh.setVisibility(View.GONE);

vars.layout_logo.setVisibility(View.GONE);

// ................................................................................................

if (vars.page_is_favorite) { ((ImageView)app.findViewById(R.id.toolbar_icon_right))
            .setImageResource(R.drawable.ic_favorite_on); }
else                       { ((ImageView)app.findViewById(R.id.toolbar_icon_right))
            .setImageResource(R.drawable.ic_favorite_off); }

// ................................................................................................

String page_dir = "file://" + vars.folder_favor.getAbsolutePath() + "/";

if (vars.html.equals("error")) { vars.can_be_favorite = false;
                                 vars.error_page.setVisibility(View.VISIBLE);
                                 vars.web_view.loadDataWithBaseURL(page_dir, "",
                                                                   null, null, null); }

else { vars.can_be_favorite = true;
       vars.error_page.setVisibility(View.GONE);
       vars.web_view.loadDataWithBaseURL(page_dir, vars.html, null, null, null); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void init_Strings (Patriots_Ukraine app) {

Resources res = app.getResources();

vars.month_names[0]  = res.getString(R.string.month_01);
vars.month_names[1]  = res.getString(R.string.month_02);
vars.month_names[2]  = res.getString(R.string.month_03);
vars.month_names[3]  = res.getString(R.string.month_04);
vars.month_names[4]  = res.getString(R.string.month_05);
vars.month_names[5]  = res.getString(R.string.month_06);
vars.month_names[6]  = res.getString(R.string.month_07);
vars.month_names[7]  = res.getString(R.string.month_08);
vars.month_names[8]  = res.getString(R.string.month_09);
vars.month_names[9]  = res.getString(R.string.month_10);
vars.month_names[10] = res.getString(R.string.month_11);
vars.month_names[11] = res.getString(R.string.month_12);
vars.month_names[12] = res.getString(R.string.year);

Initialization.init_Settings_Strings(app);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void init_Settings_Strings (Patriots_Ukraine app) {

Resources res = app.getResources();

// Мова
//vars.settings[0][0] = res.getString(R.string.settings_language_00);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static void init_Swipe_Refresh (Patriots_Ukraine app) {

// News swipe refresh

vars.swipe_refresh = app.findViewById(R.id.swipe_refresh);
vars.swipe_refresh.setOnRefreshListener(View_Listener.swipe_listener);

vars.swipe_refresh.setSize(SwipeRefreshLayout.LARGE);
vars.swipe_refresh.setColorSchemeColors(vars.color_text_light);
vars.swipe_refresh.setProgressBackgroundColorSchemeColor(vars.color_primary);

Rect rect = new Rect();
app.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

float height = app.getWindowManager().getDefaultDisplay().getHeight() -
               vars.density * 80 - rect.top -
               app.getResources().getDimensionPixelSize(R.dimen.size_tool_bar) * 2 +
              (vars.status_bar ? 0 : vars.density * 25);

vars.swipe_refresh.setProgressViewOffset(true, (int)(height * 0.3), (int)(height * 0.5));
vars.swipe_refresh.setEnabled(false);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static void init_Next_Prev_Drawable (Patriots_Ukraine app) {

Resources.Theme theme = app.getTheme();
TypedValue typedValue = new TypedValue();

theme.resolveAttribute(R.attr.color_behavior_in, typedValue, true);
int behavior_in = typedValue.data;

theme.resolveAttribute(R.attr.color_behavior_out, typedValue, true);
int behavior_out = typedValue.data;

float corners = app.getResources().getDimension(R.dimen.size_tool_bar_half);
int stroke = (int) app.getResources().getDimension(R.dimen.stroke_width_positive);

next_prev_drawable = new GradientDrawable();
next_prev_drawable.setShape(GradientDrawable.RECTANGLE);
next_prev_drawable.setCornerRadius(corners);
next_prev_drawable.setColor(behavior_in);
next_prev_drawable.setStroke(stroke, behavior_out);

app.findViewById(R.id.get_prev).setBackgroundDrawable(next_prev_drawable);
app.findViewById(R.id.get_next).setBackgroundDrawable(next_prev_drawable);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private static void init_Share_Circle_Drawable (Patriots_Ukraine app) {

Resources.Theme theme = app.getTheme();
TypedValue typedValue = new TypedValue();

theme.resolveAttribute(R.attr.color_behavior_in, typedValue, true);
int behavior_in = typedValue.data;

theme.resolveAttribute(R.attr.color_behavior_out, typedValue, true);
int behavior_out = typedValue.data;

int stroke = (int) app.getResources().getDimension(R.dimen.stroke_width_positive);

share_circle_drawable = new GradientDrawable();
share_circle_drawable.setShape(GradientDrawable.OVAL);
share_circle_drawable.setStroke(stroke, behavior_out);
share_circle_drawable.setColor(behavior_in);

app.findViewById(R.id.news_link_im).setBackgroundDrawable(share_circle_drawable);
app.findViewById(R.id.share_twitter_im).setBackgroundDrawable(share_circle_drawable);
app.findViewById(R.id.share_facebook_im).setBackgroundDrawable(share_circle_drawable);
app.findViewById(R.id.search_progress_bar_im).setBackgroundDrawable(share_circle_drawable);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}