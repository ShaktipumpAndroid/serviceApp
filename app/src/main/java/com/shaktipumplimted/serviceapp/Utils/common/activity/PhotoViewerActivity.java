package com.shaktipumplimted.serviceapp.Utils.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.ZoomImageView;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

public class PhotoViewerActivity extends AppCompatActivity {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    ZoomImageView showImg;
    Toolbar toolbar;
    Bitmap myBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        Init();
        listner();
    }

    private void listner() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void Init() {
        showImg = findViewById(R.id.showImg);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.photoGallery));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        if(getIntent().getExtras()!=null){
            if(getIntent().getStringExtra(Constant.Images).equals("1")){
                myBitmap = Utility.getBitmapFromBase64(getIntent().getStringExtra(Constant.imagePath));
            }else {
                 myBitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(Constant.imagePath));
            }
        }


        showImg.setImageBitmap(myBitmap);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}