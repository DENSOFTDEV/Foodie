package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ornolfr.ratingview.RatingView;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.RecipeReviewData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeReviewAdapter extends RecyclerView.Adapter<RecipeReviewAdapter.MyViewHolder> {

    ArrayList<RecipeReviewData> recipeReviewDatas;
    Context context;

    public RecipeReviewAdapter(Context context, ArrayList<RecipeReviewData> recipeReviewDatas) {
        this.recipeReviewDatas = recipeReviewDatas;
        this.context = context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RatingView ratingView_review;
        public TextView tv_user_Name, tv_user_review;
        public LinearLayout rl_main;
        public ImageView iv_user_image;

        public MyViewHolder(View view) {
            super(view);
            iv_user_image = (ImageView) view.findViewById(R.id.iv_user_image);
            tv_user_Name = (TextView) view.findViewById(R.id.tv_user_Name);
            tv_user_review = (TextView) view.findViewById(R.id.tv_user_review);
            rl_main = (LinearLayout) view.findViewById(R.id.rl_main);
            ratingView_review = (RatingView) view.findViewById(R.id.ratingView_review);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_user_Name.setText(recipeReviewDatas.get(position).getUsername());
        holder.tv_user_review.setText(recipeReviewDatas.get(position).getReview());

        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "PlayfairDisplay-Regular.otf");
        holder.tv_user_Name.setTypeface(fontRegular);
//        holder.tv_user_review.setTypeface(fontRegular);

        String userImage = AppConfig.IMAGE_URL + recipeReviewDatas.get(position).getUser_image();

        Picasso.with(context).load(userImage).placeholder(R.drawable.default_profile).into(holder.iv_user_image);
        if (recipeReviewDatas.get(position).getTotal_star() != null) {
            Float totalStars = Float.valueOf(String.valueOf(recipeReviewDatas.get(position).getTotal_star()));
            holder.ratingView_review.setRating(totalStars);
        }


    }


    @Override
    public int getItemCount() {
        return recipeReviewDatas.size();
    }
}
