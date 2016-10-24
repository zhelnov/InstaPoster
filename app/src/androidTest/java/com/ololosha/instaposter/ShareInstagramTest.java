package com.ololosha.instaposter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.core.deps.guava.base.Predicate;
import android.support.test.espresso.core.deps.guava.collect.Collections2;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ololosha.instaposter.models.UpcomingTweet;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * You should run this test, while instagramm app with share image is open.
 * It will click 3 times next button to actually share that picture.
 * <p>
 * To run it from adb - install test apk to device emulator and run:
 * adb shell am instrument -w -r   -e debug false -e class com.ololosha.instaposter.ShareInstagramTest#postPhotos com.ololosha.instaposter.test/android.support.test.runner.AndroidJUnitRunner
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * <p>
 * Note
 */
@RunWith(AndroidJUnit4.class)
public class ShareInstagramTest {

    private static final String TAG = ShareInstagramTest.class.getSimpleName();

    private static final String INSTAGRAM_PACKAGE = "com.instagram.android";
    private static final String UPCOMING_URL = "https://apis.nilfalse.com/dailysocks/upcoming.json";

    @Test
    public void postPhotos() throws Exception {
        List<UpcomingTweet> tweets = getTweetsList();

        //sort tweets by date
        sortTweets(tweets);

        int totalTweets = tweets.size();
        Log.d(TAG, "total tweets: " + totalTweets);
        int count = 0;
        for (UpcomingTweet tweet : tweets) {
            Log.d(TAG, "posting: " + tweet);
            postTweetToInstagram(tweet);
            count += 1;
            Log.d(TAG, "posted success (I hope :) ), progress:  " + count + "/" + totalTweets);
        }

    }

    private void postTweetToInstagram(UpcomingTweet tweet) throws Exception {
        Context context = getInstrumentation().getTargetContext();

        //TODO add post couple photos
        URL url = new URL(tweet.getPhotos().get(0));
        URLConnection connection = url.openConnection();
        InputStream input = connection.getInputStream();

        String tmpFilename = "tmpUpload.jpg";
        //MODE_WORLD_READABLE is deprecated - but works for now, to fix this - we need to grant runtime permissions to write external storage
        //TODO add runtime permissions to external storage and get rid of MODE_WORLD_READABLE
        FileOutputStream output = context.openFileOutput(tmpFilename, Context.MODE_WORLD_READABLE);


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
        openInstagramShareDir(context, tmpFile);

        confirmShare(tweet);

    }

    /**
     * Opens instagramm share dialog, like it would user to clicl share in app.
     * Instagram app should be able to open given file uri.
     *
     * @param imageFile file to share
     */
    private void openInstagramShareDir(Context context, File imageFile) {

        Uri fileUri = Uri.fromFile(imageFile);
        context.grantUriPermission(INSTAGRAM_PACKAGE, fileUri, FLAG_GRANT_READ_URI_PERMISSION);


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

        shareIntent.setPackage(INSTAGRAM_PACKAGE);
        shareIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(shareIntent);
    }


    /**
     * Main magic goes here - this simple method clicks all needed buttons in ui to satisfy user.
     *
     * @param tweet
     * @throws UiObjectNotFoundException
     */
    private void confirmShare(UpcomingTweet tweet) throws UiObjectNotFoundException {
        // Context of the app under test.
        UiDevice device = UiDevice.getInstance(getInstrumentation());

        // toggle crop type to fill screen
        clickButton("com.instagram.android:id/croptype_toggle_button");

        // click next - wait for filter screen
        clickButtonWaitNewWindow("com.instagram.android:id/save");

        // click next  - final screen will appear
        clickButtonWaitNewWindow("com.instagram.android:id/next_button_textview");

        //TODO add caption fillup here

        // click "Share" - and it's done, mothefucker ;)
        clickButtonWaitNewWindow("com.instagram.android:id/next_button_textview");
    }

    private void clickButtonWaitNewWindow(String resId) throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        device.findObject(new UiSelector()
                .resourceId(resId))
                .clickAndWaitForNewWindow();
    }

    private void clickButton(String resId) throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        device.findObject(new UiSelector()
                .resourceId(resId))
                .click();
    }

    //region Utility methods
    private List<UpcomingTweet> getTweetsList() throws Exception {
        URLConnection metadataConn = new URL(UPCOMING_URL).openConnection();
        Gson gson = new GsonBuilder().create();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                metadataConn.getInputStream()));

        String output = "";
        String line;
        while ((line = in.readLine()) != null) {
            output += line;
        }

        if (output.startsWith("cb(") && output.endsWith(");")) {
            //FIXME remove this workaround and find how to parse cb()
            output = output.substring(3, output.lastIndexOf(");"));
        }

        Type listTweetsType = new TypeToken<List<UpcomingTweet>>() {
        }.getType();
        return gson.fromJson(output, listTweetsType);
    }

    private void sortTweets(List<UpcomingTweet> tweets) {
        Collections.sort(tweets, new Comparator<UpcomingTweet>() {
            @Override
            public int compare(UpcomingTweet o1, UpcomingTweet o2) {
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        });
    }
    //endregion
}
