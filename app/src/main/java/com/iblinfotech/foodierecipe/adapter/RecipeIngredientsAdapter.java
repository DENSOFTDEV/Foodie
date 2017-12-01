package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.RecipeIngredientsData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipeIngredientsAdapter extends BaseAdapter {
    ArrayList<RecipeIngredientsData> recipeIngredientsDatas;
    Context context;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;


    public RecipeIngredientsAdapter(Context context, ArrayList<RecipeIngredientsData> recipeIngredientsDatas) {
        this.context = context;
        this.recipeIngredientsDatas = recipeIngredientsDatas;
        if (context != null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return recipeIngredientsDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return recipeIngredientsDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.row_category_ingredient_layout, null);
            holder = new ViewHolder();
            holder.iv_ingred_image = (ImageView) view.findViewById(R.id.iv_ingred_image);
            holder.tv_ingred_name = (TextView) view.findViewById(R.id.tv_ingred_name);
            holder.tv_ingred_ammount = (TextView) view.findViewById(R.id.tv_ingred_ammount);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String ingredientImage = AppConfig.IMAGE_URL + recipeIngredientsDatas.get(i).getImage();
        Picasso.with(context).load(ingredientImage).into(holder.iv_ingred_image);
        holder.tv_ingred_name.setText(recipeIngredientsDatas.get(i).getName());
        holder.tv_ingred_ammount.setText(recipeIngredientsDatas.get(i).getAmount());
//        holder.tv_ingred_ammount.setText(recipeIngredientsDatas.get(i).getAmount() + " " + recipeIngredientsDatas.get(i).getMeasure());

        Typeface fontRobotoLight = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        holder.tv_ingred_name.setTypeface(fontRobotoLight);
        holder.tv_ingred_ammount.setTypeface(fontRobotoLight);
        return view;
    }

    private static class ViewHolder {
        public ImageView iv_ingred_image;
        public TextView tv_ingred_name, tv_ingred_ammount;

    }
}
