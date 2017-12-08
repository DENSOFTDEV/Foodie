package com.iblinfotech.foodierecipe.Activity;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.SqLiteHelper.FoodieLocalData;
import com.iblinfotech.foodierecipe.adapter.RecipeImageAdapter;
import com.iblinfotech.foodierecipe.adapter.RecipeIngredientsAdapter;
import com.iblinfotech.foodierecipe.model.RecipeIngredientsDataNew;
import com.iblinfotech.foodierecipe.model.ShoppingData;
import com.iblinfotech.foodierecipe.model.SingleRecipeDetailData;
import com.iblinfotech.foodierecipe.utils.AlertDialogParser;
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
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CategoryItemViewActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<ShoppingData> shoppingItemDatas;
    private ArrayList<RecipeIngredientsDataNew> recipeIngredientsDatas = new ArrayList<>();

    private TextView tv_soaking_time, tv_category_title, tv_comments, tv_preparation_time, tv_ingredient, tv_fermantation_time, tv_item_title, tv_cook_time, tv_stepViseRecipe, tv_discription_title, tv_discription, tv_twoPerson, tv_fourPerson, tv_sixPerson, tv_addToShoppingList;
    private ViewPager viewpager_recipe_image;
    private ImageView iv_cancel, iv_cook_time, iv_preparation_time, iv_soaking_time, iv_fermantation_time, iv_bottom, iv_fav, iv_step, bg_play_steps2, bg_play_steps1;
    private GridView grid_recipe_ingredient;
    private KProgressHUD kProgressHUD;
    private RelativeLayout rl_stepViseRecipe, rl_comment_bottom;
    private ScrollView scroll_main;
    private LinearLayout ll_categoryItemView;
