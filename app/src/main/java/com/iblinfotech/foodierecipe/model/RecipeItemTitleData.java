package com.iblinfotech.foodierecipe.model;


import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

@JsonObject
public class RecipeItemTitleData implements Serializable {

    @JsonField(name = "token")
    String token;

    @JsonField(name = "recipe_name")
    String recipe_name;

    @JsonField(name = "recipe_image")
    String recipe_image;

    @JsonField(name = "username")
    String username;

    @JsonField(name = "user_image")
    String user_image;

    @JsonField(name = "is_favourite")
    String is_favourite;

    @JsonField(name = "preparation_time")
    String preparation_time;

    @JsonField(name = "cooking_time")
    String cooking_time;

    @JsonField(name = "soaking_time")
    String soaking_time;

    public String getIs_premium() {
        return is_premium;
    }

    public void setIs_premium(String is_premium) {
        this.is_premium = is_premium;
    }

    @JsonField(name = "is_premium")
    String is_premium;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonField(name = "type")
    String type;

    public String getFermentation_time() {
        return fermentation_time;
    }

    public void setFermentation_time(String fermentation_time) {
        this.fermentation_time = fermentation_time;
    }

    public String getSoaking_time() {
        return soaking_time;
    }

    public void setSoaking_time(String soaking_time) {
        this.soaking_time = soaking_time;
    }

    public String getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }

    public String getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(String preparation_time) {
        this.preparation_time = preparation_time;
    }

    @JsonField(name = "fermentation_time")
    String fermentation_time;


    public String getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(String is_favourite) {
        this.is_favourite = is_favourite;
    }


    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRecipe_image() {
        return recipe_image;
    }

    public void setRecipe_image(String recipe_image) {
        this.recipe_image = recipe_image;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
