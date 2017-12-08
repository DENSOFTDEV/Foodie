package com.iblinfotech.foodierecipe.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.iblinfotech.foodierecipe.Activity.DiscoverSwipeActivity;
import com.iblinfotech.foodierecipe.Activity.LogInActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.Activity.SearchActivity;
import com.iblinfotech.foodierecipe.adapter.CategoryAdapter;
import com.iblinfotech.foodierecipe.model.RecipeCatagoryData;
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
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyHomeFragment extends Fragment {
    private GridView grid_recipe_catagory;
    private KProgressHUD kProgressHUD;
    private TextView tv_discover, tv_search;
    private ImageView iv_search;
    private AdView mAdView;
    int removeAds;


    public MyHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        setContent(homeView);

        removeAds = GlobalClass.getPrefrenceInt(getContext(), "removeads", 3);


        if (GlobalClass.isInternetOn(getContext())) {
            getCatagories(homeView);
            if (removeAds != 0) {
                setAdMob(homeView);
            }
        } else {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }

        return homeView;
    }

    private void setContent(View view) {
        grid_recipe_catagory = (GridView) view.findViewById(R.id.grid_recipe_catagory);
        tv_discover = (TextView) view.findViewById(R.id.tv_discover);
//        tv_search = (TextView) view.findViewById(R.id.tv_search);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_search.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth * 0.166));
        Typeface fontRegular = Typeface.createFromAsset(getContext().getAssets(), "PlayfairDisplay-Regular.otf");
        tv_discover.setTypeface(fontRegular);
//        tv_search.setTypeface(RobotoLight);
        tv_discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DiscoverSwipeActivity.class));
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });
    }

    private void setAdMob(View view) {

        mAdView = (AdView) view.findViewById(R.id.ads2);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
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

    private void getCatagories(View homeView) {
        kProgressHUD = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("Recipe Loading...")
                .show();

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);
        callWebServices.gettotalRecipeCounter(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse);

                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        JSONArray catArray = mainResponseObject.getJSONArray(AppConfig.RESULT);
                        Log.e("SuccessData", "JsonObject---------------- " + catArray);
                        //Add Data
                        List<RecipeCatagoryData> recipeCatagoryDatas = LoganSquare.parseList(catArray.toString(), RecipeCatagoryData.class);
                        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), recipeCatagoryDatas, false);
                        grid_recipe_catagory.setAdapter(categoryAdapter);
                    } else {
                        Toast.makeText(getActivity(), mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
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
//                AlertDialogParser.showMessageDialog(getContext(), "" + error);
                //null pointer exception
//                java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.res.Resources android.content.Context.getResources()' on a null object reference
                Toast.makeText(getActivity(), "Please Try Again Later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}