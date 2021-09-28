package com.rutar.patriots_ukraine;

import java.util.*;
import android.widget.*;

import static com.rutar.patriots_ukraine.Async_Tasks.*;

///////////////////////////////////////////////////////////////////////////////////////////////////

public class Preview_Loader {

// Зразок класу Preview_Loader
private static Preview_Loader instance;

// Масив зберігає потоки, які обробляють конкретне зображення
private final HashMap <ImageView, Web_Image_Async_Task> thread_list;

///////////////////////////////////////////////////////////////////////////////////////////////////

private Preview_Loader() { thread_list = new HashMap <ImageView, Web_Image_Async_Task>(); }

///////////////////////////////////////////////////////////////////////////////////////////////////

public static Preview_Loader get_Instance() {

if (instance == null) { instance = new Preview_Loader(); }

return instance;

}

///////////////////////////////////////////////////////////////////////////////////////////////////

public void bind_Image (final String image_Url,
                        final ImageView image_View) {

Web_Image_Async_Task downloading_Thread = thread_list.get(image_View);

if (downloading_Thread == null) {                // Почати завантаження, якщо нема існуючого потоку

    downloading_Thread = new Web_Image_Async_Task(image_View, thread_list);
    downloading_Thread.pushUrl(image_Url);
    thread_list.put(image_View, downloading_Thread);
    downloading_Thread.execute();

}

else { downloading_Thread.pushUrl(image_Url); }   // Якщо потік є, то зображення вже завантажується

}

///////////////////////////////////////////////////////////////////////////////////////////////////

}