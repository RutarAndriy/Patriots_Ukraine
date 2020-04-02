package com.rutar.patriots_ukraine.utils;

import java.util.*;
import android.os.*;
import android.content.*;
import android.annotation.*;
import android.content.res.*;
import com.rutar.patriots_ukraine.*;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

// ................................................................................................
// Sources by Bassel Mourjan

public class Context_Wrapper extends ContextWrapper {

///////////////////////////////////////////////////////////////////////////////////////////////////

public Context_Wrapper(Context base) { super(base); }

///////////////////////////////////////////////////////////////////////////////////////////////////

@SuppressWarnings("deprecation")
public static ContextWrapper wrap (Context context) {

vars = new Variables();
vars.pref_settings  = context.getSharedPreferences("app_settings", context.MODE_PRIVATE);

Configuration config = context.getResources().getConfiguration();

String language = "uk";
config.fontScale = 1.0f;
Locale sysLocale = null;

Typeface_Util.override_Font(context, "fonts/" + Variables.FONT_NAME);

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { sysLocale = getSystemLocale(config); }
else                                                { sysLocale = getSystemLocaleLegacy(config); }

// ................................................................................................

if (!language.isEmpty() && !sysLocale.getLanguage().equals(language)) {

Locale locale = new Locale(language);
Locale.setDefault(locale);

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { setSystemLocale(config, locale); }
else                                                { setSystemLocaleLegacy(config, locale); }

}

// ................................................................................................

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
    { context = context.createConfigurationContext(config); }
else
    { context.getResources().updateConfiguration(config,
                                                 context.getResources().getDisplayMetrics()); }

// ................................................................................................

return new Context_Wrapper(context);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@SuppressWarnings("deprecation")
public static Locale getSystemLocaleLegacy (Configuration config){
    return config.locale;
}

///////////////////////////////////////////////////////////////////////////////////////////////////

@TargetApi(Build.VERSION_CODES.N)
public static Locale getSystemLocale (Configuration config) { return config.getLocales().get(0); }

///////////////////////////////////////////////////////////////////////////////////////////////////

@SuppressWarnings("deprecation")
public static void setSystemLocaleLegacy (Configuration config,
                                          Locale locale) { config.locale = locale; }

///////////////////////////////////////////////////////////////////////////////////////////////////

@TargetApi(Build.VERSION_CODES.N)
public static void setSystemLocale (Configuration config,
                                    Locale locale) { config.setLocale(locale); }

///////////////////////////////////////////////////////////////////////////////////////////////////

}