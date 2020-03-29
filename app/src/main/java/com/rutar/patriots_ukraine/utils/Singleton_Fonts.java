package com.rutar.patriots_ukraine.utils;

import android.content.*;
import android.graphics.*;
import android.content.res.*;
import com.rutar.patriots_ukraine.*;

// ................................................................................................

public class Singleton_Fonts {

private static Typeface[] fonts = null;
private static volatile Singleton_Fonts instance;

///////////////////////////////////////////////////////////////////////////////////////////////////

private Singleton_Fonts() {}

///////////////////////////////////////////////////////////////////////////////////////////////////

public Typeface get_Font (int index) {
        return fonts[index];
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

private static void set_Font (int index,
                              Typeface font) { Singleton_Fonts.fonts[index] = font; }

///////////////////////////////////////////////////////////////////////////////////////////////////

public static Singleton_Fonts get_Instance (Context context) {

Singleton_Fonts local_instance = instance;

if (local_instance == null) {

synchronized (Singleton_Fonts.class) {
    local_instance = instance;
    if (local_instance == null) { instance = local_instance = new Singleton_Fonts(); }
}

// ................................................................................................

AssetManager manager = context.getAssets();

for (int z = 0; z < Variables.font_list.length; z++) {
    set_Font(z, Typeface.createFromAsset(manager, Variables.font_list[z]));
}
}

return local_instance;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}