package com.iblinfotech.foodierecipe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class RegisterActivity extends ActivityManagePermission implements View.OnClickListener {
    private EditText edt_username, edt_emailAdress, edt_password;
    public RadioButton selectedGender;
    private RadioGroup rg_gender;
    private Button btn_signUp;
    private KProgressHUD kProgressHUD;
    private TextView tv_SignUp;
    private String mUsername, mPassword, mEmail;
    private TextView tv_username, tv_emailAddress, tv_password, tv_gender, tv_createAccount;
    private RadioButton rb_male, rb_female;
    private String device_id;
    private boolean isCamera, isGallery;
    private File mFileTemp;
    public static String TEMP_PHOTO_FILE_NAME = "temp.jpg";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Bitmap mbitmap;
    private ImageView iv_dialog_pic,iv_user_image;

    private String selectedImagePath;
    private Button btn_select;
    private ImageView iv_back;
    private boolean skiped = false;
    private TypedFile typefile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        setContentView(R.layout.activity_register);
        setContent();

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e("------", "in if------------");
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            Log.e("------", "in else------------");
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
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

        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_emailAdress = (EditText) findViewById(R.id.edt_emailAdress);
        edt_password = (EditText) findViewById(R.id.edt_password);
        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        btn_signUp = (Button) findViewById(R.id.btn_signUp);
        tv_SignUp = (TextView) findViewById(R.id.tv_SignUp);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_emailAddress = (TextView) findViewById(R.id.tv_emailAddress);
        tv_password = (TextView) findViewById(R.id.tv_password);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_createAccount = (TextView) findViewById(R.id.tv_createAccount);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_user_image = (ImageView) findViewById(R.id.iv_user_image);

        btn_signUp.setOnClickListener(this);
        tv_SignUp.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_user_image.setOnClickListener(this);


        //Added Here
        Typeface fontBold = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Bold.otf");

        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        edt_username.setTypeface(fontRegular);
        edt_password.setTypeface(fontRegular);
        edt_emailAdress.setTypeface(fontRegular);
        tv_gender.setTypeface(fontRegular);
        tv_username.setTypeface(fontRegular);
        tv_emailAddress.setTypeface(fontRegular);
        tv_password.setTypeface(fontRegular);
        rb_male.setTypeface(fontRegular);
        rb_female.setTypeface(fontRegular);
        btn_signUp.setTypeface(fontRegular);
        tv_createAccount.setTypeface(fontRegular);
        tv_SignUp.setTypeface(fontRegular);
        GlobalClass global = new GlobalClass(this);
        device_id = GlobalClass.getPrefrenceString(this, "device_id", "");
    }

    private boolean validateFieldsRegister() {
        if (!Validator.checkEmpty(edt_username)) {
            AlertDialogParser.showMessageDialog(this, getResources().getString(R.string.enterUsername));
            return false;
        }
        if (!Validator.checkLimit(edt_username)) {
            AlertDialogParser.showMessageDialog(this, getResources().getString(R.string.limitUsername));
//            AlertDialogParser.showMessageDialog(this, "Username should contains between 6 to 15 characters");
            return false;
        }
        if (!Validator.checkAlphaNumeric(edt_username)) {
            AlertDialogParser.showMessageDialog(this, getResources().getString(R.string.validUsername));
//            AlertDialogParser.showMessageDialog(this, "Username should contains only alphanumeric characters.");
            return false;
        }
        if (!Validator.checkEmpty(edt_emailAdress)) {
            AlertDialogParser.showMessageDialog(this, getResources().getString(R.string.enterEmail));
            return false;
        }
        if (!Validator.checkEmail(edt_emailAdress)) {
//            AlertDialogParser.showMessageDialog(this, getResources().getString(R.string.validEmail));
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.validEmail), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Validator.checkEmpty(edt_password)) {
//            AlertDialogParser.showMessageDialog(this, getResources().getString(R.string.enterPassword));
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.enterPassword), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Validator.checkPasswordLength(edt_password)) {
//            AlertDialogParser.showMessageDialog(this, getResources().getString(R.string.limitPassword));
            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.limitPassword), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    String mGender;

    private void getGender() {
        int selected = rg_gender.getCheckedRadioButtonId();
        selectedGender = (RadioButton) findViewById(selected);
        if (selectedGender.getText().toString().equalsIgnoreCase("Male")) {
            mGender = "M";
        } else if (selectedGender.getText().toString().equalsIgnoreCase("Female")) {
            mGender = "F";
        }
    }

    private void getRegister() {
        try{
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setLabel("Please wait..")
                .show();}
        catch (Exception e){

        }
        mUsername = edt_username.getText().toString();
        mEmail = edt_emailAdress.getText().toString();
        mPassword = edt_password.getText().toString();

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);
        Log.e("===", "mUsername---------------- " + mUsername);
        Log.e("===", "selectedImagePath---------------- " + selectedImagePath);
        if (selectedImagePath != null) {
            try {
                Bitmap bmp = BitmapFactory.decodeFile(selectedImagePath);/* Create Bitmap object for the original image*/
//            Crate new converted image file object File
                File convertedImage = new File(Environment.getExternalStorageDirectory() + "/convertedimg.png"); /* Create FileOutputStream object to write data to the converted image file*/
                FileOutputStream outStream = new FileOutputStream(convertedImage);
                boolean success = bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);

                Log.e("==####=", "convertedImage---------------- " + convertedImage);
                Log.e("==####=", "success---------------- " + success);
                typefile = new TypedFile("image/png", convertedImage);
                outStream.flush();
                outStream.close();
            } catch (IOException e) { /* TODO Auto-generated catch block*/
                e.printStackTrace();
            }
        } else {
            typefile = null;
        }

        callWebServices.registerNewUser(mUsername, mEmail, mGender, device_id, "android", typefile, mPassword, "", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    Log.e("++00000++++++++++++++", "-----mUsername----------- "+mUsername);
                    Log.e("+++0000000+++++++++++++", "-----mPassword----------- "+mPassword);
                    kProgressHUD.dismiss();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    Log.e("-----------------------", "" + stringResponse);

                    JSONObject mainResponseObject = new JSONObject(stringResponse);
//                    Log.e("-----------------------", "" + mainResponseObject.toString(4));
                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        JSONObject responseJsonObject = new JSONObject(mainResponseObject.getString(AppConfig.RESULT));
                        Log.e("SuccessData-----", "registerUser---------------- " + responseJsonObject);
//                        String userfile = responseJsonObject.getString("userfile");

//                        AlertDialogParser.showMessageDialog(RegisterActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));

                        Log.e("+++11111+++++++++++++", "-----mUsername----------- "+mUsername);
                        Log.e("+++1111+++++++++++++", "-----mPassword----------- "+mPassword);
                        checkLogin(mUsername, mPassword);
//                        finish();
                        Toast.makeText(RegisterActivity.this, "Register Succesfully!", Toast.LENGTH_SHORT).show();
                    } else {
//                        AlertDialogParser.showMessageDialog(RegisterActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));
                        Toast.makeText(RegisterActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
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
//                AlertDialogParser.showMessageDialog(RegisterActivity.this, "" + error);
                Toast.makeText(RegisterActivity.this, "Please Try Again Later!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void checkLogin(String mUsername, String mPassword) {
//        kProgressHUD = KProgressHUD.create(this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setDimAmount(0.5f)
//                .setLabel("Please wait..")
//                .show();
        Log.e("++++++++++++++++", "-----mUsername----------- "+mUsername);
        Log.e("++++++++++++++++", "-----mPassword----------- "+mPassword);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);

        callWebServices.checkLogin(mUsername,mPassword, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    Log.e("++++++++++++++++", "-----Checklogin----------- ");

//                    kProgressHUD.dismiss();
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

                        GlobalClass.setPrefrenceBoolean(RegisterActivity.this, "isLogin", true);
                        GlobalClass.setPrefrenceString(RegisterActivity.this, "user_id", user_id);
                        GlobalClass.setPrefrenceString(RegisterActivity.this, "username", username);
                        GlobalClass.setPrefrenceString(RegisterActivity.this, "user_image", user_image);
                        GlobalClass.setPrefrenceString(RegisterActivity.this, "user_current_password", RegisterActivity.this.mPassword);
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        GlobalClass.setPrefrenceBoolean(RegisterActivity.this, "isLogin", false);
//                        AlertDialogParser.showMessageDialog(RegisterActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));
                        Toast.makeText(RegisterActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();


                    }
                    GlobalClass.fbLogIn = false;

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
//                AlertDialogParser.showMessageDialog(RegisterActivity.this, "" + error);
                Toast.makeText(RegisterActivity.this, "Please Try Again Later!", Toast.LENGTH_SHORT).show();
//
            }
        });
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                selectedImagePath = destination.getPath();
                mbitmap = thumbnail;

                Log.e("image path camera", "--selectedImagePath-" + selectedImagePath);
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iv_user_image.setImageBitmap(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                selectedImagePath = cursor.getString(column_index);
                Log.e("image path", "---" + selectedImagePath);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                mbitmap = bm;
                iv_user_image.setImageBitmap(bm);
            }

//            if (selectedImagePath != null) {
//                btn_select.setEnabled(true);
//                btn_select.getBackground().setAlpha(255);
//            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
        finish();
        super.onBackPressed();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_signUp:
                Log.e("--------------", "Btn click----------------------");
                getGender();
                if (GlobalClass.isInternetOn(RegisterActivity.this)) {
                    if (validateFieldsRegister()) {
//                        OpenDialogChoosePic();
                        getRegister();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "No Internet connection!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_SignUp:
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
                break;
            case R.id.iv_back:
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
                break;
            case R.id.iv_user_image:
                if (GlobalClass.isInternetOn(RegisterActivity.this)) {
//                    if (validateFieldsRegister()) {
//                        OpenDialogChoosePic();
                    selectImage();
//                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "No Internet connection!", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

}
