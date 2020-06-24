package com.e.videocompressionapplication;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class CompressedVideoActivity extends AppCompatActivity {

    VideoView compressedVideoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compressed_video);

        Log.e("screen3: ", "called");

        compressedVideoView = findViewById(R.id.compressed_video_view);

        String videoPath = getIntent().getStringExtra("videoUrl");
        Log.e( "urlPassed: ", videoPath + "");

        MediaController controller = new MediaController(this);

        Uri uri = Uri.fromFile(new File(videoPath));

        Log.e( "url: ", uri + "");

        compressedVideoView.setVideoURI(uri);
        compressedVideoView.requestFocus();
        compressedVideoView.setMediaController(controller);
        compressedVideoView.start();
    }
}
