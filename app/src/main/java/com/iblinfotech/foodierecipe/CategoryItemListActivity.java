package com.iblinfotech.foodierecipe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.adapter.FragmentAdapter;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class CategoryItemListActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView toolbar_logo_below, iv_back, iv_top;
    private RelativeLayout rl_mainImage;
    private FragmentAdapter tabPagerAdapter;
    private TabLayout detail_tabs;
    private TextView tv_totalRecipe, tv_page_title;
    private ImageView iv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Window window = getWindow();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(Color.TRANSPARENT); }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category_item_list);
        setContent();
        setToolbarView();
        initViewPager();
    }

    private void setContent() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        tv_totalRecipe = (TextView) findViewById(R.id.tv_totalRecipe);
        tv_page_title = (TextView) findViewById(R.id.tv_page_title);
        toolbar_logo_below = (ImageView) findViewById(R.id.toolbar_logo_below);
        iv_top = (ImageView) findViewById(R.id.iv_top);
        rl_mainImage = (RelativeLayout) findViewById(R.id.rl_mainImage);
        Log.e("--------","GlobalClass.DeviceWidth: ========= "+GlobalClass.DeviceWidth);

        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        tv_page_title.setTypeface(fontRegular);
        Typeface RobotoLight = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");
        tv_totalRecipe.setTypeface(RobotoLight);

        iv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
    }

    private void setToolbarView() {
        String mRecipyTotal = getIntent().getStringExtra("mRecipyTotal");
        String mRecipyTitle = getIntent().getStringExtra("mRecipyTitle");
        String mIconImage = getIntent().getStringExtra("mIconImage");
        String mBackgroundImage = GlobalClass.getPrefrenceString(this, "mBackgroundImage", "");

        Log.e("------","-----***** "+mBackgroundImage);
        tv_totalRecipe.setText(mRecipyTotal);
        tv_page_title.setText(mRecipyTitle);
        Picasso.with(this).load(mIconImage).into(toolbar_logo_below);
        Picasso.with(this).load(mBackgroundImage).into(iv_top);
    }

    private void initViewPager() {
        detail_tabs = (TabLayout) findViewById(R.id.detail_tabs);

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        tabPagerAdapter = new FragmentAdapter(getSupportFragmentManager());
        if (pager != null) {
            pager.setAdapter(tabPagerAdapter);
            detail_tabs.setTabsFromPagerAdapter(tabPagerAdapter);
            detail_tabs.setupWithViewPager(pager);
            for (int i = 0; i < detail_tabs.getChildCount(); i++)
                detail_tabs.getChildAt(i).setBackgroundResource(R.drawable.bg_tab);
        }


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                } else if (position == 1) {
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                startActivity(new Intent(CategoryItemListActivity.this, SearchActivity.class));
                break;

        }
    }
}
