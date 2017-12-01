package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iblinfotech.foodierecipe.MainActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.SqLiteHelper.FoodieLocalData;
import com.iblinfotech.foodierecipe.fragments.MyShoppingList;
import com.iblinfotech.foodierecipe.model.RecipeIngredientsData;
import com.iblinfotech.foodierecipe.model.RecipeIngredientsDataNew;
import com.iblinfotech.foodierecipe.model.ShoppingData;
import com.iblinfotech.foodierecipe.utils.AppConfig;
import com.iblinfotech.foodierecipe.utils.GlobalClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ShoppingData> shoppingDataArrayList;
    private ArrayList<RecipeIngredientsData> searchCategoryGroupList;
    private ArrayList<RecipeIngredientsDataNew> chList;

    private RecipeIngredientsDataNew recipeIngredientsDatanew;
    private FoodieLocalData dbHelper;
    private String selection;

    public CategoryExpandableAdapter(Context context, ArrayList<ShoppingData> shoppingDataArrayList) {
        this.context = context;
        this.shoppingDataArrayList = shoppingDataArrayList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        chList = shoppingDataArrayList.get(groupPosition).getSubCategoryItemList();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        recipeIngredientsDatanew = (RecipeIngredientsDataNew) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        String expandedTextName = recipeIngredientsDatanew.getName();
        String expandedTextAmmount = recipeIngredientsDatanew.getAmount();
        String expandedChildImage = AppConfig.IMAGE_URL + recipeIngredientsDatanew.getImage();

        TextView tv_expandedListItem_name = (TextView) convertView.findViewById(R.id.tv_expandedListItem_name);
        TextView tv_expandedListItem_mesure = (TextView) convertView.findViewById(R.id.tv_expandedListItem_mesure);
        ImageView iv_expandedListItem = (ImageView) convertView.findViewById(R.id.iv_expandedListItem);

        tv_expandedListItem_name.setText(expandedTextName);
        tv_expandedListItem_mesure.setText(expandedTextAmmount);

        Typeface RobotoRegular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");

        tv_expandedListItem_name.setTypeface(RobotoRegular);
        tv_expandedListItem_mesure.setTypeface(RobotoRegular);

        Picasso.with(context).load(expandedChildImage).into(iv_expandedListItem);
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<RecipeIngredientsDataNew> chList = shoppingDataArrayList.get(groupPosition).getSubCategoryItemList();
//        Log.e("----ADAPTER----------","------"+shoppingDataArrayList.get(groupPosition).getRecipeName());
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return shoppingDataArrayList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return shoppingDataArrayList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        ImageView iv_listTitleImage = (ImageView) convertView.findViewById(R.id.iv_listTitleImage);
        ImageView iv_minusListItem = (ImageView) convertView.findViewById(R.id.iv_minusListItem);
        TextView listPerson = (TextView) convertView.findViewById(R.id.listPerson);
        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "PlayfairDisplay-Regular.otf");
        Typeface RobotoRegular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");

        final String listTitle = shoppingDataArrayList.get(groupPosition).getRecipeName();
        final String listPersonNo = shoppingDataArrayList.get(groupPosition).getServingsPerson();

        listTitleTextView.setTypeface(fontRegular);
        listPerson.setTypeface(RobotoRegular);
        listPerson.setText("PERSON " + listPersonNo);
        Log.e("----listTitle----------", "------" + listTitle);
        String listTitleImage = AppConfig.IMAGE_URL + shoppingDataArrayList.get(groupPosition).getRecipeImage();
        listTitleTextView.setText(listTitle);
        Picasso.with(context).load(listTitleImage).into(iv_listTitleImage);
        iv_minusListItem.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                askToDelete(groupPosition, listTitle);
            }
        });
        notifyDataSetChanged();
        return convertView;
    }

    private void askToDelete(final int groupPosition, final String listTitle) {
        new AlertDialog.Builder(context)
                .setMessage(R.string.are_u_sure_want_to_delete)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GlobalClass.printLog("-----------", "in loop......" + groupPosition);
                        dbHelper = new FoodieLocalData(context);
                        Cursor cursor = dbHelper.getShoppingCartList();
                        if (cursor != null && cursor.getCount() >= 0) {
                            cursor.moveToFirst();
                            while (!cursor.isAfterLast()) {
                                String recipeData = cursor.getString(cursor.getColumnIndex(FoodieLocalData.KEY_RECIPE_DATA));
                                GlobalClass.printLog("---A--------", "recipeData......" + recipeData);
                                if (recipeData.contains(listTitle)) {
                                    selection = FoodieLocalData.KEY_RECIPE_DATA + " LIKE ?";
                                    String[] selectionArgs = {recipeData};
                                    shoppingDataArrayList.remove(groupPosition);
                                    dbHelper.delete(FoodieLocalData.SHOPPING_CART_MASTER, selection, selectionArgs);
                                    cursor = dbHelper.getShoppingCartList();
                                    notifyDataSetChanged();
                                    Log.e("Cursor Counter", "--------------in if------------------------------------" + cursor.getCount());
                                    if (cursor.getCount() == 0) {
                                        MyShoppingList.tv_empty_shoppingList.setVisibility(View.VISIBLE);
                                        MyShoppingList.imageView2.setVisibility(View.VISIBLE);
                                        MyShoppingList.btn_get_new_recipe.setVisibility(View.VISIBLE);
                                    }
                                }
                                cursor.moveToNext();
                            }
                        } else {
                            MyShoppingList.tv_empty_shoppingList.setVisibility(View.VISIBLE);
                            MyShoppingList.imageView2.setVisibility(View.VISIBLE);
                            MyShoppingList.btn_get_new_recipe.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
