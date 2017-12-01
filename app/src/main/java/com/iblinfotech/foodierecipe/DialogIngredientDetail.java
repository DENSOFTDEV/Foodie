package com.iblinfotech.foodierecipe;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;

public class DialogIngredientDetail extends Dialog implements View.OnClickListener {

    public Activity c;
    TextView tv_cancel, tv_ingredient_name, tv_ingredient_detail;
    ImageView iv_ingredient_image;

    public DialogIngredientDetail(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }


//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.dialog_ingredient_detail);

        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        iv_ingredient_image = (ImageView) findViewById(R.id.iv_ingredient_image);
        tv_ingredient_name = (TextView) findViewById(R.id.tv_ingredient_name);
        tv_ingredient_detail = (TextView) findViewById(R.id.tv_ingredient_detail);
        tv_cancel.setOnClickListener(this);
        setData();
    }

    private void setData() {
        String ingredientName = GlobalClass.getPrefrenceString(c, "ingredientName", "");
        String ingredientAbout = GlobalClass.getPrefrenceString(c, "ingredientAbout", "");
        String ingredientImage = GlobalClass.getPrefrenceString(c, "ingredientImage", "");
        tv_ingredient_name.setText("- " + ingredientName + " -");
        tv_ingredient_detail.setText(ingredientAbout);

        iv_ingredient_image.setMinimumWidth(GlobalClass.DeviceWidth - 20);
        iv_ingredient_image.setMinimumHeight(GlobalClass.DeviceWidth - 20);
        Picasso.with(c).load(ingredientImage).into(iv_ingredient_image);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }


}