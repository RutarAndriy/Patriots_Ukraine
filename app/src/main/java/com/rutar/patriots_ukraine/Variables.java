package com.rutar.patriots_ukraine;

import android.content.SharedPreferences;
import android.view.*;
import android.widget.*;
import android.graphics.drawable.*;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rutar.patriots_ukraine.custom_views.Web_View;
import com.rutar.patriots_ukraine.other.News_Item;
import com.rutar.patriots_ukraine.other.Recycler_Adapter;

import java.io.File;
import java.util.ArrayList;

// ................................................................................................

public class Variables {

public static String TAG = "Patriots_Ukraine";                                               // TAG
public static String FONT_NAME = "Ubuntu.ttf";

public float app_state = 1.0f;
public boolean input_lock;

// ................................................................................................

public static final String[] font_list = new String[] {

    "TruthCYR_Light.otf",    "VDS_Thin.ttf",                                     // Звичайні шрифти
    "GOST_2.304_A.ttf",      "ISOCPEUR.ttf",
    "Azbuka04.ttf",          "BenguiatGothicC.ttf",
    "Courier10_Win95BT.ttf", "a_OldTyper.ttf",
    "Ubuntu.ttf",            "Yeseva_One.ttf",

    "Anonymous_Pro.ttf",     "Atavyros.otf",                                     // Курсивні шрифти
    "SplendorC.ttf",         "Segoe_Script.ttf",

    "B52.ttf",               "iFlash_502.ttf"                                  // Спеціальні шрифти

};

///////////////////////////////////////////////////////////////////////////////////////////////////

public int theme;
public int language;
public int image_quality;
public int page_font_size;
public int orientation;
public int preview_anim;
public int refresh_anim_show;
public int refresh_anim_hide;
public int toolbar_anim_show;
public int toolbar_anim_hide;

public boolean status_bar;
public boolean show_preview;
public boolean web_behavior;
public boolean web_behavior_f;
public boolean web_behavior_t;
public boolean web_behavior_l;

// ................................................................................................

public int dialog_index = -1;
public static String URL = "";

public int share_index = -1;
public int settings_index = -1;

public int theme_now;

public boolean AZ_sort;
public boolean search_mode;

public boolean page_is_favorite;
public boolean news_list_done;

public String html = "";
public String search_text = "";

public String version_name = "unknown";
public int version_code = 0;

public SharedPreferences pref_settings;

public ArrayList <News_Item> news_array;
public ArrayList <News_Item> favor_array;

public News_Item news_item;

public File page_file;
public File folder_prev;
public File folder_favor;

public int app_menu_index;
public int app_load_count;

public boolean app_is_rate;
public boolean app_debug_mode;
public boolean can_be_favorite;

public boolean page_is_reopen;

public int last_news_list_index;
public int last_news_list_position;

public int current_news_list_index;
public int current_news_list_position;

public double web_view_scroll_position;

public ViewGroup.LayoutParams lp_web;

public boolean done_press_search    = true;

public int display_width;
public int display_height;
public int current_orientation;

public float density;
public boolean news_list_reach_end;

public DrawerLayout drawer;
public BottomSheetBehavior behavior_web;
public BottomSheetBehavior behavior_list;
public BottomSheetBehavior behavior_snack_bar;



public SwipeRefreshLayout swipe_refresh;

public String[] month_names = new String[13];

public RecyclerView news_list;

public int[] date = new int[3];

public View layout_logo;
public View layout_search;
public View search_progress_bar;

public Web_View web_view;
public ImageView logo_app;

public Recycler_Adapter news_adapter;

public final String folder_name_prev  = "preview";
public final String folder_name_favor = "favorites";

public View menu_news;
public View menu_favorite;
public View menu_settings;
public View menu_about;
public View menu_exit;

public View layout_displayable;
public View layout_settings;
public View layout_about;

public View error_list;
public View error_page;

public TextView toolbar_title;
public ImageView toolbar_left;
public ImageView toolbar_right;
public EditText search_text_field;
public TextView behavior_snack_bar_text;

public GradientDrawable circle_background;
public LinearLayoutManager recycler_layout_manager;

public int color_accent;
public int color_primary;
public int color_primary_dark;

public int color_link;
public int color_text_dark;
public int color_text_light;
public int color_background;
public int color_list_item_dark;
public int color_list_item_light;

public long back_pressed = 0;

}