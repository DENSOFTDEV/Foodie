package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.model.RecipeIngredientsData;
import com.iblinfotech.foodierecipe.model.ShoppingData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<Object> expandableListTitle;
    private HashMap<Object, ArrayList<RecipeIngredientsData>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, ArrayList<Object> expandableListTitle, HashMap<Object, ArrayList<RecipeIngredientsData>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }


    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final RecipeIngredientsData recipeIngredientsData = (RecipeIngredientsData) getChild(listPosition, expandedListPosition);
        String expandedTextName = recipeIngredientsData.getName();
        String expandedTextAmmount = recipeIngredientsData.getAmount();
        String expandedChildImage = AppConfig.IMAGE_URL + recipeIngredientsData.getImage();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView tv_expandedListItem_name = (TextView) convertView.findViewById(R.id.tv_expandedListItem_name);
        TextView tv_expandedListItem_mesure = (TextView) convertView.findViewById(R.id.tv_expandedListItem_mesure);
        ImageView iv_expandedListItem = (ImageView) convertView.findViewById(R.id.iv_expandedListItem);
        tv_expandedListItem_name.setText(expandedTextName);
        tv_expandedListItem_mesure.setText(expandedTextAmmount);
        Picasso.with(context).load(expandedChildImage).into(iv_expandedListItem);
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        Log.e("expandableListTitle---","#######"+expandableListTitle.size());
        Log.e("----------Detail","#######"+expandableListDetail.size());
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        ImageView iv_listTitleImage = (ImageView) convertView.findViewById(R.id.iv_listTitleImage);
        final ArrayList<ShoppingData> shoppingData = (ArrayList<ShoppingData>) getGroup(listPosition);

        for (int i = 0; i < shoppingData.size(); i++) {
            String listTitle = shoppingData.get(i).getRecipeName();
            String listTitleImage = AppConfig.IMAGE_URL + shoppingData.get(i).getRecipeImage();
            listTitleTextView.setTypeface(null, Typeface.NORMAL);
            listTitleTextView.setText(listTitle);
            Picasso.with(context).load(listTitleImage).into(iv_listTitleImage);
        }

        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}