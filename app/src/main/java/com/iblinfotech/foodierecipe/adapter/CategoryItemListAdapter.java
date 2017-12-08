package com.iblinfotech.foodierecipe.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.Activity.CategoryItemViewActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.RecipeItemTitleData;
import com.iblinfotech.foodierecipe.subscriptionmenu.PackageActivity;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemListAdapter extends BaseAdapter {

    private Context context;
    private List<RecipeItemTitleData> recipeItemTitleDatas = new ArrayList<>();
    private static LayoutInflater inflater = null;
    private ViewHolder holder;
//    SharedPreferences sp;

    public CategoryItemListAdapter(Context context, List<RecipeItemTitleData> recipeItemTitleDatas) {
        this.context = context;
        this.recipeItemTitleDatas = recipeItemTitleDatas;
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (context != null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
//        sp = context.getSharedPreferences("FoodieAPP", Activity.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return recipeItemTitleDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeItemTitleDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.row_category_item_view, null);
            holder = new ViewHolder();
            holder.iv_recipe_item = (ImageView) convertView.findViewById(R.id.iv_recipe_item);
//            holder.iv_user_image=(ImageView)convertView.findViewById(R.id.iv_user_image);
            holder.tv_recipe_name = (TextView) convertView.findViewById(R.id.tv_recipe_name);
            holder.rl_main = (RelativeLayout) convertView.findViewById(R.id.rl_main);
            holder.iv_veg_non = (ImageView) convertView.findViewById(R.id.iv_veg_non);
            holder.iv_veg_non = (ImageView) convertView.findViewById(R.id.iv_veg_non);
            holder.iv_doller = (ImageView) convertView.findViewById(R.id.iv_doller);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "PlayfairDisplay-Regular.otf");
        holder.tv_recipe_name.setTypeface(fontRegular);

//        String userImage = AppConfig.IMAGE_URL + recipeItemTitleDatas.get(position).getUser_image();
        String recipyImage = AppConfig.IMAGE_URL + recipeItemTitleDatas.get(position).getRecipe_image();
        String recipeName = recipeItemTitleDatas.get(position).getRecipe_name();
        String recipeType = recipeItemTitleDatas.get(position).getType();
        Picasso.with(context).load(recipyImage).into(holder.iv_recipe_item);
        holder.tv_recipe_name.setText(recipeName);
        holder.rl_main.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth * 0.5));
        holder.iv_recipe_item.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth * 0.29));

        final String is_premium = recipeItemTitleDatas.get(position).getIs_premium();

//        final int is_premium_purchased = sp.getInt("InApp-Purchase", 0);

        final int is_premium_purchased = GlobalClass.getPrefrenceInt(context, "addpremiumrecipe", 3);

        if (is_premium.equals("1")) {
            if (is_premium_purchased == 1) {
                holder.iv_doller.setVisibility(View.GONE);
            } else {
                holder.iv_doller.setVisibility(View.VISIBLE);
            }
        } else {
            holder.iv_doller.setVisibility(View.GONE);
        }

        if (recipeType.equalsIgnoreCase("vegetarian")) {
            holder.iv_veg_non.setImageResource(R.drawable.ic_veg);
        } else {
            holder.iv_veg_non.setImageResource(R.drawable.ic_non_veg);
        }
        holder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_premium.equals("1")) {
                    if (is_premium_purchased == 1) {
                        String token = recipeItemTitleDatas.get(position).getToken();
                        Log.e("------------------", "token:: " + token);
                        Intent intent = new Intent(context, CategoryItemViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("mSingleRecipeToken", recipeItemTitleDatas.get(position).getToken());
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, PackageActivity.class);
                        context.startActivity(intent);
                    }
                } else {
                    String token = recipeItemTitleDatas.get(position).getToken();
                    Log.e("------------------", "token:: " + token);
                    Intent intent = new Intent(context, CategoryItemViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("mSingleRecipeToken", recipeItemTitleDatas.get(position).getToken());
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        public ImageView iv_recipe_item, iv_user_image, iv_veg_non, iv_doller;
        public TextView tv_recipe_name;
        public RelativeLayout rl_main;
    }
}
