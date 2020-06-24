package com.e.videocompressionapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button selectVideoButton;
    private int GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectVideoButton = findViewById(R.id.select_video);

        selectVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideoFromGallery();
            }
        });

    }

    public void  chooseVideoFromGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("result", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            Log.d("what", "cancle");
            return;
        }
        if (requestCode == GALLERY) {
            Log.d("what", "gale");
            if (data != null) {
                Uri contentURI = data.getData();

                String selectedVideoPath = getPath(contentURI);
                Log.d("path", selectedVideoPath);

                Intent intent = new Intent(this, OriginalVideoActivity.class);
                intent.putExtra("videoUrl", selectedVideoPath);
                startActivity(intent);

//                saveVideoToInternalStorage(selectedVideoPath);
//                videoView.setVideoURI(contentURI);
//                videoView.requestFocus();
//                videoView.start();

            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

}
