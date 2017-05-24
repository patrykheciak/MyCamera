package com.gmail.patrykheciak.mycamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2137;
    private ImageView imageView;
    private Button buttonSelfie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);
        buttonSelfie = (Button) findViewById(R.id.selfieButton);

        buttonSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent capturePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (capturePhotoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(capturePhotoIntent, REQUEST_CODE_CAPTURE_IMAGE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE & resultCode == RESULT_OK) {
            Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
            if (photoBitmap != null) {
                imageView.setImageBitmap(photoBitmap);
            }
        }
    }
}
