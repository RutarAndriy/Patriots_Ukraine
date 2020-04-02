package com.rutar.patriots_ukraine.listeners;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.GravityCompat;
import android.view.View;

import com.rutar.patriots_ukraine.*;
import com.rutar.patriots_ukraine.utils.*;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

// ................................................................................................

public class Back_Key_Listener {

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void on_Back_Key_Click (Patriots_Ukraine app) {

// Кнопка неактивна, якщо ввід заблоковано
if (vars.input_lock) { return; }

// ................................................................................................
// Відкрито сторінку новин
if (vars.app_state == 1.2f ||
    vars.app_state == 2.2f ||
    vars.app_state == 3.1f) {

// Якщо відкритий behavior - закриваємо його
if (vars.behavior_web.getState() == BottomSheetBehavior.STATE_COLLAPSED)
    { vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN); }

// В іншому разі - переходимо назад
else { Toolbar_Item_Listener.on_Toolbar_Item_Click(app
                            .findViewById(R.id.toolbar_icon_left), app); }

}

// ................................................................................................
// Сторінка новин закрита
else {

// Якщо відкрите бокове меню, то закриваємо його
if (vars.drawer.isDrawerOpen(GravityCompat.START))
    { vars.drawer.closeDrawer(GravityCompat.START); }

// Відображається вікно пошуку новин
else if (vars.app_state == 2.0f)
    { Toolbar_Item_Listener.on_Toolbar_Item_Click(app
                           .findViewById(R.id.toolbar_icon_right), app); }

// Відображається список шуканих новин
else if (vars.app_state == 2.1f)
    { Toolbar_Item_Listener.on_Toolbar_Item_Click(app.findViewById(R.id.toolbar_text), app); }

// Налаштування -> Головне меню
else if (vars.app_state == 4.0f) {

    Utility.get_App_State_By_Data();
    vars.layout_displayable.setVisibility(View.VISIBLE);
    Utility.start_Anim(vars.layout_settings, vars.refresh_anim_hide);
    Utility.set_Menu_Selected(R.id.menu_news);
    Utility.toolbar_Views_Hide();

}

// Про програму -> Головне меню
else if (vars.app_state == 5.0f) {

    Utility.get_App_State_By_Data();
    vars.layout_displayable.setVisibility(View.VISIBLE);
    Utility.start_Anim(vars.layout_about, vars.refresh_anim_hide);
    Utility.set_Menu_Selected(R.id.menu_news);
    Utility.toolbar_Views_Hide();

}

// Улюблені -> Головне меню
else if (vars.app_state == 3.0f) {

Utility.get_App_State_By_Data();
//Utility.start_Anim(vars.layout_logo, vars.refresh_anim_show);
Utility.set_Menu_Selected(R.id.menu_news);
Utility.toolbar_Views_Hide();

//vars.layout_logo.setVisibility(View.VISIBLE);
//vars.show_logo_layout = true;

}

// Список новин -> Головне меню
/*else if (vars.app_state == 1 && !vars.show_logo_layout) {

    if (vars.behavior_list.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
        vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    else {

        Utility.start_Anim(vars.layout_logo, vars.refresh_anim_show);
        Utility.toolbar_Views_Hide();

        vars.layout_logo.setVisibility(View.VISIBLE);
        vars.show_logo_layout = true;

    }

}*/

// Виходимо кнопкою "Назад"
else { app.exit_App(); }

}

//vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);

}

// Кінець класу <Back_Key_Listener> ///////////////////////////////////////////////////////////////

}
