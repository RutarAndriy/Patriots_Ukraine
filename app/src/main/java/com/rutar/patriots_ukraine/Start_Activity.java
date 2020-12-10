package com.rutar.patriots_ukraine;

import android.os.*;
import android.content.*;

import androidx.appcompat.app.AppCompatActivity;

// ................................................................................................
// Activity - заставка

public class Start_Activity extends AppCompatActivity {

private Intent intent = null;

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onResume() {

super.onResume();
new Run_Task().execute();

}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Клас, який створює та запускає головне вікно програми

class Run_Task extends AsyncTask <Void, Void, Void> {

@Override
protected Void doInBackground (Void... params) {

long start_time = System.currentTimeMillis();

intent = new Intent(Start_Activity.this, Patriots_Ukraine.class);

long process_time = System.currentTimeMillis() - start_time;
long sleep_time = 1500 - process_time;

try { Thread.sleep(sleep_time > 0 ? sleep_time : 1); }
catch (Exception ignored) { }

return null;

}

// ................................................................................................

@Override
protected void onPostExecute (Void result) {

super.onPostExecute(result);

startActivity(intent);
overridePendingTransition(R.anim.splash_screen_show, R.anim.splash_screen_hide);

finish();

}
}

///////////////////////////////////////////////////////////////////////////////////////////////////

}