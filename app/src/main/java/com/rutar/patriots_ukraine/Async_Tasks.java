package com.rutar.patriots_ukraine;

import java.io.*;
import java.net.*;
import java.util.*;
import android.os.*;
import android.view.*;
import android.util.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import java.util.concurrent.*;
import android.view.animation.*;

import static com.rutar.patriots_ukraine.Utilities.*;
import static com.rutar.patriots_ukraine.Patriots_Ukraine.*;

public class Async_Tasks {

///////////////////////////////////////////////////////////////////////////////////////////////////
// Даний клас оброблює html сторінку та завантажує необхідні ресурси

public static class Site_Preparation extends AsyncTask <Void, Void, Void> {

private Intent intent = null;

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected Void doInBackground (Void... params) {

    long start_time = System.currentTimeMillis();

    try { html = Html_Processor.process(list_item); }

    catch (Exception e)
        { html = null;
          Log.e(TAG, "Error: " + e.getMessage()); }

    intent = new Intent(patriots, Web_Activity.class);

    long done_time = System.currentTimeMillis() - start_time;
    long sleep_time = 700 - done_time;

    try { Thread.sleep(sleep_time > 0 ? sleep_time : 1); }
    catch (Exception e) {}

    return null;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onPostExecute (Void result) {

    super.onPostExecute(result);

    progress.dismiss();
    patriots.startActivity(intent);
    patriots.overridePendingTransition(R.anim.start_activity, R.anim.close_activity);

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Даний клас оброблює html та отримує список новин

public static class Site_Parsing extends AsyncTask <String, Void, Void > {

@Override
protected Void doInBackground (String... arg) {

    try {

    long start_time = System.currentTimeMillis();

    // Отримуємо список новин
    news_array = Html_Processor.get_News(arg[0]);

    long work_time = System.currentTimeMillis() - start_time;
    long sleep_time = 1000 - work_time;

    Thread.sleep(sleep_time > 0 ? sleep_time : 1);

    }

    catch (Exception e) { news_array = null;
                          print_Stack_Trace(e); }

    return null;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onPostExecute (Void output) {

    progress.dismiss();

    if (news_array == null) { news_adapter = new List_Adapter(patriots, new ArrayList<News_Item>());
                              news_list.setAdapter(news_adapter);
                              error_view.setVisibility(View.VISIBLE); }

    else {

        news_adapter = new List_Adapter(patriots, news_array);
        news_list.setAdapter(news_adapter); }

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Даний клас давантажує та кешує зображеня

public static class Web_Image_Async_Task extends AsyncTask {

private String latestURL;
private long lastQueueTime;
private final ImageView imageView;
private final HashMap <ImageView, Web_Image_Async_Task> imageThreadMap;

private static final long DELAY_TIME = 50;

///////////////////////////////////////////////////////////////////////////////////////////////////

public Web_Image_Async_Task(ImageView imageView,
                            HashMap <ImageView, Web_Image_Async_Task> imageThreadMap) {

this.imageView = imageView;
this.imageThreadMap = imageThreadMap;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public void pushUrl (String queueURL) {

latestURL = queueURL;
lastQueueTime = System.currentTimeMillis();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private class Status { boolean status; }

///////////////////////////////////////////////////////////////////////////////////////////////////

private void sleep() { try { Thread.sleep(50); }
                       catch (Exception e) { Log.e(TAG, e.toString()); }
}

///////////////////////////////////////////////////////////////////////////////////////////////////

private boolean setImageViewInMainThread (final ImageView imageView,
                                          final Bitmap bitmap,
                                          final String downloadedURL) {

final Semaphore semaphore = new Semaphore(0);
final Status isNewImageSet = new Status();

Handler handler = new Handler(Looper.getMainLooper());
handler.post(new Runnable() {
@Override
public void run() {

if (downloadedURL.equals(latestURL)) {

imageView.startAnimation(AnimationUtils.loadAnimation(patriots, R.anim.preview_load));

if (bitmap != null) { imageView.setImageBitmap(bitmap); }
else                { imageView.setImageResource(R.mipmap.no_image); }

imageThreadMap.remove(imageView);
isNewImageSet.status = true;

}

else { isNewImageSet.status = false; }

semaphore.release();

}
});

///////////////////////////////////////////////////////////////////////////////////////////////////

try { semaphore.acquire(); }
catch (Exception e) { Log.e(TAG, e.toString()); }

return isNewImageSet.status;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected Object doInBackground (Object... params) {
boolean isImageViewSet = false;

while (!isImageViewSet) {

// Чекаємо поки ImageView стане стабільним
while ((System.currentTimeMillis() - lastQueueTime) < DELAY_TIME) { sleep(); }

String downloadURL = latestURL;
Bitmap bitmap = null;

///////////////////////////////////////////////////////////////////////////////////////////////////
// Посилання яке починається із символів http:// позначає прев'ю, яке необхідно завантажити

if (downloadURL.startsWith("http://")) {

// Якщо прев'ю у кеші - дістаємо його, в іншому випадку - завантажуємо і кешуємо
if (new File(temp_folder_prev, process_Name(downloadURL)).exists()) {

bitmap = load_Bitmap_From_Storage(process_Name(downloadURL), null);

}

// Якщо сторінка не має прев'ю, то повертаємо відповідне зображення
else if (downloadURL.endsWith("noImage.png")) { bitmap = BitmapFactory
                    .decodeResource(patriots.getResources(), R.mipmap.no_image); }

///////////////////////////////////////////////////////////////////////////////////////////////////

else {

    try { HttpURLConnection connection = (HttpURLConnection) new URL(downloadURL).openConnection();
          connection.connect();

          InputStream input = connection.getInputStream();
          bitmap = BitmapFactory.decodeStream(input);

          save_Bitmap_To_Storage(bitmap, process_Name(downloadURL), null); }

    catch (Exception e) { Log.e(TAG, e.toString()); }

    }

}

// Посилання some_folder/some_preview позначає прев'ю улюбленої сторінки
else { bitmap = load_Bitmap_From_Storage(process_Name(downloadURL),
       new File(temp_folder_favor.getAbsolutePath() + new File(downloadURL).getParent())); }

///////////////////////////////////////////////////////////////////////////////////////////////////

isImageViewSet = setImageViewInMainThread(imageView, bitmap, downloadURL);

}

return null;

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

}