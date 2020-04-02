package com.rutar.patriots_ukraine.listeners;

import android.view.View;
import android.view.animation.AnimationUtils;

import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.custom_views.Dialog;
import com.rutar.patriots_ukraine.utils.Utility;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

public class Item_Listener {


    public static void on_Item_Click (View view, final Patriots_Ukraine app) {

        int id = view.getId();
        if (vars.input_lock) { return; }

        switch (view.getId()) {

            // Натискання на Тризуб
            case R.id.logo_app: Utility.start_Anim(vars.logo_app, R.anim.press_logo);
                vars.input_lock = true;
                return;

            // Ствердна відповідь у діалоговому вікні
            case R.id.button_positive:

                switch (vars.dialog_index) {

                    // Вибір дати
                    case Dialog.Dialog_Date_Picker:

                        vars.date[0] = Dialog.tmp_date[0];
                        vars.date[1] = Dialog.tmp_date[1];
                        vars.date[2] = Dialog.tmp_date[2];

                        //vars.app_state = 1;
                        vars.search_text = "";
                        Dialog.dismiss_Dialog();
                        Utility.set_Toolbar_Title();
                        vars.swipe_refresh.setEnabled(true);

                        //if (vars.show_logo_layout) {
                        //    Utility.start_Anim(vars.logo_app, R.anim.press_logo);
                        //}

                        //else { swipe_listener.onRefresh(); }
                        break;

                    // Видалення улюблених сторінок
                    case Dialog.Dialog_Favorite_Info:

                        Utility.delete_Dir(vars.folder_favor);
                        vars.folder_favor.mkdir();

                        Utility.prepare_Favorite_List();
                        Dialog.dismiss_Dialog();
                        break;

                }

                break;

            // Заперечна відповідь у діалоговому вікні
            case R.id.button_negative: Dialog.dismiss_Dialog();
                break;

            // Нейтральна відповідь у діалоговому вікні
            case R.id.button_neutral:

                switch (vars.dialog_index) {

                    case -1: break;
                    default: Utility.start_Preview_Anim(); break;

                }

                break;

            // Кнопка пошуку новин
            case R.id.search_button:

                if (vars.search_text_field.getText().toString().isEmpty()) {

                    Utility.show_Snack_Bar(Utility.get_String(R.string.error_search_value_is_empty), false);
                    return;

                }

                vars.search_text_field.setEnabled(false);
                vars.search_progress_bar.setVisibility(View.VISIBLE);
                vars.search_progress_bar.startAnimation(AnimationUtils
                        .loadAnimation(app, R.anim.search_progress));

                Utility.search_News();
                break;

            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12: vars.settings_index = view.getId();
                     //vars.show_dialog = 3;
                break;

        }

        if (id > 12) { Utility.start_Anim(view, R.anim.press_button);   }
        else         { Utility.start_Anim(view, R.anim.press_settings); }

    }


}
