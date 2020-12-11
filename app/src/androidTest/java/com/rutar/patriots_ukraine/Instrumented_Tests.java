package com.rutar.patriots_ukraine;

import android.content.*;

import org.junit.*;
import org.junit.runner.*;

import androidx.test.platform.app.*;
import androidx.test.ext.junit.runners.*;

import static org.junit.Assert.*;

// ................................................................................................

@RunWith(AndroidJUnit4.class)
public class Instrumented_Tests {

///////////////////////////////////////////////////////////////////////////////////////////////////

@Test
public void is_Package_Name_Correct_Test() {
// Context of the app under test.
Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
assertEquals("com.rutar.patriots_ukraine", appContext.getPackageName());
}

///////////////////////////////////////////////////////////////////////////////////////////////////

}