//    CoordinatorLayout ll_recipe;

    public static SingleRecipeDetailData singleRecipeDetailData;
    private RecipeIngredientsAdapter recipeIngredientsAdapter;
    private String mSingleRecipeToken, mcolorCode, mBackgroundImage, user_id, servingPerson, recipe_video_url;
    private boolean isFav = false;
    public static int person;
    private Typeface fontRobotoLight, PlayfairDisplayItalic;

    private FoodieLocalData dbHelper;
    private ShoppingData shoppingItemData;
    private ContentValues contentValues;
    private AdView mAdView;
    int removeAds;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        removeAds = GlobalClass.getPrefrenceInt(CategoryItemViewActivity.this, "removeads", 3);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_category_item_view);

        GlobalClass global = new GlobalClass(CategoryItemViewActivity.this);
        mSingleRecipeToken = getIntent().getStringExtra("mSingleRecipeToken");
        user_id = GlobalClass.getPrefrenceString(CategoryItemViewActivity.this, "user_id", "");
        setContent();
        if (GlobalClass.isInternetOn(CategoryItemViewActivity.this)) {
            getRecipeDetail();
            if (removeAds != 0) {

                setAdMob();
            }
        } else {
            GlobalClass.showToast(CategoryItemViewActivity.this, getResources().getString(R.string.no_internet_connection));
            ll_categoryItemView.setVisibility(View.GONE);
        }
    }

    private void setContent() {
        tv_category_title = (TextView) findViewById(R.id.tv_category_title);
        tv_item_title = (TextView) findViewById(R.id.tv_item_title);
        tv_cook_time = (TextView) findViewById(R.id.tv_cook_time);
        tv_preparation_time = (TextView) findViewById(R.id.tv_preparation_time);
        tv_soaking_time = (TextView) findViewById(R.id.tv_soaking_time);
        tv_fermantation_time = (TextView) findViewById(R.id.tv_fermantation_time);
        tv_discription_title = (TextView) findViewById(R.id.tv_discription_title);
        tv_discription = (TextView) findViewById(R.id.tv_discription);
        tv_twoPerson = (TextView) findViewById(R.id.tv_twoPerson);
        tv_fourPerson = (TextView) findViewById(R.id.tv_fourPerson);
        tv_sixPerson = (TextView) findViewById(R.id.tv_sixPerson);
        tv_stepViseRecipe = (TextView) findViewById(R.id.tv_stepViseRecipe);
        tv_addToShoppingList = (TextView) findViewById(R.id.tv_addToShoppingList);
        tv_comments = (TextView) findViewById(R.id.tv_comments);
        tv_ingredient = (TextView) findViewById(R.id.tv_ingredient);

        viewpager_recipe_image = (ViewPager) findViewById(R.id.viewpager_recipe_image);

        iv_cook_time = (ImageView) findViewById(R.id.iv_cook_time);
        iv_preparation_time = (ImageView) findViewById(R.id.iv_preparation_time);
        iv_soaking_time = (ImageView) findViewById(R.id.iv_soaking_time);
        iv_fav = (ImageView) findViewById(R.id.iv_fav);
        iv_bottom = (ImageView) findViewById(R.id.iv_bottom);
        iv_fermantation_time = (ImageView) findViewById(R.id.iv_fermantation_time);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        bg_play_steps2 = (ImageView) findViewById(R.id.bg_play_steps2);
        bg_play_steps1 = (ImageView) findViewById(R.id.bg_play_steps1);
        iv_step = (ImageView) findViewById(R.id.iv_step);

        scroll_main = (ScrollView) findViewById(R.id.scroll_main);
        rl_stepViseRecipe = (RelativeLayout) findViewById(R.id.rl_stepViseRecipe);
        rl_comment_bottom = (RelativeLayout) findViewById(R.id.rl_comment_bottom);
        grid_recipe_ingredient = (GridView) findViewById(R.id.grid_recipe_ingredient);

        ll_categoryItemView = (LinearLayout) findViewById(R.id.ll_categoryItemView);
//        ll_recipe = (CoordinatorLayout) findViewById(R.id.ll_recipe);

        tv_twoPerson.setOnClickListener(this);
        tv_fourPerson.setOnClickListener(this);
        tv_sixPerson.setOnClickListener(this);
        tv_addToShoppingList.setOnClickListener(this);
        rl_stepViseRecipe.setOnClickListener(this);
        rl_comment_bottom.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
        iv_fav.setOnClickListener(this);

        scroll_main.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scroll_main.setFocusable(true);
        scroll_main.setFocusableInTouchMode(true);
        scroll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        dbHelper = new FoodieLocalData(getApplicationContext());

        //Added Here
        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        tv_item_title.setTypeface(fontRegular);
        tv_ingredient.setTypeface(fontRegular);

        fontRobotoLight = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");
        tv_category_title.setTypeface(fontRobotoLight);
        tv_discription.setTypeface(fontRobotoLight);
        tv_stepViseRecipe.setTypeface(fontRobotoLight);
        tv_twoPerson.setTypeface(fontRobotoLight);
        tv_fourPerson.setTypeface(fontRobotoLight);
        tv_sixPerson.setTypeface(fontRobotoLight);
        tv_cook_time.setTypeface(fontRobotoLight);
        tv_soaking_time.setTypeface(fontRobotoLight);
        tv_preparation_time.setTypeface(fontRobotoLight);
        tv_fermantation_time.setTypeface(fontRobotoLight);
        tv_addToShoppingList.setTypeface(fontRobotoLight);
        tv_comments.setTypeface(fontRobotoLight);

        PlayfairDisplayItalic = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Italic.otf");
        tv_discription_title.setTypeface(PlayfairDisplayItalic);
    }

    private void setAdMob() {
        mAdView = (AdView) findViewById(R.id.ads);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mAdView.setVisibility(View.GONE);
            }

        });
    }

    private void setData(String mcolorCode) {
        tv_addToShoppingList.setTextColor(Color.parseColor(mcolorCode));
        tv_category_title.setTextColor(Color.parseColor(mcolorCode));
        tv_cook_time.setTextColor(Color.parseColor(mcolorCode));
        tv_preparation_time.setTextColor(Color.parseColor(mcolorCode));
        tv_soaking_time.setTextColor(Color.parseColor(mcolorCode));
        tv_fermantation_time.setTextColor(Color.parseColor(mcolorCode));
        tv_stepViseRecipe.setTextColor(Color.parseColor(mcolorCode));

        Drawable bg_play2 = CategoryItemViewActivity.this.getResources().getDrawable(R.drawable.bg_play_steps2);
        bg_play2.setColorFilter(new PorterDuffColorFilter(Color.parseColor(mcolorCode), PorterDuff.Mode.MULTIPLY));
        bg_play_steps2.setImageDrawable(bg_play2);
        tv_addToShoppingList.setBackgroundDrawable(bg_play2);

        Drawable ic_step = CategoryItemViewActivity.this.getResources().getDrawable(R.drawable.ic_step);
        ic_step.setColorFilter(new PorterDuffColorFilter(Color.parseColor(mcolorCode), PorterDuff.Mode.MULTIPLY));
        iv_step.setImageDrawable(ic_step);

        Drawable bg_play1 = CategoryItemViewActivity.this.getResources().getDrawable(R.drawable.bg_play_steps1);
        bg_play1.setColorFilter(new PorterDuffColorFilter(Color.parseColor(mcolorCode), PorterDuff.Mode.MULTIPLY));
        bg_play_steps1.setImageDrawable(bg_play1);

        tv_category_title.setText("- " + singleRecipeDetailData.getCategory() + " -");
        tv_item_title.setText(singleRecipeDetailData.getRecipe_name());
        tv_discription_title.setText(singleRecipeDetailData.getRecipe_description_name());
        tv_discription.setText(singleRecipeDetailData.getRecipe_description());
        tv_comments.setText(singleRecipeDetailData.getReview_count() + " COMMENTS");
//        Log.e("singleRecipeDetailData.getRecipe_image():------------------", "##################" + singleRecipeDetailData.getRecipe_image());
        viewpager_recipe_image.setAdapter(new RecipeImageAdapter(CategoryItemViewActivity.this, singleRecipeDetailData.getRecipe_image()));

        if (singleRecipeDetailData.getCooking_time().length() > 0) {
            tv_cook_time.setText(singleRecipeDetailData.getCooking_time() + "'");
            tv_cook_time.setVisibility(View.VISIBLE);
            iv_cook_time.setVisibility(View.VISIBLE);
        }
        if (singleRecipeDetailData.getSoaking_time().length() > 0) {
            tv_soaking_time.setText(singleRecipeDetailData.getSoaking_time() + "'");
            tv_soaking_time.setVisibility(View.VISIBLE);
            iv_soaking_time.setVisibility(View.VISIBLE);
        }
        if (singleRecipeDetailData.getPreparation_time().length() > 0) {
            tv_preparation_time.setText(singleRecipeDetailData.getPreparation_time() + "'");
            tv_preparation_time.setVisibility(View.VISIBLE);
            iv_preparation_time.setVisibility(View.VISIBLE);
        }
        if (singleRecipeDetailData.getFermentation_time().length() > 0) {
            tv_fermantation_time.setText(singleRecipeDetailData.getFermentation_time() + "'");
            tv_fermantation_time.setVisibility(View.VISIBLE);
            iv_fermantation_time.setVisibility(View.VISIBLE);
        }
        if (singleRecipeDetailData.getIs_favourite().equals("1")) {
            Drawable mDrawable = CategoryItemViewActivity.this.getResources().getDrawable(R.drawable.ic_unse);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#181818"), PorterDuff.Mode.MULTIPLY));
            iv_fav.setImageDrawable(mDrawable);
        } else {
            Drawable mDrawable = CategoryItemViewActivity.this.getResources().getDrawable(R.drawable.ic_unselect);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#181818"), PorterDuff.Mode.MULTIPLY));
            iv_fav.setImageDrawable(mDrawable);
        }

    }

    private void getRecipeDetail() {
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

                    Log.e("Main Responce object", "----main log----" + mainResponseObject);
//                    Log.e("-----------------------", "" + mainResponseObject.toString(4));

                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        JSONObject jsonObject = mainResponseObject.getJSONObject(AppConfig.RESULT);
                        Log.e("SuccessData", "JSONObject---------------- " + jsonObject);
                        singleRecipeDetailData = LoganSquare.parse(jsonObject.toString(), SingleRecipeDetailData.class);

                        mcolorCode = singleRecipeDetailData.getCategory_color_code();
                        mBackgroundImage = AppConfig.IMAGE_URL + singleRecipeDetailData.getCategory_background();

                        recipe_video_url = jsonObject.getString("recipe_video");
//                        recipe_video_url =("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                        Log.e("recipe video url", "----recipe vedio url----" + recipe_video_url);

                        Picasso.with(CategoryItemViewActivity.this).load(mBackgroundImage).into(iv_bottom);
                        setData(mcolorCode);
                        setIngredients(singleRecipeDetailData.getHow_much().get(1).getServings());
                    } else {
                        AlertDialogParser.showMessageDialog(CategoryItemViewActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));
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
                ll_categoryItemView.setVisibility(View.GONE);
