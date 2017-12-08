package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.Activity.CategoryItemViewActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.RecipeItemTitleData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SearchedItemAdapter extends RecyclerView.Adapter<SearchedItemAdapter.MyViewHolder> {

    ArrayList<RecipeItemTitleData> recipeItemTitleDatas;
    Context context;

    public SearchedItemAdapter(Context context, ArrayList<RecipeItemTitleData> recipeItemTitleDatas) {
        this.recipeItemTitleDatas = recipeItemTitleDatas;
        this.context = context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_recipe_name;
        public RelativeLayout rl_main;
        public ImageView iv_user_image, iv_recipe_item;

        public MyViewHolder(View view) {
            super(view);
            iv_recipe_item = (ImageView) view.findViewById(R.id.iv_recipe_item);
//            iv_user_image=(ImageView)view.findViewById(R.id.iv_user_image);
            tv_recipe_name = (TextView) view.findViewById(R.id.tv_recipe_name);
            rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_searched_item_view, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_recipe_name.setText(recipeItemTitleDatas.get(position).getRecipe_name());

        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "PlayfairDisplay-Regular.otf");
        holder.tv_recipe_name.setTypeface(fontRegular);

        String userImage = AppConfig.IMAGE_URL + recipeItemTitleDatas.get(position).getUser_image();
        String recipyImage = AppConfig.IMAGE_URL + recipeItemTitleDatas.get(position).getRecipe_image();
        String recipeName = recipeItemTitleDatas.get(position).getRecipe_name();

//        Picasso.with(context).load(userImage).into(holder.iv_user_image);
        Picasso.with(context).load(recipyImage).into(holder.iv_recipe_item);
        holder.tv_recipe_name.setText(recipeName);

        holder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = recipeItemTitleDatas.get(position).getToken();
                Log.e("------------------", "token:: " + token);
                Intent intent = new Intent(context, CategoryItemViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mSingleRecipeToken", recipeItemTitleDatas.get(position).getToken());
                context.startActivity(intent);
            }
        });


        holder.rl_main.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth * 0.7));
        holder.rl_main.getLayoutParams().width = ((int) (GlobalClass.DeviceWidth * 0.5));
    }

    @Override
    public int getItemCount() {
        return recipeItemTitleDatas.size();
    }
}
