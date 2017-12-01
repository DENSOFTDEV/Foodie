package com.iblinfotech.foodierecipe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

@JsonObject
public class RecipeHowMuchData implements Serializable {

    @JsonField(name = "servings")
    String servings;
    @JsonField(name = "ingredients")
    ArrayList<RecipeIngredientsData> ingredients;

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public ArrayList<RecipeIngredientsData> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredientsData> ingredients) {
        this.ingredients = ingredients;
    }
}
