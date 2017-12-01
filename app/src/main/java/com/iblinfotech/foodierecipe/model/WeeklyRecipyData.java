package com.iblinfotech.foodierecipe.model;


import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

@JsonObject
public class WeeklyRecipyData implements Serializable {

    @JsonField(name = "token")
    String token;
    @JsonField(name = "recipe_name")
    String recipe_name;
    @JsonField(name = "recipe_image")
    String recipe_image;
    @JsonField(name = "preparation_time")
    String preparation_time;
    @JsonField(name = "cooking_time")
    String cooking_time;
    @JsonField(name = "soaking_time")
    String soaking_time;
    @JsonField(name = "fermentation_time")
    String fermentation_time;
    @JsonField(name = "weekly_day")
    String weekly_day;

    public String getType() {
        return recipe_type;
    }

    public void setType(String recipe_type) {
        this.recipe_type = recipe_type;
    }

    @JsonField(name = "recipe_type")
    String recipe_type;

    public String getWeekly_day() {
        return weekly_day;
    }

    public void setWeekly_day(String weekly_day) {
        this.weekly_day = weekly_day;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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


}
