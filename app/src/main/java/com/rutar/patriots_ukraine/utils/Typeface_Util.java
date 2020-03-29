package com.rutar.patriots_ukraine.utils;

import android.util.*;
import android.content.*;
import android.graphics.*;
import java.lang.reflect.*;

import static com.rutar.patriots_ukraine.Variables.TAG;

// ................................................................................................

public class Typeface_Util {

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void override_Font (Context context,
                                  String custom_font) {
try {

final Typeface custom_Typeface = Typeface.createFromAsset(context.getAssets(), custom_font);
final Field default_Font_Typeface_Field = Typeface.class.getDeclaredField("SERIF");
default_Font_Typeface_Field.setAccessible(true);
default_Font_Typeface_Field.set(null, custom_Typeface);

}

catch (Exception e) {
    Log.e(TAG, "Can not set custom font " + custom_font + " instead of SERIF");
}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

}