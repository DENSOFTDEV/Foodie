package com.iblinfotech.foodierecipe;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ornolfr.ratingview.RatingView;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.CallWebServices;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private RatingView rating_set_review;
    private EditText edt_review_here;
    private Button btn_review_submit;
    private KProgressHUD kProgressHUD;
    private String mSingleRecipeToken;
    private String user_id;
    private String mTotalStars;
    private String mReview;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_review);
        user_id = GlobalClass.getPrefrenceString(AddReviewActivity.this, "user_id", "");
        setContent();
    }

    private void setContent() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        rating_set_review = (RatingView) findViewById(R.id.rating_set_review);
        edt_review_here = (EditText) findViewById(R.id.edt_review_here);
        btn_review_submit = (Button) findViewById(R.id.btn_review_submit);
        textView2 = (TextView) findViewById(R.id.textView2);

        iv_back.setOnClickListener(this);
        btn_review_submit.setOnClickListener(this);

        rating_set_review.setOnRatingChangedListener(new RatingView.OnRatingChangedListener() {
            @Override
            public void onRatingChange(float oldRating, float newRating) {
                mTotalStars = String.valueOf(newRating);
            }
        });
        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        btn_review_submit.setTypeface(fontRegular);
        textView2.setTypeface(fontRegular);
        Typeface RobotoLight = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");
        edt_review_here.setTypeface(RobotoLight);
    }

    private void AddReview() {
        mSingleRecipeToken = CategoryItemViewActivity.singleRecipeDetailData.getToken();
        mReview = edt_review_here.getText().toString();
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("Please wait..")
                .show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);
        callWebServices.AddReview(user_id, mReview, mSingleRecipeToken, mTotalStars, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse);
                    GlobalClass.showToast(AddReviewActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));
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
                GlobalClass.showToast(AddReviewActivity.this, getResources().getString(R.string.try_after_some_time));
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (kProgressHUD != null && kProgressHUD.isShowing()) {
            kProgressHUD.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                startActivity(new Intent(AddReviewActivity.this, ReviewActivity.class));
                break;
            case R.id.btn_review_submit:
                if (GlobalClass.isInternetOn(AddReviewActivity.this)) {
                    if (mTotalStars != null) {
                        if (!edt_review_here.getText().toString().trim().isEmpty()) {
                            AddReview();
                            startActivity(new Intent(AddReviewActivity.this, ReviewActivity.class));
                            finish();
                        } else {
                            GlobalClass.showToast(AddReviewActivity.this, getResources().getString(R.string.enter_comment));
                        }
                    } else {
                        GlobalClass.showToast(AddReviewActivity.this, getResources().getString(R.string.give_rating));
                    }
                } else {
                    GlobalClass.showToast(AddReviewActivity.this, getResources().getString(R.string.no_internet_connection));
                }
                break;
        }
    }


}