//                AlertDialogParser.showMessageDialog(CategoryItemViewActivity.this, "" + error);
                GlobalClass.showToast(CategoryItemViewActivity.this, getResources().getString(R.string.try_after_some_time));
            }
        });
    }

    private void setIngredients(String servings) {
        if (servings.contains("6")) {
            tv_sixPerson.setTextColor(Color.parseColor(mcolorCode));
            tv_sixPerson.setTypeface(fontRobotoLight, Typeface.BOLD);
            tv_twoPerson.setTypeface(fontRobotoLight, Typeface.NORMAL);
            tv_fourPerson.setTypeface(fontRobotoLight, Typeface.NORMAL);

            tv_twoPerson.setTextColor(getResources().getColor(R.color.black));
            tv_fourPerson.setTextColor(getResources().getColor(R.color.black));
            for (int i = 0; i < singleRecipeDetailData.getHow_much().size(); i++) {
                if (singleRecipeDetailData.getHow_much().get(i).getServings().equals("6")) {
                    person = i;
                    recipeIngredientsAdapter = new RecipeIngredientsAdapter(CategoryItemViewActivity.this, singleRecipeDetailData.getHow_much().get(i).getIngredients());
                    break;
                }
            }
        } else if (servings.contains("4")) {
            tv_fourPerson.setTextColor(Color.parseColor(mcolorCode));
            tv_fourPerson.setTypeface(fontRobotoLight, Typeface.BOLD);
            tv_twoPerson.setTypeface(fontRobotoLight, Typeface.NORMAL);
            tv_sixPerson.setTypeface(fontRobotoLight, Typeface.NORMAL);

            tv_twoPerson.setTextColor(getResources().getColor(R.color.black));
            tv_sixPerson.setTextColor(getResources().getColor(R.color.black));

            for (int i = 0; i < singleRecipeDetailData.getHow_much().size(); i++) {
                if (singleRecipeDetailData.getHow_much().get(i).getServings().equals("4")) {
                    person = i;
                    recipeIngredientsAdapter = new RecipeIngredientsAdapter(CategoryItemViewActivity.this, singleRecipeDetailData.getHow_much().get(i).getIngredients());
                    break;
                }
            }
        } else if (servings.contains("2")) {
            tv_twoPerson.setTextColor(Color.parseColor(mcolorCode));
            tv_twoPerson.setTypeface(fontRobotoLight, Typeface.BOLD);
            tv_fourPerson.setTypeface(fontRobotoLight, Typeface.NORMAL);
            tv_sixPerson.setTypeface(fontRobotoLight, Typeface.NORMAL);

            tv_sixPerson.setTextColor(getResources().getColor(R.color.black));
            tv_fourPerson.setTextColor(getResources().getColor(R.color.black));

            for (int i = 0; i < singleRecipeDetailData.getHow_much().size(); i++) {
                if (singleRecipeDetailData.getHow_much().get(i).getServings().equals("2")) {
                    person = i;
                    recipeIngredientsAdapter = new RecipeIngredientsAdapter(CategoryItemViewActivity.this, singleRecipeDetailData.getHow_much().get(i).getIngredients());
                    break;
                }
            }
        }
        OnGridItemClick(0);
        recipeIngredientsAdapter.notifyDataSetChanged();
        grid_recipe_ingredient.setAdapter(recipeIngredientsAdapter);
    }

    private void OnGridItemClick(final int i) {
        grid_recipe_ingredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {
                String ingredientName = singleRecipeDetailData.getHow_much().get(i).getIngredients().get(j).getName();
                String ingredientImage = AppConfig.IMAGE_URL + singleRecipeDetailData.getHow_much().get(i).getIngredients().get(j).getImage();
                String ingredientAbout = singleRecipeDetailData.getHow_much().get(i).getIngredients().get(j).getAbout();

                GlobalClass.setPrefrenceString(CategoryItemViewActivity.this, "ingredientName", ingredientName);
                GlobalClass.setPrefrenceString(CategoryItemViewActivity.this, "ingredientImage", ingredientImage);
                GlobalClass.setPrefrenceString(CategoryItemViewActivity.this, "ingredientAbout", ingredientAbout);
                DialogIngredientDetail cdd = new DialogIngredientDetail(CategoryItemViewActivity.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                cdd.setCancelable(false);
                cdd.show();
            }
        });
    }

    public void addtoFav() {
        if (isFav) {
            Drawable mDrawable = CategoryItemViewActivity.this.getResources().getDrawable(R.drawable.ic_select);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY));
            iv_fav.setImageDrawable(mDrawable);
        } else {
            Drawable mDrawable = CategoryItemViewActivity.this.getResources().getDrawable(R.drawable.ic_unse);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY));
            iv_fav.setImageDrawable(mDrawable);
        }
    }

    private void addFav() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);
