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
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

public class PhotoViewerActivity extends AppCompatActivity {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    ImageView showImg;
    Toolbar mToolbar;
    Bitmap myBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        Init();
        listner();
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void Init() {
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        showImg = findViewById(R.id.showImg);
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.photoGallery));
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


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
    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            showImg.setScaleX(mScaleFactor);
            showImg.setScaleY(mScaleFactor);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}