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

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView2;
    private Button btn_submit;
    private EditText edt_enter_email;

    private String email;
    private KProgressHUD kProgressHUD;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);
        setContent();
    }

    private void setContent() {
        textView2 = (TextView) findViewById(R.id.textView2);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_enter_email = (EditText) findViewById(R.id.edt_enter_email);

        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        edt_enter_email.setTypeface(fontRegular);
        btn_submit.setTypeface(fontRegular);
        textView2.setTypeface(fontRegular);

        btn_submit.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        edt_enter_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_DONE){
                    if (GlobalClass.isInternetOn(ForgotPasswordActivity.this)) {
                        if (validateFieldsRegister()) {
                            forgotPassword();
                        }
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }

    private void forgotPassword() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("Please wait..")
                .show();
        email = edt_enter_email.getText().toString().trim();
        Log.e("---------------------", "email: " + email);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);

        callWebServices.forgotPassword(email, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse);
                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        edt_enter_email.setText("");
                        Toast.makeText(ForgotPasswordActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE),Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE),Toast.LENGTH_SHORT).show();

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
                Toast.makeText(ForgotPasswordActivity.this, R.string.try_after_some_time,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateFieldsRegister() {
        if (!Validator.checkEmpty(edt_enter_email)||!Validator.checkEmail(edt_enter_email)) {
            Toast.makeText(ForgotPasswordActivity.this,  getResources().getString(R.string.validEmail),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (GlobalClass.isInternetOn(ForgotPasswordActivity.this)) {
                    if (validateFieldsRegister()) {
                        forgotPassword();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.iv_back:
                finish();
                startActivity(new Intent(ForgotPasswordActivity.this, LogInActivity.class));
                break;
        }
    }

}
