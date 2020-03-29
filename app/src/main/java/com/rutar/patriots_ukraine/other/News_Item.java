package com.rutar.patriots_ukraine.other;

import android.os.*;

public class News_Item implements Parcelable {

private String id;
private String time;
private String title;
private String page_url;
private String preview_url;
private String milliseconds;

///////////////////////////////////////////////////////////////////////////////////////////////////

public String get_Id() { return id; }
public void set_Id (String id) { this.id = id; }

public String get_Time() { return time; }
public void set_Time (String time) { this.time = time; }

public String get_Title() { return title; }
public void set_Title (String title) { this.title = title; }

public String get_Page_Url() { return page_url; }
public void set_Page_Url (String page_url) { this.page_url = page_url; }

public String get_Preview_Url() { return preview_url; }
public void set_Preview_Url (String preview_url) { this.preview_url = preview_url; }

public String get_Milliseconds() { return milliseconds; }
public void set_Milliseconds(String date_time) { this.milliseconds = date_time; }

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public String toString() { return "Id: " + id
                                + ", Time: " + time
                                + ", Title: " + title
                                + ", Page url: " + page_url
                                + ", Preview url: " + preview_url
                                + ", DateTime string: " + milliseconds; }

///////////////////////////////////////////////////////////////////////////////////////////////////

/*public String create_Sring_From_News_Item() {

return id + "|" +
       time + "|" +
       title + "|" +
       page_url + "|" +
       preview_url + "|" +
       milliseconds;

}*/

///////////////////////////////////////////////////////////////////////////////////////////////////

public void restore_News_Item_From_String (String news_item) {

/*if (news_item == null) { return; }

String[] elements = news_item.split("|");

id           = elements[0];
time         = elements[1];
title        = elements[2];
page_url     = elements[3];
preview_url  = elements[4];
milliseconds = elements[5];*/

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public int describeContents() { return 0; }

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
public void writeToParcel (Parcel dest, int flags) {

dest.writeStringArray(new String[] { id, time, title, page_url, preview_url, milliseconds});

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public News_Item() {}

///////////////////////////////////////////////////////////////////////////////////////////////////

public News_Item (Parcel parcel) {

String[] data = new String[6];
parcel.readStringArray(data);

id = data[0];
time = data[1];
title = data[2];
page_url = data[3];
preview_url = data[4];
milliseconds = data[5];

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public static final Parcelable.Creator<News_Item> CREATOR = new Parcelable.Creator<News_Item>() {

@Override
public News_Item createFromParcel (Parcel source) {
    return new News_Item(source);
}

@Override
public News_Item[] newArray (int size) {
        return new News_Item[size];
    }

};

///////////////////////////////////////////////////////////////////////////////////////////////////

}