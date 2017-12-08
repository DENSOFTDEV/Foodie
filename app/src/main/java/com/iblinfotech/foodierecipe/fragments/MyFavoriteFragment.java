package com.iblinfotech.foodierecipe.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.ads.AdListener;
import com.iblinfotech.foodierecipe.Activity.MainActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.adapter.MyFavouriteAdapter;
import com.iblinfotech.foodierecipe.model.MyFavouriteData;
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

public class MyFavoriteFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rc_FavoriteList;
    private KProgressHUD kProgressHUD;
    public static TextView tv_noFav, tv_found;
    public static Button btn_get_new_recipe;
    private List<MyFavouriteData> myFavouriteDatas = new ArrayList<>();
    private MyFavouriteAdapter myFavouriteAdapter;
    private String user_id;
    public static ImageView imageView2;

    int removeAds;


    public MyFavoriteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favourite, container, false);
        GlobalClass global = new GlobalClass(getContext());
        user_id = GlobalClass.getPrefrenceString(getContext(), "user_id", "");

        try {
            removeAds = GlobalClass.getPrefrenceInt(getContext(), "removeads", 3);
            if (removeAds != 0) {
                showInterstitial();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        setContent(view);
        if (GlobalClass.isInternetOn(getContext())) {

            if (GlobalClass.getPrefrenceBoolean(getContext(), "isLogin", false) == true && user_id.length() > 0) {
                getFavorite();
            } else {
                tv_noFav.setVisibility(View.VISIBLE);
                tv_found.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                btn_get_new_recipe.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }

        Typeface fontRobotoLight = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Light.ttf");
        tv_noFav.setTypeface(fontRobotoLight);
        tv_found.setTypeface(fontRobotoLight);
        Typeface fontRegular = Typeface.createFromAsset(getContext().getAssets(), "PlayfairDisplay-Regular.otf");
        btn_get_new_recipe.setTypeface(fontRegular);
        btn_get_new_recipe.setOnClickListener(this);
        return view;
    }

    private void showInterstitial() {
        if (GlobalClass.isInternetOn(getActivity()) && MainActivity.interstitial.isLoaded()) {
            MainActivity.interstitial.show();
            MainActivity.interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                }

                @Override
                public void onAdClosed() {
                    MainActivity.interstitial.loadAd(MainActivity.adRequest);
                }
            });
        } else {
        }
    }

    private void getFavorite() {
        kProgressHUD = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("Favourite Loading...")
                .show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);
//        user_id get from global`

        Log.e("----user_id---------", "SearchActivity--- " + user_id);

        callWebServices.getFavourite(user_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse.toString());
//                    Log.e("----getfavourite---", "" + mainResponseObject.toString(4));
                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {

                        JSONArray responseJsonArray = new JSONArray(mainResponseObject.getString(AppConfig.RESULT));
                        myFavouriteDatas = LoganSquare.parseList(responseJsonArray.toString(), MyFavouriteData.class);
                        Log.e("----size---------", "myFavouriteDatas--- " + myFavouriteDatas.size());

                        for (int i = 0; i < myFavouriteDatas.size(); i++) {
                            myFavouriteDatas.get(i).setFavourite(true);
                        }
                        myFavouriteAdapter = new MyFavouriteAdapter(getContext(), myFavouriteDatas);
                        rc_FavoriteList.setAdapter(myFavouriteAdapter);
                        myFavouriteAdapter.notifyDataSetChanged();

                    } else {
                    }
                    if (myFavouriteDatas.size() > 0) {
                        tv_noFav.setVisibility(View.GONE);
                        tv_found.setVisibility(View.GONE);
                        imageView2.setVisibility(View.GONE);
                        btn_get_new_recipe.setVisibility(View.GONE);
                    } else {
                        tv_noFav.setVisibility(View.VISIBLE);
                        tv_found.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.VISIBLE);
                        btn_get_new_recipe.setVisibility(View.VISIBLE);
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
                Toast.makeText(getActivity(), "Please Try After Some Time!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setContent(View view) {
        rc_FavoriteList = (RecyclerView) view.findViewById(R.id.rc_FavoriteList);
        tv_noFav = (TextView) view.findViewById(R.id.tv_noFav);
        tv_found = (TextView) view.findViewById(R.id.tv_found);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        btn_get_new_recipe = (Button) view.findViewById(R.id.btn_get_new_recipe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rc_FavoriteList.setLayoutManager(layoutManager);
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
