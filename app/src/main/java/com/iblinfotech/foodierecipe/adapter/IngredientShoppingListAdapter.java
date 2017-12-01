package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.RecipeIngredientsData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IngredientShoppingListAdapter extends RecyclerView.Adapter<IngredientShoppingListAdapter.ViewHolder> {

    public static ArrayList<RecipeIngredientsData> ingredientsShoppingItemDatas = new ArrayList<>();

    private Context context;
    int integer = 0;

    public IngredientShoppingListAdapter(Context context, ArrayList<RecipeIngredientsData> ingredientsShoppingItemDatas) {
        this.context = context;
        this.ingredientsShoppingItemDatas = ingredientsShoppingItemDatas;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shoppinglist_item, parent, false);

//        if ( viewType % 2 == 0)
//            itemView.setBackgroundResource(R.drawable.listview_selector_even);
//        else
//            itemView.setBackgroundResource(R.drawable.listview_selector_odd);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_itemname.setText(ingredientsShoppingItemDatas.get(position).getName());
        holder.tv_cart_quantity.setText(ingredientsShoppingItemDatas.get(position).getAmount());
//        holder.tv_mesure.setText(ingredientsShoppingItemDatas.get(position).getMeasure());
        Picasso.with(context).load(AppConfig.IMAGE_URL + ingredientsShoppingItemDatas.get(position).getImage()).into(holder.iv_cart_item);
    }

    @Override
    public int getItemCount() {
        return (null != ingredientsShoppingItemDatas ? ingredientsShoppingItemDatas.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_itemname, tv_cart_quantity;
        public ImageView iv_cart_item;

        public ViewHolder(View view) {
            super(view);
            tv_itemname = (TextView) view.findViewById(R.id.tv_itemname);
            tv_cart_quantity = (TextView) view.findViewById(R.id.tv_cart_quantity);
//            tv_mesure = (TextView) view.findViewById(R.id.tv_mesure);
            iv_cart_item = (ImageView) view.findViewById(R.id.iv_cart_item);
        }
    }


}