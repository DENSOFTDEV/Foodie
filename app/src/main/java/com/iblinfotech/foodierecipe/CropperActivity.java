package com.iblinfotech.foodierecipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImageView;

public class CropperActivity extends AppCompatActivity{

    private CropImageView cropImageView;
    public static Bitmap croppedBitmap;
    private ImageView btn_rRotate,btn_lRotate;
    private TextView tv_save_crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }


//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cropper);

        btn_rRotate = (ImageView) findViewById(R.id.btn_rRotate);
        btn_lRotate = (ImageView) findViewById(R.id.btn_lRotate);
        tv_save_crop = (TextView) findViewById(R.id.tv_save_crop);
        tv_save_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                croppedBitmap = cropImageView.getCroppedImage();
                Intent intent = new Intent(CropperActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_rRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(90);

            }
        }); btn_lRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(-90);

            }
        });

        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");

        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        cropImageView.setImageBitmap(bitmap);
        cropImageView.setGuidelines(CropImageView.Guidelines.ON_TOUCH);
        cropImageView.setScaleType(CropImageView.ScaleType.FIT_CENTER);
        cropImageView.setAutoZoomEnabled(true);
        cropImageView.setShowProgressBar(true);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
