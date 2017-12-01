package com.iblinfotech.foodierecipe.model;


import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

@JsonObject
public class MyFavouriteData implements Serializable {

    @JsonField(name = "ResultCount")
    int ResultCount;
    @JsonField(name = "token")
    String token;
    @JsonField(name = "recipe_name")
    String recipe_name;
    @JsonField(name = "recipe_image")
    String recipe_image;
    @JsonField(name = "category_name")
    String category_name;
    @JsonField(name = "category_background")
    String category_background;
    @JsonField(name = "category_color_code")
    String category_color_code;
    @JsonField(name = "preparation_time")
    String preparation_time;
    @JsonField(name = "cooking_time")
    String cooking_time;
    @JsonField(name = "soaking_time")
    String soaking_time;
    @JsonField(name = "fermentation_time")
    String fermentation_time;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    boolean isFavourite = false;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_background() {
        return category_background;
    }

    public void setCategory_background(String category_background) {
        this.category_background = category_background;
    }

    public String getCategory_color_code() {
        return category_color_code;
    }

    public void setCategory_color_code(String category_color_code) {
        this.category_color_code = category_color_code;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(String preparation_time) {
        this.preparation_time = preparation_time;
    }

    public String getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }

    public String getSoaking_time() {
        return soaking_time;
    }

    public void setSoaking_time(String soaking_time) {
        this.soaking_time = soaking_time;
    }

    public String getFermentation_time() {
        return fermentation_time;
    }

    public void setFermentation_time(String fermentation_time) {
        this.fermentation_time = fermentation_time;
    }


    public int getResultCount() {
        return ResultCount;
    }

    public void setResultCount(int resultCount) {
        ResultCount = resultCount;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_image() {
        return recipe_image;
    }

    public void setRecipe_image(String recipe_image) {
        this.recipe_image = recipe_image;
    }

}
