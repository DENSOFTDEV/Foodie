package com.iblinfotech.foodierecipe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iblinfotech.foodierecipe.model.RecipeIngredientsData;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class GlobalClass {
    static Context context;
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;
    public static int DeviceWidth, DeviceHeight;
    public static boolean fbLogIn = false;
    public static boolean key_isAdLock;

    public static int is_purchase = 0;


    public GlobalClass(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        preferences = this.context.getSharedPreferences("foodierecipe", 0);
        editor = preferences.edit();

        //count device height & width

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        DeviceWidth = size.x;
        DeviceHeight = size.y;
    }

    public static void clearData() {
        editor.clear();
        editor.commit();
    }

    public static String getPrefrenceString(Context context, String key, String defValue) {
        String value = preferences.getString(key, defValue);
        return value;
    }

    public static int getPrefrenceInt(Context context, String key, int defValue) {
        int value = preferences.getInt(key, defValue);
        return value;
    }

    public static boolean getPrefrenceBoolean(Context context, String key, boolean defValue) {
        boolean value = preferences.getBoolean(key, defValue);
        return value;
    }

    public static float getPrefrenceFloat(Context context, String key, float defValue) {
        float value = preferences.getFloat(key, defValue);
        return value;
    }

    public static long getPrefrenceLong(Context context, String key, long defValue) {
        long value = preferences.getLong(key, defValue);
        return value;
    }

    public static void setPrefrenceString(Context context, String key, String value) {
        editor.putString(key, value);
        editor.commit();

    }

    public static void setPrefrenceInt(Context context, String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setPrefrenceBoolean(Context context, String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setPrefrenceFloat(Context context, String key, float value) {
        editor.putFloat(key, value);
        editor.commit();

    }

    public static void setPrefrenceLong(Context context, String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public static void errorLog(Context context, String tag, String value) {
        Log.e("" + context.getClass().getName() + ":" + tag, value);
    }

    public static void printLog(String tag, String strMessage) {
        Log.e(tag, "-----------------------------------------" + strMessage);
    }

    public static void showToast(Context context, String strMessage) {
        Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
    }

    public static void saveArray(Context context, HashMap<Object, ArrayList<RecipeIngredientsData>> arrayListHashMap) {

        Log.e("storeShoppingList", "=====" + arrayListHashMap.size());
        SharedPreferences sharedpreferences = context.getSharedPreferences("ShoppingList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayListHashMap);
        editor.putString("shoppingList", json);
        editor.apply();
    }

    public static HashMap<Object, ArrayList<RecipeIngredientsData>> getShoppingList(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("ShoppingList", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("shoppingList", "");
        Type type = new TypeToken<HashMap<Object, ArrayList<RecipeIngredientsData>>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static String getImageEncoded(ImageView imageView) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageprofile = stream.toByteArray();
        return Base64.encodeToString(imageprofile, 0);
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

//    public static void gotoLogin(Context activity) {
//        activity.startActivity(new Intent(activity,LoginActivity.class));
//    }

}
