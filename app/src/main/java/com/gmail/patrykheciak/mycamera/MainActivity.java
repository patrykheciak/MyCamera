package com.gmail.patrykheciak.mycamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2137;
    private static final int REQUEST_CODE_PHOTO_GALLERY = 1337;
    private ImageView imageView;
    private Button buttonSelfie;
    private Button buttonGallery;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);
        buttonSelfie = (Button) findViewById(R.id.selfieButton);
        buttonGallery = (Button) findViewById(R.id.galleryButton);
        buttonSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelfieButton();
            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGalleryButton();
            }
        });


        handleExternalIntent();



    }

    private void handleExternalIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (action.equals(Intent.ACTION_SEND) && type.startsWith("image")) {
            Uri photoUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            imageView.setImageURI(photoUri);
        }
    }

    private void setGalleryButton() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(galleryIntent, "Choose photo"),
                REQUEST_CODE_PHOTO_GALLERY
        );

    }

    private void setSelfieButton() {
        Intent capturePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        capturePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoUri());
        if (capturePhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(capturePhotoIntent, REQUEST_CODE_CAPTURE_IMAGE);
        }
    }

    private Uri getPhotoUri() {
//        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()).format(new Date());
        File photoFile = new File(Environment.getExternalStorageDirectory(), "avatar.jpg");
        return Uri.fromFile(photoFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE & resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
                if (photoBitmap != null) {
                    imageView.setImageBitmap(photoBitmap);
                }
            } else {
                imageView.setImageURI(getPhotoUri());
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(getPhotoUri());
                sendBroadcast(intent);
            }
        } else if (requestCode == REQUEST_CODE_PHOTO_GALLERY && resultCode == RESULT_OK) {
            Uri photoImage = data.getData();
            imageView.setImageURI(photoImage);
        }
    }
}
