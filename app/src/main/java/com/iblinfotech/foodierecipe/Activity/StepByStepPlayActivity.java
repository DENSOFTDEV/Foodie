package com.iblinfotech.foodierecipe.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.adapter.RecipeStepsAdapter;
import com.iblinfotech.foodierecipe.utils.GlobalClass;

public class StepByStepPlayActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewpager_recipe_stepByStep;
    private ImageView iv_back_inSteps;
    private TextView tv_privious, tv_nextStep, tv_steps_not_available;
    private LinearLayout ll_next_step, ll_bottom, ll_pre_step;
    private String mcolorCode;
    private int currentPosition = 0;
    private String mSingleRecipeToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        setContentView(R.layout.activity_step_by_step_play);
        mSingleRecipeToken = getIntent().getStringExtra("mSingleRecipeToken");

        setContent();
    }

    private void setContent() {
        viewpager_recipe_stepByStep = (ViewPager) findViewById(R.id.viewpager_recipe_stepByStep);
        iv_back_inSteps = (ImageView) findViewById(R.id.iv_back_inSteps);
        tv_privious = (TextView) findViewById(R.id.tv_privious);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        ll_pre_step = (LinearLayout) findViewById(R.id.ll_pre_step);
        tv_nextStep = (TextView) findViewById(R.id.tv_nextStep);
        tv_steps_not_available = (TextView) findViewById(R.id.tv_steps_not_available);
        ll_next_step = (LinearLayout) findViewById(R.id.ll_next_step);

        ll_next_step.setOnClickListener(this);
        ll_pre_step.setOnClickListener(this);
        iv_back_inSteps.setOnClickListener(this);

        mcolorCode = getIntent().getStringExtra("mcolorCode");
        ll_bottom.setBackgroundColor(Color.parseColor(mcolorCode));
        Typeface fontRobotoLight = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");
        tv_privious.setTypeface(fontRobotoLight);
        tv_nextStep.setTypeface(fontRobotoLight);


        if (GlobalClass.isInternetOn(StepByStepPlayActivity.this)) {

            Log.e("----", "----****: "+ CategoryItemViewActivity.singleRecipeDetailData.getMethod().size());


            if (CategoryItemViewActivity.singleRecipeDetailData.getMethod().size() != 0) {

                if (CategoryItemViewActivity.singleRecipeDetailData.getMethod().size() < 2) {
                    ll_next_step.setVisibility(View.GONE);
                    ll_pre_step.setVisibility(View.INVISIBLE);
                }
                viewpager_recipe_stepByStep.setAdapter(new RecipeStepsAdapter(StepByStepPlayActivity.this, CategoryItemViewActivity.singleRecipeDetailData.getMethod()));
                    if(CategoryItemViewActivity.singleRecipeDetailData.getMethod().size() < 2){
                ll_next_step.setVisibility(View.INVISIBLE);
                ll_pre_step.setVisibility(View.INVISIBLE);
                    }
            } else {
                ll_bottom.setVisibility(View.GONE);
                tv_steps_not_available.setVisibility(View.VISIBLE);
                iv_back_inSteps.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_gray));
            }
        } else {
            Toast.makeText(StepByStepPlayActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }

        viewpager_recipe_stepByStep.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                checkPosition();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_next_step:
                viewpager_recipe_stepByStep.setCurrentItem(getItem(+1)); //getItem(-1) for previous
                break;
            case R.id.ll_pre_step:
                viewpager_recipe_stepByStep.setCurrentItem(getItem(-1)); //getItem(-1) for previous
                break;
            case R.id.iv_back_inSteps:
                Intent intent = new Intent(StepByStepPlayActivity.this, CategoryItemViewActivity.class);
                intent.putExtra("mSingleRecipeToken", mSingleRecipeToken);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;

        }
    }

    private void checkPosition() {
        ll_next_step.setVisibility(View.VISIBLE);
        ll_pre_step.setVisibility(View.VISIBLE);
        Log.e("checkposition", "-------------------------------" + viewpager_recipe_stepByStep.getCurrentItem());
        Log.e("size::::", "--" + CategoryItemViewActivity.singleRecipeDetailData.getMethod().size());

        if (currentPosition == 0) {
            ll_pre_step.setVisibility(View.GONE);
            ll_next_step.setVisibility(View.VISIBLE);
        } else if (currentPosition == CategoryItemViewActivity.singleRecipeDetailData.getMethod().size() - 1) {
            ll_pre_step.setVisibility(View.VISIBLE);
            ll_next_step.setVisibility(View.GONE);
        } else {
            ll_pre_step.setVisibility(View.VISIBLE);
            ll_next_step.setVisibility(View.VISIBLE);
        }
    }

    private int getItem(int i) {
        return viewpager_recipe_stepByStep.getCurrentItem() + i;
    }

}
