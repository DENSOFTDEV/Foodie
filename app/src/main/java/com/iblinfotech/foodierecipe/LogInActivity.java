package com.iblinfotech.foodierecipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.CallWebServices;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.iblinfotech.foodierecipe.utils.Validator;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_username, edt_password;
    private Button btn_login;
    private TextView tv_forgotPass, tv_SignUp,textView2, fbLogin;;
    private KProgressHUD kProgressHUD;
    private String mUsername, mPassword,fb_id, fb_userName, fb_profilePic, fb_name, fb_email, fb_birthDate,fb_user_gender,device_id;
    private LinearLayout lv_fblogin;
    private ImageView iv_back;

    // Facebook
    private ProfileTracker profileTracker;
    private PendingAction pendingAction = PendingAction.NONE;
    private CallbackManager callbackManager;

    private final String PENDING_ACTION_BUNDLE_KEY = "com.progressgraphs.progressgraphs:PendingAction";
    private static final String PERMISSION = "publish_actions";


    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.e("HelloFacebook", "Canceled");
        }
        @Override
        public void onError(FacebookException error) {
            Log.e("fb error", "" + error);
            Toast.makeText(LogInActivity.this, "Facebook login error", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.e("HelloFacebook", "Success!");
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(LogInActivity.this).setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton("ok", null).show();
        }
    };
    private AdView mAdView;

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                Log.e("accessToken----", ":: " + accessToken);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        Bundle bFacebookData = getFacebookData(object);
                    }
                });

                Bundle parameters = new Bundle();
                Log.e("parameters------", ":: " + accessToken);

                parameters.putString("fields", "id,name, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

                handlePendingAction();
            }

            @Override
            public void onCancel() {
                if (pendingAction != PendingAction.NONE) {
                    pendingAction = PendingAction.NONE;
                }
                updateUI();
            }

            @Override
            public void onError(FacebookException exception) {
                if (pendingAction != PendingAction.NONE
                        && exception instanceof FacebookAuthorizationException) {
                    pendingAction = PendingAction.NONE;
                }
            }

        });

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, shareCallback);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
            }
        };

        GlobalClass global = new GlobalClass(LogInActivity.this);
        setContent();
        if (GlobalClass.isInternetOn(LogInActivity.this)) {
            setAdMob();
        } else {
            Toast.makeText(LogInActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdMob() {
        mAdView = (AdView)findViewById(R.id.ads);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
    private void setContent() {
        GlobalClass global = new GlobalClass(this);
        device_id = GlobalClass.getPrefrenceString(this, "device_id", "");
        if (GlobalClass.isInternetOn(LogInActivity.this)) {
            if (GlobalClass.getPrefrenceBoolean(LogInActivity.this, "isLogin", false) == true) {
//                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                Intent intent=new Intent(LogInActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
            showToast();
        }

        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        lv_fblogin = (LinearLayout) findViewById(R.id.lv_fblogin);
        tv_forgotPass = (TextView) findViewById(R.id.tv_forgotPass);
        tv_SignUp = (TextView) findViewById(R.id.tv_SignUp);
        fbLogin = (TextView) findViewById(R.id.fbLogin);
        textView2 = (TextView) findViewById(R.id.textView2);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        btn_login.setOnClickListener(this);
        lv_fblogin.setOnClickListener(this);
        tv_SignUp.setOnClickListener(this);
        tv_forgotPass.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        edt_username.setTypeface(fontRegular);
        edt_password.setTypeface(fontRegular);
        tv_forgotPass.setTypeface(fontRegular);
        textView2.setTypeface(fontRegular);
        tv_SignUp.setTypeface(fontRegular);
        btn_login.setTypeface(fontRegular);
        fbLogin.setTypeface(fontRegular);

        edt_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_DONE){
                    Log.e("------------------",":::::::::"+edt_password.getText().toString());
                    if (GlobalClass.isInternetOn(LogInActivity.this)) {
                        if (validateFieldsLogIn()) {
                            checkLogin();
                        }
                    } else {
                        showToast();
                    }
                }
                return false;
            }
        });
    }

    private boolean validateFieldsLogIn() {
        if (!Validator.checkEmpty(edt_username)) {
            Toast.makeText(LogInActivity.this,  getResources().getString(R.string.enterUsername), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Validator.checkEmpty(edt_password)) {
            Toast.makeText(LogInActivity.this,  getResources().getString(R.string.enterPassword), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showToast() {
        Toast.makeText(LogInActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
    }

    public void checkLogin() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("Please wait..")
                .show();
        mUsername = edt_username.getText().toString();
        mPassword = edt_password.getText().toString();

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);

        callWebServices.checkLogin(mUsername, mPassword, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse);

//                    Log.e("-----------------------", "" + mainResponseObject.toString(4));
                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        JSONObject responseJsonObject = new JSONObject(mainResponseObject.getString(AppConfig.RESULT));
                        Log.e("SuccessData", "JsonObject---------------- " + responseJsonObject);
                        String username = responseJsonObject.getString("username");
                        String user_id = responseJsonObject.getString("user_id");
                        String user_image = AppConfig.IMAGE_URL + responseJsonObject.getString("user_image");
                        Log.e("user_image", "---------------- " + user_image);

                        GlobalClass.setPrefrenceBoolean(LogInActivity.this, "isLogin", true);
                        GlobalClass.setPrefrenceString(LogInActivity.this, "user_id", user_id);
                        GlobalClass.setPrefrenceString(LogInActivity.this, "user_current_password", mPassword);
                        GlobalClass.setPrefrenceString(LogInActivity.this, "username", username);
                        GlobalClass.setPrefrenceString(LogInActivity.this, "user_image", user_image);

                        Intent intent=new Intent(LogInActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        GlobalClass.setPrefrenceBoolean(LogInActivity.this, "isLogin", false);
                        Toast.makeText(LogInActivity.this,  mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                    GlobalClass.fbLogIn = false;

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
                Toast.makeText(LogInActivity.this, "Please Try Again Later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
        }
    }

    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();

        if (enableButtons && profile != null) {
            String stringImage = profile.getProfilePictureUri(420, 420).toString();
            Log.e("update UI------------", "" + profile.getFirstName() + "  " + profile.getLastName() + "   " + stringImage + "  " + profile.getId());
            fb_userName = profile.getFirstName();
            fb_id = profile.getId();
            fb_profilePic = stringImage;

            fbLogin();
        } else {
            Log.e("OOOOHhhhhhh", "cancel!!!!!");
        }
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {
            Log.e("fb...data", "" + object.toString());
            String id = object.getString("id");
            URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=400&height=400");
                Log.e("profile_pic", profile_pic + "");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            fb_id = object.getString("id");
            fb_userName = object.getString("name");
            fb_profilePic = "" + profile_pic;
            if (object.has("name"))
                bundle.putString("name", object.getString("name"));
            if (object.has("first_name") && object.has("last_name")) {
                fb_name = object.getString("first_name") + " " + object.getString("last_name");
            } else {
                fb_name = " ";
            }
            if (object.has("birthday")) {
                fb_birthDate = object.getString("birthday");
            } else {
                fb_birthDate = "";
            }
            if (object.has("email")) {
                fb_email = object.getString("email");
            } else {
                fb_email = "";
            }
            if (object.has("gender")) {
                fb_user_gender = object.getString("gender");
            } else {
                fb_user_gender = "female";
            }
            if (fb_user_gender.equalsIgnoreCase("Male")) {
                fb_user_gender = "M";
            } else if (fb_user_gender.equalsIgnoreCase("Female")) {
                fb_user_gender = "F";
            }

            fbLogin();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    private void fbLogin() {
        Log.e("---name-----------", "" + fb_userName);
        Log.e("--------fb_email------", "" + fb_email);
        Log.e("-----fb_id-------", "" + fb_id);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);
        callWebServices.checkFBregister(fb_userName, fb_email, fb_user_gender, device_id, "android", "", "", fb_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse);

                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        JSONObject responseJsonObject = new JSONObject(mainResponseObject.getString(AppConfig.RESULT));
                        Log.e("SuccessData", "JsonObject---------------- " + responseJsonObject);
                        String username = responseJsonObject.getString("username");
                        String user_image = responseJsonObject.getString("user_image");
                        String user_id = responseJsonObject.getString("user_id");
                        GlobalClass global = new GlobalClass(LogInActivity.this);
                        GlobalClass.setPrefrenceBoolean(LogInActivity.this, "isLogin", true);

                        GlobalClass.setPrefrenceString(LogInActivity.this, "username", username);
                        GlobalClass.setPrefrenceString(LogInActivity.this, "user_image", fb_profilePic);
                        Log.e("user_id", "----fb------------ " + user_id);
                        GlobalClass.setPrefrenceBoolean(LogInActivity.this, "isLogin", true);
                        GlobalClass.setPrefrenceString(LogInActivity.this, "user_id", user_id);
                        finish();
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        GlobalClass.fbLogIn = true;
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        GlobalClass.fbLogIn = false;
                        Toast.makeText(LogInActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();

                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("RetrofitError", " ---- :  " + error);
                Toast.makeText(LogInActivity.this, R.string.try_after_some_time, Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LogInActivity.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_login:
                if (GlobalClass.isInternetOn(LogInActivity.this)) {
                    if (validateFieldsLogIn()) {
                        checkLogin();
                    }
                } else {
                    showToast();
                }

                break;

            case R.id.lv_fblogin:
                if (GlobalClass.isInternetOn(LogInActivity.this)) {
                    LoginManager.getInstance().logInWithPublishPermissions(LogInActivity.this, Arrays.asList(PERMISSION));

                } else {
                    showToast();
                }
                break;
            case R.id.tv_SignUp:
                if (GlobalClass.isInternetOn(LogInActivity.this)) {
                    startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
                    finish();
                } else {
                    showToast();
                }

                break;
            case R.id.tv_forgotPass:
                if (GlobalClass.isInternetOn(LogInActivity.this)) {
                    startActivity(new Intent(LogInActivity.this, ForgotPasswordActivity.class));
                } else {
                    showToast();
                }

                break;
            case R.id.iv_back:
                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                finish();
                break;
        }

    }
}
