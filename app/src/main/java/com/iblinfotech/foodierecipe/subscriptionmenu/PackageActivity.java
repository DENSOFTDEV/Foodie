package com.iblinfotech.foodierecipe.subscriptionmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.MainActivity;
import com.iblinfotech.foodierecipe.R;

/**
 * Created by iblinfotech on 11/28/17.
 */

public class PackageActivity extends AppCompatActivity{
    public ImageView iv_back;
    private Button remove_ads,premium_recepies,ad_premium_recepies;
    private TextView adpack,recipe,ad_recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        remove_ads = (Button) findViewById(R.id.remove_ads);
        premium_recepies = (Button) findViewById(R.id.premium_recepies);
        ad_premium_recepies = (Button) findViewById(R.id.ad_premium_recepies);
        adpack = (TextView) findViewById(R.id.adpack);
        recipe = (TextView) findViewById(R.id.recipe);
        ad_recipe = (TextView) findViewById(R.id.ad_recipe);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i =new Intent(PackageActivity.this,MainActivity.class);
//                startActivity(i);
                finish();
            }
        });
    }
}