package com.rutar.patriots_ukraine;

import java.util.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.settings_preview;

///////////////////////////////////////////////////////////////////////////////////////////////////

class List_Adapter extends BaseAdapter {

private final int color_1;
private final int color_2;

private final LayoutInflater inflater;
private final ArrayList <News_Item> news;

///////////////////////////////////////////////////////////////////////////////////////////////////

public List_Adapter (Context context, ArrayList <News_Item> news) {

this.news = news;
inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

TypedValue typedValue = new TypedValue();
Resources.Theme theme = context.getTheme();

theme.resolveAttribute(R.attr.list_item_color_1, typedValue, true);
color_1 = typedValue.data;

theme.resolveAttribute(R.attr.list_item_color_2, typedValue, true);
color_2 = typedValue.data;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public int getCount() {
        return news.size();
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
public View getView (int position, View convertView, ViewGroup parent) {

View view = convertView;

if (view == null) { view = inflater.inflate(R.layout.list_item, null); }

final TextView time     = (TextView)  view.findViewById(R.id.time);
final TextView text     = (TextView)  view.findViewById(R.id.text);
final ImageView preview = (ImageView) view.findViewById(R.id.preview);

time.setText(news.get(position).get_Time());
text.setText(news.get(position).get_Title());
preview.setImageBitmap(null);

view.setBackgroundColor(position%2 == 0 ? color_1 : color_2);

if (settings_preview == 0) { preview.setVisibility(View.VISIBLE);
                             Preview_Loader.get_Instance().bind_Image(news
                                           .get(position).get_Preview_Url(), preview); }

else { preview.setVisibility(View.GONE); }

return view;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}