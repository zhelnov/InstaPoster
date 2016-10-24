package com.ololosha.instaposter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * You should run this test, while instagramm app with share image is open.
 * It will click 3 times next button to actually share that picture.
 *
 * To run it from adb - install test apk to device emulator and run:
 * adb shell am instrument -w -r   -e debug false -e class com.ololosha.instaposter.ShareInstagramTest#postPhoto com.ololosha.instaposter.test/android.support.test.runner.AndroidJUnitRunner
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ShareInstagramTest {

    public static final String INSTAGRAM_PACKAGE = "com.instagram.android";

    @Test
    public void postPhoto() throws Exception {
        Context context = getInstrumentation().getTargetContext();

        URL url = new URL("http://pbs.twimg.com/media/CvORUlOWgAE-qQs.jpg");
        URLConnection connection = url.openConnection();
        InputStream input = connection.getInputStream();

        String tmpFilename = "tmpUpload.jpg";
        //MODE_WORLD_READABLE is deprecated - but works for now, to fix this - we need to grant runtime permissions to write external storage
        //TODO add runtime permissions to external storage and get rid of MODE_WORLD_READABLE
        FileOutputStream output = getInstrumentation().getTargetContext().openFileOutput(tmpFilename, Context.MODE_WORLD_READABLE);


        //TODO add some error handling
        int read;
        byte[] buffer = new byte[1024];
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }

        output.flush();
        output.close();
        input.close();

        File tmpFile = context.getFileStreamPath(tmpFilename);
        openInstagramShareDir(tmpFile);

        confirmShare();
    }

    private void openInstagramShareDir(File imageFile) {
        Context context = getInstrumentation().getTargetContext();

        Uri fileUri = Uri.fromFile(imageFile);
        context.grantUriPermission(INSTAGRAM_PACKAGE, fileUri, FLAG_GRANT_READ_URI_PERMISSION);


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

        shareIntent.setPackage(INSTAGRAM_PACKAGE);
        shareIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(shareIntent);
    }

    private void confirmShare() throws UiObjectNotFoundException {
        // Context of the app under test.
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
