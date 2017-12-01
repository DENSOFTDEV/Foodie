package com.iblinfotech.foodierecipe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

@JsonObject
public class RecipeSearchedIngredientsData implements Serializable {

    @JsonField(name = "image_id")
    String image_id;
    @JsonField(name = "image_url")
    String image_url;
    @JsonField(name = "ingredient_name")
    String ingredient_name;
    @JsonField(name = "about_ingredient")
    String about_ingredient;
    @JsonField(name = "grocery_id")
    String grocery_id;
    @JsonField(name = "is_active")
    String is_active;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getAbout_ingredient() {
        return about_ingredient;
    }

    public void setAbout_ingredient(String about_ingredient) {
        this.about_ingredient = about_ingredient;
    }

    public String getGrocery_id() {
        return grocery_id;
    }

    public void setGrocery_id(String grocery_id) {
        this.grocery_id = grocery_id;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}
