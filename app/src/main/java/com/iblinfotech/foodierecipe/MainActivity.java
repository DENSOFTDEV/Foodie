package com.iblinfotech.foodierecipe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.crash.FirebaseCrash;
import com.iblinfotech.foodierecipe.adapter.MenuListAdapter;
import com.iblinfotech.foodierecipe.fragments.MyFavoriteFragment;
import com.iblinfotech.foodierecipe.fragments.MyHomeFragment;
import com.iblinfotech.foodierecipe.fragments.MyShoppingList;
import com.iblinfotech.foodierecipe.fragments.MyWeeklyMenuFragment;
import com.iblinfotech.foodierecipe.subscriptionmenu.PackageActivity;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class MainActivity extends ActivityManagePermission implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView iv_navigationDrawer, iv_search;
    private String[] array_menu;
    private ListView lv_menu;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private int currentPosition = 0;
    private MenuListAdapter menuListAdapter;
    private CircleImageView iv_profile_image;
    public static int mSelectedItem;
    private TextView tv_username, tv_shopping_list, tv_stor;
    private RelativeLayout rl_title;
    private CoordinatorLayout cd_mainBackGround;
    private String device_id;
    private View view_weekly_backgroundColor;
    public static InterstitialAd interstitial;
    public static AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseCrash.log("Activity created");
        FirebaseCrash.logcat(Log.ERROR, "tag", "Message");

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        GlobalClass global = new GlobalClass(MainActivity.this);

        askPermission();
        setContent();

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.interstitial_full_screen));
        adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void askPermission() {

        askCompactPermissions(new String[]{PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE, PermissionUtils.Manifest_READ_EXTERNAL_STORAGE}, new PermissionResult() {
            @Override
            public void permissionGranted() {
            }

            @Override
            public void permissionDenied() {
            }
        });
    }

    private void setContent() {

        iv_navigationDrawer = (ImageView) findViewById(R.id.iv_navigationDrawer);
        lv_menu = (ListView) findViewById(R.id.lv_menu);
        iv_profile_image = (CircleImageView) findViewById(R.id.iv_profile_image);
        tv_username = (TextView) findViewById(R.id.tv_username);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        cd_mainBackGround = (CoordinatorLayout) findViewById(R.id.cd_mainBackGround);
        tv_shopping_list = (TextView) findViewById(R.id.tv_shopping_list);
        tv_stor = (TextView) findViewById(R.id.tv_stor);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        view_weekly_backgroundColor = (View) findViewById(R.id.view_weekly_backgroundColor);
        mSelectedItem = 0;
        view_weekly_backgroundColor.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("----------", "device_id:---------------------------" + device_id);
        GlobalClass.setPrefrenceString(MainActivity.this, "device_id", device_id);
        iv_navigationDrawer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HidesoftKeyboard();
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SearchActivity.class));

            }
        });

        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        tv_shopping_list.setTypeface(fontRegular);

        array_menu = new String[]{"HOME", "WEEKLY MENU", "MY FAVOURITE", "SHOPPING LIST", "STORE", "CHANGE PASSWORD", "LOGIN", "LOGOUT"};
        menuListAdapter = new MenuListAdapter(MainActivity.this, array_menu, currentPosition);
        lv_menu.setAdapter(menuListAdapter);
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mSelectedItem = position;
                setViewWithPostionSelected(position);
                menuListAdapter.notifyDataSetChanged();
            }
        });
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.maincontainer, new MyHomeFragment()).commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (GlobalClass.getPrefrenceBoolean(MainActivity.this, "isLogin", false) == true) {
            String user_image = GlobalClass.getPrefrenceString(MainActivity.this, "user_image", "");
            String username = GlobalClass.getPrefrenceString(MainActivity.this, "username", "");
            if (user_image.length() != 0) {
                Picasso.with(MainActivity.this).load(user_image).placeholder(R.drawable.ic_launcher).into(iv_profile_image);
            }
            tv_username.setText(username);
        } else {
            GlobalClass.setPrefrenceString(MainActivity.this, "user_id", device_id);
            tv_username.setText(R.string.welcome);
        }
    }

    private void setViewWithPostionSelected(int position) {
        currentPosition = position;
        switch (position) {
            //HOME
            case 0:
                HidesoftKeyboard();
                view_weekly_backgroundColor.setVisibility(View.GONE);
                view_weekly_backgroundColor.setVisibility(View.GONE);
                iv_search.setVisibility(View.GONE);
                cd_mainBackGround.setBackgroundResource(R.drawable.bg_cooking_home);
                tv_shopping_list.setVisibility(View.GONE);
                iv_navigationDrawer.setImageResource(R.drawable.ic_menu);

                FragmentTransaction fragmentHome = fragmentManager.beginTransaction();
                fragmentHome.replace(R.id.maincontainer, new MyHomeFragment()).commit();
                drawer.closeDrawer(Gravity.LEFT);
                break;
            //WEEKLY MENU
            case 1:
                HidesoftKeyboard();
                view_weekly_backgroundColor.setVisibility(View.VISIBLE);
                iv_search.setVisibility(View.VISIBLE);
                iv_search.setImageResource(R.drawable.ic_search_white);
                iv_navigationDrawer.setImageResource(R.drawable.ic_menu_menu);
                tv_shopping_list.setVisibility(View.VISIBLE);
                tv_shopping_list.setTextColor(Color.WHITE);
                tv_shopping_list.setText("WEEKLY MENU");
                cd_mainBackGround.setBackgroundResource(R.drawable.bg_plain);

                FragmentTransaction fragmentMyWeekly = fragmentManager.beginTransaction();
                fragmentMyWeekly.replace(R.id.maincontainer, new MyWeeklyMenuFragment()).commit();
                drawer.closeDrawer(Gravity.LEFT);
                break;
            //MY FAV.
            case 2:
                HidesoftKeyboard();
                view_weekly_backgroundColor.setVisibility(View.GONE);
                iv_search.setVisibility(View.VISIBLE);
                iv_search.setImageResource(R.drawable.ic_search);
                iv_navigationDrawer.setImageResource(R.drawable.ic_menu);
                tv_shopping_list.setVisibility(View.VISIBLE);
                tv_shopping_list.setTextColor(getResources().getColor(R.color.colorAppName_cd4a2c));
                tv_shopping_list.setText("MY FAVOURITE");
                cd_mainBackGround.setBackgroundResource(R.drawable.bg_plain);

                FragmentTransaction fragmentMyRecipes = fragmentManager.beginTransaction();
                fragmentMyRecipes.replace(R.id.maincontainer, new MyFavoriteFragment()).commit();
                drawer.closeDrawer(Gravity.LEFT);
                break;
            //SHOPPING LIST
            case 3:
                HidesoftKeyboard();
                view_weekly_backgroundColor.setVisibility(View.GONE);
                iv_search.setVisibility(View.GONE);
                tv_shopping_list.setVisibility(View.VISIBLE);
                iv_navigationDrawer.setImageResource(R.drawable.ic_menu);
                tv_shopping_list.setTextColor(getResources().getColor(R.color.colorAppName_cd4a2c));
                tv_shopping_list.setText("SHOPPING LIST");
                cd_mainBackGround.setBackgroundResource(R.drawable.bg_plain);

                FragmentTransaction fragmentShopping = fragmentManager.beginTransaction();
                fragmentShopping.replace(R.id.maincontainer, new MyShoppingList()).commit();
                drawer.closeDrawer(Gravity.LEFT);
                break;
            //STORE
            case 4:
                HidesoftKeyboard();
                view_weekly_backgroundColor.setVisibility(View.GONE);
                iv_search.setVisibility(View.GONE);
                iv_navigationDrawer.setImageResource(R.drawable.ic_menu);
                tv_stor.setTextColor(getResources().getColor(R.color.colorAppName_cd4a2c));
                tv_stor.setText("STORE");
                cd_mainBackGround.setBackgroundResource(R.drawable.bg_plain);
                Intent intent =  new Intent(MainActivity.this,PackageActivity.class);
                startActivity(intent);
                drawer.closeDrawer(Gravity.LEFT);
                finish();
                break;

            //CHANGE PASSWORD
            case 5:
                HidesoftKeyboard();
                drawer.closeDrawer(Gravity.LEFT);
                if (GlobalClass.fbLogIn) {
                    GlobalClass.showToast(MainActivity.this, getResources().getString(R.string.fb_users_cant_change_password));
                } else {
                    startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                }
                break;
            //LOGIN
            case 6:
                HidesoftKeyboard();
                drawer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                finish();

                break;
            //LOGOUT
            case 7:
                HidesoftKeyboard();
                drawer.closeDrawer(Gravity.LEFT);
                askToLogout();
                break;
        }
    }

    private void HidesoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private void askToLogout() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.are_u_sure_want_to_logout)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                        GlobalClass.setPrefrenceBoolean(MainActivity.this, "isLogin", false);
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}