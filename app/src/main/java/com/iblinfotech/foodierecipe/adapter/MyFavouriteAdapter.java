package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iblinfotech.foodierecipe.Activity.CategoryItemViewActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.fragments.MyFavoriteFragment;
import com.iblinfotech.foodierecipe.model.MyFavouriteData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.CallWebServices;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;

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

public class MyFavouriteAdapter extends RecyclerView.Adapter<MyFavouriteAdapter.ViewHolder> {
    private List<MyFavouriteData> myFavouriteDatas = new ArrayList<>();
    private Context context;
    private boolean isFav = true;
    MyFavoriteFragment myFavoriteFragment;

    public MyFavouriteAdapter(Context context, List<MyFavouriteData> myFavouriteDatas) {
        this.context = context;
        this.myFavouriteDatas = myFavouriteDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_favorite_list_item, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyFavouriteAdapter.ViewHolder holder, final int position) {
        holder.tv_fav_cat_name.setText(myFavouriteDatas.get(position).getCategory_name());
        holder.tv_fav_item_name.setText(myFavouriteDatas.get(position).getRecipe_name());
//        holder.tv_fav_item_name.setText(myFavouriteDatas.get(position).getCategory_color_code());
        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "PlayfairDisplay-Regular.otf");
        Typeface RobotoLight = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        holder.tv_fav_cat_name.setTypeface(fontRegular);
        holder.tv_fav_item_name.setTypeface(RobotoLight, 5);

        if (myFavouriteDatas.get(position).getCooking_time() != null && myFavouriteDatas.get(position).getCooking_time().length() > 0) {
            holder.tv_cook_time.setText(myFavouriteDatas.get(position).getCooking_time());
            holder.iv_cook_time.setVisibility(View.VISIBLE);
            holder.tv_cook_time.setVisibility(View.VISIBLE);
        }
        if (myFavouriteDatas.get(position).getPreparation_time() != null && myFavouriteDatas.get(position).getPreparation_time().length() > 0) {
            holder.tv_preparation_time.setText(myFavouriteDatas.get(position).getPreparation_time());
            holder.iv_preparation_time.setVisibility(View.VISIBLE);
            holder.tv_preparation_time.setVisibility(View.VISIBLE);
        }
        if (myFavouriteDatas.get(position).getFermentation_time() != null && myFavouriteDatas.get(position).getFermentation_time().length() > 0) {
            holder.tv_fermantation_time.setText(myFavouriteDatas.get(position).getFermentation_time());
            holder.iv_fermantation_time.setVisibility(View.VISIBLE);
            holder.tv_fermantation_time.setVisibility(View.VISIBLE);
        }
        if (myFavouriteDatas.get(position).getSoaking_time() != null && myFavouriteDatas.get(position).getSoaking_time().length() > 0) {
            holder.tv_soaking_time.setText(myFavouriteDatas.get(position).getSoaking_time());
            holder.iv_soaking_time.setVisibility(View.VISIBLE);
            holder.tv_soaking_time.setVisibility(View.VISIBLE);
        }

        if (myFavouriteDatas.get(position).isFavourite()) {
            Drawable mDrawable = context.getResources().getDrawable(R.drawable.ic_select);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY));
            holder.iv_fav.setImageDrawable(mDrawable);
        } else {
            Drawable mDrawable = context.getResources().getDrawable(R.drawable.ic_unse);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY));
            holder.iv_fav.setImageDrawable(mDrawable);

        }
        holder.iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFav(position);
            }
        });


        Picasso.with(context).load(AppConfig.IMAGE_URL + myFavouriteDatas.get(position).getRecipe_image()).into(holder.iv_recipe_item);

        holder.iv_recipe_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = myFavouriteDatas.get(position).getToken();
                Log.e("---------fav---------", "token:: " + token);
                Intent intent = new Intent(context, CategoryItemViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mSingleRecipeToken", myFavouriteDatas.get(position).getToken());
                context.startActivity(intent);
            }
        });


    }

    private void addFav(final int position) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConfig.BASE_API_URL).build();
        CallWebServices callWebServices = restAdapter.create(CallWebServices.class);
//        user_id get from global`
        GlobalClass global = new GlobalClass(context);
        String user_id = GlobalClass.getPrefrenceString(context, "user_id", "");
        String mSingleRecipeToken = myFavouriteDatas.get(position).getToken();
        Log.e("-fav---recipe token----", "CategoryItemViewActivity--- " + mSingleRecipeToken);
        callWebServices.AddToFavouriteRecipe(mSingleRecipeToken, user_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response2.getBody().in()));
                    String stringResponse = bufferedReader.readLine();
                    JSONObject mainResponseObject = new JSONObject(stringResponse.toString());
                    myFavouriteDatas.get(position).setFavourite(false);
                    myFavouriteDatas.remove(position);
                    if (myFavouriteDatas.size() <= 0) {
                        myFavoriteFragment.imageView2.setVisibility(View.VISIBLE);
                        myFavoriteFragment.btn_get_new_recipe.setVisibility(View.VISIBLE);
                        myFavoriteFragment.tv_noFav.setVisibility(View.VISIBLE);
                        myFavoriteFragment.tv_found.setVisibility(View.VISIBLE);
                    }else {
                        myFavoriteFragment.imageView2.setVisibility(View.GONE);
                        myFavoriteFragment.btn_get_new_recipe.setVisibility(View.GONE);
                        myFavoriteFragment.tv_noFav.setVisibility(View.GONE);
                        myFavoriteFragment.tv_found.setVisibility(View.GONE);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                notifyDataSetChanged();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("RetrofitError", " ---- :  " + error);
                Toast.makeText(context, "Please Try After Some Time!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != myFavouriteDatas ? myFavouriteDatas.size() : 0);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_fav_cat_name, tv_fav_item_name, tv_cook_time, tv_fermantation_time, tv_preparation_time, tv_soaking_time;
        public ImageView iv_recipe_item, iv_cook_time, iv_fermantation_time, iv_preparation_time, iv_soaking_time, iv_fav;

        public ViewHolder(View view) {
            super(view);
            tv_fav_cat_name = (TextView) view.findViewById(R.id.tv_fav_cat_name);
            tv_preparation_time = (TextView) view.findViewById(R.id.tv_preparation_time);
            tv_soaking_time = (TextView) view.findViewById(R.id.tv_soaking_time);
            tv_cook_time = (TextView) view.findViewById(R.id.tv_cook_time);
            tv_fav_item_name = (TextView) view.findViewById(R.id.tv_fav_item_name);
            tv_fermantation_time = (TextView) view.findViewById(R.id.tv_fermantation_time);
            iv_recipe_item = (ImageView) view.findViewById(R.id.iv_recipe_item);
            iv_cook_time = (ImageView) view.findViewById(R.id.iv_cook_time);
            iv_fermantation_time = (ImageView) view.findViewById(R.id.iv_fermantation_time);
            iv_preparation_time = (ImageView) view.findViewById(R.id.iv_preparation_time);
            iv_soaking_time = (ImageView) view.findViewById(R.id.iv_soaking_time);
            iv_fav = (ImageView) view.findViewById(R.id.iv_fav);

            Drawable mDrawable = context.getResources().getDrawable(R.drawable.ic_unse);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY));
            iv_fav.setImageDrawable(mDrawable);
        }
    }

}
