package com.ololosha.instaposter;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String type = "image/*";
        String filename = "/test2.jpg";
        String mediaPath = Environment.getExternalStorageDirectory() + filename;
        createInstagramIntent(type, mediaPath);

        finish();
    }




    private void createInstagramIntent(String type, String mediaPath){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);
        Log.d("Ololo", "uri" + uri);
        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        share.setPackage("com.instagram.android");

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }
}
