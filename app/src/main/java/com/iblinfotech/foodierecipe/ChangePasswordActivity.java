package com.iblinfotech.foodierecipe;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iblinfotech.foodierecipe.utils.AlertDialogParser;
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

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_password_submit;
    private EditText edt_confirm_password, edt_new_password, edt_current_password;
    private ImageView iv_back;
    private TextView textView2;
    private String user_current_password, entered_new_password, user_id;
    private KProgressHUD kProgressHUD;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
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

        setContentView(R.layout.activity_change_password);
        setContent();

        GlobalClass global = new GlobalClass(ChangePasswordActivity.this);
        user_current_password = GlobalClass.getPrefrenceString(this, "user_current_password", "").trim();
        Log.e("--------", "user_current_password: " + user_current_password);
        user_id = GlobalClass.getPrefrenceString(ChangePasswordActivity.this, "user_id", "");
    }

    private void setContent() {
        btn_password_submit = (Button) findViewById(R.id.btn_password_submit);
        edt_current_password = (EditText) findViewById(R.id.edt_current_password);
        textView2 = (TextView) findViewById(R.id.textView2);
        edt_new_password = (EditText) findViewById(R.id.edt_new_password);
        edt_confirm_password = (EditText) findViewById(R.id.edt_confirm_password);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_back.setOnClickListener(this);
        btn_password_submit.setOnClickListener(this);

        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        textView2.setTypeface(fontRegular);
        btn_password_submit.setTypeface(fontRegular);

        Typeface fontRobotoLight = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");
        edt_current_password.setTypeface(fontRobotoLight);
        edt_new_password.setTypeface(fontRobotoLight);
        edt_confirm_password.setTypeface(fontRobotoLight);

        edt_confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (GlobalClass.isInternetOn(ChangePasswordActivity.this)) {
                        if (validateFieldsPassword()) {
                            newPasswordChange();
                        }
                    } else {
                        GlobalClass.showToast(ChangePasswordActivity.this, getResources().getString(R.string.no_internet_connection));
                    }
                }
                return false;
            }
        });
    }

    private boolean validateFieldsPassword() {
        if (GlobalClass.fbLogIn) {
            GlobalClass.showToast(ChangePasswordActivity.this, getResources().getString(R.string.fbUserNotAllowed));
            return false;
        }
        if (!Validator.checkEmpty(edt_current_password)) {
            GlobalClass.showToast(ChangePasswordActivity.this, getResources().getString(R.string.entryNull));
            return false;
        }
        if (!Validator.checkEmpty(edt_new_password)) {
            GlobalClass.showToast(ChangePasswordActivity.this, getResources().getString(R.string.entrynewPass));
            return false;
        }
        if (!Validator.checkEmpty(edt_confirm_password)) {
            GlobalClass.showToast(ChangePasswordActivity.this, getResources().getString(R.string.entryconfirmPass));
            return false;
        }
        if (!edt_new_password.getText().toString().trim().equalsIgnoreCase(edt_confirm_password.getText().toString())) {
            GlobalClass.showToast(ChangePasswordActivity.this, getResources().getString(R.string.paswordDifferent));
            return false;
        }
        return true;
    }

    private void newPasswordChange() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("Please wait..")
                .show();
        entered_new_password = edt_confirm_password.getText().toString().trim();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);

        callWebServices.ChangePassword(user_id, user_current_password, entered_new_password, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse);
                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        edt_current_password.setText("");
                        edt_new_password.setText("");
                        edt_confirm_password.setText("");
                        startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                        finish();
                    }
                    GlobalClass.showToast(ChangePasswordActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));
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
                GlobalClass.showToast(ChangePasswordActivity.this, getResources().getString(R.string.try_after_some_time));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_password_submit:
                if (GlobalClass.isInternetOn(ChangePasswordActivity.this)) {
                    if (validateFieldsPassword()) {
                        newPasswordChange();
                    }
                } else {
                    GlobalClass.showToast(ChangePasswordActivity.this, getResources().getString(R.string.no_internet_connection));
                }
                break;
        }
    }
}
