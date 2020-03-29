package com.rutar.patriots_ukraine;

import android.os.*;
import android.content.*;
import android.support.v7.app.*;

// ................................................................................................

public class Start_Activity extends AppCompatActivity {

///////////////////////////////////////////////////////////////////////////////////////////////////

@Override
protected void onResume() {

super.onResume();

new Handler().postDelayed(new Runnable() {

    public void run() {

        Intent intent = new Intent(Start_Activity.this, Patriots_Ukraine.class);
        startActivity(intent);

        overridePendingTransition(R.anim.splash_screen_show, R.anim.splash_screen_hide);
        finish();

    }

}, 500);
}

///////////////////////////////////////////////////////////////////////////////////////////////////

}