//        user_id get from global`

        Log.e("----user_id---------", "CategoryItemViewActivity--- " + user_id);
        Log.e("----recipe token----", "CategoryItemViewActivity--- " + mSingleRecipeToken);

        callWebServices.AddToFavouriteRecipe(mSingleRecipeToken, user_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
//                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse.toString());
                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        GlobalClass.showToast(CategoryItemViewActivity.this, getResources().getString(R.string.fav_added));
                    } else {
                        GlobalClass.showToast(CategoryItemViewActivity.this, getResources().getString(R.string.fav_removed));
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("RetrofitError", " ---- :  " + error);
                GlobalClass.showToast(CategoryItemViewActivity.this, getResources().getString(R.string.try_after_some_time));
            }
        });
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.you_must_be_login_first)
                .setCancelable(false)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(CategoryItemViewActivity.this, LogInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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

    public ArrayList<ShoppingData> getRecipeTitle() {
        getChildDetail();
        shoppingItemDatas = new ArrayList<>();
        shoppingItemData = new ShoppingData();
        shoppingItemData.setRecipeName(singleRecipeDetailData.getRecipe_name());
        shoppingItemData.setRecipeImage(singleRecipeDetailData.getRecipe_image().get(0));
        shoppingItemData.setServingsPerson(singleRecipeDetailData.getHow_much().get(person).getServings());
        shoppingItemData.setRecipeToken(singleRecipeDetailData.getToken());
        shoppingItemData.setSubCategoryItemList(recipeIngredientsDatas);
//        Log.e("-AAAA--", "--Ankita---getRecipe_name()-------" + singleRecipeDetailData.getRecipe_name());
//        Log.e("-AAAA--", "--Ankita---shoppingItemData-------" + singleRecipeDetailData.getRecipe_image().size());
        shoppingItemDatas.add(shoppingItemData);
        return shoppingItemDatas;
    }

    public ArrayList<RecipeIngredientsDataNew> getChildDetail() {
        recipeIngredientsDatas = new ArrayList<>();
        for (int i = 0; i < singleRecipeDetailData.getHow_much().get(person).getIngredients().size(); i++) {
            RecipeIngredientsDataNew recipeIngredientsDataNew = new RecipeIngredientsDataNew();
            recipeIngredientsDataNew.setName(singleRecipeDetailData.getHow_much().get(person).getIngredients().get(i).getName());
            recipeIngredientsDataNew.setAmount(singleRecipeDetailData.getHow_much().get(person).getIngredients().get(i).getAmount());
            recipeIngredientsDataNew.setImage(singleRecipeDetailData.getHow_much().get(person).getIngredients().get(i).getImage());
            recipeIngredientsDataNew.setServingsPerson(singleRecipeDetailData.getHow_much().get(person).getServings());
//            Log.e("-BBBBBB--", "--servings-------" + singleRecipeDetailData.getHow_much().get(person).getServings());
            servingPerson = singleRecipeDetailData.getHow_much().get(person).getServings();
            recipeIngredientsDatas.add(recipeIngredientsDataNew);
        }

        return recipeIngredientsDatas;
    }

    private void addToShoppingList() {

        contentValues = new ContentValues();
        Gson gson;
        contentValues.put(FoodieLocalData.KEY_RECIPE_ID, mSingleRecipeToken);
        contentValues.put(FoodieLocalData.KEY_PERSON, person);

        gson = new Gson();
        final String recipeData = gson.toJson(getRecipeTitle().get(0));
        contentValues.put(FoodieLocalData.KEY_RECIPE_DATA, recipeData);

        gson = new Gson();
        final String ingredientData = gson.toJson(getChildDetail());
//        GlobalClass.printLog("Ingredient Data", "````````````````" + ingredientData);

        contentValues.put(FoodieLocalData.KEY_RECIPE_INGREDIENT_DATA, ingredientData);
        GlobalClass.printLog("contentValues", "" + contentValues);
        FoodieLocalData dbHelper = new FoodieLocalData(this);
        Cursor cursor = dbHelper.getShoppingCartList();
        GlobalClass.printLog("cursor", "````````````````" + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            if (dbHelper.checkTokenAlreadyExists(mSingleRecipeToken) == true) {
                GlobalClass.showToast(CategoryItemViewActivity.this, getResources().getString(R.string.already_added));
            } else {
                askToAdd(contentValues);
            }
        } else {
            askToAdd(contentValues);
        }
    }

    private void askToAdd(final ContentValues contentValues) {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to Add Ingredients for " + servingPerson + " Persons?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbHelper.insertData(FoodieLocalData.SHOPPING_CART_MASTER, contentValues);
                        GlobalClass.showToast(CategoryItemViewActivity.this, getString(R.string.added_to_shopping_list));
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_twoPerson:
                setIngredients("2");
                break;
            case R.id.tv_fourPerson:
                setIngredients("4");
                break;
            case R.id.tv_sixPerson:
                setIngredients("6");
                break;
            case R.id.tv_addToShoppingList:
                addToShoppingList();
                break;
            case R.id.rl_stepViseRecipe:
                if (recipe_video_url != " " && !recipe_video_url.isEmpty() && recipe_video_url != null) {

                    AlertDialog alertDialog = new AlertDialog.Builder(CategoryItemViewActivity.this).create();
                    alertDialog.setTitle("Let's cooking");
                    alertDialog.setMessage("Select option");

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.video), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
//                            String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
//                            if (!recipe_video_url.isEmpty() && recipe_video_url.matches(pattern)) {
//                                / Valid youtube URL
//                                String short_url = recipe_video_url.substring(recipe_video_url.lastIndexOf('/') + 1, recipe_video_url.length());
//                                watchYoutubeVideo(CategoryItemViewActivity.this, short_url);
//                            } else {
//                                 Not Valid youtube URL
//                                Intent intent = new Intent(CategoryItemViewActivity.this, VideoPlayerActivity.class);
//                                intent.putExtra("url", recipe_video_url);
//                                startActivity(intent);
//                            }
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe_video_url));
                            startActivity(browserIntent);
                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.setp_by_step), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(CategoryItemViewActivity.this, StepByStepPlayActivity.class);
                            intent.putExtra("mcolorCode", mcolorCode);
                            intent.putExtra("mSingleRecipeToken", mSingleRecipeToken);
                            startActivity(intent);
                        }
                    });

                    alertDialog.show();
                } else {
                    Intent intent = new Intent(CategoryItemViewActivity.this, StepByStepPlayActivity.class);
                    intent.putExtra("mcolorCode", mcolorCode);
                    intent.putExtra("mSingleRecipeToken", mSingleRecipeToken);
                    startActivity(intent);
                }
                break;
            case R.id.iv_fav:
                if (GlobalClass.getPrefrenceBoolean(CategoryItemViewActivity.this, "isLogin", false) == true) {
                    isFav = !isFav;
                    addtoFav();
                    addFav();
                } else {
                    showDialog();
                }
                break;
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.rl_comment_bottom:
                finish();
                Intent intent2 = new Intent(CategoryItemViewActivity.this, ReviewActivity.class);
                intent2.putExtra("mSingleRecipeToken", mSingleRecipeToken);
                startActivity(intent2);
                break;
        }
    }

    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}