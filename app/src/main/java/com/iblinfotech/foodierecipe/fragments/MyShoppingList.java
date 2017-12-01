package com.iblinfotech.foodierecipe.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.iblinfotech.foodierecipe.MainActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.SqLiteHelper.FoodieLocalData;
import com.iblinfotech.foodierecipe.adapter.CategoryExpandableAdapter;
import com.iblinfotech.foodierecipe.adapter.IngredientShoppingListAdapter;
import com.iblinfotech.foodierecipe.model.RecipeIngredientsData;
import com.iblinfotech.foodierecipe.model.ShoppingData;
import com.iblinfotech.foodierecipe.utils.GlobalClass;

import java.util.ArrayList;
import java.util.HashMap;

public class MyShoppingList extends Fragment implements View.OnClickListener {
    public static TextView tv_empty_shoppingList;
    public static ImageView imageView2;
    public static Button btn_get_new_recipe;
    private ExpandableListView expandableListView;
    public static HashMap<Object, ArrayList<RecipeIngredientsData>> expandableListDetail = new HashMap<>();
    private FoodieLocalData dbHelper;
    private AdView mAdView;

    public MyShoppingList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View homeView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        dbHelper = new FoodieLocalData(getActivity());
        setContent(homeView);
        setAdMob(homeView);
        getShoppingData();
        return homeView;
    }

    private void setContent(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        tv_empty_shoppingList = (TextView) view.findViewById(R.id.tv_empty_shoppingList);
        btn_get_new_recipe = (Button) view.findViewById(R.id.btn_get_new_recipe);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        Typeface fontRobotoLight = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Light.ttf");
        tv_empty_shoppingList.setTypeface(fontRobotoLight);
        Typeface fontRegular = Typeface.createFromAsset(getContext().getAssets(), "PlayfairDisplay-Regular.otf");
        btn_get_new_recipe.setTypeface(fontRegular);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        btn_get_new_recipe.setOnClickListener(this);
    }

    private void setAdMob(View view) {

        mAdView = (AdView) view.findViewById(R.id.ads);
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
    private void getShoppingData() {
        ArrayList<ShoppingData> shoppingDataArrayList = new ArrayList<>();
        ArrayList<ShoppingData[]> recipeIngredientsDataArrayList = new ArrayList<>();
        ShoppingData[] shoppingData1 = new ShoppingData[0];
        Cursor cursor = dbHelper.getShoppingCartList();
        Log.e("Cursor Counter", "--------------------------------------------------" + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            tv_empty_shoppingList.setVisibility(View.GONE);
            btn_get_new_recipe.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                Gson gson;
                String recipeId = cursor.getString(cursor.getColumnIndex(FoodieLocalData.KEY_RECIPE_ID));
                String person = cursor.getString(cursor.getColumnIndex(FoodieLocalData.KEY_PERSON));
                String recipeData = cursor.getString(cursor.getColumnIndex(FoodieLocalData.KEY_RECIPE_DATA));
                String ingredientData = cursor.getString(cursor.getColumnIndex(FoodieLocalData.KEY_RECIPE_INGREDIENT_DATA));
//                Log.e("Cursor Counter", "------------recipeId--------------------------------------" + recipeId);
                GlobalClass.printLog("---A---added..-----", "recipeData......" + recipeData);

//                Log.e("Cursor Counter", "------------person--------------------------------------" + person);
//                Log.e("Cursor Counter", "-------------ingredientData-------------------------------------" + ingredientData);
                gson = new Gson();
                ShoppingData shoppingData = gson.fromJson(recipeData, ShoppingData.class);
//                GlobalClass.printLog("Recipe shoppingData----------", "" + shoppingData.toString());

                gson = new Gson();
                shoppingData1 = gson.fromJson(ingredientData, ShoppingData[].class);
                GlobalClass.printLog("Recipe shoppingData1----------", "" + shoppingData1);
                recipeIngredientsDataArrayList.add(shoppingData1);

                shoppingDataArrayList.add(shoppingData);

                cursor.moveToNext();
            }
        } else {
            tv_empty_shoppingList.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            btn_get_new_recipe.setVisibility(View.VISIBLE);
        }
        CategoryExpandableAdapter categoryExpandableAdapter = new CategoryExpandableAdapter(getActivity(), shoppingDataArrayList);
        expandableListView.setAdapter(categoryExpandableAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_new_recipe:
                if (GlobalClass.isInternetOn(getContext())) {
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
