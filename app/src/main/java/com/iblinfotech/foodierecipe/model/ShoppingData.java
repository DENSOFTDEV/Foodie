package com.iblinfotech.foodierecipe.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingData implements Serializable {

    String recipeName;
    String recipeImage;
    String servingsPerson;

    public String getRecipeToken() {
        return recipeToken;
    }

    public void setRecipeToken(String recipeToken) {
        this.recipeToken = recipeToken;
    }

    String recipeToken;


    public String getServingsPerson() {
        return servingsPerson;
    }

    public void setServingsPerson(String servingsPerson) {
        this.servingsPerson = servingsPerson;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    private ArrayList<RecipeIngredientsDataNew> subCategoryItemList;

    public ArrayList<RecipeIngredientsDataNew> getSubCategoryItemList() {
        return subCategoryItemList;
    }

    public void setSubCategoryItemList(ArrayList<RecipeIngredientsDataNew> subCategoryItemList) {
        this.subCategoryItemList = subCategoryItemList;
    }


}
