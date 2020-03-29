package com.rutar.patriots_ukraine.listeners;

import android.view.View;

import com.rutar.patriots_ukraine.Initialization;
import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.utils.Utility;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

public class Menu_Item_Listener {

private static int debug_count;

    ///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховувач елементів меню

    public static void on_Menu_Item_Click (View view, Patriots_Ukraine app) {

        if (vars.input_lock) { return; }
        vars.error_list.setVisibility(View.GONE);

        switch (view.getId()) {

// ................................................................................................

            case R.id.menu_news: // Елемент меню "Новини"

                Utility.get_App_State_By_Data();
                Initialization.init_App_State(app);
                break;

// ................................................................................................

            case R.id.menu_favorite: // Елемент меню "Улюблені"

                vars.app_state = 3.0f;
                Initialization.init_App_State(app);
                break;

// ................................................................................................

            case R.id.menu_settings: // Елемент меню "Налаштування"

                vars.app_state = 4.0f;
                Initialization.init_App_State(app);
                break;

// ................................................................................................

            case R.id.menu_about: // Елемент меню "Про грограму"

                vars.app_state = 5.0f;
                Initialization.init_App_State(app);
                break;

// ................................................................................................

            case R.id.menu_exit: // Елемент меню "Вихід"

                app.exit_App();
                break;

// ................................................................................................

            case R.id.logo_menu: // Логотип

                debug_count++;

                if (debug_count == 3) { vars.app_debug_mode = true;
                    app.findViewById(R.id.debug_view).setVisibility(View.VISIBLE); }
                if (debug_count >= 4) { debug_count = 0;
                    vars.app_debug_mode = false;
                    app.findViewById(R.id.debug_view).setVisibility(View.GONE); }

                break;

        }

// ................................................................................................

        Utility.set_Menu_Selected(view.getId());
        Utility.toolbar_Views_Hide();
        Utility.update_Drawer_State();
        Utility.close_Drawer_Layout();

    }


}
