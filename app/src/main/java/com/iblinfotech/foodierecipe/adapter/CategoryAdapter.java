package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.Activity.CategoryItemListActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.RecipeCatagoryData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    public List<RecipeCatagoryData> recipeCatagoryDatas;
    private static LayoutInflater inflater = null;
    private Drawable drawable;
    private Target loadtarget;
    private ViewHolder holder;
    private String token, recipyTitle;
    private String recipyTotal;

    public CategoryAdapter(Context context, List<RecipeCatagoryData> recipeCatagoryDatas, boolean isClickable) {
        this.context = context;
        this.recipeCatagoryDatas = recipeCatagoryDatas;
        if (context != null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

    @Override
    public int getCount() {
        return recipeCatagoryDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeCatagoryDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_recipe_catagory, null);
            holder = new ViewHolder();
            holder.img_recipe_item = (ImageView) convertView.findViewById(R.id.img_recipe_item);
            holder.rl_mainImage = (RelativeLayout) convertView.findViewById(R.id.rl_mainImage);
            holder.iv_recipe_item = (ImageView) convertView.findViewById(R.id.iv_recipe_item);
            holder.tv_recipe_name = (TextView) convertView.findViewById(R.id.tv_recipe_name);
            holder.tv_recipe_count = (TextView) convertView.findViewById(R.id.tv_recipe_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.rl_mainImage.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth * 0.5));

        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "PlayfairDisplay-Regular.otf");
        Typeface RobotoLight = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        String imgItem = AppConfig.IMAGE_URL + recipeCatagoryDatas.get(position).getIcon_image();
        String imgbackground = AppConfig.IMAGE_URL + recipeCatagoryDatas.get(position).getBackground_image();
        recipyTitle = recipeCatagoryDatas.get(position).getCategory();
        recipyTotal = recipeCatagoryDatas.get(position).getTotal() + " RECIPES";
        Picasso.with(context).load(imgbackground).into(holder.iv_recipe_item);
        Picasso.with(context).load(imgItem).into(holder.img_recipe_item);
        holder.tv_recipe_name.setText(recipyTitle);
        holder.tv_recipe_count.setText(recipyTotal);
        holder.tv_recipe_name.setTypeface(fontRegular);
        holder.tv_recipe_count.setTypeface(RobotoLight);
        holder.rl_mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = recipeCatagoryDatas.get(position).getCategory();
                String Total = recipeCatagoryDatas.get(position).getTotal() + " RECIPES";
                String titleimg = AppConfig.IMAGE_URL + recipeCatagoryDatas.get(position).getIcon_image();
                String colorCode = recipeCatagoryDatas.get(position).getColor_code();
                String backgroundimg = AppConfig.IMAGE_URL + recipeCatagoryDatas.get(position).getBackground_image();

                token = recipeCatagoryDatas.get(position).getToken();
                GlobalClass global = new GlobalClass(context);
                GlobalClass.setPrefrenceString(context, "mCategoryToken", token);
                GlobalClass.setPrefrenceString(context, "mBackgroundImage", backgroundimg);
                GlobalClass.setPrefrenceString(context, "mcolorCode", colorCode);
                Log.e("---------", "my token----------" + token);
                Intent intent = new Intent(context, CategoryItemListActivity.class);
                intent.putExtra("mRecipyTitle", title);
                intent.putExtra("mRecipyTotal", Total);
                intent.putExtra("mIconImage", titleimg);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        public ImageView img_recipe_item, iv_recipe_item;
        public RelativeLayout rl_mainImage;
        public TextView tv_recipe_name;
        public TextView tv_recipe_count;
    }
}
