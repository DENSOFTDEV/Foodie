package com.iblinfotech.foodierecipe.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.adapter.SwipeDeckAdapter;
import com.iblinfotech.foodierecipe.model.DiscoverModeData;
import com.iblinfotech.foodierecipe.utils.AlertDialogParser;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.CallWebServices;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.iblinfotech.foodierecipe.view.SwipeDeck;
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

public class DiscoverSwipeActivity extends AppCompatActivity implements View.OnClickListener {

    private SwipeDeck swipe_deck;
    private ImageView iv_cancel;
    public static List<DiscoverModeData> discoverModeDatas = new ArrayList<>();
    public static SwipeDeckAdapter adapter;
    private TextView tv_discover, tv_noIdea, tv_swipeCard;
    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_discover_swipe);

        setContent();
        getDiscoverMode();
    }

    private void setContent() {
        swipe_deck = (SwipeDeck) findViewById(R.id.swipe_deck);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        tv_swipeCard = (TextView) findViewById(R.id.tv_swipeCard);
        tv_discover = (TextView) findViewById(R.id.tv_discover);
        tv_noIdea = (TextView) findViewById(R.id.tv_noIdea);

        swipe_deck.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth));
        swipe_deck.setHardwareAccelerationEnabled(true);
        iv_cancel.setOnClickListener(this);

        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        Typeface RobotoRegular = Typeface.createFromAsset(this.getAssets(), "Roboto-Regular.ttf");
        tv_discover.setTypeface(fontRegular);

        tv_noIdea.setTypeface(RobotoRegular);
        tv_swipeCard.setTypeface(RobotoRegular);
    }

    private void getDiscoverMode() {
        kProgressHUD = KProgressHUD.create(DiscoverSwipeActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("Fetching recipe...")
                .show();

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);
//        user_id get from global
        GlobalClass global = new GlobalClass(DiscoverSwipeActivity.this);
        String user_id = GlobalClass.getPrefrenceString(DiscoverSwipeActivity.this, "user_id", "");
        Log.e("----user_id---------", "discoverModeDatas--- " + user_id);

        callWebServices.getDiscoverMode(user_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse.toString());
//                    Log.e("----getDiscover---", "" + mainResponseObject.toString(4));
                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        JSONArray responseJsonArray = new JSONArray(mainResponseObject.getString(AppConfig.RESULT));
                        Log.e("----discover---------", "responseJsonArray--- " + responseJsonArray.toString(4));
                        discoverModeDatas = LoganSquare.parseList(responseJsonArray.toString(), DiscoverModeData.class);
                        Log.e("----size---------", "discoverModeDatas--- " + discoverModeDatas.size());
                        adapter = new SwipeDeckAdapter(discoverModeDatas, DiscoverSwipeActivity.this);
                        swipe_deck.setAdapter(adapter);

                    } else {
                        AlertDialogParser.showMessageDialog(DiscoverSwipeActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));
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
                GlobalClass.showToast(DiscoverSwipeActivity.this, getResources().getString(R.string.try_after_some_time));
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_cancel:
                finish();
                break;

        }
    }
}
