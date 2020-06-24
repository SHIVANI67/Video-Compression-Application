package com.e.videocompressionapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;

public class OriginalVideoActivity extends AppCompatActivity {

    VideoView originalVideoView;
    Button compressedButton;
    FFmpeg fFmpeg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_video);

        Log.e("screen2: ", "called");

        originalVideoView = findViewById(R.id.original_video_view);
        compressedButton = findViewById(R.id.compress_button);

        String videoPath = getIntent().getStringExtra("videoUrl");
        Log.e( "urlPassed: ", videoPath + "");

        MediaController controller = new MediaController(this);

        Uri uri = Uri.fromFile(new File(videoPath));

        Log.e( "url: ", uri + "");

        originalVideoView.setVideoURI(uri);
        originalVideoView.requestFocus();
        originalVideoView.setMediaController(controller);
        originalVideoView.start();

        File mydir = this.getDir("CompressedVideos", Context.MODE_PRIVATE); //Creating an internal dir;
        if (!mydir.exists())
        {
            mydir.mkdirs();
        }

        String outputVideoPath = mydir.getAbsolutePath();
        Log.e("outputVideoPath: ", outputVideoPath + "" );

        final String[] videoCommand = {"-y", "-i", videoPath, "-s", "160x120", "-r", "25", "-vcodec", "mpeg4", "-b:v", "150k", "-b:a", "48000", "-ac", "2", "-ar", "22050", outputVideoPath};

        compressedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originalVideoView.pause();
                initializeFFM();
                try {
                    executeCommand(videoCommand);
                } catch (FFmpegCommandAlreadyRunningException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initializeFFM() {

        if(fFmpeg == null) {
           fFmpeg = FFmpeg.getInstance(getApplicationContext());
        }
        try{
            fFmpeg.loadBinary(new LoadBinaryResponseHandler() {


                @Override
                public void onFailure() {
                    super.onFailure();
                    Log.e( "loadLibrary: ", "loading failed");
                }

                @Override
                public void onSuccess() {
                    super.onSuccess();
                    Log.e( "loadLibrary: ", "loading successful");
                }

                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(String[] command) throws FFmpegCommandAlreadyRunningException {
        fFmpeg.execute(command, new ExecuteBinaryResponseHandler() {
            @Override
            public void onSuccess(String message) {
                super.onSuccess(message);
                Log.e( "compressVideo: ", "compress success: " + message);
            }

            @Override
            public void onProgress(String message) {
                super.onProgress(message);
                Log.e( "compressVideo: ", "compress in progress: " + message);
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                Log.e( "compressVideo: ", "compress failed: " + message);
            }

            @Override
            public void onStart() {
                super.onStart();
                Log.e( "compressVideo: ", "compress started");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.e( "compressVideo: ", "compress finish:" );
            }
        });
    }
}
