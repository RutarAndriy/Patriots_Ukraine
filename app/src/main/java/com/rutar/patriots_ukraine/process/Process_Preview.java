package com.rutar.patriots_ukraine.process;

import java.util.*;
import android.os.*;
import android.util.*;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.graphics.*;

import com.rutar.patriots_ukraine.Patriots_Ukraine;
import com.rutar.patriots_ukraine.R;
import com.rutar.patriots_ukraine.Settings;
import com.rutar.patriots_ukraine.utils.Utility;

import static com.rutar.patriots_ukraine.Patriots_Ukraine.app;
import static com.rutar.patriots_ukraine.Patriots_Ukraine.vars;

import static com.rutar.patriots_ukraine.Variables.*;

// ................................................................................................

public class Process_Preview {

private static Process_Preview instance;                            // Зразок класу Process_Preview
private boolean queue_thread_paused = false;

private Thread queue_thread;                                                 // Потік черги обробки
private LinkedList <Queue_Object> queue_list;                           // Черга оброблених запитів
private HashMap <ImageView, Url_Getter> hash_map;                     // Масив необроблених запитів

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод дозволяє отримати екземпляр даного зласу

public static Process_Preview get_Instance() {

    if (instance == null) { instance = new Process_Preview(); }
    return instance;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Конструктор класу

private Process_Preview() {

hash_map = new HashMap();
queue_list = new LinkedList();

start_Queue_Thread();

}

///////////////////////////////////////////////////////////////////////////////////////////////////

private void start_Queue_Thread() {

queue_thread = new Thread(new Runnable() {

@Override
public void run() {

while (true) {

// Призупинення потоку, якщо немає задач
if (queue_list.isEmpty()) { pause_Thread(); }

final Queue_Object object = queue_list.poll();
final Bitmap bitmap = Utility.get_Preview_Bitmap(object);

// ................................................................................................

app.runOnUiThread(new Runnable() {

@Override
public void run() {

    ImageView image_view        = object.get_Object_View();
    boolean   preview_not_exist = object.is_Preview_Not_Exist();

    // Стаття не має прев'ю
    if (preview_not_exist)   { image_view.setImageResource(R.drawable.im_preview_not_exist); }

    // Помилка завантаження прев'ю
    else if (bitmap == null) { image_view.setImageResource(R.drawable.im_preview_not_found); }

    // Прев'ю успішно завантажено
    else { image_view.setImageBitmap(bitmap); }

    // ............................................................................................

    image_view.startAnimation(AnimationUtils.loadAnimation(app, vars.preview_anim));

}});

// ................................................................................................

try { Thread.sleep(50); }
catch (Exception e) {}

}
}

});

// ................................................................................................

queue_thread.setPriority(Thread.MIN_PRIORITY);
queue_thread.start();

Log.i(TAG, "Queue thread start");

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод завантажує зображення по url та присвоює його image_view

public void bind_Image (final String image_Url, final ImageView image_View) {

Url_Getter getter = hash_map.get(image_View);

// Якщо немає запиту за даним image_view, то створюємо його і додаємо в список
if (getter == null) { getter = new Url_Getter(image_View);
                      getter.update_Url(image_Url);
                      hash_map.put(image_View, getter);
                      getter.execute(); }

// Якщо потік є, то оновлюємо його дані
else { getter.update_Url(image_Url); }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Клас дозволяє отримати актуальн значення url зображення

private class Url_Getter extends AsyncTask <Void, Void, Void> {

private String    getter_url;                            // URL, по якому завантажується зображення
private ImageView getter_view;        // Екземпляр image_View для якого необхідно задати зображення

private final long DELAY_TIME = 400;                             // Час "спокою" для url зображення

// ................................................................................................

private long last_url_change_time;                 /* У даній меремінній зберігається час останньої
                                                    зміни url зображення. Згідно специфіки ListView
                                        даний url може сильно "стрибати, а отже перед тим як задати
                          зображення для image_View слід зачекати, поки url перейде в "стан спокою"
                    і перестане змінюватися. Лише тоді по заданому url можна скачувати зображння */

///////////////////////////////////////////////////////////////////////////////////////////////////
// Конструктор

public Url_Getter (ImageView getter_view) {

    this.getter_url = null;
    this.getter_view = getter_view;
    this.last_url_change_time = System.currentTimeMillis();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Метод оновлює url завантажуваного зображення

public void update_Url (String new_url) {

    this.getter_url = new_url;
    this.last_url_change_time = System.currentTimeMillis();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Виконання задачі у фоні

@Override
protected Void doInBackground (Void... voids) {

// Чекаємо поки ImageView стане стабільним
while ((System.currentTimeMillis() - last_url_change_time) < DELAY_TIME) {

    try { Thread.sleep(DELAY_TIME); }
    catch (Exception e) {}

}

return null;

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Завершення виконання задачі

@Override
protected void onPostExecute (Void object) {

queue_list.add(new Queue_Object(getter_url, getter_view));
hash_map.remove(getter_view);

resume_Thread();

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Клас - об'єкт запиту черги

public class Queue_Object {

private String object_url;
private ImageView object_view;
private boolean preview_not_exist;

// ................................................................................................

public Queue_Object (String object_url,
                     ImageView object_view) {

this.object_url        = object_url;
this.object_view       = object_view;

this.preview_not_exist = false;

}

// ................................................................................................

public String    get_Object_Url()       { return object_url;        }
public ImageView get_Object_View()      { return object_view;       }

public boolean   is_Preview_Not_Exist() { return preview_not_exist; }

// ................................................................................................

public void set_Preview_Not_Exist (boolean exist) { this.preview_not_exist = exist; }

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Призупинення потоку обробки черги

private void pause_Thread() {

    synchronized (queue_list) {

        queue_thread_paused = true;
        Log.i(TAG, "Queue thread is paused");

        try { queue_list.wait(); }
        catch (Exception e) {}

    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Поновлення потоку обробки черги

public void resume_Thread() {

    synchronized (queue_list) {

        if (!vars.news_list_done) { return; }

        if (queue_thread_paused) {
            Log.i(TAG, "Queue thread is resumed");
        }

        queue_thread_paused = false;
        queue_list.notify();

    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////

}
