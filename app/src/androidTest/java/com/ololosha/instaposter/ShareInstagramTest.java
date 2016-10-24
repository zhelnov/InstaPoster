package com.ololosha.instaposter;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * You should run this test, while instagramm app with share image is open.
 * It will click 3 times next button to actually share that picture.
 *
 * To run it from adb - install test apk to device emulator and run:
 * adb shell am instrument -w -r   -e debug false -e class com.ololosha.instaposter.ShareInstagramTest#confirmShare com.ololosha.instaposter.test/android.support.test.runner.AndroidJUnitRunner
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ShareInstagramTest {
    @Test
    public void confirmShare() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        UiDevice device = UiDevice.getInstance(getInstrumentation());

        // find next button
        device.findObject(new UiSelector()
                .resourceId("com.instagram.android:id/save"))
                .clickAndWaitForNewWindow();


        //click next button - final screen will appear
        device.findObject(new UiSelector()
                .resourceId("com.instagram.android:id/next_button_textview"))
                .clickAndWaitForNewWindow();

        //click "Share" button
        device.findObject(new UiSelector()
                .resourceId("com.instagram.android:id/next_button_textview"))
                .clickAndWaitForNewWindow();

    }
}
