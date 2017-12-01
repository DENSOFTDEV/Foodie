package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.RecipeSearchedIngredientsData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchedIngredientAdapter extends BaseAdapter {
    private Context context;
    private List<RecipeSearchedIngredientsData> ingredientdata;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;

    public SearchedIngredientAdapter(Context context, List<RecipeSearchedIngredientsData> ingredientdata) {
        this.context = context;
        this.ingredientdata = ingredientdata;
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (context != null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

    @Override
    public int getCount() {
        return ingredientdata.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredientdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        convertView = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_searched_ingredient_layout, null);
            holder = new ViewHolder();
            holder.iv_searched_ingred_image = (ImageView) convertView.findViewById(R.id.iv_searched_ingred_image);
            holder.tv_searched_ingred_name = (TextView) convertView.findViewById(R.id.tv_searched_ingred_name);
            holder.ll_mainImage = (LinearLayout) convertView.findViewById(R.id.ll_mainImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Typeface RobotoLight = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");

        String searched_ingred_image = AppConfig.IMAGE_URL + ingredientdata.get(position).getImage_url();
        Picasso.with(context).load(searched_ingred_image).into(holder.iv_searched_ingred_image);

        holder.tv_searched_ingred_name.setText(ingredientdata.get(position).getIngredient_name());
        holder.tv_searched_ingred_name.setTypeface(RobotoLight);
        holder.ll_mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        public ImageView iv_searched_ingred_image;
        public LinearLayout ll_mainImage;
        public TextView tv_searched_ingred_name;

    }
}
