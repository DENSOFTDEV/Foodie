package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.CategoryItemViewActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.WeeklyRecipyData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeeklyRecipeAdapter extends RecyclerView.Adapter<WeeklyRecipeAdapter.MyViewHolder> {

    List<WeeklyRecipyData> weeklyRecipyDatas;
    Context context;
    private String todayDay;
    private int selectedPos = 0;
    String weekDay;
    public WeeklyRecipeAdapter(Context context, List<WeeklyRecipyData> weeklyRecipyDatas) {
        this.weeklyRecipyDatas = weeklyRecipyDatas;
        this.context = context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_soaking_time, iv_weekly_itemimage, iv_cook_time,iv_veg_non, iv_fermantation_time, iv_preparation_time;
        private LinearLayout ll_soaking_time, ll_weekly_item, ll_cook_time, ll_fermantation_time, ll_preparation_time;
        private TextView tv_soaking_time, tv_preparation_time, tv_weekly_day, tv_weekly_itemname, tv_cook_time, tv_fermantation_time;
        private CardView weekly_card_view;

        public MyViewHolder(View view) {
            super(view);

            ll_weekly_item = (LinearLayout) view.findViewById(R.id.ll_weekly_item);
            ll_cook_time = (LinearLayout) view.findViewById(R.id.ll_cook_time);
            ll_preparation_time = (LinearLayout) view.findViewById(R.id.ll_preparation_time);
            ll_soaking_time = (LinearLayout) view.findViewById(R.id.ll_soaking_time);
            ll_fermantation_time = (LinearLayout) view.findViewById(R.id.ll_fermantation_time);

            iv_weekly_itemimage = (ImageView) view.findViewById(R.id.iv_weekly_itemimage);
            iv_fermantation_time = (ImageView) view.findViewById(R.id.iv_fermantation_time);
            iv_cook_time = (ImageView) view.findViewById(R.id.iv_cook_time);
            iv_veg_non = (ImageView) view.findViewById(R.id.iv_veg_non);
            iv_preparation_time = (ImageView) view.findViewById(R.id.iv_preparation_time);
            iv_soaking_time = (ImageView) view.findViewById(R.id.iv_soaking_time);

            tv_weekly_day = (TextView) view.findViewById(R.id.tv_weekly_day);
            tv_fermantation_time = (TextView) view.findViewById(R.id.tv_fermantation_time);
            tv_cook_time = (TextView) view.findViewById(R.id.tv_cook_time);
            tv_weekly_itemname = (TextView) view.findViewById(R.id.tv_weekly_itemname);
            tv_soaking_time = (TextView) view.findViewById(R.id.tv_soaking_time);
            tv_preparation_time = (TextView) view.findViewById(R.id.tv_preparation_time);
            weekly_card_view = (CardView) view.findViewById(R.id.weekly_card_view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_weekly_item_view, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)  {
        holder.itemView.setSelected(selectedPos == position);
        holder.tv_weekly_itemname.setText(weeklyRecipyDatas.get(position).getRecipe_name());

        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "PlayfairDisplay-Regular.otf");
        Typeface RobotoLight = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");

        holder.tv_weekly_itemname.setTypeface(fontRegular);
        holder.tv_weekly_day.setTypeface(RobotoLight);
        holder.tv_cook_time.setTypeface(RobotoLight);
        holder.tv_fermantation_time.setTypeface(RobotoLight);
        holder.tv_preparation_time.setTypeface(RobotoLight);
        holder.tv_soaking_time.setTypeface(RobotoLight);

        String recipyImage = AppConfig.IMAGE_URL + weeklyRecipyDatas.get(position).getRecipe_image();
        String recipeName = weeklyRecipyDatas.get(position).getRecipe_name();
        Picasso.with(context).load(recipyImage).into(holder.iv_weekly_itemimage);
        holder.tv_weekly_itemname.setText(recipeName);
//        holder.tv_weekly_itemname.getLayoutParams().width = ((int) (GlobalClass.DeviceWidth / 1.2));
        String cooking_time = weeklyRecipyDatas.get(position).getCooking_time();
        String fermentation_time = weeklyRecipyDatas.get(position).getFermentation_time();
        String soaking_time = weeklyRecipyDatas.get(position).getSoaking_time();
        String preparation_time = weeklyRecipyDatas.get(position).getPreparation_time();
        String weekly_day = weeklyRecipyDatas.get(position).getWeekly_day();
        String recipe_type = weeklyRecipyDatas.get(position).getType();

        Log.e("------type-----","---------"+recipe_type);
        if (recipe_type.equalsIgnoreCase("vegetarian")){
            Log.e("----------type-","---------if-----"+recipe_type);

            holder.iv_veg_non.setImageResource(R.drawable.ic_veg);
        }else {
            Log.e("----------type--","-------else---"+recipe_type);

            holder.iv_veg_non.setImageResource(R.drawable.ic_non_veg);
        }


        holder.tv_weekly_day.setText(weekly_day);

        todayDay = holder.tv_weekly_day.getText().toString().trim();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());

        if (cooking_time.length() > 0) {
            holder.ll_cook_time.setVisibility(View.VISIBLE);
            holder.iv_cook_time.setVisibility(View.VISIBLE);
            holder.tv_cook_time.setVisibility(View.VISIBLE);
            holder.tv_cook_time.setText(cooking_time + "'");
        }
        if (fermentation_time.length() > 0) {
            holder.ll_fermantation_time.setVisibility(View.VISIBLE);
            holder.iv_fermantation_time.setVisibility(View.VISIBLE);
            holder.tv_fermantation_time.setVisibility(View.VISIBLE);
            holder.tv_fermantation_time.setText(fermentation_time + "'");
        }
        if (soaking_time.length() > 0) {
            holder.ll_soaking_time.setVisibility(View.VISIBLE);
            holder.tv_soaking_time.setVisibility(View.VISIBLE);
            holder.iv_soaking_time.setVisibility(View.VISIBLE);
            holder.tv_soaking_time.setText(soaking_time + "'");
        }
        if (preparation_time.length() > 0) {
            holder.ll_preparation_time.setVisibility(View.VISIBLE);
            holder.iv_preparation_time.setVisibility(View.VISIBLE);
            holder.tv_preparation_time.setVisibility(View.VISIBLE);
            holder.tv_preparation_time.setText(preparation_time + "'");
        }
        holder.ll_weekly_item.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth));
        holder.ll_weekly_item.getLayoutParams().width = ((int) (GlobalClass.DeviceWidth * 0.7));

//        Log.e("------------", "CARD HEIGHT:---" + ((int) (GlobalClass.DeviceWidth * 5)));
        holder.ll_weekly_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = weeklyRecipyDatas.get(position).getToken();
                Log.e("------------------", "token:: " + token);
                Intent intent = new Intent(context, CategoryItemViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mSingleRecipeToken", weeklyRecipyDatas.get(position).getToken());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weeklyRecipyDatas.size();
    }
}
