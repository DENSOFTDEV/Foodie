package com.iblinfotech.foodierecipe.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.adapter.CategoryItemListAdapter;
import com.iblinfotech.foodierecipe.model.RecipeItemTitleData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.CallWebServices;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CategoryItem_newestFragment extends Fragment {
    private GridView grid_recipe_item;
    View homeView;
    private KProgressHUD kProgressHUD;
    private List<RecipeItemTitleData> recipeItemTitleDatas = new ArrayList<>();
    private List<RecipeItemTitleData> newRecipeItemTitleDatas = new ArrayList<>();
    // FLAG FOR CURRENT PAGE
    private int current_page;

    // BOOLEAN TO CHECK IF NEW FEEDS ARE LOADING
    Boolean loadingMore = true;
    Boolean stopLoadingData = false;
    private CategoryItemListAdapter categoryItemListAdapter;
    private ProgressBar progress_load_more;
    private LinearLayout linlaProgressBar;

    public CategoryItem_newestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.fragment_category_item_newest, container, false);
        setContent(homeView);
        if (GlobalClass.isInternetOn(getContext())) {
            current_page = 0;
            getRecipe();
        } else {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
        return homeView;
    }

    private void setContent(View view) {
        GlobalClass global = new GlobalClass(getContext());
        grid_recipe_item = (GridView) view.findViewById(R.id.grid_recipe_item);
//        progress_load_more = (ProgressBar) view.findViewById(R.id.progress_load_more);
//        linlaProgressBar = (LinearLayout) view.findViewById(R.id.linlaProgressBar);
//        linlaProgressBar.setVisibility(View.GONE);

// ONSCROLLLISTENER
        grid_recipe_item.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(loadingMore)) {

                    if (stopLoadingData == false) {
                        Log.e("--------endd-----", "-------end------");
//                        linlaProgressBar.setVisibility(View.GONE);
                        loadMorePhotos();
                    }

                }
            }
        });
    }
    private void loadMorePhotos() {
        loadingMore = true;
        current_page += 1;
//        linlaProgressBar.setVisibility(View.VISIBLE);
//        Log.e("--------loadMorePhotos-method-----", "current_page---" + current_page);

        String token = GlobalClass.getPrefrenceString(getContext(), "mCategoryToken", "");
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);

        callWebServices.getRecipeByCategory(token, String.valueOf(current_page), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    Log.e("232323", "success: "+response.toString() );
                    Log.e("232323", "success: "+response2.toString() );
//                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse.toString());
//                    Log.e("-----------------------", "" + mainResponseObject.toString(4));
                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {

                        JSONArray responseJsonArray = new JSONArray(mainResponseObject.getString(AppConfig.RESULT));
//                        Log.e("SuccessData", "responseJsonArray---------more------- " + responseJsonArray);

                        newRecipeItemTitleDatas = LoganSquare.parseList(responseJsonArray.toString(), RecipeItemTitleData.class);
                        Log.e("----size ---- more ----", "newRecipeItemTitleDatas--- " + newRecipeItemTitleDatas.size());
                        int currentPosition = grid_recipe_item.getFirstVisiblePosition();
                        recipeItemTitleDatas.addAll(newRecipeItemTitleDatas);
                        categoryItemListAdapter = new CategoryItemListAdapter(getContext(), recipeItemTitleDatas);
                        grid_recipe_item.setAdapter(categoryItemListAdapter);
                        grid_recipe_item.setSelection(currentPosition + 1);
                        categoryItemListAdapter.notifyDataSetChanged();
                        loadingMore = false;
//                        linlaProgressBar.setVisibility(View.GONE);

                    } else {
//                        AlertDialogParser.showMessageDialog(getContext(), mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void failure(RetrofitError error) {
//                if (kProgressHUD.isShowing()) {
//                    kProgressHUD.dismiss();
//                }
                Log.e("RetrofitError", " ---- :  " + error);
//                AlertDialogParser.showMessageDialog(getContext(), "" + error);
                Toast.makeText(getActivity(), "Please Try After Some Time!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getRecipe() {
//        Log.e("--------getRecipe-method-----", "-------current_page------" + current_page);
        loadingMore = true;
        String token = GlobalClass.getPrefrenceString(getContext(), "mCategoryToken", "");
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);

        callWebServices.getRecipeByCategory(token, String.valueOf(current_page), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
//                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse.toString());
//                    Log.e("------------response-----------", "" + mainResponseObject.toString(4));
                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {

                        JSONArray responseJsonArray = new JSONArray(mainResponseObject.getString(AppConfig.RESULT));
//                        Log.e("SuccessData", "responseJsonArray---------------- " + responseJsonArray);

                        recipeItemTitleDatas = LoganSquare.parseList(responseJsonArray.toString(), RecipeItemTitleData.class);
                        Log.e("----size----", "recipeItemTitleDatas--- " + recipeItemTitleDatas.size());
                        categoryItemListAdapter = new CategoryItemListAdapter(getContext(), recipeItemTitleDatas);
                        grid_recipe_item.setAdapter(categoryItemListAdapter);
                        loadingMore = false;
                        categoryItemListAdapter.notifyDataSetChanged();

                    } else {
//                        AlertDialogParser.showMessageDialog(getContext(), mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void failure(RetrofitError error) {
//                if (kProgressHUD.isShowing()) {
//                    kProgressHUD.dismiss();
//                }
                Log.e("RetrofitError", " ---- :  " + error);
//                AlertDialogParser.showMessageDialog(getContext(), "" + error);
                Toast.makeText(getActivity(), "Please Try After Some Time!", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
