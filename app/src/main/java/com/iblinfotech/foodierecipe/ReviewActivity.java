package com.iblinfotech.foodierecipe;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.iblinfotech.foodierecipe.adapter.RecipeReviewAdapter;
import com.iblinfotech.foodierecipe.model.SingleRecipeDetailData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.CallWebServices;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_mainImage;
    private RecyclerView recycler_review;
    private TextView tv_plus, tv_recipe_category, tv_recipe_title, tv_total_comments;
    private ImageView iv_top, iv_back;
    private String mSingleRecipeToken, mBackgroundImage, mcolorCode, user_id;
    private KProgressHUD kProgressHUD;
    private TextView tv_no_comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_review);
        mSingleRecipeToken = getIntent().getStringExtra("mSingleRecipeToken");
        user_id = GlobalClass.getPrefrenceString(ReviewActivity.this, "user_id", "");

        setContent();
        if (GlobalClass.isInternetOn(ReviewActivity.this)) {
            getRecipeDetail();
        } else {
            GlobalClass.showToast(ReviewActivity.this, getResources().getString(R.string.no_internet_connection));
        }

    }

    private void getRecipeDetail() {
        mSingleRecipeToken = CategoryItemViewActivity.singleRecipeDetailData.getToken();
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("Please wait..")
                .show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);

        callWebServices.getRecipeById(user_id, mSingleRecipeToken, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse);
//                    Log.e("-----------------------", "" + mainResponseObject.toString(4));

                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        JSONObject jsonObject = mainResponseObject.getJSONObject(AppConfig.RESULT);
                        Log.e("SuccessData", "JSONObject---------------- " + jsonObject);
                        //Add Data
                        CategoryItemViewActivity.singleRecipeDetailData = LoganSquare.parse(jsonObject.toString(), SingleRecipeDetailData.class);

                        RecipeReviewAdapter recipeReviewAdapter = new RecipeReviewAdapter(ReviewActivity.this, CategoryItemViewActivity.singleRecipeDetailData.getReview());
//                        Log.e("---------", "Ankita---11--------" + CategoryItemViewActivity.singleRecipeDetailData.getReview().size());
                        LinearLayoutManager llm = new LinearLayoutManager(ReviewActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        recycler_review.setLayoutManager(llm);
                        recycler_review.setAdapter(recipeReviewAdapter);
                        recipeReviewAdapter.notifyDataSetChanged();

                        mcolorCode = CategoryItemViewActivity.singleRecipeDetailData.getCategory_color_code();
                        mBackgroundImage = AppConfig.IMAGE_URL + CategoryItemViewActivity.singleRecipeDetailData.getCategory_background();
                        Picasso.with(ReviewActivity.this).load(mBackgroundImage).into(iv_top);
                        tv_recipe_category.setText(CategoryItemViewActivity.singleRecipeDetailData.getCategory());
                        tv_recipe_title.setText(CategoryItemViewActivity.singleRecipeDetailData.getRecipe_name());
                        tv_total_comments.setText(CategoryItemViewActivity.singleRecipeDetailData.getReview_count() + " COMMENTS");

                        if (CategoryItemViewActivity.singleRecipeDetailData.getReview().size() == 0) {
                            recycler_review.setVisibility(View.GONE);
                            tv_no_comments.setVisibility(View.VISIBLE);

                        }

                    } else {
                        GlobalClass.showToast(ReviewActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
                Log.e("RetrofitError", " ---- :  " + error);
                GlobalClass.showToast(ReviewActivity.this, getResources().getString(R.string.try_after_some_time));
            }
        });
    }

    private void setContent() {
        rl_mainImage = (RelativeLayout) findViewById(R.id.rl_mainImage);
        iv_top = (ImageView) findViewById(R.id.iv_top);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_plus = (TextView) findViewById(R.id.tv_plus);
        tv_recipe_category = (TextView) findViewById(R.id.tv_recipe_category);
        tv_recipe_title = (TextView) findViewById(R.id.tv_recipe_title);
        tv_no_comments = (TextView) findViewById(R.id.tv_no_comments);
        tv_total_comments = (TextView) findViewById(R.id.tv_total_comments);
        recycler_review = (RecyclerView) findViewById(R.id.recycler_review);

        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        Typeface RobotoLight = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");
        tv_total_comments.setTypeface(RobotoLight);
        tv_recipe_title.setTypeface(fontRegular);
        tv_no_comments.setTypeface(RobotoLight);
        tv_recipe_category.setTypeface(RobotoLight);

        iv_back.setOnClickListener(this);
        tv_plus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                Intent BackIntent = new Intent(ReviewActivity.this, CategoryItemViewActivity.class);
                BackIntent.putExtra("mSingleRecipeToken", mSingleRecipeToken);
                startActivity(BackIntent);
                break;

            case R.id.tv_plus:
                if (GlobalClass.getPrefrenceBoolean(ReviewActivity.this, "isLogin", false)) {
                    finish();
                    Intent intent = new Intent(ReviewActivity.this, AddReviewActivity.class);
                    intent.putExtra("mSingleRecipeToken", mSingleRecipeToken);
                    startActivity(intent);
                } else {
                    showDialog();
                }

                break;
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.you_must_be_login_first)
                .setCancelable(false)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ReviewActivity.this.finish();
                        startActivity(new Intent(ReviewActivity.this, LogInActivity.class));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
