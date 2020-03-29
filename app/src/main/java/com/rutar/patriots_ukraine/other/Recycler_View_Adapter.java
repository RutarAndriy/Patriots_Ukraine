package com.rutar.patriots_ukraine.other;

import java.util.*;

import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.Variables;
import com.rutar.patriots_ukraine.listeners.Listener;
import com.rutar.patriots_ukraine.process.Process_Preview;

import android.support.v7.widget.RecyclerView;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.app;
import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

// ................................................................................................

public class Recycler_View_Adapter extends
             RecyclerView.Adapter <Recycler_View_Adapter.News_View_Holder>{

private List <News_Item> news;

///////////////////////////////////////////////////////////////////////////////////////////////////

public Recycler_View_Adapter (List <News_Item> news) { this.news = news; }

///////////////////////////////////////////////////////////////////////////////////////////////////
// Допоміжний клас, який представляє дані елемента списку

public static class News_View_Holder extends RecyclerView.ViewHolder {

TextView time;
TextView title;
ImageView preview;
LinearLayout layout;

News_View_Holder (View item_view) {

    super(item_view);
    layout = item_view.findViewById(R.id.list_item);
    time = item_view.findViewById(R.id.list_item_time);
    title = item_view.findViewById(R.id.list_item_title);
    preview = item_view.findViewById(R.id.list_item_preview);

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public int getItemCount() {
        return news.size();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public News_View_Holder onCreateViewHolder (ViewGroup group, int i) {

View view = LayoutInflater.from(group.getContext())
                          .inflate(R.layout.layout_list_item, group, false);

final News_View_Holder view_holder = new News_View_Holder(view);

view.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View it) {

        int adapter_position = view_holder.getAdapterPosition();
        if (adapter_position != RecyclerView.NO_POSITION) {
            Recycler_View_Adapter.this.item_Clicked(adapter_position);
        }

    }
});

return view_holder;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public void onBindViewHolder (News_View_Holder holder, int position) {

holder.time.setText(news.get(position).get_Time());
holder.title.setText(news.get(position).get_Title());
holder.preview.setImageResource(0);

Process_Preview.get_Instance().bind_Image(news.get(position).get_Preview_Url(), holder.preview);

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Прослуховування натискань на список

private void item_Clicked (int position) {

if (vars.news_list_reach_end) { vars.behavior_list.setState(BottomSheetBehavior.STATE_HIDDEN);
                                vars.news_list_reach_end = false;
                                return; }

try { Listener.on_List_Item_Click(news.get(position)); }
catch (Exception e) { Log.e(Variables.TAG, "Click on empty item"); }

// Show debug info
if (app.findViewById(R.id.debug_view).getVisibility() == View.VISIBLE) {
    Toast.makeText(app, "Position: " + position, Toast.LENGTH_SHORT).show();
}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

}