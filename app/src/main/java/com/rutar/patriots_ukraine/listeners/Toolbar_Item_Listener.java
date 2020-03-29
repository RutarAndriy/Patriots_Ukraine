package com.rutar.patriots_ukraine.listeners;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.custom_views.Dialog;
import com.rutar.patriots_ukraine.utils.Utility;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

public class Toolbar_Item_Listener {

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач toolbar елементів

public static void on_Toolbar_Item_Click (View view, Patriots_Ukraine app) {

if (vars.input_lock) { return; }

if (vars.drawer.isDrawerOpen(GravityCompat.START))
  { vars.drawer.closeDrawer(GravityCompat.START); }

// ................................................................................................

if (view.getId() == R.id.toolbar_text) {

switch (String.valueOf(vars.app_state)) {

    case "1.0":
    case "1.1": break;

    case "2.1": vars.layout_search.setVisibility(View.VISIBLE);
                Utility.start_Anim(vars.layout_search, vars.refresh_anim_show);
                Utility.toolbar_Views_Hide();
                break;

    case "3.0": vars.AZ_sort =! vars.AZ_sort;
                Utility.prepare_Favorite_List();
                Utility.set_Snackbar_Show_Time(1300);
                Utility.show_Snackbar("Відсортовано за зростанням", false);
                break;

}

Utility.start_Anim(view, R.anim.press_button);

}

// ................................................................................................

else if (view.getId() == R.id.toolbar_icon_left) {

switch (String.valueOf(vars.app_state)) {

case "1.2":
case "2.2":
case "3.1": break;

default: if (vars.drawer.isDrawerOpen(GravityCompat.START))
              { vars.drawer.closeDrawer(GravityCompat.START); }
         else { vars.drawer.openDrawer(GravityCompat.START);  }
         break;

}

Utility.start_Anim(view, R.anim.press_button);

}

// ................................................................................................

else if (view.getId() == R.id.toolbar_icon_right) {

switch (String.valueOf(vars.app_state)) {

    case "1.2":
    case "2.2":
    case "3.1": ImageView image = (ImageView) view;
                if (vars.can_be_favorite) { vars.page_is_favorite =! vars.page_is_favorite; }

                if (vars.page_is_favorite) { image.setImageResource(R.drawable.ic_favorite_on); }
                else                      { image.setImageResource(R.drawable.ic_favorite_off); }

                Utility.start_Anim(image, R.anim.press_star);
                break;

    case "1.0":
    case "1.1":
    case "2.0":
    case "3.0":
    case "4.0":
    case "5.0": Utility.start_Anim(view, R.anim.press_button);
                break;

}
}

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}