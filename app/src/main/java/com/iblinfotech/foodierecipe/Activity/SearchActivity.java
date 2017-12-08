package com.iblinfotech.foodierecipe.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.adapter.SearchedIngredientAdapter;
import com.iblinfotech.foodierecipe.adapter.SearchedItemAdapter;
import com.iblinfotech.foodierecipe.model.SearchResultData;
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

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_search;
    private ImageView iv_search, iv_cancel_search, iv_back,iv_nodataFound;
    private GridView grid_searched_ingredient;
    private KProgressHUD kProgressHUD;
    private SearchResultData searchResultData;
    private RecyclerView rec_serched_recipe;
    private TextView tv_ingredients, tv_recipes, tv_search_title,tv_found,tv_noDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        setContentView(R.layout.activity_search);


        setContent();
        edt_search.requestFocus();
    }

    private void setContent() {
        edt_search = (EditText) findViewById(R.id.edt_search);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_cancel_search = (ImageView) findViewById(R.id.iv_cancel_search);
        iv_nodataFound = (ImageView) findViewById(R.id.iv_nodataFound);
        tv_ingredients = (TextView) findViewById(R.id.tv_ingredients);
        tv_noDataFound = (TextView) findViewById(R.id.tv_noDataFound);
        tv_found = (TextView) findViewById(R.id.tv_found);
        tv_recipes = (TextView) findViewById(R.id.tv_recipes);
        tv_search_title = (TextView) findViewById(R.id.tv_search_title);
        grid_searched_ingredient = (GridView) findViewById(R.id.grid_searched_ingredient);
        rec_serched_recipe = (RecyclerView) findViewById(R.id.rec_serched_recipe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rec_serched_recipe.setLayoutManager(layoutManager);

        Typeface fontRobotoBold = Typeface.createFromAsset(this.getAssets(), "Roboto-Bold.ttf");
        tv_ingredients.setTypeface(fontRobotoBold);
        tv_recipes.setTypeface(fontRobotoBold);
        Typeface fontRegular = Typeface.createFromAsset(this.getAssets(), "PlayfairDisplay-Regular.otf");
        tv_search_title.setTypeface(fontRegular);
        Typeface fontRobotoLight = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");
//        edt_search.setSelected(false);
        edt_search.setTypeface(fontRobotoLight);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_search.getText().toString().length() >= 3) {
                    if (GlobalClass.isInternetOn(SearchActivity.this)) {
                            getSearch(edt_search.getText().toString().trim());
                    } else {
                        Toast.makeText(SearchActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    tv_ingredients.setVisibility(View.GONE);
                    tv_recipes.setVisibility(View.GONE);
                    rec_serched_recipe.setAdapter(null);
                    grid_searched_ingredient.setAdapter(null);
                }
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_DONE){
                    Log.e("------------------",":::::::::"+edt_search.getText().toString());
                    getSearch(edt_search.getText().toString().trim());
                }
                return false;
            }
        });
        iv_cancel_search.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_back.setOnClickListener(this);
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

    private void getSearch(String searchValue) {
        String user_id;
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);

        GlobalClass global = new GlobalClass(SearchActivity.this);
        if (GlobalClass.getPrefrenceBoolean(SearchActivity.this, "isLogin", false) == true) {
            user_id = GlobalClass.getPrefrenceString(SearchActivity.this, "user_id", "");
        } else {
            user_id = "";
            Log.e("----user_id---------", "--- " + user_id);
        }
        callWebServices.getSearch(searchValue, user_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse.toString());
//                    Log.e("-----------------------", "" + mainResponseObject.toString(4));
                    if (mainResponseObject.getInt(AppConfig.RESPONSE_CODE) == 1) {
                        JSONObject jsonObject = mainResponseObject.getJSONObject(AppConfig.RESULT);
                        Log.e("SuccessData", "searched------------jsonObject------------ " + jsonObject);
                        searchResultData = LoganSquare.parse(jsonObject.toString(), SearchResultData.class);
                        Log.e("---------------: ", ":: " + searchResultData.getRecipe().get(0).getRecipe_name());
                        setData();
                        iv_nodataFound.setVisibility(View.GONE);
                        tv_noDataFound.setVisibility(View.GONE);
                        tv_found.setVisibility(View.GONE);
                        Log.e("1111111", "success: "+searchResultData );
                    } else {
                        tv_recipes.setVisibility(View.GONE);
                        rec_serched_recipe.setAdapter(null);
//                        setData();
//                        AlertDialogParser.showMessageDialog(SearchActivity.this, mainResponseObject.getString(AppConfig.RESPONSE_MESSAGE));
                        iv_nodataFound.setVisibility(View.VISIBLE);
                        tv_noDataFound.setVisibility(View.VISIBLE);
                        tv_found.setVisibility(View.VISIBLE);
//                        Log.e("---------------: ", ":: " + searchResultData.getRecipe().get(0).getRecipe_name());

                        Log.e("2222222", "success: "+searchResultData );
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("RetrofitError", " ---- :  " + error);
//                AlertDialogParser.showMessageDialog(SearchActivity.this, "" + error);
                Toast.makeText(SearchActivity.this, "Please,Try After Some Time!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setData() {
        if (searchResultData.getRecipe().size() > 0) {
            tv_recipes.setVisibility(View.VISIBLE);
            SearchedItemAdapter searchedItemAdapter = new SearchedItemAdapter(SearchActivity.this, searchResultData.getRecipe());
            rec_serched_recipe.setAdapter(searchedItemAdapter);
        } else {
            tv_ingredients.setVisibility(View.GONE);
            tv_recipes.setVisibility(View.GONE);
            rec_serched_recipe.setAdapter(null);
        }
        if (searchResultData.getIngredients().size() > 0) {
            tv_ingredients.setVisibility(View.VISIBLE);
            SearchedIngredientAdapter searchedIngredientAdapter = new SearchedIngredientAdapter(SearchActivity.this, searchResultData.getIngredients());
            grid_searched_ingredient.setAdapter(searchedIngredientAdapter);
        } else {
            tv_ingredients.setVisibility(View.GONE);
            grid_searched_ingredient.setAdapter(null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back: {
                finish();
            }
            break;
            case R.id.iv_cancel_search: {
                edt_search.setText("");
            }
            break;
            case R.id.iv_search: {
                if (GlobalClass.isInternetOn(SearchActivity.this)) {
                    if (edt_search.getText().toString().trim().length() > 0) {
                        tv_ingredients.setVisibility(View.GONE);
                        tv_recipes.setVisibility(View.GONE);
                        getSearch(edt_search.getText().toString().trim());
                    } else {
                        Toast.makeText(SearchActivity.this, "Please add something", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SearchActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }

            }
            break;


        }

    }
}
