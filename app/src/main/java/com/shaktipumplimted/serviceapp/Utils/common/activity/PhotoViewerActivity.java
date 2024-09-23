package com.shaktipumplimted.serviceapp.Utils.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

public class PhotoViewerActivity extends AppCompatActivity {
    ImageView showImg;
    Toolbar mToolbar;
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
        showImg = findViewById(R.id.showImg);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.photoGallery));
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        Bitmap myBitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(Constant.imagePath));

        showImg.setImageBitmap(myBitmap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}