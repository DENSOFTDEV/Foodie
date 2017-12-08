package com.iblinfotech.foodierecipe.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.iblinfotech.foodierecipe.Activity.MainActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.adapter.WeeklyRecipeAdapter;
import com.iblinfotech.foodierecipe.model.WeeklyRecipyData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.CallWebServices;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWeeklyMenuFragment extends Fragment implements View.OnClickListener {

    private RecyclerViewPager rec_weekly_recipe;
    private LinearLayout ll_offer_another_recipe;
    private KProgressHUD kProgressHUD;
    private List<WeeklyRecipyData> weeklyRecipyDatas = new ArrayList<>();
    String user_id, user_token;
    private TextView tv_offer_another_recipe;
    WeeklyRecipeAdapter weeklyRecipeAdapter = new WeeklyRecipeAdapter(getContext(), weeklyRecipyDatas);
    public String[] weekName = {"week1", "week2", "week3"};
    public int mIfCounter = 1;
    public static int scrollPosition;
    private String mDay;
    private String weekly_day;
    private LinearLayoutManager layoutManager;
    private AdView mAdView;
    int removeAds;


    public MyWeeklyMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        removeAds = GlobalClass.getPrefrenceInt(getContext(), "removeads", 3);

        View view = inflater.inflate(R.layout.fragment_weekly_menu, container, false);

        GlobalClass global = new GlobalClass(getContext());
        user_id = GlobalClass.getPrefrenceString(getContext(), "user_id", "");
        setContent(view);
        if (GlobalClass.isInternetOn(getContext())) {
            getWeeklyMenu("week1");
            if (removeAds != 0) {
                setAdMob(view);
            }
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void setContent(View view) {
//        rec_weekly_recipe = (RecyclerView) view.findViewById(R.id.rec_weekly_recipe);
        rec_weekly_recipe = (RecyclerViewPager) view.findViewById(R.id.rec_weekly_recipe);
        ll_offer_another_recipe = (LinearLayout) view.findViewById(R.id.ll_offer_another_recipe);
        tv_offer_another_recipe = (TextView) view.findViewById(R.id.tv_offer_another_recipe);

        rec_weekly_recipe.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + mRecyclerViewPager.getFirstVisiblePosition());
                int childCount = rec_weekly_recipe.getChildCount();
                int width = rec_weekly_recipe.getChildAt(0).getWidth();
                int padding = (rec_weekly_recipe.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        rec_weekly_recipe.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
            }
        });

        rec_weekly_recipe.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (rec_weekly_recipe.getChildCount() < 3) {
                    if (rec_weekly_recipe.getChildAt(1) != null) {
                        if (rec_weekly_recipe.getCurrentPosition() == 0) {
                            View v1 = rec_weekly_recipe.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = rec_weekly_recipe.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (rec_weekly_recipe.getChildAt(0) != null) {
                        View v0 = rec_weekly_recipe.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (rec_weekly_recipe.getChildAt(2) != null) {
                        View v2 = rec_weekly_recipe.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });

        Typeface fontRegular = Typeface.createFromAsset(getContext().getAssets(), "PlayfairDisplay-Regular.otf");
        tv_offer_another_recipe.setTypeface(fontRegular);
        ll_offer_another_recipe.setOnClickListener(this);
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

    private void scrollToPosition() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < weeklyRecipyDatas.size(); i++) {
            weekly_day = weeklyRecipyDatas.get(i).getWeekly_day();
            switch (day) {
                case 1:
                    mDay = "SUNDAY";
                    scrollPosition = 0;
                    break;
                case 2:
                    mDay = "MONDAY";
                    scrollPosition = 1;
                    break;

                case 3:
                    mDay = "TUESDAY";
                    scrollPosition = 2;
                    break;

                case 4:
                    mDay = "WEDNESDAY";
                    scrollPosition = 3;
                    break;

                case 5:
                    mDay = "THURSDAY";
                    scrollPosition = 4;
                    break;

                case 6:
                    mDay = "FRIDAY";
                    scrollPosition = 5;
                    break;

                case 7:
                    mDay = "SATURDAY";
                    scrollPosition = 6;
                    break;
                default:
                    mDay = "SUNDAY";
                    scrollPosition = 0;
                    break;
            }
            if (mDay.equalsIgnoreCase(weekly_day)) {
                layoutManager.scrollToPositionWithOffset(scrollPosition, 7);
                Log.e("----------", "scrollPosition:----------------------------------------" + scrollPosition);
                Log.e("----------", "weekly_day:----------------------------------------" + weekly_day);
                Log.e("----------", "mDay:----------------------------------------" + mDay);

            }

        }


    }

    private void getWeeklyMenu(final String week) {
        kProgressHUD = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("Weekly Menu getting ready...")
                .show();
        Log.e("----------", "Weekly_userId:----------------------------------------" + user_id);
        Log.e("----------", "Week:-----week------------------------------" + week);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);
        callWebServices.getWeeklyMenu(user_token, user_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {

                    Toast.makeText(getActivity(), "........." + response2, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), ".........1" + response, Toast.LENGTH_SHORT).show();
                    Log.e("weekly", "success: " + response2);
                    Log.e("weekly", "success: " + response);
                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse.toString());
//                    Log.e("-----------response----------------", "" + mainResponseObject.toString(4));
                    JSONArray responseJsonArray = new JSONArray(mainResponseObject.getString(week));
//                    Log.e("weekly menu:", "week1---------------- " + responseJsonArray.toString(4));
                    weeklyRecipyDatas = LoganSquare.parseList(responseJsonArray.toString(), WeeklyRecipyData.class);
                    weeklyRecipeAdapter = new WeeklyRecipeAdapter(getContext(), weeklyRecipyDatas);

                    layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                    rec_weekly_recipe.setLayoutManager(layoutManager);
                    rec_weekly_recipe.setHasFixedSize(true);
                    rec_weekly_recipe.setAdapter(weeklyRecipeAdapter);
                    weeklyRecipeAdapter.notifyDataSetChanged();
                    scrollToPosition();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_offer_another_recipe:
                String week = weekName[mIfCounter];
                if (mIfCounter < weekName.length - 1) {
                    mIfCounter++;
                } else {
                    mIfCounter = 0;
                }
                Log.e("---week----", "-============" + week);
                getWeeklyMenu(week);
                break;
        }
    }


}
