package com.iblinfotech.foodierecipe.model;


import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

@JsonObject
public class SearchResultData implements Serializable {

    @JsonField(name = "recipe")
    ArrayList<RecipeItemTitleData> recipe;
    @JsonField(name = "ingredients")
    ArrayList<RecipeSearchedIngredientsData> ingredients;

    public ArrayList<RecipeSearchedIngredientsData> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeSearchedIngredientsData> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<RecipeItemTitleData> getRecipe() {
        return recipe;
    }

    public void setRecipe(ArrayList<RecipeItemTitleData> recipe) {
        this.recipe = recipe;
    }

    public String getResultCount() {
        return ResultCount;
    }

    public void setResultCount(String resultCount) {
        ResultCount = resultCount;
    }

    @JsonField(name = "ResultCount")
    String ResultCount;

}
