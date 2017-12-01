package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.RecipeMethodData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeStepsAdapter extends PagerAdapter {
    private ArrayList<RecipeMethodData> recipeMethodDatas;
    private LayoutInflater inflater;
    private Context context;

    public RecipeStepsAdapter(Context context, ArrayList<RecipeMethodData> recipeMethodDatas) {
        this.context = context;
        this.recipeMethodDatas = recipeMethodDatas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return recipeMethodDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingsteps_layout, view, false);
        assert imageLayout != null;
        Typeface RobotoLight = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");


        final LinearLayout ll_text = (LinearLayout) imageLayout.findViewById(R.id.ll_text);
        final ImageView iv_steps_images = (ImageView) imageLayout.findViewById(R.id.iv_steps_images);
        ProgressBar progressbar = (ProgressBar) imageLayout.findViewById(R.id.progressbar);
        final TextView tv_steps_text = (TextView) imageLayout.findViewById(R.id.tv_steps_text);
        if (recipeMethodDatas.get(position).getImage().toString().trim().length() > 0) {
            iv_steps_images.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.VISIBLE);
            String imageUral = AppConfig.IMAGE_URL + recipeMethodDatas.get(position).getImage();
            Picasso.with(context).load(imageUral).into(iv_steps_images);
        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            ll_text.setLayoutParams(params);
            iv_steps_images.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
        }
        iv_steps_images.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth));
        progressbar.getLayoutParams().height = ((int) (GlobalClass.DeviceWidth));
        tv_steps_text.setTypeface(RobotoLight);
        tv_steps_text.setText(recipeMethodDatas.get(position).getStep());
//        tv_steps_text.setText("This is testing item description i need helpppppp taaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
