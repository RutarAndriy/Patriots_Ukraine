package com.rutar.patriots_ukraine.listeners;

import android.os.Handler;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.other.News_Item;
import com.rutar.patriots_ukraine.process.Process_Get_Page;
import com.rutar.patriots_ukraine.utils.Utility;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

public class List_Item_Listener {

///////////////////////////////////////////////////////////////////////////////////////////////////

public static void on_List_Item_Click (final News_Item item) {

if (vars.input_lock) { return; }

if (vars.behavior_list.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
    vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);
    return;
}

// ................................................................................................

vars.input_lock = true;
vars.swipe_refresh.setRefreshing(true);
vars.behavior_web.setState(BottomSheetBehavior.STATE_HIDDEN);
vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);

Utility.show_Snack_Bar(Utility.get_String(R.string.progress_get_page), true);

Handler handler = new Handler();
handler.postDelayed(new Runnable() {

    @Override
    public void run() {

        vars.news_item = item;
        new Process_Get_Page().execute(vars.news_item);

    }

}, 1000);
}

///////////////////////////////////////////////////////////////////////////////////////////////////

}