package com.rutar.patriots_ukraine.other;

import java.util.*;

import android.util.*;
import android.view.*;
import android.widget.*;
import android.content.*;

import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.listeners.List_Item_Listener;
import com.rutar.patriots_ukraine.listeners.View_Listener;
import com.rutar.patriots_ukraine.process.Process_Preview;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

///////////////////////////////////////////////////////////////////////////////////////////////////

public class List_Adapter extends BaseAdapter {

private final LayoutInflater inflater;
private final ArrayList <News_Item> news;

///////////////////////////////////////////////////////////////////////////////////////////////////

public List_Adapter (Context context, ArrayList <News_Item> news) {

this.news = news;
inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public int getCount() {

switch (vars.current_orientation) {

    case 0:  return news.size();
    default: return news.size()%2==0 ? news.size()/2 : (news.size()+1)/2;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public Object getItem (int position) {
        return news.get(position);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public long getItemId (int position) {
        return position;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public View getView (int position, View convert_view, ViewGroup parent) {

///////////////////////////////////////////////////////////////////////////////////////////////////
// Портретна орієнтація екрану

if (vars.current_orientation == 0) {

// Такий код іноді зумовлює помилки у list_view
 View view = convert_view;
   if (view == null) { view = inflater.inflate(R.layout.layout_list_item, null); }

//View view = inflater.inflate(R.layout.layout_list_item, null);

((TextView) view.findViewById(R.id.list_item_time)).setText(news.get(position).get_Time());
((TextView) view.findViewById(R.id.list_item_title)).setText(news.get(position).get_Title());
((TextView) view.findViewById(R.id.list_item_title)).setTextColor(vars.color_text_dark);

ImageView preview = (ImageView) view.findViewById(R.id.list_item_preview);
preview.setImageBitmap(null);

// ................................................................................................

set_Element_Colors(true, view, position);

// ................................................................................................

if (vars.show_preview) {

    preview.setVisibility(View.VISIBLE);
    Process_Preview.get_Instance().bind_Image(news.get(position).get_Preview_Url(), preview);

}

else { preview.setVisibility(View.GONE); }

view.setOnClickListener(listener);
view.setTag(position);

return view;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Ландшафтна орієнтація екрану

else {

View view = null;
LinearLayout layout = new LinearLayout(app);
layout.setOrientation(LinearLayout.HORIZONTAL);

LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                       LinearLayout.LayoutParams.MATCH_PARENT,
                                       LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

///////////////////////////////////////////////////////////////////////////////////////////////////

for (int z = 0; z < 2; z++) {

int pos = position * 2 + z;

if (layout.getChildCount() < 2) { view = inflater.inflate(R.layout.layout_list_item, null); }
else { view = layout.getChildAt(z); }

try {

    ImageView preview = ((ImageView) view.findViewById(R.id.list_item_preview));
    preview.setImageBitmap(null);

    ((TextView) view.findViewById(R.id.list_item_time)).setText(news.get(pos).get_Time());
    ((TextView) view.findViewById(R.id.list_item_title)).setText(news.get(pos).get_Title());
    ((TextView) view.findViewById(R.id.list_item_title)).setTextColor(vars.color_text_dark);

    // ............................................................................................

    set_Element_Colors(false, view, pos);

    // ............................................................................................

    if (vars.show_preview) {

        preview.setVisibility(View.VISIBLE);
        Process_Preview.get_Instance().bind_Image(news.get(pos).get_Preview_Url(), preview);

    }

    else { preview.setVisibility(View.GONE); }

}

catch (Exception e) {

    view.findViewById(R.id.list_item_time).setVisibility(View.GONE);
    view.findViewById(R.id.list_item_title).setVisibility(View.GONE);

    view.setBackgroundColor((pos + 1) % 4 > 1 ? vars.color_list_item_light :
                                                vars.color_list_item_dark);

}

///////////////////////////////////////////////////////////////////////////////////////////////////

view.setOnClickListener(listener);
view.setLayoutParams(params);
view.setTag(pos);

layout.addView(view);

}

return layout;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void set_Element_Colors (boolean is_Portrait_Orientation, View view, int position) {

// ................................................................................................
// Портретна орієнтація екрану

if (is_Portrait_Orientation) {

if (position % 2 == 0) { view.setBackgroundColor(vars.color_list_item_light); }
else                   { view.setBackgroundColor(vars.color_list_item_dark);  }

}

// ................................................................................................
// Ландшафтна орієнтація екрану

else {

if ((position + 1) % 4 > 1) { view.setBackgroundColor(vars.color_list_item_light); }
else                        { view.setBackgroundColor(vars.color_list_item_dark);  }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

View.OnClickListener listener = new View.OnClickListener() {

@Override
public void onClick (View view) {

try { List_Item_Listener.on_List_Item_Click(news.get((int)view.getTag())); }
catch (Exception e) { Log.d(vars.TAG, "Click on empty item"); }

// Show debug info
if (app.findViewById(R.id.debug_view).getVisibility() == View.VISIBLE) {

    Toast.makeText(app, "Position: " + view.getTag(), Toast.LENGTH_SHORT).show();

}
}

};

///////////////////////////////////////////////////////////////////////////////////////////////////

}