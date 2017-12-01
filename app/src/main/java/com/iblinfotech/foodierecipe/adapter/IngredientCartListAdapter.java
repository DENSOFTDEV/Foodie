package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.IngredientsShoppingItemData;
import com.iblinfotech.foodierecipe.model.RecipeIngredientsData;
import com.iblinfotech.foodierecipe.model.ShoppingData;
import com.iblinfotech.foodierecipe.model.SingleRecipeDetailData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IngredientCartListAdapter extends RecyclerView.Adapter<IngredientCartListAdapter.ViewHolder> {

    public static ArrayList<RecipeIngredientsData> recipeIngredientsDatas = new ArrayList<>();
    public static ArrayList<IngredientsShoppingItemData> ingredientsShoppingItemDatas = new ArrayList<>();
    private static int person;
    private static ArrayList<ShoppingData> shoppingItemDatas;
    public static boolean isAlreadyInList;
    private Context context;
    public static SingleRecipeDetailData singleRecipeDetailData;

    public IngredientCartListAdapter(Context context, SingleRecipeDetailData singleRecipeDetailData, int person) {
        this.context = context;
        this.person = person;
        this.singleRecipeDetailData = singleRecipeDetailData;
        this.recipeIngredientsDatas = singleRecipeDetailData.getHow_much().get(person).getIngredients();
        Log.e("---", "-----person in adapter-------" + person);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_item, parent, false);
        return new ViewHolder(itemView);
    }

    public static ArrayList<RecipeIngredientsData> getChildDetail() {
        return recipeIngredientsDatas;
    }

    public static ArrayList<ShoppingData> getRecipeTitle() {
        shoppingItemDatas = new ArrayList<>();
        ShoppingData shoppingItemData = new ShoppingData();
        shoppingItemData.setRecipeName(singleRecipeDetailData.getRecipe_name());
        shoppingItemData.setRecipeImage(singleRecipeDetailData.getRecipe_image().get(0));
        Log.e("-AAAA--", "--Kirti---getRecipe_name()-------" + singleRecipeDetailData.getRecipe_name());
//        Log.e("-AAAA--", "--Ankita---shoppingItemData-------" + singleRecipeDetailData.getRecipe_image().size());
        shoppingItemDatas.add(shoppingItemData);
        return shoppingItemDatas;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_itemname.setText(recipeIngredientsDatas.get(position).getName());
//        holder.tv_mesure.setText(recipeIngredientsDatas.get(position).getMeasure());
        holder.tv_cart_quantity.setText(recipeIngredientsDatas.get(position).getAmount());
        Picasso.with(context).load(AppConfig.IMAGE_URL + recipeIngredientsDatas.get(position).getImage()).into(holder.iv_cart_item);

    }

    @Override
    public int getItemCount() {
        return (null != recipeIngredientsDatas ? recipeIngredientsDatas.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_itemname, tv_cart_quantity, tv_mesure;
        public ImageView iv_cart_item;

        public ViewHolder(View view) {
            super(view);
            tv_itemname = (TextView) view.findViewById(R.id.tv_itemname);
            tv_cart_quantity = (TextView) view.findViewById(R.id.tv_cart_quantity);
//            tv_mesure = (TextView) view.findViewById(R.id.tv_mesure);
            iv_cart_item = (ImageView) view.findViewById(R.id.iv_cart_item);
        }


        public boolean addDataInList() {
            isAlreadyInList = false;
            for (int i = 0; i < shoppingItemDatas.size(); i++) {
                if (shoppingItemDatas.get(i).getRecipeName().equalsIgnoreCase(tv_itemname.getText().toString())) {
                    isAlreadyInList=true;
                }
            }
            return isAlreadyInList;
        }
    }


}
