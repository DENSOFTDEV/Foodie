package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.Activity.CategoryItemViewActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.DiscoverModeData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SwipeDeckAdapter extends BaseAdapter {
    private List<DiscoverModeData> discoverModeDatas;
    private Context context;
    private static LayoutInflater inflater = null;
    private ViewHolder holder;

    public SwipeDeckAdapter(List<DiscoverModeData> discoverModeDatas, Context context) {
        this.discoverModeDatas = discoverModeDatas;
        this.context = context;
        if (context != null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

    @Override
    public int getCount() {
        return discoverModeDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return discoverModeDatas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {

            view = LayoutInflater.from(context).inflate(R.layout.row_swipecard_item_view, null);
            holder = new ViewHolder();
            holder.iv_dis_itemimage = (ImageView) view.findViewById(R.id.iv_dis_itemimage);
            holder.iv_cook_time = (ImageView) view.findViewById(R.id.iv_cook_time);
            holder.iv_fermantation_time = (ImageView) view.findViewById(R.id.iv_fermantation_time);
            holder.iv_preparation_time = (ImageView) view.findViewById(R.id.iv_preparation_time);
            holder.iv_soaking_time = (ImageView) view.findViewById(R.id.iv_soaking_time);
            holder.iv_veg_non = (ImageView) view.findViewById(R.id.iv_veg_non);

            holder.tv_dis_itemname = (TextView) view.findViewById(R.id.tv_dis_itemname);
            holder.tv_cook_time = (TextView) view.findViewById(R.id.tv_cook_time);
            holder.tv_fermantation_time = (TextView) view.findViewById(R.id.tv_fermantation_time);
            holder.tv_preparation_time = (TextView) view.findViewById(R.id.tv_preparation_time);
            holder.tv_soaking_time = (TextView) view.findViewById(R.id.tv_soaking_time);

            holder.ll_discover_item = (LinearLayout) view.findViewById(R.id.ll_discover_item);
            holder.ll_cook_time = (LinearLayout) view.findViewById(R.id.ll_cook_time);
            holder.ll_fermantation_time = (LinearLayout) view.findViewById(R.id.ll_fermantation_time);
            holder.ll_preparation_time = (LinearLayout) view.findViewById(R.id.ll_preparation_time);
            holder.ll_soaking_time = (LinearLayout) view.findViewById(R.id.ll_soaking_time);
            holder.iv_dis_itemimage.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth * 0.6));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "PlayfairDisplay-Regular.otf");
        Typeface RobotoRegular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");

        holder.tv_dis_itemname.setTypeface(fontRegular);
        holder.tv_dis_itemname.getLayoutParams().width = ((int) (GlobalClass.DeviceWidth / 1.8));

        holder.tv_cook_time.setTypeface(RobotoRegular);
        holder.tv_fermantation_time.setTypeface(RobotoRegular);
        holder.tv_preparation_time.setTypeface(RobotoRegular);
        holder.tv_soaking_time.setTypeface(RobotoRegular);

        String recipyImage = AppConfig.IMAGE_URL + discoverModeDatas.get(position).getRecipe_image();
        String recipeName = discoverModeDatas.get(position).getRecipe_name();
        String cooking_time = discoverModeDatas.get(position).getCooking_time();
        String fermentation_time = discoverModeDatas.get(position).getFermentation_time();
        String soaking_time = discoverModeDatas.get(position).getSoaking_time();
        String preparation_time = discoverModeDatas.get(position).getPreparation_time();
        String type = discoverModeDatas.get(position).getType();

        if (type.equalsIgnoreCase("vegetarian")){
            holder.iv_veg_non.setImageResource(R.drawable.ic_veg);
        }else {
            holder.iv_veg_non.setImageResource(R.drawable.ic_non_veg);
        }

        if (cooking_time.length() > 0) {
            holder.ll_cook_time.setVisibility(View.VISIBLE);
            holder.iv_cook_time.setVisibility(View.VISIBLE);
            holder.tv_cook_time.setVisibility(View.VISIBLE);
            holder.tv_cook_time.setText(cooking_time+"'");
        }
        if (fermentation_time.length() > 0) {
            holder.ll_fermantation_time.setVisibility(View.VISIBLE);
            holder.iv_fermantation_time.setVisibility(View.VISIBLE);
            holder.tv_fermantation_time.setVisibility(View.VISIBLE);
            holder.tv_fermantation_time.setText(fermentation_time+"'");
        }
        if (soaking_time.length() > 0) {
            holder.ll_soaking_time.setVisibility(View.VISIBLE);
            holder.tv_soaking_time.setVisibility(View.VISIBLE);
            holder.iv_soaking_time.setVisibility(View.VISIBLE);
            holder.tv_soaking_time.setText(soaking_time+"'");
        }
        if (preparation_time.length() > 0) {
            holder.ll_preparation_time.setVisibility(View.VISIBLE);
            holder.iv_preparation_time.setVisibility(View.VISIBLE);
            holder.tv_preparation_time.setVisibility(View.VISIBLE);
            holder.tv_preparation_time.setText(preparation_time+"'");
        }
        Picasso.with(context).load(recipyImage).into(holder.iv_dis_itemimage);
        holder.tv_dis_itemname.setText(recipeName);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryItemViewActivity.class);
                Log.e("----------", "token: " + discoverModeDatas.get(position).getToken());
                intent.putExtra("mSingleRecipeToken", discoverModeDatas.get(position).getToken());
                context.startActivity(intent);
            }
        });
        return view;
    }

    private static class ViewHolder {
        ImageView iv_dis_itemimage, iv_cook_time, iv_fermantation_time, iv_preparation_time, iv_soaking_time,iv_veg_non;
        TextView tv_dis_itemname, tv_cook_time, tv_fermantation_time, tv_preparation_time, tv_soaking_time;
        LinearLayout ll_cook_time, ll_fermantation_time, ll_preparation_time, ll_soaking_time, ll_discover_item;
    }
}
