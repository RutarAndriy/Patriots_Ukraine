package com.rutar.patriots_ukraine.listeners;

import android.view.View;

import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.Settings;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

public class Settings_Item_Listener {


    public static void on_Settings_Item_Click (View view, final Patriots_Ukraine app) {

        int id = view.getId();
        if (vars.input_lock) { return; }

        Settings.on_Settings_Item_Click(id);

    }


}
